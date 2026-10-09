package io.github.annamandzulashvili.mobile.core.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Run-level state: the settings, loaded once per JVM, and a run id used for evidence folders.
 * Read-only after the first access, so it is safe to share between parallel tests.
 */
public final class RunContext {

    private static volatile Settings settings;
    private static final String RUN_ID = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));

    private RunContext() {
    }

    public static Settings settings() {
        Settings local = settings;
        if (local == null) {
            synchronized (RunContext.class) {
                local = settings;
                if (local == null) {
                    local = ConfigLoader.load();
                    settings = local;
                }
            }
        }
        return local;
    }

    public static String runId() {
        return RUN_ID;
    }
}
