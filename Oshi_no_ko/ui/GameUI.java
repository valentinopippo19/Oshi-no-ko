package ui;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import javax.swing.*;

import items.Item;
import model.GameCharacter;
import model.PlayerCharacter;
import skills.Skill;
import system.*;

public class GameUI extends JFrame implements BattleListener {

    private JPanel mainPanel;
    private GameController controller;

    private JPanel enemyPanel;
    private JPanel playerPanel;
    private JTextArea logArea;

    private JButton attackBtn;
    private JButton skillBtn;
    private JButton defendBtn;
    private JButton itemBtn;

    private GameCharacter selectedTarget;

    public GameUI() {

        setTitle("OSHI NO BATTLE RPG");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        controller = new GameController(this);

        showStartScreen();

        setVisible(true);
    }

    // ==================================================
    // START SCREEN
    // ==================================================
    public void showStartScreen() {

        mainPanel.removeAll();

        BackgroundPanel bg =
            new BackgroundPanel("resources/portada.jpg");

        bg.setLayout(new BorderLayout());

        JLabel title = new JLabel("OSHI NO BATTLE RPG", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(Color.WHITE);

        JPanel menu = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 200));
        menu.setOpaque(false);

        JButton newGame = new JButton("NUEVA PARTIDA");
        JButton loadGame = new JButton("CARGAR PARTIDA");

        newGame.setPreferredSize(new Dimension(200, 80));
        loadGame.setPreferredSize(new Dimension(200, 80));

        newGame.addActionListener(e -> controller.startNewGame());
        loadGame.addActionListener(e -> controller.loadGame());

        menu.add(newGame);
        menu.add(loadGame);

        bg.add(title, BorderLayout.NORTH);
        bg.add(menu, BorderLayout.CENTER);

        mainPanel.add(bg);

        refreshMain();
    }

    // ==================================================
    // CHARACTER SELECTION
    // ==================================================
    public void characterSelectionScreen() {

        mainPanel.removeAll();

        BackgroundPanel bg =
            new BackgroundPanel("resources/fondoSeleccion.jpg");

        bg.setLayout(new BorderLayout());

        List<GameCharacter> allPlayers =
                CharacterFactory.createAllCharacters();

        List<GameCharacter> allEnemies =
                EnemyFactory.createEnemies();

        JPanel allyPanel = createSelectionPanel("ALIADOS", allPlayers);
        JPanel enemyPanelUI = createSelectionPanel("ENEMIGOS", allEnemies);

        JPanel center = new JPanel(new GridLayout(1, 2, 20, 0));
        center.setOpaque(false);

        center.add(allyPanel);
        center.add(enemyPanelUI);

        JButton startBattle = new JButton("INICIAR BATALLA");

        startBattle.addActionListener(e -> {

            List<GameCharacter> party = new ArrayList<>();
            List<GameCharacter> enemies = new ArrayList<>();

            collectSelectedCharacters(allyPanel, party);
            collectSelectedCharacters(enemyPanelUI, enemies);

            if (party.isEmpty() || enemies.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selección inválida");
                return;
            }

            controller.startBattle(party, enemies);
        });

        bg.add(center, BorderLayout.CENTER);
        bg.add(startBattle, BorderLayout.SOUTH);

        mainPanel.add(bg);

        refreshMain();
    }

    // ==================================================
    // BATTLE SCREEN
    // ==================================================
    public void showBattleScreen() {

        mainPanel.removeAll();

        BackgroundPanel bg =
            new BackgroundPanel("resources/fondo.jpg");

        bg.setLayout(new BorderLayout());

        enemyPanel = new JPanel(new FlowLayout());
        playerPanel = new JPanel(new FlowLayout());

        enemyPanel.setOpaque(false);
        playerPanel.setOpaque(false);

        logArea = new JTextArea();
        logArea.setEditable(false);

        bg.add(enemyPanel, BorderLayout.EAST);
        bg.add(playerPanel, BorderLayout.WEST);
        bg.add(new JScrollPane(logArea), BorderLayout.SOUTH);

        JPanel actions = new JPanel(new FlowLayout());
        JButton saveBtn = new JButton("GUARDAR");

        saveBtn.addActionListener(e -> controller.saveGame());

        actions.add(saveBtn);

        attackBtn = new JButton("ATACAR");
        skillBtn = new JButton("SKILL");
        defendBtn = new JButton("DEFENDER");
        itemBtn = new JButton("ITEM");

        attackBtn.addActionListener(e -> controller.attack(selectedTarget));
        defendBtn.addActionListener(e -> controller.defend());
        skillBtn.addActionListener(e -> openSkillMenu());
        itemBtn.addActionListener(e -> openInventoryMenu());

        actions.add(attackBtn);
        actions.add(skillBtn);
        actions.add(defendBtn);
        actions.add(itemBtn);

        bg.add(actions, BorderLayout.SOUTH);

        mainPanel.add(bg);

        refreshMain();

        BattleSystem.setUI(this);
        refresh();
        BattleSystem.startBattle();
    }

    // ==================================================
    // SELECTION PANEL (FIX REAL)
    // ==================================================
    private JPanel createSelectionPanel(String title, List<GameCharacter> chars) {

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);

        JLabel label = new JLabel(title, JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(Color.WHITE);

        JPanel grid = new JPanel(new GridLayout(0, 1, 10, 10));
        grid.setOpaque(false);

        for (GameCharacter c : chars) {
            grid.add(createSelectionCard(c));
        }

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        wrapper.add(label, BorderLayout.NORTH);
        wrapper.add(scroll, BorderLayout.CENTER);

        return wrapper;
    }

    private JPanel createSelectionCard(GameCharacter c) {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setOpaque(false);
        panel.setBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2)
        );

        JLabel name = new JLabel(
                c.getName(),
                JLabel.CENTER
        );

        name.setForeground(Color.WHITE);
        name.setFont(new Font("Arial", Font.BOLD, 16));

        String imagePath = getCharacterImage(c.getName());

        ImageIcon icon = new ImageIcon(imagePath);

        Image img = icon.getImage().getScaledInstance(
                120,
                120,
                Image.SCALE_SMOOTH
        );

        JLabel imageLabel =
                new JLabel(new ImageIcon(img));

        JTextArea stats = new JTextArea(
                "HP: " + c.getMaxHp()
                + "\nATK: " + c.getAttack()
                + "\nDEF: " + c.getDefense()
        );

        stats.setEditable(false);
        stats.setOpaque(false);
        stats.setForeground(Color.black);

        JToggleButton toggle =
                new JToggleButton("SELECCIONAR");

        panel.putClientProperty("character", c);
        panel.putClientProperty("toggle", toggle);

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);

        center.add(imageLabel, BorderLayout.NORTH);
        center.add(stats, BorderLayout.CENTER);

        panel.add(name, BorderLayout.NORTH);
        panel.add(center, BorderLayout.CENTER);
        panel.add(toggle, BorderLayout.SOUTH);

        return panel;
    }

    private String getCharacterImage(String name) {

        name = name.toLowerCase();

        if (name.contains("ai"))
            return "resources/ai.png";

        if (name.contains("akane"))
            return "resources/akane.png";

        if (name.contains("kana"))
            return "resources/kana.png";

        if (name.contains("ruby"))
            return "resources/ruby.png";

        if (name.contains("mem"))
            return "resources/mem.png";

        if (name.contains("miyako"))
            return "resources/miyako.png";

        return "resources/ai.png";
    }

    private void collectSelectedCharacters(
            JPanel wrapper,
            List<GameCharacter> result
    ) {

        JScrollPane scroll = (JScrollPane) wrapper.getComponent(1);
        JPanel grid = (JPanel) scroll.getViewport().getView();

        for (Component comp : grid.getComponents()) {

            JPanel card = (JPanel) comp;

            JToggleButton btn =
                    (JToggleButton) card.getClientProperty("toggle");

            if (btn != null && btn.isSelected()) {

                GameCharacter c =
                        (GameCharacter) card.getClientProperty("character");

                result.add(c);
            }
        }
    }

    // ==================================================
    // INVENTORY
    // ==================================================
    private void openInventoryMenu() {

        List<Item> items = GameState.getInventory().getItems();
        if (items.isEmpty()) return;

        Item selected = (Item) JOptionPane.showInputDialog(
                this,
                "Seleccioná item",
                "Inventario",
                JOptionPane.PLAIN_MESSAGE,
                null,
                items.toArray(new Item[0]),
                items.get(0)
        );

        if (selected == null || selectedTarget == null) return;

        controller.useItem(selected, selectedTarget);
    }

    // ==================================================
    // SKILLS
    // ==================================================
    private void openSkillMenu() {

        GameCharacter current =
                BattleSystem.getCurrentCharacter();

        if (current == null) return;

        List<Skill> skills = current.getSkills();
        if (skills.isEmpty()) return;

        Skill selected = (Skill) JOptionPane.showInputDialog(
                this,
                "Seleccioná skill",
                "Skills",
                JOptionPane.PLAIN_MESSAGE,
                null,
                skills.toArray(new Skill[0]),
                skills.get(0)
        );

        if (selected == null) return;

        controller.useSkill(selected, selectedTarget);
    }

    // ==================================================
    // BATTLE UI
    // ==================================================
    public void enableButtons(boolean enabled) {

        if (attackBtn != null) attackBtn.setEnabled(enabled);
        if (skillBtn != null) skillBtn.setEnabled(enabled);
        if (defendBtn != null) defendBtn.setEnabled(enabled);
        if (itemBtn != null) itemBtn.setEnabled(enabled);
    }

    private JPanel createBattleCard(GameCharacter c) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JButton button = new JButton();

        button.addActionListener(
                e -> selectedTarget = c);

        ImageIcon icon =
                new ImageIcon(
                        getCharacterImage(c.getName()));

        Image img =
                icon.getImage().getScaledInstance(
                        90,
                        90,
                        Image.SCALE_SMOOTH);

        button.setIcon(new ImageIcon(img));

        button.setVerticalTextPosition(
                SwingConstants.BOTTOM);

        button.setHorizontalTextPosition(
                SwingConstants.CENTER);

        String extra = "";

        if (c instanceof PlayerCharacter p) {
            extra =
                "<br>LVL: " + p.getLevel()
                + "<br>EXP: " + p.getExp() + "/" + p.getExpToLevel();
        }

        button.setText(
            "<html><center>"
            + c.getName()
            + extra
            + "<br>HP: " + c.getHp() + "/" + c.getMaxHp()
            + "<br>ATK: " + c.getAttack()
            + "<br>DEF: " + c.getDefense()
            + "</center></html>"
        );

        JProgressBar hpBar =
                new JProgressBar(
                        0,
                        c.getMaxHp());

        hpBar.setValue(c.getHp());
        hpBar.setStringPainted(true);

        panel.add(button, BorderLayout.CENTER);
        panel.add(hpBar, BorderLayout.SOUTH);

        return panel;
    }

    public void refresh() {

        if (playerPanel == null || enemyPanel == null)
            return;

        playerPanel.removeAll();
        enemyPanel.removeAll();

        for (GameCharacter c : GameState.getParty()) {

            if (c.isAlive()) {
                playerPanel.add(createBattleCard(c));
            }
        }

        for (GameCharacter c : GameState.getEnemies()) {

            if (c.isAlive()) {
                enemyPanel.add(createBattleCard(c));
            }
        }

        playerPanel.revalidate();
        enemyPanel.revalidate();

        playerPanel.repaint();
        enemyPanel.repaint();
    }

    // ==================================================
    // END SCREEN
    // ==================================================
    public void showEndScreen(boolean win) {

        System.out.println("SHOW END SCREEN");

        mainPanel.removeAll();

        BackgroundPanel bg =
                new BackgroundPanel("resources/contratapa.jpg");

        bg.setLayout(new BorderLayout());

        JLabel result =
                new JLabel(
                        win ? "¡GANASTE!" : "¡PERDISTE!",
                        JLabel.CENTER);

        result.setFont(new Font("Arial", Font.BOLD, 40));
        result.setForeground(Color.WHITE);

        // ===== NUEVO PANEL DE RESUMEN =====

        JTextArea summaryArea = new JTextArea();
        summaryArea.setEditable(false);
        summaryArea.setOpaque(false);
        summaryArea.setForeground(Color.WHITE);
        summaryArea.setFont(new Font("Arial", Font.BOLD, 16));

        StringBuilder summary = new StringBuilder();

        if (win) {

            summary.append("RECOMPENSAS:\n\n");

            for (GameCharacter character : GameState.getParty()) {

                PlayerCharacter p = (PlayerCharacter) character;

                summary.append(p.getName())
                        .append("\n");

                summary.append("Nivel: ")
                        .append(p.getLevel())
                        .append("\n");

                summary.append("EXP actual: ")
                        .append(p.getExp())
                        .append("/")
                        .append(p.getExpToLevel())
                        .append("\n");

                summary.append("HP Máx: ")
                        .append(p.getMaxHp())
                        .append("\n");

                summary.append("Ataque: ")
                        .append(p.getAttack())
                        .append("\n");

                summary.append("Defensa: ")
                        .append(p.getDefense())
                        .append("\n\n");
            }
        }

        summaryArea.setText(summary.toString());

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        centerPanel.add(result, BorderLayout.NORTH);
        centerPanel.add(summaryArea, BorderLayout.CENTER);

        // ===== BOTONES =====

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.setOpaque(false);

        JButton homeBtn = new JButton("VOLVER AL INICIO");
        JButton selectBtn =
                new JButton("SELECCIONAR PERSONAJES");

        homeBtn.addActionListener(
                e -> showStartScreen());

        selectBtn.addActionListener(
                e -> characterSelectionScreen());

        buttons.add(homeBtn);
        buttons.add(selectBtn);

        bg.add(centerPanel, BorderLayout.CENTER);
        bg.add(buttons, BorderLayout.SOUTH);

        mainPanel.add(bg);

        refreshMain();
    }

    public void refreshMain() {
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void log(String text) {
        if (logArea != null) {
            logArea.append("\n• " + text);
        }
    }

    public void setSelectedTarget(GameCharacter c) {
        selectedTarget = c;
    }
}