package apple.utilities.database.concurrent.serialize;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

@FunctionalInterface
public interface ConcurrentAJDSerializer {

    default <T> void serialize(T obj, File file) throws IOException {
        serialize(obj, new FileWriter(file));
    }

    default <T> String serialize(T obj) {
        try (StringWriter writer = new StringWriter()) {
            serialize(obj, writer);
            return writer.toString();
        } catch (IOException e) {
            // should never happen since we're using a StringWriter which doesn't have the same exceptions
            throw new RuntimeException(e);
        }
    }

    <T> void serialize(T obj, Writer writer) throws IOException;
}
