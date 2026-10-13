package io.github.annamandzulashvili.mobile.app.architecture;

import static org.assertj.core.api.Assertions.assertThat;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import io.github.annamandzulashvili.mobile.app.screens.base.BrowserScreen;
import io.github.annamandzulashvili.mobile.app.screens.base.GeolocationScreen;
import io.github.annamandzulashvili.mobile.app.screens.base.HomeScreen;
import io.github.annamandzulashvili.mobile.app.screens.base.SpeedTestScreen;
import io.github.annamandzulashvili.mobile.app.screens.base.TabBar;
import io.github.annamandzulashvili.mobile.app.screens.base.ToastMessage;
import io.github.annamandzulashvili.mobile.core.architecture.LayerRules;
import io.github.annamandzulashvili.mobile.core.enums.Platform;
import io.github.annamandzulashvili.mobile.core.screens.ScreenFactory;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/** Runs in every build without a device: layer rules and screen wiring. */
class AppArchitectureTest {

    private static final String ROOT = "io.github.annamandzulashvili.mobile.app";
    private final JavaClasses classes = new ClassFileImporter().importPackages(ROOT);

    @Test
    void layersPointDownOnly() {
        LayerRules.forModule(ROOT).forEach(rule -> rule.check(classes));
    }

    @ParameterizedTest
    @EnumSource(Platform.class)
    void everyAbstractScreenHasExactlyOneImplementationPerPlatform(Platform platform) {
        List<Class<?>> screens = List.of(HomeScreen.class, TabBar.class, BrowserScreen.class,
                GeolocationScreen.class, SpeedTestScreen.class, ToastMessage.class);

        screens.forEach(screen -> assertThat(ScreenFactory.resolve(screen, platform).getSimpleName())
                .startsWith(platform == Platform.ANDROID ? "Android" : "Ios"));
    }
}
