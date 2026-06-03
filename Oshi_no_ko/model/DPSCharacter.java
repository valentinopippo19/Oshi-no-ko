package model;

public class DPSCharacter
        extends PlayerCharacter {

    private static final int BONUS_DAMAGE = 5;

    public DPSCharacter(
            String name,
            int hp,
            int mana,
            int atk,
            int def,
            int spd,
            String imagePath
    ) {

        super(
                name,
                hp,
                mana,
                atk,
                def,
                spd,
                imagePath
        );
    }

    @Override
    protected int getClassAttackBonus() {

        return BONUS_DAMAGE;
    }

    @Override
    public void act() {

        // El jugador actúa desde la UI
    }
}