package sote.inventory.miniwalls;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import sote.Game;
import sote.GameProvider;
import sote.Main;
import sote.inventory.Inventory;
import sote.miniwalls.Miniwalls;

public class MiniwallsRespawnInventory extends Inventory{

    public MiniwallsRespawnInventory(){
    }

    @Override
    public void register(Player player){
        PlayerInventory inventory = ((EntityHuman)player).getInventory();
        Main.canArmor.put(player,true);
        inventory.clearAll();
        Main.canArmor.put(player,false);
        Item item = Item.get(Item.WOODEN_SWORD,0,1);
        item.setCustomName(Main.getMessage(player,"item.soldier"));
        Item item2 = Item.get(Item.BOW,0,1);
        item2.setCustomName(Main.getMessage(player,"item.archer"));
        Item item3 = Item.get(Item.WOODEN_PICKAXE,0,1);
        item3.setCustomName(Main.getMessage(player,"item.builder"));
        int set = 0;
        int set2 = 1;
        int set3 = 2;
        inventory.setHotbarSlotIndex(set,set);
        inventory.setItem(set,item);
        inventory.setHotbarSlotIndex(set2,set2);
        inventory.setItem(set2,item2);
        inventory.setHotbarSlotIndex(set3,set3);
        inventory.setItem(set3,item3);
        inventory.sendContents(player);
    }

    @Override
    public void Function(Player player,Item item){
        Game game = GameProvider.getPlayingGame(player);
        if(!(game instanceof Miniwalls)) return;
        Miniwalls miniwalls = (Miniwalls) game;
        switch(item.getId()+":"+item.getDamage()){
            case "268:0":
                miniwalls.setJob(player, 0);
            break;
            case "261:0":
                miniwalls.setJob(player, 1);
            break;
            case "270:0":
                miniwalls.setJob(player, 2);
            break;
        }
    }
}