package sote.inventory.skywars;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import sote.Game;
import sote.GameProvider;
import sote.Main;
import sote.inventory.Inventory;
import sote.inventory.Inventorys;
import sote.inventory.ProfileChestInventory;
import sote.inventory.ServerChestInventorys;
import sote.skywarssolo.SkywarsSolo;

public class SkywarsSoloWaitInventory extends Inventory{

    public SkywarsSoloWaitInventory(){
    }

    @Override
    public void register(Player player){
        PlayerInventory inventory = ((EntityHuman)player).getInventory();
        Main.canArmor.put(player,true);
        inventory.clearAll();
        Main.canArmor.put(player,false);
        Item item = Item.get(397,3,1);
        item.setCustomName(Main.getMessage(player,"item.status"));
        Item item2 = Item.get(347,0,1);
        item2.setCustomName(Main.getMessage(player,"item.leavegame"));
        int set = 0;
        int set2 = 0;
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
            case "397:3":
                ProfileChestInventory p = new ProfileChestInventory(player);
                p.setProfile(new SkywarsStatsProfile(p));
                ServerChestInventorys.setData(player, p);
            break;
            case "347:0":
                Game game = GameProvider.getPlayingGame(player);
                if(!(game instanceof SkywarsSolo)) return;
                game.Quit(player);
            break;
        }
    }
}