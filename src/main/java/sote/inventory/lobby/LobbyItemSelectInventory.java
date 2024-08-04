package sote.inventory.lobby;

import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import sote.Main;
import sote.inventory.Inventory;
import sote.inventory.Inventorys;
import sote.lobbyitem.LobbyItems;

public class LobbyItemSelectInventory extends Inventory{

    public LobbyItemSelectInventory(){
    }

    @Override
    public void register(Player player){
        PlayerInventory inventory = ((EntityHuman)player).getInventory();
        Main.canArmor.put(player,true);
        inventory.clearAll();
        Main.canArmor.put(player,false);
        Item item;
        item = Item.get(Item.GHAST_TEAR,0,1);
        item.setCustomName(Main.getMessage(player,"item.back"));
        inventory.setHotbarSlotIndex(0,0);
        inventory.setItem(0,item);
        int count = 1;
        Map<String, Boolean> map = (Map<String, Boolean>) LobbyItems.lobbyitemData.get(player.getName().toLowerCase());
        if(map.get("unknown")){
            item = Item.get(1,0,1);
            item.setCustomName(Main.getMessage(player,"item.unknown"));
            inventory.setHotbarSlotIndex(count,count);
            inventory.setItem(count,item);
            count++;
        }
        if(map.get("fireparticle")){
            item = Item.get(377,0,1);
            item.setCustomName(Main.getMessage(player,"item.fireparticle"));
            inventory.setHotbarSlotIndex(count,count);
            inventory.setItem(count,item);
            count++;
        }
        if(map.get("flameparticle")){
            item = Item.get(378,0,1);
            item.setCustomName(Main.getMessage(player,"item.flameparticle"));
            inventory.setHotbarSlotIndex(count,count);
            inventory.setItem(count,item);
            count++;
        }
        if(map.get("waterparticle")){
            item = Item.get(373,0,1);
            item.setCustomName(Main.getMessage(player,"item.waterparticle"));
            inventory.setHotbarSlotIndex(count,count);
            inventory.setItem(count,item);
            count++;
        }
        if(map.get("musicparticle")){
            item = Item.get(25,0,1);
            item.setCustomName(Main.getMessage(player,"item.musicparticle"));
            inventory.setHotbarSlotIndex(count,count);
            inventory.setItem(count,item);
            count++;
        }
        if(map.get("smokeparticle")){
            item = Item.get(369,0,1);
            item.setCustomName(Main.getMessage(player,"item.smokeparticle"));
            inventory.setHotbarSlotIndex(count,count);
            inventory.setItem(count,item);
            count++;
        }
        if(map.get("heartparticle")){
            item = Item.get(1,0,1);
            item.setCustomName(Main.getMessage(player,"item.heartparticle"));
            inventory.setHotbarSlotIndex(count,count);
            inventory.setItem(count,item);
            count++;
        }
        if(map.get("colorparticle")){
            item = Item.get(1,0,1);
            item.setCustomName(Main.getMessage(player,"item.colorparticle"));
            inventory.setHotbarSlotIndex(count,count);
            inventory.setItem(count,item);
            count++;
        }
        if(map.get("rainbowparticle")){
            item = Item.get(1,0,1);
            item.setCustomName(Main.getMessage(player,"item.rainbowparticle"));
            inventory.setHotbarSlotIndex(count,count);
            inventory.setItem(count,item);
            count++;
        }
        if(map.get("soteparticle")){
            item = Item.get(1,0,1);
            item.setCustomName(Main.getMessage(player,"item.soteparticle"));
            inventory.setHotbarSlotIndex(count,count);
            inventory.setItem(count,item);
            count++;
        }
        if(map.get("enderparticle")){
            item = Item.get(1,0,1);
            item.setCustomName(Main.getMessage(player,"item.enderparticle"));
            inventory.setHotbarSlotIndex(count,count);
            inventory.setItem(count,item);
            count++;
        }
        if(map.get("bombluncher")){
            item = Item.get(46,0,1);
            item.setCustomName(Main.getMessage(player,"item.bombluncher"));
            inventory.setHotbarSlotIndex(count,count);
            inventory.setItem(count,item);
            count++;
        }
        if(map.get("blockluncher")){
            item = Item.get(2,0,1);
            item.setCustomName(Main.getMessage(player,"item.blockluncher"));
            inventory.setHotbarSlotIndex(count,count);
            inventory.setItem(count,item);
            count++;
        }
        inventory.sendContents(player);
    }

    @Override
    public void Function(Player player,Item item){
        if(item.getId() == 370){
            Inventorys.setData(player, new StartInventory());
        }else{
            LobbyItems.setSellectLobbyItem(player,item);
            player.sendMessage(Main.getMessage(player,"lobbyitem.set"));
        }
    }
}