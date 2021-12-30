package apple.utilities.database.ajd;

import apple.utilities.request.*;
import apple.utilities.request.settings.RequestSettingsBuilder;
import apple.utilities.util.ExceptionUnpackaging;
import apple.utilities.util.FileFormatting;
import com.google.gson.Gson;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

public class AppleAJDInstImpl<DBType> implements FileFormatting {
    private static final Gson DEFAULT_GSON = new Gson();
    protected final Class<DBType> dbType;
    private final AppleRequestQueue ioService;
    private final File file;
    private Gson gson = null;
    private DBType thing;

    public AppleAJDInstImpl(File file, Class<DBType> dbType, AppleRequestQueue ioService) {
        this.file = file;
        this.dbType = dbType;
        this.ioService = ioService;
    }

    public AppleAJDInstImpl(File file, Class<DBType> dbType, AppleRequestQueue ioService, Gson gson) {
        this.file = file;
        this.dbType = dbType;
        this.ioService = ioService;
        this.gson = gson;
    }

    public AppleRequestService.RequestHandler<?> save() {
        file.getParentFile().mkdirs();
        return getIOService()
                .queueVoid(
                        new AppleJsonToFile(file, thing)
                                .withGson(getGson())
                );
    }

    private Gson getGson() {
        return gson == null ? DEFAULT_GSON : gson;
    }

    private AppleRequestQueue getIOService() {
        return ioService;
    }

    public AppleRequestService.RequestHandler<DBType> load(Consumer<DBType> parentRunAfter) {
        Consumer<DBType> runAfter = (thing) -> {
            this.thing = thing;
            parentRunAfter.accept(thing);
        };
        RequestSettingsBuilder<DBType> settings = RequestSettingsBuilder.empty();
        settings.addExceptionHandler((e) -> {
            if (!ExceptionUnpackaging.exists(e, FileNotFoundException.class)) {
                ExceptionHandler.throwE(e);
            }
        }, Integer.MAX_VALUE);
        return getIOService().queue(new AppleJsonFromFile<>(file, dbType).withGson(getGson()), runAfter, settings);
    }

    public @Nullable DBType loadNow() {
        return load((c) -> {
        }).complete();
    }

    public DBType loadNowOrMake() {
        thing = loadNow();
        if (thing != null) return thing;
        try {
            thing = dbType.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        save();
        return thing;
    }
}
