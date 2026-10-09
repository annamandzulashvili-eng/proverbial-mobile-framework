package io.github.annamandzulashvili.mobile.app.screens.base;

import io.github.annamandzulashvili.mobile.core.context.ContextType;
import io.github.annamandzulashvili.mobile.core.driver.DriverManager;
import io.github.annamandzulashvili.mobile.core.driver.TestSession;
import io.github.annamandzulashvili.mobile.core.screens.BaseScreen;
import java.time.Duration;
import java.util.Optional;
import org.openqa.selenium.By;

/** In-app browser: URL field, Find button and a native WebView that shows the page. */
public abstract class BrowserScreen extends BaseScreen {

    protected BrowserScreen(By container) {
        super(container, "Browser screen");
    }

    protected abstract By urlField();

    protected abstract By findButton();

    protected abstract By webView();

    public void open(String url) {
        type(urlField(), url);
        tap(findButton());
    }

    public boolean isPageShown(Duration timeout) {
        return isVisible(webView(), timeout);
    }

    /**
     * The page URL read inside the WEBVIEW context; the scope switches back to NATIVE_APP afterwards.
     * Empty when the app does not expose its WebView (release builds usually disable WebView debugging).
     */
    public Optional<String> urlInsideWebView(Duration wait) {
        TestSession session = DriverManager.current();
        try {
            return Optional.of(session.contexts().inContext(ContextType.WEBVIEW, wait, () -> session.driver().getCurrentUrl()));
        } catch (org.openqa.selenium.TimeoutException e) {
            return Optional.empty();
        }
    }
}
