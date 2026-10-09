package io.github.annamandzulashvili.mobile.core.config;

import java.time.Duration;

/** Every timeout lives here, nowhere else. */
public record TimeoutsSettings(
        int conditionSeconds,
        int pollingMillis,
        int commandSeconds,
        int webviewContextSeconds,
        int appUploadSeconds,
        int newCommandTimeoutSeconds) {

    public Duration condition() {
        return Duration.ofSeconds(conditionSeconds);
    }

    public Duration polling() {
        return Duration.ofMillis(pollingMillis);
    }

    public Duration command() {
        return Duration.ofSeconds(commandSeconds);
    }

    public Duration webviewContext() {
        return Duration.ofSeconds(webviewContextSeconds);
    }

    public Duration appUpload() {
        return Duration.ofSeconds(appUploadSeconds);
    }
}
