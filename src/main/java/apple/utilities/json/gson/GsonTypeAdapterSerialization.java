package apple.utilities.json.gson;

public interface GsonTypeAdapterSerialization<Typee> {
    String getTypeId();

    Class<? extends Typee> getTypeClass();
}
