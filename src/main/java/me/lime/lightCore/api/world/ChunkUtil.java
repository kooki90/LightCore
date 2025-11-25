package me.lime.lightCore.api.world;

import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Utility class for chunk-related operations
 * Useful for finding safe spawn locations
 */
public final class ChunkUtil {

    private ChunkUtil() {}

    private static final Set<Material> UNSAFE_BLOCKS = Set.of(
            Material.LAVA, Material.WATER, Material.CACTUS,
            Material.CAMPFIRE, Material.FIRE, Material.MAGMA_BLOCK,
            Material.SOUL_CAMPFIRE, Material.SOUL_FIRE, Material.SWEET_BERRY_BUSH,
            Material.WITHER_ROSE, Material.END_PORTAL, Material.NETHER_PORTAL
    );

    /**
     * Get a snapshot of a chunk
     * @param chunk The chunk to snapshot
     * @return The chunk snapshot, or null if chunk is null
     */
    @Nullable
    public static ChunkSnapshot snapshot(@Nullable Chunk chunk) {
        return chunk == null ? null : chunk.getChunkSnapshot(true, false, false);
    }

    /**
     * Get a chunk asynchronously
     * @param world The world
     * @param chunkX Chunk X coordinate
     * @param chunkZ Chunk Z coordinate
     * @return CompletableFuture containing the chunk
     */
    @NotNull
    public static CompletableFuture<@Nullable Chunk> getChunkAsync(@Nullable World world, int chunkX, int chunkZ) {
        return world == null 
                ? CompletableFuture.completedFuture(null) 
                : world.getChunkAtAsync(chunkX, chunkZ, true);
    }

    /**
     * Get a chunk snapshot asynchronously
     * @param world The world
     * @param chunkX Chunk X coordinate
     * @param chunkZ Chunk Z coordinate
     * @return CompletableFuture containing the chunk snapshot
     */
    @NotNull
    public static CompletableFuture<@Nullable ChunkSnapshot> getSnapshotAsync(@Nullable World world, int chunkX, int chunkZ) {
        return getChunkAsync(world, chunkX, chunkZ)
                .thenApplyAsync(chunk -> chunk == null ? null : chunk.getChunkSnapshot(true, false, false));
    }

    /**
     * Check if a position in a chunk snapshot is safe to stand on
     * @param snapshot The chunk snapshot
     * @param x Local X coordinate (0-15)
     * @param y Y coordinate
     * @param z Local Z coordinate (0-15)
     * @param minY Minimum Y of the world
     * @param maxY Maximum Y of the world
     * @return true if the position is safe
     */
    public static boolean isSafe(@Nullable ChunkSnapshot snapshot, int x, int y, int z, int minY, int maxY) {
        if (snapshot == null || y <= minY || y >= maxY - 1) return false;

        final Material floor = snapshot.getBlockType(x, y - 1, z);
        if (!floor.isSolid() || UNSAFE_BLOCKS.contains(floor)) return false;

        return snapshot.getBlockType(x, y, z).isAir() && snapshot.getBlockType(x, y + 1, z).isAir();
    }

    /**
     * Find a safe Y coordinate in the Overworld
     * @param snapshot The chunk snapshot
     * @param x Local X coordinate (0-15)
     * @param z Local Z coordinate (0-15)
     * @param minY Minimum Y of the world
     * @param maxY Maximum Y of the world
     * @return Safe Y coordinate, or Integer.MIN_VALUE if not found
     */
    public static int getSafeYOverworld(@NotNull ChunkSnapshot snapshot, int x, int z, int minY, int maxY) {
        final int y = snapshot.getHighestBlockYAt(x, z) + 1;
        return isSafe(snapshot, x, y, z, minY, maxY) ? y : Integer.MIN_VALUE;
    }

    /**
     * Find a safe Y coordinate in the End
     * @param snapshot The chunk snapshot
     * @param x Local X coordinate (0-15)
     * @param z Local Z coordinate (0-15)
     * @param minY Minimum Y of the world
     * @param maxY Maximum Y of the world
     * @return Safe Y coordinate, or Integer.MIN_VALUE if not found
     */
    public static int getSafeYEnd(@NotNull ChunkSnapshot snapshot, int x, int z, int minY, int maxY) {
        final int y = snapshot.getHighestBlockYAt(x, z) + 1;
        return isSafe(snapshot, x, y, z, minY, maxY) ? y : Integer.MIN_VALUE;
    }

    /**
     * Find a safe Y coordinate in the Nether
     * @param snapshot The chunk snapshot
     * @param x Local X coordinate (0-15)
     * @param z Local Z coordinate (0-15)
     * @param minY Minimum Y of the world
     * @param maxY Maximum Y of the world
     * @return Safe Y coordinate, or Integer.MIN_VALUE if not found
     */
    public static int getSafeYNether(@NotNull ChunkSnapshot snapshot, int x, int z, int minY, int maxY) {
        for (int y = 120; y >= 32; y--) {
            if (isSafe(snapshot, x, y, z, minY, maxY))
                return y;
        }
        return Integer.MIN_VALUE;
    }

    /**
     * Find a safe Y coordinate based on the world environment
     * @param snapshot The chunk snapshot
     * @param world The world (used to determine environment)
     * @param x World X coordinate
     * @param z World Z coordinate
     * @return Safe Y coordinate, or Integer.MIN_VALUE if not found
     */
    public static int createSafeY(@Nullable ChunkSnapshot snapshot, @NotNull World world, int x, int z) {
        final int bx = x & 0xF;
        final int bz = z & 0xF;
        final int minY = world.getMinHeight();
        final int maxY = world.getMaxHeight();

        return switch (world.getEnvironment()) {
            case NETHER -> getSafeYNether(Objects.requireNonNull(snapshot), bx, bz, minY, maxY);
            case THE_END -> getSafeYEnd(Objects.requireNonNull(snapshot), bx, bz, minY, maxY);
            default -> getSafeYOverworld(Objects.requireNonNull(snapshot), bx, bz, minY, maxY);
        };
    }

    /**
     * Find a safe Y coordinate asynchronously
     * @param world The world
     * @param x World X coordinate
     * @param z World Z coordinate
     * @return CompletableFuture containing the safe Y coordinate
     */
    @NotNull
    public static CompletableFuture<Integer> createSafeYAsync(@NotNull World world, int x, int z) {
        final int chunkX = x >> 4;
        final int chunkZ = z >> 4;

        return getSnapshotAsync(world, chunkX, chunkZ)
                .thenApplyAsync(snapshot -> createSafeY(snapshot, world, x, z));
    }
}
