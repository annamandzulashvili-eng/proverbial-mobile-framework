package io.github.annamandzulashvili.mobile.app.models;

/** One row of login validation data from TestData.json. */
public record LoginCase(String name, String email, String password, String expectedError) {

    @Override
    public String toString() {
        return name;
    }
}
