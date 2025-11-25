package me.lime.lightCore.api.util;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class for PersistentDataContainer operations
 */
public final class PDCUtil {

    private PDCUtil() {}

    private static JavaPlugin plugin;

    /**
     * Initialize with your plugin instance
     * @param pluginInstance Your plugin
     */
    public static void init(@NotNull JavaPlugin pluginInstance) {
        plugin = pluginInstance;
    }

    /**
     * Set a value in a PDC
     * @param pdc The container
     * @param key The key
     * @param type The data type
     * @param value The value
     */
    public static <T, Z> void set(
            @NotNull PersistentDataContainer pdc,
            @NotNull String key,
            @NotNull PersistentDataType<T, Z> type,
            @NotNull Z value
    ) {
        pdc.set(createKey(key), type, value);
    }

    /**
     * Get a value from a PDC
     * @param pdc The container
     * @param key The key
     * @param type The data type
     * @return The value, or null
     */
    @Nullable
    public static <T, Z> Z get(
            @NotNull PersistentDataContainer pdc,
            @NotNull String key,
            @NotNull PersistentDataType<T, Z> type
    ) {
        return pdc.get(createKey(key), type);
    }

    /**
     * Check if a PDC has a key
     * @param pdc The container
     * @param key The key
     * @param type The data type
     * @return true if the key exists
     */
    public static boolean has(
            @NotNull PersistentDataContainer pdc,
            @NotNull String key,
            @NotNull PersistentDataType<?, ?> type
    ) {
        return pdc.has(createKey(key), type);
    }

    /**
     * Remove a key from a PDC
     * @param pdc The container
     * @param key The key
     */
    public static void remove(@NotNull PersistentDataContainer pdc, @NotNull String key) {
        pdc.remove(createKey(key));
    }

    // ==================== Convenience Methods ====================

    /**
     * Get a boolean value
     */
    public static boolean getBoolean(@NotNull PersistentDataContainer pdc, @NotNull String key) {
        Byte value = get(pdc, key, PersistentDataType.BYTE);
        return value != null && value == (byte) 1;
    }

    /**
     * Set a boolean value
     */
    public static void setBoolean(@NotNull PersistentDataContainer pdc, @NotNull String key, boolean value) {
        set(pdc, key, PersistentDataType.BYTE, (byte) (value ? 1 : 0));
    }

    /**
     * Get a string value
     */
    @Nullable
    public static String getString(@NotNull PersistentDataContainer pdc, @NotNull String key) {
        return get(pdc, key, PersistentDataType.STRING);
    }

    /**
     * Set a string value
     */
    public static void setString(@NotNull PersistentDataContainer pdc, @NotNull String key, @NotNull String value) {
        set(pdc, key, PersistentDataType.STRING, value);
    }

    /**
     * Get an integer value
     */
    public static int getInt(@NotNull PersistentDataContainer pdc, @NotNull String key, int def) {
        Integer value = get(pdc, key, PersistentDataType.INTEGER);
        return value != null ? value : def;
    }

    /**
     * Set an integer value
     */
    public static void setInt(@NotNull PersistentDataContainer pdc, @NotNull String key, int value) {
        set(pdc, key, PersistentDataType.INTEGER, value);
    }

    /**
     * Get a long value
     */
    public static long getLong(@NotNull PersistentDataContainer pdc, @NotNull String key, long def) {
        Long value = get(pdc, key, PersistentDataType.LONG);
        return value != null ? value : def;
    }

    /**
     * Set a long value
     */
    public static void setLong(@NotNull PersistentDataContainer pdc, @NotNull String key, long value) {
        set(pdc, key, PersistentDataType.LONG, value);
    }

    /**
     * Get a double value
     */
    public static double getDouble(@NotNull PersistentDataContainer pdc, @NotNull String key, double def) {
        Double value = get(pdc, key, PersistentDataType.DOUBLE);
        return value != null ? value : def;
    }

    /**
     * Set a double value
     */
    public static void setDouble(@NotNull PersistentDataContainer pdc, @NotNull String key, double value) {
        set(pdc, key, PersistentDataType.DOUBLE, value);
    }

    @NotNull
    private static NamespacedKey createKey(@NotNull String key) {
        if (plugin == null) {
            throw new IllegalStateException("PDCUtil not initialized! Call PDCUtil.init(plugin) first.");
        }
        return new NamespacedKey(plugin, key);
    }
}
