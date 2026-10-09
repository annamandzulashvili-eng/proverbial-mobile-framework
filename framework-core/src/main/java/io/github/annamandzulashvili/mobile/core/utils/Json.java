package io.github.annamandzulashvili.mobile.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

/** One shared ObjectMapper and small helpers around it. */
public final class Json {

    public static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .enable(SerializationFeature.INDENT_OUTPUT);

    private Json() {
    }

    /** A JSON string literal, quotes included. Used to put text safely into executor scripts. */
    public static String quote(String text) {
        try {
            return MAPPER.writeValueAsString(text == null ? "" : text);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String write(Object value) {
        try {
            return MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    public static JsonNode read(String json) {
        try {
            return MAPPER.readTree(json);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Not valid JSON: " + e.getOriginalMessage(), e);
        }
    }

    /** Reads a classpath resource, for example {@code testdata/TestData.json}, into a type. */
    public static <T> T resource(String path, Class<T> type) {
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            if (in == null) {
                throw new IllegalArgumentException("Resource not found: " + path);
            }
            return MAPPER.readValue(in, type);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read " + path, e);
        }
    }
}
