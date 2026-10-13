package io.github.annamandzulashvili.mobile.app.screens.ios;

import io.github.annamandzulashvili.mobile.app.enums.Tab;
import io.github.annamandzulashvili.mobile.app.screens.base.TabBar;
import io.github.annamandzulashvili.mobile.core.enums.Platform;
import io.github.annamandzulashvili.mobile.core.screens.ScreenFor;
import io.github.annamandzulashvili.mobile.core.selectors.Selectors;
import org.openqa.selenium.By;

@ScreenFor(Platform.IOS)
public class IosTabBar extends TabBar {

    public IosTabBar() {
        super(Selectors.accessibilityId("Browser"));
    }

    @Override
    protected By tab(Tab tab) {
        return switch (tab) {
            case HOME -> Selectors.accessibilityId("Home");
            case BROWSER -> Selectors.accessibilityId("Browser");
        };
    }
}
