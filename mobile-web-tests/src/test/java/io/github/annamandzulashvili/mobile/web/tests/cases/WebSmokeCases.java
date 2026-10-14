package io.github.annamandzulashvili.mobile.web.tests.cases;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.annamandzulashvili.mobile.core.hooks.Smoke;
import io.github.annamandzulashvili.mobile.core.hooks.TestCaseId;
import io.github.annamandzulashvili.mobile.web.models.WebTestData;
import io.github.annamandzulashvili.mobile.web.steps.WebSteps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Mobile web smoke, written once; Chrome on Android and Safari on iOS run the same scenarios. */
public abstract class WebSmokeCases {

    protected final WebSteps steps = new WebSteps();
    protected final WebTestData data = WebTestData.get();

    @Test
    @Smoke
    @TestCaseId("C3001")
    @DisplayName("[C3001] Home page opens in the mobile browser")
    void homePageOpens() {
        assertThat(steps.home.openAndReadTitle()).isNotBlank();
    }

    @Test
    @Smoke
    @TestCaseId("C3002")
    @DisplayName("[C3002] Search returns matching products")
    void searchReturnsProducts() {
        assertThat(steps.search.search(data.searchTerm()))
                .isNotEmpty()
                .anySatisfy(name -> assertThat(name).containsIgnoringCase(data.searchTerm()));
    }

    @Test
    @Smoke
    @TestCaseId("C3003")
    @DisplayName("[C3003] A product added from search appears in the cart")
    void addToCart() {
        String added = steps.purchase.addFirstResultToCart(data.searchTerm());

        assertThat(added).as("add-to-cart confirmation").isNotBlank();
        assertThat(steps.purchase.cartItems()).contains(added);
    }

    @Test
    @Smoke
    @TestCaseId("C3004")
    @DisplayName("[C3004] A new customer can register")
    void registration() {
        assertThat(steps.registration.registerNewCustomer().email()).contains("@example.com");
    }
}
