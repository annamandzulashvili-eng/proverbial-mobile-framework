package io.github.annamandzulashvili.mobile.core.driver;

import io.appium.java_client.AppiumDriver;
import io.github.annamandzulashvili.mobile.core.config.DeviceProfile;
import io.github.annamandzulashvili.mobile.core.context.ContextSwitcher;
import io.github.annamandzulashvili.mobile.core.enums.Platform;

/**
 * Everything that belongs to one running test: the driver, what it was created for and its context switcher.
 * One instance per test, held by {@link DriverManager}. No static mutable state anywhere else.
 */
public final class TestSession {

    private final AppiumDriver driver;
    private final Target target;
    private final DeviceProfile device;
    private final String appId;
    private final ContextSwitcher contexts;

    public TestSession(AppiumDriver driver, Target target, DeviceProfile device, String appId) {
        this.driver = driver;
        this.target = target;
        this.device = device;
        this.appId = appId;
        this.contexts = new ContextSwitcher(ContextSwitcher.ContextApi.of(driver));
    }

    public AppiumDriver driver() {
        return driver;
    }

    public Target target() {
        return target;
    }

    public Platform platform() {
        return target.platform();
    }

    public DeviceProfile device() {
        return device;
    }

    /** Android package or iOS bundle id of the app under test; empty for web sessions. */
    public String appId() {
        return appId;
    }

    public ContextSwitcher contexts() {
        return contexts;
    }

    public String sessionId() {
        return driver.getSessionId() == null ? "" : driver.getSessionId().toString();
    }
}
