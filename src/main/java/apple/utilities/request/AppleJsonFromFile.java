package apple.utilities.request;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;

public class AppleJsonFromFile<Out> implements AppleRequest<Out> {
    private final File fileToSaveTo;
    private final Class<Out> getThis;
    private final Type getThisType;
    private Gson gson = new Gson();

    public AppleJsonFromFile(File fileToSaveTo, Class<Out> getThis) {
        this.fileToSaveTo = fileToSaveTo;
        this.getThis = getThis;
        this.getThisType = null;
    }

    public AppleJsonFromFile(File fileToSaveTo, TypeToken<Out> getThis) {
        this.fileToSaveTo = fileToSaveTo;
        this.getThisType = getThis.getType();
        this.getThis = null;
    }

    public AppleJsonFromFile<Out> withGson(Gson gson) {
        this.gson = gson;
        return this;
    }

    @Override
    public Out get() throws AppleRequestException {
        try (FileReader reader = new FileReader(fileToSaveTo)) {
            return gson.fromJson(reader, getThis == null ? getThisType : getThis);
        } catch (IOException | JsonIOException | JsonSyntaxException e) {
            throw new AppleRequest.AppleRequestException(fileToSaveTo.getAbsolutePath() + " had an IOException", e);
        }
    }
}
