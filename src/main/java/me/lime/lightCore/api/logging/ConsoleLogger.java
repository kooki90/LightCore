package me.lime.lightCore.api.logging;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for logging messages to the console with colored log levels
 */
public final class ConsoleLogger {

    private ConsoleLogger() {}

    /**
     * Log an info message (light blue)
     * @param message The message to log
     */
    public static void info(@NotNull String message) {
        log(message, LogLevel.INFO);
    }

    /**
     * Log a warning message (yellow/orange)
     * @param message The message to log
     */
    public static void warn(@NotNull String message) {
        log(message, LogLevel.WARN);
    }

    /**
     * Log an error message (light red)
     * @param message The message to log
     */
    public static void error(@NotNull String message) {
        log(message, LogLevel.ERROR);
    }

    /**
     * Log a debug message (bright green)
     * @param message The message to log
     */
    public static void debug(@NotNull String message) {
        log(message, LogLevel.DEBUG);
    }

    /**
     * Log a fatal message (bright red)
     * @param message The message to log
     */
    public static void fatal(@NotNull String message) {
        log(message, LogLevel.FATAL);
    }

    /**
     * Log a success message (green)
     * @param message The message to log
     */
    public static void success(@NotNull String message) {
        log(message, LogLevel.SUCCESS);
    }

    /**
     * Log a message with a specific log level
     * @param message The message to log
     * @param level The log level
     */
    public static void log(@NotNull String message, @NotNull LogLevel level) {
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        console.sendMessage(format(message, level));
    }

    /**
     * Log a message with a custom prefix
     * @param prefix The prefix (e.g., plugin name)
     * @param message The message to log
     * @param level The log level
     */
    public static void log(@NotNull String prefix, @NotNull String message, @NotNull LogLevel level) {
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        console.sendMessage("§7[§f" + prefix + "§7] " + format(message, level));
    }

    /**
     * Log an info message with prefix
     */
    public static void info(@NotNull String prefix, @NotNull String message) {
        log(prefix, message, LogLevel.INFO);
    }

    /**
     * Log a warning message with prefix
     */
    public static void warn(@NotNull String prefix, @NotNull String message) {
        log(prefix, message, LogLevel.WARN);
    }

    /**
     * Log an error message with prefix
     */
    public static void error(@NotNull String prefix, @NotNull String message) {
        log(prefix, message, LogLevel.ERROR);
    }

    /**
     * Log a debug message with prefix
     */
    public static void debug(@NotNull String prefix, @NotNull String message) {
        log(prefix, message, LogLevel.DEBUG);
    }

    @NotNull
    private static String format(@NotNull String message, @NotNull LogLevel level) {
        return level.getColorCode() + message;
    }

    /**
     * Log levels with associated colors
     */
    public enum LogLevel {
        INFO("§x§F§F§F§B§C§9"),      // Light blue
        WARN("§x§F§F§E§D§0§0"),      // Yellow/Orange
        ERROR("§x§F§F§5§7§5§7"),     // Light red
        DEBUG("§x§8§A§F§F§0§0"),     // Bright green
        FATAL("§x§F§F§0§0§0§0"),     // Bright red
        SUCCESS("§x§0§0§F§F§0§0");   // Green

        private final String colorCode;

        LogLevel(@NotNull String colorCode) {
            this.colorCode = colorCode;
        }

        @NotNull
        public String getColorCode() {
            return colorCode;
        }
    }
}
