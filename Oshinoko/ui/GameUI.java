package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import system.BattleSystem;
import model.*;
import system.*;

// =====================
// INTERFAZ RPG
// =====================
public class GameUI extends JFrame {
    private JPanel mainPanel, playerPanel, enemyPanel;
    private JTextArea log;
    private JButton attackBtn, skillBtn, defendBtn, itemBtn;
    public GameCharacter selectedEnemy;
    private long startTime;

    private ImageIcon portadaImg = new ImageIcon("resources/portada.jpg");
    private ImageIcon contratapaImg = new ImageIcon("resources/contratapa.jpg");
    private ImageIcon battleBg = new ImageIcon("resources/fondo.jpg");

    public GameUI() {
        setTitle("OSHI NO BATTLE RPG");
        setSize(1000,700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);
        showStartScreen();
        setVisible(true);
    }

    // =====================
    // PORTADA
    // =====================
    public void showStartScreen(){
        mainPanel.removeAll();
        JPanel coverPanel = new JPanel(new BorderLayout());
        JLabel coverLabel = new JLabel(portadaImg);
        coverLabel.setHorizontalAlignment(JLabel.CENTER);
        coverLabel.setVerticalAlignment(JLabel.CENTER);
        coverPanel.add(coverLabel, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        JButton newGame = new JButton("Nueva Partida");
        JButton loadGame = new JButton("Cargar Partida");

        newGame.addActionListener(e -> characterSelectionScreen());
        loadGame.addActionListener(e -> {
            GameState.load();
            showBattleScreen();
        });

        buttons.add(newGame);
        buttons.add(loadGame);
        coverPanel.add(buttons, BorderLayout.SOUTH);
        mainPanel.add(coverPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // =====================
    // SELECCIÓN DE ALIADOS Y ENEMIGOS
    // =====================
    public void characterSelectionScreen() {
        mainPanel.removeAll();
        JLabel bgLabel = new JLabel(new ImageIcon("resources/fondoSeleccion.jpg"));
        bgLabel.setLayout(new BorderLayout());

        JPanel selectPanel = new JPanel();
        selectPanel.setOpaque(false); // 🔥 importante para ver el fondo
        selectPanel.setLayout(new BoxLayout(selectPanel, BoxLayout.Y_AXIS));

        bgLabel.add(selectPanel, BorderLayout.CENTER);
        mainPanel.add(bgLabel, BorderLayout.CENTER);
        selectPanel.setLayout(new BoxLayout(selectPanel, BoxLayout.Y_AXIS));
        selectPanel.add(new JLabel("Selecciona tus aliados y enemigos:"));

        List<GameCharacter> all = GameState.getAllCharacters();
        List<JCheckBox> allyBoxes = new ArrayList<>();
        List<JCheckBox> enemyBoxes = new ArrayList<>();

        for(GameCharacter c: all){
            JPanel row = new JPanel();
            row.setOpaque(false);
            JCheckBox ally = new JCheckBox("Aliado: " + c.name + " (" + c.role + ")");
            JCheckBox enemy = new JCheckBox("Enemigo: " + c.name + " (" + c.role + ")");

            // 🔥 hacer transparente
            ally.setOpaque(false);
            enemy.setOpaque(false);

            // 🔥 agregar imagen
            if(c.imageIcon != null){
                Image img = c.imageIcon.getImage().getScaledInstance(40,40,Image.SCALE_SMOOTH);
                ally.setIcon(new ImageIcon(img));
                enemy.setIcon(new ImageIcon(img));
            }
            ally.setHorizontalTextPosition(SwingConstants.RIGHT);
            enemy.setHorizontalTextPosition(SwingConstants.RIGHT);
            allyBoxes.add(ally);
            enemyBoxes.add(enemy);
            row.add(ally);
            row.add(enemy);
            selectPanel.add(row);
        }

        JButton startBattle = new JButton("Iniciar Batalla");
        startBattle.addActionListener(e -> {
            GameState.party.clear();
            GameState.enemies.clear();

            for(int i=0;i<all.size();i++){
                if(allyBoxes.get(i).isSelected()) GameState.party.add(all.get(i).cloneCharacter());
                if(enemyBoxes.get(i).isSelected()) GameState.enemies.add(all.get(i).cloneCharacter());
            }

            if(GameState.party.size() < 2 || GameState.party.size() > 4){
                JOptionPane.showMessageDialog(this,"Seleccioná entre 2 y 4 aliados!");
                return;
            }

            if(GameState.enemies.isEmpty()){
                JOptionPane.showMessageDialog(this,"Seleccioná al menos un enemigo!");
                return;
            }

            startTime = System.currentTimeMillis();

            // 🔥 GUARDAR PARTIDA
            GameState.save();

            showBattleScreen();
        });

        selectPanel.add(startBattle);
        mainPanel.add(selectPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // =====================
    // PANTALLA DE BATALLA
    // =====================
    public void showBattleScreen(){
        mainPanel.removeAll();
        JLabel bgLabel = new JLabel(battleBg);
        bgLabel.setLayout(new BorderLayout());
        mainPanel.add(bgLabel, BorderLayout.CENTER);

        // Panel jugadores
        playerPanel = new JPanel();
        playerPanel.setOpaque(false);
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.X_AXIS));
        for(GameCharacter c: GameState.party){
            JLabel lbl = characterLabel(c);

            // 🔥 borde azul para aliados
            lbl.setBorder(BorderFactory.createLineBorder(Color.CYAN,2));

            playerPanel.add(lbl);
        }
        bgLabel.add(playerPanel, BorderLayout.WEST);

        // Panel enemigos
        enemyPanel = new JPanel();
        enemyPanel.setOpaque(false);
        enemyPanel.setLayout(new BoxLayout(enemyPanel, BoxLayout.X_AXIS));
        refreshEnemies();
        if(!GameState.enemies.isEmpty()){
            selectedEnemy = GameState.enemies.get(0);
        }
        bgLabel.add(enemyPanel, BorderLayout.EAST);

        // Panel inferior: log + botones
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        log = new JTextArea("Última acción: ");
        log.setEditable(false);
        log.setFont(new Font("Arial",Font.PLAIN,14));
        bottom.add(new JScrollPane(log), BorderLayout.CENTER);

        JPanel actions = new JPanel();
        actions.setOpaque(false);
        attackBtn = new JButton("Atacar");
        skillBtn = new JButton("Skill");
        defendBtn = new JButton("Defender");
        itemBtn = new JButton("Item");

        attackBtn.addActionListener(e -> BattleSystem.attack());
        skillBtn.addActionListener(e -> BattleSystem.skill());
        defendBtn.addActionListener(e -> BattleSystem.defend());
        itemBtn.addActionListener(e -> BattleSystem.useItem());

        actions.add(attackBtn);
        actions.add(skillBtn);
        actions.add(defendBtn);
        actions.add(itemBtn);
        bottom.add(actions, BorderLayout.SOUTH);

        bgLabel.add(bottom, BorderLayout.SOUTH);

        mainPanel.revalidate();
        mainPanel.repaint();

        BattleSystem.setUI(this);
        BattleSystem.startBattle();
    }

    public void refreshEnemies(){
        if(enemyPanel==null) return;

        enemyPanel.removeAll();

        for(GameCharacter e: GameState.enemies){

            JLabel lbl = characterLabel(e);

            // 🔥 borde normal
            lbl.setBorder(BorderFactory.createLineBorder(Color.RED,2));

            // 🔥 si está seleccionado → borde amarillo
            if(e == selectedEnemy){
                lbl.setBorder(BorderFactory.createLineBorder(Color.YELLOW,3));
            }

            lbl.addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent me){
                    selectedEnemy = e;
                    log("Seleccionaste a " + e.name);

                    // 🔥 refresca para actualizar el borde
                    refreshEnemies();
                }
            });

            enemyPanel.add(lbl);
        }

        enemyPanel.revalidate();
        enemyPanel.repaint();
    }


    private JLabel characterLabel(GameCharacter c){
        String text = "<html><center>" + c.name
                + "<br>HP:" + c.getHp() + "/" + c.maxHp
                + "<br>MP:" + c.mana + "/" + c.maxMana
                + "<br>ATK:" + c.attack + " DEF:" + c.defense
                + "</center></html>";
        JLabel lbl = new JLabel(text);
        if(c.imageIcon!=null) lbl.setIcon(scaleImage(c.imageIcon,64,64));
        lbl.setHorizontalTextPosition(JLabel.CENTER);
        lbl.setVerticalTextPosition(JLabel.BOTTOM);
        lbl.setHorizontalAlignment(JLabel.CENTER);
        lbl.setVerticalAlignment(JLabel.CENTER);
        return lbl;
    }

    private ImageIcon scaleImage(ImageIcon img,int w,int h){
        Image i = img.getImage().getScaledInstance(w,h,Image.SCALE_SMOOTH);
        return new ImageIcon(i);
    }

    public void log(String text){ log.setText("Última acción: " + text);}
    public void enableButtons(boolean enable){
        attackBtn.setEnabled(enable);
        skillBtn.setEnabled(enable);
        defendBtn.setEnabled(enable);
        itemBtn.setEnabled(enable);
    }
    public void refresh(){
        refreshEnemies();

        // 🔥 refrescar jugadores también
        playerPanel.removeAll();

        for(GameCharacter c: GameState.party){
            JLabel lbl = characterLabel(c);
            lbl.setBorder(BorderFactory.createLineBorder(Color.CYAN,2));
            playerPanel.add(lbl);
        }

        playerPanel.revalidate();
        playerPanel.repaint();

        repaint();
    }

    public void showEndScreen(boolean win){
        mainPanel.removeAll();
        JPanel endPanel = new JPanel(new BorderLayout());
        JLabel backLabel = new JLabel(contratapaImg);
        backLabel.setHorizontalAlignment(JLabel.CENTER);
        endPanel.add(backLabel, BorderLayout.CENTER);

        long time = (System.currentTimeMillis()-startTime)/1000;
        String message = (win?"GANASTE":"PERDISTE") + "<br>Tiempo: " + time + "s";

        JLabel result = new JLabel("<html><center>"+message+"</center></html>", JLabel.CENTER);
        result.setFont(new Font("Arial",Font.BOLD,24));
        result.setForeground(Color.WHITE);
        endPanel.add(result, BorderLayout.NORTH);

        JButton replay = new JButton("Volver a jugar");
        replay.addActionListener(e -> showStartScreen());
        endPanel.add(replay, BorderLayout.SOUTH);

        mainPanel.add(endPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}