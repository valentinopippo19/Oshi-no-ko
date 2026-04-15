package model;

import java.io.Serializable;

// =====================
// SKILL
// =====================

public class Skill implements Serializable {
    public String name;
    public int manaCost;
    public SkillType type;

    public Skill(String n, int m, SkillType t){
        name = n;
        manaCost = m;
        type = t;
    }

    public void use(GameCharacter user, GameCharacter target) throws GameException {

        if(user.mana < manaCost){
            throw new GameException("No hay suficiente mana");
        }

        user.mana -= manaCost;

        switch(type){
            case DAMAGE:
                if(target != null)
                    target.takeDamage(user.attack + 10);
                break;

            case HEAL:
                user.heal(20);
                break;

            case BUFF:
                user.attack += 5;
                break;
        }
    }
}