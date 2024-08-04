package sote.inventory.blockhunt;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import sote.Main;
import sote.inventory.Inventory;

public class BlockhuntSeekerInventory extends Inventory{

    public BlockhuntSeekerInventory(){
    }

    @Override
    public void register(Player player){
        PlayerInventory inventory = ((EntityHuman)player).getInventory();
        Main.canArmor.put(player,true);
        inventory.clearAll();
        Item item = Item.get(Item.DIAMOND_SWORD,0,1);
        inventory.setHotbarSlotIndex(0,0);
        inventory.setItem(0,item);
        Item item2 = Item.get(Item.IRON_HELMET,0,1);
        inventory.setHelmet(item2);
        Item item3 = Item.get(Item.IRON_CHESTPLATE,0,1);
        inventory.setChestplate(item3);
        Item item4 = Item.get(Item.IRON_LEGGINGS,0,1);
        inventory.setLeggings(item4);
        Item item5 = Item.get(Item.IRON_BOOTS,0,1);
        inventory.setBoots(item5);
        inventory.sendContents(player);
        Main.canArmor.put(player,false);
    }

    @Override
    public void Function(Player player,Item item){
        switch(item.getId()+":"+item.getDamage()){
        }
    }
}