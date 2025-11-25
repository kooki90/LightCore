package me.lime.lightCore.api.config;

import me.lime.lightCore.api.logging.ConsoleLogger;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility for migrating config files when the plugin updates
 * Preserves user values while adding new config options
 */
public final class ConfigMigrator {

    private final JavaPlugin plugin;
    private final String fileName;
    private final String versionPath;
    private final String backupPath;
    private final boolean makeBackup;
    private final File file;

    private ConfigMigrator(
            @NotNull JavaPlugin plugin,
            @NotNull String fileName,
            @NotNull String versionPath,
            @NotNull String backupPath,
            boolean makeBackup
    ) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.versionPath = versionPath;
        this.backupPath = backupPath;
        this.makeBackup = makeBackup;
        this.file = new File(plugin.getDataFolder(), fileName);
    }

    /**
     * Create a ConfigMigrator builder
     * @param plugin The plugin instance
     * @return A new Builder
     */
    @NotNull
    public static Builder builder(@NotNull JavaPlugin plugin) {
        return new Builder(plugin);
    }

    /**
     * Create a ConfigMigrator with default settings
     * @param plugin The plugin instance
     * @param fileName The config file name
     * @param versionPath The path to the version number in config
     * @return A new ConfigMigrator
     */
    @NotNull
    public static ConfigMigrator of(
            @NotNull JavaPlugin plugin,
            @NotNull String fileName,
            @NotNull String versionPath
    ) {
        return builder(plugin)
                .fileName(fileName)
                .versionPath(versionPath)
                .build();
    }

    /**
     * Perform the migration if needed
     * @return true if migration was performed, false if already up to date
     */
    public boolean migrate() {
        YamlConfiguration latest = loadLatestConfig();
        YamlConfiguration current = loadCurrentConfig();

        int currentVer = current.getInt(versionPath, 0);
        int latestVer = latest.getInt(versionPath, 0);

        if (currentVer >= latestVer) {
            ConsoleLogger.info(plugin.getName(), "Config is up to date (v" + currentVer + ")");
            return false;
        }

        ConsoleLogger.warn(plugin.getName(), "Config outdated: v" + currentVer + " -> v" + latestVer);

        if (makeBackup) {
            backup(currentVer);
        }

        // Extract user values that exist in both configs
        Map<String, Object> preserved = extractPreserved(current, latest);

        // Override with latest config
        overrideConfig();

        // Restore user values
        restoreValues(preserved);

        ConsoleLogger.success(plugin.getName(), "Config migrated successfully to v" + latestVer);
        return true;
    }

    /**
     * Check if migration is needed
     * @return true if the config is outdated
     */
    public boolean needsMigration() {
        YamlConfiguration latest = loadLatestConfig();
        YamlConfiguration current = loadCurrentConfig();

        int currentVer = current.getInt(versionPath, 0);
        int latestVer = latest.getInt(versionPath, 0);

        return currentVer < latestVer;
    }

    /**
     * Get the current config version
     * @return The current version number
     */
    public int getCurrentVersion() {
        return loadCurrentConfig().getInt(versionPath, 0);
    }

    /**
     * Get the latest config version from resources
     * @return The latest version number
     */
    public int getLatestVersion() {
        return loadLatestConfig().getInt(versionPath, 0);
    }

    private void backup(int version) {
        File dir = new File(plugin.getDataFolder(), backupPath);

        if (!dir.exists() && !dir.mkdirs()) {
            ConsoleLogger.warn(plugin.getName(), "Failed to create backup directory");
            return;
        }

        try {
            File backupFile = new File(dir, fileName.replace(".yml", "") + "-v" + version + ".yml");
            Files.copy(file.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            ConsoleLogger.info(plugin.getName(), "Backup created: " + backupFile.getName());
        } catch (IOException e) {
            ConsoleLogger.error(plugin.getName(), "Failed to create backup: " + e.getMessage());
        }
    }

    @NotNull
    private Map<String, Object> extractPreserved(@NotNull YamlConfiguration current, @NotNull YamlConfiguration latest) {
        Map<String, Object> result = new HashMap<>();

        for (String key : latest.getKeys(true)) {
            // Skip version path
            if (key.equals(versionPath)) continue;

            // Only preserve if user has modified it
            if (current.contains(key) && !latest.isConfigurationSection(key)) {
                result.put(key, current.get(key));
            }
        }

        return result;
    }

    private void restoreValues(@NotNull Map<String, Object> values) {
        try {
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            values.forEach(yaml::set);
            yaml.save(file);
        } catch (IOException e) {
            ConsoleLogger.error(plugin.getName(), "Failed to restore config values: " + e.getMessage());
        }
    }

    private void overrideConfig() {
        try (InputStream in = getLatestStream()) {
            Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            ConsoleLogger.error(plugin.getName(), "Failed to override config: " + e.getMessage());
        }
    }

    @NotNull
    private YamlConfiguration loadLatestConfig() {
        try (Reader reader = new InputStreamReader(getLatestStream())) {
            return YamlConfiguration.loadConfiguration(reader);
        } catch (IOException e) {
            ConsoleLogger.error(plugin.getName(), "Failed to load latest config: " + e.getMessage());
            return new YamlConfiguration();
        }
    }

    @NotNull
    private InputStream getLatestStream() {
        InputStream in = plugin.getResource(fileName);
        if (in == null) {
            throw new IllegalStateException("Missing resource: " + fileName);
        }
        return in;
    }

    @NotNull
    private YamlConfiguration loadCurrentConfig() {
        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Builder for ConfigMigrator
     */
    public static final class Builder {
        private final JavaPlugin plugin;
        private String fileName = "config.yml";
        private String versionPath = "config-version";
        private String backupPath = "backups";
        private boolean makeBackup = true;

        private Builder(@NotNull JavaPlugin plugin) {
            this.plugin = plugin;
        }

        /**
         * Set the config file name
         * @param fileName The file name (e.g., "config.yml")
         * @return This builder
         */
        @NotNull
        public Builder fileName(@NotNull String fileName) {
            this.fileName = fileName;
            return this;
        }

        /**
         * Set the path to the version number in the config
         * @param versionPath The version path (e.g., "config-version")
         * @return This builder
         */
        @NotNull
        public Builder versionPath(@NotNull String versionPath) {
            this.versionPath = versionPath;
            return this;
        }

        /**
         * Set the backup directory path
         * @param backupPath The backup path (e.g., "backups")
         * @return This builder
         */
        @NotNull
        public Builder backupPath(@NotNull String backupPath) {
            this.backupPath = backupPath;
            return this;
        }

        /**
         * Set whether to make backups before migration
         * @param makeBackup true to make backups
         * @return This builder
         */
        @NotNull
        public Builder makeBackup(boolean makeBackup) {
            this.makeBackup = makeBackup;
            return this;
        }

        /**
         * Build the ConfigMigrator
         * @return The ConfigMigrator instance
         */
        @NotNull
        public ConfigMigrator build() {
            return new ConfigMigrator(plugin, fileName, versionPath, backupPath, makeBackup);
        }
    }
}
