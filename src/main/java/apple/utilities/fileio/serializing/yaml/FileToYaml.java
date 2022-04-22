package apple.utilities.fileio.serializing.yaml;

import apple.utilities.fileio.common.exception.SupplierIOException;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public interface FileToYaml extends YamlUtils {
    default <T> T loadYaml(File file, Class<T> type) throws IOException {
        return this.loadYaml(file, type, null);
    }

    default <T> T loadYaml(File file, Class<T> type, Yaml yaml) throws IOException {
        if (yaml == null) yaml = defaultYaml();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return yaml.loadAs(reader, type);
        }
    }


    default <T> SupplierIOException<T> loadYamlLater(File file, Class<? extends T> type) {
        return loadYamlLater(file, type, null);
    }

    default <T> SupplierIOException<T> loadYamlLater(File file, Class<? extends T> type, @Nullable Yaml yaml) {
        return () -> this.loadYaml(file, type, yaml);
    }
}
