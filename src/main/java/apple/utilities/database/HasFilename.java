package apple.utilities.database;

import apple.utilities.util.ArrayUtils;
import apple.utilities.util.FileFormatting;
import java.io.File;
import java.util.Collections;
import java.util.List;

public interface HasFilename {

    String getSaveFileName();

    default List<String> getSubFolder() {
        return Collections.emptyList();
    }

    default File fileWithParent(File rootFolder) {
        String[] subFolder = getSubFolder().toArray(String[]::new);
        File parent = FileFormatting.fileWithChildren(rootFolder, subFolder);
        return new File(parent, getSaveFileName());
    }

    default String[] getSaveFilePath() {
        return ArrayUtils.combine(getSubFolder(), getSaveFileName())
            .toArray(String[]::new);
    }

}
