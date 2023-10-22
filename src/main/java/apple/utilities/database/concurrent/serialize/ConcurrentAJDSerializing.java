package apple.utilities.database.concurrent.serialize;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public final class ConcurrentAJDSerializing implements IConcurrentAJDSerializing {

    private ConcurrentAJDDeserializer deserializer;
    private ConcurrentAJDSerializer serializer;

    public ConcurrentAJDSerializing(
        ConcurrentAJDSerializer serializer,
        ConcurrentAJDDeserializer deserializer
    ) {
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    public ConcurrentAJDSerializing() {
        Gson gson = new Gson();
        this.serializer = gson::toJson;
        this.deserializer = gson::fromJson;
    }

    @Override
    public <T> T deserialize(Reader reader, Class<T> type) throws IOException {
        return deserializer.deserialize(reader, type);
    }

    @Override
    public <T> void serialize(T obj, Writer writer) throws IOException {
        serializer.serialize(obj, writer);
    }

    public ConcurrentAJDSerializing serializer(ConcurrentAJDSerializer serializer) {
        this.serializer = serializer;
        return this;
    }

    public ConcurrentAJDSerializing deserializer(ConcurrentAJDDeserializer deserializer) {
        this.deserializer = deserializer;
        return this;
    }

    public ConcurrentAJDSerializing asGson(Gson gson) {
        this.serializer = gson::toJson;
        this.deserializer = gson::fromJson;
        return this;
    }

}
