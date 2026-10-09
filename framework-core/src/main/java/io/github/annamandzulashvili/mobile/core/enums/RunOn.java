package io.github.annamandzulashvili.mobile.core.enums;

import java.util.Locale;

/** Where the Appium session is created. */
public enum RunOn {
    BROWSERSTACK,
    LOCAL;

    public static RunOn from(String value) {
        if (value == null || value.isBlank()) {
            return BROWSERSTACK;
        }
        return switch (value.trim().toLowerCase(Locale.ROOT)) {
            case "browserstack" -> BROWSERSTACK;
            case "local" -> LOCAL;
            default -> throw new IllegalArgumentException("Unknown runOn '" + value + "'. Expected browserstack or local");
        };
    }
}
