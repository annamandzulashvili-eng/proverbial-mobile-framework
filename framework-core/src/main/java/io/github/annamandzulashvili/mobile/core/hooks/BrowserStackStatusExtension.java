package io.github.annamandzulashvili.mobile.core.hooks;

import io.github.annamandzulashvili.mobile.core.browserstack.BrowserStackSession;
import io.github.annamandzulashvili.mobile.core.driver.DriverManager;
import io.github.annamandzulashvili.mobile.core.reporting.BuildUrlFile;
import java.util.Optional;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.opentest4j.TestAbortedException;

/**
 * Writes the test result into the BrowserStack session. A skipped (aborted) test is annotated, not marked
 * failed, so the dashboard's red count stays honest.
 */
public class BrowserStackStatusExtension implements BeforeEachCallback, AfterEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        DriverManager.currentIfAny().ifPresent(session -> {
            BrowserStackSession.annotate(session, "Test started: " + context.getDisplayName(), "info");
            BrowserStackSession.buildUrl(session).ifPresent(BuildUrlFile::writeOnce);
        });
    }

    @Override
    public void afterEach(ExtensionContext context) {
        DriverManager.currentIfAny().ifPresent(session -> {
            Optional<Throwable> error = context.getExecutionException();
            if (error.isEmpty()) {
                BrowserStackSession.markPassed(session);
            } else if (error.get() instanceof TestAbortedException) {
                BrowserStackSession.annotate(session, "Skipped: " + error.get().getMessage(), "warn");
            } else {
                BrowserStackSession.markFailed(session, String.valueOf(error.get().getMessage()));
            }
        });
    }
}
