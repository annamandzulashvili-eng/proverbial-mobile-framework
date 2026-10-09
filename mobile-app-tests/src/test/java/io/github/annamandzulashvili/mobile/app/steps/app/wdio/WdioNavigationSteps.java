package io.github.annamandzulashvili.mobile.app.steps.app.wdio;

import io.github.annamandzulashvili.mobile.app.enums.WdioTab;
import io.github.annamandzulashvili.mobile.app.screens.android.wdio.WdioHomeScreen;
import io.github.annamandzulashvili.mobile.app.screens.android.wdio.WdioTabBar;
import io.github.annamandzulashvili.mobile.core.config.RunContext;
import io.qameta.allure.Step;

public class WdioNavigationSteps {

    @Step("WDIO home screen is shown")
    public boolean homeShown() {
        return new WdioHomeScreen().isDisplayedWithin(RunContext.settings().timeouts().condition());
    }

    @Step("Open the {tab} tab")
    public void open(WdioTab tab) {
        new WdioTabBar().open(tab);
    }
}
