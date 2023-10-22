package apple.utilities.database.keyed;

import apple.utilities.database.SaveFileableKeyed;
import apple.utilities.request.AppleRequestQueue;
import apple.utilities.request.keyed.AppleRequestKeyQueue;
import apple.utilities.request.keyed.AppleRequestOnConflict;
import apple.utilities.request.settings.RequestSettingsBuilder;
import apple.utilities.request.settings.RequestSettingsBuilderVoid;
import com.google.gson.Gson;
import java.io.File;

@Deprecated
public class AppleJsonDatabaseKeyedBuilder<DBType extends SaveFileableKeyed> {

    private Class<DBType> dbClass;
    private File dbFolder;
    private AppleRequestQueue loadingService;
    private AppleRequestKeyQueue<Boolean> savingService;
    private Gson gson;
    private RequestSettingsBuilderVoid savingSettings;
    private RequestSettingsBuilder<DBType> loadingSettings;
    private AppleRequestOnConflict<Boolean> onConflict;

    public AppleJsonDatabaseKeyedBuilder(Class<DBType> dbClass,
        File dbFolder,
        AppleRequestQueue loadingService,
        AppleRequestKeyQueue<Boolean> savingService,
        AppleRequestOnConflict<Boolean> onConflict) {
        this.dbClass = dbClass;
        this.dbFolder = dbFolder;
        this.loadingService = loadingService;
        this.savingService = savingService;
        this.onConflict = onConflict;
    }

    public AppleJsonDatabaseKeyedBuilder() {
    }

    public static <DBType extends SaveFileableKeyed> AppleJsonDatabaseKeyedBuilder<DBType> empty() {
        return new AppleJsonDatabaseKeyedBuilder<>();
    }

    public static <DBType extends SaveFileableKeyed>
    AppleJsonDatabaseKeyedBuilder<DBType> required(Class<DBType> dbClass,
        File dbFolder,
        AppleRequestQueue loadingService,
        AppleRequestKeyQueue<Boolean> savingService,
        AppleRequestOnConflict<Boolean> onConflict) {
        return new AppleJsonDatabaseKeyedBuilder<>(dbClass, dbFolder, loadingService, savingService, onConflict);
    }

    public AppleJsonDatabaseKeyedBuilder<DBType> withRequired(Class<DBType> dbClass,
        File dbFolder,
        AppleRequestQueue loadingService,
        AppleRequestKeyQueue<Boolean> savingService) {
        this.dbClass = dbClass;
        this.dbFolder = dbFolder;
        this.loadingService = loadingService;
        this.savingService = savingService;
        return this;
    }

    public AppleJsonDatabaseKeyedBuilder<DBType> withDbClass(Class<DBType> dbClass) {
        this.dbClass = dbClass;
        return this;
    }


    public AppleJsonDatabaseKeyedBuilder<DBType> withDbFolder(File dbFolder) {
        this.dbFolder = dbFolder;
        return this;
    }

    public AppleJsonDatabaseKeyedBuilder<DBType> withLoadingService(AppleRequestQueue loadingService) {
        this.loadingService = loadingService;
        return this;
    }

    public AppleJsonDatabaseKeyedBuilder<DBType> withSavingService(AppleRequestKeyQueue<Boolean> savingService) {
        this.savingService = savingService;
        return this;
    }

    public AppleJsonDatabaseKeyedBuilder<DBType> withGson(Gson gson) {
        this.gson = gson;
        return this;
    }

    public AppleJsonDatabaseKeyedBuilder<DBType> withSavingSettings(RequestSettingsBuilderVoid savingSettings) {
        this.savingSettings = savingSettings;
        return this;
    }

    public AppleJsonDatabaseKeyedBuilder<DBType> withLoadingSettings(RequestSettingsBuilder<DBType> loadingSettings) {
        this.loadingSettings = loadingSettings;
        return this;
    }

    public AppleJsonDatabaseKeyedBuilder<DBType> withOnConflict(AppleRequestOnConflict<Boolean> onConflict) {
        this.onConflict = onConflict;
        return this;
    }

    public AppleJsonDatabaseKeyed<DBType> create() {
        if (dbClass == null || dbFolder == null || loadingService == null || savingService == null)
            throw new NullPointerException(
                "Database requires [dbClass || dbFolder || loadingService || savingService] to not be null");
        return new AppleJsonDatabaseKeyed<>(dbClass, dbFolder, loadingService, savingService, gson, savingSettings, loadingSettings,
            onConflict);
    }
}