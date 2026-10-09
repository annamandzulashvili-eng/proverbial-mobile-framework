package io.github.annamandzulashvili.mobile.app.models;

import io.github.annamandzulashvili.mobile.core.utils.Json;
import java.util.List;

/** Typed view of testdata/TestData.json. Public demo data only, never secrets. */
public record TestData(String browserUrl, String browserUrlHost, String gpsLocation, Wdio wdio) {

    public record Wdio(String validEmail, String validPassword, List<LoginCase> invalidLogins, String formText) {
    }

    private static final TestData INSTANCE = Json.resource("testdata/TestData.json", TestData.class);

    public static TestData get() {
        return INSTANCE;
    }
}
