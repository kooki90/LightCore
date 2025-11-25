package me.lime.lightCore.api.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for scheduling tasks
 */
public final class SchedulerUtil {

    private SchedulerUtil() {}

    private static JavaPlugin plugin;

    /**
     * Initialize with your plugin instance
     * @param pluginInstance Your plugin
     */
    public static void init(@NotNull JavaPlugin pluginInstance) {
        plugin = pluginInstance;
    }

    private static void checkInit() {
        if (plugin == null) {
            throw new IllegalStateException("SchedulerUtil not initialized! Call SchedulerUtil.init(plugin) first.");
        }
    }

    // ==================== Sync ====================

    /**
     * Run a task on the main thread
     * @param task The task to run
     * @return The BukkitTask
     */
    @NotNull
    public static BukkitTask sync(@NotNull Runnable task) {
        checkInit();
        return Bukkit.getScheduler().runTask(plugin, task);
    }

    /**
     * Run a task on the main thread after a delay
     * @param task The task to run
     * @param delayTicks Delay in ticks (20 ticks = 1 second)
     * @return The BukkitTask
     */
    @NotNull
    public static BukkitTask syncLater(@NotNull Runnable task, long delayTicks) {
        checkInit();
        return Bukkit.getScheduler().runTaskLater(plugin, task, delayTicks);
    }

    /**
     * Run a repeating task on the main thread
     * @param task The task to run
     * @param delayTicks Initial delay in ticks
     * @param periodTicks Period between runs in ticks
     * @return The BukkitTask
     */
    @NotNull
    public static BukkitTask syncRepeating(@NotNull Runnable task, long delayTicks, long periodTicks) {
        checkInit();
        return Bukkit.getScheduler().runTaskTimer(plugin, task, delayTicks, periodTicks);
    }

    // ==================== Async ====================

    /**
     * Run a task asynchronously
     * @param task The task to run
     * @return The BukkitTask
     */
    @NotNull
    public static BukkitTask async(@NotNull Runnable task) {
        checkInit();
        return Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
    }

    /**
     * Run a task asynchronously after a delay
     * @param task The task to run
     * @param delayTicks Delay in ticks
     * @return The BukkitTask
     */
    @NotNull
    public static BukkitTask asyncLater(@NotNull Runnable task, long delayTicks) {
        checkInit();
        return Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, task, delayTicks);
    }

    /**
     * Run a repeating task asynchronously
     * @param task The task to run
     * @param delayTicks Initial delay in ticks
     * @param periodTicks Period between runs in ticks
     * @return The BukkitTask
     */
    @NotNull
    public static BukkitTask asyncRepeating(@NotNull Runnable task, long delayTicks, long periodTicks) {
        checkInit();
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task, delayTicks, periodTicks);
    }

    // ==================== Cancel ====================

    /**
     * Cancel a task
     * @param task The task to cancel
     */
    public static void cancel(@NotNull BukkitTask task) {
        task.cancel();
    }

    /**
     * Cancel a task by ID
     * @param taskId The task ID
     */
    public static void cancel(int taskId) {
        Bukkit.getScheduler().cancelTask(taskId);
    }

    /**
     * Cancel all tasks for the plugin
     */
    public static void cancelAll() {
        checkInit();
        Bukkit.getScheduler().cancelTasks(plugin);
    }
}
