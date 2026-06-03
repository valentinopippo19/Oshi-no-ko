package items;

import model.GameCharacter;

public class HealItem extends Item {

    private final int healAmount;

    public HealItem(
            String name,
            int healAmount
    ) {

        super(name);

        if (healAmount <= 0) {

            throw new IllegalArgumentException(
                    "Heal amount debe ser mayor a 0"
            );
        }

        this.healAmount = healAmount;
    }

    @Override
    public void use(GameCharacter target) {

        target.heal(healAmount);
    }

    public int getHealAmount() {
        return healAmount;
    }

    @Override
    public String toString() {

        return getName() +
                " (+" + healAmount + " HP)";
    }
}