package me.lime.lightCore.api.world;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class for world-related operations
 */
public final class WorldUtil {

    private WorldUtil() {}

    /**
     * Check if the world is the Nether
     * @param world The world to check
     * @return true if the world is the Nether
     */
    public static boolean isNether(@Nullable World world) {
        return world != null && world.getEnvironment() == Environment.NETHER;
    }

    /**
     * Check if the world is the End
     * @param world The world to check
     * @return true if the world is the End
     */
    public static boolean isEnd(@Nullable World world) {
        return world != null && world.getEnvironment() == Environment.THE_END;
    }

    /**
     * Check if the world is the Overworld
     * @param world The world to check
     * @return true if the world is the Overworld
     */
    public static boolean isOverworld(@Nullable World world) {
        return world != null && world.getEnvironment() == Environment.NORMAL;
    }

    /**
     * Check if the world has a specific environment
     * @param world The world to check
     * @param env The environment to compare
     * @return true if the world has the specified environment
     */
    public static boolean isEnvironment(@Nullable World world, @Nullable Environment env) {
        return world != null && env != null && world.getEnvironment() == env;
    }

    /**
     * Check if two locations are in the same world
     * @param a First location
     * @param b Second location
     * @return true if both locations are in the same world
     */
    public static boolean isSameWorld(@Nullable Location a, @Nullable Location b) {
        if (a == null || b == null) return false;
        final World worldA = a.getWorld();
        final World worldB = b.getWorld();
        return worldA != null && worldA.equals(worldB);
    }

    /**
     * Check if a location is in a specific world
     * @param loc The location to check
     * @param world The world to compare
     * @return true if the location is in the specified world
     */
    public static boolean isLocationInWorld(@Nullable Location loc, @Nullable World world) {
        if (loc == null || world == null) return false;
        final World locWorld = loc.getWorld();
        return locWorld != null && locWorld.equals(world);
    }

    /**
     * Get the spawn location of a world
     * @param world The world
     * @return The spawn location, or null if world is null
     */
    @Nullable
    public static Location getSpawn(@Nullable World world) {
        return world != null ? world.getSpawnLocation() : null;
    }

    /**
     * Check if the world's spawn chunk is loaded
     * @param world The world to check
     * @return true if the spawn chunk is loaded
     */
    public static boolean isLoaded(@Nullable World world) {
        return world != null && world.isChunkLoaded(world.getSpawnLocation().getChunk());
    }

    /**
     * Get the name of a world
     * @param world The world
     * @return The world name, or "unknown" if world is null
     */
    @NotNull
    public static String getName(@Nullable World world) {
        return world != null ? world.getName() : "unknown";
    }

    /**
     * Get a friendly type name for the world environment
     * @param world The world
     * @return The type name (overworld, nether, end, custom, or unknown)
     */
    @NotNull
    public static String getType(@Nullable World world) {
        if (world == null) return "unknown";

        return switch (world.getEnvironment()) {
            case NETHER -> "nether";
            case THE_END -> "end";
            case NORMAL -> "overworld";
            default -> "custom";
        };
    }
}
