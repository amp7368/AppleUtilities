package apple.utilities.json.gson;

import apple.utilities.json.gson.serialize.JsonSerializingWithGson;
import apple.utilities.structures.Pair;
import apple.utilities.structures.choiceable.ChoiceableFunction;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

@Deprecated
public class GsonTypeAdapterMapper<Typee, Enumm extends GsonTypeAdapterSerialization<Typee>> implements
    JsonSerializingWithGson<Typee> {

    private static final String TYPE_ID = "typeId";
    private final Map<String, Enumm> typesById;
    private final Map<Class<?>, Enumm> typesByClass;
    private final Class<Typee> superClazz;

    protected GsonTypeAdapterMapper(Map<String, Enumm> typesById, Map<Class<?>, Enumm> typesByClass,
        Class<Typee> superClazz) {
        this.typesById = typesById;
        this.typesByClass = typesByClass;
        this.superClazz = superClazz;
    }

    protected GsonTypeAdapterMapper(Enumm[] values, Class<Typee> superClazz) {
        this.typesByClass = new HashMap<>();
        this.typesById = new HashMap<>();
        this.superClazz = superClazz;
        for (Enumm enumm : values) {
            this.typesByClass.put(enumm.getTypeClass(), enumm);
            this.typesById.put(enumm.getTypeId(), enumm);
        }
    }


    public static <Typee> GsonTypeAdapterMapper<Typee, GsonTypeAdapterSimpleMapping<Typee>> create(
        Collection<Pair<String, Class<Typee>>> values, Class<Typee> superClazz) {
        Map<String, GsonTypeAdapterSimpleMapping<Typee>> typesById = new HashMap<>();
        Map<Class<?>, GsonTypeAdapterSimpleMapping<Typee>> typesByClass = new HashMap<>();
        for (Pair<String, Class<Typee>> enumm : values) {
            GsonTypeAdapterSimpleMapping<Typee> mapping = new GsonTypeAdapterSimpleMapping<>(
                enumm.getKey(), enumm.getValue());
            typesById.put(enumm.getKey(), mapping);
            typesByClass.put(enumm.getValue(), mapping);
        }
        return new GsonTypeAdapterMapper<>(typesById, typesByClass, superClazz);
    }

    public static <Typee, Enumm extends GsonTypeAdapterSerialization<Typee>> GsonTypeAdapterMapper<Typee, Enumm> create(
        Map<String, Enumm> typesById, Map<Class<?>, Enumm> typesByClass, Class<Typee> superClazz) {
        return new GsonTypeAdapterMapper<>(typesById, typesByClass, superClazz);
    }

    public static <Typee, Enumm extends GsonTypeAdapterSerialization<Typee>> GsonTypeAdapterMapper<Typee, Enumm> create(
        Enumm[] values, Class<Typee> superClazz) {
        return new GsonTypeAdapterMapper<>(values, superClazz);
    }

    public GsonSerializing<Typee> getGsonSerializing(GsonBuilderDynamic gsonBuilder) {
        return new GsonSerializing<>(this, this.dynamicToFinalized(gsonBuilder));
    }

    public GsonSerializing<Typee> getGsonSerializing(Gson gsonSimple) {
        return new GsonSerializing<>(this, ChoiceableFunction.create(gsonSimple));
    }

    @Override
    public Typee deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context,
        Gson gson) throws JsonParseException {
        JsonObject json = jsonElement.getAsJsonObject();
        String typeId = json.get(TYPE_ID).getAsString();
        Enumm enumm = this.get(typeId);
        if (enumm == null)
            throw new JsonParseException("typeId not set for " + jsonElement);
        return gson.fromJson(json, enumm.getTypeClass());
    }

    @Override
    public JsonElement serialize(Typee object, Type type, JsonSerializationContext context,
        Gson gson) {
        Class<?> clazz = object.getClass();
        @Nullable Enumm enumm = this.get(clazz);
        if (enumm == null)
            throw new JsonParseException("typeId not set for " + clazz + "{" + object + "}");
        JsonObject json = gson.toJsonTree(object, clazz).getAsJsonObject();
        json.add(TYPE_ID, new JsonPrimitive(enumm.getTypeId()));
        return json;
    }

    public ChoiceableFunction<GsonSerializing<Typee>, Gson> dynamicToFinalized(
        GsonBuilderDynamic gsonBuilderDynamic) {
        return ChoiceableFunction.create(
            serializer -> gsonBuilderDynamic.copy().without(serializer).create());
    }

    @Nullable
    public Enumm get(String typeId) {
        return typesById.get(typeId);
    }

    @Nullable
    public Enumm get(Class<?> typeId) {
        return typesByClass.get(typeId);
    }

    public Class<?> getSuper() {
        return this.superClazz;
    }
}
