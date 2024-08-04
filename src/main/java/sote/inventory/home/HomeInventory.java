package sote.inventory.home;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import sote.Main;
import sote.inventory.Inventory;
import sote.inventory.ServerChestInventorys;
import sote.stat.Stat;

public class HomeInventory extends Inventory{

    public HomeInventory(){
    }

    @Override
    public void register(Player player){
        PlayerInventory inventory = ((EntityHuman)player).getInventory();
        Main.canArmor.put(player,true);
        inventory.clearAll();
        Main.canArmor.put(player,false);
        Item item = Item.get(Item.CLOCK,0,1);
        item.setCustomName(Main.getMessage(player,"item.leavehome"));
        Item item2 = Item.get(Item.GLISTERING_MELON,0,1);
        item2.setCustomName(Main.getMessage(player,"item.home.option"));
        inventory.setHotbarSlotIndex(8,8);
        inventory.setItem(8,item);
        inventory.setHotbarSlotIndex(7,7);
        //inventory.setItem(7,item2);
        inventory.sendContents(player);
    }

    @Override
    public void Function(Player player,Item item){//TODO
        switch(item.getId()+":"+item.getDamage()){
            case "397:3":
                Stat.sendStat(player);
            break;
            case "347:0":
                //Inventorys.setData(player, new StartInventory());
                //player.teleport(Server.getInstance().getLevelByName("lobby").getSafeSpawn());
            break;
            case "382:0":
                ServerChestInventorys.setData(player, new HomeOptionChestInventory(player));
            break;
        }
    }
}