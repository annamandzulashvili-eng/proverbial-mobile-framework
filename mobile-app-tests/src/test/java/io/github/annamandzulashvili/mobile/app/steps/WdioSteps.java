package io.github.annamandzulashvili.mobile.app.steps;

import io.github.annamandzulashvili.mobile.app.steps.app.wdio.DragSteps;
import io.github.annamandzulashvili.mobile.app.steps.app.wdio.FormsSteps;
import io.github.annamandzulashvili.mobile.app.steps.app.wdio.LoginSteps;
import io.github.annamandzulashvili.mobile.app.steps.app.wdio.SwipeSteps;
import io.github.annamandzulashvili.mobile.app.steps.app.wdio.WdioNavigationSteps;

/** Composition root for the WebdriverIO demo app steps. */
public final class WdioSteps {

    public final WdioNavigationSteps navigation = new WdioNavigationSteps();
    public final LoginSteps login = new LoginSteps();
    public final FormsSteps forms = new FormsSteps();
    public final SwipeSteps swipe = new SwipeSteps();
    public final DragSteps drag = new DragSteps();
}
