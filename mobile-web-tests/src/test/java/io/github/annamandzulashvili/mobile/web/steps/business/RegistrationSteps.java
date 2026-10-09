package io.github.annamandzulashvili.mobile.web.steps.business;

import io.github.annamandzulashvili.mobile.web.models.Customer;
import io.github.annamandzulashvili.mobile.web.steps.app.AccountSteps;
import io.qameta.allure.Step;

/** Creates a fresh customer. Each call uses a new e-mail, so tests never share accounts. */
public class RegistrationSteps {

    private final AccountSteps account;

    public RegistrationSteps(AccountSteps account) {
        this.account = account;
    }

    @Step("Register a new customer")
    public Customer registerNewCustomer() {
        Customer customer = Customer.unique();
        account.register(customer, true);
        if (!account.accountCreated()) {
            throw new IllegalStateException("Registration did not reach the success page");
        }
        return customer;
    }
}
