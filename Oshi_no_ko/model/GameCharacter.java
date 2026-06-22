package model;

import effects.AttackBuffEffect;
import effects.StatusEffect;
import skills.Skill;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class GameCharacter
        implements Serializable {

    private final String name;

    // =====================================================
    // STATS
    // =====================================================

    protected int maxHp;
    protected int hp;

    protected int maxMana;
    protected int mana;

    protected int baseAttack;
    protected int baseDefense;

    protected int speed;

    // =====================================================
    // STATUS
    // =====================================================

    private String status = "NORMAL";

    // =====================================================
    // EFFECTS
    // =====================================================

    protected List<StatusEffect> effects =
            new ArrayList<>();

    // =====================================================
    // SKILLS
    // =====================================================

    protected List<Skill> skills =
            new ArrayList<>();

    // =====================================================
    // IMAGE
    // =====================================================

    protected transient ImageIcon imageIcon;

    protected String imagePath;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public GameCharacter(
            String name,
            int maxHp,
            int maxMana,
            int attack,
            int defense,
            int speed,
            String imagePath
    ) {

        this.name = name;

        this.maxHp = maxHp;
        this.hp = maxHp;

        this.maxMana = maxMana;
        this.mana = maxMana;

        this.baseAttack = attack;
        this.baseDefense = defense;

        this.speed = speed;

        this.imagePath = imagePath;

        if(imagePath != null){

            this.imageIcon =
                    new ImageIcon(imagePath);
        }
    }

    // =====================================================
    // LIFE
    // =====================================================

    public boolean isAlive(){

        return hp > 0;
    }

    public void takeDamage(int damage){

        int finalDamage =
                Math.max(
                        1,
                        damage - getDefense()
                );

        hp -= finalDamage;

        if(hp < 0){

            hp = 0;
        }
    }

    public void heal(int amount){

        hp += amount;

        if(hp > maxHp){

            hp = maxHp;
        }
    }

    // =====================================================
    // MANA
    // =====================================================

    public void spendMana(int amount)
            throws GameException {

        if(mana < amount){

            throw new GameException(
                    "No hay suficiente mana"
            );
        }

        mana -= amount;
    }

    public void restoreMana(int amount){

        mana += amount;

        if(mana > maxMana){

            mana = maxMana;
        }
    }

    // =====================================================
    // EFFECTS
    // =====================================================

    public void addEffect(
            StatusEffect effect
    ){

        effects.add(effect);

        effect.onApply(this);
    }

    public void applyStatusEffects(){

        Iterator<StatusEffect> iterator =
                effects.iterator();

        while(iterator.hasNext()){

            StatusEffect effect =
                    iterator.next();

            effect.onTurnStart(this);

            effect.decreaseDuration();

            if(effect.isExpired()){

                effect.onExpire(this);

                iterator.remove();
            }
        }
    }

    // =====================================================
    // STATS
    // =====================================================

    public int getAttack(){

        int total =
                baseAttack;

        for(StatusEffect e : effects){

            total =
                    e.modifyAttack(total);
        }

        return total;
    }

    public int getDefense(){

        int total =
                baseDefense;

        for(StatusEffect e : effects){

            total =
                    e.modifyDefense(total);
        }

        return total;
    }

    // =====================================================
    // SKILLS
    // =====================================================

    public void addSkill(
            Skill skill
    ){

        skills.add(skill);
    }

    // =====================================================
    // IMAGE
    // =====================================================

    public ImageIcon getImageIcon(){

        if(imageIcon == null &&
                imagePath != null){

            imageIcon =
                    new ImageIcon(imagePath);
        }

        return imageIcon;
    }

    // =====================================================
    // GETTERS
    // =====================================================

    public String getName(){

        return name;
    }

    public int getHp(){

        return hp;
    }

    public int getMana(){

        return mana;
    }

    public int getSpeed(){

        return speed;
    }

    public int getMaxHp(){

        return maxHp;
    }

    public int getMaxMana(){

        return maxMana;
    }

    public List<Skill> getSkills(){

        return skills;
    }

    public List<StatusEffect> getEffects(){

        return effects;
    }

    public String getImagePath(){

        return imagePath;
    }

    public String getStatus(){

        return status;
    }

    public void setStatus(
            String status
    ){

        this.status = status;
    }

    // =====================================================
    // IA
    // =====================================================

    public abstract battle.BattleAction act(
            List<GameCharacter> allies,
            List<GameCharacter> enemies
    );

    public abstract void act();

    protected int getClassManaBonus() {
        throw new UnsupportedOperationException("Unimplemented method 'getClassManaBonus'");
    }

    protected int getClassAttackBonus() {
        throw new UnsupportedOperationException("Unimplemented method 'getClassAttackBonus'");
    }

    public void addEffect(AttackBuffEffect effect) {
        throw new UnsupportedOperationException("Unimplemented method 'addEffect'");
    }

    public abstract int getLevel();

    public abstract void gainExp(int totalBattleExp);

}