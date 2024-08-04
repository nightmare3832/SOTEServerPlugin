package sote.murder.hat;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import sote.Main;
import sote.PlayerClone;
import sote.murder.achievements.MurderAchievements;
import sote.stat.Stat;

public class MurderHats{

    public static LinkedHashMap<String, Item> item = new LinkedHashMap<String, Item>();
    public static LinkedHashMap<String, Integer> needHats = new LinkedHashMap<String, Integer>();
    public static LinkedHashMap<String, LinkedHashMap<Integer, Integer>> Conditions = new LinkedHashMap<String, LinkedHashMap<Integer, Integer>>();

    public static final int CONDITIONS_COIN = 0;
    public static final int CONDITIONS_BYSTANDER_WIN = 1;
    public static final int CONDITIONS_MURDER_WIN = 2;
    public static final int CONDITIONS_WEAPONS_TRADED = 3;
    public static final int CONDITIONS_HATS_OWNED = 4;
    public static final int CONDITIONS_EMERALD_COLLECTED = 5;
    public static final int CONDITIONS_BYSTANDER_KILLS = 6;
    public static final int CONDITIONS_MURDER_KILLS = 7;

    public MurderHats(){
        register();
    }

    public static void buyItem(Player player,String str){
        int coin = 0;
        coin = Conditions.get(str).get(CONDITIONS_COIN);
        Map<String, Boolean> map = (Map<String, Boolean>) hatData.get(player.getName().toLowerCase());
        if(map.get(str)){
            return;
        }
        if(!canBuy(player, str)){
            player.sendMessage("cant buy");
            return;
        }
            if(Stat.getCoin(player) >= coin){
                ((Map<String, Boolean>) hatData.get(player.getName().toLowerCase())).put(str,true);
                Stat.setCoin(player,Stat.getCoin(player)-coin);
                player.sendMessage(Main.getMessage(player,"shop.buy.item"));
                int value = (int)(10 * MurderAchievements.getLevel(player, "Hat_Owner"));
                if(getHaveHatCount(player) >= value){
                    MurderAchievements.setLevel(player, "Hat_Owner", MurderAchievements.getLevel(player, "Hat_Owner") + 1);
                }
            }else{
                player.sendMessage(Main.getMessage(player,"shop.no.coin"));
            }
    }

    public static void addPlayer(Player player){
        HashMap<String, Boolean> v1 = new HashMap<String, Boolean>();
        Stat.setMurderHat(player, "unknown");
        for (Map.Entry<String,Item> e : item.entrySet()){
            v1.put(e.getKey(), false);
        }
        hatData.put(player.getName().toLowerCase(),v1);
    }

    public static String getShopName(Player player, String str){
        if(str.equals("unknown")) return Main.getMessage(player, "item.murder.shop.hats.unknown");
        Map<String, Boolean> mapHat = (Map<String, Boolean>) hatData.get(player.getName().toLowerCase());
        String hatString = str.replace("_", " ");
        String coinString = String.valueOf(Conditions.get(str).get(CONDITIONS_COIN));
        if(mapHat.get(str)){
            return Main.getMessage(player, "item.murder.shop.hats", new String[]{hatString, coinString, Main.getMessage(player, "item.murder.shop.hats.unlocked")});
        }else{
            String conditions = "";
            boolean canBuy = true;
            boolean isFirst = true;
            int value = 0;
            for (Map.Entry<Integer, Integer> e : Conditions.get(str).entrySet()){
                switch(e.getKey()){
                    case CONDITIONS_COIN:
                        value = Stat.getCoin(player);
                        if(value < e.getValue()){
                            canBuy = false;
                            conditions += "\n" + Main.getMessage(player, "item.murder.shop.hats.conditions.coin", new String[]{String.valueOf(e.getValue() - value)});
                        }
                    break;
                    case CONDITIONS_BYSTANDER_WIN:
                        value = Stat.getMurderBystanderWin(player);
                        if(value < e.getValue()){
                            canBuy = false;
                            conditions += "\n" + Main.getMessage(player, "item.murder.shop.hats.conditions.bystander.win", new String[]{String.valueOf(e.getValue() - value)});
                        }
                    break;
                    case CONDITIONS_MURDER_WIN:
                        value = Stat.getMurderMurderWin(player);
                        if(value < e.getValue()){
                            canBuy = false;
                            conditions += "\n" + Main.getMessage(player, "item.murder.shop.hats.conditions.murder.win", new String[]{String.valueOf(e.getValue() - value)});
                        }
                    break;
                    case CONDITIONS_WEAPONS_TRADED:
                        value = Stat.getMurderWeaponTraded(player);
                        if(value < e.getValue()){
                            canBuy = false;
                            conditions += "\n" + Main.getMessage(player, "item.murder.shop.hats.conditions.weapons.traded", new String[]{String.valueOf(e.getValue() - value)});
                        }
                    break;
                    case CONDITIONS_HATS_OWNED:
                        value = getHaveHatCount(player);
                        if(value < e.getValue()){
                            canBuy = false;
                            conditions += "\n" + Main.getMessage(player, "item.murder.shop.hats.conditions.hats.owned", new String[]{String.valueOf(e.getValue() - value)});
                        }
                    break;
                    case CONDITIONS_EMERALD_COLLECTED:
                        value = Stat.getMurderEmeraldCollected(player);
                        if(value < e.getValue()){
                            canBuy = false;
                            conditions += "\n" + Main.getMessage(player, "item.murder.shop.hats.conditions.emerald.collected", new String[]{String.valueOf(e.getValue() - value)});
                        }
                    break;
                    case CONDITIONS_BYSTANDER_KILLS:
                        value = Stat.getMurderBystanderKills(player);
                        if(value < e.getValue()){
                            canBuy = false;
                            conditions += "\n" + Main.getMessage(player, "item.murder.shop.hats.conditions.bystander.kills", new String[]{String.valueOf(e.getValue() - value)});
                        }
                    break;
                    case CONDITIONS_MURDER_KILLS:
                        value = Stat.getMurderMurderKills(player);
                        if(value < e.getValue()){
                            canBuy = false;
                            conditions += "\n" + Main.getMessage(player, "item.murder.shop.hats.conditions.murder.kills", new String[]{String.valueOf(e.getValue() - value)});
                        }
                    break;
                }
            }
            if(!canBuy) return Main.getMessage(player, "item.murder.shop.hats", new String[]{hatString, coinString, conditions});
            else return Main.getMessage(player, "item.murder.shop.hats", new String[]{hatString, coinString, Main.getMessage(player, "item.murder.shop.hats.can.unlock")});
        }
    }

    public static String getConfirmName(Player player, String str){
        String hatString = str.replace("_", " ");
        String coinString = String.valueOf(Conditions.get(str).get(CONDITIONS_COIN));
        String conditions = "";
        for (Map.Entry<Integer, Integer> e : Conditions.get(str).entrySet()){
            switch(e.getKey()){
                case CONDITIONS_BYSTANDER_WIN:
                    conditions += "\n" + Main.getMessage(player, "item.murder.shop.hats.conditions.confirm.bystander.win", new String[]{String.valueOf(e.getValue())});
                break;
                case CONDITIONS_MURDER_WIN:
                    conditions += "\n" + Main.getMessage(player, "item.murder.shop.hats.conditions.confirm.murder.win", new String[]{String.valueOf(e.getValue())});
                break;
                case CONDITIONS_WEAPONS_TRADED:
                    conditions += "\n" + Main.getMessage(player, "item.murder.shop.hats.conditions.confirm.weapons.traded", new String[]{String.valueOf(e.getValue())});
                break;
                case CONDITIONS_HATS_OWNED:
                    conditions += "\n" + Main.getMessage(player, "item.murder.shop.hats.conditions.confirm.hats.owned", new String[]{String.valueOf(e.getValue())});
                break;
                case CONDITIONS_EMERALD_COLLECTED:
                    conditions += "\n" + Main.getMessage(player, "item.murder.shop.hats.conditions.confirm.emerald.collected", new String[]{String.valueOf(e.getValue())});
                break;
                case CONDITIONS_BYSTANDER_KILLS:
                    conditions += "\n" + Main.getMessage(player, "item.murder.shop.hats.conditions.confirm.bystander.kills", new String[]{String.valueOf(e.getValue())});
                break;
                case CONDITIONS_MURDER_KILLS:
                    conditions += "\n" + Main.getMessage(player, "item.murder.shop.hats.conditions.confirm.murder.kills", new String[]{String.valueOf(e.getValue())});
                break;
            }
        }
        return Main.getMessage(player, "item.murder.shop.hats.confirm", new String[]{hatString, coinString, conditions});
    }

    public static String getCostumeName(Player player, String str){
        String hatString = str.replace("_", " ");
        String coinString = String.valueOf(Conditions.get(str).get(CONDITIONS_COIN));
        String conditions = "";
        for (Map.Entry<Integer, Integer> e : Conditions.get(str).entrySet()){
            switch(e.getKey()){
                case CONDITIONS_BYSTANDER_WIN:
                    conditions += "\n" + Main.getMessage(player, "item.murder.costume.hats.conditions.bystander.win", new String[]{String.valueOf(e.getValue())});
                break;
                case CONDITIONS_MURDER_WIN:
                    conditions += "\n" + Main.getMessage(player, "item.murder.costume.hats.conditions.murder.win", new String[]{String.valueOf(e.getValue())});
                break;
                case CONDITIONS_WEAPONS_TRADED:
                    conditions += "\n" + Main.getMessage(player, "item.murder.costume.hats.conditions.weapons.traded", new String[]{String.valueOf(e.getValue())});
                break;
                case CONDITIONS_HATS_OWNED:
                    conditions += "\n" + Main.getMessage(player, "item.murder.costume.hats.conditions.hats.owned", new String[]{String.valueOf(e.getValue())});
                break;
                case CONDITIONS_EMERALD_COLLECTED:
                    conditions += "\n" + Main.getMessage(player, "item.murder.costume.hats.conditions.emerald.collected", new String[]{String.valueOf(e.getValue())});
                break;
                case CONDITIONS_BYSTANDER_KILLS:
                    conditions += "\n" + Main.getMessage(player, "item.murder.costume.hats.conditions.bystander.kills", new String[]{String.valueOf(e.getValue())});
                break;
                case CONDITIONS_MURDER_KILLS:
                    conditions += "\n" + Main.getMessage(player, "item.murder.costume.hats.conditions.murder.kills", new String[]{String.valueOf(e.getValue())});
                break;
            }
        }
        return Main.getMessage(player, "item.murder.costume.hats", new String[]{hatString, coinString, conditions});
    }

    public static int getHaveHatCount(Player player){
        int count = 0;
        Map<String, Boolean> map = (Map<String, Boolean>) hatData.get(player.getName().toLowerCase());
        for (Map.Entry<String,Boolean> e : map.entrySet()){
            if(e.getValue()) count++;
        }
        return count;
    }

    public static boolean canReveal(Player player, String str){
        if(getHaveHatCount(player) >= needHats.get(str)) return true;
        else return false;
    }

    public static boolean canBuy(Player player, String str){
        boolean canBuy = true;
        int value = 0;
        for (Map.Entry<Integer, Integer> e : Conditions.get(str).entrySet()){
            switch(e.getKey()){
                case CONDITIONS_COIN:
                    value = Stat.getCoin(player);
                    if(value < e.getValue()){
                        canBuy = false;
                        System.out.println(e.getValue());
                    }
                break;
                case CONDITIONS_BYSTANDER_WIN:
                    value = Stat.getMurderBystanderWin(player);
                    if(value < e.getValue()){
                        canBuy = false;
                        System.out.println(e.getValue());
                    }
                break;
                case CONDITIONS_MURDER_WIN:
                    value = Stat.getMurderMurderWin(player);
                    if(value < e.getValue()){
                        canBuy = false;
                        System.out.println(e.getValue());
                    }
                break;
                case CONDITIONS_WEAPONS_TRADED:
                    value = Stat.getMurderWeaponTraded(player);
                    if(value < e.getValue()){
                        canBuy = false;
                        System.out.println(e.getValue());
                    }
                break;
                case CONDITIONS_HATS_OWNED:
                    value = getHaveHatCount(player);
                    if(value < e.getValue()){
                        canBuy = false;
                        System.out.println(e.getValue());
                    }
                break;
                case CONDITIONS_EMERALD_COLLECTED:
                    value = Stat.getMurderEmeraldCollected(player);
                    if(value < e.getValue()){
                        canBuy = false;
                        System.out.println(e.getValue());
                    }
                break;
                case CONDITIONS_BYSTANDER_KILLS:
                    value = Stat.getMurderBystanderKills(player);
                    if(value < e.getValue()){
                        canBuy = false;
                        System.out.println(e.getValue());
                    }
                break;
                case CONDITIONS_MURDER_KILLS:
                    value = Stat.getMurderMurderKills(player);
                    if(value < e.getValue()){
                        canBuy = false;
                        System.out.println(e.getValue());
                    }
                break;
            }
        }
        return canBuy;
    }

    public static Item getItem(String str){
        if(!item.containsKey(str) || item.get(str).getId() == 95) return Item.get(0);
        return item.get(str);
    }

    public static String getHats(Player player){
        Map<String, Boolean> map = hatData.get(player.getName().toLowerCase());
        return new GsonBuilder().setPrettyPrinting().create().toJson(map);
    }

    public static void setHats(Player player, String str){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        HashMap<String, Boolean> map = gson.fromJson(str, new TypeToken<HashMap<String, Boolean>>(){}.getType());
        for (Map.Entry<String, Item> e : item.entrySet()){
            if(!map.containsKey(e.getKey())) map.put(e.getKey(), false);
        }
        map.put("unknown", true);
        hatData.put(player.getName().toLowerCase(), map);
    }

    public static void register(){
        String name = "";
        LinkedHashMap<Integer, Integer> conditions;
        //////////////////////////////////////////////////////////
        name = "unknown";
        item.put(name, Item.get(95, 0));
        needHats.put(name, 0);
        conditions = new LinkedHashMap<Integer, Integer>();
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Cobblestone_Hat";
        item.put(name, Item.get(Item.COBBLESTONE, 0));
        needHats.put(name, 0);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_COIN, 1000);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Dirt_Hat";
        item.put(name, Item.get(Item.DIRT, 0));
        needHats.put(name, 0);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_COIN, 1000);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Gravel_Hat";
        item.put(name, Item.get(Item.GRAVEL, 0));
        needHats.put(name, 0);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_BYSTANDER_WIN, 1);
        conditions.put(CONDITIONS_COIN, 1000);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Sand_Hat";
        item.put(name, Item.get(Item.SAND, 0));
        needHats.put(name, 0);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_BYSTANDER_WIN, 2);
        conditions.put(CONDITIONS_COIN, 1000);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Stone_Hat";
        item.put(name, Item.get(Item.STONE, 0));
        needHats.put(name, 0);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_BYSTANDER_WIN, 3);
        conditions.put(CONDITIONS_COIN, 1000);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Sandstone_Hat";
        item.put(name, Item.get(Item.SANDSTONE, 0));
        needHats.put(name, 1);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_WEAPONS_TRADED, 3);
        conditions.put(CONDITIONS_HATS_OWNED, 2);
        conditions.put(CONDITIONS_COIN, 1250);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Oak_Plank_Hat";
        item.put(name, Item.get(Item.PLANK, 0));
        needHats.put(name, 1);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_WEAPONS_TRADED, 5);
        conditions.put(CONDITIONS_HATS_OWNED, 2);
        conditions.put(CONDITIONS_COIN, 1500);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Oak_Wood_Hat";
        item.put(name, Item.get(Item.WOOD, 0));
        needHats.put(name, 1);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_WEAPONS_TRADED, 7);
        conditions.put(CONDITIONS_HATS_OWNED, 2);
        conditions.put(CONDITIONS_COIN, 1750);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Clay_Hat";
        item.put(name, Item.get(Item.CLAY_BLOCK, 0));
        needHats.put(name, 2);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_EMERALD_COLLECTED, 40);
        conditions.put(CONDITIONS_HATS_OWNED, 3);
        conditions.put(CONDITIONS_COIN, 2000);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Granite_Hat";
        item.put(name, Item.get(Item.STONE, 1));
        needHats.put(name, 2);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_EMERALD_COLLECTED, 50);
        conditions.put(CONDITIONS_HATS_OWNED, 3);
        conditions.put(CONDITIONS_COIN, 2250);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Plished_Granite_Hat";
        item.put(name, Item.get(Item.STONE, 2));
        needHats.put(name, 2);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_BYSTANDER_WIN, 10);
        conditions.put(CONDITIONS_HATS_OWNED, 3);
        conditions.put(CONDITIONS_COIN, 2500);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Spruce_Plank_Hat";
        item.put(name, Item.get(Item.PLANK, 1));
        needHats.put(name, 3);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_MURDER_WIN, 10);
        conditions.put(CONDITIONS_HATS_OWNED, 4);
        conditions.put(CONDITIONS_COIN, 2750);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Spruce_Wood_Hat";
        item.put(name, Item.get(Item.WOOD, 1));
        needHats.put(name, 3);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_MURDER_WIN, 15);
        conditions.put(CONDITIONS_HATS_OWNED, 4);
        conditions.put(CONDITIONS_COIN, 3000);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "White_Wool_Hat";
        item.put(name, Item.get(Item.WOOL, 0));
        needHats.put(name, 4);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_MURDER_KILLS, 20);
        conditions.put(CONDITIONS_HATS_OWNED, 5);
        conditions.put(CONDITIONS_COIN, 3250);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Light_Gray_Wool_Hat";
        item.put(name, Item.get(Item.WOOL, 8));
        needHats.put(name, 4);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_MURDER_KILLS, 25);
        conditions.put(CONDITIONS_HATS_OWNED, 5);
        conditions.put(CONDITIONS_COIN, 3500);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Gray_Wool_Hat";
        item.put(name, Item.get(Item.WOOL, 7));
        needHats.put(name, 4);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_MURDER_KILLS, 30);
        conditions.put(CONDITIONS_HATS_OWNED, 5);
        conditions.put(CONDITIONS_COIN, 3750);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Black_Wool_Hat";
        item.put(name, Item.get(Item.WOOL, 15));
        needHats.put(name, 5);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_BYSTANDER_KILLS, 1);
        conditions.put(CONDITIONS_HATS_OWNED, 6);
        conditions.put(CONDITIONS_COIN, 4000);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Brown_Wool_Hat";
        item.put(name, Item.get(Item.WOOL, 12));
        needHats.put(name, 5);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_BYSTANDER_KILLS, 2);
        conditions.put(CONDITIONS_HATS_OWNED, 6);
        conditions.put(CONDITIONS_COIN, 4250);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Red_Wool_Hat";
        item.put(name, Item.get(Item.WOOL, 14));
        needHats.put(name, 6);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_BYSTANDER_KILLS, 3);
        conditions.put(CONDITIONS_HATS_OWNED, 7);
        conditions.put(CONDITIONS_COIN, 4500);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Orange_Wool_Hat";
        item.put(name, Item.get(Item.WOOL, 1));
        needHats.put(name, 6);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_BYSTANDER_KILLS, 3);
        conditions.put(CONDITIONS_HATS_OWNED, 7);
        conditions.put(CONDITIONS_COIN, 4750);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Yellow_Wool_Hat";
        item.put(name, Item.get(Item.WOOL, 4));
        needHats.put(name, 6);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_BYSTANDER_KILLS, 3);
        conditions.put(CONDITIONS_HATS_OWNED, 7);
        conditions.put(CONDITIONS_COIN, 5000);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Lime_Wool_Hat";
        item.put(name, Item.get(Item.WOOL, 5));
        needHats.put(name, 7);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_BYSTANDER_KILLS, 5);
        conditions.put(CONDITIONS_HATS_OWNED, 8);
        conditions.put(CONDITIONS_COIN, 5250);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Green_Wool_Hat";
        item.put(name, Item.get(Item.WOOL, 13));
        needHats.put(name, 7);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_BYSTANDER_KILLS, 5);
        conditions.put(CONDITIONS_HATS_OWNED, 8);
        conditions.put(CONDITIONS_COIN, 5500);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Cyan_Wool_Hat";
        item.put(name, Item.get(Item.WOOL, 9));
        needHats.put(name, 7);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_BYSTANDER_KILLS, 5);
        conditions.put(CONDITIONS_HATS_OWNED, 8);
        conditions.put(CONDITIONS_COIN, 5750);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Light_Blue_Wool_Hat";
        item.put(name, Item.get(Item.WOOL, 3));
        needHats.put(name, 8);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_BYSTANDER_KILLS, 10);
        conditions.put(CONDITIONS_HATS_OWNED, 9);
        conditions.put(CONDITIONS_COIN, 6000);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Blue_Wool_Hat";
        item.put(name, Item.get(Item.WOOL, 11));
        needHats.put(name, 8);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_BYSTANDER_KILLS, 10);
        conditions.put(CONDITIONS_HATS_OWNED, 9);
        conditions.put(CONDITIONS_COIN, 6250);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Purple_Wool_Hat";
        item.put(name, Item.get(Item.WOOL, 10));
        needHats.put(name, 8);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_BYSTANDER_KILLS, 10);
        conditions.put(CONDITIONS_HATS_OWNED, 9);
        conditions.put(CONDITIONS_COIN, 6500);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Magenta_Wool_Hat";
        item.put(name, Item.get(Item.WOOL, 2));
        needHats.put(name, 9);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_BYSTANDER_KILLS, 15);
        conditions.put(CONDITIONS_HATS_OWNED, 10);
        conditions.put(CONDITIONS_COIN, 6750);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Pink_Wool_Hat";
        item.put(name, Item.get(Item.WOOL, 6));
        needHats.put(name, 9);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_BYSTANDER_KILLS, 15);
        conditions.put(CONDITIONS_HATS_OWNED, 10);
        conditions.put(CONDITIONS_COIN, 7000);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Birch_Plank_Hat";
        item.put(name, Item.get(Item.PLANK, 2));
        needHats.put(name, 10);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_MURDER_KILLS, 35);
        conditions.put(CONDITIONS_HATS_OWNED, 10);
        conditions.put(CONDITIONS_COIN, 7250);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Birch_Wood_Hat";
        item.put(name, Item.get(Item.WOOD, 2));
        needHats.put(name, 10);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_MURDER_KILLS, 40);
        conditions.put(CONDITIONS_HATS_OWNED, 10);
        conditions.put(CONDITIONS_COIN, 7500);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Glowstone_Hat";
        item.put(name, Item.get(Item.GLOWSTONE_BLOCK, 0));
        needHats.put(name, 11);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_BYSTANDER_KILLS, 15);
        conditions.put(CONDITIONS_MURDER_KILLS, 50);
        conditions.put(CONDITIONS_HATS_OWNED, 12);
        conditions.put(CONDITIONS_COIN, 7750);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Redstone_Lamp_Hat";
        item.put(name, Item.get(Item.LIT_REDSTONE_LAMP, 0));
        needHats.put(name, 11);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_WEAPONS_TRADED, 30);
        conditions.put(CONDITIONS_HATS_OWNED, 12);
        conditions.put(CONDITIONS_COIN, 8000);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Hay_Bale_Hat";
        item.put(name, Item.get(Item.HAY_BALE, 0));
        needHats.put(name, 11);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_WEAPONS_TRADED, 35);
        conditions.put(CONDITIONS_HATS_OWNED, 12);
        conditions.put(CONDITIONS_COIN, 8250);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Jungle_Plank_Hat";
        item.put(name, Item.get(Item.PLANK, 3));
        needHats.put(name, 12);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_WEAPONS_TRADED, 40);
        conditions.put(CONDITIONS_HATS_OWNED, 13);
        conditions.put(CONDITIONS_COIN, 8500);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Jungle_Wood_Hat";
        item.put(name, Item.get(Item.WOOD, 3));
        needHats.put(name, 12);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_WEAPONS_TRADED, 45);
        conditions.put(CONDITIONS_HATS_OWNED, 13);
        conditions.put(CONDITIONS_COIN, 8750);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Quartz_Ore_Hat";
        item.put(name, Item.get(Item.QUARTZ_ORE, 0));
        needHats.put(name, 13);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_WEAPONS_TRADED, 50);
        conditions.put(CONDITIONS_HATS_OWNED, 14);
        conditions.put(CONDITIONS_COIN, 9000);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Brick_Hat";
        item.put(name, Item.get(Item.BRICKS_BLOCK, 0));
        needHats.put(name, 13);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_WEAPONS_TRADED, 55);
        conditions.put(CONDITIONS_HATS_OWNED, 14);
        conditions.put(CONDITIONS_COIN, 9250);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Nether_Brick_Hat";
        item.put(name, Item.get(Item.NETHER_BRICK_BLOCK, 0));
        needHats.put(name, 13);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_WEAPONS_TRADED, 60);
        conditions.put(CONDITIONS_HATS_OWNED, 14);
        conditions.put(CONDITIONS_COIN, 9500);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Acacia_Plank_Hat";
        item.put(name, Item.get(Item.PLANK, 4));
        needHats.put(name, 14);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_WEAPONS_TRADED, 65);
        conditions.put(CONDITIONS_HATS_OWNED, 15);
        conditions.put(CONDITIONS_COIN, 9750);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Acacia_Wood_Hat";
        item.put(name, Item.get(Item.WOOD, 4));
        needHats.put(name, 14);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_WEAPONS_TRADED, 70);
        conditions.put(CONDITIONS_HATS_OWNED, 15);
        conditions.put(CONDITIONS_COIN, 10000);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Quartz_Block_Hat";
        item.put(name, Item.get(Item.QUARTZ_BLOCK, 0));
        needHats.put(name, 15);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_WEAPONS_TRADED, 75);
        conditions.put(CONDITIONS_EMERALD_COLLECTED, 100);
        conditions.put(CONDITIONS_HATS_OWNED, 16);
        conditions.put(CONDITIONS_COIN, 10250);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Pillar_Quartz_Block_Hat";
        item.put(name, Item.get(Item.QUARTZ_BLOCK, 2));
        needHats.put(name, 15);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_WEAPONS_TRADED, 80);
        conditions.put(CONDITIONS_EMERALD_COLLECTED, 110);
        conditions.put(CONDITIONS_HATS_OWNED, 16);
        conditions.put(CONDITIONS_COIN, 10500);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Chiseled_Quartz_Block_Hat";
        item.put(name, Item.get(Item.QUARTZ_BLOCK, 1));
        needHats.put(name, 15);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_WEAPONS_TRADED, 85);
        conditions.put(CONDITIONS_EMERALD_COLLECTED, 120);
        conditions.put(CONDITIONS_HATS_OWNED, 17);
        conditions.put(CONDITIONS_COIN, 10750);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Dark_Oak_Plank_Hat";
        item.put(name, Item.get(Item.PLANK, 5));
        needHats.put(name, 16);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_WEAPONS_TRADED, 65);
        conditions.put(CONDITIONS_HATS_OWNED, 15);
        conditions.put(CONDITIONS_COIN, 11000);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Dark_Oak_Wood_Hat";
        item.put(name, Item.get(Item.WOOD, 5));
        needHats.put(name, 16);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_WEAPONS_TRADED, 70);
        conditions.put(CONDITIONS_HATS_OWNED, 17);
        conditions.put(CONDITIONS_COIN, 11250);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Diorite_Hat";
        item.put(name, Item.get(Item.STONE, 3));
        needHats.put(name, 17);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_MURDER_WIN, 30);
        conditions.put(CONDITIONS_BYSTANDER_WIN, 60);
        conditions.put(CONDITIONS_HATS_OWNED, 18);
        conditions.put(CONDITIONS_COIN, 11750);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Polished_Diorite_Hat";
        item.put(name, Item.get(Item.STONE, 4));
        needHats.put(name, 17);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_MURDER_WIN, 35);
        conditions.put(CONDITIONS_BYSTANDER_WIN, 65);
        conditions.put(CONDITIONS_HATS_OWNED, 18);
        conditions.put(CONDITIONS_COIN, 12000);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Andesite_Hat";
        item.put(name, Item.get(Item.STONE, 5));
        needHats.put(name, 17);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_MURDER_WIN, 40);
        conditions.put(CONDITIONS_BYSTANDER_WIN, 70);
        conditions.put(CONDITIONS_HATS_OWNED, 18);
        conditions.put(CONDITIONS_COIN, 12250);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Polished_Diorite_Hat";
        item.put(name, Item.get(Item.STONE, 4));
        needHats.put(name, 17);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_MURDER_WIN, 45);
        conditions.put(CONDITIONS_BYSTANDER_WIN, 75);
        conditions.put(CONDITIONS_HATS_OWNED, 18);
        conditions.put(CONDITIONS_COIN, 12500);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Podzol_Hat";
        item.put(name, Item.get(Item.PODZOL, 0));
        needHats.put(name, 18);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_MURDER_KILLS, 60);
        conditions.put(CONDITIONS_BYSTANDER_KILLS, 40);
        conditions.put(CONDITIONS_HATS_OWNED, 19);
        conditions.put(CONDITIONS_COIN, 12750);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Ice_Hat";
        item.put(name, Item.get(Item.ICE, 0));
        needHats.put(name, 18);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_MURDER_KILLS, 65);
        conditions.put(CONDITIONS_BYSTANDER_KILLS, 45);
        conditions.put(CONDITIONS_HATS_OWNED, 19);
        conditions.put(CONDITIONS_COIN, 13000);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Packed_Ice_Hat";
        item.put(name, Item.get(Item.PACKED_ICE, 0));
        needHats.put(name, 18);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_MURDER_KILLS, 70);
        conditions.put(CONDITIONS_BYSTANDER_KILLS, 50);
        conditions.put(CONDITIONS_HATS_OWNED, 19);
        conditions.put(CONDITIONS_COIN, 13250);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Prismarine_Hat";
        item.put(name, Item.get(Item.PRISMARINE, 0));
        needHats.put(name, 19);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_MURDER_WIN, 55);
        conditions.put(CONDITIONS_BYSTANDER_WIN, 85);
        conditions.put(CONDITIONS_HATS_OWNED, 20);
        conditions.put(CONDITIONS_COIN, 13500);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Prismarine_Brick_Hat";
        item.put(name, Item.get(Item.PRISMARINE, 1));
        needHats.put(name, 19);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_MURDER_WIN, 60);
        conditions.put(CONDITIONS_BYSTANDER_WIN, 90);
        conditions.put(CONDITIONS_HATS_OWNED, 20);
        conditions.put(CONDITIONS_COIN, 13750);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Sea_Lantern_Hat";
        item.put(name, Item.get(Item.SEA_LANTERN, 0));
        needHats.put(name, 19);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_MURDER_WIN, 65);
        conditions.put(CONDITIONS_BYSTANDER_WIN, 95);
        conditions.put(CONDITIONS_HATS_OWNED, 20);
        conditions.put(CONDITIONS_COIN, 14000);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Stonebrick_Hat";
        item.put(name, Item.get(Item.STONE_BRICK, 0));
        needHats.put(name, 20);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_MURDER_WIN, 70);
        conditions.put(CONDITIONS_BYSTANDER_WIN, 100);
        conditions.put(CONDITIONS_HATS_OWNED, 21);
        conditions.put(CONDITIONS_COIN, 14250);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Stonebrick_Cracked_Hat";
        item.put(name, Item.get(Item.STONE_BRICK, 1));
        needHats.put(name, 20);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_MURDER_WIN, 75);
        conditions.put(CONDITIONS_BYSTANDER_WIN, 105);
        conditions.put(CONDITIONS_HATS_OWNED, 21);
        conditions.put(CONDITIONS_COIN, 14500);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Stonebrick_Mossy_Hat";
        item.put(name, Item.get(Item.STONE_BRICK, 2));
        needHats.put(name, 20);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_MURDER_WIN, 80);
        conditions.put(CONDITIONS_BYSTANDER_WIN, 110);
        conditions.put(CONDITIONS_HATS_OWNED, 21);
        conditions.put(CONDITIONS_COIN, 14750);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Melon_Hat";
        item.put(name, Item.get(Item.MELON_BLOCK, 0));
        needHats.put(name, 21);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_MURDER_KILLS, 80);
        conditions.put(CONDITIONS_BYSTANDER_KILLS, 60);
        conditions.put(CONDITIONS_HATS_OWNED, 22);
        conditions.put(CONDITIONS_COIN, 15000);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Pumpkin_Hat";
        item.put(name, Item.get(Item.PUMPKIN, 0));
        needHats.put(name, 21);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_MURDER_KILLS, 85);
        conditions.put(CONDITIONS_BYSTANDER_KILLS, 65);
        conditions.put(CONDITIONS_HATS_OWNED, 22);
        conditions.put(CONDITIONS_COIN, 15250);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Jack_oLantern_Hat";
        item.put(name, Item.get(Item.JACK_O_LANTERN, 0));
        needHats.put(name, 21);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_MURDER_KILLS, 90);
        conditions.put(CONDITIONS_BYSTANDER_KILLS, 70);
        conditions.put(CONDITIONS_HATS_OWNED, 22);
        conditions.put(CONDITIONS_COIN, 15250);
        Conditions.put(name, conditions);
        
        
        
        
        
        
        
        
        
        /*//////////////////////////////////////////////////////////
        name = "Coal_Ore_Hat";
        item.put(name, Item.get(Item.COAL_ORE, 0));
        needHats.put(name, 10);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_BYSTANDER_KILLS, 15);
        conditions.put(CONDITIONS_MURDER_KILLS, 50);
        conditions.put(CONDITIONS_HATS_OWNED, 11);
        conditions.put(CONDITIONS_COIN, 7250);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Iron_Ore_Hat";
        item.put(name, Item.get(Item.IRON_ORE, 0));
        needHats.put(name, 10);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_WEAPONS_TRADED, 30);
        conditions.put(CONDITIONS_HATS_OWNED, 11);
        conditions.put(CONDITIONS_COIN, 7500);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Gold_Ore_Hat";
        item.put(name, Item.get(Item.GOLD_ORE, 0));
        needHats.put(name, 10);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_WEAPONS_TRADED, 35);
        conditions.put(CONDITIONS_HATS_OWNED, 11);
        conditions.put(CONDITIONS_COIN, 7750);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Emerald_Ore_Hat";
        item.put(name, Item.get(Item.EMERALD_ORE, 0));
        needHats.put(name, 11);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_WEAPONS_TRADED, 40);
        conditions.put(CONDITIONS_HATS_OWNED, 12);
        conditions.put(CONDITIONS_COIN, 8000);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Diamond_Ore_Hat";
        item.put(name, Item.get(Item.DIAMOND_ORE, 0));
        needHats.put(name, 11);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_WEAPONS_TRADED, 45);
        conditions.put(CONDITIONS_HATS_OWNED, 12);
        conditions.put(CONDITIONS_COIN, 8250);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Coal_Block_Hat";
        item.put(name, Item.get(Item.COAL_BLOCK, 0));
        needHats.put(name, 12);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_EMERALD_COLLECTED, 100);
        conditions.put(CONDITIONS_HATS_OWNED, 13);
        conditions.put(CONDITIONS_COIN, 7250);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Iron_Block_Hat";
        item.put(name, Item.get(Item.IRON_BLOCK, 0));
        needHats.put(name, 12);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_EMERALD_COLLECTED, 110);
        conditions.put(CONDITIONS_HATS_OWNED, 13);
        conditions.put(CONDITIONS_COIN, 7500);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Gold_Block_Hat";
        item.put(name, Item.get(Item.GOLD_BLOCK, 0));
        needHats.put(name, 12);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_EMERALD_COLLECTED, 120);
        conditions.put(CONDITIONS_HATS_OWNED, 13);
        conditions.put(CONDITIONS_COIN, 7750);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Emerald_Block_Hat";
        item.put(name, Item.get(Item.EMERALD_ORE, 0));
        needHats.put(name, 13);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_EMERALD_COLLECTED, 130);
        conditions.put(CONDITIONS_HATS_OWNED, 14);
        conditions.put(CONDITIONS_COIN, 8000);
        Conditions.put(name, conditions);
        //////////////////////////////////////////////////////////
        name = "Diamond_Block_Hat";
        item.put(name, Item.get(Item.DIAMOND_ORE, 0));
        needHats.put(name, 13);
        conditions = new LinkedHashMap<Integer, Integer>();
        conditions.put(CONDITIONS_EMERALD_COLLECTED, 140);
        conditions.put(CONDITIONS_HATS_OWNED, 14);
        conditions.put(CONDITIONS_COIN, 8250);
        Conditions.put(name, conditions);*/
    }

    public static String getHats(PlayerClone player){
        String result = "";
        Map<String, Boolean> map = (Map<String, Boolean>) hatData.get(player.getName().toLowerCase());
        boolean isFirst = true;
        for (Map.Entry<String,Boolean> e : map.entrySet()){
            if(!isFirst) result += ",";
            else isFirst = false;
            result += e.getKey()+":";
            if(e.getValue()) result += 1;
            else result += 0;
        }
        return result;
    }

    public static HashMap<String, HashMap<String, Boolean>> hatData = new HashMap<String, HashMap<String, Boolean>>();
    public static File file;

}