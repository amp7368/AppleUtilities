package apple.utilities.database.ajd;

import apple.utilities.structures.empty.Placeholder;
import apple.utilities.threading.util.supplier.SupplierUncaught;

import java.io.File;

public class AppleAJD implements AppleAJDUtil {
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
