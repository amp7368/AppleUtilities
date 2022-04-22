package apple.utilities.fileio.serializing.yaml;

import org.yaml.snakeyaml.Yaml;

public interface YamlUtils {
    Yaml DEFAULT_YAML = new Yaml();

    default Yaml defaultYaml() {
        return DEFAULT_YAML;
    }
}
