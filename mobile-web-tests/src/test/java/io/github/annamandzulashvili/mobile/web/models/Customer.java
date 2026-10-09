package io.github.annamandzulashvili.mobile.web.models;

import io.github.annamandzulashvili.mobile.core.utils.DataGenerator;

/** Registration data. {@link #unique()} gives a new e-mail per call so parallel runs never collide. */
public record Customer(String firstName, String lastName, String email, String telephone, String password, String confirmPassword) {

    public static Customer unique() {
        return new Customer("Ana", "Portfolio", DataGenerator.uniqueEmail("pmf"), DataGenerator.phone(), "Portfolio123", "Portfolio123");
    }

    public Customer withFirstName(String value) {
        return new Customer(value, lastName, email, telephone, password, confirmPassword);
    }

    public Customer withConfirmPassword(String value) {
        return new Customer(firstName, lastName, email, telephone, password, value);
    }
}
