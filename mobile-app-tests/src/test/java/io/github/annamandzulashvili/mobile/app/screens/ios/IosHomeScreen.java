package io.github.annamandzulashvili.mobile.app.screens.ios;

import io.github.annamandzulashvili.mobile.app.enums.HomeButton;
import io.github.annamandzulashvili.mobile.app.screens.base.HomeScreen;
import io.github.annamandzulashvili.mobile.core.enums.Platform;
import io.github.annamandzulashvili.mobile.core.screens.ScreenFor;
import io.github.annamandzulashvili.mobile.core.selectors.Selectors;
import org.openqa.selenium.By;

@ScreenFor(Platform.IOS)
public class IosHomeScreen extends HomeScreen {

    public IosHomeScreen() {
        super(Selectors.accessibilityId(HomeButton.COLOR.id()));
    }

    @Override
    protected By button(HomeButton button) {
        return Selectors.accessibilityId(button.id());
    }
}
