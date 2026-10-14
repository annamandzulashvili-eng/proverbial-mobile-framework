package io.github.annamandzulashvili.mobile.web.tests.cases;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.annamandzulashvili.mobile.core.hooks.Regression;
import io.github.annamandzulashvili.mobile.core.hooks.TestCaseId;
import io.github.annamandzulashvili.mobile.web.models.Customer;
import io.github.annamandzulashvili.mobile.web.models.RegistrationCase;
import io.github.annamandzulashvili.mobile.web.models.WebTestData;
import io.github.annamandzulashvili.mobile.web.steps.WebSteps;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public abstract class WebRegressionCases {

    protected final WebSteps steps = new WebSteps();
    protected final WebTestData data = WebTestData.get();

    static Stream<RegistrationCase> invalidRegistrations() {
        return WebTestData.get().invalidRegistrations().stream();
    }

    @Test
    @Regression
    @TestCaseId("C3101")
    @DisplayName("[C3101] A search without matches says so")
    void noResults() {
        assertThat(steps.search.searchShowsNoResults(data.noResultsTerm())).isTrue();
    }

    @Test
    @Regression
    @TestCaseId("C3102")
    @DisplayName("[C3102] Login with an unknown e-mail shows a warning")
    void loginWarning() {
        steps.account.logIn(data.unknownEmail(), "WrongPassword1");

        assertThat(steps.account.loginShowsWarning(data.loginWarning())).isTrue();
    }

    @ParameterizedTest(name = "[C3103] Registration validation: {0}")
    @MethodSource("invalidRegistrations")
    @Regression
    @TestCaseId("C3103")
    void registrationValidation(RegistrationCase registrationCase) {
        steps.account.register(registrationCase.apply(Customer.unique()), registrationCase.acceptPrivacyPolicy());

        assertThat(steps.account.registrationErrors()).anySatisfy(error -> assertThat(error).contains(registrationCase.expectedError()));
    }

    @Test
    @Regression
    @TestCaseId("C3104")
    @DisplayName("[C3104] Removing the only cart item empties the cart")
    void removeFromCart() {
        assertThat(steps.purchase.addFirstResultToCart(data.searchTerm())).isNotBlank();

        assertThat(steps.cart.removeFirstAndCheckEmpty()).isTrue();
    }

    @Test
    @Regression
    @TestCaseId("C3105")
    @DisplayName("[C3105] A registered customer can log out")
    void logout() {
        steps.registration.registerNewCustomer();

        assertThat(steps.account.logOut()).isTrue();
    }

    @Test
    @Regression
    @TestCaseId("C3106")
    @DisplayName("[C3106] Sorting by price puts the cheapest product first")
    void sortByPrice() {
        List<BigDecimal> prices = steps.search.pricesSortedAscending(data.searchTerm());

        assertThat(prices).isNotEmpty().isSorted();
    }
}
