package io.github.annamandzulashvili.mobile.app.screens.ios;

import io.appium.java_client.AppiumBy;
import io.github.annamandzulashvili.mobile.app.screens.base.ToastMessage;
import io.github.annamandzulashvili.mobile.core.enums.Platform;
import io.github.annamandzulashvili.mobile.core.screens.ScreenFor;

/**
 * iOS has no system toast, so the app draws a label. Its exact text is not published; this matches any
 * label mentioning "toast" and must be pinned after the discovery run (KNOWN-ISSUES.md).
 */
@ScreenFor(Platform.IOS)
public class IosToastMessage extends ToastMessage {

    public IosToastMessage() {
        super(AppiumBy.iOSNsPredicateString("type == 'XCUIElementTypeStaticText' AND label CONTAINS[c] 'toast'"));
    }
}
