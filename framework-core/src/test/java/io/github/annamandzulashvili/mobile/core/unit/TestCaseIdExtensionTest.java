package io.github.annamandzulashvili.mobile.core.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.annamandzulashvili.mobile.core.hooks.TestCaseId;
import io.github.annamandzulashvili.mobile.core.hooks.TestCaseIdExtension;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class TestCaseIdExtensionTest {

    @TestCaseId("C1001")
    void withId() {
    }

    void withoutId() {
    }

    @TestCaseId("TC-1")
    void malformed() {
    }

    @Test
    void acceptsAMatchingIdAndDisplayName() throws Exception {
        assertThat(TestCaseIdExtension.check(method("withId"), "[C1001] App opens")).isEqualTo("C1001");
    }

    @Test
    void rejectsMissingMalformedAndMismatchedIds() throws Exception {
        assertThatThrownBy(() -> TestCaseIdExtension.check(method("withoutId"), "x")).hasMessageContaining("has no @TestCaseId");
        assertThatThrownBy(() -> TestCaseIdExtension.check(method("malformed"), "[TC-1] x")).hasMessageContaining("malformed");
        assertThatThrownBy(() -> TestCaseIdExtension.check(method("withId"), "[C9999] x")).hasMessageContaining("must start with [C1001]");
    }

    private Method method(String name) throws NoSuchMethodException {
        return getClass().getDeclaredMethod(name);
    }
}
