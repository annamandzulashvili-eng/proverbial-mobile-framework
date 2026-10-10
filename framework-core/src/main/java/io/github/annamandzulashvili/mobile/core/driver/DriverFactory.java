package io.github.annamandzulashvili.mobile.core.driver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.github.annamandzulashvili.mobile.core.browserstack.AppProvider;
import io.github.annamandzulashvili.mobile.core.config.AppProfile;
import io.github.annamandzulashvili.mobile.core.config.ConfigException;
import io.github.annamandzulashvili.mobile.core.config.DeviceProfile;
import io.github.annamandzulashvili.mobile.core.config.Settings;
import io.github.annamandzulashvili.mobile.core.driver.capabilities.AppCapabilities;
import io.github.annamandzulashvili.mobile.core.driver.capabilities.BrowserCapabilities;
import io.github.annamandzulashvili.mobile.core.driver.capabilities.BrowserStackOptions;
import io.github.annamandzulashvili.mobile.core.driver.capabilities.DeviceCatalog;
import io.github.annamandzulashvili.mobile.core.enums.Module;
import io.github.annamandzulashvili.mobile.core.enums.Platform;
import io.github.annamandzulashvili.mobile.core.enums.RunOn;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import org.openqa.selenium.MutableCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Creates one Appium session for one {@link Target}. Capability building itself is pure and unit-tested. */
public final class DriverFactory {

    private static final Logger LOG = LoggerFactory.getLogger(DriverFactory.class);

    private DriverFactory() {
    }

    public static TestSession create(Settings settings, Target target) {
        RunOn runOn = settings.runOn();
        DeviceProfile device = DeviceCatalog.resolve(settings, target.platform(), target.deviceType(), runOn);
        MutableCapabilities caps;
        String appId = "";
        if (target.module() == Module.APP) {
            AppProfile app = settings.app(target.appName(), target.platform());
            String appRef = runOn == RunOn.BROWSERSTACK ? AppProvider.appReference(settings, app) : app.localAppPath();
            caps = AppCapabilities.build(target.platform(), app, runOn == RunOn.LOCAL ? device : null, appRef, settings.timeouts());
            appId = target.platform() == Platform.ANDROID ? nullToEmpty(app.appPackage()) : nullToEmpty(app.bundleId());
        } else {
            caps = BrowserCapabilities.build(target.platform(), settings.web(), runOn == RunOn.LOCAL ? device : null, settings.timeouts());
        }
        if (runOn == RunOn.BROWSERSTACK) {
            caps.setCapability("bstack:options", BrowserStackOptions.build(settings, target, device));
        }
        URL url = toUrl(runOn == RunOn.BROWSERSTACK ? settings.browserstack().hubUrl() : settings.local().appiumUrl());
        LOG.info("Starting {} {} session on {} ({} {})", target.platform().id(), target.module().id(), runOn, device.deviceName(), device.platformVersion());
        AppiumDriver driver = target.platform() == Platform.ANDROID ? new AndroidDriver(url, caps) : new IOSDriver(url, caps);
        return new TestSession(driver, target, device, appId);
    }

    private static URL toUrl(String value) {
        try {
            return URI.create(value).toURL();
        } catch (IllegalArgumentException | MalformedURLException e) {
            throw new ConfigException("Not a valid Appium URL: " + value, e);
        }
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
