package system;

import model.EnemyCharacter;
import model.GameCharacter;
import model.HealerEnemy;

import java.util.ArrayList;
import java.util.List;

public class EnemyFactory {

    // ======================================================
    // CREAR TODOS LOS ENEMIGOS
    // ======================================================

    public static List<GameCharacter> createEnemies() {

        List<GameCharacter> enemies =
                new ArrayList<>();

        enemies.add(createAiEnemy());

        enemies.add(createRubyEnemy());

        enemies.add(createKanaEnemy());

        enemies.add(createAkaneEnemy());

        enemies.add(createMemchoEnemy());

        enemies.add(createMiyakoEnemy());

        return enemies;
    }

    private static EnemyCharacter createAiEnemy() {
        return new EnemyCharacter(
                "Ai",
                100,
                100,
                10,
                5,
                7,
                50,
                "resources/ai.png"
        );
    }

    private static EnemyCharacter createRubyEnemy() {
        return new EnemyCharacter(
                "Ruby",
                80,
                50,
                15,
                5,
                10,
                50,
                "resources/ruby.png"
        );
    }

    private static EnemyCharacter createKanaEnemy() {
        return new EnemyCharacter(
                "Kana",
                85,
                70,
                12,
                6,
                11,
                50,
                "resources/kana.png"
        );
    }

    private static EnemyCharacter createAkaneEnemy() {
        return new EnemyCharacter(
                "Akane",
                90,
                120,
                9,
                7,
                8,
                50,
                "resources/akane.png"
        );
    }

    private static EnemyCharacter createMemchoEnemy() {
        return new HealerEnemy(
                "Memcho",
                75,
                60,
                18,
                4,
                12,
                75,
                "resources/mem.png"
        );
    }

    private static EnemyCharacter createMiyakoEnemy() {
        return new EnemyCharacter(
                "Miyako",
                140,
                40,
                8,
                12,
                4,
                50,
                "resources/miyako.png"
        );
    }
}