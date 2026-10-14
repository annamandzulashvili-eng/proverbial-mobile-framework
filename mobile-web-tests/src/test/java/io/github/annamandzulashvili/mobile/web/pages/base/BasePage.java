package io.github.annamandzulashvili.mobile.web.pages.base;

import io.github.annamandzulashvili.mobile.core.config.RunContext;
import io.github.annamandzulashvili.mobile.core.screens.BaseScreen;
import io.github.annamandzulashvili.mobile.core.waits.Waits;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Base of every web page. Pages open themselves by URL route, which keeps tests independent of the
 * mobile menu. Locators are CSS on the site's ids; XPath only where CSS cannot express it.
 */
public abstract class BasePage extends BaseScreen {

    protected BasePage(By container, String name) {
        super(container, name);
    }

    /** Opens {@code baseUrl + "index.php?route=" + route}. */
    protected void openRoute(String route) {
        driver().get(baseUrl() + "index.php?route=" + route);
        waitUntilDisplayed();
    }

    protected static String baseUrl() {
        String base = RunContext.settings().web().baseUrl();
        return base.endsWith("/") ? base : base + "/";
    }

    /** All visible matches; responsive pages render desktop and mobile copies of some elements. */
    protected List<WebElement> visibleAll(By locator) {
        return Waits.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator), Waits.defaultTimeout());
    }

    public String title() {
        return driver().getTitle();
    }
}
