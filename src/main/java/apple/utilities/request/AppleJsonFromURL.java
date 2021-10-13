package apple.utilities.request;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class AppleJsonFromURL<Out> implements AppleRequest<Out> {
    private final String url;
    private final Class<Out> outputType;
    private Gson gson = new Gson();

    public AppleJsonFromURL(String url, Class<Out> outputType) {
        this.url = url.replace(" ", "%20");
        this.outputType = outputType;
    }

    public AppleJsonFromURL<Out> withGson(Gson gson) {
        this.gson = gson;
        return this;
    }

    @Override
    public Out get() throws AppleRequestException {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
            return gson.fromJson(input, outputType);
        } catch (MalformedURLException e) {
            throw new AppleRequestException("url is not valid", e);
        } catch (IOException | JsonIOException | JsonSyntaxException e) {
            throw new AppleRequestException(url + " had an IOException", e);
        }
    }
}
