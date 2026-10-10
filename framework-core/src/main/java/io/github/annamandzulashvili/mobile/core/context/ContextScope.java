package io.github.annamandzulashvili.mobile.core.context;

/**
 * Returned by {@link ContextSwitcher#enter}. Closing it switches back to the context that was active before,
 * also when the body threw. Use with try-with-resources:
 *
 * <pre>{@code
 * try (var webview = contexts.enter(ContextType.WEBVIEW)) {
 *     page.clickSave();
 * }
 * }</pre>
 */
public final class ContextScope implements AutoCloseable {

    private final ContextSwitcher switcher;
    private final String previous;

    ContextScope(ContextSwitcher switcher, String previous) {
        this.switcher = switcher;
        this.previous = previous;
    }

    @Override
    public void close() {
        switcher.restore(previous);
    }
}
