package io.github.annamandzulashvili.mobile.app.screens.ios;

import io.appium.java_client.AppiumBy;
import io.github.annamandzulashvili.mobile.app.screens.base.BrowserScreen;
import io.github.annamandzulashvili.mobile.core.enums.Platform;
import io.github.annamandzulashvili.mobile.core.screens.ScreenFor;
import io.github.annamandzulashvili.mobile.core.selectors.Selectors;
import org.openqa.selenium.By;

@ScreenFor(Platform.IOS)
public class IosBrowserScreen extends BrowserScreen {

    public IosBrowserScreen() {
        super(Selectors.accessibilityId("url"));
    }

    @Override
    protected By urlField() {
        return Selectors.accessibilityId("url");
    }

    @Override
    protected By findButton() {
        return Selectors.accessibilityId("find");
    }

    @Override
    protected By webView() {
        return AppiumBy.iOSClassChain("**/XCUIElementTypeWebView");
    }
}
