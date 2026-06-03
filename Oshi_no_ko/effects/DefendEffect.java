package effects;

public class DefendEffect
        extends StatusEffect {

    private final int defenseBonus;

    public DefendEffect(
            int duration,
            int defenseBonus
    ) {

        super(duration);

        this.defenseBonus = defenseBonus;
    }

    @Override
    public int modifyDefense(int value) {

        return value + defenseBonus;
    }

    @Override
    public String getName() {
        return "Defend";
    }
}