package io.github.annamandzulashvili.mobile.app.screens.android;

import io.github.annamandzulashvili.mobile.app.screens.base.SpeedTestScreen;
import io.github.annamandzulashvili.mobile.core.enums.Platform;
import io.github.annamandzulashvili.mobile.core.screens.ScreenFor;
import io.github.annamandzulashvili.mobile.core.selectors.Selectors;

@ScreenFor(Platform.ANDROID)
public class AndroidSpeedTestScreen extends SpeedTestScreen {

    public AndroidSpeedTestScreen() {
        // The content is a WebView; confirm with the discovery run (KNOWN-ISSUES.md).
        super(Selectors.androidClass("android.webkit.WebView"));
    }

    @Override
    public void goBack() {
        AndroidBackNavigation.pressBack();
    }
}
