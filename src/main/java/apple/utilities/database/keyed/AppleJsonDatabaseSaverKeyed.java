package apple.utilities.database.keyed;

import apple.utilities.database.SaveFileableKeyed;
import apple.utilities.request.AppleJsonToFile;
import apple.utilities.request.AppleRequestService;
import apple.utilities.request.keyed.AppleRequestKeyQueue;
import apple.utilities.request.keyed.AppleRequestOnConflict;
import apple.utilities.request.settings.RequestSettingsBuilderVoid;
import com.google.gson.Gson;

import java.io.File;

public interface AppleJsonDatabaseSaverKeyed<DBType extends SaveFileableKeyed> {
    Gson DEFAULT_GSON = new Gson();

    default AppleRequestService.RequestHandler<?> save(DBType saving) {
        return save(saving, getDefaultOnConflict());
    }

    default AppleRequestService.RequestHandler<?> delete(DBType saving) {
        return delete(saving, getDefaultOnConflict());
    }

    default AppleRequestService.RequestHandler<?> delete(DBType saving, AppleRequestOnConflict<Boolean> onConflict) {
        File dbFile = new File(getDBFolder(), saving.getSaveFile().getPath());
        return getSavingService()
                .queue(saving.getSaveId(),
                        dbFile::delete,
                        getSavingSettings(),
                        onConflict
                );
    }

    default AppleRequestService.RequestHandler<?> save(DBType saving, AppleRequestOnConflict<Boolean> onConflict) {
        File dbFile = new File(getDBFolder(), saving.getSaveFile().getPath());
        return getSavingService()
                .queue(saving.getSaveId(),
                        new AppleJsonToFile(dbFile, saving)
                                .withGson(getGson()),
                        getSavingSettings(),
                        onConflict
                );
    }

    default Gson getGson() {
        return DEFAULT_GSON;
    }

    File getDBFolder();

    AppleRequestKeyQueue<Boolean> getSavingService();

    RequestSettingsBuilderVoid getSavingSettings();

    AppleRequestOnConflict<Boolean> getDefaultOnConflict();
}
