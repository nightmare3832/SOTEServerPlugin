package sote.inventory.murder;

import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import sote.Game;
import sote.GameProvider;
import sote.Main;
import sote.inventory.Inventory;
import sote.inventory.Inventorys;
import sote.inventory.ServerChestInventorys;
import sote.murder.Murder;
import sote.stat.Stat;

public class MurderDeathInventory extends Inventory{

    public MurderDeathInventory(){
    }

    @Override
    public void register(Player player){
        PlayerInventory inventory = ((EntityHuman)player).getInventory();
        Main.canArmor.put(player,true);
        inventory.clearAll();
        Main.canArmor.put(player,false);
        Item item = Item.get(Item.COMPASS);
        item.setCustomName(Main.getMessage(player, "item.teleport.selector"));
        Item item2 = Item.get(Item.CLOCK,0,1);
        item2.setCustomName(Main.getMessage(player,"item.leavegame"));
        int set = 0;
        int set2 = 1;
        switch(Inventorys.getSize(player)){
            case 5:
                set = 0;
                set2 = 4;
            break;
            case 6:
                set = 0;
                set2 = 5;
            break;
            case 7:
                set = 0;
               set2 = 6;
            break;
            case 8:
                set = 0;
               set2 = 7;
            break;
            case 9:
                set = 0;
               set2 = 8;
            break;
        }
        inventory.setHotbarSlotIndex(set,set);
        inventory.setItem(set,item);
        inventory.setHotbarSlotIndex(set2,set2);
        inventory.setItem(set2,item2);
        inventory.sendContents(player);
    }

    @Override
    public void Function(Player player,Item item){
        switch(item.getId()+":"+item.getDamage()){
            case "345:0":
                Game game = GameProvider.getPlayingGame(player);
                if(game instanceof Murder){
                    if(game.canOpenChest) ServerChestInventorys.setData(player, new MurderTeleportSelectorChestInventory(player));
                }
            break;
            case "347:0":
                Stat.setNameTag(player);
                game = GameProvider.getPlayingGame(player);
                Murder murder = (Murder) game;
                for (Map.Entry<String,Player> ee : game.AllPlayers.entrySet()){
                    player.showPlayer(ee.getValue());
                    ee.getValue().showPlayer(player);
                }
                int c = 0;
                for(Map.Entry<Long,Integer> ee : murder.eiddata.entrySet()){
                    if(ee.getKey() instanceof Long){
                        RemoveEntityPacket pk = new RemoveEntityPacket();
                        pk.eid = ee.getKey();
                        player.dataPacket(pk);
                        c++;
                    }
                }
                player.setAttribute(Attribute.getAttribute(Attribute.MAX_HUNGER).setValue(20));
                player.setAttribute(Attribute.getAttribute(Attribute.EXPERIENCE).setValue(0));
                player.setAttribute(Attribute.getAttribute(Attribute.EXPERIENCE_LEVEL).setValue(0));
                murder.Quit(player);
                player.setGamemode(2);
                player.setAllowFlight(false);
            break;
    }
    }
}