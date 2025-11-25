package me.lime.lightCore.api.color;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Internal service for text formatting with caching
 */
public final class TextService {

    private TextService() {}

    // Legacy color code mappings
    private static final Map<String, String> LEGACY_COLOR_CODES = Map.ofEntries(
            Map.entry("black", "0"),
            Map.entry("dark_blue", "1"),
            Map.entry("dark_green", "2"),
            Map.entry("dark_aqua", "3"),
            Map.entry("dark_red", "4"),
            Map.entry("dark_purple", "5"),
            Map.entry("gold", "6"),
            Map.entry("gray", "7"),
            Map.entry("dark_gray", "8"),
            Map.entry("blue", "9"),
            Map.entry("green", "a"),
            Map.entry("aqua", "b"),
            Map.entry("red", "c"),
            Map.entry("light_purple", "d"),
            Map.entry("yellow", "e"),
            Map.entry("white", "f"),
            Map.entry("bold", "l"),
            Map.entry("italic", "o"),
            Map.entry("underlined", "n"),
            Map.entry("strikethrough", "m"),
            Map.entry("obfuscated", "k"),
            Map.entry("reset", "r")
    );

    // Patterns
    private static final Pattern MINIMESSAGE_TAG = Pattern.compile(
            "<(/?)(#[a-fA-F0-9]{6}|[a-zA-Z_]+)>");
    private static final Pattern HEX_TAG = Pattern.compile(
            "&#([a-fA-F0-9]{6})");
    private static final Pattern MINIMESSAGE_FEATURES = Pattern.compile(
            "<(click|hover|insertion|gradient|rainbow|head|/|#)", Pattern.CASE_INSENSITIVE);

    // Serializers
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.builder()
            .character('&')
            .hexCharacter('#')
            .useUnusualXRepeatedCharacterHexFormat()
            .build();

    // Simple caches with size limit
    private static final int MAX_CACHE_SIZE = 1000;
    private static final Map<String, Component> COMPONENT_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, String> STRING_CACHE = new ConcurrentHashMap<>();

    /**
     * Format a string to Component with MiniMessage/Legacy support
     */
    @NotNull
    public static Component format(@Nullable String input) {
        if (input == null || input.isBlank()) return Component.empty();

        // Check cache
        Component cached = COMPONENT_CACHE.get(input);
        if (cached != null) return cached;

        // Parse
        Matcher minimessage = MINIMESSAGE_FEATURES.matcher(input);
        Component component = minimessage.find()
                ? MINI_MESSAGE.deserialize(input)
                : LEGACY_SERIALIZER.deserialize(input);

        // Remove default italic (for item lores)
        component = component.decoration(TextDecoration.ITALIC, false);

        // Cache with size limit
        if (COMPONENT_CACHE.size() < MAX_CACHE_SIZE) {
            COMPONENT_CACHE.put(input, component);
        }

        return component;
    }

    /**
     * Format a Component
     */
    @NotNull
    public static Component format(@Nullable Component component) {
        if (component == null || component.equals(Component.empty())) return Component.empty();

        String serialized = MINI_MESSAGE.serialize(component);
        Matcher mm = MINIMESSAGE_FEATURES.matcher(serialized);

        Component result = mm.find()
                ? MINI_MESSAGE.deserialize(serialized)
                : LEGACY_SERIALIZER.deserialize(LEGACY_SERIALIZER.serialize(component));

        return result.decoration(TextDecoration.ITALIC, false);
    }

    /**
     * Format a string to legacy string with § codes
     */
    @NotNull
    public static String formatLegacy(@Nullable String input) {
        if (input == null || input.isBlank()) return "";

        // Check cache
        String cached = STRING_CACHE.get(input);
        if (cached != null) return cached;

        // Translate & codes
        String step = ChatColor.translateAlternateColorCodes('&', input);

        // Handle hex codes (&#RRGGBB)
        Matcher hex = HEX_TAG.matcher(step);
        if (hex.find()) {
            int len = step.length();
            StringBuilder sb = new StringBuilder(len + 24);
            int last = 0;

            do {
                sb.append(step, last, hex.start()).append(ChatColor.of("#" + hex.group(1)));
                last = hex.end();
            } while (hex.find());

            sb.append(step, last, len);
            step = sb.toString();
        }

        // Handle MiniMessage tags
        Matcher matcher = MINIMESSAGE_TAG.matcher(step);
        int len = step.length();
        StringBuilder out = new StringBuilder(len + 24);
        Deque<String> stack = new ArrayDeque<>(4);
        int last = 0;

        while (matcher.find()) {
            out.append(step, last, matcher.start());

            boolean closing = matcher.group(1).length() == 1;
            String raw = matcher.group(2);
            String tag = raw.isEmpty() ? raw : raw.toLowerCase(Locale.ROOT);

            if (closing && !stack.isEmpty() && (stack.contains(tag) || (tag.length() == 7 && tag.charAt(0) == '#'))) {
                out.append(ChatColor.RESET);
                stack.remove(tag);

                for (String t : stack) {
                    out.append(resolve(t));
                }
            } else if (!closing) {
                stack.push(tag);
                out.append(resolve(tag));
            }
            last = matcher.end();
        }

        String result = out.append(step, last, len).toString();

        // Cache with size limit
        if (STRING_CACHE.size() < MAX_CACHE_SIZE) {
            STRING_CACHE.put(input, result);
        }

        return result;
    }

    /**
     * Format a list of strings to Components
     */
    @NotNull
    public static List<Component> formatList(@Nullable List<String> input) {
        if (input == null || input.isEmpty()) return List.of();
        return input.stream()
                .map(TextService::format)
                .toList();
    }

    /**
     * Format a list of Components
     */
    @NotNull
    public static List<Component> formatComponentList(@Nullable List<Component> input) {
        if (input == null || input.isEmpty()) return List.of();
        return input.stream()
                .map(TextService::format)
                .toList();
    }

    /**
     * Format a list of strings to legacy strings
     */
    @NotNull
    public static List<String> formatLegacyList(@Nullable List<String> input) {
        if (input == null || input.isEmpty()) return List.of();
        return input.stream()
                .map(TextService::formatLegacy)
                .toList();
    }

    /**
     * Convert Component to legacy string
     */
    @NotNull
    public static String toLegacyString(@Nullable Component component) {
        if (component == null) return "";
        return LEGACY_SERIALIZER.serialize(component);
    }

    /**
     * Convert Component to MiniMessage string
     */
    @NotNull
    public static String toMiniMessageString(@Nullable Component component) {
        if (component == null) return "";
        return MINI_MESSAGE.serialize(component);
    }

    /**
     * Resolve a tag to legacy color code
     */
    @NotNull
    private static String resolve(@NotNull String tag) {
        if (tag.startsWith("#") && tag.length() == 7) {
            try {
                return ChatColor.of(tag).toString();
            } catch (IllegalArgumentException ignored) {
                return "";
            }
        }
        String code = LEGACY_COLOR_CODES.get(tag);
        return code != null ? "§" + code : "";
    }

    /**
     * Clear all caches (useful for reloads)
     */
    public static void clearCache() {
        COMPONENT_CACHE.clear();
        STRING_CACHE.clear();
    }
}
