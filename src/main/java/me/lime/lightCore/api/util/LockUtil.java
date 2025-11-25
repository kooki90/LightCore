package me.lime.lightCore.api.util;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 * Utility class for managing locks by UUID
 */
public final class LockUtil {

    private LockUtil() {}

    private static final Map<UUID, ReentrantLock> LOCKS = new ConcurrentHashMap<>();

    /**
     * Acquire a lock for a UUID
     * @param id The UUID
     * @return The lock
     */
    @NotNull
    public static ReentrantLock acquire(@NotNull UUID id) {
        return LOCKS.computeIfAbsent(id, k -> new ReentrantLock());
    }

    /**
     * Release a lock for a UUID
     * @param id The UUID
     * @param lock The lock to release
     */
    public static void release(@NotNull UUID id, ReentrantLock lock) {
        if (lock == null || !lock.isHeldByCurrentThread()) return;

        lock.unlock();
        if (!lock.isLocked()) {
            LOCKS.remove(id, lock);
        }
    }

    /**
     * Try to acquire a lock with timeout
     * @param id The UUID
     * @param time The timeout duration
     * @param unit The time unit
     * @return true if lock was acquired
     */
    public static boolean tryLock(@NotNull UUID id, long time, @NotNull TimeUnit unit) {
        ReentrantLock lock = acquire(id);
        try {
            return lock.tryLock(time, unit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * Execute an action while holding a lock
     * @param id The UUID
     * @param action The action to execute
     */
    public static void withLock(@NotNull UUID id, @NotNull Runnable action) {
        ReentrantLock lock = acquire(id);
        lock.lock();
        try {
            action.run();
        } finally {
            release(id, lock);
        }
    }

    /**
     * Execute an action while holding a lock and return the result
     * @param id The UUID
     * @param action The action to execute
     * @return The result
     */
    public static <T> T withLock(@NotNull UUID id, @NotNull Supplier<T> action) {
        ReentrantLock lock = acquire(id);
        lock.lock();
        try {
            return action.get();
        } finally {
            release(id, lock);
        }
    }

    /**
     * Check if a UUID is currently locked
     * @param id The UUID
     * @return true if locked
     */
    public static boolean isLocked(@NotNull UUID id) {
        ReentrantLock lock = LOCKS.get(id);
        return lock != null && lock.isLocked();
    }

    /**
     * Force release all locks held by current thread for a UUID
     * @param id The UUID
     */
    public static void forceRelease(@NotNull UUID id) {
        ReentrantLock lock = LOCKS.remove(id);
        if (lock == null) return;

        while (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    /**
     * Clear all locks
     */
    public static void clearAll() {
        LOCKS.clear();
    }
}
