package skills;

import model.GameCharacter;
import model.GameException;

import java.io.Serializable;

public abstract class Skill
        implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;

    protected final int manaCost;

    public Skill(
            String name,
            int manaCost
    ) {

        if (name == null ||
                name.isBlank()) {

            throw new IllegalArgumentException(
                    "La skill debe tener nombre"
            );
        }

        if (manaCost < 0) {

            throw new IllegalArgumentException(
                    "El mana cost no puede ser negativo"
            );
        }

        this.name = name;

        this.manaCost = manaCost;
    }

    // ======================================================
    // ACTION
    // ======================================================

    public abstract void use(
            GameCharacter user,
            GameCharacter target
    ) throws GameException;

    // ======================================================
    // VALIDATION
    // ======================================================

    protected void validateTarget(
            GameCharacter target
    ) throws GameException {

        if (target == null) {

            throw new GameException(
                    "Objetivo inválido"
            );
        }

        if (!target.isAlive()) {

            throw new GameException(
                    "El objetivo está derrotado"
            );
        }
    }

    // ======================================================
    // GETTERS
    // ======================================================

    public String getName() {
        return name;
    }

    public int getManaCost() {
        return manaCost;
    }

    // ======================================================
    // UI
    // ======================================================

    @Override
    public String toString() {

        return name +
                " (" + manaCost + " MP)";
    }
}