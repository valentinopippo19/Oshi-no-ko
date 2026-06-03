package model;

public class TankCharacter
        extends PlayerCharacter {

    private static final int BONUS_DEFENSE = 8;

    public TankCharacter(
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

    @Override
    public int getDefense() {

        return super.getDefense()
                + BONUS_DEFENSE;
    }
}