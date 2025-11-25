package me.lime.lightCore.api.util;

import java.io.IOException;

/**
 * Utility class for creating exceptions
 */
public final class ExceptionUtil {

    private ExceptionUtil() {}

    public static RuntimeException runtime(String message) {
        return new RuntimeException(message);
    }

    public static RuntimeException runtime(String message, Throwable cause) {
        return new RuntimeException(message, cause);
    }

    public static IllegalArgumentException illegalArgument(String message) {
        return new IllegalArgumentException(message);
    }

    public static IllegalArgumentException illegalArgument(String message, Throwable cause) {
        return new IllegalArgumentException(message, cause);
    }

    public static IllegalStateException illegalState(String message) {
        return new IllegalStateException(message);
    }

    public static IllegalStateException illegalState(String message, Throwable cause) {
        return new IllegalStateException(message, cause);
    }

    public static NullPointerException nullPointer(String message) {
        return new NullPointerException(message);
    }

    public static UnsupportedOperationException unsupported(String message) {
        return new UnsupportedOperationException(message);
    }

    public static IOException io(String message) {
        return new IOException(message);
    }

    public static IOException io(String message, Throwable cause) {
        return new IOException(message, cause);
    }

    public static ReflectiveOperationException reflection(String message) {
        return new ReflectiveOperationException(message);
    }

    public static ReflectiveOperationException reflection(String message, Throwable cause) {
        return new ReflectiveOperationException(message, cause);
    }
}
