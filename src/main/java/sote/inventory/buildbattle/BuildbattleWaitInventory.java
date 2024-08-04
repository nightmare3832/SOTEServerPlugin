package sote.inventory.buildbattle;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import sote.Game;
import sote.GameProvider;
import sote.Main;
import sote.buildbattle.Buildbattle;
import sote.inventory.Inventory;

public class BuildbattleWaitInventory extends Inventory{

    public BuildbattleWaitInventory(){
    }

    @Override
    public void register(Player player){
        PlayerInventory inventory = ((EntityHuman)player).getInventory();
        Main.canArmor.put(player,true);
        inventory.clearAll();
        Main.canArmor.put(player,false);
        Item item = Item.get(Item.PAPER,0,1);
        item.setCustomName(Main.getMessage(player,"item.status"));
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
            case "339:0":
                //Stat.sendBuildbattleStat(player);
            break;
            case "347:0":
                Game game = GameProvider.getPlayingGame(player);
                if(!(game instanceof Buildbattle)) return;
                game.Quit(player);
            break;
        }
    }
}