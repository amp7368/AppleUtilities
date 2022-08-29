package apple.utilities.fileio.serializing.json;

import apple.utilities.fileio.common.exception.ConsumerIOException;
import apple.utilities.fileio.common.exception.SupplierIOException;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import org.jetbrains.annotations.Nullable;

public interface JsonFileIO extends GsonUtils {

    default <T> void saveJson(File file, T saveThis) throws IOException {
        saveJson(file, saveThis, null);
    }

    default <T> void saveJson(File file, T saveThis, @Nullable Gson gson) throws IOException {
        file.getParentFile().mkdirs();
        if (gson == null)
            gson = defaultGson();
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

    default <T> T loadJson(File file, Type type) throws IOException {
        return this.loadJson(file, type, null);
    }

    default <T> T loadJson(InputStream inputStream, Type type) throws IOException {
        return this.loadJson(inputStream, type, null);
    }

    default <T> T loadJson(File file, Type type, @Nullable Gson gson) throws IOException {
        if (gson == null)
            gson = defaultGson();
        if (!file.exists())
            return null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return gson.fromJson(reader, type);
        }
    }

    default <T> T loadJson(InputStream inputStream, Type type, @Nullable Gson gson)
        throws IOException {
        if (gson == null)
            gson = defaultGson();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return gson.fromJson(reader, type);
        }
    }

    default <T> SupplierIOException<T> loadJsonLater(File file, Type type) {
        return loadJsonLater(file, type, null);
    }

    default <T> SupplierIOException<T> loadJsonLater(InputStream inputStream, Type type) {
        return loadJsonLater(inputStream, type, null);
    }

    default <T> SupplierIOException<T> loadJsonLater(File file, Type type, @Nullable Gson gson) {
        return () -> this.loadJson(file, type, gson);
    }

    default <T> SupplierIOException<T> loadJsonLater(InputStream inputStream, Type type,
        @Nullable Gson gson) {
        return () -> this.loadJson(inputStream, type, gson);
    }
}
