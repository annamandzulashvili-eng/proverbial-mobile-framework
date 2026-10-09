package io.github.annamandzulashvili.mobile.app.screens.android;

import io.github.annamandzulashvili.mobile.app.enums.HomeButton;
import io.github.annamandzulashvili.mobile.app.screens.base.HomeScreen;
import io.github.annamandzulashvili.mobile.core.enums.Platform;
import io.github.annamandzulashvili.mobile.core.screens.ScreenFor;
import io.github.annamandzulashvili.mobile.core.selectors.Selectors;
import org.openqa.selenium.By;

@ScreenFor(Platform.ANDROID)
public class AndroidHomeScreen extends HomeScreen {

    static final String ID = "com.lambdatest.proverbial:id/";

    public AndroidHomeScreen() {
        super(Selectors.id(ID + HomeButton.COLOR.id()));
    }

    @Override
    protected By button(HomeButton button) {
        return Selectors.id(ID + button.id());
    }
}
