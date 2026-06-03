package model;

import battle.BattleAction;
import battle.HealAction;

import java.util.List;

public class HealerEnemy extends EnemyCharacter {

    private static final int HEAL_AMOUNT = 20;

    public HealerEnemy(
            String name,
            int hp,
            int mana,
            int atk,
            int def,
            int spd,
            int expReward,
            String imagePath
    ) {

        super(
                name,
                hp,
                mana,
                atk,
                def,
                spd,
                expReward,
                imagePath
        );
    }

    @Override
    public BattleAction act(
            List<GameCharacter> allies,
            List<GameCharacter> enemies
    ) {

        GameCharacter target =
                findLowestHpAlly(allies);

        if (target == null) {

            return super.act(allies, enemies);
        }

        return new HealAction(
                this,
                target,
                HEAL_AMOUNT
        );
    }

    private GameCharacter findLowestHpAlly(
            List<GameCharacter> allies
    ) {

        GameCharacter lowest = null;

        for (GameCharacter ally : allies) {

            if (!ally.isAlive()) continue;

            if (lowest == null ||
                    ally.getHp() < lowest.getHp()) {

                lowest = ally;
            }
        }

        return lowest;
    }
}
