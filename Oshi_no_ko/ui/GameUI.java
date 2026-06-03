package ui;

import items.Item;
import model.GameCharacter;
import model.PlayerCharacter;
import skills.Skill;
import system.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameUI extends JFrame {

    // ======================================================
    // CONSTANTES
    // ======================================================

    private static final Color PANEL_COLOR =
            new Color(0, 0, 0, 190);

    private static final Color BUTTON_COLOR =
            new Color(45, 45, 45);

    private static final Font TITLE_FONT =
            new Font("Arial", Font.BOLD, 40);

    private static final Font NORMAL_FONT =
            new Font("Arial", Font.BOLD, 16);

    // ======================================================
    // COMPONENTES
    // ======================================================

    private JPanel mainPanel;

    private JPanel playerPanel;
    private JPanel enemyPanel;

    private JTextArea logArea;

    private JLabel turnLabel;

    private JButton attackBtn;
    private JButton skillBtn;
    private JButton defendBtn;
    private JButton itemBtn;

    // ======================================================
    // ESTADO
    // ======================================================

    private GameCharacter selectedEnemy;

    // ======================================================
    // CONSTRUCTOR
    // ======================================================

    public GameUI() {

        setTitle("OSHI NO BATTLE RPG");

        setSize(1280, 720);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        mainPanel =
                new JPanel(new BorderLayout());

        setContentPane(mainPanel);

        showStartScreen();

        setVisible(true);
    }

    // ======================================================
    // START SCREEN
    // ======================================================

    public void showStartScreen() {

        mainPanel.removeAll();

        BackgroundPanel bg =
                new BackgroundPanel(
                        "resources/portada.jpg"
                );

        bg.setLayout(new BorderLayout());

        JLabel title =
                new JLabel("OSHI NO BATTLE RPG");

        title.setHorizontalAlignment(
                JLabel.CENTER
        );

        title.setForeground(Color.WHITE);

        title.setFont(TITLE_FONT);

        title.setBorder(
                BorderFactory.createEmptyBorder(
                        40,
                        0,
                        40,
                        0
                )
        );

        bg.add(title, BorderLayout.NORTH);

        JPanel menu =
                createDarkPanel();

        menu.setLayout(
                new GridLayout(2, 1, 20, 20)
        );

        menu.setBorder(
                BorderFactory.createEmptyBorder(
                        30,
                        40,
                        30,
                        40
                )
        );

        JButton newGame =
                createButton("NUEVA PARTIDA");

        JButton loadGame =
                createButton("CARGAR PARTIDA");

        // ==================================================
        // NUEVA PARTIDA
        // ==================================================

        newGame.addActionListener(e -> {

            GameState.clear();

            characterSelectionScreen();
        });

        // ==================================================
        // CARGAR PARTIDA
        // ==================================================

        loadGame.addActionListener(e -> {

            try {
                SaveManager.load();
            } catch (ClassNotFoundException | IOException e1) {
                e1.printStackTrace();
            }

            if (GameState.getParty().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "No hay partida guardada"
                );

                return;
            }

            showBattleScreen();
        });

        menu.add(newGame);

        menu.add(loadGame);

        JPanel wrapper =
                new JPanel(new GridBagLayout());

        wrapper.setOpaque(false);

        wrapper.add(menu);

        bg.add(wrapper, BorderLayout.CENTER);

        mainPanel.add(bg);

        refreshMain();
    }

    // ======================================================
    // CHARACTER SELECTION
    // ======================================================

    public void characterSelectionScreen() {

        mainPanel.removeAll();

        BackgroundPanel bg =
                new BackgroundPanel(
                        "resources/fondoSeleccion.jpg"
                );

        bg.setLayout(new BorderLayout());

        JLabel title =
                new JLabel("SELECCIONÁ LOS EQUIPOS");

        title.setHorizontalAlignment(
                JLabel.CENTER
        );

        title.setForeground(Color.WHITE);

        title.setFont(TITLE_FONT);

        title.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        0,
                        20,
                        0
                )
        );

        bg.add(title, BorderLayout.NORTH);

        List<GameCharacter> allPlayers =
                CharacterFactory.createAllCharacters();

        List<GameCharacter> allEnemies =
                EnemyFactory.createEnemies();

        JPanel center =
                new JPanel(
                        new GridLayout(1, 2, 30, 0)
                );

        center.setOpaque(false);

        center.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        40,
                        20,
                        40
                )
        );

        JPanel allyPanel =
                createSelectionPanel(
                        "ALIADOS",
                        allPlayers
                );

        JPanel enemySelectionPanel =
                createSelectionPanel(
                        "ENEMIGOS",
                        allEnemies
                );

        center.add(allyPanel);

        center.add(enemySelectionPanel);

        bg.add(center, BorderLayout.CENTER);

        JButton startBattle =
                createButton("INICIAR BATALLA");

        startBattle.setPreferredSize(
                new Dimension(300, 60)
        );

        startBattle.addActionListener(e -> {

            List<GameCharacter> party =
                    new ArrayList<>();

            List<GameCharacter> enemies =
                    new ArrayList<>();

            collectSelectedCharacters(
                    allyPanel,
                    allPlayers,
                    party
            );

            collectSelectedCharacters(
                    enemySelectionPanel,
                    allEnemies,
                    enemies
            );

            if (party.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Seleccioná al menos un aliado"
                );

                return;
            }

            if (enemies.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Seleccioná al menos un enemigo"
                );

                return;
            }

            GameState.setParty(party);

            GameState.setEnemies(enemies);

            try {
                SaveManager.save(null);
        } catch (IOException e1) {
                e1.printStackTrace();
        }

            showBattleScreen();
        });

        JPanel south =
                new JPanel();

        south.setOpaque(false);

        south.add(startBattle);

        bg.add(south, BorderLayout.SOUTH);

        mainPanel.add(bg);

        refreshMain();
    }

    // ======================================================
    // BATTLE SCREEN
    // ======================================================

    public void showBattleScreen() {

        mainPanel.removeAll();

        selectedEnemy = null;

        BackgroundPanel bg =
                new BackgroundPanel(
                        "resources/fondo.jpg"
                );

        bg.setLayout(new BorderLayout());

        // ==================================================
        // TOP
        // ==================================================

        turnLabel =
                new JLabel("INICIANDO BATALLA");

        turnLabel.setHorizontalAlignment(
                JLabel.CENTER
        );

        turnLabel.setForeground(Color.WHITE);

        turnLabel.setFont(
                new Font("Arial", Font.BOLD, 28)
        );

        turnLabel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        0,
                        20,
                        0
                )
        );

        bg.add(turnLabel, BorderLayout.NORTH);

        // ==================================================
        // ENEMIES
        // ==================================================

        enemyPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                40,
                                30
                        )
                );

        enemyPanel.setOpaque(false);
        bg.add(enemyPanel, BorderLayout.EAST);

        // ==================================================
        // BOTTOM
        // ==================================================

        JPanel bottom =
                new JPanel(new BorderLayout());

        bottom.setOpaque(false);

        // ==================================================
        // PLAYERS
        // ==================================================

        playerPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                30,
                                20
                        )
                );

        playerPanel.setOpaque(false);
        bg.add(playerPanel, BorderLayout.WEST);

        // ==================================================
        // LOG
        // ==================================================

        logArea =
                new JTextArea(6, 30);

        logArea.setEditable(false);

        logArea.setLineWrap(true);

        logArea.setWrapStyleWord(true);

        logArea.setFont(
                new Font("Monospaced", Font.BOLD, 15)
        );

        logArea.setBackground(
                new Color(0,0,0,210)
        );

        logArea.setForeground(Color.WHITE);

        JScrollPane scroll =
                new JScrollPane(logArea);

        bottom.add(scroll, BorderLayout.CENTER);
        bg.add(bottom, BorderLayout.SOUTH);

        // ==================================================
        // ACTIONS
        // ==================================================

        JPanel actions =
                createDarkPanel();

        actions.setLayout(
                new FlowLayout(
                        FlowLayout.CENTER,
                        20,
                        10
                )
        );

        attackBtn = createButton("ATACAR");
        skillBtn = createButton("SKILL");
        defendBtn = createButton("DEFENDER");
        itemBtn = createButton("ITEM");

        actions.add(attackBtn);
        actions.add(skillBtn);
        actions.add(defendBtn);
        actions.add(itemBtn);

        bottom.add(actions, BorderLayout.SOUTH);

        bg.add(bottom, BorderLayout.SOUTH);

        mainPanel.add(bg);

        // ==================================================
        // ACTIONS
        // ==================================================

        attackBtn.addActionListener(e -> {

            if (selectedEnemy == null) {

                log("Seleccioná un enemigo");

                return;
            }

            BattleSystem.attack(selectedEnemy);

            refresh();
        });

        skillBtn.addActionListener(
                e -> openSkillMenu()
        );

        defendBtn.addActionListener(e -> {

            BattleSystem.defend();

            refresh();
        });

        itemBtn.addActionListener(
                e -> openInventoryMenu()
        );

        BattleSystem.setUI(this);

        refresh();

        BattleSystem.startBattle();

        refreshMain();
    }

    // ======================================================
    // SKILLS
    // ======================================================

    private void openSkillMenu() {

        GameCharacter current =
                BattleSystem.getCurrentCharacter();

        if (current == null) {
            return;
        }

        List<Skill> skills =
                current.getSkills();

        if (skills.isEmpty()) {

            log("No tiene skills");

            return;
        }

        Skill selected =
                (Skill) JOptionPane.showInputDialog(
                        this,
                        "Seleccioná una skill",
                        "Skills",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        skills.toArray(),
                        skills.get(0)
                );

        if (selected != null) {

            BattleSystem.useSkill(
                    selected,
                    selectedEnemy
            );

            refresh();
        }
    }

    // ======================================================
    // INVENTORY
    // ======================================================

    private void openInventoryMenu() {

        List<Item> items =
                GameState.getInventory()
                        .getItems();

        if (items.isEmpty()) {

            log("No hay items");

            return;
        }

        Item selected =
                (Item) JOptionPane.showInputDialog(
                        this,
                        "Seleccioná un item",
                        "Inventario",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        items.toArray(),
                        items.get(0)
                );

        if (selected != null) {

            BattleSystem.useItem(
                    selected,
                    BattleSystem.getCurrentCharacter()
            );

            refresh();
        }
    }

    // ======================================================
    // REFRESH
    // ======================================================

    public void refresh() {

        if (playerPanel == null ||
                enemyPanel == null) {
            return;
        }

        refreshPlayers();

        refreshEnemies();

        refreshTurnLabel();

        repaint();
    }

    private void refreshTurnLabel() {

        if (turnLabel == null) {
            return;
        }

        GameCharacter current =
                BattleSystem.getCurrentCharacter();

        if (current == null) {

            turnLabel.setText("");

            return;
        }

        turnLabel.setText(
                "TURNO DE " +
                        current.getName().toUpperCase()
        );
    }

    private void refreshPlayers() {

        playerPanel.removeAll();

        for (GameCharacter c :
                GameState.getParty()) {

            if (!c.isAlive()) {
                continue;
            }

            playerPanel.add(
                    createCharacterCard(c, false)
            );
        }

        playerPanel.revalidate();

        playerPanel.repaint();
    }

    private void refreshEnemies() {

        enemyPanel.removeAll();

        if (selectedEnemy != null &&
                !selectedEnemy.isAlive()) {

            selectedEnemy = null;
        }

        for (GameCharacter c :
                GameState.getEnemies()) {

            if (!c.isAlive()) {
                continue;
            }

            enemyPanel.add(
                    createCharacterCard(c, true)
            );
        }

        enemyPanel.revalidate();

        enemyPanel.repaint();
    }

    // ======================================================
    // CHARACTER CARD
    // ======================================================

    private JPanel createCharacterCard(
            GameCharacter c,
            boolean enemy
    ) {

        JPanel card =
                createDarkPanel();

        card.setPreferredSize(
                new Dimension(200, 320)
        );

        card.setLayout(new BorderLayout());

        boolean selected =
                selectedEnemy == c;

        boolean currentTurn =
                BattleSystem.getCurrentCharacter() == c;

        Color borderColor =
                currentTurn
                        ? Color.YELLOW
                        : selected
                        ? Color.RED
                        : Color.WHITE;

        int borderSize =
                currentTurn
                        ? 5
                        : selected
                        ? 4
                        : 2;

        card.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                borderColor,
                                borderSize
                        ),

                        BorderFactory.createEmptyBorder(
                                10,
                                10,
                                10,
                                10
                        )
                )
        );

        // ==================================================
        // NAME
        // ==================================================

        JLabel name =
                new JLabel(c.getName());

        name.setHorizontalAlignment(
                JLabel.CENTER
        );

        name.setForeground(Color.WHITE);

        name.setFont(
                new Font("Arial", Font.BOLD, 20)
        );

        card.add(name, BorderLayout.NORTH);

        // ==================================================
        // IMAGE
        // ==================================================

        JLabel image =
                new JLabel();

        image.setHorizontalAlignment(
                JLabel.CENTER
        );

        if (c.getImageIcon() != null) {

            Image img =
                    c.getImageIcon()
                            .getImage()
                            .getScaledInstance(
                                    150,
                                    150,
                                    Image.SCALE_SMOOTH
                            );

            image.setIcon(
                    new ImageIcon(img)
            );
        }

        JPanel center =
                new JPanel(new BorderLayout());

        center.setOpaque(false);

        center.add(image, BorderLayout.CENTER);

        // ==================================================
        // STATUS
        // ==================================================

        JLabel status =
                new JLabel(
                        "Estado: " +
                                c.getStatus()
                );

        status.setHorizontalAlignment(
                JLabel.CENTER
        );

        status.setForeground(Color.CYAN);

        center.add(status, BorderLayout.SOUTH);

        card.add(center, BorderLayout.CENTER);

        // ==================================================
        // BARS
        // ==================================================

        JPanel bars =
                new JPanel(
                        new GridLayout(3,1,0,5)
                );

        bars.setOpaque(false);

        // ==================================================
        // HP
        // ==================================================

        JProgressBar hpBar =
                new JProgressBar(
                        0,
                        c.getMaxHp()
                );

        hpBar.setValue(c.getHp());

        hpBar.setStringPainted(true);

        hpBar.setString(
                "HP: " +
                        c.getHp()
                        + " / "
                        + c.getMaxHp()
        );

        // ==================================================
        // MP
        // ==================================================

        JProgressBar manaBar =
                new JProgressBar(
                        0,
                        c.getMaxMana()
                );

        manaBar.setValue(c.getMana());

        manaBar.setStringPainted(true);

        manaBar.setString(
                "MP: " +
                        c.getMana()
                        + " / "
                        + c.getMaxMana()
        );

        bars.add(hpBar);

        bars.add(manaBar);

        // ==================================================
        // LEVEL
        // ==================================================

        if (c instanceof PlayerCharacter player) {

            JLabel level =
                    new JLabel(
                            "LVL " + player.getLevel()
                    );

            level.setForeground(Color.ORANGE);

            level.setHorizontalAlignment(
                    JLabel.CENTER
            );

            bars.add(level);

        } else {

            JLabel enemyLabel =
                    new JLabel("ENEMY");

            enemyLabel.setForeground(Color.RED);

            enemyLabel.setHorizontalAlignment(
                    JLabel.CENTER
            );

            bars.add(enemyLabel);
        }

        card.add(bars, BorderLayout.SOUTH);

        // ==================================================
        // CLICK ENEMY
        // ==================================================

        if (enemy) {

            card.addMouseListener(
                    new MouseAdapter() {

                        @Override
                        public void mouseClicked(
                                MouseEvent e
                        ) {

                            selectedEnemy = c;

                            log(
                                    "Objetivo seleccionado: "
                                            + c.getName()
                            );

                            refreshEnemies();
                        }
                    }
            );
        }

        return card;
    }

    // ======================================================
    // SELECTION PANEL
    // ======================================================

    private JPanel createSelectionPanel(
            String title,
            List<GameCharacter> chars
    ) {

        JPanel wrapper =
                createDarkPanel();

        wrapper.setLayout(new BorderLayout());

        JLabel label =
                new JLabel(title);

        label.setHorizontalAlignment(
                JLabel.CENTER
        );

        label.setForeground(Color.WHITE);

        label.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        wrapper.add(label, BorderLayout.NORTH);

        JPanel grid =
                new JPanel(
                        new GridLayout(0,2,15,15)
                );

        grid.setOpaque(false);

        for (GameCharacter c : chars) {

            grid.add(
                    createCharacterToggle(c)
            );
        }

        JScrollPane scroll =
                new JScrollPane(grid);

        scroll.setOpaque(false);

        scroll.getViewport().setOpaque(false);

        wrapper.add(scroll, BorderLayout.CENTER);

        return wrapper;
    }

    private JToggleButton createCharacterToggle(
            GameCharacter c
    ) {

        JToggleButton btn =
                new JToggleButton(
                        c.getName()
                );

        btn.setVerticalTextPosition(
                SwingConstants.BOTTOM
        );

        btn.setHorizontalTextPosition(
                SwingConstants.CENTER
        );

        btn.setForeground(Color.WHITE);

        btn.setBackground(
                new Color(40,40,40)
        );

        btn.setFocusPainted(false);

        btn.setFont(NORMAL_FONT);

        if (c.getImageIcon() != null) {

            Image img =
                    c.getImageIcon()
                            .getImage()
                            .getScaledInstance(
                                    100,
                                    100,
                                    Image.SCALE_SMOOTH
                            );

            btn.setIcon(
                    new ImageIcon(img)
            );
        }

        return btn;
    }

    private void collectSelectedCharacters(
            JPanel panel,
            List<GameCharacter> source,
            List<GameCharacter> result
    ) {

        JScrollPane scroll =
                (JScrollPane)
                        panel.getComponent(1);

        JPanel grid =
                (JPanel)
                        scroll.getViewport()
                                .getView();

        Component[] components =
                grid.getComponents();

        for (int i = 0;
             i < components.length;
             i++) {

            JToggleButton btn =
                    (JToggleButton)
                            components[i];

            if (btn.isSelected()) {

                result.add(source.get(i));
            }
        }
    }

    // ======================================================
    // HELPERS
    // ======================================================

    private JPanel createDarkPanel() {

        JPanel p =
                new JPanel();

        p.setBackground(PANEL_COLOR);

        return p;
    }

    private JButton createButton(String text) {

        JButton b =
                new JButton(text);

        b.setFocusPainted(false);

        b.setFont(NORMAL_FONT);

        b.setForeground(Color.WHITE);

        b.setBackground(BUTTON_COLOR);

        b.setPreferredSize(
                new Dimension(160, 50)
        );

        return b;
    }

    private void refreshMain() {

        mainPanel.revalidate();

        mainPanel.repaint();
    }

    // ======================================================
    // LOG
    // ======================================================

    public void log(String text) {

        if (logArea == null) {
            return;
        }

        logArea.append("\n• " + text);

        logArea.setCaretPosition(
                logArea.getDocument().getLength()
        );
    }

    // ======================================================
    // BUTTONS
    // ======================================================

    public void enableButtons(boolean enabled) {

        if (attackBtn == null) {
            return;
        }

        attackBtn.setEnabled(enabled);

        skillBtn.setEnabled(enabled);

        defendBtn.setEnabled(enabled);

        itemBtn.setEnabled(enabled);
    }

    // ======================================================
    // END SCREEN
    // ======================================================

    public void showEndScreen(boolean win) {

        mainPanel.removeAll();

        BackgroundPanel bg =
                new BackgroundPanel(
                        "resources/contratapa.jpg"
                );

        bg.setLayout(
                new GridBagLayout()
        );

        JPanel panel =
                createDarkPanel();

        panel.setLayout(
                new BorderLayout()
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        40,
                        60,
                        40,
                        60
                )
        );

        JLabel result =
                new JLabel(
                        win
                                ? "GANASTE"
                                : "PERDISTE"
                );

        result.setHorizontalAlignment(
                JLabel.CENTER
        );

        result.setForeground(
                win
                        ? Color.GREEN
                        : Color.RED
        );

        result.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        42
                )
        );

        panel.add(result, BorderLayout.CENTER);

        JButton menu =
                createButton("VOLVER AL MENÚ");

        menu.addActionListener(
                e -> showStartScreen()
        );

        panel.add(menu, BorderLayout.SOUTH);

        bg.add(panel);

        mainPanel.add(bg);

        refreshMain();
    }

    // ======================================================
    // BACKGROUND PANEL
    // ======================================================

    private static class BackgroundPanel
            extends JPanel {

        private final Image background;

        public BackgroundPanel(String path) {

            background =
                    new ImageIcon(path)
                            .getImage();
        }

        @Override
        protected void paintComponent(
                Graphics g
        ) {

            super.paintComponent(g);

            g.drawImage(
                    background,
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    this
            );
        }
    }
}