package io.github.annamandzulashvili.mobile.core.driver.capabilities;

import io.github.annamandzulashvili.mobile.core.config.BrowserStackSettings;
import io.github.annamandzulashvili.mobile.core.config.DeviceProfile;
import io.github.annamandzulashvili.mobile.core.config.RunSettings;
import io.github.annamandzulashvili.mobile.core.config.Settings;
import io.github.annamandzulashvili.mobile.core.driver.Target;
import java.util.LinkedHashMap;
import java.util.Map;

/** Builds the {@code bstack:options} block. Pure and unit-tested. */
public final class BrowserStackOptions {

    private BrowserStackOptions() {
    }

    public static Map<String, Object> build(Settings settings, Target target, DeviceProfile device) {
        BrowserStackSettings bs = settings.browserstack();
        Map<String, Object> options = new LinkedHashMap<>();
        options.put("userName", bs.userName());
        options.put("accessKey", bs.accessKey());
        options.put("deviceName", device.deviceName());
        options.put("osVersion", device.platformVersion());
        options.put("realMobile", "true");
        options.put("projectName", bs.projectName());
        options.put("buildName", buildName(bs.buildNamePattern(), settings.run(), target));
        options.put("sessionName", target.testName());
        options.put("video", bs.video());
        options.put("networkLogs", bs.networkLogs());
        options.put("idleTimeout", bs.idleTimeout());
        options.put("interactiveDebugging", bs.interactiveDebugging());
        options.put("debug", bs.debug());
        if (AppCapabilities.notBlank(bs.appiumVersion())) {
            options.put("appiumVersion", bs.appiumVersion());
        }
        if (AppCapabilities.notBlank(target.gpsLocation())) {
            options.put("gpsLocation", target.gpsLocation());
        }
        return options;
    }

    /**
     * {@code {suite} {module} for {platform} from {branch} #{buildNumber}}, so every session of one run
     * lands in the same BrowserStack build.
     */
    public static String buildName(String pattern, RunSettings run, Target target) {
        return pattern
                .replace("{suite}", orDash(run.suite()))
                .replace("{module}", target.module().id())
                .replace("{platform}", target.platform().id())
                .replace("{branch}", orDash(run.branch()))
                .replace("{buildNumber}", orDash(run.buildNumber()));
    }

    private static String orDash(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }
}
