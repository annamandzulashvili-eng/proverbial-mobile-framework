package io.github.annamandzulashvili.mobile.core.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.annamandzulashvili.mobile.core.config.AppProfile;
import io.github.annamandzulashvili.mobile.core.config.DeviceProfile;
import io.github.annamandzulashvili.mobile.core.config.Settings;
import io.github.annamandzulashvili.mobile.core.driver.Target;
import io.github.annamandzulashvili.mobile.core.driver.capabilities.AppCapabilities;
import io.github.annamandzulashvili.mobile.core.driver.capabilities.BrowserCapabilities;
import io.github.annamandzulashvili.mobile.core.driver.capabilities.BrowserStackOptions;
import io.github.annamandzulashvili.mobile.core.enums.DeviceType;
import io.github.annamandzulashvili.mobile.core.enums.Module;
import io.github.annamandzulashvili.mobile.core.enums.Platform;
import java.nio.file.Path;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.openqa.selenium.MutableCapabilities;

class CapabilitiesTest {

    @TempDir
    Path tmp;

    private final AppProfile android = new AppProfile("android", "ProverbialAndroid", "", "com.lambdatest.proverbial", "", "");
    private final AppProfile ios = new AppProfile("ios", "ProverbialIOS", "", "", "", "");

    @Test
    void androidAppCapabilities() {
        Settings settings = TestSettings.withCredentials(tmp);

        MutableCapabilities caps = AppCapabilities.build(Platform.ANDROID, android, null, "ProverbialAndroid", settings.timeouts());

        assertThat(caps.getCapability("platformName").toString()).isEqualToIgnoringCase("android");
        assertThat(caps.getCapability("appium:automationName").toString()).isEqualToIgnoringCase("UiAutomator2");
        assertThat(caps.getCapability("appium:app")).isEqualTo("ProverbialAndroid");
        assertThat(caps.getCapability("appium:appPackage")).isEqualTo("com.lambdatest.proverbial");
        assertThat(caps.getCapability("appium:autoGrantPermissions")).isEqualTo(false);
        assertThat(caps.getCapability("appium:deviceName")).as("cloud devices go through bstack:options").isNull();
    }

    @Test
    void iosNeverAutoAcceptsAlerts() {
        Settings settings = TestSettings.withCredentials(tmp);

        MutableCapabilities caps = AppCapabilities.build(Platform.IOS, ios, null, "ProverbialIOS", settings.timeouts());

        assertThat(caps.getCapability("appium:automationName").toString()).isEqualToIgnoringCase("XCUITest");
        assertThat(caps.getCapability("appium:autoAcceptAlerts")).isEqualTo(false);
        assertThat(caps.getCapability("appium:bundleId")).as("unknown bundle ids are not guessed").isNull();
    }

    @Test
    void localRunsPutTheDeviceIntoAppiumCapabilities() {
        Settings settings = TestSettings.withCredentials(tmp);
        DeviceProfile emulator = new DeviceProfile("android", "phone", "emulator-5554", "14");

        MutableCapabilities caps = AppCapabilities.build(Platform.ANDROID, android, emulator, "apps/app.apk", settings.timeouts());

        assertThat(caps.getCapability("appium:deviceName")).isEqualTo("emulator-5554");
        assertThat(caps.getCapability("appium:platformVersion")).isEqualTo("14");
    }

    @Test
    void missingAppFailsFast() {
        Settings settings = TestSettings.withCredentials(tmp);

        assertThatThrownBy(() -> AppCapabilities.build(Platform.ANDROID, android, null, " ", settings.timeouts()))
                .hasMessageContaining("No app to install");
    }

    @Test
    void browserCapabilitiesPickTheBrowserPerPlatform() {
        Settings settings = TestSettings.withCredentials(tmp);
        io.github.annamandzulashvili.mobile.core.config.WebSettings web =
                new io.github.annamandzulashvili.mobile.core.config.WebSettings("https://example.com", Map.of("android", "chrome", "ios", "safari"));

        assertThat(BrowserCapabilities.build(Platform.ANDROID, web, null, settings.timeouts()).getBrowserName()).isEqualTo("chrome");
        assertThat(BrowserCapabilities.build(Platform.IOS, web, null, settings.timeouts()).getBrowserName()).isEqualTo("safari");
    }

    @Test
    void browserStackOptions() {
        Settings settings = TestSettings.withCredentials(tmp);
        Target target = new Target(Module.APP, Platform.ANDROID, DeviceType.PHONE, "proverbial", "[C1001] App opens", "41.7151,44.8271");
        DeviceProfile device = settings.devices().get("androidPhone");

        Map<String, Object> options = BrowserStackOptions.build(settings, target, device);

        assertThat(options)
                .containsEntry("userName", "demo_user")
                .containsEntry("deviceName", device.deviceName())
                .containsEntry("osVersion", device.platformVersion())
                .containsEntry("sessionName", "[C1001] App opens")
                .containsEntry("buildName", "smoke app for android from local #local")
                .containsEntry("gpsLocation", "41.7151,44.8271")
                .doesNotContainKey("appiumVersion");
    }
}
