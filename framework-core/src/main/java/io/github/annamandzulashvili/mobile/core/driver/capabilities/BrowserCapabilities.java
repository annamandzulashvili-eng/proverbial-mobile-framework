package io.github.annamandzulashvili.mobile.core.driver.capabilities;

import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.github.annamandzulashvili.mobile.core.config.ConfigException;
import io.github.annamandzulashvili.mobile.core.config.DeviceProfile;
import io.github.annamandzulashvili.mobile.core.config.TimeoutsSettings;
import io.github.annamandzulashvili.mobile.core.config.WebSettings;
import io.github.annamandzulashvili.mobile.core.enums.Platform;
import java.time.Duration;
import org.openqa.selenium.MutableCapabilities;

/** Mobile browser capabilities: Chrome on Android, Safari on iOS (from webSettings.json). */
public final class BrowserCapabilities {

    private BrowserCapabilities() {
    }

    public static MutableCapabilities build(Platform platform, WebSettings web, DeviceProfile device, TimeoutsSettings timeouts) {
        if (web == null || web.browsers() == null || !web.browsers().containsKey(platform.id())) {
            throw new ConfigException("webSettings.json has no browser for " + platform.id());
        }
        String browser = web.browsers().get(platform.id());
        Duration newCommandTimeout = Duration.ofSeconds(timeouts.newCommandTimeoutSeconds());
        MutableCapabilities caps = platform == Platform.ANDROID
                ? new UiAutomator2Options().setNewCommandTimeout(newCommandTimeout)
                : new XCUITestOptions().setNewCommandTimeout(newCommandTimeout);
        caps.setCapability("browserName", browser);
        return AppCapabilities.withDevice(caps, device);
    }
}
