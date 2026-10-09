package io.github.annamandzulashvili.mobile.core.config;

/**
 * One app build under test. The key in "apps" is {@code <app>-<platform>}, for example
 * {@code proverbial-android}.
 */
public record AppProfile(
        String platform,
        String browserStackApp,
        String localAppPath,
        String appPackage,
        String appActivity,
        String bundleId) {
}
