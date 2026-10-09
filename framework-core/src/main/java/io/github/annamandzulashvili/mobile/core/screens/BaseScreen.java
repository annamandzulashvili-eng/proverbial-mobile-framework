package io.github.annamandzulashvili.mobile.core.screens;

import io.appium.java_client.AppiumDriver;
import io.github.annamandzulashvili.mobile.core.driver.DriverManager;
import io.github.annamandzulashvili.mobile.core.enums.Platform;
import io.github.annamandzulashvili.mobile.core.waits.Waits;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Base of every screen and page. Each screen is anchored on a container locator that proves it is shown.
 * Screens hold locators and user actions only: no assertions, no test data, no state.
 */
public abstract class BaseScreen {

    private final By container;
    private final String name;

    protected BaseScreen(By container, String name) {
        this.container = container;
        this.name = name;
    }

    public String name() {
        return name;
    }

    public boolean isDisplayed() {
        return Waits.isVisibleWithin(container, Duration.ofSeconds(2));
    }

    public boolean isDisplayedWithin(Duration timeout) {
        return Waits.isVisibleWithin(container, timeout);
    }

    /** Waits for the container; fails with the screen name in the message. */
    public void waitUntilDisplayed() {
        try {
            Waits.visible(container);
        } catch (org.openqa.selenium.TimeoutException e) {
            throw new AssertionError(name + " is not displayed (waited for " + container + ")", e);
        }
    }

    protected AppiumDriver driver() {
        return DriverManager.driver();
    }

    protected Platform platform() {
        return DriverManager.platform();
    }

    protected WebElement find(By locator) {
        return Waits.visible(locator);
    }

    protected void tap(By locator) {
        Waits.clickable(locator).click();
    }

    protected void type(By locator, String text) {
        WebElement field = Waits.clickable(locator);
        field.clear();
        field.sendKeys(text);
    }

    protected String text(By locator) {
        return find(locator).getText();
    }

    protected boolean isVisible(By locator, Duration timeout) {
        return Waits.isVisibleWithin(locator, timeout);
    }
}
