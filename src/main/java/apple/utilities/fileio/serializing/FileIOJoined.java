package apple.utilities.fileio.serializing;

import apple.utilities.fileio.serializing.json.JsonFileIO;
import apple.utilities.fileio.serializing.yaml.YamlFileIO;

public interface FileIOJoined extends YamlFileIO, JsonFileIO {
}
