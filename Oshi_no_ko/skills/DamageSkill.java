package skills;

import model.GameCharacter;
import model.GameException;

public class DamageSkill extends Skill {

    private final int power;

    public DamageSkill(
            String name,
            int manaCost,
            int power
    ) {

        super(name, manaCost);

        if (power <= 0) {

            throw new IllegalArgumentException(
                    "El poder debe ser positivo"
            );
        }

        this.power = power;
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
                    "El objetivo está derrotado"
            );
        }

        user.spendMana(manaCost);

        int damage =
                calculateDamage(user);

        target.takeDamage(damage);
    }

    protected int calculateDamage(
            GameCharacter user
    ) {

        return user.getAttack() + power;
    }

    public int getPower() {
        return power;
    }

    @Override
    public String toString() {

        return getName() +
                " (" + manaCost +
                " MP)";
    }
}