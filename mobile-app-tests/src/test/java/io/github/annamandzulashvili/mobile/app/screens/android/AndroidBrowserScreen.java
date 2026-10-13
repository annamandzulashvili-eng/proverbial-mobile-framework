package io.github.annamandzulashvili.mobile.app.screens.android;

import io.github.annamandzulashvili.mobile.app.screens.base.BrowserScreen;
import io.github.annamandzulashvili.mobile.core.enums.Platform;
import io.github.annamandzulashvili.mobile.core.screens.ScreenFor;
import io.github.annamandzulashvili.mobile.core.selectors.Selectors;
import org.openqa.selenium.By;

@ScreenFor(Platform.ANDROID)
public class AndroidBrowserScreen extends BrowserScreen {

    public AndroidBrowserScreen() {
        super(Selectors.id(AndroidHomeScreen.ID + "url"));
    }

    @Override
    protected By urlField() {
        return Selectors.id(AndroidHomeScreen.ID + "url");
    }

    @Override
    protected By findButton() {
        return Selectors.id(AndroidHomeScreen.ID + "find");
    }

    @Override
    protected By webView() {
        return Selectors.androidClass("android.webkit.WebView");
    }
}
