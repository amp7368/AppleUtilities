package apple.utilities.util;

public class BooleanUtils {
    public static boolean isAtLeastNTrue(int n, boolean... bs) {
        for (boolean b : bs) {
            if (b && --n == 0) return true;
        }
        return false;
    }

    public static boolean isAtLeastN(int n, boolean... bs) {
        int n1 = n;
        for (boolean b : bs) {
            if (b) {
                if (--n == 0) return true;
            } else {
                if (--n1 == 0) return true;
            }
        }
        return false;
    }
}
