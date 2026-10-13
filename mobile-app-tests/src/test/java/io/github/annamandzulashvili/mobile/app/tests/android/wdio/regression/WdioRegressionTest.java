package io.github.annamandzulashvili.mobile.app.tests.android.wdio.regression;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.annamandzulashvili.mobile.app.models.LoginCase;
import io.github.annamandzulashvili.mobile.app.models.TestData;
import io.github.annamandzulashvili.mobile.app.enums.WdioTab;
import io.github.annamandzulashvili.mobile.app.steps.WdioSteps;
import io.github.annamandzulashvili.mobile.core.hooks.AndroidTest;
import io.github.annamandzulashvili.mobile.core.hooks.App;
import io.github.annamandzulashvili.mobile.core.hooks.Regression;
import io.github.annamandzulashvili.mobile.core.hooks.TestCaseId;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@AndroidTest
@App("wdio")
class WdioRegressionTest {

    private final WdioSteps steps = new WdioSteps();
    private final TestData.Wdio data = TestData.get().wdio();

    static Stream<LoginCase> invalidLogins() {
        return TestData.get().wdio().invalidLogins().stream();
    }

    @ParameterizedTest(name = "[C2101] Login validation: {0}")
    @MethodSource("invalidLogins")
    @Regression
    @TestCaseId("C2101")
    void loginValidation(LoginCase loginCase) {
        assertThat(steps.navigation.homeShown()).as("WDIO home").isTrue();
        steps.navigation.open(WdioTab.LOGIN);
        steps.login.logIn(loginCase.email(), loginCase.password());

        assertThat(steps.login.showsError(loginCase.expectedError())).as(loginCase.expectedError()).isTrue();
    }

    @Test
    @Regression
    @TestCaseId("C2102")
    @DisplayName("[C2102] Sign up with different passwords is rejected")
    void signUpPasswordMismatch() {
        assertThat(steps.navigation.homeShown()).as("WDIO home").isTrue();
        steps.navigation.open(WdioTab.LOGIN);
        steps.login.signUp(data.validEmail(), data.validPassword(), data.validPassword() + "x");

        assertThat(steps.login.showsError("Please enter the same password")).isTrue();
    }

    @Test
    @Regression
    @TestCaseId("C2103")
    @DisplayName("[C2103] Swiping the carousel shows another card")
    void carouselSwipe() {
        assertThat(steps.navigation.homeShown()).as("WDIO home").isTrue();
        steps.navigation.open(WdioTab.SWIPE);
        String[] cards = steps.swipe.swipeCarouselLeft().split("\\|", -1);

        assertThat(cards[1]).as("card after swipe").isNotEqualTo(cards[0]);
    }

    @Test
    @Regression
    @TestCaseId("C2104")
    @DisplayName("[C2104] Scrolling down reveals the hidden logo")
    void hiddenLogo() {
        assertThat(steps.navigation.homeShown()).as("WDIO home").isTrue();
        steps.navigation.open(WdioTab.SWIPE);

        assertThat(steps.swipe.findHiddenLogo()).isTrue();
    }

    @Test
    @Regression
    @TestCaseId("C2105")
    @DisplayName("[C2105] Drag and drop solves the puzzle")
    void dragAndDrop() {
        assertThat(steps.navigation.homeShown()).as("WDIO home").isTrue();
        steps.navigation.open(WdioTab.DRAG);

        assertThat(steps.drag.solvePuzzle()).as("Congratulations shown").isTrue();
    }

    @Test
    @Regression
    @TestCaseId("C2106")
    @DisplayName("[C2106] Toggling the switch changes its hint")
    void switchToggle() {
        assertThat(steps.navigation.homeShown()).as("WDIO home").isTrue();
        steps.navigation.open(WdioTab.FORMS);
        String[] hints = steps.forms.toggleSwitch().split("\\|", -1);

        assertThat(hints[1]).isNotEqualTo(hints[0]);
    }
}
