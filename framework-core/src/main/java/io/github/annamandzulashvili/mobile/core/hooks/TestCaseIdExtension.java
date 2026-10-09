package io.github.annamandzulashvili.mobile.core.hooks;

import java.lang.reflect.Method;
import java.util.regex.Pattern;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * Fails a device test before it starts a session if it has no {@link TestCaseId}, if the id is malformed,
 * or if the display name does not start with the same "[Cxxxx]" token.
 */
public class TestCaseIdExtension implements BeforeEachCallback {

    static final Pattern ID = Pattern.compile("C\\d{3,}");

    @Override
    public void beforeEach(ExtensionContext context) {
        check(context.getRequiredTestMethod(), context.getDisplayName());
    }

    public static String check(Method method, String displayName) {
        TestCaseId id = method.getAnnotation(TestCaseId.class);
        String where = method.getDeclaringClass().getSimpleName() + "." + method.getName();
        if (id == null) {
            throw new IllegalStateException(where + " has no @TestCaseId");
        }
        if (!ID.matcher(id.value()).matches()) {
            throw new IllegalStateException(where + " has a malformed @TestCaseId '" + id.value() + "', expected C followed by digits");
        }
        if (!displayName.startsWith("[" + id.value() + "]")) {
            throw new IllegalStateException(where + " display name must start with [" + id.value() + "], was: " + displayName);
        }
        return id.value();
    }
}
