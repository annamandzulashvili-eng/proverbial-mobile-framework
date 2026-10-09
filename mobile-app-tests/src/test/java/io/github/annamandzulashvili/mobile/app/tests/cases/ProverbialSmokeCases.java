package io.github.annamandzulashvili.mobile.app.tests.cases;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.annamandzulashvili.mobile.app.enums.HomeButton;
import io.github.annamandzulashvili.mobile.app.models.TestData;
import io.github.annamandzulashvili.mobile.app.steps.ProverbialSteps;
import io.github.annamandzulashvili.mobile.core.hooks.Smoke;
import io.github.annamandzulashvili.mobile.core.hooks.TestCaseId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Smoke checks for Proverbial, written once. AndroidProverbialSmokeTest and IosProverbialSmokeTest
 * only add the platform annotation, so both platforms run exactly the same scenarios.
 */
public abstract class ProverbialSmokeCases {

    protected final ProverbialSteps steps = new ProverbialSteps();

    @Test
    @Smoke
    @TestCaseId("C1001")
    @DisplayName("[C1001] App opens on the home screen with all six buttons")
    void appOpensOnHome() {
        assertThat(steps.launch.launch()).as("home screen after launch").isTrue();
        assertThat(steps.home.allButtonsVisible()).as("all six home buttons").isTrue();
    }

    @Test
    @Smoke
    @TestCaseId("C1002")
    @DisplayName("[C1002] Text button changes the welcome message")
    void textButtonChangesMessage() {
        assertThat(steps.launch.launch()).as("home screen after launch").isTrue();
        assertThat(steps.home.tapTextChangesWelcomeMessage()).as("welcome message replaced").isTrue();
    }

    @Test
    @Smoke
    @TestCaseId("C1003")
    @DisplayName("[C1003] Color button changes the welcome message colour")
    void colorButtonChangesColour() {
        assertThat(steps.launch.launch()).as("home screen after launch").isTrue();
        assertThat(steps.home.tapColorChangesWelcomeMessage()).as("welcome message pixels changed").isTrue();
    }

    @Test
    @Smoke
    @TestCaseId("C1004")
    @DisplayName("[C1004] Browser tab opens a web page in the in-app browser")
    void browserTabRendersPage() {
        assertThat(steps.launch.launch()).as("home screen after launch").isTrue();
        assertThat(steps.browse.openPage(TestData.get().browserUrl())).as("page rendered in the WebView").isTrue();
    }

    @Test
    @Smoke
    @TestCaseId("C1005")
    @DisplayName("[C1005] Speed Test opens and Back returns to home")
    void speedTestOpensAndReturns() {
        assertThat(steps.launch.launch()).as("home screen after launch").isTrue();
        assertThat(steps.openAndReturn.openAndReturn(HomeButton.SPEED_TEST)).as("back on home").isTrue();
    }
}
