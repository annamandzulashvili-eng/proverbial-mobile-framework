package io.github.annamandzulashvili.mobile.web.steps.app;

import io.github.annamandzulashvili.mobile.web.pages.HomePage;
import io.qameta.allure.Step;

public class HomeSteps {

    private final HomePage home = new HomePage();

    @Step("Open the home page and read its title")
    public String openAndReadTitle() {
        home.open();
        return home.title();
    }
}
