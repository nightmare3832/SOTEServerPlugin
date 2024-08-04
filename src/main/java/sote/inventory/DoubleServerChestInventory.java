package sote.inventory;

import java.util.HashMap;
import java.util.Map;

import cn.nukkit.inventory.ContainerInventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class DoubleServerChestInventory extends ContainerInventory implements InventoryHolder {

    public Map<Integer, Item> inventory = new HashMap<Integer, Item>();

    public DoubleServerChestInventory(Map<Integer, Item> items) {
        super(null, InventoryType.DOUBLE_CHEST);
        this.holder = this;
        this.setContents(items);
    }

    @Override
    public cn.nukkit.inventory.Inventory getInventory(){
        return this;
    }

    @Override
    public Item getItem(int index) {
        return this.inventory.get(index);
    }

    @Override
    public boolean setItem(int index, Item item) {
        this.inventory.put(index, item);
        return true;
    }

    @Override
    public boolean clear(int index) {
        this.inventory.put(index, Item.get(Item.AIR));
        return true;
    }

    @Override
    public Map<Integer, Item> getContents() {
        return this.inventory;
    }

    @Override
    public void setContents(Map<Integer, Item> items) {
        this.inventory = new HashMap<Integer, Item>();
        for(Map.Entry<Integer, Item> e : items.entrySet()){
            if(e.getKey() == -1) continue;
            this.setItem(e.getKey(), e.getValue());
        }
    }
}