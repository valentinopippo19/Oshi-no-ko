package system;

import model.*;
import javax.swing.*;
import java.util.*;

public class EnemyFactory {

    public static List<GameCharacter> createEnemies(){

        List<GameCharacter> enemies = new ArrayList<>();

        ImageIcon enemyImg = new ImageIcon("resources/enemy.png");

        GameCharacter e1 = new GameCharacter("Enemy1", Role.DPS, 60, 30, 12, 4, 8, enemyImg);
        GameCharacter e2 = new GameCharacter("Enemy2", Role.DPS, 70, 20, 10, 5, 6, enemyImg);

        enemies.add(e1);
        enemies.add(e2);

        return enemies;
    }
}