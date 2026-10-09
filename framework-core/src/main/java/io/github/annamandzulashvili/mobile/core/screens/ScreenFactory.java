package io.github.annamandzulashvili.mobile.core.screens;

import io.github.annamandzulashvili.mobile.core.driver.DriverManager;
import io.github.annamandzulashvili.mobile.core.enums.Platform;
import io.github.annamandzulashvili.mobile.core.utils.ClassScanner;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Returns the platform implementation of an abstract screen.
 *
 * <p>Implementations live in sibling packages of the abstract screen's package parent
 * (for example {@code screens.base.HomeScreen} is implemented in {@code screens.android} and
 * {@code screens.ios}) and carry {@link ScreenFor}. Zero or more than one match is an error that lists
 * the candidates, so a missing or duplicated implementation is found at once.
 */
public final class ScreenFactory {

    private static final Map<String, Class<?>> CACHE = new ConcurrentHashMap<>();

    private ScreenFactory() {
    }

    public static <T> T get(Class<T> screen) {
        return get(screen, DriverManager.platform());
    }

    public static <T> T get(Class<T> screen, Platform platform) {
        Class<?> implementation = CACHE.computeIfAbsent(screen.getName() + "@" + platform, key -> resolve(screen, platform));
        try {
            return screen.cast(implementation.getDeclaredConstructor().newInstance());
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Cannot create " + implementation.getSimpleName() + ": it needs a public no-arg constructor", e);
        }
    }

    public static Class<?> resolve(Class<?> screen, Platform platform) {
        if (!Modifier.isAbstract(screen.getModifiers()) && !screen.isInterface()) {
            return screen;
        }
        String root = parentPackage(screen.getPackageName());
        List<Class<?>> candidates = ClassScanner.classesIn(root, screen.getClassLoader()).stream()
                .filter(screen::isAssignableFrom)
                .filter(type -> !Modifier.isAbstract(type.getModifiers()) && !type.isInterface())
                .filter(type -> type.isAnnotationPresent(ScreenFor.class))
                .toList();
        List<Class<?>> matches = candidates.stream()
                .filter(type -> Arrays.asList(type.getAnnotation(ScreenFor.class).value()).contains(platform))
                .toList();
        if (matches.size() != 1) {
            throw new IllegalStateException("Expected exactly one " + platform.id() + " implementation of " + screen.getSimpleName()
                    + " under " + root + ", found " + matches.size() + ". Candidates: "
                    + candidates.stream().map(Class::getSimpleName).toList());
        }
        return matches.get(0);
    }

    private static String parentPackage(String packageName) {
        int dot = packageName.lastIndexOf('.');
        return dot < 0 ? packageName : packageName.substring(0, dot);
    }
}
