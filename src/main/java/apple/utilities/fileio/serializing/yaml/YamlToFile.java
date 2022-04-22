package apple.utilities.fileio.serializing.yaml;

import apple.utilities.fileio.common.exception.ConsumerIOException;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public interface YamlToFile extends YamlUtils {
    default <T> void saveYaml(File file, T saveThis) throws IOException {
        saveYaml(file, saveThis, null);
    }

    default <T> void saveYaml(File file, T saveThis, Yaml yaml) throws IOException {
        file.getParentFile().mkdirs();
        if (yaml == null) yaml = defaultYaml();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            yaml.dump(saveThis, writer);
        }

    }

    default <T> ConsumerIOException<T> saveYamlLater(File file) {
        return this.saveYamlLater(file, null);
    }

    default <T> ConsumerIOException<T> saveYamlLater(File file, @Nullable Yaml yaml) {
        return (saveThis) -> this.saveYaml(file, saveThis, yaml);
    }
}
