package model;

public class DPSCharacter extends GameCharacter {

    public DPSCharacter(String name, int hp, int mana, int atk, int def, int spd){
        super(name, Role.DPS, hp, mana, atk, def, spd, null);
    }

    @Override
    public void takeDamage(int dmg){
        // DPS recibe más daño (ejemplo)
        super.takeDamage(dmg + 2);
    }
}