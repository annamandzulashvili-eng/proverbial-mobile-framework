package io.github.annamandzulashvili.mobile.core.waits;

import io.github.annamandzulashvili.mobile.core.config.RunContext;
import io.github.annamandzulashvili.mobile.core.driver.DriverManager;
import java.time.Duration;
import java.util.function.Function;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Explicit waits on the current session. Implicit wait stays 0; there is no sleep anywhere. */
public final class Waits {

    private Waits() {
    }

    public static Duration defaultTimeout() {
        return RunContext.settings().timeouts().condition();
    }

    public static WebElement visible(By locator) {
        return visible(locator, defaultTimeout());
    }

    public static WebElement visible(By locator, Duration timeout) {
        return until(ExpectedConditions.visibilityOfElementLocated(locator), timeout);
    }

    public static WebElement clickable(By locator) {
        return until(ExpectedConditions.elementToBeClickable(locator), defaultTimeout());
    }

    public static WebElement present(By locator) {
        return until(ExpectedConditions.presenceOfElementLocated(locator), defaultTimeout());
    }

    public static void gone(By locator) {
        gone(locator, defaultTimeout());
    }

    public static void gone(By locator, Duration timeout) {
        until(ExpectedConditions.invisibilityOfElementLocated(locator), timeout);
    }

    /** True if the element becomes visible within {@code timeout}; never throws for "not there". */
    public static boolean isVisibleWithin(By locator, Duration timeout) {
        try {
            visible(locator, timeout);
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    public static <T> T until(Function<WebDriver, T> condition, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(DriverManager.driver(), timeout,
                RunContext.settings().timeouts().polling());
        return wait.until(condition);
    }
}
