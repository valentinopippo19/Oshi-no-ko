package system;

import model.*;
import skills.*;

import java.util.ArrayList;
import java.util.List;

public class CharacterFactory {

    private CharacterFactory() {

    }

    public static List<GameCharacter>
    createAllCharacters() {

        List<GameCharacter> characters =
                new ArrayList<>();

        characters.add(createAi());

        characters.add(createRuby());

        characters.add(createKana());

        characters.add(createAkane());

        characters.add(createMemcho());

        characters.add(createMiyako());

        return characters;
    }

    // ======================================================
    // AI
    // ======================================================

    private static GameCharacter createAi() {

        PlayerCharacter ai =
                new SupportCharacter(
                        "Ai",
                        100,
                        100,
                        10,
                        5,
                        7,
                        "resources/ai.png"
                );

        ai.addSkill(
                new HealSkill(
                        "Curar",
                        10,
                        25
                )
        );

        return ai;
    }

    // ======================================================
    // RUBY
    // ======================================================

    private static GameCharacter createRuby() {

        PlayerCharacter ruby =
                new DPSCharacter(
                        "Ruby",
                        80,
                        50,
                        15,
                        5,
                        10,
                        "resources/ruby.png"
                );

        ruby.addSkill(
                new DamageSkill(
                        "Fireball",
                        10,
                        30
                )
        );

        return ruby;
    }

    // ======================================================
    // KANA
    // ======================================================

    private static GameCharacter createKana() {

        PlayerCharacter kana =
                new SupportCharacter(
                        "Kana",
                        85,
                        70,
                        12,
                        6,
                        11,
                        "resources/kana.png"
                );

        kana.addSkill(
                new BuffSkill(
                        "Buff",
                        8,
                        5,
                        3
                )
        );

        return kana;
    }

    // ======================================================
    // AKANE
    // ======================================================

    private static GameCharacter createAkane() {

        PlayerCharacter akane =
                new SupportCharacter(
                        "Akane",
                        90,
                        120,
                        9,
                        7,
                        8,
                        "resources/akane.png"
                );

        akane.addSkill(
                new HealSkill(
                        "Mega Heal",
                        15,
                        40
                )
        );

        return akane;
    }

    // ======================================================
    // MEMCHO
    // ======================================================

    private static GameCharacter createMemcho() {

        PlayerCharacter mem =
                new DPSCharacter(
                        "Memcho",
                        75,
                        60,
                        18,
                        4,
                        12,
                        "resources/mem.png"
                );

        mem.addSkill(
                new DamageSkill(
                        "Star Beam",
                        12,
                        35
                )
        );

        return mem;
    }

    // ======================================================
    // MIYAKO
    // ======================================================

    private static GameCharacter createMiyako() {

        PlayerCharacter miyako =
                new TankCharacter(
                        "Miyako",
                        140,
                        40,
                        8,
                        12,
                        4,
                        "resources/miyako.png"
                );

        miyako.addSkill(
                new BuffSkill(
                        "Defense Up",
                        10,
                        8,
                        3
                )
        );

        return miyako;
    }
}