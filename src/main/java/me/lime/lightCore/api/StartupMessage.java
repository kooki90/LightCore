package me.lime.lightCore.api;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

public class StartupMessage {
    
    /**
     * Print a startup message with name and hex color
     * @param name The plugin/feature name
     * @param hex The hex color code (e.g., "&#FB7208")
     */
    public static void print(String name, String hex) {
        String coloredName = translateHex(hex) + name;
        
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(coloredName + ChatColor.GRAY + " has been enabled!");
        Bukkit.getConsoleSender().sendMessage("");
    }
    
    /**
     * Print a startup message with AUTO-GENERATED ASCII art from text
     * Uses built-in A-Z ASCII art generator
     * @param name The plugin/feature name (will be converted to ASCII art)
     * @param hex The hex color code (e.g., "&#FB7208")
     */
    public static void printWithAscii(String name, String hex) {
        String color = translateHex(hex);
        
        // Generate ASCII art automatically from text
        String[] asciiLines = AsciiArt.generate(name);
        
        Bukkit.getConsoleSender().sendMessage("");
        
        // Print ASCII art
        for (String line : asciiLines) {
            Bukkit.getConsoleSender().sendMessage(color + line);
        }
        
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(color + name + ChatColor.GRAY + " has been enabled!");
        Bukkit.getConsoleSender().sendMessage("");
    }
    
    /**
     * Print a startup message with CUSTOM ASCII art
     * @param name The plugin/feature name
     * @param hex The hex color code (e.g., "&#FB7208")
     * @param ascii The custom ASCII art lines
     */
    public static void printWithCustomAscii(String name, String hex, String... ascii) {
        String color = translateHex(hex);
        
        Bukkit.getConsoleSender().sendMessage("");
        
        // Print custom ASCII art
        for (String line : ascii) {
            Bukkit.getConsoleSender().sendMessage(color + line);
        }
        
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(color + name + ChatColor.GRAY + " has been enabled!");
        Bukkit.getConsoleSender().sendMessage("");
    }
    
    /**
     * Translate hex color codes to Bukkit color format
     * Supports &#RRGGBB format (e.g., "&#FB7208")
     */
    private static String translateHex(String hex) {
        if (hex == null || !hex.startsWith("&#")) {
            return ChatColor.WHITE.toString();
        }
        
        // Remove &# prefix and convert to #RRGGBB format
        hex = hex.substring(2);
        
        return ChatColor.of("#" + hex).toString();
    }
}
