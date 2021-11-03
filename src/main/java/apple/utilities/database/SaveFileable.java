package apple.utilities.database;

import apple.utilities.util.FileFormatting;

import java.io.File;

public interface SaveFileable {
    String getSaveFileName();

    default File getSaveFile() {
        return new File(getSaveFileName());
    }

    default String extension(String file, String extension) {
        return FileFormatting.extension(file, extension);
    }

    default String extensionJson(String file) {
        return FileFormatting.extensionJson(file);
    }

    default String extensionYml(String file) {
        return FileFormatting.extensionYml(file);
    }

    default String extensionDb(String file) {
        return FileFormatting.extensionDb(file);
    }
}
