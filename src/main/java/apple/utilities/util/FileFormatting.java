package apple.utilities.util;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FileFormatting {
    public static File getDBFolder(Class<?> mainClass) {
        List<String> list = Arrays.asList(mainClass.getProtectionDomain().getCodeSource().getLocation().getPath().split("/"));
        return new File(String.join("/", list.subList(0, list.size() - 1)));
    }

    public static File fileWithChildren(File file, String... children) {
        for (String child : children) {
            file = new File(file, child);
        }
        return file;
    }

    public static String extension(String file, String fileExtension) {
        return file + "." + fileExtension;
    }

    public static String extensionJson(String file) {
        return extension(file, "json");
    }

    public static String extensionYml(String file) {
        return extension(file, "yml");
    }

    public static String extensionDb(String file) {
        return extension(file, "db");
    }
}
