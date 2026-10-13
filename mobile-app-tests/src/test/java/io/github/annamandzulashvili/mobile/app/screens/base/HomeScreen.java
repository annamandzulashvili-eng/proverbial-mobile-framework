package io.github.annamandzulashvili.mobile.app.screens.base;

import io.github.annamandzulashvili.mobile.app.enums.HomeButton;
import io.github.annamandzulashvili.mobile.core.screens.BaseScreen;
import io.github.annamandzulashvili.mobile.core.selectors.Selectors;
import io.github.annamandzulashvili.mobile.core.waits.Waits;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;

/**
 * Proverbial home screen: welcome message and six buttons. Behaviour is shared; each platform
 * implementation only says how to find a button.
 */
public abstract class HomeScreen extends BaseScreen {

    /** Start of the welcome message, identical on both platforms. */
    public static final String WELCOME_TEXT = "Welcome to lambdatest";

    protected HomeScreen(By container) {
        super(container, "Home screen");
    }

    protected abstract By button(HomeButton button);

    public void tap(HomeButton button) {
        tap(button(button));
    }

    public boolean isButtonVisible(HomeButton button, Duration timeout) {
        return isVisible(button(button), timeout);
    }

    /** Waits until the home buttons are gone, which proves another screen was opened. */
    public void waitUntilLeft() {
        Waits.gone(button(HomeButton.COLOR));
    }

    public boolean isWelcomeMessageVisible(Duration timeout) {
        return isVisible(welcomeMessage(), timeout);
    }

    /** Pixels of the welcome message, used to prove that the Color button changed its colour. */
    public byte[] welcomeMessageImage() {
        return find(welcomeMessage()).getScreenshotAs(OutputType.BYTES);
    }

    protected By welcomeMessage() {
        return Selectors.textContains(platform(), WELCOME_TEXT);
    }
}
