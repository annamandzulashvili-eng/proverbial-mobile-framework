package io.github.annamandzulashvili.mobile.web.steps.app;

import io.github.annamandzulashvili.mobile.web.pages.CartPage;
import io.qameta.allure.Step;
import java.util.List;

public class CartSteps {

    private final CartPage cart = new CartPage();

    @Step("Read the cart")
    public List<String> items() {
        cart.open();
        return cart.itemNames();
    }

    @Step("Remove the first cart item")
    public boolean removeFirstAndCheckEmpty() {
        cart.open();
        cart.removeFirstItem();
        return cart.isEmpty();
    }
}
