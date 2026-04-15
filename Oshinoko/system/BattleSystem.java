package system;

import model.*;
import ui.GameUI;
import java.util.*;

public class BattleSystem {

    public static GameCharacter current;
    public static GameCharacter selectedTarget;
    public static GameUI ui;

    public static void setUI(GameUI gameUI){
        ui = gameUI;
    }

    public static void startBattle(){

        if(GameState.enemies.isEmpty()){
            GameState.enemies.addAll(EnemyFactory.createEnemies());
            System.out.println("Enemies generados: " + GameState.enemies.size());
        } else {
            System.out.println("Enemies cargados: " + GameState.enemies.size());
        }

        TurnManager.initTurnOrder();
        nextTurn();
    }

    public static void nextTurn(){

        current = TurnManager.next();

        if(current == null){
            ui.showEndScreen(false);
            return;
        }

        current.applyStatusEffects();

        if(GameState.party.contains(current)){
            selectedTarget = ui.selectedEnemy;
            ui.log("Turno de " + current.name);

            ui.enableButtons(true);
            ui.refresh();
        } else {
            enemyAI(current);
        }
    }

    public static void attack(){

        if(ui.selectedEnemy == null){
            ui.log("Seleccioná un enemigo!");
            ui.enableButtons(true);
            return;
        }

        GameCharacter target = ui.selectedEnemy;

        int before = target.getHp();
        target.takeDamage(current.attack);
        int dmg = before - target.getHp();

        ui.log(current.name + " atacó a " + target.name + " (-" + dmg + " HP)");

        if(!target.isAlive()){
            ui.log(target.name + " fue derrotado!");

            if(!GameState.enemies.isEmpty()){
                ui.selectedEnemy = GameState.enemies.stream()
                        .filter(GameCharacter::isAlive)
                        .findFirst()
                        .orElse(null);
            } else {
                ui.selectedEnemy = null;
            }
        }

        endTurn();
    }

    public static void skill(){

        if(current.skills.isEmpty()){
            ui.log(current.name + " no tiene skills!");
            ui.enableButtons(true);
            return;
        }

        Skill s = current.skills.get(0); // simplificado

        if(current.mana < s.manaCost){
            ui.log("No tenés mana!");
            ui.enableButtons(true);
            return;
        }

        GameCharacter target = ui.selectedEnemy;

        try {
            s.use(current, target);
        } catch (GameException e) {
            ui.log(e.getMessage());
            ui.enableButtons(true);
            return;
        }

        ui.log(current.name + " usó " + s.name);
        endTurn();
    }

    public static void defend(){
        current.defend();
        ui.log(current.name + " se defiende");
        endTurn();
    }

    private static void enemyAI(GameCharacter enemy){

        List<GameCharacter> vivos = GameState.party.stream()
                .filter(GameCharacter::isAlive)
                .toList();

        if(vivos.isEmpty()){
            endTurn();
            return;
        }

        GameCharacter target = vivos.get(new Random().nextInt(vivos.size()));

        int before = target.getHp();
        target.takeDamage(enemy.attack);
        int dmg = before - target.getHp();

        ui.log(enemy.name + " atacó a " + target.name + " (-" + dmg + " HP)");

        endTurn();
    }

    private static void endTurn(){

        // 🔥 victoria
        if(GameState.enemies.stream().noneMatch(GameCharacter::isAlive)){

            for(GameCharacter c : GameState.party){
                if(c.isAlive()){
                    c.gainExp(50);
                }
            }

            ui.log("¡Ganaste!");
            GameState.save(); // ✔ ahora sí guardar acá
            ui.showEndScreen(true);
            return;
        }

        // 🔥 derrota
        if(GameState.party.stream().noneMatch(GameCharacter::isAlive)){
            ui.showEndScreen(false);
            return;
        }

        TurnManager.endTurn(current);

        ui.enableButtons(false);
        ui.refresh();

        nextTurn();
    }

    public static boolean isBattleOver(){
        return GameState.party.stream().noneMatch(GameCharacter::isAlive)
            || GameState.enemies.stream().noneMatch(GameCharacter::isAlive);
    }

    public static boolean playerWon(){
        return GameState.party.stream().anyMatch(GameCharacter::isAlive);
    }

    public static Object useItem() {
        throw new UnsupportedOperationException("Unimplemented method 'useItem'");
    }
}