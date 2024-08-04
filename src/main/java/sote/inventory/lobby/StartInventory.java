package sote.inventory.lobby;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import sote.Main;
import sote.inventory.Inventory;
import sote.inventory.ProfileChestInventory;
import sote.inventory.ServerChestInventorys;
import sote.inventory.murder.MurderStatsProfile;

public class StartInventory extends Inventory{

    public StartInventory(){
    }

    @Override
    public void register(Player player){
        PlayerInventory inventory = ((EntityHuman)player).getInventory();
        Main.canArmor.put(player,true);
        inventory.clearAll();
        Main.canArmor.put(player,false);
        Item item = Item.get(397,3,1);
        item.setCustomName(Main.getMessage(player,"item.status"));
        Item item4 = Item.get(54,0,1);
        item4.setCustomName(Main.getMessage(player,"item.lobbyitem"));
        Item item2 = Item.get(403,0,1);
        item2.setCustomName(Main.getMessage(player,"item.gamemenu"));
        inventory.setHotbarSlotIndex(0,0);
        inventory.setItem(0,item);
        inventory.setHotbarSlotIndex(1,1);
        inventory.setItem(1,item2);
        inventory.setHotbarSlotIndex(8,8);
        inventory.setItem(8,item4);
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
            case "403:0":
                //ServerChestInventorys.setData(player,new GameSelectChestInventory(player));
                //Inventorys.setData(player, new GameSelectInventory(0));
            break;
            case "404:0":
                //ServerChestInventorys.setData(player,new SettingChestInventory());
                //Inventorys.setData(player, new SettingInventory());
            break;
            case "54:0":
                //ServerChestInventorys.setData(player, new LobbyItemSelectChestInventory(player));
                //Inventorys.setData(player, new LobbyItemSelectInventory());
            break;
        }
    }
}