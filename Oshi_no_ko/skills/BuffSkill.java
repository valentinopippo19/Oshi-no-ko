package skills;

import effects.AttackBuffEffect;
import model.GameCharacter;
import model.GameException;

public class BuffSkill extends Skill {

    private final int buffAmount;

    private final int duration;

    public BuffSkill(
            String name,
            int manaCost,
            int buffAmount,
            int duration
    ) {

        super(name, manaCost);

        if (buffAmount <= 0) {

            throw new IllegalArgumentException(
                    "El buff debe ser positivo"
            );
        }

        if (duration <= 0) {

            throw new IllegalArgumentException(
                    "La duración debe ser positiva"
            );
        }

        this.buffAmount = buffAmount;

        this.duration = duration;
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

        user.spendMana(manaCost);

        target.addEffect(
                new AttackBuffEffect(
                        buffAmount,
                        duration
                )
        );
    }

    public int getBuffAmount() {
        return buffAmount;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {

        return getName() +
                " (+" + buffAmount +
                " ATK / " +
                duration + " turnos)";
    }
}