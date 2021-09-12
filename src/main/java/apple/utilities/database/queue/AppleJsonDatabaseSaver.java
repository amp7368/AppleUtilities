package apple.utilities.database.queue;

import apple.utilities.database.SaveFileable;
import apple.utilities.request.AppleJsonToFile;
import apple.utilities.request.AppleRequestQueue;
import apple.utilities.request.AppleRequestService;
import apple.utilities.request.settings.RequestSettingsBuilderVoid;
import apple.utilities.util.FileFormatting;
import com.google.gson.Gson;

import java.io.File;

public interface AppleJsonDatabaseSaver<DBType extends SaveFileable> {
    Gson DEFAULT_GSON = new Gson();

    static File getDBFolder(Class<?> mainClass) {
        return FileFormatting.getDBFolder(mainClass);
    }

    default AppleRequestService.RequestHandler<?> save(DBType saving) {
        File dbFile = new File(getDBFolder(), saving.getSaveFile().getPath());
        dbFile.getParentFile().mkdirs();
        return getIOService().queueVoid(new AppleJsonToFile(dbFile, saving).withGson(getGson()), getSavingSettings());
    }

    default Gson getGson() {
        return DEFAULT_GSON;
    }

    File getDBFolder();

    AppleRequestQueue getIOService();

    RequestSettingsBuilderVoid getSavingSettings();
}
