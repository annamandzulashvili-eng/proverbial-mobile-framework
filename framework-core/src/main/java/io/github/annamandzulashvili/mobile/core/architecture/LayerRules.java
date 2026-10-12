package io.github.annamandzulashvili.mobile.core.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.lang.ArchRule;
import java.util.List;

/**
 * The layering rule, enforced by ArchUnit in every test module:
 *
 * <pre>
 * tests -> steps.business -> steps.app -> screens / pages -> core
 * </pre>
 *
 * <p>Each module calls {@link #forModule(String)} with its root package (for example
 * {@code io.github.annamandzulashvili.mobile.app}) and checks the returned rules against its classes.
 */
public final class LayerRules {

    public static final String CORE = "io.github.annamandzulashvili.mobile.core";

    private LayerRules() {
    }

    public static List<ArchRule> forModule(String root) {
        String tests = root + ".tests..";
        String steps = root + ".steps..";
        String business = root + ".steps.business..";
        String appSteps = root + ".steps.app..";
        String screens = root + ".screens..";
        String pages = root + ".pages..";
        return List.of(
                noClasses().that().resideInAPackage(tests)
                        .should().dependOnClassesThat().resideInAnyPackage(screens, pages, CORE + ".driver..", CORE + ".selectors..",
                                CORE + ".waits..", CORE + ".gestures..", "io.appium..", "org.openqa.selenium..")
                        .because("tests orchestrate steps and assert; locators and driver calls live below them"),
                noClasses().that().resideInAPackage(steps)
                        .should().dependOnClassesThat().resideInAnyPackage(CORE + ".driver..", CORE + ".selectors..")
                        .because("steps talk to screens and core services, never to the raw driver or locators"),
                noClasses().that().resideInAPackage(business)
                        .should().dependOnClassesThat().resideInAnyPackage(screens, pages)
                        .because("business steps compose app steps; they do not touch screens directly"),
                noClasses().that().resideInAPackage(appSteps)
                        .should().dependOnClassesThat().resideInAPackage(business)
                        .because("dependencies point down only"),
                noClasses().that().resideInAnyPackage(screens, pages)
                        .should().dependOnClassesThat().resideInAnyPackage(steps, tests)
                        .because("screens know nothing about steps or tests"),
                fields().that().areDeclaredInClassesThat().resideInAnyPackage(steps, screens, pages)
                        .and().areStatic()
                        .should().beFinal()
                        .because("static mutable state breaks parallel runs; per-test state lives in the TestSession"));
    }

    /** Core must never know about a test module. Checked in framework-core. */
    public static ArchRule coreIsAppAgnostic() {
        return noClasses().that().resideInAPackage(CORE + "..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "io.github.annamandzulashvili.mobile.app..", "io.github.annamandzulashvili.mobile.web..")
                .because("framework-core is reused by every module");
    }
}
