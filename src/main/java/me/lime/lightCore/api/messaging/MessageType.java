package me.lime.lightCore.api.messaging;

import org.jetbrains.annotations.NotNull;

/**
 * Message types supported by the messaging system
 */
public enum MessageType {
    CHAT,
    ACTIONBAR,
    TITLE,
    BOSSBAR;

    /**
     * Parse a message type from a string
     * @param raw The raw string to parse
     * @return The parsed MessageType, defaults to CHAT if invalid
     */
    @NotNull
    public static MessageType get(@NotNull String raw) {
        if (raw.isEmpty()) {
            return CHAT;
        }

        try {
            return MessageType.valueOf(raw.trim().toUpperCase());
        } catch (IllegalArgumentException ignored) {
            return CHAT;
        }
    }
}
