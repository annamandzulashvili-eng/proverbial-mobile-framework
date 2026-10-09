package io.github.annamandzulashvili.mobile.app.steps.business;

import io.github.annamandzulashvili.mobile.app.enums.HomeButton;
import io.github.annamandzulashvili.mobile.app.steps.app.ExternalScreenSteps;
import io.github.annamandzulashvili.mobile.app.steps.app.HomeSteps;
import io.github.annamandzulashvili.mobile.app.steps.device.DeviceSteps;
import io.qameta.allure.Step;
import java.time.Duration;

/** Opens a feature screen from home and comes back: the same journey for Geolocation and Speed Test. */
public class OpenAndReturnSteps {

    private final HomeSteps home;
    private final ExternalScreenSteps external;
    private final DeviceSteps device;

    public OpenAndReturnSteps(HomeSteps home, ExternalScreenSteps external, DeviceSteps device) {
        this.home = home;
        this.external = external;
        this.device = device;
    }

    @Step("Open {button} and come back to home")
    public boolean openAndReturn(HomeButton button) {
        home.tap(button);
        // Geolocation asks for location permission the first time; accept it so the screen can open.
        device.allowPermissionIfAsked(Duration.ofSeconds(3));
        home.waitUntilLeft();
        switch (button) {
            case GEOLOCATION -> external.leaveGeolocation();
            case SPEED_TEST -> external.leaveSpeedTest();
            default -> throw new IllegalArgumentException(button + " does not open a separate screen");
        }
        return home.isShown();
    }
}
