package apple.utilities.database.ajd.impl;

import apple.file.yml.BaseYcm;
import apple.utilities.database.ajd.AppleAJD;
import apple.utilities.fileio.serializing.FileIOJoined;
import apple.utilities.structures.empty.Placeholder;
import apple.utilities.threading.service.base.create.AsyncTaskQueueStart;
import apple.utilities.threading.util.supplier.SupplierUncaught;
import com.google.gson.Gson;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AppleAJDBase<DBType> implements AppleAJD<DBType> {

    private static final List<Class<?>> primitives = List.of(String.class, Boolean.class, Character.class, Byte.class, Short.class,
        Integer.class, Long.class, Float.class, Double.class, Collection.class);

    protected final AsyncTaskQueueStart<?> queue;
    protected final Class<DBType> dbType;

    protected AppleAJDSerializing serializing;

    public AppleAJDBase(Class<DBType> dbType, AsyncTaskQueueStart<?> queue) {
        this.queue = queue;
        this.dbType = dbType;
        setSerializingJson(null);
    }

    // modify 'initial', discard 'loaded'
    protected DBType merge(DBType loaded) {
        return this.mergeAllowSwap(makeNew(), loaded, 0, false);
    }

    private <T> T mergeAllowSwap(T mergeOnto, T mergeFrom, int depth, boolean didSwap) {
        if (mergeOnto == null)
            return mergeFrom;
        if (mergeFrom == null)
            return mergeOnto;
        if (depth > 50) // arbitrary magic number.
            return mergeFrom;
        if (mergeFrom.getClass() != mergeOnto.getClass()) {
            if (didSwap)
                return mergeOnto;
            didSwap = true;
            T temp = mergeFrom;
            mergeFrom = mergeOnto;
            mergeOnto = temp;
        }
        for (Field field : getFields(mergeOnto.getClass())) {
            if (isFieldIgnored(field))
                continue;
            field.trySetAccessible();
            Object val = mergeField(mergeOnto, mergeFrom, depth, field, didSwap);
            if (val == null)
                continue;
            try {
                field.set(mergeOnto, val);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return mergeOnto;
    }

    private boolean isFieldIgnored(Field field) {
        return Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers());
    }

    @NotNull
    private Set<Field> getFields(Class<?> clazz) {
        Set<Field> fields = new HashSet<>();
        fields.addAll(List.of(clazz.getFields()));
        fields.addAll(List.of(clazz.getDeclaredFields()));
        return fields;
    }

    private <T> Object mergeField(T initial, T loaded, int depth, Field field, boolean didSwap) {
        try {
            Object initialFieldVal = field.get(initial);
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
            } else if (this.isValueSimple(field.getType())) {
                return loadedFieldVal;
            } else if (this.isMap(field.getType())) {
                Map<Object, Object> map = new HashMap<>();
                map.putAll((Map<?, ?>) initialFieldVal);
                map.putAll((Map<?, ?>) loadedFieldVal);
                return map;
            } else {
                return this.mergeAllowSwap(initialFieldVal, loadedFieldVal, depth + 1, didSwap);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // might be different classes
        }
        return null;
    }

    private boolean isMap(Class<?> fieldType) {
        return Map.class.isAssignableFrom(fieldType);
    }

    private boolean isValueSimple(Class<?> fieldType) {
        return fieldType.isPrimitive() || primitives.stream().anyMatch((c) -> c.isAssignableFrom(fieldType));
    }

    public DBType makeNew() {
        try {
            return this.dbType.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException("There is not a no-args constructor in " + this.dbType.getName(), e);
        }
    }

    public void setSerializing(AppleAJDSerializer serializer, AppleAJDDeserializer deserializer) {
        this.serializing = new AppleAJDSerializing(serializer, deserializer);
    }

    @Override
    public void setSerializingJson(@Nullable Gson gson) {
        this.serializing = new AppleAJDSerializing(new AppleAJDSerializer() {
            @Override
            public <T> SupplierUncaught<Placeholder> accept(File file, T saveThis) {
                return () -> {
                    FileIOJoined.get().saveJson(file, saveThis, gson);
                    return null;
                };
            }
        }, new AppleAJDDeserializer() {
            @Override
            public <T> SupplierUncaught<T> accept(File file, Class<T> type) {
                return () -> {
                    if (!file.exists())
                        return null;
                    return FileIOJoined.get().loadJson(file, type, gson);
                };
            }
        });

    }

    @Override
    public void setSerializingYaml(@Nullable BaseYcm ycm) {
        this.serializing = new AppleAJDSerializing(new AppleAJDSerializer() {
            @Override
            public <T> SupplierUncaught<Placeholder> accept(File file, T saveThis) {
                return () -> {
                    FileIOJoined.get().saveYcm(file, saveThis, ycm);
                    return null;
                };
            }
        }, new AppleAJDDeserializer() {
            @Override
            public <T> SupplierUncaught<T> accept(File file, Class<T> saveThis) {
                return () -> {
                    if (!file.exists())
                        return null;
                    return FileIOJoined.get().loadYcm(file, saveThis, ycm);
                };
            }
        });
    }


    @FunctionalInterface
    public interface AppleAJDSerializer {

        <DBType> SupplierUncaught<Placeholder> accept(File file, DBType saveThis);
    }

    @FunctionalInterface
    public interface AppleAJDDeserializer {

        <DBType> SupplierUncaught<DBType> accept(File file, Class<DBType> saveThis);
    }

    public record AppleAJDSerializing(AppleAJDSerializer serializer, AppleAJDDeserializer deserializer) {

        public <DBType> SupplierUncaught<Placeholder> serializer(File file, DBType saveThis) {
            return this.serializer.accept(file, saveThis);
        }

        public <DBType> SupplierUncaught<DBType> deserializer(File file, Class<DBType> type) {
            return this.deserializer.accept(file, type);
        }
    }
}
