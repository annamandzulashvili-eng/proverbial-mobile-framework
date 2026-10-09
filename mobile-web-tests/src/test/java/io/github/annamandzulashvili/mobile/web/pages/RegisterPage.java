package io.github.annamandzulashvili.mobile.web.pages;

import io.github.annamandzulashvili.mobile.core.selectors.Selectors;
import io.github.annamandzulashvili.mobile.web.models.Customer;
import io.github.annamandzulashvili.mobile.web.pages.base.BasePage;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class RegisterPage extends BasePage {

    private static final By FIRST_NAME = Selectors.css("#input-firstname");
    private static final By LAST_NAME = Selectors.css("#input-lastname");
    private static final By EMAIL = Selectors.css("#input-email");
    private static final By TELEPHONE = Selectors.css("#input-telephone");
    private static final By PASSWORD = Selectors.css("#input-password");
    private static final By CONFIRM = Selectors.css("#input-confirm");
    private static final By AGREE = Selectors.css("label[for='input-agree']");
    private static final By CONTINUE = Selectors.css("input[type='submit'][value='Continue']");
    private static final By FIELD_ERRORS = Selectors.css(".text-danger");
    private static final By PAGE_WARNING = Selectors.css(".alert-danger");

    public RegisterPage() {
        super(Selectors.css("#input-firstname"), "Register page");
    }

    public void open() {
        openRoute("account/register");
    }

    public void fill(Customer customer, boolean acceptPrivacyPolicy) {
        type(FIRST_NAME, customer.firstName());
        type(LAST_NAME, customer.lastName());
        type(EMAIL, customer.email());
        type(TELEPHONE, customer.telephone());
        type(PASSWORD, customer.password());
        type(CONFIRM, customer.confirmPassword());
        if (acceptPrivacyPolicy) {
            tap(AGREE);
        }
    }

    public void submit() {
        tap(CONTINUE);
    }

    /** Field-level and page-level error texts. */
    public List<String> errors() {
        if (!isVisible(FIELD_ERRORS, Duration.ofSeconds(5)) && !isVisible(PAGE_WARNING, Duration.ofSeconds(1))) {
            return List.of();
        }
        List<WebElement> found = new java.util.ArrayList<>(driver().findElements(FIELD_ERRORS));
        found.addAll(driver().findElements(PAGE_WARNING));
        return found.stream().filter(WebElement::isDisplayed).map(WebElement::getText).map(String::trim).toList();
    }
}
