package me.lime.lightCore.api.command;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Utility class for command tab completions and common operations
 */
public final class CommandHelper {

    private CommandHelper() {}

    // Registered custom completions
    private static final Map<String, Supplier<Collection<String>>> COMPLETIONS = new HashMap<>();

    /**
     * Register a custom tab completion
     * @param id The completion ID (e.g., "ranks", "warps")
     * @param supplier Supplier that returns the completion options
     */
    public static void registerCompletion(@NotNull String id, @NotNull Supplier<Collection<String>> supplier) {
        COMPLETIONS.put(id.toLowerCase(), supplier);
    }

    /**
     * Get a registered completion
     * @param id The completion ID
     * @return The completion options, or empty list if not found
     */
    @NotNull
    public static Collection<String> getCompletion(@NotNull String id) {
        Supplier<Collection<String>> supplier = COMPLETIONS.get(id.toLowerCase());
        return supplier != null ? supplier.get() : Collections.emptyList();
    }

    /**
     * Unregister a custom completion
     * @param id The completion ID
     */
    public static void unregisterCompletion(@NotNull String id) {
        COMPLETIONS.remove(id.toLowerCase());
    }

    /**
     * Clear all registered completions
     */
    public static void clearCompletions() {
        COMPLETIONS.clear();
    }

    // ==================== Built-in Completions ====================

    /**
     * Get online player names (filtered by viewer visibility)
     * @param viewer The player viewing the tab completion (can be null)
     * @return List of visible player names
     */
    @NotNull
    public static List<String> getOnlinePlayers(@Nullable Player viewer) {
        return Bukkit.getOnlinePlayers().stream()
                .filter(p -> viewer == null || viewer.canSee(p))
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    /**
     * Get online player names
     * @return List of all online player names
     */
    @NotNull
    public static List<String> getOnlinePlayers() {
        return getOnlinePlayers(null);
    }

    /**
     * Get world names
     * @return List of all world names
     */
    @NotNull
    public static List<String> getWorlds() {
        return Bukkit.getWorlds().stream()
                .map(World::getName)
                .collect(Collectors.toList());
    }

    /**
     * Get boolean options
     * @return List containing "true" and "false"
     */
    @NotNull
    public static List<String> getBooleans() {
        return Arrays.asList("true", "false");
    }

    /**
     * Get number range as strings
     * @param min Minimum value (inclusive)
     * @param max Maximum value (inclusive)
     * @return List of numbers as strings
     */
    @NotNull
    public static List<String> getNumbers(int min, int max) {
        List<String> numbers = new ArrayList<>();
        for (int i = min; i <= max; i++) {
            numbers.add(String.valueOf(i));
        }
        return numbers;
    }

    // ==================== Tab Complete Helpers ====================

    /**
     * Filter completions by partial input
     * @param options Available options
     * @param partial Partial input to filter by
     * @return Filtered list matching the partial input
     */
    @NotNull
    public static List<String> filter(@NotNull Collection<String> options, @Nullable String partial) {
        if (partial == null || partial.isEmpty()) {
            return new ArrayList<>(options);
        }
        String lower = partial.toLowerCase();
        return options.stream()
                .filter(s -> s.toLowerCase().startsWith(lower))
                .collect(Collectors.toList());
    }

    /**
     * Filter player names by partial input
     * @param viewer The viewing player (for visibility check)
     * @param partial Partial input to filter by
     * @return Filtered list of player names
     */
    @NotNull
    public static List<String> filterPlayers(@Nullable Player viewer, @Nullable String partial) {
        return filter(getOnlinePlayers(viewer), partial);
    }

    /**
     * Filter world names by partial input
     * @param partial Partial input to filter by
     * @return Filtered list of world names
     */
    @NotNull
    public static List<String> filterWorlds(@Nullable String partial) {
        return filter(getWorlds(), partial);
    }

    // ==================== Command Sender Helpers ====================

    /**
     * Check if sender is a player
     * @param sender The command sender
     * @return true if sender is a player
     */
    public static boolean isPlayer(@NotNull CommandSender sender) {
        return sender instanceof Player;
    }

    /**
     * Get sender as player (or null if not a player)
     * @param sender The command sender
     * @return The player, or null
     */
    @Nullable
    public static Player asPlayer(@NotNull CommandSender sender) {
        return sender instanceof Player ? (Player) sender : null;
    }

    /**
     * Check if sender has permission
     * @param sender The command sender
     * @param permission The permission to check
     * @return true if sender has permission
     */
    public static boolean hasPermission(@NotNull CommandSender sender, @NotNull String permission) {
        return sender.hasPermission(permission);
    }

    /**
     * Create a simple TabCompleter
     * @param completions Map of argument index to completion supplier
     * @return A TabCompleter instance
     */
    @NotNull
    public static TabCompleter createTabCompleter(@NotNull Map<Integer, Supplier<List<String>>> completions) {
        return (sender, command, alias, args) -> {
            int index = args.length - 1;
            Supplier<List<String>> supplier = completions.get(index);
            if (supplier == null) return Collections.emptyList();
            return filter(supplier.get(), args[index]);
        };
    }

    /**
     * Create a TabCompleter with player filter for first argument
     * @return TabCompleter that suggests online players
     */
    @NotNull
    public static TabCompleter playerTabCompleter() {
        return (sender, command, alias, args) -> {
            if (args.length == 1) {
                Player viewer = asPlayer(sender);
                return filterPlayers(viewer, args[0]);
            }
            return Collections.emptyList();
        };
    }
}
