package me.lime.lightCore.api.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for dispatching commands
 */
public final class CommandDispatchUtil {

    private CommandDispatchUtil() {}

    /**
     * Create a command builder
     * @param command The command template (use <player> for player name)
     * @return A new Builder
     */
    @NotNull
    public static Builder command(@NotNull String command) {
        return new Builder(command);
    }

    /**
     * Execute a command as console immediately
     * @param command The command to execute
     */
    public static void console(@NotNull String command) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    /**
     * Execute a command as a player immediately
     * @param player The player
     * @param command The command to execute
     */
    public static void player(@NotNull Player player, @NotNull String command) {
        player.performCommand(command);
    }

    /**
     * Builder for command execution
     */
    public static final class Builder {
        private final String template;
        private boolean console = false;
        private long delay = 0;

        private Builder(@NotNull String template) {
            this.template = template;
        }

        /**
         * Execute as console
         * @return This builder
         */
        @NotNull
        public Builder asConsole() {
            this.console = true;
            return this;
        }

        /**
         * Execute as player
         * @return This builder
         */
        @NotNull
        public Builder asPlayer() {
            this.console = false;
            return this;
        }

        /**
         * Add a delay before execution
         * @param ticks Delay in ticks (20 ticks = 1 second)
         * @return This builder
         */
        @NotNull
        public Builder withDelay(long ticks) {
            this.delay = ticks;
            return this;
        }

        /**
         * Execute the command for a player
         * @param player The player (used for <player> replacement and execution)
         */
        public void execute(@NotNull Player player) {
            String command = template.replace("<player>", player.getName());

            Runnable task = () -> {
                if (console) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                } else {
                    player.performCommand(command);
                }
            };

            if (delay > 0) {
                SchedulerUtil.syncLater(task, delay);
            } else {
                SchedulerUtil.sync(task);
            }
        }

        /**
         * Execute the command without player context
         */
        public void execute() {
            Runnable task = () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), template);

            if (delay > 0) {
                SchedulerUtil.syncLater(task, delay);
            } else {
                SchedulerUtil.sync(task);
            }
        }
    }
}
