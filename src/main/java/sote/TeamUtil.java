package sote;

import java.util.HashMap;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemColorArmor;

public class TeamUtil{

    public static HashMap<Integer, HashMap<String, String>> data = new HashMap<>();

    public static String getNameByNumber(int team){
        return data.get(team).get("Name");
    }

    public static String getShortNameByNumber(int team){
        return data.get(team).get("ShortName");
    }

    public static String getColorCodeByNumber(int team){
        return data.get(team).get("ColorCode");
    }

    public static int getWoolColorByNumber(int team){
        return Integer.parseInt(data.get(team).get("WoolColor"));
    }

    public static Item getArmorColor(String code, ItemColorArmor item){
        if(code.equals("§0")) return item.setColor(1,1,1);
        if(code.equals("§1")) return item.setColor(0,1,168);
        if(code.equals("§2")) return item.setColor(0,170,3);
        if(code.equals("§3")) return item.setColor(1,169,170);
        if(code.equals("§4")) return item.setColor(171,1,1);
        if(code.equals("§5")) return item.setColor(170,0,167);
        if(code.equals("§6")) return item.setColor(255,171,4);
        if(code.equals("§7")) return item.setColor(170,170,170);
        if(code.equals("§8")) return item.setColor(85,85,85);
        if(code.equals("§9")) return item.setColor(86,84,255);
        if(code.equals("§a")) return item.setColor(84,255,88);
        if(code.equals("§b")) return item.setColor(84,255,255);
        if(code.equals("§c")) return item.setColor(255,80,82);
        if(code.equals("§d")) return item.setColor(255,85,252);
        if(code.equals("§e")) return item.setColor(255,255,85);
        if(code.equals("§f")) return item.setColor(250,250,250);
        return item;
    }

    public static void regster(){
        HashMap<String, String> d = new HashMap<>();
        d.put("Name", "Red");
        d.put("ShortName", "R");
        d.put("ColorCode", "§c");
        d.put("WoolColor", "14");
        data.put(0, d);
        d = new HashMap<>();
        d.put("Name", "Blue");
        d.put("ShortName", "B");
        d.put("ColorCode", "§1");
        d.put("WoolColor", "11");
        data.put(1, d);
        d = new HashMap<>();
        d.put("Name", "Green");
        d.put("ShortName", "G");
        d.put("ColorCode", "§a");
        d.put("WoolColor", "5");
        data.put(2, d);
        d = new HashMap<>();
        d.put("Name", "Yellow");
        d.put("ShortName", "Y");
        d.put("ColorCode", "§e");
        d.put("WoolColor", "4");
        data.put(3, d);
        d = new HashMap<>();
        d.put("Name", "Aqua");
        d.put("ShortName", "A");
        d.put("ColorCode", "§b");
        d.put("WoolColor", "3");
        data.put(4, d);
        d = new HashMap<>();
        d.put("Name", "White");
        d.put("ShortName", "W");
        d.put("ColorCode", "§f");
        d.put("WoolColor", "0");
        data.put(5, d);
        d = new HashMap<>();
        d.put("Name", "Pink");
        d.put("ShortName", "P");
        d.put("ColorCode", "§d");
        d.put("WoolColor", "6");
        data.put(6, d);
        d = new HashMap<>();
        d.put("Name", "Gray");
        d.put("ShortName", "G");
        d.put("ColorCode", "§8");
        d.put("WoolColor", "7");
        data.put(7, d);
    }

}
