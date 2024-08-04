package sote.inventory.murder;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import sote.Game;
import sote.GameProvider;
import sote.Main;
import sote.inventory.Inventory;
import sote.inventory.Inventorys;
import sote.inventory.ProfileChestInventory;
import sote.inventory.ServerChestInventorys;
import sote.murder.Murder;

public class MurderWaitInventory extends Inventory{

    public MurderWaitInventory(){
    }

    @Override
    public void register(Player player){
        PlayerInventory inventory = ((EntityHuman)player).getInventory();
        Main.canArmor.put(player,true);
        inventory.clearAll();
        Main.canArmor.put(player,false);
        Item item = Item.get(Item.SKULL,3,1);
        item.setCustomName(Main.getMessage(player,"item.status"));
        Item item2 = Item.get(Item.CLOCK,0,1);
        item2.setCustomName(Main.getMessage(player,"item.leavegame"));
        Item item3 = Item.get(Item.EMERALD,0,1);
        item3.setCustomName(Main.getMessage(player,"item.shop"));
        Item item4 = Item.get(Item.ENDER_CHEST,0,1);
        item4.setCustomName(Main.getMessage(player,"item.costume"));
        Item item5 = Item.get(Item.PAINTING,0,1);
        item5.setCustomName(Main.getMessage(player,"item.vote"));
        int set = 0;
        int set2 = 0;
        int set3 = 0;
        int set4 = 0;
        int set5 = 0;
        switch(Inventorys.getSize(player)){
            case 5:
                set = 0;
                set3 = 1;
                set4 = 2;
                set5 = 3;
                set2 = 4;
            break;
            case 6:
                set = 0;
                set3 = 2;
                set4 = 3;
                set5 = 4;
                set2 = 5;
            break;
            case 7:
                set = 0;
                set3 = 2;
                set4 = 3;
                set5 = 5;
                set2 = 6;
            break;
            case 8:
                set = 0;
                set3 = 3;
                set4 = 4;
                set5 = 6;
                set2 = 7;
            break;
            case 9:
                set = 0;
                set3 = 3;
                set4 = 4;
                set5 = 7;
                set2 = 8;
            break;
        }
        inventory.setHotbarSlotIndex(set,set);
        inventory.setItem(set,item);
        inventory.setHotbarSlotIndex(set2,set2);
        inventory.setItem(set2,item2);
        inventory.setHotbarSlotIndex(set3,set3);
        inventory.setItem(set3,item3);
        inventory.setHotbarSlotIndex(set4,set4);
        inventory.setItem(set4,item4);
        inventory.setHotbarSlotIndex(set5,set5);
        Game game = GameProvider.getPlayingGame(player);
        Murder murder = (Murder) game;
        if(game.AllPlayers.containsValue(player) && murder.setting.get("map").equals("default")){
            inventory.setItem(set5,item5);
        }
        inventory.sendContents(player);
    }

    @Override
    public void Function(Player player,Item item){
        switch(item.getId()+":"+item.getDamage()){
            case "397:3":
                ProfileChestInventory p = new ProfileChestInventory(player);
                p.setProfile(new MurderStatsProfile(p));
                ServerChestInventorys.setData(player, p);
            break;
            case "347:0":
                Game game = GameProvider.getPlayingGame(player);
                if(!(game instanceof Murder)) return;
                game.Quit(player);
            break;
            case "388:0":
                ServerChestInventorys.setData(player, new MurderShopChestInventory(player));
            break;
            case "130:0":
                ServerChestInventorys.setData(player, new MurderCostumeChestInventory(player));
            break;
            case "321:0":
                ServerChestInventorys.setData(player, new MurderVoteChestInventory(player));
            break;
        }
    }
}