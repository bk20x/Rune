package rune.editor.system;

import com.badlogic.gdx.utils.Array;
import rune.editor.objects.Item;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Vector;

public class Inventory {

    private final transient Vector<Item> inventory;
    public final LinkedHashMap<String,Integer> sortedInventory;

    public Inventory() {
        inventory = new Vector<>();
        sortedInventory = new LinkedHashMap<>();

    }

    public void updateItems() {




    }



    public void addItems(Vector<Item> items){
        items.forEach(inventory::add);
        for(Item i : items){
            sortedInventory.put(i.name, sortedInventory.getOrDefault(i.name, 0) + i.amount);
        }
    }

    public void addItems(Item... items){
        Collections.addAll(inventory, items);
        for(Item i : items){
            sortedInventory.put(i.name, sortedInventory.getOrDefault(i.name, 0) + i.amount);
        }
    }

    public void removeItem(Item i,int amount){
        if (i != null) {
            inventory.remove(i);
            if (sortedInventory.containsKey(i.name) && sortedInventory.get(i.name) > 0) {
                sortedInventory.replace(i.name, sortedInventory.getOrDefault(i.name, 0) - amount);
            }
            if (sortedInventory.containsKey(i.name) && sortedInventory.get(i.name) < 1) {
                sortedInventory.remove(i.name);
            }
        }


    }

    public Vector<Item> getInventory(){
        return inventory;
    }


    public Item getItem(String itemName){
        for (Item item : inventory) {
            if (item!= null){
                if(item.name.equals(itemName)){
                    return item;
                }
            }
        }
        return null;
    }

    public int size(){
        return inventory.size();
    }

}
