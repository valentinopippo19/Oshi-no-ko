package system;

import model.GameCharacter;

import java.io.*;
import java.util.List;

public class SaveManager {

    private static final String SAVE_FILE = "save.dat";

    private SaveManager() {
    }

    // ======================================================
    // SAVE
    // ======================================================

    public static void save() throws IOException {

        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream(SAVE_FILE))) {

            out.writeObject(GameState.getParty());
            out.writeObject(GameState.getEnemies());
            out.writeObject(GameState.getSelectedEnemies());
            out.writeObject(GameState.getInventory());
        }
    }

    // ======================================================
    // LOAD
    // ======================================================

    @SuppressWarnings("unchecked")
    public static boolean load()
            throws IOException, ClassNotFoundException {

        File file = new File(SAVE_FILE);

        if (!file.exists()) {
            return false;
        }

        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(file))) {

            List<GameCharacter> party =
                    (List<GameCharacter>) in.readObject();

            List<GameCharacter> enemies =
                    (List<GameCharacter>) in.readObject();

            List<GameCharacter> selectedEnemies =
                    (List<GameCharacter>) in.readObject();

            Inventory inventory =
                    (Inventory) in.readObject();

            GameState.restore(
                    party,
                    enemies,
                    selectedEnemies,
                    inventory
            );

            return true;
        }
    }
}