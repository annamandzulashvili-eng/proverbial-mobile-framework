package io.github.annamandzulashvili.mobile.app.enums;

/** Tabs of the WebdriverIO demo app; the label is the tab's accessibility id. */
public enum WdioTab {
    HOME("Home"),
    WEBVIEW("Webview"),
    LOGIN("Login"),
    FORMS("Forms"),
    SWIPE("Swipe"),
    DRAG("Drag");

    private final String label;

    WdioTab(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
