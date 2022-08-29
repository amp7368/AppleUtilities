package apple.utilities.database.ajd;

import apple.utilities.database.SaveFileable;
import apple.utilities.structures.empty.Placeholder;
import apple.utilities.threading.service.base.create.AsyncTaskQueueStart;
import apple.utilities.threading.util.supplier.SupplierUncaught;
import com.google.gson.Gson;

import java.io.File;

public class AppleAJD implements AppleAJDUtil {
    public static <DBType, TaskExtra> AppleAJDInst<DBType, TaskExtra> createInst(Class<DBType> dbType, File file, AsyncTaskQueueStart<TaskExtra> queue) {
        return new AppleAJDInstImpl<>(dbType, file, queue);
    }

    public static <DBType extends SaveFileable, TaskExtra> AppleAJDTyped<DBType, TaskExtra> createTyped(Class<DBType> dbType, File folder, AsyncTaskQueueStart<TaskExtra> queue) {
        return new AppleAJDTypedImpl<>(dbType, folder, queue);
    }

    private AppleAJDSerializing serializing;

    {
        setSerializingJson();
    }

    public void setSerializing(AppleAJDSerializer serializer, AppleAJDDeserializer deserializer) {
        this.serializing = new AppleAJDSerializing(serializer, deserializer);
    }

    public void setSerializingJson() {
        this.setSerializing(this::jsonSerializer, this::jsonDeserializer);
    }

    public void setSerializingJson(Gson gson) {
        AppleAJDSerializer serializer = new AppleAJDSerializer() {
            @Override
            public <DBType> SupplierUncaught<Placeholder> accept(File dbFile, DBType saveThis) {
                return jsonSerializer(dbFile, saveThis, gson);
            }
        };
        AppleAJDDeserializer deserializer = new AppleAJDDeserializer() {
            @Override
            public <DBType> SupplierUncaught<DBType> accept(File file, Class<DBType> saveThis) {
                return jsonDeserializer(file, saveThis, gson);
            }
        };
        this.setSerializing(serializer, deserializer);
    }

    public void setSerializingYaml() {
        this.setSerializing(this::yamlSerializer, this::yamlDeserializer);
    }

    @Override
    public <DBType> SupplierUncaught<Placeholder> serializer(File file, DBType saveThis) {
        return this.serializing.serializer().accept(file, saveThis);
    }

    @Override
    public <DBType> SupplierUncaught<DBType> deserializer(File file, Class<DBType> dbType) {
        return this.serializing.deserializer().accept(file, dbType);
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
    }


}
