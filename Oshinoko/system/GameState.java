package system;

import model.GameCharacter;
import model.Role;
import model.Skill;
import model.SkillType;

import javax.swing.*;
import java.io.*;
import java.util.*;

// =====================
// GAMESTATE E INVENTARIO
// =====================
public class GameState implements java.io.Serializable {

     public static List<GameCharacter> party = new ArrayList<>();
    public static List<GameCharacter> enemies = new ArrayList<>();
    public static Inventory inventory = new Inventory();

    private static final String SAVE_FILE = "save.dat";

    // 🔥 GUARDAR PARTIDA (INCLUYE ENEMIGOS)
    public static void save(){
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))){
            out.writeObject(party);
            out.writeObject(enemies); // 🔥 CLAVE
            out.writeObject(inventory);

            System.out.println("Guardado OK - Party: " + party.size() + " Enemies: " + enemies.size());

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // 🔥 CARGAR PARTIDA
    @SuppressWarnings("unchecked")
    public static void load(){
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(SAVE_FILE))){

            List<GameCharacter> loadedParty = (List<GameCharacter>) in.readObject();
            List<GameCharacter> loadedEnemies = (List<GameCharacter>) in.readObject();
            Inventory loadedInventory = (Inventory) in.readObject();

            // 🔥 NO REEMPLAZAR REFERENCIAS
            party.clear();
            party.addAll(loadedParty);

            enemies.clear();
            enemies.addAll(loadedEnemies);

            inventory = loadedInventory;

            for(GameCharacter c : party){
                c.resetToBase();
            }

            for(GameCharacter c : enemies){
                c.resetToBase();
            }

            System.out.println("Cargado OK - Party: " + party.size() + " Enemies: " + enemies.size());

        }catch(Exception e){
            party.clear();
            enemies.clear();
            inventory = new Inventory();
        }

        reloadImages();
    }

    // 🔥 IMÁGENES
    public static void reloadImages(){
        Map<String, ImageIcon> imgs = new HashMap<>();
        imgs.put("Ai", new ImageIcon("resources/ai.png"));
        imgs.put("Ruby", new ImageIcon("resources/ruby.png"));
        imgs.put("Mem-cho", new ImageIcon("resources/mem.png"));
        imgs.put("Akane", new ImageIcon("resources/akane.png"));
        imgs.put("Kana", new ImageIcon("resources/kana.png"));
        imgs.put("Miyako", new ImageIcon("resources/miyako.png"));

        for(GameCharacter c : party){
            c.imageIcon = imgs.get(c.name);
        }
        for(GameCharacter c : enemies){
            c.imageIcon = imgs.get(c.name);
        }
    }


    public static List<GameCharacter> getAllCharacters(){
        List<GameCharacter> list = new ArrayList<>();

        ImageIcon aiImg = new ImageIcon("resources/ai.png");
        ImageIcon rubyImg = new ImageIcon("resources/ruby.png");
        ImageIcon memImg = new ImageIcon("resources/mem.png");
        ImageIcon akaneImg = new ImageIcon("resources/akane.png");
        ImageIcon kanaImg = new ImageIcon("resources/kana.png");
        ImageIcon miyakoImg = new ImageIcon("resources/miyako.png");

        GameCharacter ai = new GameCharacter("Ai",Role.SUPPORT,100,100,10,5,7, aiImg);
        GameCharacter ruby = new GameCharacter("Ruby",Role.DPS,80,50,15,5,10, rubyImg);
        GameCharacter mem = new GameCharacter("Mem-cho",Role.SPEED,75,60,12,5,12, memImg);
        GameCharacter akane = new GameCharacter("Akane",Role.TACTIC,90,80,11,6,9, akaneImg);
        GameCharacter kana = new GameCharacter("Kana",Role.MAGE,85,70,12,6,11, kanaImg);
        GameCharacter miyako = new GameCharacter("Miyako",Role.SUPPORT,110,60,8,10,6, miyakoImg);

        ai.skills.add(new Skill("Curar",10,SkillType.HEAL));
        ruby.skills.add(new Skill("Fireball",10,SkillType.DAMAGE));
        mem.skills.add(new Skill("Quick Attack",5,SkillType.DAMAGE));
        akane.skills.add(new Skill("Buff ATK",8,SkillType.BUFF));
        kana.skills.add(new Skill("Actuación Pro",12,SkillType.DAMAGE));
        miyako.skills.add(new Skill("Apoyo Manager",10,SkillType.HEAL));

        list.add(ai); list.add(ruby); list.add(mem);
        list.add(akane); list.add(kana); list.add(miyako);

        return list;
    }
}