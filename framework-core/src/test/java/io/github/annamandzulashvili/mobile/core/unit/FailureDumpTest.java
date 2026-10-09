package io.github.annamandzulashvili.mobile.core.unit;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.annamandzulashvili.mobile.core.config.ReportingSettings;
import io.github.annamandzulashvili.mobile.core.reporting.FailureDump;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FailureDumpTest {

    @TempDir
    Path tmp;

    @Test
    void aFailureWithoutASessionStillLeavesTheErrorAndTheFacts() throws IOException {
        ReportingSettings settings = new ReportingSettings(tmp.toString(), true, true, tmp.resolve("url.txt").toString());

        Path dir = FailureDump.capture(settings, "[C1001] App opens", new AssertionError("Home screen is not displayed"), null);

        assertThat(dir.getFileName().toString()).isEqualTo("_C1001_App_opens");
        assertThat(Files.readString(dir.resolve("error.txt"))).contains("Home screen is not displayed");
        assertThat(Files.readString(dir.resolve("session.json"))).contains("the session did not start");
        assertThat(Files.readString(dir.getParent().resolve("manifest.jsonl"))).contains("C1001");
        assertThat(dir.resolve("screenshot.png")).doesNotExist();
    }
}
