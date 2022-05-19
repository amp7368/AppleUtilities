package apple.utilities.fileio.serializing.yaml;

import apple.file.yml.BaseYcm;
import org.yaml.snakeyaml.Yaml;

public interface YamlUtils {
    Yaml DEFAULT_YAML = new Yaml();

    default Yaml defaultYaml() {
        return DEFAULT_YAML;
    }

    BaseYcm DEFAULT_YCM = new BaseYcm();

    default BaseYcm defaultYcm() {
        return DEFAULT_YCM;
    }
}
