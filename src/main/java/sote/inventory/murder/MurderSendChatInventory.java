package sote.inventory.murder;

import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import sote.Game;
import sote.GameProvider;
import sote.Main;
import sote.inventory.Inventory;
import sote.inventory.Inventorys;
import sote.murder.Murder;

public class MurderSendChatInventory extends Inventory{

    public MurderSendChatInventory(int w){
        number = w;
    }

    @Override
    public void register(Player player){
        PlayerInventory inventory = ((EntityHuman)player).getInventory();
        Main.canArmor.put(player,true);
        inventory.clearAll();
        Main.canArmor.put(player,false);
        Item item = Item.get(Item.REDSTONE_DUST,0,1);
        item.setCustomName(Main.getMessage(player,"item.back"));
        Item item3 = Item.get(Item.DYE,1,1);
        item3.setCustomName(Main.getMessage(player,"item.murder.chat1"));
        Item item4 = Item.get(Item.DYE,14,1);
        item4.setCustomName(Main.getMessage(player,"item.murder.chat2"));
        Item item5 = Item.get(Item.DYE,11,1);
        item5.setCustomName(Main.getMessage(player,"item.murder.chat3"));
        Item item6 = Item.get(Item.DYE,10,1);
        item6.setCustomName(Main.getMessage(player,"item.murder.chat4"));
        Item item7 = Item.get(Item.DYE,2,1);
        item7.setCustomName(Main.getMessage(player,"item.murder.chat5"));
        Item item8 = Item.get(Item.DYE,4,1);
        item8.setCustomName(Main.getMessage(player,"item.murder.chat6"));
        Item item9 = Item.get(Item.DYE,6,1);
        item9.setCustomName(Main.getMessage(player,"item.murder.chat7"));
        Item item10 = Item.get(Item.DYE,12,1);
        item10.setCustomName(Main.getMessage(player,"item.murder.chat8"));
        int set = 0;
        int set3 = 0;
        int set4 = 0;
        int set5 = 0;
        int set6 = 0;
        int set7 = 0;
        int set8 = 0;
        int set9 = 0;
        int set10 = 0;
        switch(Inventorys.getSize(player)){
            case 5:
                set = 4;
                set3 = 0;
                set4 = 1;
                set5 = 2;
                set6 = 3;
                set7 = 5;
                set8 = 6;
                set9 = 7;
                set10 = 8;
            break;
            case 6:
                set = 5;
                set3 = 0;
                set4 = 1;
                set5 = 2;
                set6 = 3;
                set7 = 4;
                set8 = 6;
                set9 = 7;
                set10 = 8;
            break;
            case 7:
                set = 6;
                set3 = 0;
                set4 = 1;
                set5 = 2;
                set6 = 3;
                set7 = 4;
                set8 = 5;
                set9 = 7;
                set10 = 8;
            break;
            case 8:
                set = 7;
                set3 = 0;
                set4 = 1;
                set5 = 2;
                set6 = 3;
                set7 = 4;
                set8 = 5;
                set9 = 6;
                set10 = 8;
            break;
            case 9:
                set = 8;
                set3 = 0;
                set4 = 1;
                set5 = 2;
                set6 = 3;
                set7 = 4;
                set8 = 5;
                set9 = 6;
                set10 = 7;
            break;
        }
        inventory.setHotbarSlotIndex(set,set);
        inventory.setItem(set,item);
        inventory.setHotbarSlotIndex(set4,set4);
        inventory.setItem(set4,item4);
        inventory.setHotbarSlotIndex(set3,set3);
        inventory.setItem(set3,item3);
        inventory.setHotbarSlotIndex(set5,set5);
        inventory.setItem(set5,item5);
        inventory.setHotbarSlotIndex(set6,set6);
        inventory.setItem(set6,item6);
        inventory.setHotbarSlotIndex(set7,set7);
        inventory.setItem(set7,item7);
        inventory.setHotbarSlotIndex(set8,set8);
        inventory.setItem(set8,item8);
        inventory.setHotbarSlotIndex(set9,set9);
        inventory.setItem(set9,item9);
        inventory.setHotbarSlotIndex(set10,set10);
        inventory.setItem(set10,item10);
        Game game = GameProvider.getPlayingGame(player);
        if(!(game instanceof Murder)) return;
        Murder murder = (Murder) game;
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
        PlayerChatEvent chatEvent;
        switch(item.getId()+":"+item.getDamage()){
            case "331:0":
                if(number == 0){
                    Game game = GameProvider.getPlayingGame(player);
                    if(!(game instanceof Murder)) return;
                    Murder murder = (Murder) game;
                    Inventorys.setData(player, new MurderInventory());
                    Inventorys.data2.get(player).register(player);
                }
                if(number == 1){
                    Game game = GameProvider.getPlayingGame(player);
                    if(!(game instanceof Murder)) return;
                    Murder murder = (Murder) game;
                    Inventorys.setData(player, new BystanderInventory());
                    Inventorys.data2.get(player).register(player);
                }
            break;
            case "351:1":
                chatEvent = new PlayerChatEvent(player,Main.getMessage(player,"item.murder.chat.c1"));
                Server.getInstance().getPluginManager().callEvent(chatEvent);
            break;
            case "351:14":
                chatEvent = new PlayerChatEvent(player,Main.getMessage(player,"item.murder.chat.c2"));
                Server.getInstance().getPluginManager().callEvent(chatEvent);
            break;
            case "351:11":
                chatEvent = new PlayerChatEvent(player,Main.getMessage(player,"item.murder.chat.c3"));
                Server.getInstance().getPluginManager().callEvent(chatEvent);
            break;
            case "351:10":
                String msg = "";
                int c = 0;
                Game game = GameProvider.getPlayingGame(player);
                if(!(game instanceof Murder)) return;
                Murder murder = (Murder) game;
                for (Map.Entry<String,Player> e : murder.Players.entrySet()){
                    if(player.distance(e.getValue()) <= 20 && !e.getValue().equals(player) && e.getValue().getGamemode() == 2){
                        if(!msg.equals("")) msg += " ";
                        msg += "§f"+e.getValue().getNameTag()+"§f";
                        c++;
                    }
                }
                if(!msg.equals("") && c != 0) chatEvent = new PlayerChatEvent(player,Main.getMessage(player,"item.murder.chat.c4",new String[]{msg}));
                else chatEvent = new PlayerChatEvent(player,Main.getMessage(player,"item.murder.chat.c9"));
                Server.getInstance().getPluginManager().callEvent(chatEvent);
            break;
            case "351:2":
                chatEvent = new PlayerChatEvent(player,Main.getMessage(player,"item.murder.chat.c5"));
                Server.getInstance().getPluginManager().callEvent(chatEvent);
            break;
            case "351:4":
                chatEvent = new PlayerChatEvent(player,Main.getMessage(player,"item.murder.chat.c6"));
                Server.getInstance().getPluginManager().callEvent(chatEvent);
            break;
            case "351:6":
                chatEvent = new PlayerChatEvent(player,Main.getMessage(player,"item.murder.chat.c7"));
                Server.getInstance().getPluginManager().callEvent(chatEvent);
            break;
            case "351:12":
                chatEvent = new PlayerChatEvent(player,Main.getMessage(player,"item.murder.chat.c8"));
                Server.getInstance().getPluginManager().callEvent(chatEvent);
            break;
        }
    }

    public int number;
}