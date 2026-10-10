package io.github.annamandzulashvili.mobile.core.api;

import io.github.annamandzulashvili.mobile.core.config.BrowserStackSettings;
import io.github.annamandzulashvili.mobile.core.utils.Json;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

/** Minimal BrowserStack REST client (App Automate upload). Java's built-in HttpClient, no extra dependency. */
public final class BrowserStackApi {

    private final BrowserStackSettings settings;
    private final HttpClient http;

    public BrowserStackApi(BrowserStackSettings settings) {
        this.settings = settings;
        this.http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(30)).build();
    }

    /** Uploads an .apk/.ipa and returns its {@code bs://} url. The custom id lets tests refer to the latest upload. */
    public String uploadApp(Path file, String customId, Duration timeout) {
        String boundary = "----pmf" + UUID.randomUUID();
        try {
            List<byte[]> parts = new ArrayList<>();
            parts.add(("--" + boundary + "\r\nContent-Disposition: form-data; name=\"file\"; filename=\"" + file.getFileName()
                    + "\"\r\nContent-Type: application/octet-stream\r\n\r\n").getBytes(StandardCharsets.UTF_8));
            parts.add(Files.readAllBytes(file));
            parts.add(("\r\n--" + boundary + "\r\nContent-Disposition: form-data; name=\"custom_id\"\r\n\r\n" + customId
                    + "\r\n--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));
            HttpRequest request = HttpRequest.newBuilder(URI.create(settings.apiUrl() + "/app-automate/upload"))
                    .timeout(timeout)
                    .header("Authorization", basicAuth())
                    .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                    .POST(HttpRequest.BodyPublishers.ofByteArrays(parts))
                    .build();
            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() / 100 != 2) {
                throw new IllegalStateException("App upload failed with HTTP " + response.statusCode() + ": " + response.body());
            }
            return Json.read(response.body()).path("app_url").asText();
        } catch (IOException e) {
            throw new IllegalStateException("App upload failed: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("App upload interrupted", e);
        }
    }

    private String basicAuth() {
        String token = settings.userName() + ":" + settings.accessKey();
        return "Basic " + Base64.getEncoder().encodeToString(token.getBytes(StandardCharsets.UTF_8));
    }
}
