package io.github.annamandzulashvili.mobile.core.browserstack;

import io.github.annamandzulashvili.mobile.core.api.BrowserStackApi;
import io.github.annamandzulashvili.mobile.core.config.AppProfile;
import io.github.annamandzulashvili.mobile.core.config.ConfigException;
import io.github.annamandzulashvili.mobile.core.config.Settings;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Decides which app build a BrowserStack session installs.
 *
 * <ul>
 *   <li>Default: the profile's {@code browserStackApp} custom id, which always points to the latest upload.</li>
 *   <li>With {@code -DappPath=...}: uploads that file once per run (not once per test) and shares the
 *       resulting {@code bs://} url with every parallel test.</li>
 * </ul>
 */
public final class AppProvider {

    private static final Logger LOG = LoggerFactory.getLogger(AppProvider.class);
    private static final Map<String, String> UPLOADED = new ConcurrentHashMap<>();

    private AppProvider() {
    }

    public static String appReference(Settings settings, AppProfile app) {
        String appPath = settings.run().appPath();
        if (appPath == null || appPath.isBlank()) {
            if (app.browserStackApp() == null || app.browserStackApp().isBlank()) {
                throw new ConfigException("App profile for " + app.platform() + " has no browserStackApp custom id");
            }
            return app.browserStackApp();
        }
        return UPLOADED.computeIfAbsent(appPath, path -> upload(settings, app, Path.of(path)));
    }

    private static String upload(Settings settings, AppProfile app, Path file) {
        if (!Files.isRegularFile(file)) {
            throw new ConfigException("appPath does not point to a file: " + file);
        }
        LOG.info("Uploading {} to BrowserStack once for this run", file.getFileName());
        String url = new BrowserStackApi(settings.browserstack()).uploadApp(file, app.browserStackApp(), settings.timeouts().appUpload());
        LOG.info("Uploaded as {}", url);
        return url;
    }
}
