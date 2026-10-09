package io.github.annamandzulashvili.mobile.app.screens.android.wdio;

import io.github.annamandzulashvili.mobile.core.gestures.Direction;
import io.github.annamandzulashvili.mobile.core.gestures.Gestures;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import io.github.annamandzulashvili.mobile.core.enums.Platform;
import io.github.annamandzulashvili.mobile.core.screens.BaseScreen;
import io.github.annamandzulashvili.mobile.core.screens.ScreenFor;
import io.github.annamandzulashvili.mobile.core.selectors.Selectors;

/** Horizontal card carousel and a hidden logo at the bottom ("You found me!!!"). */
@ScreenFor(Platform.ANDROID)
public class SwipeScreen extends BaseScreen {

    private static final By CAROUSEL = Selectors.accessibilityId("card");
    private static final By LOGO = Selectors.accessibilityId("WebdriverIO logo");

    public SwipeScreen() {
        super(Selectors.accessibilityId("Swipe-screen"), "WDIO swipe screen");
    }

    public String visibleCardText() {
        return find(CAROUSEL).getText();
    }

    public void swipeCarousel(Direction direction) {
        WebElement carousel = find(CAROUSEL);
        Gestures.swipe(carousel, direction, 0.8);
    }

    public boolean scrollToHiddenLogo(int maxSwipes) {
        return Gestures.scrollUntilVisible(LOGO, Direction.UP, maxSwipes).isDisplayed();
    }
}
