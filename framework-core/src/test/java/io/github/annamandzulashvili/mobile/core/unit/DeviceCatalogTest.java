package io.github.annamandzulashvili.mobile.core.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.annamandzulashvili.mobile.core.config.Settings;
import io.github.annamandzulashvili.mobile.core.driver.capabilities.DeviceCatalog;
import io.github.annamandzulashvili.mobile.core.enums.DeviceType;
import io.github.annamandzulashvili.mobile.core.enums.Platform;
import io.github.annamandzulashvili.mobile.core.enums.RunOn;
import java.nio.file.Path;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class DeviceCatalogTest {

    @TempDir
    Path tmp;

    @Test
    void defaultKeysByPlatformAndFormFactor() {
        assertThat(DeviceCatalog.defaultKey(Platform.ANDROID, DeviceType.PHONE)).isEqualTo("androidPhone");
        assertThat(DeviceCatalog.defaultKey(Platform.ANDROID, DeviceType.TABLET)).isEqualTo("androidTablet");
        assertThat(DeviceCatalog.defaultKey(Platform.IOS, DeviceType.PHONE)).isEqualTo("iphone");
        assertThat(DeviceCatalog.defaultKey(Platform.IOS, DeviceType.TABLET)).isEqualTo("ipad");
    }

    @Test
    void explicitDeviceWins() {
        Settings settings = TestSettings.load(Map.of(), Map.of("device", "androidTablet"), tmp);

        assertThat(DeviceCatalog.resolve(settings, Platform.ANDROID, DeviceType.PHONE, RunOn.BROWSERSTACK).type()).isEqualTo("tablet");
    }

    @Test
    void deviceOfTheOtherPlatformIsRejected() {
        Settings settings = TestSettings.load(Map.of(), Map.of("device", "iphone"), tmp);

        assertThatThrownBy(() -> DeviceCatalog.resolve(settings, Platform.ANDROID, DeviceType.PHONE, RunOn.BROWSERSTACK))
                .hasMessageContaining("is ios but the test runs on android");
    }

    @Test
    void unknownDeviceListsTheKnownOnes() {
        Settings settings = TestSettings.load(Map.of(), Map.of("device", "nokia"), tmp);

        assertThatThrownBy(() -> DeviceCatalog.resolve(settings, Platform.ANDROID, DeviceType.PHONE, RunOn.BROWSERSTACK))
                .hasMessageContaining("Unknown device 'nokia'")
                .hasMessageContaining("androidPhone");
    }

    @Test
    void localRunsUseTheEmulator() {
        Settings settings = TestSettings.load(Map.of(), Map.of(), tmp);

        assertThat(DeviceCatalog.resolve(settings, Platform.ANDROID, DeviceType.PHONE, RunOn.LOCAL).deviceName()).isEqualTo("emulator-5554");
    }
}
