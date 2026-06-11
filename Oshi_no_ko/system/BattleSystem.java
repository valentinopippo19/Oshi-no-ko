package system;

import items.Item;
import model.*;
import skills.Skill;
import ui.GameUI;

import java.util.List;

public class BattleSystem {

    private static GameCharacter current;

    private static GameUI ui;

    private static int totalBattleExp = 0;

    // =====================================================
    // UI
    // =====================================================

    public static void setUI(GameUI gameUI){

        ui = gameUI;
    }

    public static GameCharacter getCurrentCharacter(){

        return current;
    }

    // =====================================================
    // START BATTLE
    // =====================================================

    public static void startBattle(){

        totalBattleExp = 0;

        if(GameState.getEnemies().isEmpty()){

            GameState.getEnemies().addAll(
                    EnemyFactory.createEnemies()
            );
        }

        TurnManager.initTurnOrder(
                GameState.getParty(),
                GameState.getEnemies()
        );

        nextTurn();
    }

    // =====================================================
    // NEXT TURN
    // =====================================================

    public static void nextTurn(){

        GameState.sync();

        if(isBattleOver()){

            finishBattle();

            return;
        }

        current = TurnManager.next();

        if(current == null || !current.isAlive()){

            nextTurn();

            return;
        }

        current.applyStatusEffects();

        if(current instanceof PlayerCharacter){

            ui.enableButtons(true);

            ui.log(
                    "Turno de " +
                    current.getName()
            );

        } else {

            current.act();

            endTurn();
        }

        ui.refresh();
    }

    // =====================================================
    // ATTACK
    // =====================================================

    public static void attack(
            GameCharacter target
    ){

        if(current == null || target == null){

            ui.log("Acción inválida");

            return;
        }

        target.takeDamage(
                current.getAttack()
        );

        ui.log(
                current.getName()
                + " atacó a "
                + target.getName()
        );

        removeIfDead(target);

        endTurn();
    }

    // =====================================================
    // SKILL
    // =====================================================

    public static void useSkill(
            Skill skill,
            GameCharacter target
    ){

        if(skill == null || current == null){

            ui.log("Acción inválida");

            return;
        }

        try {

            if(target == null){

                target = current;
            }

            skill.use(current, target);

            ui.log(
                    current.getName()
                    + " usó "
                    + skill.getName()
            );

            removeIfDead(target);

            endTurn();

        } catch (GameException e){

            ui.log(e.getMessage());
        }
    }

    // =====================================================
    // DEFEND
    // =====================================================

    public static void defend(){

        if(current == null){
            return;
        }

        current.defend();

        ui.log(
                current.getName()
                + " se defendió"
        );

        endTurn();
    }

    // =====================================================
    // ITEM
    // =====================================================

    public static void useItem(
            Item item,
            GameCharacter target
    ){

        if(item == null || target == null){

            ui.log("Item inválido");

            return;
        }

        item.use(target);

        ui.log(
                current.getName()
                + " usó "
                + item.getName()
                + " sobre "
                + target.getName()
        );

        GameState.getInventory()
                .remove(item);

        removeIfDead(target);

        endTurn();
    }

    // =====================================================
    // REMOVE DEAD
    // =====================================================

    private static void removeIfDead(
            GameCharacter target
    ){

        if(target == null){
            return;
        }

        if(!target.isAlive()){

            // =============================================
            // EXP
            // =============================================

            if(target instanceof EnemyCharacter e){

                totalBattleExp +=
                        e.getExpReward();
            }

            GameState.getEnemies()
                    .remove(target);

            GameState.getParty()
                    .remove(target);

            ui.log(
                    target.getName()
                    + " fue derrotado"
            );
        }
    }

    // =====================================================
    // END TURN
    // =====================================================

    private static void endTurn(){

        TurnManager.endTurn(current);

        ui.enableButtons(false);

        ui.refresh();

        nextTurn();
    }

    // =====================================================
    // BATTLE OVER
    // =====================================================

    public static boolean isBattleOver(){

        boolean playersAlive =
                GameState.getParty()
                .stream()
                .anyMatch(
                        GameCharacter::isAlive
                );

        boolean enemiesAlive =
                GameState.getEnemies()
                .stream()
                .anyMatch(
                        GameCharacter::isAlive
                );

        return !playersAlive
                || !enemiesAlive;
    }

    // =====================================================
    // FINISH BATTLE
    // =====================================================

    private static void finishBattle(){

        boolean playerWon =
                GameState.getParty()
                .stream()
                .anyMatch(GameCharacter::isAlive);

        if(playerWon){

            rewardExperience();

            // Restaura la selección original de enemigos
            GameState.setEnemies(
                    GameState.getSelectedEnemies()
            );

            ui.showEndScreen(true);

        } else {

            ui.showEndScreen(false);
        }
    }

    // =====================================================
    // EXP
    // =====================================================

    private static void rewardExperience(){

        for(GameCharacter player :
                GameState.getParty()){

            if(player instanceof PlayerCharacter p
                    && p.isAlive()){

                p.gainExp(totalBattleExp);
            }
        }

        ui.log(
                "EXP obtenida: "
                + totalBattleExp
        );
    }

    // =====================================================
    // HELPERS
    // =====================================================

    public static List<GameCharacter>
    getAlivePlayers(){

        return GameState.getParty()
                .stream()
                .filter(
                        GameCharacter::isAlive
                )
                .toList();
    }

    public static List<GameCharacter>
    getAliveEnemies(){

        return GameState.getEnemies()
                .stream()
                .filter(
                        GameCharacter::isAlive
                )
                .toList();
    }

    public static GameUI getUI() {
        return ui;
    }
}