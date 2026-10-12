package io.github.annamandzulashvili.mobile.core.reporting;

import io.github.annamandzulashvili.mobile.core.config.RunContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Writes the BrowserStack build URL to a file once per run. CI reads that file for the build description
 * and the chat message, so no log needs to be grepped.
 */
public final class BuildUrlFile {

    private static final Logger LOG = LoggerFactory.getLogger(BuildUrlFile.class);
    private static final AtomicBoolean WRITTEN = new AtomicBoolean();

    private BuildUrlFile() {
    }

    public static void writeOnce(String url) {
        if (!WRITTEN.compareAndSet(false, true)) {
            return;
        }
        Path file = Path.of(RunContext.settings().reporting().buildUrlFile());
        try {
            Files.createDirectories(file.toAbsolutePath().getParent());
            Files.writeString(file, url);
            LOG.info("BrowserStack build: {}", url);
        } catch (IOException e) {
            LOG.warn("Could not write {}: {}", file, e.getMessage());
        }
    }
}
