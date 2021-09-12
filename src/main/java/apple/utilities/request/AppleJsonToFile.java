package apple.utilities.request;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AppleJsonToFile implements AppleRequestVoid {
    private final File fileToSaveTo;
    private final Object saveThis;
    private Gson gson = new Gson();

    public AppleJsonToFile(File fileToSaveTo, Object saveThis) {
        this.fileToSaveTo = fileToSaveTo;
        this.saveThis = saveThis;
    }

    public AppleJsonToFile withGson(Gson gson) {
        this.gson = gson;
        return this;
    }

    @Override
    public void complete() throws AppleRequest.AppleRequestException {
        fileToSaveTo.getParentFile().mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSaveTo))) {
            gson.toJson(saveThis, writer);
        } catch (IOException | JsonIOException | JsonSyntaxException e) {
            throw new AppleRequest.AppleRequestException(fileToSaveTo.getAbsolutePath() + " had an IOException", e);
        }
    }
}
