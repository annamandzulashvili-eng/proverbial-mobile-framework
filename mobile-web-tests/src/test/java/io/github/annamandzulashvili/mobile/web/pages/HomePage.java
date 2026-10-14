package io.github.annamandzulashvili.mobile.web.pages;

import io.github.annamandzulashvili.mobile.core.selectors.Selectors;
import io.github.annamandzulashvili.mobile.web.pages.base.BasePage;

public class HomePage extends BasePage {

    public HomePage() {
        super(Selectors.css("body"), "Home page");
    }

    public void open() {
        openRoute("common/home");
    }
}
