package io.github.annamandzulashvili.mobile.app.screens.android.wdio;

import io.github.annamandzulashvili.mobile.core.gestures.Gestures;
import java.time.Duration;
import java.util.List;
import io.github.annamandzulashvili.mobile.core.enums.Platform;
import io.github.annamandzulashvili.mobile.core.screens.BaseScreen;
import io.github.annamandzulashvili.mobile.core.screens.ScreenFor;
import io.github.annamandzulashvili.mobile.core.selectors.Selectors;

/** Drag-and-drop puzzle: nine pieces (drag-l1..r3) onto nine targets (drop-l1..r3). */
@ScreenFor(Platform.ANDROID)
public class DragScreen extends BaseScreen {

    public static final List<String> PIECES = List.of("l1", "c1", "r1", "l2", "c2", "r2", "l3", "c3", "r3");

    public DragScreen() {
        super(Selectors.accessibilityId("Drag-drop-screen"), "WDIO drag screen");
    }

    public void dragPiece(String piece) {
        Gestures.dragTo(find(Selectors.accessibilityId("drag-" + piece)), find(Selectors.accessibilityId("drop-" + piece)));
    }

    public boolean isSolved(Duration timeout) {
        return isVisible(Selectors.textContains(Platform.ANDROID, "Congratulations"), timeout);
    }

    public void retry() {
        tap(Selectors.accessibilityId("renew"));
    }
}
