package io.github.annamandzulashvili.mobile.core.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.annamandzulashvili.mobile.core.context.ContextScope;
import io.github.annamandzulashvili.mobile.core.context.ContextSwitcher;
import io.github.annamandzulashvili.mobile.core.context.ContextType;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class ContextSwitcherTest {

    /** Fake driver: contexts can appear late, and every switch is recorded. */
    static final class FakeContexts implements ContextSwitcher.ContextApi {
        final List<String> names = new ArrayList<>(List.of("NATIVE_APP"));
        final List<String> switches = new ArrayList<>();
        String current = "NATIVE_APP";
        int callsBeforeWebviewAppears = -1;

        @Override
        public List<String> contextNames() {
            if (callsBeforeWebviewAppears-- == 0) {
                names.add("WEBVIEW_com.app");
            }
            return List.copyOf(names);
        }

        @Override
        public String currentContext() {
            return current;
        }

        @Override
        public void switchTo(String name) {
            switches.add(name);
            current = name;
        }
    }

    private static final Duration SHORT = Duration.ofSeconds(3);

    @Test
    void scopeRestoresThePreviousContextEvenWhenTheBodyThrows() {
        FakeContexts fake = new FakeContexts();
        fake.names.add("WEBVIEW_1");
        ContextSwitcher switcher = new ContextSwitcher(fake);

        assertThatThrownBy(() -> {
            try (ContextScope ignored = switcher.enter(ContextType.WEBVIEW, SHORT)) {
                throw new IllegalStateException("boom");
            }
        }).hasMessage("boom");

        assertThat(fake.switches).containsExactly("WEBVIEW_1", "NATIVE_APP");
        assertThat(switcher.current()).isEqualTo(ContextType.NATIVE);
    }

    @Test
    void picksTheNewestWebview() {
        FakeContexts fake = new FakeContexts();
        fake.names.addAll(List.of("WEBVIEW_old", "WEBVIEW_new"));
        ContextSwitcher switcher = new ContextSwitcher(fake);

        String title = switcher.inContext(ContextType.WEBVIEW, SHORT, () -> fake.current);

        assertThat(title).isEqualTo("WEBVIEW_new");
    }

    @Test
    void waitsForAWebviewThatAppearsLate() {
        FakeContexts fake = new FakeContexts();
        fake.callsBeforeWebviewAppears = 2;
        ContextSwitcher switcher = new ContextSwitcher(fake);

        switcher.switchTo(ContextType.WEBVIEW, SHORT);

        assertThat(fake.current).isEqualTo("WEBVIEW_com.app");
    }

    @Test
    void failsWithAClearMessageWhenNoWebviewAppears() {
        FakeContexts fake = new FakeContexts();
        fake.callsBeforeWebviewAppears = Integer.MAX_VALUE;
        ContextSwitcher switcher = new ContextSwitcher(fake);

        assertThatThrownBy(() -> switcher.switchTo(ContextType.WEBVIEW, Duration.ofSeconds(1)))
                .hasMessageContaining("WEBVIEW context to appear");
    }

    @Test
    void contextTypeMatchesByPrefix() {
        assertThat(ContextType.of("WEBVIEW_chrome")).isEqualTo(ContextType.WEBVIEW);
        assertThat(ContextType.of("NATIVE_APP")).isEqualTo(ContextType.NATIVE);
    }
}
