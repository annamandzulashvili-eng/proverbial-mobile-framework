package io.github.annamandzulashvili.mobile.core.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Builds {@link Settings} from layered sources. Later layers win:
 *
 * <ol>
 *   <li>core JSON files: classpath {@code config/core/<name>.json}</li>
 *   <li>module JSON files: classpath {@code config/<name>.json} (a module file with the same name overrides core)</li>
 *   <li>environment variables: {@code section.key -> SECTION_KEY}, plus a few well-known aliases</li>
 *   <li>{@code credentials.json} in the module folder or the project root (git-ignored)</li>
 *   <li>{@code -D} system properties: full path ({@code -Dbrowserstack.hubUrl=...}) or a short run alias ({@code -Dplatform=ios})</li>
 * </ol>
 *
 * <p>{@code credentials.json} beats environment variables on purpose: a developer machine may carry
 * another BrowserStack account in its environment, and the local file must win there. CI has no
 * {@code credentials.json}, so CI uses the environment. Files ending in {@code .template.json} are never read.
 */
public final class ConfigLoader {

    /** Every file name the loader looks for, in both {@code config/core/} and {@code config/}. */
    public static final List<String> FILE_NAMES = List.of(
            "runSettings",
            "browserStackSettings",
            "browserStackScripts",
            "devicesSettings",
            "timeoutsSettings",
            "reportingSettings",
            "localSettings",
            "appAndroidSettings",
            "appIosSettings",
            "webSettings");

    /** Environment variables that do not follow the SECTION_KEY rule but are standard elsewhere. */
    static final Map<String, String> ENV_ALIASES = Map.of(
            "BROWSERSTACK_USERNAME", "browserstack.userName",
            "BROWSERSTACK_ACCESS_KEY", "browserstack.accessKey",
            "BUILD_NUMBER", "run.buildNumber",
            "BUILD_URL", "run.buildUrl",
            "BRANCH_NAME", "run.branch");

    /** Short -D names for the run selectors. */
    static final Map<String, String> PROPERTY_ALIASES = Map.of(
            "platform", "run.platform",
            "runOn", "run.runOn",
            "device", "run.device",
            "suite", "run.suite",
            "threads", "run.threads",
            "app", "run.app",
            "appPath", "run.appPath",
            "env", "run.env",
            "branch", "run.branch");

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private ConfigLoader() {
    }

    public static Settings load() {
        return load(ConfigSources.current(), Thread.currentThread().getContextClassLoader());
    }

    public static Settings load(ConfigSources sources, ClassLoader classLoader) {
        ObjectNode tree = loadTree(sources, classLoader);
        try {
            return MAPPER.treeToValue(tree, Settings.class);
        } catch (IOException e) {
            throw new ConfigException("Configuration does not match the expected shape: " + e.getMessage(), e);
        }
    }

    static ObjectNode loadTree(ConfigSources sources, ClassLoader classLoader) {
        ObjectNode tree = MAPPER.createObjectNode();
        for (String name : FILE_NAMES) {
            readClasspath(classLoader, "config/core/" + name + ".json").ifPresent(node -> deepMerge(tree, node));
        }
        for (String name : FILE_NAMES) {
            readClasspath(classLoader, "config/" + name + ".json").ifPresent(node -> deepMerge(tree, node));
        }
        applyEnvironment(tree, sources.env());
        credentialsFile(sources.workingDir()).ifPresent(path -> deepMerge(tree, readFile(path)));
        applySystemProperties(tree, sources.systemProperties());
        return tree;
    }

    private static java.util.Optional<JsonNode> readClasspath(ClassLoader classLoader, String resource) {
        try (InputStream in = classLoader.getResourceAsStream(resource)) {
            return in == null ? java.util.Optional.empty() : java.util.Optional.of(MAPPER.readTree(in));
        } catch (IOException e) {
            throw new ConfigException("Cannot parse " + resource + ": " + e.getMessage(), e);
        }
    }

    private static JsonNode readFile(Path path) {
        try {
            return MAPPER.readTree(Files.readString(path));
        } catch (IOException e) {
            throw new ConfigException("Cannot parse " + path.getFileName() + ": " + e.getMessage(), e);
        }
    }

    static java.util.Optional<Path> credentialsFile(Path workingDir) {
        for (Path dir = workingDir; dir != null; dir = dir.getParent()) {
            Path candidate = dir.resolve("credentials.json");
            if (Files.isRegularFile(candidate)) {
                return java.util.Optional.of(candidate);
            }
            if (Files.exists(dir.resolve(".git")) || Files.exists(dir.resolve("credentials.template.json"))) {
                break; // project root reached
            }
        }
        return java.util.Optional.empty();
    }

    static void deepMerge(ObjectNode target, JsonNode source) {
        if (source == null || !source.isObject()) {
            return;
        }
        Iterator<Map.Entry<String, JsonNode>> fields = source.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            JsonNode existing = target.get(field.getKey());
            if (existing instanceof ObjectNode existingObject && field.getValue().isObject()) {
                deepMerge(existingObject, field.getValue());
            } else {
                target.set(field.getKey(), field.getValue().deepCopy());
            }
        }
    }

    private static void applyEnvironment(ObjectNode tree, Map<String, String> env) {
        for (String path : leafPaths(tree)) {
            String value = env.get(envName(path));
            if (value != null) {
                set(tree, path, value);
            }
        }
        ENV_ALIASES.forEach((name, path) -> {
            String value = env.get(name);
            if (value != null && !value.isBlank()) {
                set(tree, path, value);
            }
        });
    }

    private static void applySystemProperties(ObjectNode tree, Map<String, String> properties) {
        for (String path : leafPaths(tree)) {
            String value = properties.get(path);
            if (value != null) {
                set(tree, path, value);
            }
        }
        PROPERTY_ALIASES.forEach((name, path) -> {
            String value = properties.get(name);
            // Maven passes an empty string for an unset property; empty must not wipe the file value.
            if (value != null && !value.isBlank()) {
                set(tree, path, value);
            }
        });
    }

    /** {@code browserstack.accessKey -> BROWSERSTACK_ACCESSKEY}. */
    static String envName(String path) {
        return path.replace('.', '_').toUpperCase(Locale.ROOT);
    }

    static List<String> leafPaths(JsonNode node) {
        List<String> paths = new ArrayList<>();
        collect(node, "", paths);
        return paths;
    }

    private static void collect(JsonNode node, String prefix, List<String> paths) {
        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String path = prefix.isEmpty() ? field.getKey() : prefix + "." + field.getKey();
            if (field.getValue().isObject()) {
                collect(field.getValue(), path, paths);
            } else {
                paths.add(path);
            }
        }
    }

    static void set(ObjectNode tree, String path, String value) {
        String[] parts = path.split("\\.");
        ObjectNode current = tree;
        for (int i = 0; i < parts.length - 1; i++) {
            JsonNode child = current.get(parts[i]);
            if (!(child instanceof ObjectNode)) {
                child = current.putObject(parts[i]);
            }
            current = (ObjectNode) child;
        }
        current.set(parts[parts.length - 1], TextNode.valueOf(value));
    }
}
