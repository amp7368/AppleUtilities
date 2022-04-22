package apple.utilities.fileio.serializing.json;

import apple.utilities.fileio.common.exception.ConsumerIOException;
import com.google.gson.Gson;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public interface JsonToFile extends GsonUtils {
    default <T> void saveJson(File file, T saveThis) throws IOException {
        saveJson(file, saveThis, null);
    }

    default <T> void saveJson(File file, T saveThis, @Nullable Gson gson) throws IOException {
        file.getParentFile().mkdirs();
        if (gson == null) gson = defaultGson();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            gson.toJson(saveThis, writer);
        }
    }

    default <T> ConsumerIOException<T> saveJsonLater(File file) {
        return this.saveJsonLater(file, null);
    }

    default <T> ConsumerIOException<T> saveJsonLater(File file, @Nullable Gson gson) {
        return (saveThis) -> this.saveJson(file, saveThis, gson);
    }
}
