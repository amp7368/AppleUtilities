package apple.utilities.fileio.serializing.yaml;

import apple.file.yml.BaseYcm;
import java.io.File;
import java.io.IOException;

public interface FileIOYcm {

    default BaseYcm defaultYcm() {
        return new BaseYcm() {
        };
    }

    default <T> T loadYcm(File file, Class<T> type, BaseYcm ycm) throws IOException {
        file.getParentFile().mkdirs();
        if (ycm == null)
            ycm = defaultYcm();
        return ycm.load(file, type);
    }

    default <T> void saveYcm(File file, T saveThis, BaseYcm ycm) throws IOException {
        file.getParentFile().mkdirs();
        if (ycm == null)
            ycm = defaultYcm();
        ycm.save(file, saveThis);
    }
}
