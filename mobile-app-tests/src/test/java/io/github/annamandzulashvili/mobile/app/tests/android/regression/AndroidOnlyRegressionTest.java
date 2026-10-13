package io.github.annamandzulashvili.mobile.app.tests.android.regression;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.annamandzulashvili.mobile.app.enums.HomeButton;
import io.github.annamandzulashvili.mobile.app.steps.ProverbialSteps;
import io.github.annamandzulashvili.mobile.core.hooks.AndroidTest;
import io.github.annamandzulashvili.mobile.core.hooks.Regression;
import io.github.annamandzulashvili.mobile.core.hooks.TestCaseId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Behaviour that exists only on Android: the notification shade. */
@AndroidTest
class AndroidOnlyRegressionTest {

    private final ProverbialSteps steps = new ProverbialSteps();

    @Test
    @Regression
    @TestCaseId("C1201")
    @DisplayName("[C1201] Notification button posts a notification to the shade")
    void notificationAppearsInTheShade() {
        assertThat(steps.launch.launch()).as("home screen after launch").isTrue();
        steps.home.tap(HomeButton.NOTIFICATION);
        assertThat(steps.notifications.shadeShows("Proverbial")).as("notification from Proverbial").isTrue();
    }
}
