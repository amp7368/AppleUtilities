package apple.utilities.request;

import apple.utilities.threading.util.supplier.SupplierUncaught;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
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
        try (InputStream stream = new URI(url).toURL().openStream()) {
            BufferedReader input = new BufferedReader(new InputStreamReader(stream));
            return gson.fromJson(input, outputType);
        } catch (MalformedURLException | URISyntaxException e) {
            String msg = "Cannot parse URL: '%s' - ".formatted(url);
            throw new MalformedURLException(msg + e.getMessage());
        } catch (IOException e) {
            String msg = "IOException with stream from URL: '%s'".formatted(url);
            throw new IOException(msg, e);
        } catch (JsonParseException e) {
            String msg = "Error parsing JSON in %s".formatted(url);
            throw new JsonParseException(msg, e);
        }
    }

}
