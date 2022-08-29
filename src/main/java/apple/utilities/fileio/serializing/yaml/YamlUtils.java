package apple.utilities.fileio.serializing.yaml;

import apple.file.yml.BaseYcm;
import org.yaml.snakeyaml.Yaml;

public interface YamlUtils {

    default Yaml defaultYaml() {
        return new Yaml();
    }

    BaseYcm DEFAULT_YCM = new BaseYcm();

    default BaseYcm defaultYcm() {
        return DEFAULT_YCM;
    }
}
