package apple.utilities.database.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.rmi.AccessException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class ReflectionsUtil {

    private static final Set<Class<?>> primitives = Set.of(String.class, Boolean.class, Character.class, Byte.class, Short.class,
        Integer.class, Long.class, Float.class, Double.class, Collection.class);

    public static <T> T merge(Class<T> mergeOnto, T mergeFrom) throws AccessException {
        T created = makeNew(mergeOnto);
        return mergeAllowSwap(created, mergeFrom, 0);
    }

    private static <T> T mergeAllowSwap(T mergeOnto, T mergeFrom, int depth) throws TieredAccessException {
        if (mergeOnto == null)
            return mergeFrom;
        if (mergeFrom == null)
            return mergeOnto;
        if (depth > 75) // arbitrary magic number.
            return mergeFrom;
        if (mergeFrom.getClass() != mergeOnto.getClass()) {
            return mergeFrom;
        }
        for (Field field : getFields(mergeOnto.getClass())) {
            if (isFieldIgnored(field)) continue;
            if (!field.trySetAccessible()) {
                String msg = "Field '%s' of class '%s' is inaccessible for reflection %n".formatted(
                    field.getName(),
                    mergeOnto.getClass().getCanonicalName());
                throw new TieredAccessException(msg);
            }
            Object val;
            try {
                val = mergeField(mergeOnto, mergeFrom, depth, field);
            } catch (TieredAccessException e) {
                String msg = "Failed to update field '%s' of class '%s'".formatted(
                    mergeOnto.getClass().getCanonicalName(),
                    field.getType().getCanonicalName());
                e.pushMsg(msg);
                throw e;
            }
            if (val == null)
                continue;

            try {
                field.set(mergeOnto, val);
            } catch (IllegalAccessException e) {
                String msg = "Field '%s' of class '%s' could not be set using reflection (even after setting accessible)".formatted(
                    field.getName(),
                    mergeOnto.getClass().getCanonicalName());
                throw new TieredAccessException(msg, e);
            }
        }
        return mergeOnto;
    }

    private static boolean isFieldIgnored(Field field) {
        int fieldModifiers = field.getModifiers();
        return Modifier.isStatic(fieldModifiers);//|| Modifier.isTransient(fieldModifiers);
    }

    @NotNull
    private static Set<Field> getFields(Class<?> clazz) {
        Set<Field> fields = new HashSet<>();
        fields.addAll(List.of(clazz.getFields()));
        fields.addAll(List.of(clazz.getDeclaredFields()));
        return fields;
    }

    private static <T> Object mergeField(T initial, T loaded, int depth, Field field) throws TieredAccessException {
        Object initialFieldVal = null;
        try {
            initialFieldVal = field.get(initial);
            Field loadedField;
            try {
                loadedField = loaded.getClass().getDeclaredField(field.getName());
            } catch (NoSuchFieldException e) {
                loadedField = loaded.getClass().getField(field.getName());
            }
            loadedField.trySetAccessible();
            Object loadedFieldVal = loadedField.get(loaded);

            if (loadedFieldVal == null) {
                return initialFieldVal;
            } else if (initialFieldVal == null) {
                return loadedFieldVal;
            } else if (isValueSimple(field.getType())) {
                return loadedFieldVal;
            } else if (isMap(field.getType())) {
                Map<Object, Object> map = new HashMap<>();
                map.putAll((Map<?, ?>) initialFieldVal);
                map.putAll((Map<?, ?>) loadedFieldVal);
                return map;
            } else {
                if (Modifier.isTransient(field.getModifiers())) return loadedFieldVal;
                return mergeAllowSwap(initialFieldVal, loadedFieldVal, depth + 1);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            // might be different classes
        }
        return initialFieldVal;
    }

    private static boolean isMap(Class<?> fieldType) {
        return fieldType == HashMap.class || fieldType == Map.class;
    }

    private static boolean isValueSimple(Class<?> fieldType) {
        return fieldType.isPrimitive() || primitives.stream().anyMatch((c) -> c.isAssignableFrom(fieldType));
    }

    public static <DBType> DBType makeNew(Class<DBType> type) {
        try {
            return type.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException("There is not a no-args constructor in " + type.getName(), e);
        }
    }

    private static class TieredAccessException extends AccessException {

        private final List<String> stackOfMsgs = new ArrayList<>();

        public TieredAccessException(String msg) {
            super(msg);
            this.pushMsg(msg);
        }

        public TieredAccessException(String msg, Exception cause) {
            super(msg, cause);
            this.pushMsg(msg);
        }

        public void pushMsg(String msg) {
            this.stackOfMsgs.add(0, msg);
        }

        @Override
        public String getMessage() {
            return String.join(" Caused by:\n", stackOfMsgs);
        }
    }
}
