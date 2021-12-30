package apple.utilities.database.ajd;

import apple.utilities.database.SaveFileable;
import apple.utilities.request.AppleRequestQueue;
import apple.utilities.request.AppleRequestService;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collection;
import java.util.function.Consumer;

public class AppleAJDTypedImpl<DBType extends SaveFileable> implements AppleAJD {
    protected final Class<DBType> dbType;
    private final File dbFolder;
    private final AppleRequestQueue ioService;
    private Gson gson = null;

    public AppleAJDTypedImpl(File dbFolder, Class<DBType> dbType, AppleRequestQueue ioService) {
        this.dbFolder = dbFolder;
        this.dbType = dbType;
        this.ioService = ioService;
    }

    public AppleAJDTypedImpl(File dbFolder, Class<DBType> dbType, AppleRequestQueue ioService, Gson gson) {
        this.dbFolder = dbFolder;
        this.dbType = dbType;
        this.ioService = ioService;
        this.gson = gson;
    }

    public @NotNull Collection<DBType> loadAllNow() {
        return AppleAJD.super.loadAllNow(dbType);
    }

    public AppleRequestService.RequestHandler<DBType> load(String fileName, Consumer<DBType> runAfter) {
        return AppleAJD.super.load(dbType, fileName, runAfter);
    }

    public AppleRequestService.RequestHandler<DBType> load(File file, Consumer<DBType> runAfter) {
        return AppleAJD.super.load(dbType, file, runAfter);
    }

    public @Nullable DBType loadNow(String fileName) {
        return AppleAJD.super.loadNow(dbType, fileName);
    }

    public @Nullable DBType loadNow(File file) {
        return AppleAJD.super.loadNow(dbType, file);
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
    public Gson getGson() {
        return gson == null ? AppleAJD.super.getGson() : gson;
    }
}
