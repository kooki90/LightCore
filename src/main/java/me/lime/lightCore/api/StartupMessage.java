package me.lime.lightCore.api;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

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
     * Print a startup message with AUTO-GENERATED ASCII art and automatic plugin/server info
     * Uses built-in A-Z ASCII art generator
     * Automatically detects the calling plugin and extracts version, authors
     * @param name The plugin name (will be converted to ASCII art)
     * @param hex The hex color code (e.g., "&#FB7208")
     */
    @SuppressWarnings("deprecation")
    public static void printWithAscii(String name, String hex) {
        // Try to find the calling plugin automatically
        JavaPlugin callingPlugin = getCallingPlugin();
        
        String version = null;
        String authors = null;
        
        if (callingPlugin != null) {
            version = callingPlugin.getDescription().getVersion();
            List<String> authorsList = callingPlugin.getDescription().getAuthors();
            authors = authorsList.isEmpty() ? null : String.join(", ", authorsList);
        }
        
        printWithAscii(name, hex, version, authors);
    }
    
    /**
     * Print a startup message with AUTO-GENERATED ASCII art, version, and authors
     * @param name The plugin/feature name (will be converted to ASCII art)
     * @param hex The hex color code (e.g., "&#FB7208")
     * @param version Plugin version
     * @param authors Plugin authors (comma-separated or single)
     */
    public static void printWithAscii(String name, String hex, String version, String authors) {
        String color = translateHex(hex);
        String yellow = ChatColor.of("#FFD700").toString();
        String gray = ChatColor.GRAY.toString();
        
        // Generate ASCII art automatically from text
        String[] asciiLines = AsciiArt.generate(name);
        
        Bukkit.getConsoleSender().sendMessage("");
        
        // Print ASCII art
        for (String line : asciiLines) {
            Bukkit.getConsoleSender().sendMessage(color + line);
        }
        
        Bukkit.getConsoleSender().sendMessage("");
        
        // Print plugin information if provided
        if (version != null || authors != null) {
            Bukkit.getConsoleSender().sendMessage(yellow + "Plugin Information:");
            if (version != null) {
                Bukkit.getConsoleSender().sendMessage(gray + " • " + ChatColor.WHITE + "Version: " + yellow + version);
            }
            if (authors != null) {
                Bukkit.getConsoleSender().sendMessage(gray + " • " + ChatColor.WHITE + "Authors: " + yellow + authors);
            }
            Bukkit.getConsoleSender().sendMessage("");
            
            // Print server information
            Bukkit.getConsoleSender().sendMessage(yellow + "Server Information:");
            Bukkit.getConsoleSender().sendMessage(gray + " • " + ChatColor.WHITE + "Software: " + yellow + Bukkit.getName());
            Bukkit.getConsoleSender().sendMessage(gray + " • " + ChatColor.WHITE + "Version: " + yellow + Bukkit.getVersion());
            Bukkit.getConsoleSender().sendMessage("");
        }
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
     * Automatically detect the calling plugin from the stack trace
     * @return The JavaPlugin instance of the calling plugin, or null if not found
     */
    private static JavaPlugin getCallingPlugin() {
        try {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            
            for (StackTraceElement element : stackTrace) {
                String className = element.getClassName();
                
                // Skip our own classes
                if (className.startsWith("me.lime.lightCore.api.")) {
                    continue;
                }
                
                try {
                    Class<?> clazz = Class.forName(className);
                    if (JavaPlugin.class.isAssignableFrom(clazz)) {
                        return JavaPlugin.getProvidingPlugin(clazz.asSubclass(JavaPlugin.class));
                    }
                } catch (ClassNotFoundException ignored) {
                }
            }
        } catch (Exception ignored) {
        }
        
        return null;
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
