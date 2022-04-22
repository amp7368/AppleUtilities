package apple.utilities.fileio.serializing.json;

import apple.utilities.fileio.common.exception.SupplierIOException;
import com.google.gson.Gson;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public interface FileToJson extends GsonUtils {
    default <T> T loadJson(File file, Class<T> type) throws IOException {
        return this.loadJson(file, type, null);
    }

    default <T> T loadJson(File file, Class<T> type, @Nullable Gson gson) throws IOException {
        if (gson == null) gson = defaultGson();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return gson.fromJson(reader, type);
        }
    }

    default <T> SupplierIOException<T> loadJsonLater(File file, Class<? extends T> type) {
        return loadJsonLater(file, type, null);
    }

    default <T> SupplierIOException<T> loadJsonLater(File file, Class<? extends T> type, @Nullable Gson gson) {
        return () -> this.loadJson(file, type, gson);
    }
}
