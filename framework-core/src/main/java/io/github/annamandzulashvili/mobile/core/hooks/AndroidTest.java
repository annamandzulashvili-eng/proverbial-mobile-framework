package io.github.annamandzulashvili.mobile.core.hooks;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * A device test on android. Adds the "e2e" and "android" tags and the lifecycle extensions, in this order:
 * TC-ID check, session start, BrowserStack status, failure evidence. JUnit runs the after-callbacks in
 * reverse order, so evidence is captured while the session is still alive and the session is closed last.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Tag("e2e")
@Tag("android")
@ExtendWith({TestCaseIdExtension.class, DriverExtension.class, BrowserStackStatusExtension.class, FailureDumpExtension.class})
public @interface AndroidTest {
}
