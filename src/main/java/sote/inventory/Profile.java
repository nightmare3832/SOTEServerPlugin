package sote.inventory;

import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.item.Item;

public abstract class Profile{

    public Profile(ProfileChestInventory owner){
        this.owner = owner;
    }

    public HashMap<Integer, Item> update(Player player){
        return new HashMap<Integer, Item>();
    }

    public void Function(Player player,int slot,Item item){
    }

    public ProfileChestInventory owner;

}