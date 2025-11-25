package me.lime.lightCore.api.messaging;

import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Quick static utility for sending messages
 * For more control, use MessageBuilder.create()
 */
public final class Message {
    
    private Message() {}
    
    /**
     * Send a chat message with color support
     * @param sender The recipient
     * @param message The message (supports &#RRGGBB hex colors)
     */
    public static void chat(@NotNull CommandSender sender, @NotNull String message) {
        MessageBuilder.create()
                .to(sender)
                .message(message)
                .sendChat();
    }
    
    /**
     * Send a chat message with placeholders
     * @param sender The recipient
     * @param message The message (supports &#RRGGBB hex colors)
     * @param placeholders Pairs of placeholder and value
     */
    public static void chat(@NotNull CommandSender sender, @NotNull String message, String... placeholders) {
        MessageBuilder.create()
                .to(sender)
                .message(message)
                .placeholders(placeholders)
                .sendChat();
    }
    
    /**
     * Send an action bar message (player only)
     * @param player The player
     * @param message The message (supports &#RRGGBB hex colors)
     */
    public static void actionbar(@NotNull Player player, @NotNull String message) {
        MessageBuilder.create()
                .to(player)
                .message(message)
                .sendActionbar();
    }
    
    /**
     * Send an action bar message with placeholders (player only)
     * @param player The player
     * @param message The message (supports &#RRGGBB hex colors)
     * @param placeholders Pairs of placeholder and value
     */
    public static void actionbar(@NotNull Player player, @NotNull String message, String... placeholders) {
        MessageBuilder.create()
                .to(player)
                .message(message)
                .placeholders(placeholders)
                .sendActionbar();
    }
    
    /**
     * Send a title message (player only)
     * @param player The player
     * @param title The main title text
     * @param subtitle The subtitle text (can be null)
     */
    public static void title(@NotNull Player player, @NotNull String title, String subtitle) {
        MessageBuilder.create()
                .to(player)
                .title(title, subtitle)
                .sendTitle();
    }
    
    /**
     * Send a title message with custom timing (player only)
     * @param player The player
     * @param title The main title text
     * @param subtitle The subtitle text (can be null)
     * @param fadeIn Fade in time in ticks
     * @param stay Stay time in ticks
     * @param fadeOut Fade out time in ticks
     */
    public static void title(@NotNull Player player, @NotNull String title, String subtitle, 
                            int fadeIn, int stay, int fadeOut) {
        MessageBuilder.create()
                .to(player)
                .title(title, subtitle)
                .titleTimes(fadeIn, stay, fadeOut)
                .sendTitle();
    }
    
    /**
     * Show a boss bar (player only)
     * @param player The player
     * @param plugin The plugin instance (for auto-hide scheduling)
     * @param title The boss bar title
     * @param color The boss bar color
     * @param durationTicks Duration in ticks (0 for permanent)
     */
    public static void bossbar(@NotNull Player player, @NotNull JavaPlugin plugin,
                               @NotNull String title, @NotNull BossBar.Color color, int durationTicks) {
        MessageBuilder.create()
                .to(player)
                .plugin(plugin)
                .bossbar(title)
                .bossbarColor(color)
                .bossbarDuration(durationTicks)
                .sendBossbar();
    }
    
    /**
     * Show a boss bar with progress (player only)
     * @param player The player
     * @param plugin The plugin instance (for auto-hide scheduling)
     * @param title The boss bar title
     * @param progress Progress from 0.0 to 1.0
     * @param color The boss bar color
     * @param durationTicks Duration in ticks (0 for permanent)
     */
    public static void bossbar(@NotNull Player player, @NotNull JavaPlugin plugin,
                               @NotNull String title, float progress, 
                               @NotNull BossBar.Color color, int durationTicks) {
        MessageBuilder.create()
                .to(player)
                .plugin(plugin)
                .bossbar(title)
                .bossbarProgress(progress)
                .bossbarColor(color)
                .bossbarDuration(durationTicks)
                .sendBossbar();
    }
    
    /**
     * Play a sound to a player
     * @param player The player
     * @param sound The sound to play
     */
    public static void sound(@NotNull Player player, @NotNull Sound sound) {
        MessageBuilder.create()
                .to(player)
                .sound(sound)
                .playSound();
    }
    
    /**
     * Play a sound to a player with volume and pitch
     * @param player The player
     * @param sound The sound to play
     * @param volume Volume (1.0 is normal)
     * @param pitch Pitch (1.0 is normal)
     */
    public static void sound(@NotNull Player player, @NotNull Sound sound, float volume, float pitch) {
        MessageBuilder.create()
                .to(player)
                .sound(sound)
                .volume(volume)
                .pitch(pitch)
                .playSound();
    }
}
