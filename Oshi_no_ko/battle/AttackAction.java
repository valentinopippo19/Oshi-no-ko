package battle;

import model.GameCharacter;

public class AttackAction implements BattleAction {

    private final GameCharacter attacker;

    private final GameCharacter target;

    public AttackAction(
            GameCharacter attacker,
            GameCharacter target
    ) {

        this.attacker = attacker;

        this.target = target;
    }

    @Override
    public void execute() {

        target.takeDamage(attacker.getAttack());
    }

    public GameCharacter getAttacker() {
        return attacker;
    }

    public GameCharacter getTarget() {
        return target;
    }
}