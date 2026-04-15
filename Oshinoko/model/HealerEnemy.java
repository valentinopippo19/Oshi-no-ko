package model;

import java.util.List;

public class HealerEnemy extends GameCharacter {

    public HealerEnemy(String name, int hp, int mana, int atk, int def, int spd) {
        super(name, Role.SUPPORT, hp, mana, atk, def, spd, null);
    }

    public void act(List<GameCharacter> allies){
        GameCharacter ally = allies.stream()
                .filter(GameCharacter::isAlive)
                .findFirst()
                .orElse(null);

        if(ally != null){
            ally.heal(20);
        }
    }
}