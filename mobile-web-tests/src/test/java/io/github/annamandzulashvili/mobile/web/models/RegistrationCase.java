package io.github.annamandzulashvili.mobile.web.models;

/** One invalid registration from TestData.json: what to break and which error must appear. */
public record RegistrationCase(String name, String firstName, String confirmPassword, boolean acceptPrivacyPolicy, String expectedError) {

    public Customer apply(Customer valid) {
        Customer customer = valid;
        if (firstName != null) {
            customer = customer.withFirstName(firstName);
        }
        if (confirmPassword != null) {
            customer = customer.withConfirmPassword(confirmPassword);
        }
        return customer;
    }

    @Override
    public String toString() {
        return name;
    }
}
