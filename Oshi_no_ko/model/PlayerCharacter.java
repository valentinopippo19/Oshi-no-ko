package model;

import battle.BattleAction;

import java.util.List;

public class PlayerCharacter
        extends GameCharacter {

    // =====================================================
    // LEVEL
    // =====================================================

    protected int level = 1;

    protected int exp = 0;

    protected int expToLevel = 100;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public PlayerCharacter(
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

    // =====================================================
    // PLAYER TURN
    // =====================================================

    @Override
    public BattleAction act(
            List<GameCharacter> allies,
            List<GameCharacter> enemies
    ) {

        // El jugador actúa desde la UI
        return null;
    }

    @Override
    public void act() {

        // El jugador actúa desde GameUI
    }

    // =====================================================
    // EXPERIENCE
    // =====================================================

    public void gainExp(
            int amount
    ) {

        if(amount <= 0){

            return;
        }

        exp += amount;

        while(exp >= expToLevel){

            exp -= expToLevel;

            levelUp();
        }
    }

    // =====================================================
    // LEVEL UP
    // =====================================================

    private void levelUp(){

        level++;

        expToLevel += 50;

        // ==============================================
        // STATS
        // ==============================================

        maxHp += 10;

        maxMana += 5;

        baseAttack += 2;

        baseDefense += 2;

        speed += 1;

        // ==============================================
        // FULL RESTORE
        // ==============================================

        hp = maxHp;

        mana = maxMana;
    }

    // =====================================================
    // GETTERS
    // =====================================================

    public int getLevel(){

        return level;
    }

    public int getExp(){

        return exp;
    }

    public int getExpToLevel(){

        return expToLevel;
    }

    protected int getClassManaBonus() {
        throw new UnsupportedOperationException("Unimplemented method 'getClassManaBonus'");
    }

    protected int getClassAttackBonus() {
        throw new UnsupportedOperationException("Unimplemented method 'getClassAttackBonus'");
    }
}