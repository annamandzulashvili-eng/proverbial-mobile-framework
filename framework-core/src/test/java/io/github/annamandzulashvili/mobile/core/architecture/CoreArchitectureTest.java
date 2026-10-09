package io.github.annamandzulashvili.mobile.core.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class CoreArchitectureTest {

    private final JavaClasses core = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages(LayerRules.CORE);

    @Test
    void coreDoesNotDependOnTestModules() {
        LayerRules.coreIsAppAgnostic().check(core);
    }
}
