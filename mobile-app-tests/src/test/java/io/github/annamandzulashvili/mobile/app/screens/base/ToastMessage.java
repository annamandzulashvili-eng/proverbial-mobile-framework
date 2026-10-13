package io.github.annamandzulashvili.mobile.app.screens.base;

import io.github.annamandzulashvili.mobile.core.screens.BaseScreen;
import java.time.Duration;
import org.openqa.selenium.By;

/** Short message shown by the Toast button. Android has a real Toast; iOS draws its own view. */
public abstract class ToastMessage extends BaseScreen {

    protected ToastMessage(By container) {
        super(container, "Toast message");
    }

    public boolean appears(Duration timeout) {
        return isDisplayedWithin(timeout);
    }
}
