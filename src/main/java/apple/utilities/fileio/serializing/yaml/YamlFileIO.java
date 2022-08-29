package apple.utilities.fileio.serializing.yaml;

import apple.file.yml.BaseYcm;
import apple.utilities.fileio.common.exception.ConsumerIOException;
import apple.utilities.fileio.common.exception.SupplierIOException;
import com.google.gson.Gson;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

public interface YamlFileIO extends YamlUtils {

    default <T> T loadYaml(File file, Class<T> type) throws IOException {
        return this.loadYaml(file, type, null);
    }

    default <T> T loadYaml(File file, Class<T> type, @Nullable BaseYcm ycm) throws IOException {
        if (ycm == null)
            ycm = defaultYcm();
        return ycm.load(file, type);
    }

    default <T> SupplierIOException<T> loadYamlLater(File file, Class<? extends T> type) {
        return loadYamlLater(file, type, null);
    }

    default <T> SupplierIOException<T> loadYamlLater(File file, Class<? extends T> type,
        @Nullable BaseYcm yaml) {
        return () -> this.loadYaml(file, type, yaml);
    }

    default <T> void saveYaml(File file, T saveThis) throws IOException {
        saveYaml(file, saveThis, null);
    }

    default <T> void saveYaml(File file, T saveThis, Yaml yaml) throws IOException {
        file.getParentFile().mkdirs();
        if (yaml == null)
            yaml = defaultYaml();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(yaml.dumpAsMap(saveThis));
        }
    }

    default <T> ConsumerIOException<T> saveYamlLater(File file) {
        return this.saveYamlLater(file, null);
    }

    default <T> ConsumerIOException<T> saveYamlLater(File file, @Nullable Yaml yaml) {
        return (saveThis) -> this.saveYaml(file, saveThis, yaml);
    }

    class Hello {

        public String a = "a";
        public boolean b = true;
    }
}
