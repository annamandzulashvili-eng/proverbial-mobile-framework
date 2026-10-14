package io.github.annamandzulashvili.mobile.web.steps;

import io.github.annamandzulashvili.mobile.web.steps.app.AccountSteps;
import io.github.annamandzulashvili.mobile.web.steps.app.CartSteps;
import io.github.annamandzulashvili.mobile.web.steps.app.HomeSteps;
import io.github.annamandzulashvili.mobile.web.steps.app.ProductSteps;
import io.github.annamandzulashvili.mobile.web.steps.app.SearchSteps;
import io.github.annamandzulashvili.mobile.web.steps.business.PurchaseSteps;
import io.github.annamandzulashvili.mobile.web.steps.business.RegistrationSteps;

/** Composition root of the web module's steps. */
public final class WebSteps {

    public final HomeSteps home = new HomeSteps();
    public final SearchSteps search = new SearchSteps();
    public final ProductSteps product = new ProductSteps();
    public final CartSteps cart = new CartSteps();
    public final AccountSteps account = new AccountSteps();

    public final PurchaseSteps purchase = new PurchaseSteps(search, product, cart);
    public final RegistrationSteps registration = new RegistrationSteps(account);
}
