package main;

import java.io.IOException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import system.SaveManager;
import ui.GameUI;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {

                // =========================
                // LOOK AND FEEL
                // =========================

                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName()
                );

            } catch (Exception e) {

                e.printStackTrace();
            }

            // =========================
            // CARGAR PARTIDA
            // =========================

            try {
                SaveManager.load();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }

            // =========================
            // INICIAR JUEGO
            // =========================

            new GameUI();
        });
    }
}