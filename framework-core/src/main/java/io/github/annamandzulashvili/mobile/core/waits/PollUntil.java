package io.github.annamandzulashvili.mobile.core.waits;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import org.openqa.selenium.TimeoutException;

/**
 * Bounded polling that does not need a driver. Every attempt that throws counts as "not yet".
 * The timeout is always respected, so a hanging condition cannot hang the test.
 */
public final class PollUntil {

    private PollUntil() {
    }

    public static void condition(BooleanSupplier condition, Duration timeout, Duration interval, String description) {
        value(() -> condition.getAsBoolean() ? Boolean.TRUE : null, timeout, interval, description);
    }

    public static <T> T value(Supplier<T> attempt, Duration timeout, Duration interval, String description) {
        return value(attempt, timeout, interval, description, Clock.systemUTC());
    }

    static <T> T value(Supplier<T> attempt, Duration timeout, Duration interval, String description, Clock clock) {
        Instant deadline = clock.instant().plus(timeout);
        RuntimeException lastError = null;
        while (true) {
            try {
                T result = attempt.get();
                if (result != null) {
                    return result;
                }
            } catch (RuntimeException e) {
                lastError = e;
            }
            if (!clock.instant().isBefore(deadline)) {
                String reason = lastError == null ? "" : " Last error: " + lastError.getMessage();
                throw new TimeoutException("Timed out after " + timeout.toSeconds() + "s waiting for " + description + "." + reason, lastError);
            }
            sleep(interval);
        }
    }

    private static void sleep(Duration interval) {
        try {
            Thread.sleep(interval.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while polling", e);
        }
    }
}
