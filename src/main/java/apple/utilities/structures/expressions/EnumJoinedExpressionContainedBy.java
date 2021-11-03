package apple.utilities.structures.expressions;

public abstract class EnumJoinedExpressionContainedBy<EnumType> extends EnumJoinedExpressionImpl<EnumType> {
    @Override
    public boolean operate(EnumType[] enums) {
        return EnumJoinedExpressionContains.contains12(enums, getMyEnums());
    }

    @Override
    public EnumOperatorType getOperatorType() {
        return EnumOperatorType.ARG_CONTAINS_ME;
    }
}
