package sote.setting;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import cn.nukkit.Player;
import sote.Main;

public class Setting{

    public Setting(){
    }

    public static void addPlayer(Player player){
        HashMap<String, Boolean> v1 = new HashMap<String, Boolean>();
        v1.put("hideplayer", false);
        v1.put("hidechat", false);
        setting.put(player.getName().toLowerCase(), v1);
    }

    public static boolean getPlayerHide(Player player){
        return setting.get(player.getName().toLowerCase()).get("hideplayer");
    }

    public static void setPlayerHide(Player player, boolean value){
        setting.get(player.getName().toLowerCase()).put("hideplayer", value);
        Map<Long,Player> players = player.getLevel().getPlayers();
        if(!value){
            for (Map.Entry<Long,Player> e : players.entrySet()){
                player.showPlayer(e.getValue());
            }
            Main.setHide.put(player,false);
        }else{
            for (Map.Entry<Long,Player> e : players.entrySet()){
                player.hidePlayer(e.getValue());
            }
            Main.setHide.put(player,true);
        }
    }

    public static boolean getChatHide(Player player){
        if(!setting.containsKey(player.getName().toLowerCase())) return false;
        return setting.get(player.getName().toLowerCase()).get("hidechat");
    }

    public static void setChatHide(Player player, boolean value){
        setting.get(player.getName().toLowerCase()).put("hidechat", value);
    }

    public static String getSettings(Player player){
        Map<String, Boolean> map = setting.get(player.getName().toLowerCase());
        return new GsonBuilder().setPrettyPrinting().create().toJson(map);
    }

    public static void setSettings(Player player, String str){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        setting.put(player.getName().toLowerCase(), gson.fromJson(str, new TypeToken<HashMap<String, Boolean>>(){}.getType()));
        Main.setHide.put(player,  getPlayerHide(player));
    }

    public static HashMap<String, HashMap<String, Boolean>> setting = new HashMap<String, HashMap<String, Boolean>>();

}
