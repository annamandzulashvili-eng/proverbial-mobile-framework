package io.github.annamandzulashvili.mobile.app.steps.app;

import io.github.annamandzulashvili.mobile.core.system.NotificationShade;
import io.qameta.allure.Step;
import java.time.Duration;

/** Android notification shade. iOS gives no API to open Notification Center, so these steps are Android-only. */
public class NotificationSteps {

    @Step("Notification shade shows a notification from {appName}")
    public boolean shadeShows(String appName) {
        NotificationShade.open();
        try {
            return NotificationShade.containsText(appName, Duration.ofSeconds(10));
        } finally {
            NotificationShade.close();
        }
    }
}
