package model;

import system.BattleSystem;

import java.util.List;
import java.util.Random;

public class EnemyCharacter extends GameCharacter {

    private static final Random RANDOM = new Random();

    private final int expReward;
    private boolean defending = false;

    public EnemyCharacter(
            String name,
            int hp,
            int mana,
            int atk,
            int def,
            int spd,
            int expReward,
            String imagePath
    ) {
        super(name, hp, mana, atk, def, spd, imagePath);
        this.expReward = expReward;
    }

    public int getExpReward() {
        return expReward;
    }

    // =====================================================
    // IA (NO USADA POR AHORA EN TU SYSTEM ACTUAL)
    // =====================================================

    @Override
    public battle.BattleAction act(
            List<GameCharacter> allies,
            List<GameCharacter> enemies
    ) {
        return null;
    }

    // =====================================================
    // TURNO REAL (USADO POR BattleSystem)
    // =====================================================

    @Override
    public void act() {

        List<GameCharacter> targets = BattleSystem.getAlivePlayers();

        if (targets.isEmpty()) return;

        GameCharacter target =
                targets.get(RANDOM.nextInt(targets.size()));

        target.takeDamage(getAttack());
    }

    // =====================================================
    // STATUS
    // =====================================================

    @Override
    public String getStatus() {

        if (!isAlive()) return "DEAD";
        if (defending) return "DEFENDING";
        return "NORMAL";
    }

    // =====================================================
    // STATUS EFFECTS (mínimo funcional)
    // =====================================================

    @Override
    public void applyStatusEffects() {
        effects.removeIf(e -> e.isExpired());
    }

    public void addEffect(effects.AttackBuffEffect effect) {
        effects.add(effect);
    }

    @Override
    public void restoreMana(int amount) {
        mana = Math.min(maxMana, mana + amount);
    }

    @Override
    public int getLevel() {
        return 0; // Los enemigos no tienen nivel
    }

    @Override
    public void gainExp(int totalBattleExp) {
        // Los enemigos no ganan experiencia
    }
}