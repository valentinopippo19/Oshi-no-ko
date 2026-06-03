package effects;

import model.GameCharacter;

import java.io.Serializable;

public abstract class StatusEffect
        implements Serializable {

    protected int duration;

    public StatusEffect(int duration) {

        this.duration = duration;
    }

    // ======================================================
    // LIFECYCLE
    // ======================================================

    public void onApply(GameCharacter target) {

    }

    public void onExpire(GameCharacter target) {

    }

    public void onTurnStart(GameCharacter target) {

    }

    // ======================================================
    // STAT MODIFIERS
    // ======================================================

    public int modifyAttack(int value) {
        return value;
    }

    public int modifyDefense(int value) {
        return value;
    }

    // ======================================================
    // DURATION
    // ======================================================

    public void decreaseDuration() {

        duration--;
    }

    public boolean isExpired() {

        return duration <= 0;
    }

    public int getDuration() {
        return duration;
    }

    public abstract String getName();
}