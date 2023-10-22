package apple.utilities.database.concurrent.serialize;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface ConcurrentAJDDeserializer {

    @Nullable
    default <T> T deserialize(File file, Class<T> type) throws IOException {
        if (!file.exists()) return null;

        return deserialize(new FileReader(file), type);
    }

    <T> T deserialize(Reader reader, Class<T> type) throws IOException;
}
