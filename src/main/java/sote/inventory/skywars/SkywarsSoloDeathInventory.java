package sote.inventory.skywars;

import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import sote.Game;
import sote.GameProvider;
import sote.Main;
import sote.inventory.Inventory;
import sote.inventory.ServerChestInventorys;
import sote.skywarssolo.SkywarsSolo;
import sote.stat.Stat;

public class SkywarsSoloDeathInventory extends Inventory{

    public SkywarsSoloDeathInventory(){
    }

    @Override
    public void register(Player player){
        PlayerInventory inventory = ((EntityHuman)player).getInventory();
        Main.canArmor.put(player,true);
        inventory.clearAll();
        Main.canArmor.put(player,false);
        Item item;
        item = Item.get(347,0,1);
        item.setCustomName(Main.getMessage(player,"item.leavegame"));
        inventory.setHotbarSlotIndex(8,8);
        inventory.setItem(8,item);
        item = Item.get(Item.COMPASS,0,1);
        item.setCustomName(Main.getMessage(player,"item.teleport.selector"));
        inventory.setHotbarSlotIndex(0,0);
        inventory.setItem(0,item);
        inventory.sendContents(player);
    }

    @Override
    public void Function(Player player,Item item){
        if(item.getId() == 347){
            Game game = GameProvider.getPlayingGame(player);
            if(!(game instanceof SkywarsSolo)) return;
            for (Map.Entry<String,Player> ee : game.AllPlayers.entrySet()){
                player.showPlayer(ee.getValue());
                ee.getValue().showPlayer(player);
            }
            player.setGamemode(2);
            player.setGamemode(1);
            player.setGamemode(2);
            Stat.setNameTag(player);
            player.setAttribute(Attribute.getAttribute(Attribute.MAX_HUNGER).setValue(20));
            player.setAttribute(Attribute.getAttribute(Attribute.EXPERIENCE).setValue(0));
            player.setAttribute(Attribute.getAttribute(Attribute.EXPERIENCE_LEVEL).setValue(0));
            player.setAllowFlight(false);
            game.Quit(player);
        }else if(item.getId() == Item.COMPASS){
            Game game = GameProvider.getPlayingGame(player);
            if(!(game instanceof SkywarsSolo)) return;
            if(game.canOpenChest) ServerChestInventorys.setData(player, new SkywarsSoloTeleportSelectorChestInventory(player));
        }
    }
}