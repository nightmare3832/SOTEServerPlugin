package sote.inventory.blockhunt;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import sote.Main;
import sote.inventory.Inventory;

public class BlockhuntHiderInventory extends Inventory{

    public BlockhuntHiderInventory(){
    }

    @Override
    public void register(Player player){
        PlayerInventory inventory = ((EntityHuman)player).getInventory();
        Main.canArmor.put(player,true);
        inventory.clearAll();
        //if(i == 1){
            Item item = Item.get(Item.WOODEN_SWORD,0,1);
            inventory.setHotbarSlotIndex(0,0);
            inventory.setItem(0,item);
            Item item2 = Item.get(Item.COMPASS,0,1);
            inventory.setHotbarSlotIndex(1,1);
            inventory.setItem(1,item2);
        //}
        inventory.sendContents(player);
        Main.canArmor.put(player,false);
    }

    @Override
    public void Function(Player player,Item item){
        switch(item.getId()+":"+item.getDamage()){
        }
    }
}