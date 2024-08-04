package sote.inventory;

import java.util.HashMap;
import java.util.LinkedHashMap;

import cn.nukkit.Player;
import cn.nukkit.item.Item;

public class ServerChestInventorys{

    public ServerChestInventorys(){
    }

    public static void setData(Player player,ServerChestInventory inv){
        if(data2.containsKey(player)) data2.remove(player);
        data2.put(player,inv);
        inv.register();
        isOpen.put(player, true);
    }

    public static void Function(Player player,int slot,Item item, int windowid){
        if(data2.containsKey(player)){
            if(isOpen.containsKey(player) && isOpen.get(player)){
                data2.get(player).Function(slot, item);
            }
        }
    }

    public static void close(Player player, int windowid){
        if(data2.containsKey(player)){
            if(isOpen.containsKey(player) && isOpen.get(player)){
                data2.get(player).close();
                isOpen.put(player,  false);
            }
        }
    }

    public static void remove(Player player,Item item){
        player.getInventory().remove(item);
    }

    public static HashMap<Player, Boolean> isOpen = new HashMap<Player, Boolean>();
    public static LinkedHashMap<Player, ServerChestInventory> data2 = new LinkedHashMap<Player, ServerChestInventory>();

}