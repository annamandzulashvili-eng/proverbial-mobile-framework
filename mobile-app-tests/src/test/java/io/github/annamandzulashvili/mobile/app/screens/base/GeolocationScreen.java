package io.github.annamandzulashvili.mobile.app.screens.base;

import org.openqa.selenium.By;

/** Opened by the Geolocation button. */
public abstract class GeolocationScreen extends ExternalContentScreen {

    protected GeolocationScreen(By container) {
        super(container, "Geolocation screen");
    }
}
