package apple.utilities.database.ajd.impl;

import apple.file.yml.BaseYcm;
import apple.utilities.database.ajd.AppleAJD;
import apple.utilities.database.util.ReflectionsUtil;
import apple.utilities.fileio.serializing.FileIOJoined;
import apple.utilities.structures.empty.Placeholder;
import apple.utilities.threading.service.base.create.AsyncTaskQueueStart;
import apple.utilities.threading.util.supplier.SupplierUncaught;
import com.google.gson.Gson;
import java.io.File;
import java.rmi.AccessException;
import org.jetbrains.annotations.Nullable;

public class AppleAJDBase<DBType> implements AppleAJD<DBType> {

    protected final AsyncTaskQueueStart<?> queue;
    protected final Class<DBType> dbType;

    protected AppleAJDSerializing serializing;

    public AppleAJDBase(Class<DBType> dbType, AsyncTaskQueueStart<?> queue) {
        this.queue = queue;
        this.dbType = dbType;
        setSerializingJson(null);
    }

    // modify 'initial', discard 'loaded'
    protected DBType merge(DBType loaded) throws AccessException {
        return ReflectionsUtil.merge(this.dbType, loaded);
    }

    @Override
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
