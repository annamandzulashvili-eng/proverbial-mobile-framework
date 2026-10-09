package io.github.annamandzulashvili.mobile.app.screens.android;

import io.github.annamandzulashvili.mobile.app.screens.base.ToastMessage;
import io.github.annamandzulashvili.mobile.core.enums.Platform;
import io.github.annamandzulashvili.mobile.core.screens.ScreenFor;
import io.github.annamandzulashvili.mobile.core.selectors.Selectors;

/** UiAutomator2 exposes a system Toast as an android.widget.Toast node for a few seconds. */
@ScreenFor(Platform.ANDROID)
public class AndroidToastMessage extends ToastMessage {

    public AndroidToastMessage() {
        super(Selectors.xpath("//android.widget.Toast"));
    }
}
