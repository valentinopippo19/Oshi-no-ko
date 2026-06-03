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

        enemies.add(createAkaneEnemy());

        enemies.add(createMemchoHealer());

        return enemies;
    }

    // ======================================================
    // AKANE DPS
    // ======================================================

    private static EnemyCharacter createAkaneEnemy() {

        return new EnemyCharacter(
                "Akane",
                70,
                30,
                15,
                5,
                8,
                50,
                "resources/akane.png"
        );
    }

    // ======================================================
    // MEMCHO HEALER
    // ======================================================

    private static HealerEnemy createMemchoHealer() {

        return new HealerEnemy(
                "Memcho",
                60,
                50,
                8,
                4,
                6,
                75,
                "resources/mem.png"
        );
    }
}