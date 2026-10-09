package io.github.annamandzulashvili.mobile.core.config;

/** What a failure leaves behind. */
public record ReportingSettings(
        String failureDumpDir,
        boolean screenshotOnFailure,
        boolean pageSourceOnFailure,
        String buildUrlFile) {
}
