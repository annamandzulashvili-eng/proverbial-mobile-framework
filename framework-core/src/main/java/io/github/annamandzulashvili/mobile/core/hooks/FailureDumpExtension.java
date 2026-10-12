package io.github.annamandzulashvili.mobile.core.hooks;

import io.github.annamandzulashvili.mobile.core.config.RunContext;
import io.github.annamandzulashvili.mobile.core.driver.DriverManager;
import io.github.annamandzulashvili.mobile.core.reporting.FailureDump;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.opentest4j.TestAbortedException;

/** On a real failure (not a skip) collects screenshot, page source and session facts. Nothing on green tests. */
public class FailureDumpExtension implements AfterEachCallback {

    @Override
    public void afterEach(ExtensionContext context) {
        context.getExecutionException()
                .filter(error -> !(error instanceof TestAbortedException))
                .ifPresent(error -> FailureDump.capture(
                        RunContext.settings().reporting(),
                        context.getDisplayName(),
                        error,
                        DriverManager.currentIfAny().orElse(null)));
    }
}
