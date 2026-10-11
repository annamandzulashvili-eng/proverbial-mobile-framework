package io.github.annamandzulashvili.mobile.core.utils;

import java.util.UUID;

/** Unique, readable test data. Unique per call so parallel tests never collide. */
public final class DataGenerator {

    private DataGenerator() {
    }

    public static String uniqueSuffix() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }

    public static String uniqueEmail(String prefix) {
        return prefix + "+" + uniqueSuffix() + "@example.com";
    }

    /** Ten digits, starting with 5, which passes the usual 3–32 character phone checks. */
    public static String phone() {
        long digits = Math.abs(UUID.randomUUID().getMostSignificantBits()) % 1_000_000_000L;
        return "5" + String.format("%09d", digits);
    }
}
