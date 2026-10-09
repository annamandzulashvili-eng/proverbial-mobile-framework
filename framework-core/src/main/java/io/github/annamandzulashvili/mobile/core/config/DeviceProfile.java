package io.github.annamandzulashvili.mobile.core.config;

/** One logical device from devicesSettings.json (androidPhone, androidTablet, iphone, ipad). */
public record DeviceProfile(String platform, String type, String deviceName, String platformVersion) {
}
