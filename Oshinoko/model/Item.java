package model;

import java.io.Serializable;

// =====================
// ITEM
// =====================

public class Item implements Serializable {
    public String name;
    public ItemType type;
    public int value;

    public Item(String n, ItemType t, int v){
        name = n;
        type = t;
        value = v;
    }
}