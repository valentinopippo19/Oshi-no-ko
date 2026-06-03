package battle;

import model.GameCharacter;
import model.GameException;
import skills.Skill;

public class SkillAction
        implements BattleAction {

    private final Skill skill;

    private final GameCharacter user;

    private final GameCharacter target;

    public SkillAction(
            Skill skill,
            GameCharacter user,
            GameCharacter target
    ) {

        this.skill = skill;
        this.user = user;
        this.target = target;
    }

    @Override
    public void execute() {

        try {

            skill.use(user, target);

        } catch (GameException e) {

            throw new RuntimeException(e);
        }
    }
}