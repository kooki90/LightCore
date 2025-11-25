package me.lime.lightCore.api.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility class for rotating through lists
 */
public final class RotationUtil {

    private RotationUtil() {}

    /**
     * Get the current element without advancing
     * @param list The list to rotate through
     * @param index The atomic index
     * @return The current element, or null if list is empty
     */
    @Nullable
    public static <T> T current(@NotNull List<T> list, @NotNull AtomicInteger index) {
        return list.isEmpty() ? null : list.get(Math.floorMod(index.get(), list.size()));
    }

    /**
     * Get the next element and advance the index
     * @param list The list to rotate through
     * @param index The atomic index
     * @return The next element, or null if list is empty
     */
    @Nullable
    public static <T> T next(@NotNull List<T> list, @NotNull AtomicInteger index) {
        return list.isEmpty() ? null : list.get(Math.floorMod(index.incrementAndGet(), list.size()));
    }

    /**
     * Get the previous element and decrement the index
     * @param list The list to rotate through
     * @param index The atomic index
     * @return The previous element, or null if list is empty
     */
    @Nullable
    public static <T> T previous(@NotNull List<T> list, @NotNull AtomicInteger index) {
        return list.isEmpty() ? null : list.get(Math.floorMod(index.decrementAndGet(), list.size()));
    }

    /**
     * Peek at the next element without advancing
     * @param list The list to rotate through
     * @param index The atomic index
     * @return The next element, or null if list is empty
     */
    @Nullable
    public static <T> T peekNext(@NotNull List<T> list, @NotNull AtomicInteger index) {
        return list.isEmpty() ? null : list.get(Math.floorMod(index.get() + 1, list.size()));
    }

    /**
     * Peek at the previous element without decrementing
     * @param list The list to rotate through
     * @param index The atomic index
     * @return The previous element, or null if list is empty
     */
    @Nullable
    public static <T> T peekPrevious(@NotNull List<T> list, @NotNull AtomicInteger index) {
        return list.isEmpty() ? null : list.get(Math.floorMod(index.get() - 1, list.size()));
    }

    /**
     * Reset the index to 0
     * @param index The atomic index
     */
    public static void reset(@NotNull AtomicInteger index) {
        index.set(0);
    }

    /**
     * Set the index to a specific position
     * @param list The list
     * @param index The atomic index
     * @param position The position to set
     */
    public static <T> void setPosition(@NotNull List<T> list, @NotNull AtomicInteger index, int position) {
        if (!list.isEmpty()) {
            index.set(Math.floorMod(position, list.size()));
        }
    }
}
