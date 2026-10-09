package io.github.annamandzulashvili.mobile.core.enums;

import java.util.Locale;

/** What a test drives: a native app or the mobile browser. */
public enum Module {
    APP,
    WEB;

    public String id() {
        return name().toLowerCase(Locale.ROOT);
    }
}
