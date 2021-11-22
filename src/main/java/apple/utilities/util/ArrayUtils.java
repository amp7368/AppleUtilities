package apple.utilities.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class ArrayUtils {
    public static <T> T[] combine(T[][] arrays, Function<Integer, T[]> constructor) {
        int length = 0;
        for (T[] array : arrays) length += array.length;
        T[] finalArray = constructor.apply(length);
        int index = 0;
        for (T[] array : arrays) {
            int l = array.length;
            System.arraycopy(array, 0, finalArray, index, l);
            index += l;
        }
        return finalArray;
    }

    public static <T> T[] combine(T[] array1, T[] array2, Function<Integer, T[]> constructor) {
        if (array1.length == 0) return array2;
        else if (array2.length == 0) return array1;
        int length = array1.length + array2.length;
        T[] finalArray = constructor.apply(length);
        int index = 0;
        int l = array1.length;
        System.arraycopy(array1, 0, finalArray, index, l);
        index += l = array2.length;
        System.arraycopy(array2, 0, finalArray, index, l);
        return finalArray;
    }

    public static Object[] combineObjectArrays(Object[] array1, Object[] array2, Function<Integer, Object[]> constructor) {
        if (array1.length == 0) return array2;
        else if (array2.length == 0) return array1;
        int length = array1.length + array2.length;
        Object[] finalArray = constructor.apply(length);
        int index = 0;
        int l = array1.length;
        System.arraycopy(array1, 0, finalArray, index, l);
        index += l = array2.length;
        System.arraycopy(array2, 0, finalArray, index, l);
        return finalArray;
    }

    public static <T> T[] combine(T[] array, T element, Function<Integer, T[]> constructor) {
        int arrayLength = array.length;
        T[] finalArray = constructor.apply(arrayLength + 1);
        System.arraycopy(array, 0, finalArray, 0, array.length);
        finalArray[arrayLength] = element;
        return finalArray;
    }

    @SafeVarargs
    public static <T> List<T> combine(List<T> array1, T... rest) {
        List<T> newList = new ArrayList<>(array1);
        newList.addAll(Arrays.asList(rest));
        return newList;
    }

    public static <T> T[] subList(T[] array, Function<Integer, T[]> arrayCreator, int beginIndex, int endIndex) {
        int size = endIndex - beginIndex;
        if (size <= 0) size = array.length + (size % array.length);
        T[] newArray = arrayCreator.apply(size);
        System.arraycopy(array, beginIndex, newArray, 0, size);
        return newArray;
    }
}
