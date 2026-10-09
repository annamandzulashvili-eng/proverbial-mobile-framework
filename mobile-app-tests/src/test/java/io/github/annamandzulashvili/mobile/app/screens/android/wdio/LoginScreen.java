package io.github.annamandzulashvili.mobile.app.screens.android.wdio;

import java.time.Duration;
import io.github.annamandzulashvili.mobile.core.enums.Platform;
import io.github.annamandzulashvili.mobile.core.screens.BaseScreen;
import io.github.annamandzulashvili.mobile.core.screens.ScreenFor;
import io.github.annamandzulashvili.mobile.core.selectors.Selectors;

/** Login / Sign up form of the WebdriverIO demo app. */
@ScreenFor(Platform.ANDROID)
public class LoginScreen extends BaseScreen {

    public LoginScreen() {
        super(Selectors.accessibilityId("Login-screen"), "WDIO login screen");
    }

    public void switchToLogin() {
        tap(Selectors.accessibilityId("button-login-container"));
    }

    public void switchToSignUp() {
        tap(Selectors.accessibilityId("button-sign-up-container"));
    }

    public void enterEmail(String email) {
        type(Selectors.accessibilityId("input-email"), email);
    }

    public void enterPassword(String password) {
        type(Selectors.accessibilityId("input-password"), password);
    }

    public void enterRepeatPassword(String password) {
        type(Selectors.accessibilityId("input-repeat-password"), password);
    }

    public void submitLogin() {
        tap(Selectors.accessibilityId("button-LOGIN"));
    }

    public void submitSignUp() {
        tap(Selectors.accessibilityId("button-SIGN UP"));
    }

    public boolean showsError(String message, Duration timeout) {
        return isVisible(Selectors.textContains(Platform.ANDROID, message), timeout);
    }
}
