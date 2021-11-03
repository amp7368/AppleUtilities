package apple.utilities.structures.expressions;

public abstract class EnumJoinedExpressionContains<EnumType> extends EnumJoinedExpressionImpl<EnumType> {
    public static <EnumType> boolean contains12(EnumType[] enums1, EnumType[] enums2) {
        for (EnumType e1 : enums1) {
            boolean fail = true;
            for (EnumType e2 : enums2) {
                if (e1 == e2) {
                    fail = false;
                    break;
                }
            }
            if (fail) return true;
        }
        return false;
    }

    @Override
    public boolean operate(EnumType[] enums) {
        return contains12(getMyEnums(), enums);
    }

    @Override
    public EnumOperatorType getOperatorType() {
        return EnumOperatorType.ME_CONTAINS_ARG;
    }
}
