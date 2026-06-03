package system;

import items.Item;
import model.GameCharacter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Inventory
        implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Item> items;

    public Inventory() {

        this.items =
                new ArrayList<>();
    }

    // ======================================================
    // ADD
    // ======================================================

    public void add(Item item) {

        if (item == null) {

            throw new IllegalArgumentException(
                    "El item no puede ser null"
            );
        }

        items.add(item);
    }

    // ======================================================
    // USE
    // ======================================================

    public void useItem(
            Item item,
            GameCharacter target
    ) {

        if (item == null) {

            throw new IllegalArgumentException(
                    "Item inválido"
            );
        }

        if (!items.contains(item)) {

            throw new IllegalArgumentException(
                    "El item no está en inventario"
            );
        }

        item.use(target);

        remove(item);
    }

    // ======================================================
    // REMOVE
    // ======================================================

    public void remove(Item item) {

        items.remove(item);
    }

    // ======================================================
    // CLEAR
    // ======================================================

    public void clear() {

        items.clear();
    }

    // ======================================================
    // GETTERS
    // ======================================================

    public List<Item> getItems() {

        return Collections.unmodifiableList(
                items
        );
    }

    public boolean isEmpty() {

        return items.isEmpty();
    }

    public int size() {

        return items.size();
    }
}