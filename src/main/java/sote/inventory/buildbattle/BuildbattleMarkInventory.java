package sote.inventory.buildbattle;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import sote.Game;
import sote.GameProvider;
import sote.Main;
import sote.buildbattle.Buildbattle;
import sote.inventory.Inventory;

public class BuildbattleMarkInventory extends Inventory{

    public BuildbattleMarkInventory(){
    }

    @Override
    public void register(Player player){
        PlayerInventory inventory = ((EntityHuman)player).getInventory();
        Main.canArmor.put(player,true);
        inventory.clearAll();
        Main.canArmor.put(player,false);
        inventory.sendContents(player);
        Item item = Item.get(Item.CLAY_BLOCK,14,1);
        item.setCustomName(Main.getMessage(player,"item.buildbattle.superpoop"));
        Item item2 = Item.get(Item.CLAY_BLOCK,6,1);
        item2.setCustomName(Main.getMessage(player,"item.buildbattle.poop"));
        Item item3 = Item.get(Item.CLAY_BLOCK,5,1);
        item3.setCustomName(Main.getMessage(player,"item.buildbattle.ok"));
        Item item4 = Item.get(Item.CLAY_BLOCK,13,1);
        item4.setCustomName(Main.getMessage(player,"item.buildbattle.good"));
        Item item5 = Item.get(Item.CLAY_BLOCK,11,1);
        item5.setCustomName(Main.getMessage(player,"item.buildbattle.epic"));
        Item item6 = Item.get(Item.CLAY_BLOCK,4,1);
        item6.setCustomName(Main.getMessage(player,"item.buildbattle.legendary"));
        int set = 0;
        int set2 = 1;
        int set3 = 2;
        int set4 = 3;
        int set5 = 4;
        int set6 = 5;
        inventory.setHotbarSlotIndex(set,set);
        inventory.setItem(set,item);
        inventory.setHotbarSlotIndex(set2,set2);
        inventory.setItem(set2,item2);
        inventory.setHotbarSlotIndex(set3,set3);
        inventory.setItem(set3,item3);
        inventory.setHotbarSlotIndex(set4,set4);
        inventory.setItem(set4,item4);
        inventory.setHotbarSlotIndex(set5,set5);
        inventory.setItem(set5,item5);
        inventory.setHotbarSlotIndex(set6,set6);
        inventory.setItem(set6,item6);
        inventory.sendContents(player);
    }

    @Override
    public void Function(Player player,Item item){
        Game game = GameProvider.getPlayingGame(player);
        if(!(game instanceof Buildbattle)) return;
        Buildbattle buildbattle = (Buildbattle) game;
        switch(item.getId()+":"+item.getDamage()){
            case "159:14":
                buildbattle.addMark(player, 1);
            break;
            case "159:6":
                buildbattle.addMark(player, 2);
            break;
            case "159:5":
                buildbattle.addMark(player, 3);
            break;
            case "159:13":
                buildbattle.addMark(player, 4);
            break;
            case "159:11":
                buildbattle.addMark(player, 5);
            break;
            case "159:4":
                buildbattle.addMark(player, 6);
            break;
        }
    }
}