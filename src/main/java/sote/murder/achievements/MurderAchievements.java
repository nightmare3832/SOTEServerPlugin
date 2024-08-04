package sote.murder.achievements;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import cn.nukkit.Player;
import cn.nukkit.item.Item;

public class MurderAchievements{

    public static LinkedHashMap<String, Item> item = new LinkedHashMap<String, Item>();
    public static LinkedHashMap<String, Integer> points = new LinkedHashMap<String, Integer>();
    public static LinkedHashMap<String, Boolean> haveLevel = new LinkedHashMap<String, Boolean>();

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

    public MurderAchievements(){
        register();
    }

    public static void addPlayer(Player player){
        HashMap<String, Integer> v1 = new HashMap<String, Integer>();
        for (Map.Entry<String,Item> e : item.entrySet()){
            v1.put(e.getKey(), 1);
        }
        achievementsData.put(player.getName().toLowerCase(), v1);
    }

    public static String getItemName(Player player, String str){
        String title = "§1"+str.replace("_", " ");
        String de = "";
        int value = 0;
        int level = getLevel(player, str);
        switch(str){
            case "Shoot_The_Murderer":
                value = (int)(50 * level);
                de = "§7You have killed the murderer "+value+" times!";
            break;
            case "Win_As_The_Murderer":
                value = (int)(20 * level);
                de = "§7You have won as the murderer "+value+" times!";
            break;
            case "Picking_Up_The_Pieces":
                value = (int)(400 * level);
                de = "§7You have picked up "+value+" emeralds!";
            break;
            case "Trading_With_The_Villagers":
                value = (int)(40 * level);
                de = "§7You traded emeralds for "+value+" weapons!";
            break;
            case "Saint":
                de = "§7You have reached max karma!";
            break;
            case "Kill_Frenzy":
                de = "§7Secret Achievements!";
            break;
            case "Parkour_Around_Murder":
                de = "§7Complete the second lobby parkour!";
            break;
            case "Being Watched":
                de = "§7Play a game with a member of staff!";
            break;
            case "Slow_But_Effective":
                de = "§7Secret Achievements!";
            break;
            case "Long_Shot":
                de = "§7Kill someone from over 25 blocks away\nwithout losing any karma!";
            break;
            case "Not_So_Personal":
                de = "§7Throw your knife to kill someone!";
            break;
            case "Clean_Hands":
                de = "§7Win as te murderer without stabbing anyone!";
            break;
            case "Personailty_Issues":
                value = (int)(1 * level);
                de = "§7You changed identity "+value+" times in\na single game as the murderer!";
            break;
            case "Best_Gun":
                de = "§7You upgraded your gun twice\nin a single game!";
            break;
            case "Double_Knives":
                de = "§7You got a third knife!";
            break;
            case "Hat_Owner":
                value = (int)(10 * level);
                de = "§7You own "+value+" hats!";
            break;
            case "Kill_Yourself":
                de = "§7Kill someone disguised as you\nwithout losing any karma!";
            break;
            case "Not_So_Secret_Identity":
                de = "§7Be desguised as yourself!";
            break;
            case "Cutting_It_Close":
                de = "§7Secret Achievements!";
            break;
        }
        String levelString = "";
        if(haveLevel.get(str) && level != 1){
            if(level >= 6) level = 5;
            levelString = " "+LEVEL_STRING[level - 1];
        }
        return title+levelString+"\n"+de;
    }

    public static int getLevel(Player player, String str){
        Map<String, Integer> map = achievementsData.get(player.getName().toLowerCase());
        if(!map.containsKey(str)) return 1;
        return map.get(str);
    }

    public static void setLevel(Player player, String str, int level){
        Map<String, Integer> map = achievementsData.get(player.getName().toLowerCase());
        map.put(str, level);
    }

    public static String getAchievements(Player player){
        Map<String, Integer> map = achievementsData.get(player.getName().toLowerCase());
        return new GsonBuilder().setPrettyPrinting().create().toJson(map);
    }

    public static void setAchievements(Player player, String str){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        HashMap<String, Integer> map = gson.fromJson(str, new TypeToken<HashMap<String, Integer>>(){}.getType());
        for (Map.Entry<String, Item> e : item.entrySet()){
            if(!(map.containsKey(e.getKey()))) map.put(e.getKey(), 1);
        }
        achievementsData.put(player.getName().toLowerCase(), map);
    }

    public static void register(){
        String name = "";
        name = "Shoot_The_Murderer";//OK
        item.put(name, Item.get(Item.WOODEN_HOE, 0));
        haveLevel.put(name, true);
        name = "Win_As_The_Murderer";//OK
        item.put(name, Item.get(Item.WOODEN_HOE, 0));
        haveLevel.put(name, true);
        name = "Picking_Up_The_Pieces";//OK
        item.put(name, Item.get(Item.EMERALD, 0));
        haveLevel.put(name, true);
        name = "Trading_With_The_Villagers";//OK
        item.put(name, Item.get(Item.EMERALD_BLOCK, 0));
        haveLevel.put(name, true);
        name = "Saint";//OK
        item.put(name, Item.get(Item.ENCHANT_BOOK, 0));
        haveLevel.put(name, false);
        name = "Kill_Frenzy";//NO
        item.put(name, Item.get(Item.WOODEN_HOE, 0));
        haveLevel.put(name, false);
        name = "Parkour_Around_Murder";//OK
        item.put(name, Item.get(Item.COMPASS, 0));
        haveLevel.put(name, false);
        name = "Being_Watched";//OK
        item.put(name, Item.get(Item.SKULL, 0));
        haveLevel.put(name, false);
        name = "Slow_But_Effective";//NO
        item.put(name, Item.get(Item.BED, 0));
        haveLevel.put(name, false);
        name = "Long_Shot";//OK
        item.put(name, Item.get(Item.ARROW, 0));
        haveLevel.put(name, false);
        name = "Not_So_Personal";//OK
        item.put(name, Item.get(Item.WOODEN_SWORD, 0));
        haveLevel.put(name, false);
        name = "Clean_Hands";//OK
        item.put(name, Item.get(Item.WOODEN_SWORD, 0));
        haveLevel.put(name, false);
        name = "Personailty_Isues";//NO
        item.put(name, Item.get(Item.ANVIL, 0));
        haveLevel.put(name, true);
        name = "Best_Gun";//NO
        item.put(name, Item.get(Item.WOODEN_PICKAXE, 0));
        haveLevel.put(name, false);
        name = "Double_Knives";//OK
        item.put(name, Item.get(Item.WOODEN_SWORD, 0));
        haveLevel.put(name, false);
        name = "Hat_Owner";//OK
        item.put(name, Item.get(Item.TNT, 0));
        haveLevel.put(name, true);
        name = "Kill_Yourself";//OK
        item.put(name, Item.get(Item.WOODEN_HOE, 0));
        haveLevel.put(name, false);
        name = "Not_So_Secret_Identity";//OK
        item.put(name, Item.get(Item.WOODEN_HOE, 0));
        haveLevel.put(name, false);
        name = "Cutting_It_Close";//NO
        item.put(name, Item.get(Item.WOODEN_HOE, 0));
        haveLevel.put(name, false);
    }

    public static HashMap<String, HashMap<String, Integer>> achievementsData = new HashMap<String, HashMap<String, Integer>>();
    public static File file;

}