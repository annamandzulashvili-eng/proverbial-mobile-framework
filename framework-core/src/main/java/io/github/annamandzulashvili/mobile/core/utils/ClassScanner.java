package io.github.annamandzulashvili.mobile.core.utils;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

/** Lists the classes of a package and its sub-packages, from class folders and jars. No extra library needed. */
public final class ClassScanner {

    private ClassScanner() {
    }

    public static List<Class<?>> classesIn(String packageName, ClassLoader loader) {
        String path = packageName.replace('.', '/');
        List<String> names = new ArrayList<>();
        try {
            Enumeration<URL> roots = loader.getResources(path);
            for (URL root : Collections.list(roots)) {
                if ("file".equals(root.getProtocol())) {
                    collectFromDirectory(Path.of(root.toURI()), packageName, names);
                } else if ("jar".equals(root.getProtocol())) {
                    collectFromJar(root, path, names);
                }
            }
        } catch (IOException | URISyntaxException e) {
            throw new IllegalStateException("Cannot scan package " + packageName, e);
        }
        List<Class<?>> classes = new ArrayList<>();
        for (String name : names) {
            try {
                classes.add(Class.forName(name, false, loader));
            } catch (ClassNotFoundException | LinkageError e) {
                // Not loadable here (for example a class of another platform's optional dependency); skip it.
            }
        }
        return classes;
    }

    private static void collectFromDirectory(Path dir, String packageName, List<String> names) throws IOException {
        try (Stream<Path> files = Files.walk(dir)) {
            files.filter(file -> file.toString().endsWith(".class") && !file.getFileName().toString().contains("$"))
                    .forEach(file -> {
                        String relative = dir.relativize(file).toString().replace(java.io.File.separatorChar, '.');
                        names.add(packageName + "." + relative.substring(0, relative.length() - ".class".length()));
                    });
        }
    }

    private static void collectFromJar(URL root, String path, List<String> names) throws IOException {
        JarURLConnection connection = (JarURLConnection) root.openConnection();
        try (JarFile jar = connection.getJarFile()) {
            for (JarEntry entry : Collections.list(jar.entries())) {
                String entryName = entry.getName();
                if (entryName.startsWith(path + "/") && entryName.endsWith(".class") && !entryName.contains("$")) {
                    names.add(entryName.substring(0, entryName.length() - ".class".length()).replace('/', '.'));
                }
            }
        }
    }
}
