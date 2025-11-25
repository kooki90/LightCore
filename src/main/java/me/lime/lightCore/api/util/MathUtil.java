package me.lime.lightCore.api.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Utility class for math and location operations
 */
public final class MathUtil {

    private MathUtil() {}

    private static final ThreadLocalRandom RNG = ThreadLocalRandom.current();

    public static int randomInt(int min, int max) {
        return RNG.nextInt(min, max + 1);
    }

    public static double randomDouble(double min, double max) {
        return RNG.nextDouble(min, max);
    }

    public static boolean chance(double percent) {
        return RNG.nextDouble(100) < percent;
    }

    public static double pow(double base, int exp) {
        return Math.pow(base, exp);
    }

    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public static double map(double value, double inMin, double inMax, double outMin, double outMax) {
        return inMax == inMin ? outMin : (value - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }

    public static double distance2D(@NotNull Location a, @NotNull Location b) {
        if (!sameWorld(a, b)) return -1;
        double dx = a.getX() - b.getX();
        double dz = a.getZ() - b.getZ();
        return Math.sqrt(dx * dx + dz * dz);
    }

    public static double distance3D(@NotNull Location a, @NotNull Location b) {
        if (!sameWorld(a, b)) return -1;
        return a.distance(b);
    }

    public static double distanceSquared2D(@NotNull Location a, @NotNull Location b) {
        double dx = a.getX() - b.getX();
        double dz = a.getZ() - b.getZ();
        return dx * dx + dz * dz;
    }

    public static double distanceSquared3D(@NotNull Location a, @NotNull Location b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        double dz = a.getZ() - b.getZ();
        return dx * dx + dy * dy + dz * dz;
    }

    @NotNull
    public static Vector direction(@NotNull Location from, @NotNull Location to) {
        return to.toVector().subtract(from.toVector()).normalize();
    }

    @NotNull
    public static Vector randomDirection() {
        double yaw = RNG.nextDouble(0, 360);
        double pitch = RNG.nextDouble(-90, 90);
        double x = -Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));
        double y = -Math.sin(Math.toRadians(pitch));
        double z = Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));
        return new Vector(x, y, z).normalize();
    }

    @NotNull
    public static Location offset(@NotNull Location loc, double x, double y, double z) {
        return loc.clone().add(x, y, z);
    }

    @NotNull
    public static Location randomOffset(@NotNull Location base, double radius) {
        double angle = RNG.nextDouble(0, Math.PI * 2);
        double x = Math.cos(angle) * radius;
        double z = Math.sin(angle) * radius;
        return base.clone().add(x, 0, z);
    }

    public static boolean within(double val, double min, double max) {
        return val >= min && val <= max;
    }

    public static boolean isSameBlock(@NotNull Location a, @NotNull Location b) {
        return sameWorld(a, b)
                && a.getBlockX() == b.getBlockX()
                && a.getBlockY() == b.getBlockY()
                && a.getBlockZ() == b.getBlockZ();
    }

    @NotNull
    public static Location centerBlock(@NotNull Location loc) {
        return new Location(loc.getWorld(), loc.getBlockX() + 0.5, loc.getY(), loc.getBlockZ() + 0.5);
    }

    public static int chunkX(double worldX) {
        return ((int) Math.floor(worldX)) >> 4;
    }

    public static int chunkZ(double worldZ) {
        return ((int) Math.floor(worldZ)) >> 4;
    }

    @NotNull
    public static Location centerOfChunk(@NotNull World world, int chunkX, int chunkZ) {
        int x = (chunkX << 4) + 8;
        int z = (chunkZ << 4) + 8;
        return new Location(world, x, world.getHighestBlockYAt(x, z), z);
    }

    @NotNull
    public static Vector chunkToWorld(int chunkX, int chunkZ) {
        return new Vector(chunkX << 4, 0, chunkZ << 4);
    }

    public static boolean inSameChunk(@NotNull Location a, @NotNull Location b) {
        return sameWorld(a, b)
                && (a.getBlockX() >> 4) == (b.getBlockX() >> 4)
                && (a.getBlockZ() >> 4) == (b.getBlockZ() >> 4);
    }

    public static boolean isInWorldBorder(@NotNull World world, double x, double z) {
        WorldBorder border = world.getWorldBorder();
        Location center = border.getCenter();
        double radius = border.getSize() / 2.0;
        return Math.abs(x - center.getX()) <= radius && Math.abs(z - center.getZ()) <= radius;
    }

    private static boolean sameWorld(@NotNull Location a, @NotNull Location b) {
        World wa = a.getWorld();
        World wb = b.getWorld();
        return wa != null && wa.equals(wb);
    }
}
