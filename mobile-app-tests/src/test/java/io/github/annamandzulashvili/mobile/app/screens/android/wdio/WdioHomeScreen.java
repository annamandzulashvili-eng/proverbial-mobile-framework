package io.github.annamandzulashvili.mobile.app.screens.android.wdio;

import io.github.annamandzulashvili.mobile.core.enums.Platform;
import io.github.annamandzulashvili.mobile.core.screens.BaseScreen;
import io.github.annamandzulashvili.mobile.core.screens.ScreenFor;
import io.github.annamandzulashvili.mobile.core.selectors.Selectors;

@ScreenFor(Platform.ANDROID)
public class WdioHomeScreen extends BaseScreen {

    public WdioHomeScreen() {
        super(Selectors.accessibilityId("Home-screen"), "WDIO home screen");
    }
}
