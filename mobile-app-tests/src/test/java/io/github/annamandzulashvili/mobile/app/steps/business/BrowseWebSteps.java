package io.github.annamandzulashvili.mobile.app.steps.business;

import io.github.annamandzulashvili.mobile.app.enums.Tab;
import io.github.annamandzulashvili.mobile.app.steps.app.BrowserSteps;
import io.github.annamandzulashvili.mobile.app.steps.app.TabBarSteps;
import io.qameta.allure.Step;

/** Home -> Browser tab -> type URL -> page rendered. */
public class BrowseWebSteps {

    private final TabBarSteps tabs;
    private final BrowserSteps browser;

    public BrowseWebSteps(TabBarSteps tabs, BrowserSteps browser) {
        this.tabs = tabs;
        this.browser = browser;
    }

    @Step("Open {url} through the Browser tab")
    public boolean openPage(String url) {
        tabs.open(Tab.BROWSER);
        if (!browser.isShown()) {
            return false;
        }
        browser.open(url);
        return browser.pageShown();
    }
}
