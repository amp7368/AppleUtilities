package apple.utilities.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUnpackaging {
    /**
     * ignores all "ignored" exceptions
     *
     * @param e       the initial exception
     * @param ignored the exceptions to ignore
     * @return the unpackaged exception that is not ignored or null if all exceptions were ignored
     */
    @Nullable
    public static Exception ignore(@NotNull Exception e, @NotNull Class<?>... ignored) {
        while (isType(ignored, e)) {
            Throwable cause = e.getCause();
            if (cause instanceof Exception) {
                e = (Exception) cause;
            } else {
                return null;
            }
        }
        return e;
    }

    public static boolean exists(@NotNull Exception e, @NotNull Class<?>... checkFor) {
        while (!isType(checkFor, e)) {
            Throwable cause = e.getCause();
            if (cause instanceof Exception) {
                e = (Exception) cause;
            } else {
                return false;
            }
        }
        return true;
    }

    private static boolean isType(Class<?>[] ignored, @NotNull Exception e) {
        for (Class<?> ignore : ignored) {
            if (ignore.isInstance(e)) return true;
        }
        return false;
    }

    public static String getStackTrace(Exception e) {
        StringWriter exceptionString = new StringWriter();
        e.printStackTrace(new PrintWriter(exceptionString));
        return exceptionString.toString();
    }
}
