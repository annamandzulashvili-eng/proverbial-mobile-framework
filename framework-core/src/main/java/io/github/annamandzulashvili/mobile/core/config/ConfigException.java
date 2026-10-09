package io.github.annamandzulashvili.mobile.core.config;

/** Thrown when configuration is missing or invalid. Messages name keys, never values. */
public class ConfigException extends RuntimeException {

    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
