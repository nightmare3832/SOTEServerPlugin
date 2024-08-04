package sote.inventory.murder;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import sote.Main;
import sote.inventory.Inventory;
import sote.inventory.ProfileChestInventory;
import sote.inventory.ServerChestInventorys;

public class MurderLobbyInventory extends Inventory{

    public MurderLobbyInventory(){
    }

    @Override
    public void register(Player player){
        PlayerInventory inventory = ((EntityHuman)player).getInventory();
        Main.canArmor.put(player,true);
        inventory.clearAll();
        Main.canArmor.put(player,false);
        Item item = Item.get(Item.SKULL,3,1);
        item.setCustomName(Main.getMessage(player,"item.status"));
        Item item2 = Item.get(Item.ENCHANT_BOOK,0,1);
        item2.setCustomName(Main.getMessage(player,"item.gamemenu"));
        Item item3 = Item.get(Item.EMERALD,0,1);
        item3.setCustomName(Main.getMessage(player,"item.shop"));
        Item item4 = Item.get(Item.ENDER_CHEST,0,1);
        item4.setCustomName(Main.getMessage(player,"item.costume"));
        Item item5 = Item.get(Item.COMPARATOR,0,1);
        item5.setCustomName(Main.getMessage(player,"item.setting"));
        inventory.setHotbarSlotIndex(0,0);
        inventory.setItem(0,item);
        inventory.setHotbarSlotIndex(1,1);
        inventory.setItem(1,item2);
        inventory.setHotbarSlotIndex(4,4);
        inventory.setItem(4,item4);
        inventory.setHotbarSlotIndex(5,5);
        inventory.setItem(5,item3);
        inventory.setHotbarSlotIndex(8,8);
        //inventory.setItem(8,item5);
        inventory.sendContents(player);
    }

    @Override
    public void Function(Player player,Item item){
        switch(item.getId()+":"+item.getDamage()){
            case "403:0":
                //ServerChestInventorys.setData(player,new GameSelectChestInventory());
                //Inventorys.setData(player, new GameSelectInventory(3));
            break;
            case "397:3":
                ProfileChestInventory p = new ProfileChestInventory(player);
                p.setProfile(new MurderStatsProfile(p));
                ServerChestInventorys.setData(player, p);
            break;
            case "330:0":
                //Inventorys.setData(player, new StartInventory());
                //player.teleport(Server.getInstance().getLevelByName("lobby").getSafeSpawn());
            break;
            case "388:0":
                ServerChestInventorys.setData(player, new MurderShopChestInventory(player));
            break;
            case "130:0":
                ServerChestInventorys.setData(player, new MurderCostumeChestInventory(player));
            break;
            case "404:0":
                //ServerChestInventorys.setData(player,new SettingChestInventory());
            break;
        }
    }
}