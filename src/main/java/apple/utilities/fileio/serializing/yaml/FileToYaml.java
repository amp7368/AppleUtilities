package apple.utilities.fileio.serializing.yaml;

import apple.file.yml.BaseYcm;
import apple.utilities.fileio.common.exception.SupplierIOException;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public interface FileToYaml extends YamlUtils {
    default <T> T loadYaml(File file, Class<T> type) throws IOException {
        return this.loadYaml(file, type, null);
    }

    default <T> T loadYaml(File file, Class<T> type, @Nullable BaseYcm ycm) throws IOException {
        if (ycm == null) ycm = defaultYcm();
        return ycm.load(file, type);
    }


    default <T> SupplierIOException<T> loadYamlLater(File file, Class<? extends T> type) {
        return loadYamlLater(file, type, null);
    }

    default <T> SupplierIOException<T> loadYamlLater(File file, Class<? extends T> type, @Nullable BaseYcm yaml) {
        return () -> this.loadYaml(file, type, yaml);
    }
}
