package system;

import items.HealItem;
import items.Item;
import items.ManaItem;
import model.*;
import skills.Skill;
import battle.DefendAction;
import ui.BattleListener;

import java.util.List;
import java.util.Random;

import javax.swing.SwingUtilities;

public class BattleSystem {

    private static GameCharacter current;
    private static BattleListener ui;
    private static int totalBattleExp = 0;

    // =====================================================
    // UI
    // =====================================================

    public static void setUI(BattleListener ui) {
        BattleSystem.ui = ui;
    }

    public static GameCharacter getCurrentCharacter() {
        return current;
    }

    // =====================================================
    // START BATTLE
    // =====================================================

    public static void startBattle() {

        totalBattleExp = 0;

        if (GameState.getEnemies().isEmpty()) {
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

    public static void nextTurn() {

        GameState.sync();

        if (isBattleOver()) {
            finishBattle();
            return;
        }

        current = TurnManager.next();

        if (current == null || !current.isAlive()) {
            nextTurn();
            return;
        }

        current.applyStatusEffects();

        if (current instanceof PlayerCharacter) {

            ui.enableButtons(true);

            ui.log("Turno de " + current.getName());

            ui.refresh();
            return;
        }

        // TURNOS ENEMIGOS
        executeEnemyTurn();

        endTurn();
    }

    // =====================================================
    // ENEMY TURN
    // =====================================================

    private static void executeEnemyTurn() {

        if (current instanceof HealerEnemy healer) {

            healer.act(
                    GameState.getEnemies(),
                    GameState.getParty()
            ).execute();

        } else {
            current.act();
        }
    }

    // =====================================================
    // ATTACK
    // =====================================================

    public static void attack(GameCharacter target) {

        if (current == null || target == null) {
            ui.log("Acción inválida");
            return;
        }

        target.takeDamage(current.getAttack());

        ui.log(current.getName() + " atacó a " + target.getName());

        removeIfDead(target);

        endTurn();
    }

    // =====================================================
    // SKILL
    // =====================================================

    public static void useSkill(Skill skill, GameCharacter target) {

        if (skill == null || current == null) {
            ui.log("Acción inválida");
            return;
        }

        try {

            if (target == null) {
                target = current;
            }

            skill.use(current, target);

            ui.log(current.getName() + " usó " + skill.getName());

            removeIfDead(target);

            endTurn();

        } catch (GameException e) {
            ui.log(e.getMessage());
        }
    }

    // =====================================================
    // DEFEND
    // =====================================================

    public static void defend() {

        if (current == null) return;

        new DefendAction(current).execute();

        ui.log(current.getName() + " se defendió");

        endTurn();
    }

    // =====================================================
    // ITEM
    // =====================================================

    public static void useItem(Item item, GameCharacter target) {

        if (item == null || target == null) {
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

        GameState.getInventory().remove(item);

        removeIfDead(target);

        endTurn();
    }

    // =====================================================
    // REMOVE DEAD
    // =====================================================

    private static void removeIfDead(GameCharacter target) {

        if (target == null) return;

        if (!target.isAlive()) {

            if (target instanceof EnemyCharacter e) {
                totalBattleExp += e.getExpReward();
            }

            GameState.getEnemies().remove(target);
            GameState.getParty().remove(target);

            ui.log(target.getName() + " fue derrotado");

            ui.refresh();

            // Verificar inmediatamente si terminó la batalla
            if (isBattleOver()) {
                finishBattle();
            }
        }
    }

    // =====================================================
    // END TURN
    // =====================================================

    private static void endTurn() {

        GameState.sync();

        ui.refresh();

        if (isBattleOver()) {
            finishBattle();
            return;
        }

        TurnManager.endTurn(current);

        ui.enableButtons(false);

        SwingUtilities.invokeLater(BattleSystem::nextTurn);
    }

    // =====================================================
    // BATTLE OVER
    // =====================================================

    public static boolean isBattleOver() {

        boolean playersAlive = GameState.getParty()
                .stream()
                .anyMatch(GameCharacter::isAlive);

        boolean enemiesAlive = GameState.getEnemies()
                .stream()
                .anyMatch(GameCharacter::isAlive);

        System.out.println("Jugadores vivos: " + playersAlive);
        System.out.println("Enemigos vivos: " + enemiesAlive);
        System.out.println("Cantidad enemigos: "
                + GameState.getEnemies().size());

        return !playersAlive || !enemiesAlive;
    }

    // =====================================================
    // FINISH BATTLE
    // =====================================================

    private static void finishBattle() {

        System.out.println("=== FINISH BATTLE ===");

        boolean playersAlive = GameState.getParty()
                .stream()
                .anyMatch(GameCharacter::isAlive);

        boolean enemiesAlive = GameState.getEnemies()
                .stream()
                .anyMatch(GameCharacter::isAlive);

        System.out.println("Players vivos: " + playersAlive);
        System.out.println("Enemies vivos: " + enemiesAlive);
        System.out.println("Party size: " + GameState.getParty().size());
        System.out.println("Enemies size: " + GameState.getEnemies().size());

        if (!enemiesAlive) {

            System.out.println("VICTORIA");

            rewardExperience();
            ui.showEndScreen(true);
            return;
        }

        if (!playersAlive) {

            System.out.println("DERROTA");

            ui.showEndScreen(false);
        }
    }

    // =====================================================
    // EXP
    // =====================================================

    private static void rewardExperience() {

        ui.log("=== RECOMPENSAS ===");
        ui.log("EXP total obtenida: " + totalBattleExp);

        for (GameCharacter p : GameState.getParty()) {

            int nivelAnterior = p.getLevel();
            int atkAnterior = p.getAttack();
            int defAnterior = p.getDefense();
            int hpAnterior = p.getMaxHp();

            p.gainExp(totalBattleExp);

            ui.log(p.getName() + " ganó "
                    + totalBattleExp + " EXP");

            if (p.getLevel() > nivelAnterior) {

                ui.log(
                    p.getName()
                    + " subió al nivel "
                    + p.getLevel()
                );

                ui.log(
                    "ATK: " + atkAnterior
                    + " -> " + p.getAttack()
                );

                ui.log(
                    "DEF: " + defAnterior
                    + " -> " + p.getDefense()
                );

                ui.log(
                    "HP Máx: " + hpAnterior
                    + " -> " + p.getMaxHp()
                );
            }
        }

        Random random = new Random();

        int opcion = random.nextInt(2);

        if(opcion == 0){

            GameState.getInventory().add(
                    new HealItem("Poción",50));

            ui.log("Obtienes una Poción.");
        }
        else{

            GameState.getInventory().add(
                    new ManaItem("Éter",30));

            ui.log("Obtienes un Éter.");
        }
    }

    // =====================================================
    // HELPERS
    // =====================================================

    public static List<GameCharacter> getAlivePlayers() {
        return GameState.getParty()
                .stream()
                .filter(GameCharacter::isAlive)
                .toList();
    }

    public static List<GameCharacter> getAliveEnemies() {
        return GameState.getEnemies()
                .stream()
                .filter(GameCharacter::isAlive)
                .toList();
    }

    public static BattleListener getUI() {
        return ui;
    }
}