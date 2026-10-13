package io.github.annamandzulashvili.mobile.app.tests.cases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import io.github.annamandzulashvili.mobile.app.enums.HomeButton;
import io.github.annamandzulashvili.mobile.app.models.TestData;
import io.github.annamandzulashvili.mobile.app.steps.ProverbialSteps;
import io.github.annamandzulashvili.mobile.core.hooks.GpsLocation;
import io.github.annamandzulashvili.mobile.core.hooks.Regression;
import io.github.annamandzulashvili.mobile.core.hooks.TestCaseId;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Regression checks for Proverbial that behave the same on both platforms. */
public abstract class ProverbialRegressionCases {

    protected final ProverbialSteps steps = new ProverbialSteps();

    @Test
    @Regression
    @TestCaseId("C1101")
    @DisplayName("[C1101] Toast button shows a short message")
    void toastAppears() {
        assertThat(steps.launch.launch()).as("home screen after launch").isTrue();
        assertThat(steps.home.tapToastShowsMessage()).as("toast message").isTrue();
    }

    @Test
    @Regression
    @TestCaseId("C1102")
    @GpsLocation("41.7151,44.8271")
    @DisplayName("[C1102] Geolocation opens with a mocked GPS location and Back returns to home")
    void geolocationOpensAndReturns() {
        assertThat(steps.launch.launch()).as("home screen after launch").isTrue();
        assertThat(steps.openAndReturn.openAndReturn(HomeButton.GEOLOCATION)).as("back on home").isTrue();
    }

    @Test
    @Regression
    @TestCaseId("C1103")
    @DisplayName("[C1103] App returns to home after being in the background")
    void survivesBackground() {
        assertThat(steps.launch.launch()).as("home screen after launch").isTrue();
        steps.device.background(3);
        assertThat(steps.home.isShown()).as("home screen after returning").isTrue();
    }

    @Test
    @Regression
    @TestCaseId("C1104")
    @DisplayName("[C1104] Home keeps its buttons after rotating to landscape and back")
    void survivesRotation() {
        assertThat(steps.launch.launch()).as("home screen after launch").isTrue();
        try {
            steps.device.rotateToLandscape();
            assertThat(steps.device.isLandscape()).as("landscape").isTrue();
            assertThat(steps.home.isShown()).as("home screen in landscape").isTrue();
        } finally {
            steps.device.rotateToPortrait();
        }
        assertThat(steps.home.allButtonsVisible()).as("all buttons back in portrait").isTrue();
    }

    @Test
    @Regression
    @TestCaseId("C1105")
    @DisplayName("[C1105] Page URL can be read inside the WebView context")
    void webviewContextExposesTheUrl() {
        assertThat(steps.launch.launch()).as("home screen after launch").isTrue();
        TestData data = TestData.get();
        assertThat(steps.browse.openPage(data.browserUrl())).as("page rendered in the WebView").isTrue();

        Optional<String> url = steps.browser.urlInsideWebView();

        assumeTrue(url.isPresent(), "The app does not expose its WebView to automation (see KNOWN-ISSUES.md)");
        assertThat(url.get()).contains(data.browserUrlHost());
    }
}
