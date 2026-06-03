package system;

import java.io.*;

public class SaveManager {

    private static final String SAVE_FILE =
            "save.dat";

    private SaveManager() {

    }

    // ======================================================
    // SAVE
    // ======================================================

    public static void save(
            GameState gameState
    ) throws IOException {

        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream(
                                     SAVE_FILE
                             )
                     )) {

            out.writeObject(gameState);
        }
    }

    // ======================================================
    // LOAD
    // ======================================================

    public static GameState load()
            throws IOException,
            ClassNotFoundException {

        File file =
                new File(SAVE_FILE);

        if (!file.exists()) {

            return new GameState();
        }

        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(file)
                     )) {

            Object obj =
                    in.readObject();

            if (!(obj instanceof GameState state)) {

                throw new IOException(
                        "Save corrupto"
                );
            }

            return state;
        }
    }
}