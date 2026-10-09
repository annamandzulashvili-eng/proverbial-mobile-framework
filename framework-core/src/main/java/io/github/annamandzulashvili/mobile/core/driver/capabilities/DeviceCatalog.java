package io.github.annamandzulashvili.mobile.core.driver.capabilities;

import io.github.annamandzulashvili.mobile.core.config.ConfigException;
import io.github.annamandzulashvili.mobile.core.config.DeviceProfile;
import io.github.annamandzulashvili.mobile.core.config.LocalSettings;
import io.github.annamandzulashvili.mobile.core.config.Settings;
import io.github.annamandzulashvili.mobile.core.enums.DeviceType;
import io.github.annamandzulashvili.mobile.core.enums.Platform;
import io.github.annamandzulashvili.mobile.core.enums.RunOn;

/**
 * Maps a logical device to a concrete one. Order: explicit {@code -Ddevice=<key>}, then the
 * default key for platform and form factor ({@code androidPhone}, {@code androidTablet},
 * {@code iphone}, {@code ipad}). Local runs use the emulator/simulator from localSettings.json.
 */
public final class DeviceCatalog {

    private DeviceCatalog() {
    }

    public static String defaultKey(Platform platform, DeviceType type) {
        return switch (platform) {
            case ANDROID -> type == DeviceType.TABLET ? "androidTablet" : "androidPhone";
            case IOS -> type == DeviceType.TABLET ? "ipad" : "iphone";
        };
    }

    public static DeviceProfile resolve(Settings settings, Platform platform, DeviceType type, RunOn runOn) {
        if (runOn == RunOn.LOCAL) {
            LocalSettings local = settings.local();
            return platform == Platform.ANDROID
                    ? new DeviceProfile("android", "phone", local.androidDeviceName(), local.androidPlatformVersion())
                    : new DeviceProfile("ios", "phone", local.iosDeviceName(), local.iosPlatformVersion());
        }
        String requested = settings.run().device();
        String key = requested == null || requested.isBlank() ? defaultKey(platform, type) : requested;
        DeviceProfile device = settings.devices() == null ? null : settings.devices().get(key);
        if (device == null) {
            throw new ConfigException("Unknown device '" + key + "'. Known: " + (settings.devices() == null ? "none" : settings.devices().keySet()));
        }
        if (!platform.id().equalsIgnoreCase(device.platform())) {
            throw new ConfigException("Device '" + key + "' is " + device.platform() + " but the test runs on " + platform.id());
        }
        return device;
    }
}
