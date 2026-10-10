package io.github.annamandzulashvili.mobile.core.driver;

import io.appium.java_client.AppiumDriver;
import io.github.annamandzulashvili.mobile.core.enums.Platform;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Holds the {@link TestSession} of the current test thread. JUnit runs each test on one thread,
 * so a ThreadLocal keeps parallel tests apart.
 */
public final class DriverManager {

    private static final Logger LOG = LoggerFactory.getLogger(DriverManager.class);
    private static final ThreadLocal<TestSession> SESSION = new ThreadLocal<>();

    private DriverManager() {
    }

    public static void start(TestSession session) {
        SESSION.set(session);
    }

    public static Optional<TestSession> currentIfAny() {
        return Optional.ofNullable(SESSION.get());
    }

    public static TestSession current() {
        TestSession session = SESSION.get();
        if (session == null) {
            throw new IllegalStateException("No Appium session on this thread. Is the test annotated with @AndroidTest or @IosTest?");
        }
        return session;
    }

    public static AppiumDriver driver() {
        return current().driver();
    }

    public static Platform platform() {
        return current().platform();
    }

    /** Quits the session if there is one. Never throws, so it cannot hide the test's own failure. */
    public static void stop() {
        TestSession session = SESSION.get();
        SESSION.remove();
        if (session == null) {
            return;
        }
        try {
            session.driver().quit();
        } catch (RuntimeException e) {
            LOG.warn("Session {} did not quit cleanly: {}", session.sessionId(), e.getMessage());
        }
    }
}
