package io.github.annamandzulashvili.mobile.core.unit;

import io.github.annamandzulashvili.mobile.core.config.ConfigLoader;
import io.github.annamandzulashvili.mobile.core.config.ConfigSources;
import io.github.annamandzulashvili.mobile.core.config.Settings;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/** Builds Settings from the real core JSON files with controlled env and -D values. */
final class TestSettings {

    private TestSettings() {
    }

    static Settings load(Map<String, String> env, Map<String, String> props, Path workingDir) {
        return ConfigLoader.load(new ConfigSources(env, props, workingDir), TestSettings.class.getClassLoader());
    }

    static Settings withCredentials(Path workingDir) {
        Map<String, String> env = new HashMap<>();
        env.put("BROWSERSTACK_USERNAME", "demo_user");
        env.put("BROWSERSTACK_ACCESS_KEY", "demo-key-123");
        return load(env, Map.of(), workingDir);
    }
}
