package io.github.annamandzulashvili.mobile.app.steps.business;

import io.github.annamandzulashvili.mobile.app.steps.app.HomeSteps;
import io.github.annamandzulashvili.mobile.app.steps.device.DeviceSteps;
import io.qameta.allure.Step;
import java.time.Duration;

/**
 * Brings a fresh Proverbial session to a usable home screen. iOS asks for notification permission on
 * first launch; Android 12 does not. Both cases are handled here so tests start from the same state.
 */
public class AppLaunchSteps {

    private static final Duration PERMISSION_WAIT = Duration.ofSeconds(5);

    private final HomeSteps home;
    private final DeviceSteps device;

    public AppLaunchSteps(HomeSteps home, DeviceSteps device) {
        this.home = home;
        this.device = device;
    }

    @Step("Launch Proverbial and accept the permission pop-up")
    public boolean launch() {
        device.allowPermissionIfAsked(PERMISSION_WAIT);
        return home.isShown();
    }

    @Step("Launch Proverbial and deny the permission pop-up")
    public boolean launchDenyingPermission() {
        boolean asked = device.denyPermissionIfAsked(PERMISSION_WAIT);
        return asked && home.isShown();
    }
}
