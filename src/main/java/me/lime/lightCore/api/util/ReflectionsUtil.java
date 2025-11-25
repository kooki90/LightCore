package me.lime.lightCore.api.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Utility class for reflection operations with caching
 */
public final class ReflectionsUtil {

    private ReflectionsUtil() {}

    private static final Cache<String, Class<?>> CLASS_CACHE = Caffeine.newBuilder()
            .maximumSize(256)
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .build();

    private static final Cache<String, Constructor<?>> CONSTRUCTOR_CACHE = Caffeine.newBuilder()
            .maximumSize(256)
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .build();

    private static final Cache<String, Field> FIELD_CACHE = Caffeine.newBuilder()
            .maximumSize(512)
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .build();

    private static final Cache<String, Method> METHOD_CACHE = Caffeine.newBuilder()
            .maximumSize(512)
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .build();

    /**
     * Clear all caches
     */
    public static void clearAllCaches() {
        CLASS_CACHE.invalidateAll();
        CONSTRUCTOR_CACHE.invalidateAll();
        FIELD_CACHE.invalidateAll();
        METHOD_CACHE.invalidateAll();
    }

    @Nullable
    public static Class<?> getClass(@NotNull String path, @NotNull String name) {
        return getClass(path + "." + name);
    }

    @Nullable
    public static Class<?> getInnerClass(@NotNull String path, @NotNull String name) {
        return getClass(path + "$" + name);
    }

    @Nullable
    public static Class<?> getClass(@NotNull String path) {
        return getClass(path, true);
    }

    @Nullable
    public static Class<?> getClass(@NotNull String path, boolean printError) {
        try {
            Class<?> cached = CLASS_CACHE.getIfPresent(path);
            if (cached != null) return cached;

            Class<?> clazz = Class.forName(path);
            CLASS_CACHE.put(path, clazz);
            return clazz;

        } catch (ClassNotFoundException e) {
            if (printError) e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static Constructor<?> getConstructor(@NotNull Class<?> source, Class<?>... types) {
        String key = source.getName() + Arrays.toString(types);

        Constructor<?> cached = CONSTRUCTOR_CACHE.getIfPresent(key);
        if (cached != null) return cached;

        try {
            Constructor<?> constructor = source.getDeclaredConstructor(types);
            constructor.setAccessible(true);
            CONSTRUCTOR_CACHE.put(key, constructor);
            return constructor;

        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static Object invokeConstructor(@NotNull Constructor<?> constructor, Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return null;
        }
    }

    @NotNull
    public static List<Field> getFields(@NotNull Class<?> source, boolean includeParent) {
        List<Field> result = new ArrayList<>();
        Class<?> lookup = source;

        while (lookup != null && lookup != Object.class) {
            result.addAll(Arrays.asList(lookup.getDeclaredFields()));
            if (!includeParent) break;
            lookup = lookup.getSuperclass();
        }

        return result;
    }

    @Nullable
    public static Field getField(@NotNull Class<?> source, @NotNull String name) {
        String key = source.getName() + "#" + name;

        Field cached = FIELD_CACHE.getIfPresent(key);
        if (cached != null) return cached;

        try {
            Field field = source.getDeclaredField(name);
            field.setAccessible(true);
            FIELD_CACHE.put(key, field);
            return field;

        } catch (NoSuchFieldException e) {
            Class<?> superClass = source.getSuperclass();
            return superClass == null ? null : getField(superClass, name);
        }
    }

    @Nullable
    public static Object getFieldValue(@NotNull Object source, @NotNull String name) {
        Class<?> clazz = (source instanceof Class<?> c) ? c : source.getClass();
        Field field = getField(clazz, name);

        if (field == null) return null;

        try {
            return field.get(source instanceof Class ? null : source);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean setFieldValue(@NotNull Object source, @NotNull String name, @Nullable Object value) {
        boolean isStatic = source instanceof Class<?>;
        Class<?> clazz = isStatic ? (Class<?>) source : source.getClass();

        Field field = getField(clazz, name);
        if (field == null) return false;

        try {
            field.set(isStatic ? null : source, value);
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Nullable
    public static Method getMethod(@NotNull Class<?> source, @NotNull String name, @NotNull Class<?>... params) {
        String key = source.getName() + "#" + name + Arrays.toString(params);

        Method cached = METHOD_CACHE.getIfPresent(key);
        if (cached != null) return cached;

        try {
            Method method = source.getDeclaredMethod(name, params);
            method.setAccessible(true);
            METHOD_CACHE.put(key, method);
            return method;

        } catch (NoSuchMethodException e) {
            Class<?> superClass = source.getSuperclass();
            return superClass == null ? null : getMethod(superClass, name, params);
        }
    }

    @Nullable
    public static Object invokeMethod(@NotNull Method method, @Nullable Object target, @Nullable Object... params) {
        try {
            return method.invoke(target, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    @NotNull
    public static Set<Class<?>> getTypesAnnotatedWith(
            @NotNull ClassLoader loader,
            @NotNull String basePackage,
            @NotNull Class<? extends Annotation> annotation
    ) {
        Set<Class<?>> result = new HashSet<>();
        Set<String> names = findClassNames(loader, basePackage);

        for (String name : names) {
            try {
                Class<?> clazz = Class.forName(name, false, loader);
                if (clazz.isAnnotationPresent(annotation)) {
                    result.add(clazz);
                }
            } catch (Throwable ignored) {}
        }

        return result;
    }

    @NotNull
    public static Set<Class<?>> getSubTypesOf(
            @NotNull ClassLoader loader,
            @NotNull String basePackage,
            @NotNull Class<?> parent
    ) {
        Set<Class<?>> result = new HashSet<>();
        Set<String> names = findClassNames(loader, basePackage);

        for (String name : names) {
            try {
                Class<?> clazz = Class.forName(name, false, loader);
                if (parent.isAssignableFrom(clazz) && !parent.equals(clazz)) {
                    result.add(clazz);
                }
            } catch (Throwable ignored) {}
        }

        return result;
    }

    @NotNull
    public static Set<Class<?>> getAllTypes(@NotNull ClassLoader loader, @NotNull String basePackage) {
        Set<Class<?>> result = new HashSet<>();
        Set<String> names = findClassNames(loader, basePackage);

        for (String name : names) {
            try {
                result.add(Class.forName(name, false, loader));
            } catch (Throwable ignored) {}
        }

        return result;
    }

    @NotNull
    private static Set<String> findClassNames(@NotNull ClassLoader loader, @NotNull String basePackage) {
        Set<String> result = new HashSet<>();
        String path = basePackage.replace('.', '/');

        try {
            Enumeration<URL> resources = loader.getResources(path);

            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                String protocol = url.getProtocol();

                try {
                    if ("file".equals(protocol)) {
                        URI uri = url.toURI();
                        File file = new File(uri);

                        if (file.isDirectory()) {
                            scanDirectory(file, basePackage, result);
                        } else if (file.getName().endsWith(".jar")) {
                            try (JarFile jar = new JarFile(file)) {
                                scanJar(jar, path, result);
                            }
                        } else {
                            JarFile jar = tryGetJarFromUrl(url);
                            if (jar != null) {
                                try (JarFile j = jar) {
                                    scanJar(j, path, result);
                                }
                            }
                        }
                    } else if ("jar".equals(protocol)) {
                        JarURLConnection conn = (JarURLConnection) url.openConnection();
                        try (JarFile jar = conn.getJarFile()) {
                            scanJar(jar, path, result);
                        }
                    } else {
                        JarFile jar = tryGetJarFromUrl(url);
                        if (jar != null) {
                            try (JarFile j = jar) {
                                scanJar(j, path, result);
                            }
                        } else {
                            File f = new File(URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8));
                            if (f.exists() && f.isDirectory()) {
                                scanDirectory(f, basePackage, result);
                            }
                        }
                    }
                } catch (URISyntaxException ignored) {}
            }
        } catch (IOException ignored) {}

        return result;
    }

    private static void scanDirectory(@NotNull File root, @NotNull String basePackage, @NotNull Set<String> out) {
        int rootPathLength = root.getAbsolutePath().length() + 1;
        Deque<File> stack = new ArrayDeque<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            File dir = stack.pop();
            File[] files = dir.listFiles();

            if (files == null) continue;

            for (File f : files) {
                if (f.isDirectory()) {
                    stack.push(f);
                    continue;
                }

                String name = f.getAbsolutePath();
                if (!name.endsWith(".class")) continue;

                String relative = name.substring(rootPathLength);
                String className = basePackage + '.' + relative.replace(
                        File.separatorChar, '.').replaceAll("\\.class$", "");

                out.add(className);
            }
        }
    }

    private static void scanJar(@NotNull JarFile jarFile, @NotNull String path, @NotNull Set<String> out) {
        Enumeration<JarEntry> entries = jarFile.entries();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();

            if (entry.isDirectory()) continue;
            if (!name.startsWith(path)) continue;
            if (!name.endsWith(".class")) continue;

            String className = name.replace('/', '.').substring(0, name.length() - 6);
            out.add(className);
        }
    }

    @Nullable
    private static JarFile tryGetJarFromUrl(@NotNull URL url) {
        try {
            String spec = url.toExternalForm();
            int idx = spec.indexOf("!/");
            String jarPath = idx != -1 ? spec.substring(0, idx) : spec;

            String filePath = jarPath.startsWith("jar:") ? jarPath.substring(4) : jarPath;
            int fileIdx = filePath.indexOf("file:");
            String clean = fileIdx != -1 ? filePath.substring(fileIdx + 5) : filePath;

            String decoded = URLDecoder.decode(clean, StandardCharsets.UTF_8);
            File file = new File(decoded);

            if (file.exists() && file.isFile()) {
                return new JarFile(file);
            }
        } catch (Throwable ignored) {}

        return null;
    }
}
