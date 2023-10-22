package apple.utilities.database;

import apple.utilities.util.FileFormatting;
import java.io.File;

public interface HasFilename {

    String getSaveFileName();

    default File fileWithParent(File parentFile) {
        return FileFormatting.fileWithChildren(parentFile, getSaveFilePath());
    }

    default String[] getSaveFilePath() {
        return new String[]{getSaveFileName()};
    }

}
