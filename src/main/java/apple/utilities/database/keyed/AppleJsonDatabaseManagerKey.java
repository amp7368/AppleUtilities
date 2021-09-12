package apple.utilities.database.keyed;

import apple.utilities.database.SaveFileable;
import apple.utilities.util.FileFormatting;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public interface AppleJsonDatabaseManagerKey<DBType extends SaveFileable> extends AppleJsonDatabaseSaverKey<DBType>, AppleJsonDatabaseLoaderKey<DBType> {
    @NotNull
    static File getDBFolder(Class<?> mainClass) {
        return FileFormatting.getDBFolder(mainClass);
    }

    @Override
    default Gson getGson() {
        return AppleJsonDatabaseSaverKey.super.getGson();
    }
}
