package apple.utilities.structures.choiceable;

import java.util.function.Supplier;

public class ChoiceableSupplier<Thing> {
    Supplier<Thing> supplier;
    Thing gotten;

    public ChoiceableSupplier(Supplier<Thing> supplier) {
        this.supplier = supplier;
        this.gotten = null;
    }

    public ChoiceableSupplier(Thing gotten) {
        this.supplier = null;
        this.gotten = gotten;
    }

    public static <Thing> ChoiceableSupplier<Thing> create(Supplier<Thing> thing) {
        return new ChoiceableSupplier<>(thing);
    }

    public static <Thing> ChoiceableSupplier<Thing> create(Thing thing) {
        return new ChoiceableSupplier<>(thing);
    }

    public Thing getChoice() {
        if (gotten == null) {
            return supplier.get();
        }
        return gotten;
    }

    public void setChoice(Thing choice) {
        this.gotten = choice;
    }

    public Thing getChoiceAndSave() {
        return gotten = getChoice();
    }
}
