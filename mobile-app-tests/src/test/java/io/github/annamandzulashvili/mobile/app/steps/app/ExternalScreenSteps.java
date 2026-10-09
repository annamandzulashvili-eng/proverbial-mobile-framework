package io.github.annamandzulashvili.mobile.app.steps.app;

import io.github.annamandzulashvili.mobile.app.screens.base.ExternalContentScreen;
import io.github.annamandzulashvili.mobile.app.screens.base.GeolocationScreen;
import io.github.annamandzulashvili.mobile.app.screens.base.SpeedTestScreen;
import io.github.annamandzulashvili.mobile.core.screens.ScreenFactory;
import io.qameta.allure.Step;

/** Leaves the geolocation and speed test screens the platform's way. */
public class ExternalScreenSteps {

    @Step("Go back from the geolocation screen")
    public void leaveGeolocation() {
        screen(GeolocationScreen.class).goBack();
    }

    @Step("Go back from the speed test screen")
    public void leaveSpeedTest() {
        screen(SpeedTestScreen.class).goBack();
    }

    private static ExternalContentScreen screen(Class<? extends ExternalContentScreen> type) {
        return ScreenFactory.get(type);
    }
}
