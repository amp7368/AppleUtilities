package apple.utilities.request;

import apple.utilities.threading.util.supplier.SupplierUncaught;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class AppleJsonFromURL<Out> implements SupplierUncaught<Out> {

    private final String url;
    private final Type outputType;
    private final Gson gson;

    public AppleJsonFromURL(String url, Type outputType) {
        this(url, outputType, null);
    }

    public AppleJsonFromURL(String url, Class<Out> outputType) {
        this(url, outputType, null);
    }

    public AppleJsonFromURL(String url, Class<Out> outputType, Gson gson) {
        this(url, TypeToken.get(outputType).getType(), gson);
    }

    public AppleJsonFromURL(String url, Type outputType, Gson gson) {
        this.url = url.replace(" ", "%20");
        this.outputType = outputType;
        this.gson = Objects.requireNonNullElseGet(gson, Gson::new);
    }

    @Override
    public Out get() throws Exception {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
            return gson.fromJson(input, outputType);
        } catch (MalformedURLException e) {
            throw new MalformedURLException(getUrlErrorMessage() + e.getMessage());
        } catch (IOException e) {
            throw new IOException(getUrlErrorMessage(), e);
        }
    }

    private String getUrlErrorMessage() {
        return String.format("Error with url='%s' - ", url);
    }
}
