package apple.utilities.gson.adapter;

public interface GsonEnumTypeHolder<Super> {

    String getTypeId();

    Class<? extends Super> getTypeClass();
}
