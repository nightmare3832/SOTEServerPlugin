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
import sote.murder.achievements.MurderAchievements;
import sote.murder.weapon.Weapons;
import sote.stat.Stat;

public class MurderInventory extends Inventory{

    public MurderInventory(){
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
        Item item2 = Item.get(Item.EMERALD,0,murder.emeralds.get(player));
        item2.setCustomName(Main.getMessage(player, "item.murder.emerald"));
        Item item3 = Item.get(Item.DYE,murder.onemurder.get(player),1);
        if(murder.onemurder.get(player) == 8) item3.setCustomName(Main.getMessage(player, "item.murder.buyknife.off"));
        else if(murder.onemurder.get(player) == 10) item3.setCustomName(Main.getMessage(player, "item.murder.buyknife.on"));
        Item item4 = Item.get(Item.DYE,murder.twomurder.get(player),1);
        if(murder.twomurder.get(player) == 8) item4.setCustomName(Main.getMessage(player, "item.murder.switchidentity.off"));
        else if(murder.twomurder.get(player) == 13) item4.setCustomName(Main.getMessage(player, "item.murder.switchidentity.on"));
        Item item5 = Item.get(340,0,1);
        item5.setCustomName(Main.getMessage(player,"item.sendchat"));
        inventory.setHotbarSlotIndex(7,7);
        inventory.setItem(7,item2);
        inventory.setHotbarSlotIndex(3,3);
        inventory.setItem(3,item3);
        inventory.setHotbarSlotIndex(4,4);
        inventory.setItem(4,item4);
        inventory.setHotbarSlotIndex(8,8);
        inventory.setItem(8,item5);
        item = Weapons.getItemKnife(player);
        if(murder.knifecount.get(player) > murder.haveknife.get(player)) murder.knifecount.put(player,  murder.haveknife.get(player));
        for(int a = 0; a < murder.knifecount.get(player);a++){
            inventory.addItem(item.clone());
        }
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
                Inventorys.setData(player, new MurderSendChatInventory(0));
            break;
            case "351:10":
                Game game = GameProvider.getPlayingGame(player);
                if(!(game instanceof Murder)) return;
                Murder murder = (Murder) game;
                if(!(murder.emeralds.get(player) >= 5)) return;
                murder.knifecount.put(player, murder.knifecount.get(player)+1);
                murder.haveknife.put(player, murder.haveknife.get(player)+1);
                if(murder.haveknife.get(player) >= 3){
                    MurderAchievements.setLevel(player, "Double_Knives", 2);
                }
                murder.emeralds.put(player, murder.emeralds.get(player) - 5);
                Stat.setMurderWeaponTraded(player, Stat.getMurderWeaponTraded(player) + 1);
                player.sendMessage(Main.getMessage(player,  "murder.murder.get.more.knife"));
                if(murder.emeralds.get(player) >= 3){
                    murder.onemurder.put(player, 13);
                }else{
                    murder.onemurder.put(player, 8);
                }
                if(murder.emeralds.get(player) >= 5){
                    murder.twomurder.put(player, 10);
                }else{
                    murder.twomurder.put(player, 8);
                }
                this.register(player);
            break;
        }
    }
}