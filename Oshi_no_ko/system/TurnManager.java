package system;

import model.GameCharacter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TurnManager {

    // ======================================================
    // ORDEN DE TURNOS
    // ======================================================

    private static final List<GameCharacter> turnOrder =
            new ArrayList<>();

    private static int currentIndex = 0;

    // ======================================================
    // INIT
    // ======================================================

    public static void initTurnOrder(
            List<GameCharacter> players,
            List<GameCharacter> enemies
    ) {

        turnOrder.clear();

        turnOrder.addAll(players);

        turnOrder.addAll(enemies);

        turnOrder.removeIf(
                c -> !c.isAlive()
        );

        turnOrder.sort(
                Comparator.comparingInt(
                        GameCharacter::getSpeed
                ).reversed()
        );

        currentIndex = 0;
    }

    // ======================================================
    // NEXT
    // ======================================================

    public static GameCharacter next() {

        if(turnOrder.isEmpty()){

            return null;
        }

        // ==============================================
        // LIMPIAR MUERTOS
        // ==============================================

        turnOrder.removeIf(
                c -> !c.isAlive()
        );

        if(turnOrder.isEmpty()){

            return null;
        }

        // ==============================================
        // REINICIAR INDEX
        // ==============================================

        if(currentIndex >= turnOrder.size()){

            currentIndex = 0;
        }

        GameCharacter character =
                turnOrder.get(currentIndex);

        currentIndex++;

        return character;
    }

    // ======================================================
    // END TURN
    // ======================================================

    public static void endTurn(
            GameCharacter current
    ) {

        turnOrder.removeIf(
                c -> !c.isAlive()
        );

        if(currentIndex >= turnOrder.size()){

            currentIndex = 0;
        }
    }
}