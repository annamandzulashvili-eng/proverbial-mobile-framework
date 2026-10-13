package io.github.annamandzulashvili.mobile.app.screens.base;

import io.github.annamandzulashvili.mobile.core.screens.BaseScreen;
import org.openqa.selenium.By;

/**
 * A screen Proverbial opens on top of home (geolocation map, speed test). Its content is third-party,
 * so the framework only needs to know it is shown and how to leave it. Leaving differs per platform:
 * Android uses the hardware Back key, iOS the navigation bar's back button.
 */
public abstract class ExternalContentScreen extends BaseScreen {

    protected ExternalContentScreen(By container, String name) {
        super(container, name);
    }

    public abstract void goBack();
}
