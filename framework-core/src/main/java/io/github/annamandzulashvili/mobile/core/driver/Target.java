package io.github.annamandzulashvili.mobile.core.driver;

import io.github.annamandzulashvili.mobile.core.enums.DeviceType;
import io.github.annamandzulashvili.mobile.core.enums.Module;
import io.github.annamandzulashvili.mobile.core.enums.Platform;

/**
 * What one test needs a session for. Built by the driver extension from the test's annotations
 * and the run settings.
 *
 * @param appName     app profile name ("proverbial", "wdio"); ignored for web
 * @param gpsLocation "lat,long" for BrowserStack, or empty
 */
public record Target(Module module, Platform platform, DeviceType deviceType, String appName, String testName, String gpsLocation) {
}
