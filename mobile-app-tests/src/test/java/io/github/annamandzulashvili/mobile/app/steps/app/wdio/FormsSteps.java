package io.github.annamandzulashvili.mobile.app.steps.app.wdio;

import io.github.annamandzulashvili.mobile.app.screens.android.wdio.AlertDialog;
import io.github.annamandzulashvili.mobile.app.screens.android.wdio.FormsScreen;
import io.qameta.allure.Step;

public class FormsSteps {

    private final FormsScreen forms = new FormsScreen();

    @Step("Type \"{text}\" and read it back")
    public String typeAndReadBack(String text) {
        forms.waitUntilDisplayed();
        forms.typeText(text);
        return forms.typedTextResult();
    }

    /** Hint text before and after toggling, "before|after". */
    @Step("Toggle the switch")
    public String toggleSwitch() {
        String before = forms.switchHint();
        forms.toggleSwitch();
        return before + "|" + forms.switchHint();
    }

    @Step("Tap Active and read the alert")
    public String tapActive() {
        forms.tapActiveButton();
        AlertDialog alert = new AlertDialog();
        alert.waitUntilDisplayed();
        String message = alert.message();
        alert.confirm();
        return message;
    }
}
