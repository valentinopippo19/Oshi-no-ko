package ui;

public interface BattleListener {

    void log(String text);

    void refresh();

    void enableButtons(boolean value);

    void showEndScreen(boolean victory);
}