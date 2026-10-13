package io.github.annamandzulashvili.mobile.app.screens.android;

import io.github.annamandzulashvili.mobile.app.enums.Tab;
import io.github.annamandzulashvili.mobile.app.screens.base.TabBar;
import io.github.annamandzulashvili.mobile.core.enums.Platform;
import io.github.annamandzulashvili.mobile.core.screens.ScreenFor;
import io.github.annamandzulashvili.mobile.core.selectors.Selectors;
import org.openqa.selenium.By;

@ScreenFor(Platform.ANDROID)
public class AndroidTabBar extends TabBar {

    public AndroidTabBar() {
        super(Selectors.id(AndroidHomeScreen.ID + "buttonPage"));
    }

    @Override
    protected By tab(Tab tab) {
        return switch (tab) {
            case HOME -> Selectors.id(AndroidHomeScreen.ID + "buttonPage");
            case BROWSER -> Selectors.id(AndroidHomeScreen.ID + "webview");
        };
    }
}
