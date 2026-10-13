package io.github.annamandzulashvili.mobile.app.tests.discovery;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.annamandzulashvili.mobile.app.enums.HomeButton;
import io.github.annamandzulashvili.mobile.app.enums.Tab;
import io.github.annamandzulashvili.mobile.app.steps.ProverbialSteps;
import io.github.annamandzulashvili.mobile.core.hooks.Discovery;
import io.github.annamandzulashvili.mobile.core.hooks.TestCaseId;
import io.github.annamandzulashvili.mobile.core.reporting.PageSourceWriter;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Not a check: walks the main screens and saves their page source to target/page-sources/, so locators
 * are pinned from real markup. Run with -Pdiscovery; never part of smoke or regression.
 */
abstract class PageSourceDiscoveryCases {

    private final ProverbialSteps steps = new ProverbialSteps();

    @Test
    @Discovery
    @TestCaseId("C0001")
    @DisplayName("[C0001] Capture page source of the main Proverbial screens")
    void capturePageSources() {
        steps.launch.launch();
        Path home = PageSourceWriter.save("home");
        steps.home.tap(HomeButton.TOAST);
        PageSourceWriter.save("toast");
        steps.tabs.open(Tab.BROWSER);
        PageSourceWriter.save("browser");
        steps.tabs.open(Tab.HOME);
        steps.home.tap(HomeButton.GEOLOCATION);
        steps.device.allowPermissionIfAsked(java.time.Duration.ofSeconds(3));
        PageSourceWriter.save("geolocation");

        assertThat(home).exists();
    }
}
