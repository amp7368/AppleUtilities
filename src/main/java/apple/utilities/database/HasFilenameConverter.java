package apple.utilities.database;

import apple.utilities.util.FileFormatting;
import java.io.File;
import java.util.List;
import java.util.function.Function;

public interface HasFilenameConverter<T> {

    static <T extends HasFilename> HasFilenameConverter<T> createConverter() {
        return createConverter(T::getSaveFileName, T::getSubFolder);
    }

    static <T> HasFilenameConverter<T> createConverter(
        Function<T, String> saveFileNameFn,
        Function<T, List<String>> subFolderFn
    ) {
        return new HasFilenameConverter<>() {
            @Override
            public String getSaveFileName(T obj) {
                return saveFileNameFn.apply(obj);
            }

            @Override
            public List<String> getSubFolder(T obj) {
                return subFolderFn.apply(obj);
            }
        };
    }

    String getSaveFileName(T obj);

    List<String> getSubFolder(T obj);

    default File fileWithParent(T obj, File rootFolder) {
        String[] subFolder = getSubFolder(obj).toArray(String[]::new);
        File parent = FileFormatting.fileWithChildren(rootFolder, subFolder);
        return new File(parent, getSaveFileName(obj));
    }
}
