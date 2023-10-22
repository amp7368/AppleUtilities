package apple.utilities.database.singleton;

import apple.utilities.database.HasFilename;
import apple.utilities.database.queue.AppleJsonDatabaseManager;
import apple.utilities.request.AppleRequestQueue;
import apple.utilities.request.settings.RequestSettingsBuilder;
import apple.utilities.request.settings.RequestSettingsBuilderVoid;
import com.google.gson.Gson;

import java.io.File;

@Deprecated
public class AppleJsonDatabaseSingleton<DBType extends HasFilename> implements AppleJsonDatabaseManager<DBType> {
    private final RequestSettingsBuilder<DBType> loadingSettings;
    private final File dbFolder;
    private final AppleRequestQueue ioService;
    private final RequestSettingsBuilderVoid savingSettings;
    private final Gson gson;

    public AppleJsonDatabaseSingleton(RequestSettingsBuilder<DBType> loadingSettings, File dbFolder, AppleRequestQueue ioService, RequestSettingsBuilderVoid savingSettings) {
        this.loadingSettings = loadingSettings;
        this.dbFolder = dbFolder;
        this.ioService = ioService;
        this.savingSettings = savingSettings;
        this.gson = AppleJsonDatabaseManager.super.getGson();
    }

    public AppleJsonDatabaseSingleton(File dbFolder, AppleRequestQueue ioService) {
        this.loadingSettings = RequestSettingsBuilder.empty();
        this.dbFolder = dbFolder;
        this.ioService = ioService;
        this.savingSettings = RequestSettingsBuilderVoid.VOID;
        this.gson = AppleJsonDatabaseManager.super.getGson();
    }

    public AppleJsonDatabaseSingleton(File dbFolder, AppleRequestQueue ioService, Gson gson) {
        this.loadingSettings = RequestSettingsBuilder.empty();
        this.dbFolder = dbFolder;
        this.ioService = ioService;
        this.savingSettings = RequestSettingsBuilderVoid.VOID;
        this.gson = gson;
    }

    public AppleJsonDatabaseSingleton(RequestSettingsBuilder<DBType> loadingSettings, File dbFolder, AppleRequestQueue ioService, RequestSettingsBuilderVoid savingSettings, Gson gson) {
        this.loadingSettings = loadingSettings;
        this.dbFolder = dbFolder;
        this.ioService = ioService;
        this.savingSettings = savingSettings;
        this.gson = gson;
    }

    @Override
    public RequestSettingsBuilder<DBType> getLoadingSettings() {
        return loadingSettings;
    }

    @Override
    public File getDBFolder() {
        return dbFolder;
    }

    @Override
    public AppleRequestQueue getIOService() {
        return ioService;
    }

    @Override
    public RequestSettingsBuilderVoid getSavingSettings() {
        return savingSettings;
    }

    @Override
    public Gson getGson() {
        return gson;
    }
}
