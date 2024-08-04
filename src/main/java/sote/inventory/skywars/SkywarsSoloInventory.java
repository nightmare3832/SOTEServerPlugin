package sote.inventory.skywars;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import sote.Main;
import sote.inventory.Inventory;
import sote.skywarssolo.kit.SkywarsSoloKits;

public class SkywarsSoloInventory extends Inventory{

    public SkywarsSoloInventory(){
    }

    @Override
    public void register(Player player){
        PlayerInventory inventory = ((EntityHuman)player).getInventory();
        Main.canArmor.put(player,true);
        inventory.clearAll();
        Item[] items = SkywarsSoloKits.Kitdata.get(player).getItems();
        int count = 0;
            for(Item item: items){
                if(item.isHelmet()){
                    inventory.setHelmet(item);
                }else if(item.isChestplate()){
                    inventory.setChestplate(item);
                }else if(item.isLeggings()){
                    inventory.setLeggings(item);
                }else if(item.isBoots()){
                    inventory.setBoots(item);
                }else{
                    inventory.setHotbarSlotIndex(count,count);
                    inventory.setItem(count,item);
                    count++;
                }
            }
        Main.canArmor.put(player,false);
        inventory.sendContents(player);
        inventory.sendArmorContents(player);
    }

    @Override
    public void Function(Player player,Item item){
        switch(item.getId()+":"+item.getDamage()){
        }
    }
}