package io.github.annamandzulashvili.mobile.web.pages;

import io.github.annamandzulashvili.mobile.core.selectors.Selectors;
import io.github.annamandzulashvili.mobile.core.waits.Waits;
import io.github.annamandzulashvili.mobile.web.pages.base.BasePage;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CartPage extends BasePage {

    private static final By ITEM_NAMES = Selectors.css("#content form table tbody td.text-left a");
    private static final By REMOVE = Selectors.css("#content form table tbody button.btn-danger");
    private static final By EMPTY = Selectors.xpath("//*[@id='content']//p[contains(., 'shopping cart is empty')]");

    public CartPage() {
        super(Selectors.css("#content"), "Cart page");
    }

    public void open() {
        openRoute("checkout/cart");
    }

    public List<String> itemNames() {
        if (isEmpty()) {
            return List.of();
        }
        return visibleAll(ITEM_NAMES).stream().map(WebElement::getText).map(String::trim).toList();
    }

    public void removeFirstItem() {
        tap(REMOVE);
        Waits.isVisibleWithin(EMPTY, Duration.ofSeconds(10));
    }

    public boolean isEmpty() {
        return isVisible(EMPTY, Duration.ofSeconds(3));
    }
}
