package me.lime.lightCore.api.messaging;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A fluent builder for sending various types of messages to players
 * Supports: Chat, ActionBar, Title, BossBar with placeholders and sounds
 */
public class MessageBuilder {
    
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    
    private CommandSender target;
    private String message;
    private String[] placeholders = {};
    
    // Title settings
    private String titleMain;
    private String titleSub;
    private int fadeIn = 10;
    private int stay = 70;
    private int fadeOut = 20;
    
    // BossBar settings
    private String bossbarTitle;
    private float bossbarProgress = 1.0f;
    private BossBar.Color bossbarColor = BossBar.Color.GREEN;
    private BossBar.Overlay bossbarOverlay = BossBar.Overlay.PROGRESS;
    private int bossbarDuration = 100; // ticks
    private JavaPlugin plugin;
    
    // Sound settings
    private Sound sound;
    private float volume = 1.0f;
    private float pitch = 1.0f;
    
    private MessageBuilder() {}
    
    /**
     * Create a new MessageBuilder instance
     * @return A new MessageBuilder
     */
    @NotNull
    public static MessageBuilder create() {
        return new MessageBuilder();
    }
    
    /**
     * Set the target recipient
     * @param target The CommandSender to send to
     * @return This builder
     */
    @NotNull
    public MessageBuilder to(@NotNull CommandSender target) {
        this.target = target;
        return this;
    }
    
    /**
     * Set the message content
     * @param message The message text (supports hex colors with &#RRGGBB format)
     * @return This builder
     */
    @NotNull
    public MessageBuilder message(@NotNull String message) {
        this.message = message;
        return this;
    }
    
    /**
     * Set placeholders to replace in the message
     * @param placeholders Pairs of placeholder and value (e.g., "{player}", "Steve", "{count}", "5")
     * @return This builder
     */
    @NotNull
    public MessageBuilder placeholders(String... placeholders) {
        this.placeholders = placeholders;
        return this;
    }
    
    /**
     * Set the title text (for TITLE type)
     * @param main The main title text
     * @return This builder
     */
    @NotNull
    public MessageBuilder title(@NotNull String main) {
        this.titleMain = main;
        return this;
    }
    
    /**
     * Set the title and subtitle text (for TITLE type)
     * @param main The main title text
     * @param sub The subtitle text
     * @return This builder
     */
    @NotNull
    public MessageBuilder title(@NotNull String main, @Nullable String sub) {
        this.titleMain = main;
        this.titleSub = sub;
        return this;
    }
    
    /**
     * Set the title timing (for TITLE type)
     * @param fadeIn Fade in time in ticks
     * @param stay Stay time in ticks
     * @param fadeOut Fade out time in ticks
     * @return This builder
     */
    @NotNull
    public MessageBuilder titleTimes(int fadeIn, int stay, int fadeOut) {
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
        return this;
    }
    
    /**
     * Set the bossbar title (for BOSSBAR type)
     * @param title The bossbar title text
     * @return This builder
     */
    @NotNull
    public MessageBuilder bossbar(@NotNull String title) {
        this.bossbarTitle = title;
        return this;
    }
    
    /**
     * Set the bossbar progress (for BOSSBAR type)
     * @param progress Progress from 0.0 to 1.0
     * @return This builder
     */
    @NotNull
    public MessageBuilder bossbarProgress(float progress) {
        this.bossbarProgress = Math.max(0f, Math.min(1f, progress));
        return this;
    }
    
    /**
     * Set the bossbar color (for BOSSBAR type)
     * @param color The bossbar color
     * @return This builder
     */
    @NotNull
    public MessageBuilder bossbarColor(@NotNull BossBar.Color color) {
        this.bossbarColor = color;
        return this;
    }
    
    /**
     * Set the bossbar color by name (for BOSSBAR type)
     * @param color The color name (PINK, BLUE, RED, GREEN, YELLOW, PURPLE, WHITE)
     * @return This builder
     */
    @NotNull
    public MessageBuilder bossbarColor(@NotNull String color) {
        try {
            this.bossbarColor = BossBar.Color.valueOf(color.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ignored) {}
        return this;
    }
    
    /**
     * Set the bossbar overlay (for BOSSBAR type)
     * @param overlay The bossbar overlay
     * @return This builder
     */
    @NotNull
    public MessageBuilder bossbarOverlay(@NotNull BossBar.Overlay overlay) {
        this.bossbarOverlay = overlay;
        return this;
    }
    
    /**
     * Set the bossbar duration in ticks (for BOSSBAR type)
     * @param ticks Duration in ticks (20 ticks = 1 second), 0 for permanent
     * @return This builder
     */
    @NotNull
    public MessageBuilder bossbarDuration(int ticks) {
        this.bossbarDuration = ticks;
        return this;
    }
    
    /**
     * Set the plugin for async tasks (required for bossbar auto-hide)
     * @param plugin The plugin instance
     * @return This builder
     */
    @NotNull
    public MessageBuilder plugin(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        return this;
    }
    
    /**
     * Set the sound to play
     * @param sound The sound to play
     * @return This builder
     */
    @NotNull
    public MessageBuilder sound(@NotNull Sound sound) {
        this.sound = sound;
        return this;
    }
    
    /**
     * Set the sound by name
     * @param soundName The sound name (e.g., "ENTITY_PLAYER_LEVELUP")
     * @return This builder
     */
    @NotNull
    public MessageBuilder sound(@NotNull String soundName) {
        try {
            this.sound = Sound.valueOf(soundName.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ignored) {}
        return this;
    }
    
    /**
     * Set the sound volume
     * @param volume Volume (1.0 is normal)
     * @return This builder
     */
    @NotNull
    public MessageBuilder volume(float volume) {
        this.volume = volume;
        return this;
    }
    
    /**
     * Set the sound pitch
     * @param pitch Pitch (1.0 is normal)
     * @return This builder
     */
    @NotNull
    public MessageBuilder pitch(float pitch) {
        this.pitch = pitch;
        return this;
    }
    
    /**
     * Send a chat message
     */
    public void sendChat() {
        if (target == null || message == null) return;
        target.sendMessage(colorize(format(message)));
    }
    
    /**
     * Send an action bar message (player only)
     */
    public void sendActionbar() {
        if (!(target instanceof Player player)) return;
        if (message == null) return;
        player.sendActionBar(toComponent(format(message)));
    }
    
    /**
     * Send a title message (player only)
     */
    public void sendTitle() {
        if (!(target instanceof Player player)) return;
        
        String main = titleMain != null ? titleMain : message;
        if (main == null) return;
        
        player.showTitle(Title.title(
                toComponent(format(main)),
                toComponent(format(titleSub != null ? titleSub : "")),
                Title.Times.times(
                        Duration.ofMillis(fadeIn * 50L),
                        Duration.ofMillis(stay * 50L),
                        Duration.ofMillis(fadeOut * 50L)
                )
        ));
    }
    
    /**
     * Send a boss bar message (player only)
     */
    public void sendBossbar() {
        if (!(target instanceof Player player)) return;
        
        String title = bossbarTitle != null ? bossbarTitle : message;
        if (title == null) return;
        
        BossBar bossbar = BossBar.bossBar(
                toComponent(format(title)),
                bossbarProgress,
                bossbarColor,
                bossbarOverlay
        );
        
        player.showBossBar(bossbar);
        
        if (bossbarDuration > 0 && plugin != null) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> 
                    player.hideBossBar(bossbar), bossbarDuration);
        }
    }
    
    /**
     * Play the configured sound (player only)
     */
    public void playSound() {
        if (!(target instanceof Player player)) return;
        if (sound == null) return;
        player.playSound(player.getLocation(), sound, volume, pitch);
    }
    
    /**
     * Send a message of the specified type
     * @param type The message type
     */
    public void send(@NotNull MessageType type) {
        switch (type) {
            case CHAT -> sendChat();
            case ACTIONBAR -> sendActionbar();
            case TITLE -> sendTitle();
            case BOSSBAR -> sendBossbar();
        }
    }
    
    /**
     * Send multiple message types at once
     * @param types Comma-separated message types (e.g., "chat,actionbar")
     */
    public void send(@NotNull String types) {
        for (String type : types.split(",")) {
            send(MessageType.get(type.trim()));
        }
    }
    
    /**
     * Apply placeholders to the input string
     */
    @NotNull
    private String format(@NotNull String input) {
        if (input.isEmpty()) return input;
        String result = input;
        if (placeholders != null && placeholders.length > 0) {
            for (int i = 0; i + 1 < placeholders.length; i += 2) {
                result = result.replace(placeholders[i], placeholders[i + 1]);
            }
        }
        return result;
    }
    
    /**
     * Translate hex colors in the format &#RRGGBB
     */
    @NotNull
    private String colorize(@NotNull String text) {
        Matcher matcher = HEX_PATTERN.matcher(text);
        StringBuilder buffer = new StringBuilder();
        
        while (matcher.find()) {
            String hex = matcher.group(1);
            matcher.appendReplacement(buffer, ChatColor.of("#" + hex).toString());
        }
        matcher.appendTail(buffer);
        
        return ChatColor.translateAlternateColorCodes('&', buffer.toString());
    }
    
    /**
     * Convert text to Adventure Component with color support
     */
    @NotNull
    private Component toComponent(@NotNull String text) {
        // First apply legacy colors
        String colored = colorize(text);
        // Then convert to component
        return LegacyComponentSerializer.legacySection().deserialize(colored);
    }
}
