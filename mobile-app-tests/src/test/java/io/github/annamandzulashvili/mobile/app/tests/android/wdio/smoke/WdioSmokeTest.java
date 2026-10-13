package io.github.annamandzulashvili.mobile.app.tests.android.wdio.smoke;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.annamandzulashvili.mobile.app.models.TestData;
import io.github.annamandzulashvili.mobile.app.enums.WdioTab;
import io.github.annamandzulashvili.mobile.app.steps.WdioSteps;
import io.github.annamandzulashvili.mobile.core.hooks.AndroidTest;
import io.github.annamandzulashvili.mobile.core.hooks.App;
import io.github.annamandzulashvili.mobile.core.hooks.Smoke;
import io.github.annamandzulashvili.mobile.core.hooks.TestCaseId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** WebdriverIO demo app: forms and login, the screens Proverbial does not have. Android only. */
@AndroidTest
@App("wdio")
class WdioSmokeTest {

    private final WdioSteps steps = new WdioSteps();
    private final TestData.Wdio data = TestData.get().wdio();

    @Test
    @Smoke
    @TestCaseId("C2001")
    @DisplayName("[C2001] Valid login shows the success alert")
    void validLogin() {
        assertThat(steps.navigation.homeShown()).as("WDIO home").isTrue();
        steps.navigation.open(WdioTab.LOGIN);
        steps.login.logIn(data.validEmail(), data.validPassword());

        assertThat(steps.login.readAndCloseAlert()).isEqualTo("Success: You are logged in!");
    }

    @Test
    @Smoke
    @TestCaseId("C2002")
    @DisplayName("[C2002] Text typed into the form is echoed back")
    void formEchoesInput() {
        assertThat(steps.navigation.homeShown()).as("WDIO home").isTrue();
        steps.navigation.open(WdioTab.FORMS);

        assertThat(steps.forms.typeAndReadBack(data.formText())).isEqualTo(data.formText());
    }
}
