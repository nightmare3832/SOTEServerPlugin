package sote.bedwars.finalkilleffect;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import cn.nukkit.Player;
import cn.nukkit.math.Vector3;

public class BedwarsFinalkillEffects{

    public BedwarsFinalkillEffects(){
        
    }

    public static void addPlayer(Player player){
        HashMap<String, Boolean> v1 = new HashMap<String, Boolean>();
        setSelectFinalkillEffect(player,"unknown");
        v1.put("unknown", true);
        finalkillEffectData.put(player.getName().toLowerCase(),v1);
    }

    public static void setSelectFinalkillEffect(Player player, String name){
        selectFinalkillEffect.put(player.getName().toLowerCase(), name);
    }

    public static void run(Player player, Vector3 pos){
        switch(selectFinalkillEffect.get(player.getName().toLowerCase())){
            case "unknown":
                
        }
    }

    public static String getFinalkillEffects(Player player){
        Map<String, Boolean> map = finalkillEffectData.get(player.getName().toLowerCase());
        return new GsonBuilder().setPrettyPrinting().create().toJson(map);
    }

    public static void setFinalkillEffects(Player player, String str){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        HashMap<String, Boolean> map = gson.fromJson(str, new TypeToken<HashMap<String, Boolean>>(){}.getType());
        if(!map.containsKey("unknown")) map.put("unknown", true);
        finalkillEffectData.put(player.getName().toLowerCase(), map);
    }

    public static HashMap<String, HashMap<String, Boolean>> finalkillEffectData = new HashMap<>();
    public static HashMap<String, String> selectFinalkillEffect = new HashMap<>();

}