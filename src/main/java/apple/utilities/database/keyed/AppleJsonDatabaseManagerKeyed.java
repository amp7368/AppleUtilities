package apple.utilities.database.keyed;

import apple.utilities.database.SaveFileableKeyed;
import apple.utilities.request.AppleRequestQueue;
import apple.utilities.request.keyed.AppleRequestKeyQueue;
import apple.utilities.request.settings.RequestSettingsBuilder;
import apple.utilities.request.settings.RequestSettingsBuilderVoid;
import com.google.gson.Gson;

import java.io.File;

@Deprecated
public interface AppleJsonDatabaseManagerKeyed<DBType extends SaveFileableKeyed> extends AppleJsonDatabaseSaverKeyed<DBType>, AppleJsonDatabaseLoaderKeyed<DBType> {
    @Override
    Class<DBType> getDbClass();

    @Override
    File getDBFolder();

    @Override
    AppleRequestQueue getLoadingService();

    @Override
    AppleRequestKeyQueue<Boolean> getSavingService();

    @Override
    default Gson getGson() {
        return AppleJsonDatabaseSaverKeyed.super.getGson();
    }

    @Override
    default RequestSettingsBuilderVoid getSavingSettings() {
        return RequestSettingsBuilderVoid.empty();
    }

    @Override
    default RequestSettingsBuilder<DBType> getLoadingSettings() {
        return RequestSettingsBuilder.empty();
    }
}
