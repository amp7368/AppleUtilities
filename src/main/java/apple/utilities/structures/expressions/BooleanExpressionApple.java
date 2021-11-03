package apple.utilities.structures.expressions;

public interface BooleanExpressionApple<T> {
    boolean decide(T arg);

    default BooleanExpressionApple<T> and(BooleanExpressionApple<T> other) {
        BooleanExpressionApple<T> me = this;
        return (t) -> me.decide(t) && other.decide(t);
    }

    default BooleanExpressionApple<T> or(BooleanExpressionApple<T> other) {
        BooleanExpressionApple<T> me = this;
        return (t) -> me.decide(t) || other.decide(t);
    }

    default BooleanExpressionApple<T> not() {
        BooleanExpressionApple<T> me = this;
        return (t) -> !me.decide(t);
    }
}
