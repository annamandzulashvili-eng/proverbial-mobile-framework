package io.github.annamandzulashvili.mobile.app.enums;

/**
 * The six buttons on the Proverbial home screen. Locator ids are taken from LambdaTest's official
 * Proverbial samples: Android resource-id suffix and iOS accessibility id are the same word.
 */
public enum HomeButton {
    COLOR("color"),
    GEOLOCATION("geoLocation"),
    TEXT("Text"),
    NOTIFICATION("notification"),
    TOAST("toast"),
    SPEED_TEST("speedTest");

    private final String id;

    HomeButton(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }
}
