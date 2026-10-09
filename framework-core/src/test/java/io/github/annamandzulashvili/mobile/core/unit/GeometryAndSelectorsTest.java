package io.github.annamandzulashvili.mobile.core.unit;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.annamandzulashvili.mobile.core.gestures.Direction;
import io.github.annamandzulashvili.mobile.core.gestures.Gestures;
import io.github.annamandzulashvili.mobile.core.selectors.Selectors;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;

class GeometryAndSelectorsTest {

    @Test
    void swipeUpMovesTheFingerFromBottomToTop() {
        Point[] path = Gestures.path(new Rectangle(0, 0, 1000, 500), Direction.UP, 0.5);

        assertThat(path[0]).isEqualTo(new Point(250, 750));
        assertThat(path[1]).isEqualTo(new Point(250, 250));
    }

    @Test
    void swipeLeftMovesTheFingerFromRightToLeft() {
        Point[] path = Gestures.path(new Rectangle(0, 100, 200, 400), Direction.LEFT, 0.8);

        assertThat(path[0].x).isGreaterThan(path[1].x);
        assertThat(path[0].y).isEqualTo(path[1].y).isEqualTo(200);
    }

    @Test
    void userTextIsSafeInsideAUiSelectorPattern() {
        assertThat(Selectors.regexSafe("Hello! (World)")).isEqualTo("Hello. .World.");
    }
}
