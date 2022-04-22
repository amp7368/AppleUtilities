package apple.utilities.database;

import apple.utilities.util.FileFormatting;

import java.io.File;

public interface SaveFileable extends FileFormatting {
    String getSaveFileName();

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

    default File fileWithParent(File parentFile) {
        return this.fileWithChildrenI(parentFile, getSaveFilePath());
    }

    default String[] getSaveFilePath() {
        return new String[]{getSaveFileName()};
    }
}
