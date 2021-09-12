package apple.utilities.structures;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface EnumJoinedExpression<Me extends EnumJoinedExpression<Me, Enum>, Enum extends Enumable> extends EnumJoinedExpressionFunctional<Enum> {

    static <Me extends EnumJoinedExpression<Me, Enum>, Enum extends Enumable> JsonSerializer<Me> getSerializer() {
        return (src, typeOfSrc, context) -> {
            Enum[] myEnums = src.getMyEnums();
            List<Enum> enums = Arrays.stream(myEnums).sorted(Comparator.comparingInt(Enum::ordinal)).collect(Collectors.toList());
            String[] enumsAsStrings = new String[enums.size()];
            int i = 0;
            for (Enum e : enums) {
                enumsAsStrings[i] = e.name();
            }
            return new JsonPrimitive(String.join(",", enumsAsStrings));
        };
    }

    static <Me extends EnumJoinedExpression<Me, Enum>, Enum extends Enumable> JsonDeserializer<Me> getDeserializer(Class<Me> clazz) {
        return (json, typeOfT, context) -> {
            String[] enumsAsString = json.getAsString().split(",");
            Me deserialized = context.deserialize(json, typeOfT);
            deserialized.setEnumsFromString(enumsAsString);
            return deserialized;
        };
    }

    Enum[] getMyEnums();

    boolean getIsOr();

    void setEnums(Collection<Enum> enumsAsString);

    Function<String, Enum> getEnumConverter();

    default boolean isContained(Enum[] enums) {
        return isContained(enums, getIsOr());
    }

    @Override
    @NotNull
    default Pair<Enum[], Boolean> getEnumIsOr() {
        return new Pair<>(getMyEnums(), getIsOr());
    }

    default void setEnumsFromString(String... enumsAsString) {
        Collection<Enum> enums = new ArrayList<>();
        for (String s : enumsAsString) {
            enums.add(getEnumConverter().apply(s));
        }
        setEnums(enums);
    }
}
