package me.lime.lightCore.api.util;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class for managing cooldowns
 */
public final class CooldownUtil {

    private CooldownUtil() {}

    private static final Map<String, Map<UUID, Long>> COOLDOWNS = new ConcurrentHashMap<>();

    /**
     * Check if a player is on cooldown
     * @param key The cooldown key
     * @param uuid The player UUID
     * @return true if on cooldown
     */
    public static boolean isOnCooldown(@NotNull String key, @NotNull UUID uuid) {
        Map<UUID, Long> cache = COOLDOWNS.get(key);
        if (cache == null) return false;

        Long expiry = cache.get(uuid);
        if (expiry == null) return false;

        if (expiry <= System.currentTimeMillis()) {
            cache.remove(uuid);
            return false;
        }

        return true;
    }

    /**
     * Check if a player is on cooldown
     * @param key The cooldown key
     * @param player The player
     * @return true if on cooldown
     */
    public static boolean isOnCooldown(@NotNull String key, @NotNull Player player) {
        return isOnCooldown(key, player.getUniqueId());
    }

    /**
     * Get remaining cooldown time in milliseconds
     * @param key The cooldown key
     * @param uuid The player UUID
     * @return Remaining time in ms, or 0 if not on cooldown
     */
    public static long getRemaining(@NotNull String key, @NotNull UUID uuid) {
        Map<UUID, Long> cache = COOLDOWNS.get(key);
        if (cache == null) return 0L;

        Long expiry = cache.get(uuid);
        return expiry == null ? 0L : Math.max(0, expiry - System.currentTimeMillis());
    }

    /**
     * Get remaining cooldown time in seconds
     * @param key The cooldown key
     * @param uuid The player UUID
     * @return Remaining time in seconds
     */
    public static long getRemainingSeconds(@NotNull String key, @NotNull UUID uuid) {
        return getRemaining(key, uuid) / 1000;
    }

    /**
     * Set a cooldown
     * @param key The cooldown key
     * @param uuid The player UUID
     * @param durationMs Duration in milliseconds
     */
    public static void setCooldown(@NotNull String key, @NotNull UUID uuid, long durationMs) {
        COOLDOWNS.computeIfAbsent(key, k -> new ConcurrentHashMap<>())
                .put(uuid, System.currentTimeMillis() + durationMs);
    }

    /**
     * Set a cooldown
     * @param key The cooldown key
     * @param player The player
     * @param durationMs Duration in milliseconds
     */
    public static void setCooldown(@NotNull String key, @NotNull Player player, long durationMs) {
        setCooldown(key, player.getUniqueId(), durationMs);
    }

    /**
     * Set a cooldown in seconds
     * @param key The cooldown key
     * @param uuid The player UUID
     * @param seconds Duration in seconds
     */
    public static void setCooldownSeconds(@NotNull String key, @NotNull UUID uuid, long seconds) {
        setCooldown(key, uuid, seconds * 1000);
    }

    /**
     * Clear a specific cooldown
     * @param key The cooldown key
     * @param uuid The player UUID
     */
    public static void clear(@NotNull String key, @NotNull UUID uuid) {
        Map<UUID, Long> cache = COOLDOWNS.get(key);
        if (cache != null) {
            cache.remove(uuid);
        }
    }

    /**
     * Clear all cooldowns for a player
     * @param uuid The player UUID
     */
    public static void clearAll(@NotNull UUID uuid) {
        COOLDOWNS.values().forEach(cache -> cache.remove(uuid));
    }

    /**
     * Clear all cooldowns for a key
     * @param key The cooldown key
     */
    public static void clearKey(@NotNull String key) {
        COOLDOWNS.remove(key);
    }

    /**
     * Create a cooldown builder
     * @param key The cooldown key
     * @return A new CooldownBuilder
     */
    @NotNull
    public static CooldownBuilder of(@NotNull String key) {
        return new CooldownBuilder(key);
    }

    /**
     * Fluent builder for cooldowns
     */
    public static final class CooldownBuilder {
        private final String key;
        private UUID uuid;
        private long duration;

        private CooldownBuilder(@NotNull String key) {
            this.key = key;
        }

        @NotNull
        public CooldownBuilder player(@Nullable Player player) {
            this.uuid = player != null ? player.getUniqueId() : null;
            return this;
        }

        @NotNull
        public CooldownBuilder uuid(@NotNull UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        @NotNull
        public CooldownBuilder duration(long millis) {
            this.duration = millis;
            return this;
        }

        @NotNull
        public CooldownBuilder durationSeconds(long seconds) {
            this.duration = seconds * 1000;
            return this;
        }

        public void start() {
            if (uuid == null || duration <= 0) return;
            setCooldown(key, uuid, duration);
        }

        public boolean check() {
            return uuid != null && isOnCooldown(key, uuid);
        }

        public long remaining() {
            return uuid == null ? 0L : getRemaining(key, uuid);
        }

        public long remainingSeconds() {
            return remaining() / 1000;
        }

        public void clear() {
            if (uuid != null) {
                CooldownUtil.clear(key, uuid);
            }
        }
    }
}
