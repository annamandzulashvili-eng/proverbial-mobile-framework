package io.github.annamandzulashvili.mobile.core.reporting;

import io.github.annamandzulashvili.mobile.core.browserstack.BrowserStackSession;
import io.github.annamandzulashvili.mobile.core.config.ReportingSettings;
import io.github.annamandzulashvili.mobile.core.config.RunContext;
import io.github.annamandzulashvili.mobile.core.driver.TestSession;
import io.github.annamandzulashvili.mobile.core.utils.Json;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import org.openqa.selenium.OutputType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Writes {@code <failureDumpDir>/<runId>/<test>/} with error.txt, session.json, screenshot.png and
 * page-source.xml, appends one line to the run's manifest.jsonl and attaches the same files to Allure.
 * Each step is best-effort and independent, so a dead session still leaves the error and the facts.
 */
public final class FailureDump {

    private static final Logger LOG = LoggerFactory.getLogger(FailureDump.class);

    private FailureDump() {
    }

    public static Path capture(ReportingSettings settings, String testName, Throwable error, TestSession session) {
        Path runDir = Path.of(settings.failureDumpDir(), RunContext.runId());
        Path dir = runDir.resolve(safeName(testName));
        try {
            Files.createDirectories(dir);
            String errorText = stackTrace(error);
            write(dir.resolve("error.txt"), errorText.getBytes(StandardCharsets.UTF_8));
            Allure.addAttachment("error", "text/plain", errorText);

            Map<String, Object> facts = facts(testName, error, session);
            String factsJson = Json.write(facts);
            write(dir.resolve("session.json"), factsJson.getBytes(StandardCharsets.UTF_8));
            Allure.addAttachment("session", "application/json", factsJson);

            if (session != null && settings.screenshotOnFailure()) {
                bestEffort("screenshot", () -> {
                    byte[] png = session.driver().getScreenshotAs(OutputType.BYTES);
                    write(dir.resolve("screenshot.png"), png);
                    Allure.addAttachment("screenshot", "image/png", new ByteArrayInputStream(png), "png");
                });
            }
            if (session != null && settings.pageSourceOnFailure()) {
                bestEffort("page source", () -> {
                    String source = session.driver().getPageSource();
                    write(dir.resolve("page-source.xml"), source.getBytes(StandardCharsets.UTF_8));
                    Allure.addAttachment("page source", "text/xml", source);
                });
            }
            appendManifest(runDir, Json.MAPPER.writer().without(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT).writeValueAsString(facts));
        } catch (IOException e) {
            LOG.warn("Could not write failure dump for {}: {}", testName, e.getMessage());
        }
        return dir;
    }

    static Map<String, Object> facts(String testName, Throwable error, TestSession session) {
        Map<String, Object> facts = new LinkedHashMap<>();
        facts.put("test", testName);
        facts.put("time", Instant.now().toString());
        facts.put("error", error.getClass().getSimpleName() + ": " + error.getMessage());
        if (session != null) {
            facts.put("platform", session.platform().id());
            facts.put("module", session.target().module().id());
            facts.put("device", session.device().deviceName() + " " + session.device().platformVersion());
            facts.put("sessionId", session.sessionId());
            BrowserStackSession.sessionUrl(session).ifPresent(url -> facts.put("browserStackSession", url));
        } else {
            facts.put("session", "none: the session did not start");
        }
        return facts;
    }

    static String safeName(String testName) {
        String cleaned = testName.replaceAll("[^A-Za-z0-9._-]+", "_").replaceAll("_+", "_");
        return cleaned.length() > 120 ? cleaned.substring(0, 120) : cleaned;
    }

    private static synchronized void appendManifest(Path runDir, String line) throws IOException {
        Files.writeString(runDir.resolve("manifest.jsonl"), line + System.lineSeparator(), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    private static void write(Path file, byte[] content) throws IOException {
        Files.write(file, content);
    }

    private static String stackTrace(Throwable error) {
        StringWriter out = new StringWriter();
        error.printStackTrace(new PrintWriter(out));
        return out.toString();
    }

    private interface Step {
        void run() throws IOException;
    }

    private static void bestEffort(String what, Step step) {
        try {
            step.run();
        } catch (IOException | RuntimeException e) {
            LOG.warn("Failure dump: no {} ({})", what, e.getMessage());
        }
    }
}
