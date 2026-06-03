package items;

import model.GameCharacter;

public class ManaItem extends Item {

    private final int manaRestore;

    public ManaItem(
            String name,
            int manaRestore
    ) {

        super(name);

        if (manaRestore <= 0) {

            throw new IllegalArgumentException(
                    "La restauración de mana debe ser positiva"
            );
        }

        this.manaRestore = manaRestore;
    }

    @Override
    public void use(GameCharacter target) {

        target.restoreMana(manaRestore);
    }

    public int getManaRestore() {
        return manaRestore;
    }

    @Override
    public String toString() {

        return getName() +
                " (+" + manaRestore + " MP)";
    }
}