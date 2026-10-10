package io.github.annamandzulashvili.mobile.core.driver.capabilities;

import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.github.annamandzulashvili.mobile.core.config.AppProfile;
import io.github.annamandzulashvili.mobile.core.config.ConfigException;
import io.github.annamandzulashvili.mobile.core.config.DeviceProfile;
import io.github.annamandzulashvili.mobile.core.config.TimeoutsSettings;
import io.github.annamandzulashvili.mobile.core.enums.Platform;
import java.time.Duration;
import org.openqa.selenium.MutableCapabilities;

/** Native app capabilities. Pure function of its inputs, so it is unit-tested without a device. */
public final class AppCapabilities {

    private AppCapabilities() {
    }

    /**
     * @param appRef BrowserStack {@code bs://} id or custom_id for cloud runs, a file path for local runs
     * @param device used only for local runs; BrowserStack gets the device through bstack:options
     */
    public static MutableCapabilities build(Platform platform, AppProfile app, DeviceProfile device, String appRef, TimeoutsSettings timeouts) {
        if (appRef == null || appRef.isBlank()) {
            throw new ConfigException("No app to install for " + platform.id() + ". Set browserStackApp (cloud) or localAppPath (local)");
        }
        Duration newCommandTimeout = Duration.ofSeconds(timeouts.newCommandTimeoutSeconds());
        if (platform == Platform.ANDROID) {
            UiAutomator2Options options = new UiAutomator2Options()
                    .setApp(appRef)
                    .setNewCommandTimeout(newCommandTimeout)
                    .setAutoGrantPermissions(false);
            if (notBlank(app.appPackage())) {
                options.setAppPackage(app.appPackage());
            }
            if (notBlank(app.appActivity())) {
                options.setAppActivity(app.appActivity());
            }
            return withDevice(options, device);
        }
        XCUITestOptions options = new XCUITestOptions()
                .setApp(appRef)
                .setNewCommandTimeout(newCommandTimeout)
                // Permission pop-ups are part of what we test, so never auto-accept them.
                .setAutoAcceptAlerts(false);
        if (notBlank(app.bundleId())) {
            options.setBundleId(app.bundleId());
        }
        return withDevice(options, device);
    }

    static <T extends MutableCapabilities> T withDevice(T caps, DeviceProfile device) {
        if (device != null && notBlank(device.deviceName())) {
            caps.setCapability("appium:deviceName", device.deviceName());
        }
        if (device != null && notBlank(device.platformVersion())) {
            caps.setCapability("appium:platformVersion", device.platformVersion());
        }
        return caps;
    }

    static boolean notBlank(String value) {
        return value != null && !value.isBlank();
    }
}
