package io.github.annamandzulashvili.mobile.app.steps.app;

import io.github.annamandzulashvili.mobile.app.enums.Tab;
import io.github.annamandzulashvili.mobile.app.screens.base.TabBar;
import io.github.annamandzulashvili.mobile.core.screens.ScreenFactory;
import io.qameta.allure.Step;

public class TabBarSteps {

    @Step("Open the {tab} tab")
    public void open(Tab tab) {
        ScreenFactory.get(TabBar.class).open(tab);
    }
}
