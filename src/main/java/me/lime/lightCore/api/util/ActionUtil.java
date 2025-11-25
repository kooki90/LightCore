package me.lime.lightCore.api.util;

import me.clip.placeholderapi.PlaceholderAPI;
import me.lime.lightCore.api.logging.ConsoleLogger;
import me.lime.lightCore.api.messaging.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Utility class for executing config-defined actions
 * 
 * Supported action formats:
 * - [playercommand] /command
 * - [consolecommand] /command
 * - [consolecommandchance] 50;/command
 * - [message] &aHello!
 * - [broadcast] &aHello everyone!
 * - [title] &aMain Title;&7Subtitle
 * - [subtitle] &7Subtitle only
 * - [actionbar] &aAction bar message
 * - [sound] ENTITY_PLAYER_LEVELUP
 */
public final class ActionUtil {

    private ActionUtil() {}

    private static boolean papiEnabled = false;

    /**
     * Check if PlaceholderAPI is available
     */
    public static void checkPAPI() {
        papiEnabled = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    /**
     * Parse placeholders if PAPI is available
     */
    @NotNull
    private static String parse(@Nullable OfflinePlayer player, @Nullable String text) {
        if (text == null || text.isEmpty()) return "";
        if (!papiEnabled || player == null) return text;
        return PlaceholderAPI.setPlaceholders(player, text);
    }

    /**
     * Execute a list of actions from config
     * @param player The player to execute for
     * @param actions List of action strings
     */
    public static void execute(@NotNull Player player, @NotNull List<String> actions) {
        if (actions.isEmpty()) return;

        for (String action : actions) {
            perform(action, player);
        }
    }

    /**
     * Perform a single action
     * @param raw The raw action string (e.g., "[message] &aHello!")
     * @param player The player
     */
    public static void perform(@Nullable String raw, @NotNull Player player) {
        if (raw == null || raw.isEmpty()) return;

        int space = raw.indexOf(" ");
        if (space == -1) return;

        String type = raw.substring(0, space).toLowerCase();
        String rawContent = raw.substring(space + 1);

        switch (type) {
            case "[playercommand]" -> {
                String command = parse(player, rawContent.replace("<player>", player.getName()));
                // Remove leading slash if present
                if (command.startsWith("/")) command = command.substring(1);
                String finalCmd = command;
                SchedulerUtil.sync(() -> player.performCommand(finalCmd));
            }

            case "[consolecommand]" -> {
                String command = parse(player, rawContent.replace("<player>", player.getName()));
                if (command.startsWith("/")) command = command.substring(1);
                String finalCmd = command;
                SchedulerUtil.sync(() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCmd));
            }

            case "[consolecommandchance]" -> {
                String[] split = rawContent.split(";", 2);
                if (split.length != 2) return;

                try {
                    double chance = Double.parseDouble(split[0].trim());
                    String command = parse(player, split[1].replace("<player>", player.getName()));
                    if (command.startsWith("/")) command = command.substring(1);

                    if (MathUtil.chance(chance)) {
                        String finalCmd = command;
                        SchedulerUtil.sync(() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCmd));
                    }
                } catch (NumberFormatException e) {
                    ConsoleLogger.warn("Invalid chance format in action: " + rawContent);
                }
            }

            case "[title]" -> {
                String[] split = rawContent.split(";", 2);
                String main = parse(player, split[0].replace("<player>", player.getName()));
                String sub = split.length > 1 
                        ? parse(player, split[1].replace("<player>", player.getName())) 
                        : "";

                Message.title(player, main, sub);
            }

            case "[subtitle]" -> {
                String content = parse(player, rawContent.replace("<player>", player.getName()));
                Message.title(player, "", content);
            }

            case "[actionbar]" -> {
                String content = parse(player, rawContent.replace("<player>", player.getName()));
                Message.actionbar(player, content);
            }

            case "[message]" -> {
                String content = parse(player, rawContent.replace("<player>", player.getName()));
                Message.chat(player, content);
            }

            case "[broadcast]" -> {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    String content = parse(p, rawContent.replace("<player>", p.getName()));
                    Message.chat(p, content);
                }
            }

            case "[sound]" -> {
                try {
                    String[] parts = rawContent.split(";");
                    Sound sound = Sound.valueOf(parts[0].toUpperCase().trim());
                    float volume = parts.length > 1 ? Float.parseFloat(parts[1].trim()) : 1.0f;
                    float pitch = parts.length > 2 ? Float.parseFloat(parts[2].trim()) : 1.0f;
                    Message.sound(player, sound, volume, pitch);
                } catch (IllegalArgumentException e) {
                    ConsoleLogger.warn("Invalid sound in action: " + rawContent);
                }
            }

            default -> ConsoleLogger.warn("Unknown action type: " + type);
        }
    }
}
