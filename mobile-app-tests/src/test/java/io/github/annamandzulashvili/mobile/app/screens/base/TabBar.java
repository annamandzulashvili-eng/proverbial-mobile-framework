package io.github.annamandzulashvili.mobile.app.screens.base;

import io.github.annamandzulashvili.mobile.app.enums.Tab;
import io.github.annamandzulashvili.mobile.core.screens.BaseScreen;
import org.openqa.selenium.By;

/** Bottom tab bar. Android shows Browser|Home, iOS shows Home|Browser, so tabs are found by name, never by index. */
public abstract class TabBar extends BaseScreen {

    protected TabBar(By container) {
        super(container, "Tab bar");
    }

    protected abstract By tab(Tab tab);

    public void open(Tab tab) {
        tap(tab(tab));
    }
}
