package apple.utilities.database;

import apple.utilities.util.FileFormatting;

public interface SaveFileable extends HasFilename, FileFormatting {

    default String extension(String file, String extension) {
        return this.extensionI(file, extension);
    }

    default String extensionJson(String file) {
        return this.extensionJsonI(file);
    }

    default String extensionYml(String file) {
        return this.extensionYmlI(file);
    }

    default String extensionDb(String file) {
        return this.extensionDbI(file);
    }
}
