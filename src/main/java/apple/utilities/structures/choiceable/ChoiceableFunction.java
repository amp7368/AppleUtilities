package apple.utilities.structures.choiceable;

import java.util.function.Function;

public class ChoiceableFunction<Give, Thing> {

    Function<Give, Thing> supplier;
    Thing gotten;

    public ChoiceableFunction(Function<Give, Thing> supplier) {
        this.supplier = supplier;
        this.gotten = null;
    }

    public ChoiceableFunction(Thing gotten) {
        this.supplier = null;
        this.gotten = gotten;
    }

    public static <Give, Thing> ChoiceableFunction<Give, Thing> create(
        Function<Give, Thing> thing) {
        return new ChoiceableFunction<>(thing);
    }

    public static <Give, Thing> ChoiceableFunction<Give, Thing> create(Thing thing) {
        return new ChoiceableFunction<>(thing);
    }

    public Thing getChoice(Give give) {
        if (gotten == null) {
            return this.gotten = supplier.apply(give);
        }
        return gotten;
    }

    public void setChoice(Thing choice) {
        this.gotten = choice;
    }

    public Thing getChoiceAndSave(Give give) {
        return gotten = getChoice(give);
    }

}
