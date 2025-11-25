package me.lime.lightCore.api.config;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * A wrapper around Bukkit's YamlConfiguration with caching support
 */
public final class ConfigService {

    private final JavaPlugin plugin;
    private final String fileName;
    private final Map<String, Object> cache;
    private FileConfiguration config;
    private Path path;
    private File file;

    private ConfigService(@NotNull JavaPlugin plugin, @NotNull String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.cache = new ConcurrentHashMap<>();
    }

    /**
     * Create a new ConfigService for a plugin
     * @param plugin The plugin instance
     * @param fileName The config file name (e.g., "config.yml")
     * @return The ConfigService instance
     */
    @NotNull
    public static ConfigService create(@NotNull JavaPlugin plugin, @NotNull String fileName) {
        ConfigService service = new ConfigService(plugin, fileName);
        service.load();
        return service;
    }

    /**
     * Create a ConfigService for the default config.yml
     * @param plugin The plugin instance
     * @return The ConfigService instance
     */
    @NotNull
    public static ConfigService create(@NotNull JavaPlugin plugin) {
        return create(plugin, "config.yml");
    }

    /**
     * Load or reload the configuration
     * @return This ConfigService
     */
    @NotNull
    public ConfigService load() {
        try {
            this.path = plugin.getDataFolder().toPath().resolve(fileName);
            this.file = path.toFile();

            Files.createDirectories(path.getParent());

            if (!Files.exists(path)) {
                String resourcePath = fileName.replace("\\", "/");
                if (plugin.getResource(resourcePath) != null) {
                    plugin.saveResource(resourcePath, false);
                } else {
                    Files.createFile(path);
                }
            }

            config = YamlConfiguration.loadConfiguration(file);
            cache.clear();

            // Cache all values
            config.getKeys(true).forEach(key -> cache.put(key, config.get(key)));

        } catch (IOException e) {
            plugin.getLogger().severe("Failed to load config: " + fileName);
            e.printStackTrace();
        }

        return this;
    }

    /**
     * Reload the configuration from disk
     */
    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
        cache.clear();
        config.getKeys(true).forEach(key -> cache.put(key, config.get(key)));
    }

    /**
     * Save the configuration to disk
     */
    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save config: " + fileName);
            e.printStackTrace();
        }
    }

    /**
     * Get the underlying FileConfiguration
     * @return The FileConfiguration
     */
    @NotNull
    public FileConfiguration getConfig() {
        return config;
    }

    /**
     * Get the config file
     * @return The File
     */
    @NotNull
    public File getFile() {
        return file;
    }

    // ==================== Basic Getters ====================

    /**
     * Get a value from the config
     * @param key The config key
     * @return The value, or null
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public <T> T get(@NotNull String key) {
        return (T) cache.get(key);
    }

    /**
     * Get a value with a default
     * @param key The config key
     * @param def The default value
     * @return The value, or the default
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public <T> T get(@NotNull String key, @NotNull T def) {
        Object value = cache.get(key);
        return value != null ? (T) value : def;
    }

    /**
     * Get the raw value from config (bypasses cache)
     * @param key The config key
     * @return The raw value
     */
    @Nullable
    public Object getRaw(@NotNull String key) {
        return config.get(key);
    }

    /**
     * Set a value in the config
     * @param key The config key
     * @param value The value to set
     * @return This ConfigService
     */
    @NotNull
    public ConfigService set(@NotNull String key, @Nullable Object value) {
        cache.put(key, value);
        config.set(key, value);
        return this;
    }

    /**
     * Check if a key exists
     * @param key The config key
     * @return true if the key exists
     */
    public boolean contains(@NotNull String key) {
        return config.contains(key);
    }

    /**
     * Get all keys
     * @param deep Whether to include nested keys
     * @return Set of keys
     */
    @NotNull
    public Set<String> getKeys(boolean deep) {
        return config.getKeys(deep);
    }

    // ==================== String ====================

    @Nullable
    public String getString(@NotNull String key) {
        Object val = getRaw(key);
        if (val instanceof String) return (String) val;
        if (val instanceof List<?>) {
            return ((List<?>) val).stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
        }
        return val != null ? val.toString() : null;
    }

    @NotNull
    public String getString(@NotNull String key, @NotNull String def) {
        String val = getString(key);
        return val != null ? val : def;
    }

    // ==================== Numbers ====================

    public int getInt(@NotNull String key) {
        return get(key, 0);
    }

    public int getInt(@NotNull String key, int def) {
        return get(key, def);
    }

    public double getDouble(@NotNull String key) {
        return get(key, 0.0D);
    }

    public double getDouble(@NotNull String key, double def) {
        return get(key, def);
    }

    public float getFloat(@NotNull String key) {
        Object v = getRaw(key);
        return v instanceof Number ? ((Number) v).floatValue() : 0F;
    }

    public float getFloat(@NotNull String key, float def) {
        Object v = getRaw(key);
        return v instanceof Number ? ((Number) v).floatValue() : def;
    }

    public long getLong(@NotNull String key) {
        Object v = getRaw(key);
        return v instanceof Number ? ((Number) v).longValue() : 0L;
    }

    public long getLong(@NotNull String key, long def) {
        Object v = getRaw(key);
        return v instanceof Number ? ((Number) v).longValue() : def;
    }

    public short getShort(@NotNull String key) {
        Object v = getRaw(key);
        return v instanceof Number ? ((Number) v).shortValue() : 0;
    }

    public short getShort(@NotNull String key, short def) {
        Object v = getRaw(key);
        return v instanceof Number ? ((Number) v).shortValue() : def;
    }

    public byte getByte(@NotNull String key) {
        Object v = getRaw(key);
        return v instanceof Number ? ((Number) v).byteValue() : 0;
    }

    public byte getByte(@NotNull String key, byte def) {
        Object v = getRaw(key);
        return v instanceof Number ? ((Number) v).byteValue() : def;
    }

    // ==================== Boolean ====================

    public boolean getBoolean(@NotNull String key) {
        return get(key, false);
    }

    public boolean getBoolean(@NotNull String key, boolean def) {
        return get(key, def);
    }

    // ==================== Lists ====================

    @Nullable
    public List<?> getList(@NotNull String key) {
        return get(key);
    }

    @NotNull
    public List<?> getList(@NotNull String key, @NotNull List<?> def) {
        return get(key, def);
    }

    @NotNull
    public List<String> getStringList(@NotNull String key) {
        return config.getStringList(key);
    }

    @NotNull
    public List<Integer> getIntegerList(@NotNull String key) {
        return config.getIntegerList(key);
    }

    @NotNull
    public List<Double> getDoubleList(@NotNull String key) {
        return config.getDoubleList(key);
    }

    @NotNull
    public List<Long> getLongList(@NotNull String key) {
        return config.getLongList(key);
    }

    @NotNull
    public List<Boolean> getBooleanList(@NotNull String key) {
        return config.getBooleanList(key);
    }

    // ==================== Complex Types ====================

    @Nullable
    public ConfigurationSection getSection(@NotNull String key) {
        return config.getConfigurationSection(key);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public Map<String, Object> getMap(@NotNull String key) {
        Object raw = get(key);
        return raw instanceof Map ? (Map<String, Object>) raw : null;
    }

    @Nullable
    public Location getLocation(@NotNull String key) {
        return config.getLocation(key);
    }

    @Nullable
    public Vector getVector(@NotNull String key) {
        return config.getVector(key);
    }

    @Nullable
    public ItemStack getItemStack(@NotNull String key) {
        return config.getItemStack(key);
    }

    @Nullable
    public OfflinePlayer getOfflinePlayer(@NotNull String key) {
        return config.getOfflinePlayer(key);
    }

    /**
     * Clear the cache
     */
    public void clearCache() {
        cache.clear();
    }
}
