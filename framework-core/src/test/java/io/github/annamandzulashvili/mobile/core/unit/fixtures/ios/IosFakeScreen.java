package io.github.annamandzulashvili.mobile.core.unit.fixtures.ios;

import io.github.annamandzulashvili.mobile.core.enums.Platform;
import io.github.annamandzulashvili.mobile.core.screens.ScreenFor;
import io.github.annamandzulashvili.mobile.core.unit.fixtures.base.FakeScreen;

@ScreenFor(Platform.IOS)
public class IosFakeScreen extends FakeScreen {
    @Override
    public String platformName() {
        return "ios";
    }
}
