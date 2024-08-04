package sote.inventory.bedwars;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.inventory.PlayerInventory;
import sote.Main;
import sote.inventory.Inventory;

public class BedwarsRespawningInventory extends Inventory{

    public BedwarsRespawningInventory(){
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
}