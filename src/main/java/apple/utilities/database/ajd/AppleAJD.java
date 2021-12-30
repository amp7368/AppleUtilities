package apple.utilities.database.ajd;

import apple.utilities.database.SaveFileable;
import apple.utilities.request.*;
import apple.utilities.request.settings.RequestSettingsBuilder;
import apple.utilities.util.ExceptionUnpackaging;
import apple.utilities.util.FileFormatting;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

public interface AppleAJD extends FileFormatting {
    Gson DEFAULT_GSON = new Gson();

    default <DBType extends SaveFileable> AppleRequestService.RequestHandler<?> save(DBType saving) {
        File dbFile = new File(getDBFolder(), saving.getSaveFile().getPath());
        dbFile.getParentFile().mkdirs();
        return getIOService()
                .queueVoid(
                        new AppleJsonToFile(dbFile, saving)
                                .withGson(getGson())
                );
    }

    @NotNull
    default <DBType> Collection<DBType> loadAllNow(Class<DBType> dbType) {
        File folder = getDBFolder();
        File[] files;
        if (folder.isDirectory())
            files = folder.listFiles();
        else
            files = new File[0];
        Collection<DBType> dbs = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                dbs.add(loadNow(dbType, file));
            }
        }
        return dbs;
    }

    File getDBFolder();

    default <DBType> AppleRequestService.RequestHandler<DBType> load(Class<DBType> dbType, String fileName, Consumer<DBType> runAfter) {
        return load(dbType, new File(getDBFolder(), fileName), runAfter);
    }

    default <DBType> AppleRequestService.RequestHandler<DBType> load(Class<DBType> dbType, File file, Consumer<DBType> runAfter) {
        RequestSettingsBuilder<DBType> settings = RequestSettingsBuilder.empty();
        settings.addExceptionHandler((e) -> {
            if (!ExceptionUnpackaging.exists(e, FileNotFoundException.class)) {
                ExceptionHandler.throwE(e);
            }
        }, Integer.MAX_VALUE);
        return getIOService().queue(new AppleJsonFromFile<>(file, dbType).withGson(getGson()), runAfter, settings);
    }

    @Nullable
    default <DBType> DBType loadNow(Class<DBType> dbType, String fileName) {
        return loadNow(dbType, new File(getDBFolder(), fileName));
    }

    @Nullable
    default <DBType> DBType loadNow(Class<DBType> dbType, File file) {
        RequestSettingsBuilder<DBType> settings = RequestSettingsBuilder.empty();
        settings.addExceptionHandler((e) -> {
            if (!ExceptionUnpackaging.exists(e, FileNotFoundException.class)) {
                ExceptionHandler.throwE(e);
            }
        }, Integer.MAX_VALUE);

        return getIOService().queue(new AppleJsonFromFile<>(file, dbType).withGson(getGson()), (f) -> {
        }, settings).complete();
    }

    default Gson getGson() {
        return DEFAULT_GSON;
    }

    AppleRequestQueue getIOService();

}
