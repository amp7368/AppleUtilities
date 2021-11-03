package apple.utilities.json.gson;

public record GsonTypeAdapterSimpleMapping<Typee>(String typeId,
                                                  Class<Typee> clazz
) implements GsonTypeAdapterSerialization<Typee> {

    @Override
    public String getTypeId() {
        return typeId;
    }

    @Override
    public Class<? extends Typee> getTypeClass() {
        return clazz;
    }
}
