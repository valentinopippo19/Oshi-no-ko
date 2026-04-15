package system;

import model.*;
import java.io.Serializable;
import java.util.*;

// =====================
// INVENTARIO
// =====================

public class Inventory implements Serializable {
    public List<Item> items = new ArrayList<>();

    public void add(Item i){ items.add(i); }

    public void use(int index, GameCharacter target){
        if(index<0||index>=items.size()) return;

        Item i = items.get(index);

        switch(i.type){
            case HEAL:
                target.heal(i.value);
                break;
            case MANA:
                target.mana = Math.min(target.maxMana, target.mana + i.value);
                break;
            case BUFF:
                target.attack += i.value;
                break;
        }

        items.remove(index);
    }

    public void listItems(){
        for(Item i : items){
            System.out.println(i.name + " - " + i.type);
        }
    }
}