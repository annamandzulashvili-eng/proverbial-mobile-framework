package io.github.annamandzulashvili.mobile.web.models;

import io.github.annamandzulashvili.mobile.core.utils.Json;
import java.util.List;

/** Typed view of testdata/TestData.json for the web module. Public demo data only. */
public record WebTestData(String searchTerm, String noResultsTerm, String unknownEmail, String loginWarning,
                          List<RegistrationCase> invalidRegistrations) {

    private static final WebTestData INSTANCE = Json.resource("testdata/TestData.json", WebTestData.class);

    public static WebTestData get() {
        return INSTANCE;
    }
}
