package system;

import model.*;
import java.util.*;

// =====================
// TURN MANAGER
// =====================

public class TurnManager {
    private static Queue<GameCharacter> queue = new LinkedList<>();

    public static void initTurnOrder(){
        List<GameCharacter> all = new ArrayList<>();
        all.addAll(GameState.party);
        all.addAll(GameState.enemies);
        all.sort((a,b)->b.speed-a.speed);

        queue.clear();
        queue.addAll(all);
    }

    public static GameCharacter next(){
        while(!queue.isEmpty()){
            GameCharacter c = queue.poll();
            if(c.isAlive()) return c;
        }
        initTurnOrder();
        return next();
    }

    public static void endTurn(GameCharacter c){
        if(c.isAlive()) queue.offer(c);
    }
}