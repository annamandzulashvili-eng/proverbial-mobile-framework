package io.github.annamandzulashvili.mobile.core.selectors;

import io.appium.java_client.AppiumBy;
import io.github.annamandzulashvili.mobile.core.enums.Platform;
import org.openqa.selenium.By;

/**
 * Locator factory. Priority: accessibility id / resource-id, then text, XPath only as a last resort.
 * Locators are pinned from captured page source; nothing is discovered at runtime.
 */
public final class Selectors {

    private Selectors() {
    }

    /** Android resource-id ("com.app:id/name") or iOS name. */
    public static By id(String id) {
        return AppiumBy.id(id);
    }

    /** Android content-desc / iOS accessibilityIdentifier. */
    public static By accessibilityId(String id) {
        return AppiumBy.accessibilityId(id);
    }

    public static By androidClass(String className) {
        return AppiumBy.className(className);
    }

    /** Visible text that contains {@code text}, case-insensitive on both platforms. */
    public static By textContains(Platform platform, String text) {
        String safe = text.replace("\"", "\\\"").replace("'", "\\'");
        return platform == Platform.ANDROID
                ? AppiumBy.androidUIAutomator("new UiSelector().textMatches(\"(?i).*" + regexSafe(text) + ".*\")")
                : AppiumBy.iOSNsPredicateString("label CONTAINS[c] '" + safe + "' OR value CONTAINS[c] '" + safe + "'");
    }

    /** Replaces regex metacharacters with "." so user text can sit inside a UiSelector pattern. */
    public static String regexSafe(String text) {
        return text.replaceAll("[^\\p{L}\\p{N} ]", ".");
    }

    public static By css(String css) {
        return By.cssSelector(css);
    }

    public static By xpath(String xpath) {
        return By.xpath(xpath);
    }
}
