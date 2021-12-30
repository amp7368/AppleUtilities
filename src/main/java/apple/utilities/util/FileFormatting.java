package apple.utilities.util;

import java.io.File;
import java.nio.file.Paths;

public interface FileFormatting {
    String JSON_EXTENSION = "json";
    String YML_EXTENSION = "yml";
    String DB_EXTENSION = "db";

    static File getDBFolder(Class<?> mainClass) {
        return Paths.get("").toAbsolutePath().toFile();
//        List<String> list = Arrays.asList(mainClass.getProtectionDomain().getCodeSource().getLocation().getPath().split("/"));
//        return new File(String.join("/", list.subList(0, list.size() - 1)));
    }

    static File fileWithChildren(File file, String... children) {
        for (String child : children) {
            file = new File(file, child);
        }
        return file;
    }

    static String extension(String file, String fileExtension) {
        return file + '.' + fileExtension;
    }

    static String extensionJson(String file) {
        return extension(file, JSON_EXTENSION);
    }

    static String extensionYml(String file) {
        return extension(file, YML_EXTENSION);
    }

    static String extensionDb(String file) {
        return extension(file, DB_EXTENSION);
    }

    default File getDBFolderI(Class<?> mainClass) {
        return getDBFolder(mainClass);
    }

    default File fileWithChildrenI(File file, String... children) {
        return fileWithChildren(file, children);
    }

    default String extensionI(String file, String fileExtension) {
        return extension(file, fileExtension);
    }

    default String extensionJsonI(String file) {
        return extensionJson(file);
    }

    default String extensionYmlI(String file) {
        return extensionYml(file);
    }

    default String extensionDbI(String file) {
        return extensionDb(file);
    }
}
