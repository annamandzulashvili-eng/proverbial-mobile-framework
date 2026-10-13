package io.github.annamandzulashvili.mobile.app.screens.ios;

import io.appium.java_client.AppiumBy;
import io.github.annamandzulashvili.mobile.core.driver.DriverManager;
import io.github.annamandzulashvili.mobile.core.waits.Waits;
import java.time.Duration;
import org.openqa.selenium.By;

/** iOS has no Back key: tap the navigation bar's back button, or fall back to Appium's back command. */
final class IosBackNavigation {

    private static final By NAV_BACK = AppiumBy.iOSClassChain("**/XCUIElementTypeNavigationBar/XCUIElementTypeButton[1]");

    private IosBackNavigation() {
    }

    static void goBack() {
        if (Waits.isVisibleWithin(NAV_BACK, Duration.ofSeconds(2))) {
            DriverManager.driver().findElement(NAV_BACK).click();
        } else {
            DriverManager.driver().navigate().back();
        }
    }
}
