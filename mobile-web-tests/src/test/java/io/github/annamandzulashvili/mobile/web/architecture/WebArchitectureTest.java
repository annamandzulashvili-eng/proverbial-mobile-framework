package io.github.annamandzulashvili.mobile.web.architecture;

import static org.assertj.core.api.Assertions.assertThat;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import io.github.annamandzulashvili.mobile.core.architecture.LayerRules;
import io.github.annamandzulashvili.mobile.web.pages.SearchResultsPage;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class WebArchitectureTest {

    private static final String ROOT = "io.github.annamandzulashvili.mobile.web";

    @Test
    void layersPointDownOnly() {
        JavaClasses classes = new ClassFileImporter().importPackages(ROOT);
        LayerRules.forModule(ROOT).forEach(rule -> rule.check(classes));
    }

    @Test
    void pricesAreParsedFromOldAndNewPriceText() {
        assertThat(SearchResultsPage.parsePrice("$1,234.50")).isEqualByComparingTo(new BigDecimal("1234.50"));
        assertThat(SearchResultsPage.parsePrice("$122.00 $98.00")).isEqualByComparingTo(new BigDecimal("122.00"));
    }
}
