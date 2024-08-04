package sote.inventory;

import java.io.File;
import java.util.LinkedHashMap;

import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;
import sote.inventory.lobby.StartInventory;

public class Inventorys{

    public static final int GUI_CLASSIC = 0;
    public static final int GUI_POCKET = 1;

    public Inventorys(){
    }

    public static void addPlayer(Player player){
        setData(player,new StartInventory());
    }

    public static void setData(Player player,Inventory inv){
        if(data2.containsKey(player)) data2.remove(player);
        data2.put(player,inv);
        inv.register(player);
    }

    public static void Function(PlayerInteractEvent event){
        if(event.getBlock().getId() != 0) return;
        Player player = event.getPlayer();
        if(!data2.containsKey(player)) return;
        Item item = event.getItem();
        data2.get(player).Function(player, item);
    }

    public static void Function(Player player,Item item){
        if(!data2.containsKey(player)) return;
        data2.get(player).Function(player, item);
    }

    public static void setSize(Player player,double size){
    }

    public static int getSize(Player player){
        return 9;
    }

    public static void setGUI(Player player,int t){
        guis.put(player.getName(),t);
    }

    public static void setGUI(String player,int t){
        guis.put(player,t);
    }

    public static void setOS(Player player,int t){
        oss.put(player.getName(),t);
    }

    public static void setOS(String player,int t){
        oss.put(player,t);
    }

    public static Integer getGUI(Player player){
        if(!guis.containsKey(player.getName().toLowerCase())){
        	return 1;
        }
        return guis.get(player.getName().toLowerCase());
    }

    public static Integer getOS(Player player){
        if(!oss.containsKey(player.getName().toLowerCase())){
            return 1;
        }
        return oss.get(player.getName().toLowerCase());
    }

    public static LinkedHashMap<Player, Inventory> data2 = new LinkedHashMap<Player, Inventory>();
    public static LinkedHashMap<String, Integer> guis = new LinkedHashMap<String, Integer>();
    public static LinkedHashMap<String, Integer> oss = new LinkedHashMap<String, Integer>();
    public static File file;

}