package apple.utilities.util;

import java.text.NumberFormat;
import java.util.function.Function;

public class Pretty {
    public static String upperCaseFirst(String s) {
        String[] split = s.split(" ");
        for (int i = 0; i < split.length; i++) {
            char[] chars = split[i].toCharArray();
            if (chars.length != 0)
                chars[0] = Character.toUpperCase(chars[0]);
            for (int j = 1; j < chars.length; j++) {
                chars[j] = Character.toLowerCase(chars[j]);
            }
            split[i] = new String(chars);
        }
        return String.join(" ", split);
    }

    public static String commas(long n) {
        return NumberFormat.getNumberInstance().format(n);
    }

    public static String truncate(String s, int limit) {
        if (s.length() > limit) {
            return s.substring(0, limit - 3) + "...";
        }
        return s;
    }

    public static String plural(int count, String s) {
        return count == 1 ? s : s + "s";
    }

    /**
     * make a String prettified based on whether count is plural or singular
     * %s% will be replaced with the pluralized version
     * %c% will be replaced with the count
     *
     * @param format the String to be formatted
     * @param count  the count we are inserting into the String
     *               also determines whether %%% is replaced with "s" or ""
     * @return the prettified String
     */
    public static String pluralFormat(String format, int count) {
        return format.replace("%s%", count == 1 ? "" : "s").replace("%c%", String.valueOf(count));
    }

    @SafeVarargs
    public static <T> String join(String delimiter, Function<T, String> toString, T... objects) {
        String[] strings = new String[objects.length];
        for (int i = 0; i < objects.length; i++) {
            strings[i] = toString.apply(objects[i]);
        }
        return String.join(delimiter, strings);
    }
}
