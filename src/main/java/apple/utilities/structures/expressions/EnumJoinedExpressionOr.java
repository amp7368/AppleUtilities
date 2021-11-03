package apple.utilities.structures.expressions;

public abstract class EnumJoinedExpressionOr<EnumType> extends EnumJoinedExpressionImpl<EnumType> {
    public static <EnumType> boolean isValidOr(EnumType[] enums1, EnumType[] enums2) {
        for (EnumType e1 : enums1) {
            for (EnumType e2 : enums2) {
                if (e1 == e2) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean operate(EnumType[] enums) {
        EnumType[] enums1 = getMyEnums();
        return isValidOr(enums1, enums);
    }

    @Override
    public EnumOperatorType getOperatorType() {
        return EnumOperatorType.OR;
    }
}
