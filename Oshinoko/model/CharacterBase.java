package model;
import java.io.Serializable;

public abstract class CharacterBase implements Serializable {
    protected String name;
    protected int hp, attack;

    public abstract void specialAbility(GameCharacter target);
}