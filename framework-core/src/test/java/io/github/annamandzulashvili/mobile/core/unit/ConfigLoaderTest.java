package io.github.annamandzulashvili.mobile.core.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.annamandzulashvili.mobile.core.config.ConfigException;
import io.github.annamandzulashvili.mobile.core.config.ConfigLoader;
import io.github.annamandzulashvili.mobile.core.config.ConfigSources;
import io.github.annamandzulashvili.mobile.core.config.Settings;
import io.github.annamandzulashvili.mobile.core.enums.Platform;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ConfigLoaderTest {

    @TempDir
    Path tmp;

    @Test
    @DisplayName("core JSON files give a complete default configuration")
    void defaults() {
        Settings settings = TestSettings.load(Map.of(), Map.of(), tmp);

        assertThat(settings.platform()).isEqualTo(Platform.ANDROID);
        assertThat(settings.run().threads()).isEqualTo(2);
        assertThat(settings.devices()).containsKeys("androidPhone", "androidTablet", "iphone", "ipad");
        assertThat(settings.timeouts().condition()).hasSeconds(20);
        assertThat(settings.scripts()).containsKeys("setSessionStatus", "annotate", "getSessionDetails");
    }

    @Test
    @DisplayName("a module file with the same name overrides the core file")
    void moduleOverridesCore() throws IOException {
        Path classes = tmp.resolve("module-classes");
        Files.createDirectories(classes.resolve("config"));
        Files.writeString(classes.resolve("config/runSettings.json"), "{\"run\":{\"threads\":3}}");

        try (URLClassLoader loader = new URLClassLoader(new URL[] {classes.toUri().toURL()}, getClass().getClassLoader())) {
            Settings settings = ConfigLoader.load(new ConfigSources(Map.of(), Map.of(), tmp), loader);

            assertThat(settings.run().threads()).isEqualTo(3);
            assertThat(settings.run().platform()).as("keys the module does not set stay from core").isEqualTo("android");
        }
    }

    @Test
    @DisplayName("environment variables override JSON: SECTION_KEY and standard aliases")
    void environmentOverridesJson() {
        Settings settings = TestSettings.load(
                Map.of("RUN_THREADS", "4", "BROWSERSTACK_ACCESS_KEY", "from-env", "BUILD_NUMBER", "42"), Map.of(), tmp);

        assertThat(settings.run().threads()).isEqualTo(4);
        assertThat(settings.browserstack().accessKey()).isEqualTo("from-env");
        assertThat(settings.run().buildNumber()).isEqualTo("42");
    }

    @Test
    @DisplayName("credentials.json beats the environment, -D beats both")
    void precedence() throws IOException {
        Files.writeString(tmp.resolve("credentials.json"), "{\"browserstack\":{\"userName\":\"from-file\",\"accessKey\":\"file-key\"}}");

        Settings fileWins = TestSettings.load(Map.of("BROWSERSTACK_USERNAME", "from-env"), Map.of(), tmp);
        Settings propertyWins = TestSettings.load(Map.of("BROWSERSTACK_USERNAME", "from-env"),
                Map.of("browserstack.userName", "from-property"), tmp);

        assertThat(fileWins.browserstack().userName()).isEqualTo("from-file");
        assertThat(propertyWins.browserstack().userName()).isEqualTo("from-property");
    }

    @Test
    @DisplayName("credentials.json is found in a parent folder up to the project root")
    void credentialsInProjectRoot() throws IOException {
        Files.writeString(tmp.resolve("credentials.template.json"), "{}");
        Files.writeString(tmp.resolve("credentials.json"), "{\"browserstack\":{\"userName\":\"root-file\"}}");
        Path module = Files.createDirectories(tmp.resolve("mobile-app-tests"));

        assertThat(TestSettings.load(Map.of(), Map.of(), module).browserstack().userName()).isEqualTo("root-file");
    }

    @Test
    @DisplayName("credentials.template.json is never read")
    void templateIgnored() throws IOException {
        Files.writeString(tmp.resolve("credentials.template.json"), "{\"browserstack\":{\"userName\":\"template\"}}");

        assertThat(TestSettings.load(Map.of(), Map.of(), tmp).browserstack().userName()).isEmpty();
    }

    @Test
    @DisplayName("short -D aliases select the run; an empty value does not wipe the file value")
    void runAliases() {
        Settings settings = TestSettings.load(Map.of(), Map.of("platform", "ios", "threads", "5", "device", ""), tmp);

        assertThat(settings.platform()).isEqualTo(Platform.IOS);
        assertThat(settings.run().threads()).isEqualTo(5);
        assertThat(settings.run().device()).isEmpty();
    }

    @Test
    @DisplayName("a missing secret fails fast with the variable name and never prints values")
    void failFastWithoutLeakingValues() {
        Settings settings = TestSettings.load(Map.of("BROWSERSTACK_USERNAME", "secret-user"), Map.of(), tmp);

        assertThatThrownBy(settings::validateForDeviceRun)
                .isInstanceOf(ConfigException.class)
                .hasMessageContaining("BROWSERSTACK_ACCESS_KEY")
                .hasMessageNotContaining("secret-user");
        assertThat(settings.toString()).doesNotContain("secret-user");
    }

    @Test
    @DisplayName("production environments are refused")
    void productionGuard() {
        Settings settings = TestSettings.load(
                Map.of("BROWSERSTACK_USERNAME", "u", "BROWSERSTACK_ACCESS_KEY", "k"), Map.of("env", "prod-eu"), tmp);

        assertThatThrownBy(settings::validateForDeviceRun).hasMessageContaining("production");
    }

    @Test
    @DisplayName("threads outside 1..5 are rejected")
    void threadsRange() {
        Settings settings = TestSettings.load(
                Map.of("BROWSERSTACK_USERNAME", "u", "BROWSERSTACK_ACCESS_KEY", "k"), Map.of("threads", "9"), tmp);

        assertThatThrownBy(settings::validateForDeviceRun).hasMessageContaining("between 1 and 5");
    }

    @Test
    @DisplayName("local runs do not need BrowserStack credentials")
    void localRunNeedsNoCredentials() {
        Settings settings = TestSettings.load(Map.of(), Map.of("runOn", "local"), tmp);

        assertThat(settings.validateForDeviceRun()).isSameAs(settings);
    }
}
