package io.github.annamandzulashvili.mobile.app.tests.ios.regression;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.annamandzulashvili.mobile.app.steps.ProverbialSteps;
import io.github.annamandzulashvili.mobile.core.hooks.IosTest;
import io.github.annamandzulashvili.mobile.core.hooks.Regression;
import io.github.annamandzulashvili.mobile.core.hooks.TestCaseId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Behaviour that exists only on iOS: the notification permission pop-up on first launch. */
@IosTest
class IosOnlyRegressionTest {

    private final ProverbialSteps steps = new ProverbialSteps();

    @Test
    @Regression
    @TestCaseId("C1301")
    @DisplayName("[C1301] Denying the notification permission keeps the app usable")
    void denyingPermissionKeepsTheAppUsable() {
        assertThat(steps.launch.launchDenyingPermission()).as("pop-up shown, denied, home usable").isTrue();
        assertThat(steps.home.allButtonsVisible()).as("all six home buttons").isTrue();
    }
}
