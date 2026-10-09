package io.github.annamandzulashvili.mobile.core.config;

import java.nio.file.Path;
import java.util.Map;

/**
 * Everything the loader reads from outside the classpath. Kept as a value object so that
 * unit tests can feed fake environment variables and system properties.
 */
public record ConfigSources(Map<String, String> env, Map<String, String> systemProperties, Path workingDir) {

    public static ConfigSources current() {
        Map<String, String> props = new java.util.HashMap<>();
        System.getProperties().forEach((k, v) -> props.put(String.valueOf(k), String.valueOf(v)));
        return new ConfigSources(System.getenv(), props, Path.of("").toAbsolutePath());
    }
}
