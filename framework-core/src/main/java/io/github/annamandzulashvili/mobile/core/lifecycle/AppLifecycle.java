package io.github.annamandzulashvili.mobile.core.lifecycle;

import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.appmanagement.ApplicationState;
import io.github.annamandzulashvili.mobile.core.config.RunContext;
import io.github.annamandzulashvili.mobile.core.driver.DriverManager;
import io.github.annamandzulashvili.mobile.core.driver.TestSession;
import io.github.annamandzulashvili.mobile.core.waits.PollUntil;
import java.time.Duration;
import java.util.Map;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriver;

/** App state changes: background, terminate, activate, relaunch, deep link, rotation. */
public final class AppLifecycle {

    private AppLifecycle() {
    }

    public static void sendToBackground(Duration duration) {
        apps().runAppInBackground(duration);
    }

    public static ApplicationState state() {
        return apps().queryAppState(appId());
    }

    public static void terminate() {
        apps().terminateApp(appId());
        PollUntil.condition(() -> state() == ApplicationState.NOT_RUNNING || state() == ApplicationState.RUNNING_IN_BACKGROUND_SUSPENDED,
                RunContext.settings().timeouts().condition(), Duration.ofMillis(500), "the app to stop");
    }

    public static void activate() {
        apps().activateApp(appId());
        waitForForeground();
    }

    public static void relaunch() {
        terminate();
        activate();
    }

    public static void waitForForeground() {
        PollUntil.condition(() -> state() == ApplicationState.RUNNING_IN_FOREGROUND,
                RunContext.settings().timeouts().condition(), Duration.ofMillis(500), "the app to be in the foreground");
    }

    /** Android: {@code mobile: deepLink}. iOS real devices route deep links through Safari, so this is Android-only here. */
    public static void openDeepLink(String url) {
        TestSession session = DriverManager.current();
        session.driver().executeScript("mobile: deepLink", Map.of("url", url, "package", session.appId()));
    }

    public static void rotate(ScreenOrientation orientation) {
        WebDriver driver = DriverManager.driver();
        if (driver instanceof io.appium.java_client.remote.SupportsRotation rotation) {
            rotation.rotate(orientation);
        } else {
            throw new UnsupportedOperationException("Driver cannot rotate");
        }
    }

    public static ScreenOrientation orientation() {
        WebDriver driver = DriverManager.driver();
        if (driver instanceof io.appium.java_client.remote.SupportsRotation rotation) {
            return rotation.getOrientation();
        }
        throw new UnsupportedOperationException("Driver cannot report orientation");
    }

    private static InteractsWithApps apps() {
        return (InteractsWithApps) DriverManager.driver();
    }

    /**
     * The configured package / bundle id, or the one of the app in front when the profile leaves it empty
     * (the Proverbial iOS bundle id is not published, so it is read from the device).
     */
    static String appId() {
        TestSession session = DriverManager.current();
        if (session.appId() != null && !session.appId().isBlank()) {
            return session.appId();
        }
        if (session.driver() instanceof io.appium.java_client.android.AndroidDriver android) {
            return android.getCurrentPackage();
        }
        Object info = session.driver().executeScript("mobile: activeAppInfo");
        if (info instanceof Map<?, ?> map && map.get("bundleId") != null) {
            return String.valueOf(map.get("bundleId"));
        }
        throw new IllegalStateException("Cannot determine the app id: set appPackage/bundleId in the app profile");
    }
}
