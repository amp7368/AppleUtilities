package apple.utilities.json.gson.serialize;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import java.lang.reflect.Type;

@Deprecated
public interface JsonSerializingWithGson<Typee> {

    Typee deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context, Gson gson)
        throws JsonParseException;

    JsonElement serialize(Typee src, Type typeOfSrc, JsonSerializationContext context, Gson gson);
}
