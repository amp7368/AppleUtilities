package apple.utilities.database.keyed;

import apple.utilities.database.SaveFileable;
import apple.utilities.request.AppleJsonToFile;
import apple.utilities.request.AppleRequestService;
import apple.utilities.request.keyed.AppleRequestKeyQueue;
import apple.utilities.request.settings.RequestSettingsBuilderVoid;
import apple.utilities.util.FileFormatting;
import com.google.gson.Gson;

import java.io.File;

public interface AppleJsonDatabaseSaverKey<DBType extends SaveFileable> {
    Gson DEFAULT_GSON = new Gson();

    static File getDBFolder(Class<?> mainClass) {
        return FileFormatting.getDBFolder(mainClass);
    }

    default AppleRequestService.RequestHandler<?> save(Object key, DBType saving) {
        File dbFile = new File(getDBFolder(), saving.getSaveFile().getPath());
        dbFile.getParentFile().mkdirs();
        return getSavingService().queue(key, new AppleJsonToFile(dbFile, saving).withGson(getGson()), getSavingSettings());
    }

    default Gson getGson() {
        return DEFAULT_GSON;
    }

    File getDBFolder();

    AppleRequestKeyQueue<Boolean> getSavingService();

    RequestSettingsBuilderVoid getSavingSettings();
}
