package apple.utilities.database.ajd;

import apple.utilities.request.AppleRequestQueue;
import com.google.gson.Gson;

import java.io.File;

public class AppleAJDImpl implements AppleAJD {
    private final File dbFolder;
    private final AppleRequestQueue ioService;
    private Gson gson = null;

    public AppleAJDImpl(File dbFolder, AppleRequestQueue ioService) {
        this.dbFolder = dbFolder;
        this.ioService = ioService;
    }

    public AppleAJDImpl(File dbFolder, AppleRequestQueue ioService, Gson gson) {
        this.dbFolder = dbFolder;
        this.ioService = ioService;
        this.gson = gson;
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
