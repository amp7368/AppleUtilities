package apple.utilities.structures;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface EnumJoinedExpressionFunctional<Enum> {
    @NotNull
    Pair<Enum[], Boolean> getEnumIsOr();

    default boolean isContained(Enum[] enums, boolean isOr) {
        Pair<Enum[], Boolean> config = getEnumIsOr();
        Enum[] myEnums = config.getKey();
        if (config.getValue()) {
            for (Enum e1 : enums) {
                for (Enum e2 : myEnums) {
                    if (e1 == e2) {
                        return true;
                    }
                }
            }
            return false;
        } else {
            for (Enum e1 : enums) {
                boolean fail = true;
                for (Enum e2 : myEnums) {
                    if (e1 == e2) {
                        fail = false;
                        break;
                    }
                }
                if (fail) return false;
            }
            return true;
        }
    }
}
