package sote.inventory.skywars;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import sote.Main;
import sote.inventory.Inventory;

public class SkywarsSoloCageInventory extends Inventory{

    public SkywarsSoloCageInventory(){
    }

    @Override
    public void register(Player player){
        PlayerInventory inventory = ((EntityHuman)player).getInventory();
        Main.canArmor.put(player,true);
        inventory.clearAll();
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