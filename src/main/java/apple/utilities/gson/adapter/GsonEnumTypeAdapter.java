package apple.utilities.gson.adapter;

import apple.utilities.json.gson.GsonBuilderDynamic;
import apple.utilities.json.gson.serialize.JsonSerializing;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class GsonEnumTypeAdapter<Super> implements JsonSerializing<Super> {

    private static final String TYPE_ID = "typeId";
    private final Map<String, GsonEnumTypeHolder<Super>> typesById;
    private final Map<Class<? extends Super>, GsonEnumTypeHolder<Super>> typesByClass;
    private final GsonEnumTypeHolder<Super>[] types;
    private final GsonBuilderDynamic originalGson;
    private Gson gsonWithoutThis = null;

    private GsonEnumTypeAdapter(GsonEnumTypeHolder<Super>[] types, GsonBuilderDynamic gson) {
        this.originalGson = gson;
        this.typesByClass = new HashMap<>();
        this.typesById = new HashMap<>();
        this.types = types;
        for (GsonEnumTypeHolder<Super> type : types) {
            this.typesByClass.put(type.getTypeClass(), type);
            this.typesById.put(type.getTypeId(), type);
        }
    }

    public static <Super> GsonBuilderDynamic register(GsonEnumTypeHolder<Super>[] types,
        GsonBuilderDynamic gson, Type superClazz) {
        gson.registerTypeAdapter(superClazz, new GsonEnumTypeAdapter<>(types, gson));
        return gson;
    }

    public static <Super> GsonEnumTypeAdapter<Super> createOnly(GsonEnumTypeHolder<Super>[] types,
        GsonBuilderDynamic gson) {
        return new GsonEnumTypeAdapter<>(types, gson);
    }

    @Override
    public Super deserialize(JsonElement jsonElement, Type type,
        JsonDeserializationContext context) {
        verifyGson();
        JsonObject json = jsonElement.getAsJsonObject();
        String typeId = json.get(TYPE_ID).getAsString();
        GsonEnumTypeHolder<Super> holder = this.getOrThrow(typeId);
        return gsonWithoutThis.fromJson(json, holder.getTypeClass());
    }


    @Override
    public JsonElement serialize(Super object, Type type, JsonSerializationContext context) {
        verifyGson();
        Class<?> clazz = object.getClass();
        @NotNull GsonEnumTypeHolder<Super> holder = this.getOrThrow(clazz);
        JsonObject json = gsonWithoutThis.toJsonTree(object, clazz).getAsJsonObject();
        json.add(TYPE_ID, new JsonPrimitive(holder.getTypeId()));
        return json;
    }

    private void verifyGson() {
        if (this.gsonWithoutThis == null)
            this.gsonWithoutThis = originalGson.copy().without(this).create();
    }


    @NotNull
    public GsonEnumTypeHolder<Super> getOrThrow(String typeId) {
        GsonEnumTypeHolder<Super> get = typesById.get(typeId);
        if (get == null)
            throw new JsonParseException(String.format("TypeId '%s' is not set" + typeId));
        return get;
    }

    @NotNull
    public GsonEnumTypeHolder<Super> getOrThrow(Class<?> typeId) {
        GsonEnumTypeHolder<Super> get = typesByClass.get(typeId);
        if (get == null)
            throw new JsonParseException(String.format("TypeId '%s' is not set" + typeId));
        return get;
    }
}
