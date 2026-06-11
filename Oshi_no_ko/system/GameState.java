package system;

import model.GameCharacter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameState implements Serializable {

    private static final long serialVersionUID = 1L;

    // ======================================================
    // DATOS
    // ======================================================

    private static List<GameCharacter> party =
            new ArrayList<>();

    private static List<GameCharacter> enemies =
            new ArrayList<>();

    // NUEVO: guarda la selección original de enemigos
    private static List<GameCharacter> selectedEnemies =
            new ArrayList<>();

    private static Inventory inventory =
            new Inventory();

    // ======================================================
    // GETTERS
    // ======================================================

    public static List<GameCharacter> getParty() {

        return party;
    }

    public static List<GameCharacter> getEnemies() {

        return enemies;
    }

    public static List<GameCharacter> getSelectedEnemies() {

        return selectedEnemies;
    }

    public static Inventory getInventory() {

        return inventory;
    }

    // ======================================================
    // SETTERS
    // ======================================================

    public static void setParty(
            List<GameCharacter> newParty
    ) {

        party = new ArrayList<>(newParty);
    }

    public static void setEnemies(
            List<GameCharacter> newEnemies
    ) {

        enemies = new ArrayList<>(newEnemies);
    }

    public static void setSelectedEnemies(
            List<GameCharacter> newEnemies
    ) {

        selectedEnemies = new ArrayList<>(newEnemies);
    }

    public static void setInventory(
            Inventory newInventory
    ) {

        inventory = newInventory;
    }

    // ======================================================
    // CLEAR
    // ======================================================

    public static void clear() {

        party.clear();

        enemies.clear();

        selectedEnemies.clear();

        inventory.clear();
    }

    // ======================================================
    // SYNC
    // ======================================================

    public static void sync() {

        party.removeIf(c -> !c.isAlive());

        enemies.removeIf(c -> !c.isAlive());
    }

    // ======================================================
    // HELPERS
    // ======================================================

    public static boolean arePlayersAlive() {

        return party.stream()
                .anyMatch(GameCharacter::isAlive);
    }

    public static boolean areEnemiesAlive() {

        return enemies.stream()
                .anyMatch(GameCharacter::isAlive);
    }
}