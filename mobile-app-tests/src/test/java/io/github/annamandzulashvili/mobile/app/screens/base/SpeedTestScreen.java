package io.github.annamandzulashvili.mobile.app.screens.base;

import org.openqa.selenium.By;

/** Opened by the Speed Test button. */
public abstract class SpeedTestScreen extends ExternalContentScreen {

    protected SpeedTestScreen(By container) {
        super(container, "Speed test screen");
    }
}
