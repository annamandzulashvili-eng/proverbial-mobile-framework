package io.github.annamandzulashvili.mobile.core.hooks;

import io.github.annamandzulashvili.mobile.core.config.RunContext;
import io.github.annamandzulashvili.mobile.core.config.Settings;
import io.github.annamandzulashvili.mobile.core.driver.DriverFactory;
import io.github.annamandzulashvili.mobile.core.driver.DriverManager;
import io.github.annamandzulashvili.mobile.core.driver.Target;
import io.github.annamandzulashvili.mobile.core.driver.TestSession;
import io.github.annamandzulashvili.mobile.core.enums.DeviceType;
import io.github.annamandzulashvili.mobile.core.enums.Module;
import io.github.annamandzulashvili.mobile.core.enums.Platform;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import org.slf4j.MDC;

/**
 * Starts a fresh session before each test and always closes it after. Teardown is null-safe: when the
 * session never started, there is nothing to close and no second exception hides the first one.
 */
public class DriverExtension implements BeforeEachCallback, AfterEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        Settings settings = RunContext.settings().validateForDeviceRun();
        Target target = targetFor(context, settings);
        MDC.put("platform", target.platform().id());
        MDC.put("test", target.testName());
        TestSession session = DriverFactory.create(settings, target);
        DriverManager.start(session);
        MDC.put("session", session.sessionId());
    }

    @Override
    public void afterEach(ExtensionContext context) {
        try {
            DriverManager.stop();
        } finally {
            MDC.clear();
        }
    }

    static Target targetFor(ExtensionContext context, Settings settings) {
        Class<?> testClass = context.getRequiredTestClass();
        Platform platform = platformOf(testClass);
        Module module = AnnotationSupport.isAnnotated(testClass, WebTest.class) ? Module.WEB : Module.APP;
        DeviceType deviceType = context.getTags().contains("tablet") ? DeviceType.TABLET : DeviceType.PHONE;
        String appName = AnnotationSupport.findAnnotation(testClass, App.class).map(App::value).orElse(settings.run().app());
        String gps = context.getTestMethod()
                .flatMap(method -> AnnotationSupport.findAnnotation(method, GpsLocation.class))
                .map(GpsLocation::value)
                .orElse("");
        return new Target(module, platform, deviceType, appName, context.getDisplayName(), gps);
    }

    static Platform platformOf(Class<?> testClass) {
        boolean android = AnnotationSupport.isAnnotated(testClass, AndroidTest.class);
        boolean ios = AnnotationSupport.isAnnotated(testClass, IosTest.class);
        if (android == ios) {
            throw new IllegalStateException(testClass.getSimpleName() + " must be annotated with exactly one of @AndroidTest or @IosTest");
        }
        return android ? Platform.ANDROID : Platform.IOS;
    }
}
