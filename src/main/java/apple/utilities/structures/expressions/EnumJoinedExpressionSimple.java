package apple.utilities.structures.expressions;

public abstract class EnumJoinedExpressionSimple<EnumType> extends EnumJoinedExpressionImpl<EnumType> {
    private EnumOperatorType operator;

    public EnumJoinedExpressionSimple(EnumOperatorType operator) {
        this.operator = operator;
    }

    @Override
    public boolean operate(EnumType[] enums1) {
        EnumType[] enums2 = getMyEnums();

        return switch (operator) {
            case OR -> EnumJoinedExpressionOr.isValidOr(enums1, enums2);
            case ARG_CONTAINS_ME -> EnumJoinedExpressionContains.contains12(enums1, enums2);
            case ME_CONTAINS_ARG -> EnumJoinedExpressionContains.contains12(enums2, enums1);
            case ME_EQUALS_ARG -> EnumJoinedExpressionEquals.equals(enums1, enums2);
        };
    }

    @Override
    public EnumOperatorType getOperatorType() {
        return this.operator;
    }

    public void setOperator(EnumOperatorType operator) {
        this.operator = operator;
    }
}
