package io.github.annamandzulashvili.mobile.app.screens.android.wdio;

import io.github.annamandzulashvili.mobile.app.enums.WdioTab;
import io.github.annamandzulashvili.mobile.core.enums.Platform;
import io.github.annamandzulashvili.mobile.core.screens.BaseScreen;
import io.github.annamandzulashvili.mobile.core.screens.ScreenFor;
import io.github.annamandzulashvili.mobile.core.selectors.Selectors;

/** WebdriverIO demo app tab bar. Tabs carry their names as accessibility labels. */
@ScreenFor(Platform.ANDROID)
public class WdioTabBar extends BaseScreen {

    public WdioTabBar() {
        super(Selectors.accessibilityId("Home"), "WDIO tab bar");
    }

    public void open(WdioTab tab) {
        tap(Selectors.accessibilityId(tab.label()));
    }
}
