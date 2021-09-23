package apple.utilities.util;

import java.util.Comparator;
import java.util.Objects;
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
        return new Scored(originalScore, original);

    }

    public interface Scoreable<T> extends IntSupplier, Supplier<T> {
    }

    public static final class ScoreableSimple<T> implements Scoreable<T> {
        private final IntSupplier score;
        private final Supplier<T> giver;

        public ScoreableSimple(IntSupplier score, Supplier<T> giver) {
            this.score = score;
            this.giver = giver;
        }

        @Override
        public int getAsInt() {
            return score.getAsInt();
        }

        @Override
        public T get() {
            return giver.get();
        }

        public IntSupplier score() {
            return score;
        }

        public Supplier<T> giver() {
            return giver;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            ScoreableSimple that = (ScoreableSimple) obj;
            return Objects.equals(this.score, that.score) &&
                    Objects.equals(this.giver, that.giver);
        }

        @Override
        public int hashCode() {
            return Objects.hash(score, giver);
        }

        @Override
        public String toString() {
            return "ScoreableSimple[" +
                    "score=" + score + ", " +
                    "giver=" + giver + ']';
        }

    }

    public static final class Scored<T> {
        private final int score;
        private final T val;

        public Scored(int score, T val) {
            this.score = score;
            this.val = val;
        }

        public int score() {
            return score;
        }

        public T val() {
            return val;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            Scored that = (Scored) obj;
            return this.score == that.score &&
                    Objects.equals(this.val, that.val);
        }

        @Override
        public int hashCode() {
            return Objects.hash(score, val);
        }

        @Override
        public String toString() {
            return "Scored[" +
                    "score=" + score + ", " +
                    "val=" + val + ']';
        }

    }
}
