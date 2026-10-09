package io.github.annamandzulashvili.mobile.web.pages;

import io.github.annamandzulashvili.mobile.core.selectors.Selectors;
import io.github.annamandzulashvili.mobile.web.pages.base.BasePage;
import java.time.Duration;
import org.openqa.selenium.By;

public class ProductPage extends BasePage {

    private static final By NAME = Selectors.css("#content h1");
    private static final By ADD_TO_CART = Selectors.css("#button-cart, button.button-cart");
    private static final By ADDED = Selectors.xpath("//*[contains(@class,'alert-success') or contains(@class,'toast')][contains(., 'shopping cart')]");

    public ProductPage() {
        super(Selectors.css("#content h1"), "Product page");
    }

    public String name() {
        return text(NAME).trim();
    }

    public void addToCart() {
        tap(ADD_TO_CART);
    }

    public boolean confirmsAddedToCart() {
        return isVisible(ADDED, Duration.ofSeconds(10));
    }
}
