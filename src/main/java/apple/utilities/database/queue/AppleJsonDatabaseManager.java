package apple.utilities.database.queue;

import apple.utilities.database.SaveFileable;
import apple.utilities.util.FileFormatting;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@Deprecated
public interface AppleJsonDatabaseManager<DBType extends SaveFileable> extends AppleJsonDatabaseSaver<DBType>, AppleJsonDatabaseLoader<DBType> {
    @NotNull
    static File getDBFolder(Class<?> mainClass) {
        return FileFormatting.getDBFolder(mainClass);
    }

    @Override
    default Gson getGson() {
        return AppleJsonDatabaseSaver.super.getGson();
    }
}
