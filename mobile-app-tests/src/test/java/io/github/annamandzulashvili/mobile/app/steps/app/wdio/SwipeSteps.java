package io.github.annamandzulashvili.mobile.app.steps.app.wdio;

import io.github.annamandzulashvili.mobile.app.screens.android.wdio.SwipeScreen;
import io.github.annamandzulashvili.mobile.core.gestures.Direction;
import io.qameta.allure.Step;

public class SwipeSteps {

    private final SwipeScreen swipe = new SwipeScreen();

    /** Card text before and after one swipe to the left, "before|after". */
    @Step("Swipe the carousel left")
    public String swipeCarouselLeft() {
        swipe.waitUntilDisplayed();
        String before = swipe.visibleCardText();
        swipe.swipeCarousel(Direction.LEFT);
        return before + "|" + swipe.visibleCardText();
    }

    @Step("Scroll down to the hidden logo")
    public boolean findHiddenLogo() {
        swipe.waitUntilDisplayed();
        return swipe.scrollToHiddenLogo(6);
    }
}
