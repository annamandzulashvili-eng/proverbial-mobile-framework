package io.github.annamandzulashvili.mobile.core.config;

/** Run selectors: what to run, where and how wide. */
public record RunSettings(
        String platform,
        String runOn,
        String device,
        String suite,
        int threads,
        String app,
        String appPath,
        String env,
        String branch,
        String buildNumber,
        String buildUrl) {
}
