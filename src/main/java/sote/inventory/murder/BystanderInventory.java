package sote.inventory.murder;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import sote.Game;
import sote.GameProvider;
import sote.Main;
import sote.inventory.Inventory;
import sote.inventory.Inventorys;
import sote.murder.Murder;
import sote.murder.weapon.Weapons;

public class BystanderInventory extends Inventory{

    public BystanderInventory(){
    }

    @Override
    public void register(Player player){
        PlayerInventory inventory = ((EntityHuman)player).getInventory();
        Main.canArmor.put(player,true);
        inventory.clearAll();
        Main.canArmor.put(player,false);
        Item item;
        Game game = GameProvider.getPlayingGame(player);
        if(!(game instanceof Murder)) return;
        Murder murder = (Murder) game;
        if(murder.havegun.get(player) == true){
            item = Weapons.getItemGun(player);
            inventory.setHotbarSlotIndex(0,0);
            inventory.setItem(0,item);
        }
        Item item2 = Item.get(Item.EMERALD,0,murder.emeralds.get(player));
        item2.setCustomName(Main.getMessage(player, "item.murder.emerald"));
        Item item3 = Item.get(Item.BOOK,0,1);
        item3.setCustomName(Main.getMessage(player,"item.sendchat"));
        inventory.setHotbarSlotIndex(7,7);
        inventory.setItem(7,item2);
        inventory.setHotbarSlotIndex(8,8);
        inventory.setItem(8,item3);
        Main.canArmor.put(player,true);
        inventory.setHelmet(murder.helmet.get(player));
        inventory.setChestplate(murder.chestplate.get(player));
        inventory.setLeggings(murder.leggings.get(player));
        inventory.setBoots(murder.boots.get(player));
        Main.canArmor.put(player,false);
        inventory.sendContents(player);
    }

    @Override
    public void Function(Player player,Item item){
        switch(item.getId()+":"+item.getDamage()){
            case "340:0":
                Inventorys.setData(player, new MurderSendChatInventory(1));
            break;
        }
    }
}