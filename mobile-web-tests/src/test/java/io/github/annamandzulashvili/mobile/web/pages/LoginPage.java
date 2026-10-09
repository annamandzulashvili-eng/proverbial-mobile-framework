package io.github.annamandzulashvili.mobile.web.pages;

import io.github.annamandzulashvili.mobile.core.selectors.Selectors;
import io.github.annamandzulashvili.mobile.web.pages.base.BasePage;
import java.time.Duration;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {

    private static final By EMAIL = Selectors.css("#input-email");
    private static final By PASSWORD = Selectors.css("#input-password");
    private static final By SUBMIT = Selectors.css("input[type='submit'][value='Login']");
    private static final By WARNING = Selectors.css(".alert-danger");

    public LoginPage() {
        super(Selectors.css("#input-email"), "Login page");
    }

    public void open() {
        openRoute("account/login");
    }

    public void logIn(String email, String password) {
        type(EMAIL, email);
        type(PASSWORD, password);
        tap(SUBMIT);
    }

    public boolean showsWarning(String text) {
        return isVisible(WARNING, Duration.ofSeconds(10)) && text(WARNING).contains(text);
    }
}
