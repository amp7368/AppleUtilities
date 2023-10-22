package apple.utilities.database.concurrent.serialize;

import com.google.gson.Gson;

public interface IConcurrentAJDSerializing extends ConcurrentAJDSerializer, ConcurrentAJDDeserializer {

    static IConcurrentAJDSerializing gsonSerializing(Gson gson) {
        return new ConcurrentAJDSerializing(
            gson::toJson,
            gson::fromJson
        );
    }

}
