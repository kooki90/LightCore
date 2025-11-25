package me.lime.lightCore.api.util;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Utility class for file operations
 */
public final class FileUtil {

    private FileUtil() {}

    @NotNull
    public static File get(@NotNull JavaPlugin plugin, @NotNull String path) {
        File file = new File(plugin.getDataFolder(), path);

        if (!file.exists()) {
            if (isFile(path)) {
                file.getParentFile().mkdirs();
            } else {
                file.mkdirs();
            }
        }

        return file;
    }

    public static void saveResource(@NotNull JavaPlugin plugin, @NotNull String path, boolean replace) {
        File file = get(plugin, path);
        if (!file.exists() || replace) {
            plugin.saveResource(path, replace);
        }
    }

    public static void write(@NotNull File file, @NotNull String content) throws IOException {
        file.getParentFile().mkdirs();
        Files.writeString(file.toPath(), content);
    }

    @NotNull
    public static String read(@NotNull File file) throws IOException {
        return Files.readString(file.toPath());
    }

    @NotNull
    public static List<String> readLines(@NotNull File file) throws IOException {
        return Files.readAllLines(file.toPath());
    }

    public static void copy(@NotNull File from, @NotNull File to) throws IOException {
        to.getParentFile().mkdirs();
        Files.copy(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void move(@NotNull File from, @NotNull File to) throws IOException {
        to.getParentFile().mkdirs();
        Files.move(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void delete(@NotNull File file) throws IOException {
        if (!file.exists()) return;

        if (file.isDirectory()) {
            for (File f : Objects.requireNonNull(file.listFiles())) {
                delete(f);
            }
        }

        if (!file.delete()) {
            throw new IOException("Failed to delete file: " + file);
        }
    }

    @NotNull
    public static List<File> listRecursive(@NotNull File folder) {
        if (!folder.exists() || !folder.isDirectory()) return List.of();

        return Arrays.stream(Objects.requireNonNull(folder.listFiles()))
                .flatMap(f -> f.isDirectory() ? listRecursive(f).stream() : Stream.of(f))
                .toList();
    }

    public static void create(@NotNull File file) throws IOException {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            Files.createFile(file.toPath());
        }
    }

    public static boolean exists(@NotNull File file) {
        return file.exists();
    }

    public static boolean isFile(@NotNull String path) {
        return path.contains(".");
    }

    public static boolean isFile(@NotNull File file) {
        return file.isFile();
    }

    public static boolean isDirectory(@NotNull File file) {
        return file.isDirectory();
    }

    public static boolean isHidden(@NotNull File file) {
        return file.isHidden();
    }

    @NotNull
    public static String extension(@NotNull File file) {
        String name = file.getName();
        int dot = name.lastIndexOf('.');
        return dot == -1 ? "" : name.substring(dot + 1);
    }

    @NotNull
    public static String fileName(@NotNull File file) {
        String name = file.getName();
        int dot = name.lastIndexOf('.');
        return dot == -1 ? name : name.substring(0, dot);
    }

    @NotNull
    public static String fileName(@NotNull String path) {
        return fileName(new File(path));
    }

    @NotNull
    public static File parent(@NotNull File file) {
        return file.getParentFile();
    }

    public static long size(@NotNull File file) {
        return file.exists() ? file.length() : 0L;
    }

    public static long lastModified(@NotNull File file) {
        return file.exists() ? file.lastModified() : 0L;
    }

    public static long linesCount(@NotNull File file) throws IOException {
        try (Stream<String> lines = Files.lines(file.toPath())) {
            return lines.count();
        }
    }

    @NotNull
    public static String normalizePath(@NotNull File file) {
        return file.toPath().normalize().toAbsolutePath().toString();
    }

    @NotNull
    public static Path toPath(@NotNull File file) {
        return file.toPath();
    }
}
