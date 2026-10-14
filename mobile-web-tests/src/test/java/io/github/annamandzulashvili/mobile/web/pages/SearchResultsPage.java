package io.github.annamandzulashvili.mobile.web.pages;

import io.github.annamandzulashvili.mobile.core.selectors.Selectors;
import io.github.annamandzulashvili.mobile.web.pages.base.BasePage;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/** OpenCart search results: product cards, prices, empty-result message. */
public class SearchResultsPage extends BasePage {

    private static final By PRODUCT_NAMES = Selectors.css(".product-thumb h4 a");
    private static final By PRICES = Selectors.css(".product-thumb .price-new, .product-thumb .price");
    private static final By NO_RESULTS = Selectors.xpath("//*[@id='content']//p[contains(., 'no product that matches')]");

    public SearchResultsPage() {
        super(Selectors.css("#content"), "Search results page");
    }

    public void searchFor(String term) {
        openRoute("product/search&search=" + URLEncoder.encode(term, StandardCharsets.UTF_8));
    }

    /** Search sorted by price, lowest first, through the same URL parameters the sort drop-down uses. */
    public void searchSortedByPriceAscending(String term) {
        openRoute("product/search&search=" + URLEncoder.encode(term, StandardCharsets.UTF_8) + "&sort=p.price&order=ASC");
    }

    public List<String> productNames() {
        return visibleAll(PRODUCT_NAMES).stream().map(WebElement::getText).map(String::trim).toList();
    }

    public List<BigDecimal> prices() {
        return visibleAll(PRICES).stream().map(WebElement::getText).map(SearchResultsPage::parsePrice).toList();
    }

    public boolean showsNoResults() {
        return isVisible(NO_RESULTS, Duration.ofSeconds(5));
    }

    public void openProduct(int index) {
        visibleAll(PRODUCT_NAMES).get(index).click();
    }

    /** "$1,234.00" or "$122.00 $98.00" (old/new) -> the first amount. */
    public static BigDecimal parsePrice(String text) {
        String first = text.trim().split("\\s+")[0];
        return new BigDecimal(first.replaceAll("[^0-9.]", ""));
    }
}
