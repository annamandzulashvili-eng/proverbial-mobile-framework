package io.github.annamandzulashvili.mobile.core.enums;

import java.util.Locale;

/** The two mobile platforms the framework drives. */
public enum Platform {
    ANDROID,
    IOS;

    public static Platform from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Platform is not set. Use -Dplatform=android or -Dplatform=ios");
        }
        return switch (value.trim().toLowerCase(Locale.ROOT)) {
            case "android" -> ANDROID;
            case "ios" -> IOS;
            default -> throw new IllegalArgumentException("Unknown platform '" + value + "'. Expected android or ios");
        };
    }

    public String id() {
        return name().toLowerCase(Locale.ROOT);
    }
}
