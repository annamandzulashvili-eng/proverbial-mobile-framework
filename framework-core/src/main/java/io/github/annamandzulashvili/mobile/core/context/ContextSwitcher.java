package io.github.annamandzulashvili.mobile.core.context;

import io.appium.java_client.remote.SupportsContextSwitching;
import io.github.annamandzulashvili.mobile.core.waits.PollUntil;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import org.openqa.selenium.WebDriver;

/**
 * Switches between NATIVE_APP and WEBVIEW contexts.
 *
 * <ul>
 *   <li>{@link #enter} returns a scope that always restores the previous context on close, even after an exception.</li>
 *   <li>The list of context names is cached for the session and re-read only when the wanted one is missing.</li>
 *   <li>When several WEBVIEW contexts exist, the newest (last) one is taken.</li>
 * </ul>
 */
public final class ContextSwitcher {

    /** The three driver calls this class needs. Lets unit tests run without a device. */
    public interface ContextApi {
        List<String> contextNames();

        String currentContext();

        void switchTo(String name);

        static ContextApi of(WebDriver driver) {
            if (!(driver instanceof SupportsContextSwitching switching)) {
                throw new IllegalArgumentException(driver.getClass().getSimpleName() + " does not support context switching");
            }
            return new ContextApi() {
                @Override
                public List<String> contextNames() {
                    return new ArrayList<>(switching.getContextHandles());
                }

                @Override
                public String currentContext() {
                    return switching.getContext();
                }

                @Override
                public void switchTo(String name) {
                    switching.context(name);
                }
            };
        }
    }

    private final ContextApi api;
    private List<String> cachedNames = List.of();

    public ContextSwitcher(ContextApi api) {
        this.api = api;
    }

    public ContextType current() {
        return ContextType.of(api.currentContext());
    }

    /** Context names, refreshed from the driver. */
    public List<String> available() {
        cachedNames = List.copyOf(api.contextNames());
        return cachedNames;
    }

    public boolean has(ContextType type) {
        return find(type).isPresent() || available().stream().anyMatch(type::matches);
    }

    /** Switches and waits up to {@code timeout} for the context to appear. */
    public void switchTo(ContextType type, Duration timeout) {
        if (current() == type) {
            return;
        }
        String name = find(type).orElseGet(() -> PollUntil.value(
                () -> lastMatching(available(), type).orElse(null),
                timeout,
                Duration.ofMillis(500),
                "a " + type + " context to appear"));
        api.switchTo(name);
    }

    public ContextScope enter(ContextType type, Duration timeout) {
        String previous = api.currentContext();
        switchTo(type, timeout);
        return new ContextScope(this, previous);
    }

    public <T> T inContext(ContextType type, Duration timeout, Supplier<T> body) {
        try (ContextScope ignored = enter(type, timeout)) {
            return body.get();
        }
    }

    void restore(String previous) {
        if (previous != null && !previous.equals(api.currentContext())) {
            api.switchTo(previous);
        }
    }

    private Optional<String> find(ContextType type) {
        return lastMatching(cachedNames, type);
    }

    private static Optional<String> lastMatching(List<String> names, ContextType type) {
        String match = null;
        for (String name : names) {
            if (type.matches(name)) {
                match = name;
            }
        }
        return Optional.ofNullable(match);
    }
}
