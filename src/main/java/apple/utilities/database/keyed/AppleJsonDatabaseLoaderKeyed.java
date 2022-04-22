package apple.utilities.database.keyed;

import apple.utilities.request.AppleJsonFromFile;
import apple.utilities.request.AppleRequestQueue;
import apple.utilities.request.AppleRequestService;
import apple.utilities.request.ExceptionHandler;
import apple.utilities.request.settings.RequestSettingsBuilder;
import apple.utilities.util.ExceptionUnpackaging;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

@Deprecated
public interface AppleJsonDatabaseLoaderKeyed<DBType> {
    Gson DEFAULT_GSON = new Gson();

    Class<DBType> getDbClass();

    @NotNull
    default Collection<DBType> loadAllNow() {
        File folder = getDBFolder();
        File[] files;
        if (folder.isDirectory())
            files = folder.listFiles();
        else
            files = new File[0];
        Collection<DBType> dbs = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                dbs.add(loadNow(file));
            }
        }
        return dbs;
    }

    @Nullable
    default DBType loadNow(String fileName) {
        return loadNow(new File(getDBFolder(), fileName));
    }

    @Nullable
    default DBType loadNow(File file) {
        RequestSettingsBuilder<DBType> settings = getLoadingSettings();
        settings.addExceptionHandler((e) -> {
            if (!ExceptionUnpackaging.exists(e, FileNotFoundException.class)) {
                ExceptionHandler.throwE(e);
            }
        }, Integer.MAX_VALUE);

        return getLoadingService().queue(new AppleJsonFromFile<>(file, getDbClass()).withGson(getGson()), (f) -> {
        }, settings).complete();
    }

    default AppleRequestService.RequestHandler<DBType> load(String fileName, @Nullable Consumer<DBType> runAfter) {
        return load(new File(getDBFolder(), fileName), runAfter);
    }

    default void loadAll() {
        File folder = getDBFolder();
        File[] files;
        if (folder.isDirectory())
            files = folder.listFiles();
        else
            files = new File[0];
        if (files != null) {
            for (File file : files) {
                load(file, null);
            }
        }
    }

    default AppleRequestService.RequestHandler<DBType> load(File file, @Nullable Consumer<DBType> runAfter) {
        RequestSettingsBuilder<DBType> settings = getLoadingSettings();
        settings.addExceptionHandler((e) -> {
            if (!ExceptionUnpackaging.exists(e, FileNotFoundException.class)) {
                ExceptionHandler.throwE(e);
            }
        }, Integer.MAX_VALUE);
        return getLoadingService().queue(new AppleJsonFromFile<>(file, getDbClass()).withGson(getGson()), runAfter, settings);
    }

    default Gson getGson() {
        return DEFAULT_GSON;
    }

    File getDBFolder();

    AppleRequestQueue getLoadingService();

    RequestSettingsBuilder<DBType> getLoadingSettings();
}
