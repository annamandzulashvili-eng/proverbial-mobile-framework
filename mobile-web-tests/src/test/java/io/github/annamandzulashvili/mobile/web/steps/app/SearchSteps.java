package io.github.annamandzulashvili.mobile.web.steps.app;

import io.github.annamandzulashvili.mobile.web.pages.SearchResultsPage;
import io.qameta.allure.Step;
import java.math.BigDecimal;
import java.util.List;

public class SearchSteps {

    private final SearchResultsPage results = new SearchResultsPage();

    @Step("Search for \"{term}\"")
    public List<String> search(String term) {
        results.searchFor(term);
        return results.showsNoResults() ? List.of() : results.productNames();
    }

    @Step("Search for \"{term}\" and get the no-results message")
    public boolean searchShowsNoResults(String term) {
        results.searchFor(term);
        return results.showsNoResults();
    }

    @Step("Search for \"{term}\" sorted by price, low to high")
    public List<BigDecimal> pricesSortedAscending(String term) {
        results.searchSortedByPriceAscending(term);
        return results.prices();
    }

    @Step("Open search result #{index}")
    public void openResult(int index) {
        results.openProduct(index);
    }
}
