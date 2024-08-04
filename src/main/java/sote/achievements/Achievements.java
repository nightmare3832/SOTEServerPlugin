package sote.achievements;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.internal.LinkedTreeMap;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import sote.PlayerClone;

public class Achievements{

    public static LinkedHashMap<String, Item> item = new LinkedHashMap<String, Item>();
    public static LinkedHashMap<String, Integer> points = new LinkedHashMap<String, Integer>();
    public static LinkedHashMap<String, Boolean> haveLevel = new LinkedHashMap<String, Boolean>();
    public static LinkedHashMap<String, Integer> maxLevel = new LinkedHashMap<String, Integer>();

    public static final String LEVEL_ONE = "Ⅰ";
    public static final String LEVEL_TWO = "Ⅱ";
    public static final String LEVEL_THREE = "Ⅲ";
    public static final String LEVEL_FOUR = "Ⅳ";
    public static final String LEVEL_FIVE = "Ⅴ";
    public static final String LEVEL_SIX = "Ⅵ";

    public static final String[] LEVEL_STRING = new String[]{
            "Ⅰ",
            "Ⅱ",
            "Ⅲ",
            "Ⅳ",
            "Ⅴ",
            "Ⅵ"
    };

    public Achievements(){
        register();
    }

    public static void addPlayer(Player player){
        if(!achievementsData.containsKey(player.getName().toLowerCase())){
            LinkedTreeMap<String, String> v1 = new LinkedTreeMap<String, String>();
            for (Map.Entry<String,Item> e : item.entrySet()){
                v1.put(e.getKey(), "1");
            }
            achievementsData.put(player.getName().toLowerCase(),v1);
        }
    }

    public static void addPlayer(PlayerClone player){
        if(!achievementsData.containsKey(player.getName().toLowerCase())){
            LinkedTreeMap<String, String> v1 = new LinkedTreeMap<String, String>();
            for (Map.Entry<String,Item> e : item.entrySet()){
                v1.put(e.getKey(), "1");
            }
            achievementsData.put(player.getName().toLowerCase(),v1);
        }
    }

    public static String getItemName(Player player, String str){
        String title = "§1"+str.replace("_", " ");
        String de = "";
        int value = 0;
        int level = getLevel(player, str);
        switch(str){
            case "Increase_Level":
                value = (int)(20 * level);
                de = "§7You have increased to "+value+" level!";
            break;
        }
        String levelString = "";
        if(haveLevel.get(str) && level != 1){
            if(maxLevel.get(str) < level) level = maxLevel.get(str);
            levelString = " "+LEVEL_STRING[level - 1];
        }
        return title+levelString+"\n"+de;
    }

    public static int getLevel(Player player, String str){
        Map<String, String> map = (Map<String, String>) achievementsData.get(player.getName().toLowerCase());
        return Integer.parseInt(map.get(str));
    }

    public static void setLevel(Player player, String str, int level){
        Map<String, String> map = (Map<String, String>) achievementsData.get(player.getName().toLowerCase());
        map.put(str, String.valueOf(level));
    }

    public static String getAchievements(Player player){
        String result = "";
        Map<String, String> map = (Map<String, String>) achievementsData.get(player.getName().toLowerCase());
        boolean isFirst = true;
        for (Map.Entry<String,String> e : map.entrySet()){
            if(!isFirst) result += "%";
            else isFirst = false;
            result += e.getKey()+":"+e.getValue();
        }
        return result;
    }

    public static void setAchievements(Player player, String str){
        String[] sp = str.split("%");
        for(String data : sp){
            String[] d = data.split(":");
            setLevel(player, d[0], Integer.parseInt(d[1]));
        }
        for (Map.Entry<String, Item> e : item.entrySet()){
            if(!((Map<String, String>)achievementsData.get(player.getName().toLowerCase())).containsKey(e.getKey())) setLevel(player, e.getKey(), 1);
        }
    }

    public static void register(){
        String name = "";
        name = "Increase_Level";
        item.put(name, Item.get(Item.ENCHANT_BOOK, 0));
        haveLevel.put(name, true);
        maxLevel.put(name, 5);
    }

    public static String getAchievements(PlayerClone player){
        String result = "";
        Map<String, String> map = (Map<String, String>) achievementsData.get(player.getName().toLowerCase());
        boolean isFirst = true;
        for (Map.Entry<String,String> e : map.entrySet()){
            if(!isFirst) result += ",";
            else isFirst = false;
            result += e.getKey()+":"+e.getValue();
        }
        return result;
    }

    public static LinkedHashMap<String, Object> achievementsData = new LinkedHashMap<String, Object>();
    public static File file;

}