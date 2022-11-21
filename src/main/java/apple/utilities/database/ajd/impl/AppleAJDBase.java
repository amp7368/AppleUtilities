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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.Nullable;

public class AppleAJDBase<DBType> implements AppleAJD<DBType> {

    private static final List<Class<?>> primitives = List.of(String.class, Boolean.class, Character.class, Byte.class, Short.class,
        Integer.class, Long.class, Float.class, Double.class, Collection.class, Map.class);

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
        return this.merge(makeNew(), loaded, dbType, 0);
    }

    protected <T> T merge(T initial, T loaded, Class<?> clazz, int depth) {
        if (initial == null)
            return loaded;
        if (loaded == null)
            return initial;
        if (depth > 50) // random arbitrary magic number.
            return loaded;
        Set<Field> fields = new HashSet<>() {{
            addAll(List.of(clazz.getFields()));
            addAll(List.of(clazz.getDeclaredFields()));
        }};
        for (Field field : fields) {
            mergeField(initial, loaded, depth, field);
        }
        return initial;
    }

    private <T> void mergeField(T initial, T loaded, int depth, Field field) {
        if (Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers()))
            return;
        field.trySetAccessible();
        try {
            Object initialFieldVal = field.get(initial);
            Object loadedFieldVal = field.get(loaded);
            if (loadedFieldVal == null)
                // keep initial
                return;
            if (initialFieldVal == null || this.isValueSimple(field.getType())) {
                field.set(initial, loadedFieldVal);
            } else {
                this.merge(initialFieldVal, loadedFieldVal, field.getType(), depth + 1);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
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
