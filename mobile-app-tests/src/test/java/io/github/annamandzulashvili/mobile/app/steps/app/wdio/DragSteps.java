package io.github.annamandzulashvili.mobile.app.steps.app.wdio;

import io.github.annamandzulashvili.mobile.app.screens.android.wdio.DragScreen;
import io.qameta.allure.Step;
import java.time.Duration;

public class DragSteps {

    private final DragScreen drag = new DragScreen();

    @Step("Solve the drag-and-drop puzzle")
    public boolean solvePuzzle() {
        drag.waitUntilDisplayed();
        DragScreen.PIECES.forEach(drag::dragPiece);
        return drag.isSolved(Duration.ofSeconds(10));
    }
}
