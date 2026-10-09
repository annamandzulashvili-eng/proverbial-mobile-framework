package io.github.annamandzulashvili.mobile.core.reporting;

import io.github.annamandzulashvili.mobile.core.driver.DriverManager;
import io.github.annamandzulashvili.mobile.core.driver.TestSession;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Saves the current page source to {@code target/page-sources/<platform>-<name>.xml}. Used by the discovery
 * run to pin locators from real markup instead of guessing them at runtime.
 */
public final class PageSourceWriter {

    private PageSourceWriter() {
    }

    public static Path save(String name) {
        TestSession session = DriverManager.current();
        Path file = Path.of("target", "page-sources", session.platform().id() + "-" + name + ".xml");
        try {
            Files.createDirectories(file.getParent());
            Files.writeString(file, session.driver().getPageSource());
            return file;
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot write " + file, e);
        }
    }
}
