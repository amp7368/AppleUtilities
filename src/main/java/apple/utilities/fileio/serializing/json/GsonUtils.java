package apple.utilities.fileio.serializing.json;

import com.google.gson.Gson;

public interface GsonUtils {
    Gson DEFAULT_GSON = new Gson();

    default Gson defaultGson() {
        return DEFAULT_GSON;
    }
}
