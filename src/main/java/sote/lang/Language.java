package sote.lang;

import java.io.File;
import java.util.LinkedHashMap;

import cn.nukkit.Player;
import sote.Main;
import sote.PlayerClone;

public class Language{

    public Language(){
    }

    public static void addPlayer(Player player){
        data.put(player.getName().toLowerCase(),"en_US");
    }

    public static void setLang(Player player,String lang){
        if(!Main.lang.containsKey(lang)) lang = "en_US";
        data.put(player.getName().toLowerCase(),lang);
    }

    public static String getLang(Player player){
        return (String)data.get(player.getName().toLowerCase());
    }

    public static String getLang(PlayerClone player){
        return (String)data.get(player.getName().toLowerCase());
    }

    public static LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
    public static File file;

}