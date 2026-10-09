package io.github.annamandzulashvili.mobile.app.steps;

import io.github.annamandzulashvili.mobile.app.steps.app.BrowserSteps;
import io.github.annamandzulashvili.mobile.app.steps.app.ExternalScreenSteps;
import io.github.annamandzulashvili.mobile.app.steps.app.HomeSteps;
import io.github.annamandzulashvili.mobile.app.steps.app.NotificationSteps;
import io.github.annamandzulashvili.mobile.app.steps.app.TabBarSteps;
import io.github.annamandzulashvili.mobile.app.steps.business.AppLaunchSteps;
import io.github.annamandzulashvili.mobile.app.steps.business.BrowseWebSteps;
import io.github.annamandzulashvili.mobile.app.steps.business.OpenAndReturnSteps;
import io.github.annamandzulashvili.mobile.app.steps.device.DeviceSteps;

/**
 * Composition root: the one place that creates step objects and wires business steps to the app steps
 * they use. Tests hold one instance; step classes never create each other.
 */
public final class ProverbialSteps {

    public final HomeSteps home = new HomeSteps();
    public final TabBarSteps tabs = new TabBarSteps();
    public final BrowserSteps browser = new BrowserSteps();
    public final ExternalScreenSteps external = new ExternalScreenSteps();
    public final NotificationSteps notifications = new NotificationSteps();
    public final DeviceSteps device = new DeviceSteps();

    public final AppLaunchSteps launch = new AppLaunchSteps(home, device);
    public final BrowseWebSteps browse = new BrowseWebSteps(tabs, browser);
    public final OpenAndReturnSteps openAndReturn = new OpenAndReturnSteps(home, external, device);
}
