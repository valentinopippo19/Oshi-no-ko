package model;

public class DamageSkill extends Skill {

    public DamageSkill(String n, int m){
        super(n, m, SkillType.DAMAGE);
    }

    @Override
    public void use(GameCharacter user, GameCharacter target) throws GameException {
        if(user.mana < manaCost){
            throw new GameException("No hay suficiente mana");
        }

        user.mana -= manaCost;

        if(target != null){
            target.takeDamage(user.getAttack() + 15);
        }
    }
}