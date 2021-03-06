package apple.utilities.util;

import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

public class ObjectUtilsFormatting {
    public static <R> R defaultIfNull(R defaultValue, Supplier<R> finalValue, Supplier<?>... suppliers) {
        for (Supplier<?> supplier : suppliers) {
            if (supplier.get() == null) return defaultValue;
        }
        return finalValue.get();
    }

    @SafeVarargs
    public static <T, R> R defaultIfNull(R defaultValue, T supplied, Function<T, R> finalValue, Function<T, ?>... suppliers) {
        if (supplied == null) return defaultValue;
        for (Function<T, ?> supplier : suppliers) {
            if (supplier.apply(supplied) == null) return defaultValue;
        }
        R result = finalValue.apply(supplied);
        if (result == null) return defaultValue;
        return result;
    }


    @SafeVarargs
    public static <R> R requireNonNullElseElse(R defaultValue, R... others) {
        for (R other : others) {
            if (other != null)
                return other;
        }
        return defaultValue;
    }

    @SafeVarargs
    public static <R> R requireNonNullElseElse(R defaultValue, Supplier<R>... others) {
        for (Supplier<R> supplier : others) {
            R other = supplier.get();
            if (other != null)
                return other;
        }
        return defaultValue;
    }

    @Nullable
    public static <A, B> B failToNull(@Nullable A supplied, Function<A, B> ifNotNull) {
        return supplied == null ? null : ifNotNull.apply(supplied);
    }

    @Nullable
    public static <A, B, C> C failToNull(@Nullable A supplied, Function<A, B> ifNotNull, Function<B, C> chain2) {
        if (supplied == null) return null;
        B chain1 = ifNotNull.apply(supplied);
        if (chain1 == null) return null;
        return chain2.apply(chain1);
    }
}
