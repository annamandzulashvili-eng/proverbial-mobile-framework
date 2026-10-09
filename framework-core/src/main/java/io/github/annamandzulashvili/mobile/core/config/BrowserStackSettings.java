package io.github.annamandzulashvili.mobile.core.config;

/** BrowserStack hub, API and session options. Credentials arrive from env or credentials.json. */
public record BrowserStackSettings(
        String hubUrl,
        String apiUrl,
        String userName,
        String accessKey,
        String projectName,
        String buildNamePattern,
        String appiumVersion,
        boolean video,
        boolean networkLogs,
        int idleTimeout,
        boolean interactiveDebugging,
        boolean debug) {

    @Override
    public String toString() {
        // Never print the key, even by accident.
        return "BrowserStackSettings[hubUrl=" + hubUrl + ", userName=" + (userName == null || userName.isBlank() ? "<unset>" : "<set>")
                + ", accessKey=" + (accessKey == null || accessKey.isBlank() ? "<unset>" : "<set>") + "]";
    }
}
