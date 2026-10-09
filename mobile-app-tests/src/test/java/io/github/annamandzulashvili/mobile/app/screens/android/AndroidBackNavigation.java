package io.github.annamandzulashvili.mobile.app.screens.android;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.github.annamandzulashvili.mobile.core.driver.DriverManager;

/** Android leaves a screen with the hardware Back key. */
final class AndroidBackNavigation {

    private AndroidBackNavigation() {
    }

    static void pressBack() {
        ((AndroidDriver) DriverManager.driver()).pressKey(new KeyEvent(AndroidKey.BACK));
    }
}
