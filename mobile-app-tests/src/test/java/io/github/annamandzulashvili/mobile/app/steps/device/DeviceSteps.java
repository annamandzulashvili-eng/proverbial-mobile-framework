package io.github.annamandzulashvili.mobile.app.steps.device;

import io.github.annamandzulashvili.mobile.core.lifecycle.AppLifecycle;
import io.github.annamandzulashvili.mobile.core.system.SystemDialogs;
import io.qameta.allure.Step;
import java.time.Duration;
import org.openqa.selenium.ScreenOrientation;

/** Device-level actions that do not belong to any screen. */
public class DeviceSteps {

    @Step("Send the app to the background for {seconds}s")
    public void background(int seconds) {
        AppLifecycle.sendToBackground(Duration.ofSeconds(seconds));
        AppLifecycle.waitForForeground();
    }

    @Step("Rotate to landscape")
    public void rotateToLandscape() {
        AppLifecycle.rotate(ScreenOrientation.LANDSCAPE);
    }

    @Step("Rotate to portrait")
    public void rotateToPortrait() {
        AppLifecycle.rotate(ScreenOrientation.PORTRAIT);
    }

    public boolean isLandscape() {
        return AppLifecycle.orientation() == ScreenOrientation.LANDSCAPE;
    }

    @Step("Allow the permission pop-up if it appears")
    public boolean allowPermissionIfAsked(Duration wait) {
        return SystemDialogs.allowIfShown(wait);
    }

    @Step("Deny the permission pop-up if it appears")
    public boolean denyPermissionIfAsked(Duration wait) {
        return SystemDialogs.denyIfShown(wait);
    }
}
