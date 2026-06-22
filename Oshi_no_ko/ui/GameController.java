package ui;

import java.util.ArrayList;
import java.util.List;

import items.Item;
import items.HealItem;
import items.ManaItem;
import items.BuffItem;

import model.GameCharacter;
import skills.Skill;
import system.*;

public class GameController {

    private final GameUI ui;

    public GameController(GameUI ui) {
        this.ui = ui;
    }

    // ==================================================
    // NUEVA PARTIDA
    // ==================================================

    public void startNewGame() {

        GameState.clear();
        initializeInventory();

        ui.characterSelectionScreen();

        Inventory inventory = GameState.getInventory();

        inventory.add(
                new HealItem("Poción", 30)
        );

        inventory.add(
                new HealItem("Poción", 30)
        );

        inventory.add(
                new ManaItem("Éter", 20)
        );

        inventory.add(
                new BuffItem("Fuerza", 5, 0)
        );
    }

    private void initializeInventory() {

        GameState.getInventory().add(new HealItem("Poción HP", 50));
        GameState.getInventory().add(new HealItem("Poción HP", 50));
        GameState.getInventory().add(new ManaItem("Poción MP", 30));
        GameState.getInventory().add(new BuffItem("Buff ATK", 10, 3));
    }

    // ==================================================
    // CARGAR PARTIDA
    // ==================================================

    public void loadGame() {

        try {
            boolean ok = SaveManager.load();

            if (!ok) {
                ui.log("No hay partida guardada");
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            ui.log("Error al cargar partida");
            return;
        }

        if (GameState.getParty().isEmpty()) {
            ui.log("Partida vacía o corrupta");
            return;
        }

        
        GameState.rebuildEnemiesFromSelection();

        ui.showBattleScreen();
    }

    // ==================================================
    // BATALLA
    // ==================================================

    public void startBattle(List<GameCharacter> party,
                        List<GameCharacter> enemies) {

        GameState.setParty(party);

       
        GameState.setSelectedEnemies(new ArrayList<>(enemies));
        GameState.setEnemies(new ArrayList<>(enemies));

        ui.showBattleScreen();
    }

    public void saveGame() {
        try {
            SaveManager.save();
            ui.log("Partida guardada");
        } catch (Exception e) {
            e.printStackTrace();
            ui.log("Error al guardar");
        }
    }

    // ==================================================
    // ACCIONES
    // ==================================================

    public void attack(GameCharacter target) {
        BattleSystem.attack(target);
    }

    public void defend() {
        BattleSystem.defend();
    }

    public void useSkill(Skill skill, GameCharacter target) {
        BattleSystem.useSkill(skill, target);
    }

    public void useItem(Item item, GameCharacter target) {
        BattleSystem.useItem(item, target);
    }

    // ==================================================
    // UTIL
    // ==================================================

    public GameCharacter getCurrentCharacter() {
        return BattleSystem.getCurrentCharacter();
    }
}