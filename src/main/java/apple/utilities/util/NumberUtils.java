package apple.utilities.util;

import java.util.Comparator;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class NumberUtils {
    public static boolean betweenExclusive(int lower, int n, int upper) {
        return lower < n && n < upper;
    }

    public static boolean betweenInclusive(int lower, int n, int upper) {
        return lower <= n && n <= upper;
    }

    public static boolean between(int lower, int n, int upper) {
        return lower <= n && n < upper;
    }

    public static boolean[] betweenMultipleExclusive(int lower, int upper, int... ns) {
        boolean[] bs = new boolean[ns.length];
        for (int i = 0; i < ns.length; i++) {
            int n = ns[i];
            bs[i] = lower < n && n < upper;
        }
        return bs;
    }

    public static int getBetween(int lower, int n, int upper) {
        return Math.max(upper > n ? n : upper - 1, lower);
    }

    public static int getBetweenInclusive(int lower, int n, int upper) {
        return lower <= n ? Math.min(upper, n) : lower;
    }

    public static int getBetweenExclusive(int lower, int n, int upper) {
        return lower < n ? upper > n ? n : upper - 1 : lower + 1;
    }

    public static boolean betweenMultipleExclusiveOr(int lower, int upper, int... ns) {
        for (int n : ns) {
            if (lower < n && n < upper)
                return true;
        }
        return false;
    }

    public static boolean[] betweenMultipleInclusive(int lower, int upper, int... ns) {
        boolean[] bs = new boolean[ns.length];
        for (int i = 0; i < ns.length; i++) {
            int n = ns[i];
            bs[i] = lower <= n && n <= upper;
        }
        return bs;
    }

    public static boolean[] betweenMultiple(int lower, int upper, int... ns) {
        boolean[] bs = new boolean[ns.length];
        for (int i = 0; i < ns.length; i++) {
            int n = ns[i];
            bs[i] = lower <= n && n < upper;
        }
        return bs;
    }

    @SafeVarargs
    public static <T> Scored<T> getHighest(T original, int originalScore, Scoreable<T>... scoreable) {
        return getBest(original, originalScore, Integer::compareTo, scoreable);
    }

    @SafeVarargs
    public static <T> Scored<T> getLowest(T original, int originalScore, Scoreable<T>... scoreable) {
        return getBest(original, originalScore, (o1, o2) -> o2 - o1, scoreable);
    }

    @SafeVarargs
    public static <T> Scored<T> getBest(T original, int originalScore, Comparator<Integer> compare, Scoreable<T>... scoreable) {
        for (Scoreable<T> score : scoreable) {
            int value = score.getAsInt();
            if (compare.compare(originalScore, value) > 0) {
                original = score.get();
                originalScore = value;
            }
        }
        return new Scored<>(originalScore, original);

    }

    public interface Scoreable<T> extends IntSupplier, Supplier<T> {
    }

    public record ScoreableSimple<T>(IntSupplier score, Supplier<T> giver) implements Scoreable<T> {
        @Override
        public int getAsInt() {
            return score.getAsInt();
        }

        @Override
        public T get() {
            return giver.get();
        }
    }

    public record Scored<T>(int score, T val) {
    }
}
