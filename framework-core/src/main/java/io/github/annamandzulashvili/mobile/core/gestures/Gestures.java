package io.github.annamandzulashvili.mobile.core.gestures;

import io.github.annamandzulashvili.mobile.core.driver.DriverManager;
import io.github.annamandzulashvili.mobile.core.waits.Waits;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

/** Touch gestures with W3C Actions only (TouchAction is deprecated and not used). */
public final class Gestures {

    private static final Duration DEFAULT_SWIPE = Duration.ofMillis(600);

    private Gestures() {
    }

    public static void tap(Point point) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), point.x, point.y))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofMillis(80)))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        DriverManager.driver().perform(List.of(tap));
    }

    public static void longPress(WebElement element, Duration hold) {
        Point center = center(element.getRect());
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence press = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), center.x, center.y))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, hold))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        DriverManager.driver().perform(List.of(press));
    }

    public static void swipe(Point from, Point to, Duration duration) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), from.x, from.y))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofMillis(120)))
                .addAction(finger.createPointerMove(duration, PointerInput.Origin.viewport(), to.x, to.y))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        DriverManager.driver().perform(List.of(swipe));
    }

    /** Swipes across the middle of the screen, covering {@code percent} of it. */
    public static void swipe(Direction direction, double percent) {
        Dimension size = DriverManager.driver().manage().window().getSize();
        swipeWithin(new Rectangle(0, 0, size.height, size.width), direction, percent);
    }

    /** Swipes inside an element, for example a carousel. */
    public static void swipe(WebElement area, Direction direction, double percent) {
        swipeWithin(area.getRect(), direction, percent);
    }

    public static void dragTo(WebElement source, WebElement target) {
        swipe(center(source.getRect()), center(target.getRect()), Duration.ofMillis(900));
    }

    /** Swipes until the element is visible, at most {@code maxSwipes} times, then fails with a clear message. */
    public static WebElement scrollUntilVisible(By locator, Direction direction, int maxSwipes) {
        for (int i = 0; i <= maxSwipes; i++) {
            if (Waits.isVisibleWithin(locator, Duration.ofSeconds(1))) {
                return DriverManager.driver().findElement(locator);
            }
            swipe(direction, 0.5);
        }
        throw new org.openqa.selenium.NoSuchElementException("Not visible after " + maxSwipes + " swipes " + direction + ": " + locator);
    }

    public static Point[] path(Rectangle area, Direction direction, double percent) {
        Point center = center(area);
        int dx = (int) (area.width * percent / 2);
        int dy = (int) (area.height * percent / 2);
        return switch (direction) {
            case UP -> new Point[] {new Point(center.x, center.y + dy), new Point(center.x, center.y - dy)};
            case DOWN -> new Point[] {new Point(center.x, center.y - dy), new Point(center.x, center.y + dy)};
            case LEFT -> new Point[] {new Point(center.x + dx, center.y), new Point(center.x - dx, center.y)};
            case RIGHT -> new Point[] {new Point(center.x - dx, center.y), new Point(center.x + dx, center.y)};
        };
    }

    private static void swipeWithin(Rectangle area, Direction direction, double percent) {
        Point[] path = path(area, direction, percent);
        swipe(path[0], path[1], DEFAULT_SWIPE);
    }

    static Point center(Rectangle rect) {
        return new Point(rect.x + rect.width / 2, rect.y + rect.height / 2);
    }
}
