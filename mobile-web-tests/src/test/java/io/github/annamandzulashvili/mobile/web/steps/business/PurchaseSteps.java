package io.github.annamandzulashvili.mobile.web.steps.business;

import io.github.annamandzulashvili.mobile.web.steps.app.CartSteps;
import io.github.annamandzulashvili.mobile.web.steps.app.ProductSteps;
import io.github.annamandzulashvili.mobile.web.steps.app.SearchSteps;
import io.qameta.allure.Step;
import java.util.List;

/** Search -> open the first result -> add to cart. Returns the added product's name. */
public class PurchaseSteps {

    private final SearchSteps search;
    private final ProductSteps product;
    private final CartSteps cart;

    public PurchaseSteps(SearchSteps search, ProductSteps product, CartSteps cart) {
        this.search = search;
        this.product = product;
        this.cart = cart;
    }

    @Step("Put the first \"{term}\" result into the cart")
    public String addFirstResultToCart(String term) {
        List<String> results = search.search(term);
        if (results.isEmpty()) {
            return "";
        }
        search.openResult(0);
        return product.addToCart();
    }

    public List<String> cartItems() {
        return cart.items();
    }
}
