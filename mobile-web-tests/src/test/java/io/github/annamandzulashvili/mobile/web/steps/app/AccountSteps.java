package io.github.annamandzulashvili.mobile.web.steps.app;

import io.github.annamandzulashvili.mobile.web.models.Customer;
import io.github.annamandzulashvili.mobile.web.pages.AccountPage;
import io.github.annamandzulashvili.mobile.web.pages.LoginPage;
import io.github.annamandzulashvili.mobile.web.pages.RegisterPage;
import io.qameta.allure.Step;
import java.util.List;

public class AccountSteps {

    private final RegisterPage register = new RegisterPage();
    private final LoginPage login = new LoginPage();
    private final AccountPage account = new AccountPage();

    @Step("Submit the registration form")
    public void register(Customer customer, boolean acceptPrivacyPolicy) {
        register.open();
        register.fill(customer, acceptPrivacyPolicy);
        register.submit();
    }

    public List<String> registrationErrors() {
        return register.errors();
    }

    @Step("Account was created")
    public boolean accountCreated() {
        return account.headingContains("Your Account Has Been Created");
    }

    @Step("Log in as {email}")
    public void logIn(String email, String password) {
        login.open();
        login.logIn(email, password);
    }

    @Step("Login shows warning \"{text}\"")
    public boolean loginShowsWarning(String text) {
        return login.showsWarning(text);
    }

    @Step("Log out")
    public boolean logOut() {
        account.logOut();
        return account.headingContains("Account Logout");
    }
}
