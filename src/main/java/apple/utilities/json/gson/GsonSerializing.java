package apple.utilities.json.gson;

import apple.utilities.json.gson.serialize.JsonSerializing;
import apple.utilities.json.gson.serialize.JsonSerializingWithGson;
import apple.utilities.structures.choiceable.ChoiceableFunction;
import com.google.gson.*;

import java.lang.reflect.Type;

public class GsonSerializing<Typee> implements JsonSerializing<Typee> {
    private final JsonSerializingWithGson<Typee> serializer;
    private final ChoiceableFunction<GsonSerializing<Typee>, Gson> getGsonFinalized;

    public GsonSerializing(JsonSerializingWithGson<Typee> serializing, ChoiceableFunction<GsonSerializing<Typee>, Gson> gson) {
        this.serializer = serializing;
        this.getGsonFinalized = gson;
    }

    @Override
    public Typee deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        return serializer.deserialize(jsonElement, type, context, getChoiceAndSave());
    }

    @Override
    public JsonElement serialize(Typee object, Type type, JsonSerializationContext context) {
        return serializer.serialize(object, type, context, getChoiceAndSave());
    }

    public Gson getChoiceAndSave() {
        return getGsonFinalized.getChoiceAndSave(this);
    }
}
