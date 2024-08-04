package sote.event;

import cn.nukkit.Player;
import cn.nukkit.event.inventory.InventoryOpenEvent;
import cn.nukkit.inventory.EnchantInventory;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import cn.nukkit.utils.DyeColor;
import sote.Game;
import sote.GameProvider;
import sote.skywarssolo.SkywarsSolo;

public class Event_InventoryOpenEvent{

    public Event_InventoryOpenEvent(){
    }

    public static void onOpen(InventoryOpenEvent event){
        Player player = event.getPlayer();
        Game game = GameProvider.getPlayingGame(player);
        if(game instanceof SkywarsSolo){
            Inventory inv = event.getInventory();
            if(inv instanceof EnchantInventory){
                ((EnchantInventory)inv).setting = 1;
                inv.setItem(1, Item.get(Item.DYE, DyeColor.BLUE.getDyeData(), 3));
            }
        }
    }
}