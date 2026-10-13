package io.github.annamandzulashvili.mobile.app.screens.android.wdio;

import io.github.annamandzulashvili.mobile.core.enums.Platform;
import io.github.annamandzulashvili.mobile.core.screens.BaseScreen;
import io.github.annamandzulashvili.mobile.core.screens.ScreenFor;
import io.github.annamandzulashvili.mobile.core.selectors.Selectors;

/** Native Android alert (React Native Alert.alert). */
@ScreenFor(Platform.ANDROID)
public class AlertDialog extends BaseScreen {

    public AlertDialog() {
        super(Selectors.id("android:id/alertTitle"), "Alert dialog");
    }

    public String title() {
        return text(Selectors.id("android:id/alertTitle"));
    }

    public String message() {
        return text(Selectors.id("android:id/message"));
    }

    public void confirm() {
        tap(Selectors.id("android:id/button1"));
    }
}
