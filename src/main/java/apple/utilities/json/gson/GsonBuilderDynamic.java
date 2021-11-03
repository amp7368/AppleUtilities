package apple.utilities.json.gson;

import apple.utilities.structures.Pair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class GsonBuilderDynamic implements Supplier<Gson> {
    private final List<Pair<Class<?>, Object>> typeHierarchyAdapters = new ArrayList<>();
    private final List<Pair<Type, Object>> typeAdapters = new ArrayList<>();
    private final List<TypeAdapterFactory> typeAdapterFactories = new ArrayList<>();
    private Gson gsonFinal = null;

    public GsonBuilderDynamic() {
    }

    public GsonBuilderDynamic(GsonBuilderDynamic other) {
        this.gsonFinal = null;
        this.typeHierarchyAdapters.addAll(other.typeHierarchyAdapters);
        this.typeAdapters.addAll(other.typeAdapters);
        this.typeAdapterFactories.addAll(other.typeAdapterFactories);
    }

    @Override
    public Gson get() {
        return create();
    }

    public Gson create() {
        if (gsonFinal == null) {
            GsonBuilder builder = new GsonBuilder();
            for (Pair<Class<?>, Object> typeAdapter : typeHierarchyAdapters) {
                builder.registerTypeHierarchyAdapter(typeAdapter.getKey(), typeAdapter.getValue());
            }
            for (Pair<Type, Object> typeAdapter : typeAdapters) {
                builder.registerTypeAdapter(typeAdapter.getKey(), typeAdapter.getValue());
            }
            for (TypeAdapterFactory typeAdapter : typeAdapterFactories) {
                builder.registerTypeAdapterFactory(typeAdapter);
            }
            this.gsonFinal = builder.create();
        }
        return gsonFinal;
    }

    public GsonBuilderDynamic copy() {
        return new GsonBuilderDynamic(this);
    }

    public GsonBuilderDynamic without(Object typeAdapter) {
        this.typeHierarchyAdapters.removeIf(p -> p.getValue() == typeAdapter);
        this.typeAdapters.removeIf(p -> p.getValue() == typeAdapter);
        this.typeAdapterFactories.removeIf(t -> t == typeAdapter);
        return this;
    }

    public GsonBuilderDynamic registerTypeHierarchyAdapter(Class<?> clazz, Object typeAdapter) {
        this.typeHierarchyAdapters.add(new Pair<>(clazz, typeAdapter));
        return this;
    }

    public GsonBuilderDynamic registerTypeAdapter(Type clazz, Object typeAdapter) {
        this.typeAdapters.add(new Pair<>(clazz, typeAdapter));
        return this;
    }

    public GsonBuilderDynamic registerTypeAdapterFactory(TypeAdapterFactory typeAdapterFactory) {
        this.typeAdapterFactories.add(typeAdapterFactory);
        return this;
    }
}
