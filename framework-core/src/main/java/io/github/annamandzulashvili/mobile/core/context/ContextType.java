package io.github.annamandzulashvili.mobile.core.context;

/** Appium context families. Matching is by prefix: "WEBVIEW_com.app" is a WEBVIEW context. */
public enum ContextType {
    NATIVE("NATIVE_APP"),
    WEBVIEW("WEBVIEW");

    private final String prefix;

    ContextType(String prefix) {
        this.prefix = prefix;
    }

    public boolean matches(String contextName) {
        return contextName != null && contextName.startsWith(prefix);
    }

    public static ContextType of(String contextName) {
        return WEBVIEW.matches(contextName) ? WEBVIEW : NATIVE;
    }
}
