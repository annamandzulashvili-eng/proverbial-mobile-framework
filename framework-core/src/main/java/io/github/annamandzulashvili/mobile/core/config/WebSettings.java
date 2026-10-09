package io.github.annamandzulashvili.mobile.core.config;

import java.util.Map;

/** Mobile browser target: site under test and the browser per platform. */
public record WebSettings(String baseUrl, Map<String, String> browsers) {
}
