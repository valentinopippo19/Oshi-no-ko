package effects;

import model.GameCharacter;

public class PoisonEffect
        extends StatusEffect {

    private final int damagePerTurn;

    public PoisonEffect(
            int duration,
            int damagePerTurn
    ) {

        super(duration);

        this.damagePerTurn = damagePerTurn;
    }

    @Override
    public void onTurnStart(
            GameCharacter target
    ) {

        target.takeDamage(damagePerTurn);
    }

    @Override
    public String getName() {
        return "Poison";
    }
}