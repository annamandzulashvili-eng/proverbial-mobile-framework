package io.github.annamandzulashvili.mobile.core.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.annamandzulashvili.mobile.core.enums.Platform;
import io.github.annamandzulashvili.mobile.core.screens.ScreenFactory;
import io.github.annamandzulashvili.mobile.core.unit.fixtures.android.AndroidFakeScreen;
import io.github.annamandzulashvili.mobile.core.unit.fixtures.base.FakeScreen;
import io.github.annamandzulashvili.mobile.core.unit.fixtures.base.OrphanScreen;
import org.junit.jupiter.api.Test;

class ScreenFactoryTest {

    @Test
    void returnsThePlatformImplementation() {
        assertThat(ScreenFactory.get(FakeScreen.class, Platform.ANDROID).platformName()).isEqualTo("android");
        assertThat(ScreenFactory.get(FakeScreen.class, Platform.IOS).platformName()).isEqualTo("ios");
    }

    @Test
    void aConcreteClassIsReturnedAsIs() {
        assertThat(ScreenFactory.resolve(AndroidFakeScreen.class, Platform.IOS)).isEqualTo(AndroidFakeScreen.class);
    }

    @Test
    void missingImplementationNamesTheScreenAndPlatform() {
        assertThatThrownBy(() -> ScreenFactory.resolve(OrphanScreen.class, Platform.IOS))
                .hasMessageContaining("Expected exactly one ios implementation of OrphanScreen")
                .hasMessageContaining("found 0");
    }
}
