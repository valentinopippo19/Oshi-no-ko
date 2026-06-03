package model;

public class SupportCharacter
        extends PlayerCharacter {

    private static final int BONUS_MANA = 25;

    public SupportCharacter(
            String name,
            int maxHp,
            int maxMana,
            int attack,
            int defense,
            int speed,
            String imagePath
    ) {

        super(
                name,
                maxHp,
                maxMana,
                attack,
                defense,
                speed,
                imagePath
        );
    }

    protected int getClassManaBonus() {

        return BONUS_MANA;
    }

    @Override
    protected int getClassAttackBonus() {

        return 0;
    }
}