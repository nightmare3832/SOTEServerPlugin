package sote.inventory.buildbattle;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import sote.Main;
import sote.inventory.Inventory;
import sote.inventory.Inventorys;
import sote.inventory.ServerChestInventorys;
import sote.inventory.lobby.GameSelectChestInventory;
import sote.inventory.lobby.StartInventory;
import sote.stat.Stat;

public class BuildbattleLobbyInventory extends Inventory{

    public BuildbattleLobbyInventory(){
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
        inventory.setHotbarSlotIndex(0,0);
        inventory.setItem(0,item);
        inventory.setHotbarSlotIndex(1,1);
        inventory.setItem(1,item2);
        inventory.sendContents(player);
    }

    @Override
    public void Function(Player player,Item item){
        switch(item.getId()+":"+item.getDamage()){
            case "403:0":
                ServerChestInventorys.setData(player,new GameSelectChestInventory(player));
                //Inventorys.setData(player, new GameSelectInventory(5));
            break;
            case "397:3":
                Stat.sendStat(player);
            break;
            case "330:0":
                Inventorys.setData(player, new StartInventory());
                player.teleport(Server.getInstance().getLevelByName("lobby").getSafeSpawn());
            break;
        }
    }
}