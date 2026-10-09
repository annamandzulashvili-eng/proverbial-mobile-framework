package io.github.annamandzulashvili.mobile.core.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.annamandzulashvili.mobile.core.waits.PollUntil;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.TimeoutException;

class PollUntilTest {

    @Test
    void returnsTheFirstNonNullValueAndTreatsExceptionsAsNotYet() {
        AtomicInteger attempts = new AtomicInteger();

        String value = PollUntil.value(() -> {
            int n = attempts.incrementAndGet();
            if (n == 1) {
                throw new IllegalStateException("not ready");
            }
            return n < 3 ? null : "ready";
        }, Duration.ofSeconds(2), Duration.ofMillis(10), "the value");

        assertThat(value).isEqualTo("ready");
        assertThat(attempts).hasValue(3);
    }

    @Test
    void timesOutWithTheDescriptionAndTheLastError() {
        assertThatThrownBy(() -> PollUntil.value(() -> {
            throw new IllegalStateException("still broken");
        }, Duration.ofMillis(200), Duration.ofMillis(20), "the thing"))
                .isInstanceOf(TimeoutException.class)
                .hasMessageContaining("waiting for the thing")
                .hasMessageContaining("still broken");
    }
}
