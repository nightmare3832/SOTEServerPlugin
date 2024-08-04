package sote.murder.armor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import sote.PlayerClone;
import sote.stat.Stat;

public class Armors{

    public static final int PRICE_LETHER = 5000;
    public static final int PRICE_CHAIN = 8000;
    public static final int PRICE_IRON = 10000;
    public static final int PRICE_GOLD = 20000;
    public static final int PRICE_DIAMOND = 30000;

    public Armors(){
        /*armorData = (LinkedHashMap<String, Object>) (new Config(file + "/armor.json", Config.JSON)).getAll();
        sellectHelmet = (LinkedHashMap<String, Object>) (new Config(file + "/sellectHelmet.json", Config.JSON)).getAll();
        sellectLeggings = (LinkedHashMap<String, Object>) (new Config(file + "/sellectLeggings.json", Config.JSON)).getAll();
        sellectBoots = (LinkedHashMap<String, Object>) (new Config(file + "/sellectBoots.json", Config.JSON)).getAll();
        for (Map.Entry<String,Object> e : armorData.entrySet()){
            Map<String, Boolean> map = (Map<String, Boolean>) armorData.get(e.getKey());
            if(!map.containsKey("298:0")) map.put("298:0",false);
            if(!map.containsKey("300:0")) map.put("300:0",false);
            if(!map.containsKey("301:0")) map.put("301:0",false);
            if(!map.containsKey("302:0")) map.put("302:0",false);
            if(!map.containsKey("304:0")) map.put("304:0",false);
            if(!map.containsKey("305:0")) map.put("305:0",false);
            if(!map.containsKey("306:0")) map.put("306:0",false);
            if(!map.containsKey("308:0")) map.put("308:0",false);
            if(!map.containsKey("309:0")) map.put("309:0",false);
            if(!map.containsKey("314:0")) map.put("314:0",false);
            if(!map.containsKey("316:0")) map.put("316:0",false);
            if(!map.containsKey("317:0")) map.put("317:0",false);
            if(!map.containsKey("310:0")) map.put("310:0",false);
            if(!map.containsKey("312:0")) map.put("312:0",false);
            if(!map.containsKey("313:0")) map.put("313:0",false);
        }*/
    }

    public static void buyItem(Player player,String str){
        int coin = 0;
            switch(str){
                case "298:0":
                    coin = PRICE_LETHER;
                break;
                case "300:0":
                    coin = PRICE_LETHER;
                break;
                case "301:0":
                    coin = PRICE_LETHER;
                break;
                case "302:0":
                    coin = PRICE_CHAIN;
                break;
                case "304:0":
                    coin = PRICE_CHAIN;
                break;
                case "305:0":
                    coin = PRICE_CHAIN;
                break;
                case "306:0":
                    coin = PRICE_IRON;
                break;
                case "308:0":
                    coin = PRICE_IRON;
                break;
                case "309:0":
                    coin = PRICE_IRON;
                break;
                case "314:0":
                    coin = PRICE_GOLD;
                break;
                case "316:0":
                    coin = PRICE_GOLD;
                break;
                case "317:0":
                    coin = PRICE_GOLD;
                break;
                case "310:0":
                    coin = PRICE_DIAMOND;
                break;
                case "312:0":
                    coin = PRICE_DIAMOND;
                break;
                case "313:0":
                    coin = PRICE_DIAMOND;
                break;
            }
        Map<String, Boolean> map = armorData.get(player.getName().toLowerCase());
        if(map.get(str) == true) return;
            if(Stat.getCoin(player) >= coin){
                armorData.get(player.getName().toLowerCase()).put(str,true);
                Stat.setCoin(player,Stat.getCoin(player)-coin);
                player.sendMessage("[§aSHOP§f] §aアイテムの購入に成功しました");
            }else{
                player.sendMessage("[§aSHOP§f] §cCoinが足りません");
            }
    }

    public static void addPlayer(Player player){
        if(!armorData.containsKey(player.getName().toLowerCase())){
            HashMap<String, Boolean> v1 = new HashMap<String, Boolean>();
            setSellectHelmet(player,"0:0");
            setSellectLeggings(player,"0:0");
            setSellectBoots(player,"0:0");
            v1.put("298:0",false);
            v1.put("300:0",false);
            v1.put("301:0",false);
            v1.put("302:0",false);
            v1.put("304:0",false);
            v1.put("305:0",false);
            v1.put("306:0",false);
            v1.put("308:0",false);
            v1.put("309:0",false);
            v1.put("314:0",false);
            v1.put("316:0",false);
            v1.put("317:0",false);
            v1.put("310:0",false);
            v1.put("312:0",false);
            v1.put("313:0",false);
            armorData.put(player.getName().toLowerCase(),v1);
        }
    }

    public static String getSellectHelmetString(Player player){
         return sellectHelmet.get(player.getName().toLowerCase()).toString();
    }

    public static Item getSellectHelmet(Player player){
         return getItemString(sellectHelmet.get(player.getName().toLowerCase()).toString());
    }

    public static void setSellectHelmet(Player player,String str){
        sellectHelmet.put(player.getName().toLowerCase(),str);
    }

    public static String getSellectLeggingsString(Player player){
        return sellectLeggings.get(player.getName().toLowerCase()).toString();
    }

    public static Item getSellectLeggings(Player player){
        return getItemString(sellectLeggings.get(player.getName().toLowerCase()).toString());
    }

    public static void setSellectLeggings(Player player,String str){
        sellectLeggings.put(player.getName().toLowerCase(),str);
    }

    public static String getSellectBootsString(Player player){
        return sellectBoots.get(player.getName().toLowerCase()).toString();
    }

    public static Item getSellectBoots(Player player){
        return getItemString(sellectBoots.get(player.getName().toLowerCase()).toString());
    }

    public static void setSellectBoots(Player player,String str){
        sellectBoots.put(player.getName().toLowerCase(),str);
    }

    public static String getArmors(Player player){
        Map<String, Boolean> map = armorData.get(player.getName().toLowerCase());
        return new GsonBuilder().setPrettyPrinting().create().toJson(map);
    }

    public static void setArmors(Player player, String str){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        HashMap<String, Boolean> map = gson.fromJson(str, new TypeToken<HashMap<String, Boolean>>(){}.getType());
        if(!map.containsKey("298:0")) map.put("298:0",false);
        if(!map.containsKey("300:0")) map.put("300:0",false);
        if(!map.containsKey("301:0")) map.put("301:0",false);
        if(!map.containsKey("302:0")) map.put("302:0",false);
        if(!map.containsKey("304:0")) map.put("304:0",false);
        if(!map.containsKey("305:0")) map.put("305:0",false);
        if(!map.containsKey("306:0")) map.put("306:0",false);
        if(!map.containsKey("308:0")) map.put("308:0",false);
        if(!map.containsKey("309:0")) map.put("309:0",false);
        if(!map.containsKey("314:0")) map.put("314:0",false);
        if(!map.containsKey("316:0")) map.put("316:0",false);
        if(!map.containsKey("317:0")) map.put("317:0",false);
        if(!map.containsKey("310:0")) map.put("310:0",false);
        if(!map.containsKey("312:0")) map.put("312:0",false);
        if(!map.containsKey("313:0")) map.put("313:0",false);
        armorData.put(player.getName().toLowerCase(), map);
    }

    public static Item getItemString(String str){
        Item item;
        String[] strs = str.split(":");
        item = Item.get(Integer.parseInt(strs[0]),Integer.parseInt(strs[1]),1);
        return item;
    }

    public static String getSellectHelmetString(PlayerClone player){
         return sellectHelmet.get(player.getName().toLowerCase()).toString();
    }

    public static String getSellectLeggingsString(PlayerClone player){
        return sellectLeggings.get(player.getName().toLowerCase()).toString();
    }

    public static String getSellectBootsString(PlayerClone player){
        return sellectBoots.get(player.getName().toLowerCase()).toString();
    }

    public static HashMap<String, HashMap<String, Boolean>> armorData = new HashMap<String, HashMap<String, Boolean>>();
    public static HashMap<String, String> sellectHelmet = new HashMap<String, String>();
    public static HashMap<String, String> sellectLeggings = new HashMap<String, String>();
    public static HashMap<String, String> sellectBoots = new HashMap<String, String>();
    public static HashMap<Integer,Integer> taskid = new HashMap<Integer,Integer>();
    public static File file;

}