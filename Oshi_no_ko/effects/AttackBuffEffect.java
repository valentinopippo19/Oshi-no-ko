package effects;

public class AttackBuffEffect
        extends StatusEffect {

    private final int bonus;

    public AttackBuffEffect(
            int bonus,
            int duration
    ) {

        super(duration);

        this.bonus = bonus;
    }

    @Override
    public int modifyAttack(int value) {

        return value + bonus;
    }

    @Override
    public String getName() {
        return "Attack Buff";
    }
}