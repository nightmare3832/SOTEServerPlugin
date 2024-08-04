package sote.inventory.miniwalls;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import sote.Game;
import sote.GameProvider;
import sote.Main;
import sote.inventory.Inventory;
import sote.inventory.ServerChestInventorys;
import sote.miniwalls.Miniwalls;

public class MiniwallsDeathInventory extends Inventory{

    public MiniwallsDeathInventory(){
    }

    @Override
    public void register(Player player){
        PlayerInventory inventory = ((EntityHuman)player).getInventory();
        Main.canArmor.put(player,true);
        inventory.clearAll();
        Main.canArmor.put(player,false);
        Item item = Item.get(Item.COMPASS,0,1);
        item.setCustomName(Main.getMessage(player,"item.teleport.selector"));
        Item item2 = Item.get(Item.CLOCK,0,1);
        item2.setCustomName(Main.getMessage(player,"item.leavegame"));
        inventory.setHotbarSlotIndex(0,0);
        inventory.setItem(0,item);
        inventory.setHotbarSlotIndex(8,8);
        inventory.setItem(8,item2);
        inventory.sendContents(player);
    }

    @Override
    public void Function(Player player,Item item){
        switch(item.getId()+":"+item.getDamage()){
            case "345:0":
                Game game = GameProvider.getPlayingGame(player);
                if(!(game instanceof Miniwalls)) return;
                if(game.Players.containsValue(player)){
                    if(game.canOpenChest) ServerChestInventorys.setData(player, new MiniwallsTeleportSelectorChestInventory(player));
                }
            break;
            case "347:0":
                game = GameProvider.getPlayingGame(player);
                if(!(game instanceof Miniwalls)) return;
                game.Quit(player);
            break;
        }
    }
}