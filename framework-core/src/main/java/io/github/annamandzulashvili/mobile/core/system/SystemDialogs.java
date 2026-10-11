package io.github.annamandzulashvili.mobile.core.system;

import io.github.annamandzulashvili.mobile.core.driver.DriverManager;
import io.github.annamandzulashvili.mobile.core.enums.Platform;
import io.github.annamandzulashvili.mobile.core.selectors.Selectors;
import io.github.annamandzulashvili.mobile.core.waits.Waits;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;

/**
 * OS permission pop-ups (notifications, location). They belong to the OS, not to the app,
 * so they live in core and every app reuses them.
 */
public final class SystemDialogs {

    private static final List<By> ANDROID_ALLOW = List.of(
            Selectors.id("com.android.permissioncontroller:id/permission_allow_button"),
            Selectors.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button"),
            Selectors.id("com.android.packageinstaller:id/permission_allow_button"));
    private static final List<By> ANDROID_DENY = List.of(
            Selectors.id("com.android.permissioncontroller:id/permission_deny_button"),
            Selectors.id("com.android.packageinstaller:id/permission_deny_button"));
    private static final List<By> IOS_ALLOW = List.of(
            Selectors.accessibilityId("Allow"),
            Selectors.accessibilityId("Allow While Using App"),
            Selectors.accessibilityId("Allow Once"));
    private static final List<By> IOS_DENY = List.of(
            Selectors.accessibilityId("Don’t Allow"),
            Selectors.accessibilityId("Don't Allow"));

    private SystemDialogs() {
    }

    /** Taps "Allow" if a permission pop-up appears within {@code wait}. Returns whether one was shown. */
    public static boolean allowIfShown(Duration wait) {
        return tapFirstVisible(DriverManager.platform() == Platform.ANDROID ? ANDROID_ALLOW : IOS_ALLOW, wait);
    }

    /** Taps "Don't Allow" if a permission pop-up appears within {@code wait}. Returns whether one was shown. */
    public static boolean denyIfShown(Duration wait) {
        return tapFirstVisible(DriverManager.platform() == Platform.ANDROID ? ANDROID_DENY : IOS_DENY, wait);
    }

    private static boolean tapFirstVisible(List<By> candidates, Duration wait) {
        long deadline = System.nanoTime() + wait.toNanos();
        do {
            for (By candidate : candidates) {
                if (Waits.isVisibleWithin(candidate, Duration.ofMillis(300))) {
                    DriverManager.driver().findElement(candidate).click();
                    return true;
                }
            }
        } while (System.nanoTime() < deadline);
        return false;
    }
}
