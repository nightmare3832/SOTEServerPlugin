package sote.inventory.home;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import sote.Main;
import sote.inventory.Inventory;
import sote.inventory.Inventorys;

public class HomeGameSelectInventory extends Inventory{

    public HomeGameSelectInventory(Inventory b){
        this.back = b;
    }

    @Override
    public void register(Player player){
        PlayerInventory inventory = ((EntityHuman)player).getInventory();
        Main.canArmor.put(player,true);
        inventory.clearAll();
        Main.canArmor.put(player,false);
        Item item = Item.get(Item.GHAST_TEAR,0,1);
        item.setCustomName(Main.getMessage(player,"item.back"));
        Item item2 = Item.get(Item.REDSTONE_DUST,0,1);
        item2.setCustomName(Main.getMessage(player,"item.murder"));
        Item item3 = Item.get(Item.FEATHER,0,1);
        item3.setCustomName(Main.getMessage(player,"item.skywars"));
        Item item4 = Item.get(Item.WOOD,0,1);
        item4.setCustomName(Main.getMessage(player,"item.buildbattle"));
        inventory.setHotbarSlotIndex(0,0);
        inventory.setItem(0,item);
        inventory.setHotbarSlotIndex(1,1);
        inventory.setItem(1,item2);
        inventory.setHotbarSlotIndex(2,2);
        inventory.setItem(2,item3);
        inventory.setHotbarSlotIndex(3,3);
        inventory.setItem(3,item4);
        inventory.sendContents(player);
    }

    @Override
    public void Function(Player player,Item item){
        if(player.getLevel().getFolderName().split("home").length < 2) return;
        String id = player.getLevel().getFolderName().split("home")[1];
        switch(item.getId()+":"+item.getDamage()){
            case "370:0":
                Inventorys.setData(player, back);
            break;
            case "331:0":
                //Home.startGame(id,"murder",player);
            break;
            case "288:0":
                //Home.startGame(id,"skywars",player);
            break;
            case "17:0":
                //Home.startGame(id,"buildbattle",player);
            break;
        }
    }

    public Inventory back;
}