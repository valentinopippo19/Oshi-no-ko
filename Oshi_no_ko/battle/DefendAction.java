package battle;

import effects.DefendEffect;
import model.GameCharacter;

public class DefendAction
        implements BattleAction {

    private final GameCharacter target;

    public DefendAction(
            GameCharacter target
    ) {

        this.target = target;
    }

    @Override
    public void execute() {

        target.addEffect(
                new DefendEffect(1, 10)
        );
    }
}