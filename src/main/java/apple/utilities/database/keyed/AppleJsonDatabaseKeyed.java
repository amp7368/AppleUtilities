package apple.utilities.database.keyed;

import apple.utilities.database.SaveFileableKeyed;
import apple.utilities.request.AppleRequestQueue;
import apple.utilities.request.keyed.AppleRequestKeyQueue;
import apple.utilities.request.keyed.AppleRequestOnConflict;
import apple.utilities.request.settings.RequestSettingsBuilder;
import apple.utilities.request.settings.RequestSettingsBuilderVoid;
import com.google.gson.Gson;

import java.io.File;

public class AppleJsonDatabaseKeyed<DBType extends SaveFileableKeyed> implements AppleJsonDatabaseManagerKeyed<DBType> {
    private final Class<DBType> dbClass;
    private final File dbFolder;
    private final AppleRequestQueue loadingService;
    private final AppleRequestKeyQueue<Boolean> savingService;
    private final Gson gson;
    private final RequestSettingsBuilderVoid savingSettings;
    private final RequestSettingsBuilder<DBType> loadingSettings;
    private final AppleRequestOnConflict<Boolean> onConflict;

    public AppleJsonDatabaseKeyed(Class<DBType> dbClass,
                                  File dbFolder,
                                  AppleRequestQueue loadingService,
                                  AppleRequestKeyQueue<Boolean> savingService,
                                  Gson gson,
                                  RequestSettingsBuilderVoid savingSettings,
                                  RequestSettingsBuilder<DBType> loadingSettings,
                                  AppleRequestOnConflict<Boolean> onConflict) {
        this.dbClass = dbClass;
        this.dbFolder = dbFolder;
        this.loadingService = loadingService;
        this.savingService = savingService;
        this.gson = gson;
        this.savingSettings = savingSettings;
        this.loadingSettings = loadingSettings;
        this.onConflict = onConflict;
    }

    @Override
    public Class<DBType> getDbClass() {
        return dbClass;
    }

    @Override
    public File getDBFolder() {
        return dbFolder;
    }

    @Override
    public AppleRequestQueue getLoadingService() {
        return loadingService;
    }

    @Override
    public AppleRequestKeyQueue<Boolean> getSavingService() {
        return savingService;
    }

    @Override
    public Gson getGson() {
        return gson == null ? AppleJsonDatabaseManagerKeyed.super.getGson() : gson;
    }

    @Override
    public RequestSettingsBuilderVoid getSavingSettings() {
        return savingSettings == null ? AppleJsonDatabaseManagerKeyed.super.getSavingSettings() : savingSettings;
    }

    @Override
    public RequestSettingsBuilder<DBType> getLoadingSettings() {
        return loadingSettings == null ? AppleJsonDatabaseManagerKeyed.super.getLoadingSettings() : loadingSettings;
    }

    @Override
    public AppleRequestOnConflict<Boolean> getDefaultOnConflict() {
        return onConflict;
    }
}
