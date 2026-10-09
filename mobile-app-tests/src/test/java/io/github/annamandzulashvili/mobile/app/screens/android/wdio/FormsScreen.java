package io.github.annamandzulashvili.mobile.app.screens.android.wdio;

import io.github.annamandzulashvili.mobile.core.enums.Platform;
import io.github.annamandzulashvili.mobile.core.screens.BaseScreen;
import io.github.annamandzulashvili.mobile.core.screens.ScreenFor;
import io.github.annamandzulashvili.mobile.core.selectors.Selectors;

@ScreenFor(Platform.ANDROID)
public class FormsScreen extends BaseScreen {

    public FormsScreen() {
        super(Selectors.accessibilityId("Forms-screen"), "WDIO forms screen");
    }

    public void typeText(String text) {
        type(Selectors.accessibilityId("text-input"), text);
    }

    public String typedTextResult() {
        return text(Selectors.accessibilityId("input-text-result"));
    }

    public void toggleSwitch() {
        tap(Selectors.accessibilityId("switch"));
    }

    public String switchHint() {
        return text(Selectors.accessibilityId("switch-text"));
    }

    public void tapActiveButton() {
        tap(Selectors.accessibilityId("button-Active"));
    }
}
