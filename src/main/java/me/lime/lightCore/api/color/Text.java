package me.lime.lightCore.api.color;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Utility class for colorizing text with MiniMessage and legacy color codes
 * Supports: MiniMessage tags, legacy &codes, hex &#RRGGBB
 */
public final class Text {

    private Text() {}

    /**
     * Colorize a string to Component
     * Supports MiniMessage tags and legacy color codes
     * @param string The input string
     * @return Colored Component
     */
    @NotNull
    public static Component color(@Nullable String string) {
        return TextService.format(string);
    }

    /**
     * Colorize a Component
     * @param component The input component
     * @return Colored Component
     */
    @NotNull
    public static Component color(@Nullable Component component) {
        return TextService.format(component);
    }

    /**
     * Colorize a string to legacy format (for older APIs)
     * @param string The input string
     * @return Colored legacy string with § codes
     */
    @NotNull
    public static String colorLegacy(@Nullable String string) {
        return TextService.formatLegacy(string);
    }

    /**
     * Colorize a list of strings to Components
     * @param list The input list
     * @return List of colored Components
     */
    @NotNull
    public static List<Component> colorList(@Nullable List<String> list) {
        return TextService.formatList(list);
    }

    /**
     * Colorize a list of Components
     * @param list The input list
     * @return List of colored Components
     */
    @NotNull
    public static List<Component> colorComponentList(@Nullable List<Component> list) {
        return TextService.formatComponentList(list);
    }

    /**
     * Colorize a list of strings to legacy format
     * @param list The input list
     * @return List of colored legacy strings
     */
    @NotNull
    public static List<String> colorLegacyList(@Nullable List<String> list) {
        return TextService.formatLegacyList(list);
    }

    /**
     * Convert a Component to legacy string
     * @param component The component
     * @return Legacy string representation
     */
    @NotNull
    public static String toLegacy(@Nullable Component component) {
        return TextService.toLegacyString(component);
    }

    /**
     * Convert a Component to MiniMessage string
     * @param component The component
     * @return MiniMessage string representation
     */
    @NotNull
    public static String toMiniMessage(@Nullable Component component) {
        return TextService.toMiniMessageString(component);
    }
}
