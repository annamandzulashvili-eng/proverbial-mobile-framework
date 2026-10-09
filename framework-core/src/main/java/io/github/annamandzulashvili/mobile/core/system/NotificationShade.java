package io.github.annamandzulashvili.mobile.core.system;

import io.appium.java_client.android.AndroidDriver;
import io.github.annamandzulashvili.mobile.core.driver.DriverManager;
import io.github.annamandzulashvili.mobile.core.enums.Platform;
import io.github.annamandzulashvili.mobile.core.selectors.Selectors;
import io.github.annamandzulashvili.mobile.core.waits.Waits;
import java.time.Duration;

/** Android notification shade. iOS has no API to open Notification Center, so it is Android-only. */
public final class NotificationShade {

    private NotificationShade() {
    }

    public static void open() {
        android().openNotifications();
    }

    public static boolean containsText(String text, Duration wait) {
        return Waits.isVisibleWithin(Selectors.textContains(Platform.ANDROID, text), wait);
    }

    public static void close() {
        android().navigate().back();
    }

    private static AndroidDriver android() {
        if (!(DriverManager.driver() instanceof AndroidDriver driver)) {
            throw new UnsupportedOperationException("The notification shade can only be opened on Android");
        }
        return driver;
    }
}
