package apple.utilities.database.util;

import apple.utilities.database.util.merge.ReflectionsMergeOptions;
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
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

public class ReflectionsUtil {

    private static final Set<Class<?>> PRIMITIVES = Set.of(String.class, Boolean.class, Character.class, Byte.class, Short.class,
        Integer.class, Long.class, Float.class, Double.class);

    public static <T> T merge(Class<T> mergeOnto, T mergeFrom) throws AccessException {
        return merge(makeNew(mergeOnto), mergeFrom);
    }

    public static <T> T merge(Class<T> mergeOnto, T mergeFrom, ReflectionsMergeOptions options) throws AccessException {
        return merge(makeNew(mergeOnto), mergeFrom);
    }

    public static <T> T merge(T mergeOnto, T mergeFrom) throws AccessException {
        return merge(mergeOnto, mergeFrom, 0, new ReflectionsMergeOptions());
    }

    public static <T> T merge(T mergeOnto, T mergeFrom, ReflectionsMergeOptions options) throws AccessException {
        return merge(mergeOnto, mergeFrom, 0, options);
    }

    @Internal
    public static <T> T merge(T mergeOnto, T mergeFrom, int depth, ReflectionsMergeOptions options)
        throws TieredAccessException {
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
                return mergeFrom;
            }
            Object val;
            try {
                val = mergeField(mergeOnto, mergeFrom, depth, field, options);
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
        // maybe don't have isTransient because you should still keep the fields with transient modifiers
        // when deserializing and merging onto a newly created object?
        return Modifier.isStatic(fieldModifiers); //|| Modifier.isTransient(fieldModifiers);
    }

    @NotNull
    private static Set<Field> getFields(Class<?> clazz) {
        Set<Field> fields = new HashSet<>();
        fields.addAll(List.of(clazz.getFields()));
        fields.addAll(List.of(clazz.getDeclaredFields()));
        Class<?> parent = clazz.getSuperclass();
        if (parent != null) fields.addAll(getFields(parent));
        return fields;
    }

    private static <T> Object mergeField(T initial, T loaded, int depth, Field field, ReflectionsMergeOptions options)
        throws TieredAccessException {
        Object initialFieldVal = null;
        try {
            String fieldName = field.getName();
            initialFieldVal = field.get(initial);
            Field loadedField = getField(loaded.getClass(), fieldName);
            Object loadedFieldVal = loadedField.get(loaded);

            if (loadedFieldVal == null) {
                return initialFieldVal;
            } else if (initialFieldVal == null) {
                return loadedFieldVal;
            } else if (isMap(field.getType())) {
                try {
                    return options.handleMap().handleMap(
                        (Map<?, ?>) initialFieldVal,
                        (Map<?, ?>) loadedFieldVal,
                        depth,
                        options);
                } catch (AccessException e) {
                    String msg = "Unable to merge field %s".formatted(fieldName);
                    throw new TieredAccessException(msg, e);
                }
            } else if (isTypeSimple(field.getType())) {
                return loadedFieldVal;
            } else {
                if (Modifier.isTransient(field.getModifiers())) return loadedFieldVal;
                return merge(initialFieldVal, loadedFieldVal, depth + 1, options);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            // might be different classes
        }
        return initialFieldVal;
    }

    @NotNull
    private static <T> Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException ignored) {
        }
        try {
            if (field == null) field = clazz.getField(fieldName);
        } catch (NoSuchFieldException ignored) {
        }
        if (field != null) {
            field.trySetAccessible();
            return field;
        }
        Class<?> parent = clazz.getSuperclass();
        if (parent == null)
            throw new NoSuchFieldException("Field '%s' not found".formatted(fieldName));
        return getField(parent, fieldName);
    }

    private static boolean isMap(Class<?> fieldType) {
        return fieldType == HashMap.class || fieldType == Map.class;
    }

    public static boolean isTypeSimple(Class<?> fieldType) {
        return isTypePrimitive(fieldType) || Collection.class.isAssignableFrom(fieldType);
    }

    public static boolean isTypePrimitive(Class<?> fieldType) {
        return fieldType.isPrimitive() || PRIMITIVES.stream().anyMatch(p -> p.isAssignableFrom(fieldType));
    }

    public static <DBType> DBType makeNew(Class<DBType> type) {
        try {
            return type.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException("There is not a no-args constructor in " + type.getName(), e);
        }
    }

    public static class TieredAccessException extends AccessException {

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
