package system;

import model.GameCharacter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import items.HealItem;
import items.ManaItem;

public class GameState implements Serializable {

    private static final long serialVersionUID = 1L;

    // ======================================================
    // DATOS
    // ======================================================

    private static List<GameCharacter> party =
            new ArrayList<>();

    private static List<GameCharacter> enemies =
            new ArrayList<>();

    // Guarda la selección original de enemigos
    private static List<GameCharacter> selectedEnemies =
            new ArrayList<>();

    private static Inventory inventory =
            new Inventory();

    public static void rebuildEnemiesFromSelection() {
        enemies = new ArrayList<>(selectedEnemies);
    }

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
            List<GameCharacter> newParty) {

        party = new ArrayList<>(newParty);
    }

    public static void setEnemies(
            List<GameCharacter> newEnemies) {

        enemies = new ArrayList<>(newEnemies);
    }

    public static void setSelectedEnemies(
            List<GameCharacter> newEnemies) {

        selectedEnemies = new ArrayList<>(newEnemies);
    }

    public static void setInventory(
            Inventory newInventory) {

        inventory = newInventory;
    }

    // ======================================================
    // RESTORE (PARA CARGAR PARTIDAS)
    // ======================================================

    public static void restore(
            List<GameCharacter> savedParty,
            List<GameCharacter> savedEnemies,
            List<GameCharacter> savedSelectedEnemies,
            Inventory savedInventory) {

        party = new ArrayList<>(savedParty);

        enemies = new ArrayList<>(savedEnemies);

        selectedEnemies =
                new ArrayList<>(savedSelectedEnemies);

        inventory = savedInventory;
    }

    public static void initializeInventory() {

        inventory = new Inventory();

        inventory.add(new HealItem("Poción", 30));
        inventory.add(new HealItem("Poción", 30));
        inventory.add(new ManaItem("Éter", 20));
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

    public static boolean isBattleFinished() {

        return !arePlayersAlive()
                || !areEnemiesAlive();
    }
}