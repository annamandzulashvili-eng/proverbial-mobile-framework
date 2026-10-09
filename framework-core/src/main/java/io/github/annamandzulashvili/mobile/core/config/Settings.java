package io.github.annamandzulashvili.mobile.core.config;

import io.github.annamandzulashvili.mobile.core.enums.Platform;
import io.github.annamandzulashvili.mobile.core.enums.RunOn;
import java.util.Locale;
import java.util.Map;

/** The fully merged, typed configuration of one run. Immutable. */
public record Settings(
        RunSettings run,
        BrowserStackSettings browserstack,
        Map<String, DeviceProfile> devices,
        TimeoutsSettings timeouts,
        ReportingSettings reporting,
        LocalSettings local,
        Map<String, AppProfile> apps,
        WebSettings web,
        Map<String, String> scripts) {

    public Platform platform() {
        return Platform.from(run.platform());
    }

    public RunOn runOn() {
        return RunOn.from(run.runOn());
    }

    public AppProfile app(String appName, Platform platform) {
        String key = appName + "-" + platform.id();
        AppProfile profile = apps == null ? null : apps.get(key);
        if (profile == null) {
            throw new ConfigException("No app profile '" + key + "' in apps. Known: " + (apps == null ? "none" : apps.keySet()));
        }
        return profile;
    }

    public String script(String name) {
        String script = scripts == null ? null : scripts.get(name);
        if (script == null) {
            throw new ConfigException("No executor script '" + name + "' in browserStackScripts.json");
        }
        return script;
    }

    /**
     * Checks what a device run needs. Called once before the first session, not by unit tests.
     * Fails fast with the name of the missing key, never its value.
     */
    public Settings validateForDeviceRun() {
        platform();
        String env = run.env() == null ? "" : run.env().trim().toLowerCase(Locale.ROOT);
        if (env.startsWith("prod")) {
            throw new ConfigException("Refusing to run against environment '" + run.env() + "': production is not a test target");
        }
        if (run.threads() < 1 || run.threads() > 5) {
            throw new ConfigException("run.threads must be between 1 and 5 (BrowserStack plan limit), got " + run.threads());
        }
        if (runOn() == RunOn.BROWSERSTACK) {
            requireSet(browserstack.userName(), "BROWSERSTACK_USERNAME");
            requireSet(browserstack.accessKey(), "BROWSERSTACK_ACCESS_KEY");
            requireSet(browserstack.hubUrl(), "browserstack.hubUrl");
        }
        if (run.device() != null && !run.device().isBlank() && (devices == null || !devices.containsKey(run.device()))) {
            throw new ConfigException("Unknown device '" + run.device() + "'. Known: " + (devices == null ? "none" : devices.keySet()));
        }
        return this;
    }

    private static void requireSet(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new ConfigException(name + " is not set (environment variable, credentials.json or -D property)");
        }
    }
}
