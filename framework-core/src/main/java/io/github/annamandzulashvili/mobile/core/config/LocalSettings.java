package io.github.annamandzulashvili.mobile.core.config;

/** Local Appium server and emulator/simulator settings. */
public record LocalSettings(
        String appiumUrl,
        String androidDeviceName,
        String androidPlatformVersion,
        String iosDeviceName,
        String iosPlatformVersion) {
}
