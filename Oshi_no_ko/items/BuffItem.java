package items;

import model.GameCharacter;
import effects.AttackBuffEffect;

public class BuffItem extends Item {

    private final int attackBonus;

    private final int duration;

    public BuffItem(
            String name,
            int attackBonus,
            int duration
    ) {

        super(name);

        this.attackBonus = attackBonus;

        this.duration = duration;
    }

    @Override
    public void use(GameCharacter target) {

        target.addEffect(
                new AttackBuffEffect(
                        attackBonus,
                        duration
                )
        );
    }

    public int getAttackBonus() {
        return attackBonus;
    }

    public int getDuration() {
        return duration;
    }
}