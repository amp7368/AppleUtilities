package apple.utilities.json.gson;

@Deprecated
public interface GsonTypeAdapterSerialization<Typee> {

    String getTypeId();

    Class<? extends Typee> getTypeClass();
}
