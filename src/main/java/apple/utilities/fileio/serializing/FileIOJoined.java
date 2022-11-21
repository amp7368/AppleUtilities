package apple.utilities.fileio.serializing;

import apple.utilities.fileio.serializing.json.FileIOJson;
import apple.utilities.fileio.serializing.yaml.FileIOYaml;
import apple.utilities.fileio.serializing.yaml.FileIOYcm;

public interface FileIOJoined extends FileIOYaml, FileIOJson, FileIOYcm {

    FileIOJoined instance = new FileIOJoined() {
    };

    static FileIOJoined get() {
        return instance;
    }
}
