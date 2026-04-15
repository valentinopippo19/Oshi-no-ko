package model;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameCharacter implements Serializable {

    public String name;
    public Role role;

    // 🔥 STATS BASE (IMPORTANTÍSIMO)
    public int baseMaxHp, baseMaxMana;
    public int baseAttack, baseDefense, baseSpeed;

    // 🔥 STATS ACTUALES
    public int maxHp, maxMana, mana;
    private int hp;
    public int attack, defense, speed;

    public int level = 1, exp = 0;

    public StatusEffect status = StatusEffect.NONE;
    public int statusTurns = 0;

    public List<Skill> skills = new ArrayList<>();
    public transient ImageIcon imageIcon;

    public GameCharacter(String name, Role role, int hp, int mana, int atk, int def, int spd, ImageIcon img){
        this.name = name;
        this.role = role;

        // 🔥 BASE
        this.baseMaxHp = hp;
        this.baseMaxMana = mana;
        this.baseAttack = atk;
        this.baseDefense = def;
        this.baseSpeed = spd;

        // 🔥 ACTUAL
        this.maxHp = hp;
        this.hp = hp;
        this.maxMana = mana;
        this.mana = mana;
        this.attack = atk;
        this.defense = def;
        this.speed = spd;

        this.imageIcon = img;
    }

    public int getHp(){ return hp; }

    public void setHp(int hp){
        if(hp < 0) hp = 0;
        if(hp > maxHp) hp = maxHp;
        this.hp = hp;
    }

    public void resetToBase(){

        // 🔥 recalcula stats según nivel
        recalculateStats();

        // 🔥 llena vida y mana
        this.hp = maxHp;
        this.mana = maxMana;

        // 🔥 limpia estados
        this.status = StatusEffect.NONE;
        this.statusTurns = 0;
    }

    // 🔥 CLAVE: recalcular stats con level
    public void recalculateStats(){
        this.maxHp = baseMaxHp + (level - 1) * 20;
        this.maxMana = baseMaxMana + (level - 1) * 10;
        this.attack = baseAttack + (level - 1) * 5;
        this.defense = baseDefense + (level - 1) * 3;
        this.speed = baseSpeed;
    }

    public GameCharacter cloneCharacter(){
        GameCharacter c = new GameCharacter(name, role, baseMaxHp, baseMaxMana, baseAttack, baseDefense, baseSpeed, imageIcon);
        c.skills.addAll(skills);
        return c;
    }

    public boolean isAlive(){ return hp > 0; }

    public void takeDamage(int dmg){
        if(status == StatusEffect.DEFEND) dmg /= 2;

        int finalDamage = Math.max(0, dmg - defense);
        hp -= finalDamage;

        if(hp < 0) hp = 0;
    }

    public void defend(){
        status = StatusEffect.DEFEND;
        statusTurns = 1;
    }

    public void heal(int amount){
        hp = Math.min(maxHp, hp + amount);
    }

    public void gainExp(int amount){
        exp += amount;
        if(exp >= level * 100){
            exp = 0;
            levelUp();
        }
    }

    private void levelUp(){
        level++;

        // 🔥 en vez de sumar directo → recalculamos
        recalculateStats();

        hp = maxHp;
        mana = maxMana;
    }

    public int getAttack(){
        return attack;
    }

    public void applyStatusEffects(){
        if(status == StatusEffect.POISON){
            takeDamage(5);
        }

        if(statusTurns > 0){
            statusTurns--;
            if(statusTurns == 0){
                status = StatusEffect.NONE;
            }
        }
    }
}