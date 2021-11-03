package apple.utilities.structures.expressions;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.IntFunction;

public abstract class EnumJoinedExpressionImpl<Enum> implements EnumJoinedExpression<Enum> {
    private HashSet<Enum> myEnums = new HashSet<>();

    @Override
    public Enum[] getMyEnums() {
        return myEnums.toArray(getEnumArrayConverter());
    }

    @Override
    public void addEnum(Enum e) {
        this.myEnums.add(e);
    }

    public abstract IntFunction<Enum[]> getEnumArrayConverter();

    public void addEnums(Collection<Enum> collection) {
        this.myEnums.addAll(collection);
    }

    @Override
    public void setEnums(Collection<Enum> collection) {
        myEnums = new HashSet<>(collection);
    }

    @Override
    public void removeEnum(Enum e) {
        this.myEnums.remove(e);
    }

    @Override
    public void removeEnums(Collection<Enum> es) {
        this.myEnums.removeAll(es);
    }

    @Override
    public void clearEnums() {
        this.myEnums.clear();
    }
}
