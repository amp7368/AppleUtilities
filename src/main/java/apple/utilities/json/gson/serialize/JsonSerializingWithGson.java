package apple.utilities.json.gson.serialize;

import com.google.gson.*;

import java.lang.reflect.Type;

public interface JsonSerializingWithGson<Typee> {
    Typee deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context, Gson gson) throws JsonParseException;

    JsonElement serialize(Typee src, Type typeOfSrc, JsonSerializationContext context, Gson gson);
}
