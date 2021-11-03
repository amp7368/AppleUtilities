package apple.utilities.structures.expressions;

import java.util.Collection;

public interface EnumJoinedExpression<EnumType> {
    boolean operate(EnumType[] enums);

    EnumType[] getMyEnums();

    EnumOperatorType getOperatorType();

    void addEnums(Collection<EnumType> collection);

    void addEnum(EnumType e);

    void clearEnums();

    void removeEnums(Collection<EnumType> es);

    void removeEnum(EnumType e);

    void setEnums(Collection<EnumType> collection);

}
