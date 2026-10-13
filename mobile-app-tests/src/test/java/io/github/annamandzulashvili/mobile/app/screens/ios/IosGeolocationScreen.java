package io.github.annamandzulashvili.mobile.app.screens.ios;

import io.appium.java_client.AppiumBy;
import io.github.annamandzulashvili.mobile.app.screens.base.GeolocationScreen;
import io.github.annamandzulashvili.mobile.core.enums.Platform;
import io.github.annamandzulashvili.mobile.core.screens.ScreenFor;

@ScreenFor(Platform.IOS)
public class IosGeolocationScreen extends GeolocationScreen {

    public IosGeolocationScreen() {
        // The content is a WebView; confirm with the discovery run (KNOWN-ISSUES.md).
        super(AppiumBy.iOSClassChain("**/XCUIElementTypeWebView"));
    }

    @Override
    public void goBack() {
        IosBackNavigation.goBack();
    }
}
