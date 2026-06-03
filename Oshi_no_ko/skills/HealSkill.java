package skills;

import model.GameCharacter;
import model.GameException;

public class HealSkill extends Skill {

    private final int healAmount;

    public HealSkill(
            String name,
            int manaCost,
            int healAmount
    ) {

        super(name, manaCost);

        if (healAmount <= 0) {

            throw new IllegalArgumentException(
                    "La curación debe ser positiva"
            );
        }

        this.healAmount = healAmount;
    }

    @Override
    public void use(
            GameCharacter user,
            GameCharacter target
    ) throws GameException {

        if (target == null) {

            throw new GameException(
                    "Objetivo inválido"
            );
        }

        if (!target.isAlive()) {

            throw new GameException(
                    "No se puede curar un personaje derrotado"
            );
        }

        user.spendMana(manaCost);

        int finalHeal =
                calculateHeal(user);

        target.heal(finalHeal);
    }

    protected int calculateHeal(
            GameCharacter user
    ) {

        return healAmount;
    }

    public int getHealAmount() {
        return healAmount;
    }

    @Override
    public String toString() {

        return getName() +
                " (+" + healAmount +
                " HP)";
    }
}