package io.github.annamandzulashvili.mobile.app.steps.app;

import io.github.annamandzulashvili.mobile.app.enums.HomeButton;
import io.github.annamandzulashvili.mobile.app.screens.base.HomeScreen;
import io.github.annamandzulashvili.mobile.app.screens.base.ToastMessage;
import io.github.annamandzulashvili.mobile.core.config.RunContext;
import io.github.annamandzulashvili.mobile.core.screens.ScreenFactory;
import io.github.annamandzulashvili.mobile.core.waits.PollUntil;
import io.qameta.allure.Step;
import java.time.Duration;
import java.util.Arrays;

/** Actions and queries on the Proverbial home screen. Steps return facts; tests decide pass or fail. */
public class HomeSteps {

    private HomeScreen home() {
        return ScreenFactory.get(HomeScreen.class);
    }

    @Step("Home screen is shown")
    public boolean isShown() {
        return home().isDisplayedWithin(RunContext.settings().timeouts().condition());
    }

    @Step("Tap {button}")
    public void tap(HomeButton button) {
        home().tap(button);
    }

    @Step("Are all home buttons visible")
    public boolean allButtonsVisible() {
        return Arrays.stream(HomeButton.values()).allMatch(button -> home().isButtonVisible(button, Duration.ofSeconds(5)));
    }

    @Step("Tap Text and check whether the welcome message changed")
    public boolean tapTextChangesWelcomeMessage() {
        if (!home().isWelcomeMessageVisible(Duration.ofSeconds(10))) {
            return false;
        }
        home().tap(HomeButton.TEXT);
        return !home().isWelcomeMessageVisible(Duration.ofSeconds(3));
    }

    @Step("Tap Color and check whether the welcome message looks different")
    public boolean tapColorChangesWelcomeMessage() {
        byte[] before = home().welcomeMessageImage();
        home().tap(HomeButton.COLOR);
        try {
            PollUntil.condition(() -> !Arrays.equals(before, home().welcomeMessageImage()),
                    Duration.ofSeconds(5), Duration.ofMillis(300), "the welcome message colour to change");
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    @Step("Tap Toast and wait for the message")
    public boolean tapToastShowsMessage() {
        home().tap(HomeButton.TOAST);
        return ScreenFactory.get(ToastMessage.class).appears(Duration.ofSeconds(5));
    }

    @Step("Wait until home is replaced by another screen")
    public void waitUntilLeft() {
        home().waitUntilLeft();
    }
}
