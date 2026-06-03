package items;

import model.GameCharacter;

import java.io.Serializable;

public abstract class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;

    public Item(String name) {

        if (name == null || name.isBlank()) {

            throw new IllegalArgumentException(
                    "El item debe tener nombre"
            );
        }

        this.name = name;
    }

    // ======================================================
    // ACTION
    // ======================================================

    public abstract void use(GameCharacter target);

    // ======================================================
    // GETTERS
    // ======================================================

    public String getName() {
        return name;
    }

    // ======================================================
    // UI
    // ======================================================

    @Override
    public String toString() {
        return name;
    }
}