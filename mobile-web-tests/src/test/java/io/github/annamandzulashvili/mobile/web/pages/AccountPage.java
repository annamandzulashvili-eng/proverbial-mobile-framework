package io.github.annamandzulashvili.mobile.web.pages;

import io.github.annamandzulashvili.mobile.core.selectors.Selectors;
import io.github.annamandzulashvili.mobile.web.pages.base.BasePage;
import java.time.Duration;
import org.openqa.selenium.By;

/** Account area: the success page after registration, the account home and logout. */
public class AccountPage extends BasePage {

    private static final By HEADING = Selectors.css("#content h1, #content h2");

    public AccountPage() {
        super(Selectors.css("#content"), "Account page");
    }

    public boolean headingContains(String text) {
        return isVisible(Selectors.xpath("//*[@id='content']//*[self::h1 or self::h2][contains(., \"" + text + "\")]"), Duration.ofSeconds(15));
    }

    public String heading() {
        return text(HEADING);
    }

    public void logOut() {
        openRoute("account/logout");
    }
}
