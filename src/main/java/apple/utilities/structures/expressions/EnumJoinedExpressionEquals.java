package apple.utilities.structures.expressions;

public abstract class EnumJoinedExpressionEquals<EnumType> extends EnumJoinedExpressionImpl<EnumType> {
    public static <EnumType> boolean equals(EnumType[] enums1, EnumType[] enums2) {
        return EnumJoinedExpressionContains.contains12(enums1, enums2) && EnumJoinedExpressionContains.contains12(enums2, enums1);
    }

    @Override
    public boolean operate(EnumType[] enums) {
        EnumType[] enums1 = getMyEnums();
        return equals(enums1, enums);
    }

    @Override
    public EnumOperatorType getOperatorType() {
        return EnumOperatorType.ME_EQUALS_ARG;
    }
}
