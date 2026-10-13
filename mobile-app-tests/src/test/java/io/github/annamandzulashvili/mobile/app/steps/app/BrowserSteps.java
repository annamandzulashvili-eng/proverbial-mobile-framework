package io.github.annamandzulashvili.mobile.app.steps.app;

import io.github.annamandzulashvili.mobile.app.screens.base.BrowserScreen;
import io.github.annamandzulashvili.mobile.core.config.RunContext;
import io.github.annamandzulashvili.mobile.core.screens.ScreenFactory;
import io.qameta.allure.Step;
import java.time.Duration;
import java.util.Optional;

public class BrowserSteps {

    private BrowserScreen browser() {
        return ScreenFactory.get(BrowserScreen.class);
    }

    @Step("Browser screen is shown")
    public boolean isShown() {
        return browser().isDisplayedWithin(RunContext.settings().timeouts().condition());
    }

    @Step("Open {url} in the in-app browser")
    public void open(String url) {
        browser().open(url);
    }

    @Step("Page is rendered in the WebView")
    public boolean pageShown() {
        return browser().isPageShown(Duration.ofSeconds(30));
    }

    /**
     * Reads the page URL from inside the WebView context. Empty when the app does not expose its WebView
     * to automation (release builds usually disable WebView debugging).
     */
    @Step("Read the page URL inside the WebView context")
    public Optional<String> urlInsideWebView() {
        return browser().urlInsideWebView(RunContext.settings().timeouts().webviewContext());
    }
}
