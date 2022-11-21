package apple.utilities.fileio.serializing.json;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import org.jetbrains.annotations.Nullable;

public interface FileIOJson {

    default Gson defaultGson() {
        return new Gson();
    }

    default <T> void saveJson(File file, T saveThis, @Nullable Gson gson) throws IOException {
        file.getParentFile().mkdirs();
        if (gson == null)
            gson = defaultGson();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            gson.toJson(saveThis, writer);
        }
    }

    default <T> T loadJson(File file, Type type, @Nullable Gson gson) throws IOException {
        return this.loadJson(new FileInputStream(file), type, gson);
    }

    default <T> T loadJson(InputStream inputStream, Type type, @Nullable Gson gson) throws IOException {
        if (gson == null)
            gson = defaultGson();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return gson.fromJson(reader, type);
        }
    }
}
