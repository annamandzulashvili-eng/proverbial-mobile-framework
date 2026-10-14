package io.github.annamandzulashvili.mobile.web.steps.app;

import io.github.annamandzulashvili.mobile.web.pages.ProductPage;
import io.qameta.allure.Step;

public class ProductSteps {

    private final ProductPage product = new ProductPage();

    /** Adds the open product to the cart and returns its name, or empty when no confirmation appeared. */
    @Step("Add the open product to the cart")
    public String addToCart() {
        product.waitUntilDisplayed();
        String name = product.name();
        product.addToCart();
        return product.confirmsAddedToCart() ? name : "";
    }
}
