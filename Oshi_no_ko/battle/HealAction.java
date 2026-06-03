package battle;

import model.GameCharacter;

public class HealAction implements BattleAction {

    private final GameCharacter healer;

    private final GameCharacter target;

    private final int amount;

    public HealAction(
            GameCharacter healer,
            GameCharacter target,
            int amount
    ) {

        this.healer = healer;

        this.target = target;

        this.amount = amount;
    }

    @Override
    public void execute() {

        target.heal(amount);
    }

    public GameCharacter getHealer() {
        return healer;
    }

    public GameCharacter getTarget() {
        return target;
    }

    public int getAmount() {
        return amount;
    }
}