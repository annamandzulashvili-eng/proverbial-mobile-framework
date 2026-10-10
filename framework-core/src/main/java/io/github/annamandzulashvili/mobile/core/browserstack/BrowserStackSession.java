package io.github.annamandzulashvili.mobile.core.browserstack;

import io.github.annamandzulashvili.mobile.core.config.RunContext;
import io.github.annamandzulashvili.mobile.core.config.Settings;
import io.github.annamandzulashvili.mobile.core.driver.TestSession;
import io.github.annamandzulashvili.mobile.core.enums.RunOn;
import io.github.annamandzulashvili.mobile.core.utils.Json;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BrowserStack executor commands (from browserStackScripts.json). Every call is best-effort:
 * reporting must never turn a green test red.
 */
public final class BrowserStackSession {

    private static final Logger LOG = LoggerFactory.getLogger(BrowserStackSession.class);

    private BrowserStackSession() {
    }

    public static void markPassed(TestSession session) {
        setStatus(session, "passed", "");
    }

    public static void markFailed(TestSession session, String reason) {
        setStatus(session, "failed", reason);
    }

    public static void annotate(TestSession session, String text, String level) {
        run(session, String.format(settings().script("annotate"), Json.quote(text), level));
    }

    /** Public build URL from getSessionDetails, used for the CI build link. */
    public static Optional<String> buildUrl(TestSession session) {
        return details(session, "build_url");
    }

    public static Optional<String> sessionUrl(TestSession session) {
        return details(session, "public_url");
    }

    private static Optional<String> details(TestSession session, String field) {
        if (!isCloud()) {
            return Optional.empty();
        }
        try {
            Object result = session.driver().executeScript(settings().script("getSessionDetails"));
            String value = Json.read(String.valueOf(result)).path(field).asText("");
            return value.isBlank() ? Optional.empty() : Optional.of(value);
        } catch (RuntimeException e) {
            LOG.debug("Could not read BrowserStack session details: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private static void setStatus(TestSession session, String status, String reason) {
        String shortReason = reason == null ? "" : reason.length() > 250 ? reason.substring(0, 250) : reason;
        run(session, String.format(settings().script("setSessionStatus"), status, Json.quote(shortReason)));
    }

    private static void run(TestSession session, String script) {
        if (!isCloud()) {
            return;
        }
        try {
            session.driver().executeScript(script);
        } catch (RuntimeException e) {
            LOG.warn("BrowserStack executor call failed: {}", e.getMessage());
        }
    }

    private static boolean isCloud() {
        return settings().runOn() == RunOn.BROWSERSTACK;
    }

    private static Settings settings() {
        return RunContext.settings();
    }
}
