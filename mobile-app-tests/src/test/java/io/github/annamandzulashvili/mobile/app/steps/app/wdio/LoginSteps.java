package io.github.annamandzulashvili.mobile.app.steps.app.wdio;

import io.github.annamandzulashvili.mobile.app.screens.android.wdio.AlertDialog;
import io.github.annamandzulashvili.mobile.app.screens.android.wdio.LoginScreen;
import io.qameta.allure.Step;
import java.time.Duration;

public class LoginSteps {

    private final LoginScreen login = new LoginScreen();
    private final AlertDialog alert = new AlertDialog();

    @Step("Log in as {email}")
    public void logIn(String email, String password) {
        login.waitUntilDisplayed();
        login.switchToLogin();
        login.enterEmail(email);
        login.enterPassword(password);
        login.submitLogin();
    }

    @Step("Sign up as {email}")
    public void signUp(String email, String password, String repeatPassword) {
        login.waitUntilDisplayed();
        login.switchToSignUp();
        login.enterEmail(email);
        login.enterPassword(password);
        login.enterRepeatPassword(repeatPassword);
        login.submitSignUp();
    }

    /** Alert title and message, then the alert is closed. */
    @Step("Read and close the result alert")
    public String readAndCloseAlert() {
        alert.waitUntilDisplayed();
        String result = alert.title() + ": " + alert.message();
        alert.confirm();
        return result;
    }

    @Step("Form shows error \"{message}\"")
    public boolean showsError(String message) {
        return login.showsError(message, Duration.ofSeconds(5));
    }
}
