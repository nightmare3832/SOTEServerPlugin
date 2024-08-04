package sote.murder.upgrade;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import cn.nukkit.Player;
import sote.Main;
import sote.stat.Stat;

public class MurderUpgrades{

    public static final String[] LEVEL_STRING = new String[]{
            "Ⅰ",
            "Ⅱ",
            "Ⅲ",
            "Ⅳ",
            "Ⅴ",
            "Ⅵ"
    };

    public MurderUpgrades(){
        register();
    }

    public static void buyItem(Player player,String str){
        int coin = 0;
        if(getLevel(player,str) >= 6) return;
        coin = cost.get(str)[(getLevel(player, str) - 1)];
            if(Stat.getCoin(player) >= coin){
                setLevel(player, str, getLevel(player, str) + 1);
                Stat.setCoin(player,Stat.getCoin(player) - coin);
                player.sendMessage(Main.getMessage(player,"shop.buy.item"));
            }else{
                player.sendMessage(Main.getMessage(player,"shop.no.coin"));
            }
    }

    public static String getShopName(Player player, String str){
        int level = getLevel(player, str);
        String title = str;
        String description = "";
        String coinTag = "";
        if(level == 6) return ":)";
        int chance = 0;
        switch(level + 1){
            case 1:
                chance = 0;
                break;
            case 2:
                chance = 2;
                break;
            case 3:
                chance = 3;
                break;
            case 4:
                chance = 4;
                break;
        case 5:
            chance = 5;
            break;
        case 6:
            chance = 6;
            break;
        }
        title = str+" "+LEVEL_STRING[(level - 1)];
        int price = cost.get(str)[level - 1];
        switch(str){
            case "Emerald Upgrade":
                description = Main.getMessage(player, "item.murder.shop.upgrades.emerald.description", new String[]{String.valueOf(chance)});
                break;
            case "Gun Upgrade":
                description = Main.getMessage(player, "item.murder.shop.upgrades.gun.description", new String[]{String.valueOf(chance)});
                break;
            case "Knife Upgrade":
                description = Main.getMessage(player, "item.murder.shop.upgrades.knife.description", new String[]{String.valueOf(chance)});
                break;
        }
        if(Stat.getCoin(player) >= price){
            coinTag = Main.getMessage(player, "item.murder.shop.upgrades.coin.yes");
        }else{
            coinTag = Main.getMessage(player, "item.murder.shop.upgrades.coin.no", new String[]{String.valueOf(price - Stat.getCoin(player))});
        }
        return Main.getMessage(player, "item.murder.shop.upgrades", new String[]{title, description, String.valueOf(price), coinTag});
    }

    public static void addPlayer(Player player){
        HashMap<String, Integer> v1 = new HashMap<String, Integer>();
        for (Map.Entry<String,Integer[]> e : cost.entrySet()){
            v1.put(e.getKey(), 1);
        }
        upgradeData.put(player.getName().toLowerCase(),v1);
    }

    public static boolean getResult(Player player, String str){
        int level = getLevel(player, str);
        int chance = 0;
        switch(level){
            case 1:
                chance = 0;
                break;
            case 2:
                chance = 2;
                break;
            case 3:
                chance = 3;
                break;
            case 4:
                chance = 4;
                break;
            case 5:
                chance = 5;
                break;
            case 6:
                chance = 6;
                break;
        }
        int r = (int)(Math.random() * 99 + 1);
        if(r <= chance) return true;
        return false;
    }

    public static int getLevel(Player player, String str){
        return upgradeData.get(player.getName().toLowerCase()).get(str);
    }

    public static void setLevel(Player player, String str, int level){
        upgradeData.get(player.getName().toLowerCase()).put(str, level);
    }

    public static String getUpgrades(Player player){
        Map<String, Integer> map = upgradeData.get(player.getName().toLowerCase());
        return new GsonBuilder().setPrettyPrinting().create().toJson(map);
    }

    public static void setUpgrades(Player player, String str){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        HashMap<String, Integer> map = gson.fromJson(str, new TypeToken<HashMap<String, Integer>>(){}.getType());
        for (Map.Entry<String, Integer[]> e : cost.entrySet()){
            if(!(map.containsKey(e.getKey()))) map.put(e.getKey(), 1);
        }
        upgradeData.put(player.getName().toLowerCase(), map);
    }

    public static void register(){
        String name = "";
        name = "Emerald Upgrade";
        cost.put(name, new Integer[]{8000,16000,28000,44000,64000});
        name = "Gun Upgrade";
        cost.put(name, new Integer[]{9000,180000,31000,48000,69000});
        name = "Knife Upgrade";
        cost.put(name, new Integer[]{10000,20000,34000,52000,74000});
    }

    public static HashMap<String, HashMap<String, Integer>> upgradeData = new HashMap<String, HashMap<String, Integer>>();
    public static LinkedHashMap<String, Integer[]> cost = new LinkedHashMap<String, Integer[]>();

}