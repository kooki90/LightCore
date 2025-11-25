package me.lime.lightCore.api.world;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Set;

/**
 * Utility class for block-related operations
 */
public final class BlockUtil {

    private BlockUtil() {}

    private static final Set<Material> LIQUIDS = EnumSet.of(
            Material.WATER,
            Material.LAVA
    );

    private static final Set<Material> DANGEROUS = EnumSet.of(
            Material.LAVA,
            Material.FIRE,
            Material.CAMPFIRE,
            Material.SOUL_CAMPFIRE,
            Material.MAGMA_BLOCK,
            Material.CACTUS,
            Material.SWEET_BERRY_BUSH,
            Material.WITHER_ROSE
    );

    private static final Set<Material> CONTAINERS = EnumSet.of(
            Material.CHEST,
            Material.BARREL,
            Material.SHULKER_BOX,
            Material.HOPPER,
            Material.DROPPER,
            Material.DISPENSER,
            Material.FURNACE,
            Material.BLAST_FURNACE,
            Material.SMOKER
    );

    /**
     * Check if a block is air or empty
     * @param block The block to check
     * @return true if the block is air or null
     */
    public static boolean isAir(@Nullable Block block) {
        return block == null || block.isEmpty() || block.getType() == Material.AIR;
    }

    /**
     * Check if a block is solid (not liquid)
     * @param block The block to check
     * @return true if the block is solid
     */
    public static boolean isSolid(@Nullable Block block) {
        if (block == null) return false;
        final Material type = block.getType();
        return type.isSolid() && !LIQUIDS.contains(type);
    }

    /**
     * Check if a block is a liquid (water or lava)
     * @param block The block to check
     * @return true if the block is liquid
     */
    public static boolean isLiquid(@Nullable Block block) {
        return block != null && LIQUIDS.contains(block.getType());
    }

    /**
     * Check if a block is dangerous (lava, fire, cactus, etc.)
     * @param block The block to check
     * @return true if the block is dangerous
     */
    public static boolean isDangerous(@Nullable Block block) {
        return block != null && DANGEROUS.contains(block.getType());
    }

    /**
     * Check if a block is safe to stand on
     * @param block The block to check
     * @return true if the block is safe
     */
    public static boolean isSafe(@Nullable Block block) {
        if (block == null) return false;
        final Material type = block.getType();
        return type.isSolid() && !LIQUIDS.contains(type) && !DANGEROUS.contains(type);
    }

    /**
     * Check if a block is a container (chest, barrel, etc.)
     * @param block The block to check
     * @return true if the block is a container
     */
    public static boolean isContainer(@Nullable Block block) {
        return block != null && CONTAINERS.contains(block.getType());
    }

    /**
     * Get the material type name of a block
     * @param block The block
     * @return The material name, or "UNKNOWN" if block is null
     */
    @NotNull
    public static String getTypeName(@Nullable Block block) {
        return block != null ? block.getType().name() : "UNKNOWN";
    }

    /**
     * Check if two blocks have the same material type
     * @param a First block
     * @param b Second block
     * @return true if both blocks have the same type
     */
    public static boolean isSameType(@Nullable Block a, @Nullable Block b) {
        return a != null && b != null && a.getType() == b.getType();
    }

    /**
     * Check if a material is a liquid
     * @param material The material to check
     * @return true if the material is liquid
     */
    public static boolean isLiquid(@Nullable Material material) {
        return material != null && LIQUIDS.contains(material);
    }

    /**
     * Check if a material is dangerous
     * @param material The material to check
     * @return true if the material is dangerous
     */
    public static boolean isDangerous(@Nullable Material material) {
        return material != null && DANGEROUS.contains(material);
    }

    /**
     * Check if a material is a container
     * @param material The material to check
     * @return true if the material is a container
     */
    public static boolean isContainer(@Nullable Material material) {
        return material != null && CONTAINERS.contains(material);
    }
}
