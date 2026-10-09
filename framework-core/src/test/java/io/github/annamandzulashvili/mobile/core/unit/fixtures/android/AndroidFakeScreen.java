package io.github.annamandzulashvili.mobile.core.unit.fixtures.android;

import io.github.annamandzulashvili.mobile.core.enums.Platform;
import io.github.annamandzulashvili.mobile.core.screens.ScreenFor;
import io.github.annamandzulashvili.mobile.core.unit.fixtures.base.FakeScreen;

@ScreenFor(Platform.ANDROID)
public class AndroidFakeScreen extends FakeScreen {
    @Override
    public String platformName() {
        return "android";
    }
}
