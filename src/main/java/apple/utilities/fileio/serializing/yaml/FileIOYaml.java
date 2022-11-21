package apple.utilities.fileio.serializing.yaml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.yaml.snakeyaml.Yaml;

public interface FileIOYaml {

    default Yaml defaultYaml() {
        return new Yaml();
    }

    default <T> T loadYaml(File file, Class<T> type, Yaml ycm) throws IOException {
        file.getParentFile().mkdirs();
        if (ycm == null)
            ycm = defaultYaml();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return ycm.loadAs(reader, type);
        }
    }

    default <T> void saveYaml(File file, T saveThis, Yaml yaml) throws IOException {
        file.getParentFile().mkdirs();
        if (yaml == null)
            yaml = defaultYaml();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(yaml.dumpAsMap(saveThis));
        }
    }
}
