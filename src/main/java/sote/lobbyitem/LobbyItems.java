package sote.lobbyitem;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.scheduler.Task;
import sote.Main;
import sote.PlayerClone;

public class LobbyItems{

    public LobbyItems(){
    }

    public static void startTick(){
        Server.getInstance().getScheduler().scheduleRepeatingTask(new CallbackTick(), 1);
    }

    public static void addPlayer(Player player){
        lastshot.put(player, (long)0);
        HashMap<String, Boolean> v1 = new HashMap<String, Boolean>();
        setSellectLobbyItem(player,"unknown");
        v1.put("unknown",true);
        v1.put("fireparticle",false);
        v1.put("flameparticle",false);
        v1.put("waterparticle",false);
        v1.put("musicparticle",false);
        v1.put("smokeparticle",false);
        v1.put("heartparticle",false);
        v1.put("colorparticle",false);
        v1.put("rainbowparticle",false);
        v1.put("soteparticle",false);
        v1.put("enderparticle",false);
        v1.put("crossparticle",false);
        v1.put("heartringparticle",false);
        v1.put("bomblauncher",false);
        v1.put("blocklauncher",false);
        v1.put("balloon",false);
        lobbyitemData.put(player.getName().toLowerCase(), v1);
        setSellectLobbyItemData(player);
    }

    public static void addLobbyItem(Player player,String str){
        Map<String, Boolean> map = lobbyitemData.get(player.getName().toLowerCase());
        map.put(str, true);
    }

    public static void setSellectLobbyItem(Player player,String str){
        sellectLobbyItem.put(player.getName().toLowerCase(),str);
        setSellectLobbyItemData(player);
    }

    public static void setSellectLobbyItem(Player player,Item item){
        String str = "";
        switch(item.getId()+":"+item.getDamage()){
            case "1:0":
                str = "unknown";
            break;
            case "46:0":
                str = "bombluncher";
            break;
            case "2:0":
                str = "blockluncher";
            break;
            case "377:0":
                str = "fireparticle";
            break;
            case "378:0":
                str = "flameparticle";
            break;
            case "373:0":
                str = "waterparticle";
            break;
            case "25:0":
                str = "musicparticle";
            break;
            case "369:0":
                str = "smokeparticle";
            break;
        }
        sellectLobbyItem.put(player.getName().toLowerCase(),str);
        setSellectLobbyItemData(player);
    }

    public static LobbyItem getSellectLobbyItemData(Player player){
        return LobbyItemdata.get(player);
    }

    public static String getSellectLobbyItem(Player player){
        return sellectLobbyItem.get(player.getName().toLowerCase());
    }

    public static void setSellectLobbyItemData(Player player){
        String str = sellectLobbyItem.get(player.getName().toLowerCase()).toString();
        if(LobbyItemdata.containsKey(player) && LobbyItemdata.get(player) instanceof AppearLobbyItem){
            ((AppearLobbyItem)LobbyItemdata.get(player)).unselect(player);
        }
        switch(str){
            case "unknown":
                LobbyItemdata.put(player,new UnknownLobbyItem());
            break;
            case "fireparticle":
                LobbyItemdata.put(player,new FireParticleLobbyItem());
            break;
            case "flameparticle":
                LobbyItemdata.put(player,new FlameParticleLobbyItem());
            break;
            case "waterparticle":
                LobbyItemdata.put(player,new WaterParticleLobbyItem());
            break;
            case "musicparticle":
                LobbyItemdata.put(player,new MusicParticleLobbyItem());
            break;
            case "smokeparticle":
                LobbyItemdata.put(player,new SmokeParticleLobbyItem());
            break;
            case "heartparticle":
                LobbyItemdata.put(player,new HeartParticleLobbyItem());
            break;
            case "colorparticle":
                LobbyItemdata.put(player,new ColorParticleLobbyItem());
            break;
            case "rainbowparticle":
                LobbyItemdata.put(player,new RainbowParticleLobbyItem());
            break;
            case "soteparticle":
                LobbyItemdata.put(player,new SoteParticleLobbyItem());
            break;
            case "enderparticle":
                LobbyItemdata.put(player,new EnderParticleLobbyItem());
            break;
            case "crossparticle":
                LobbyItemdata.put(player,new CrossParticleLobbyItem());
            break;
            case "heartringparticle":
                LobbyItemdata.put(player,new HeartRingParticleLobbyItem());
            break;
            case "bomblauncher":
                LobbyItemdata.put(player,new BombLauncherLobbyItem());
            break;
            case "blocklauncher":
                LobbyItemdata.put(player,new BlockLauncherLobbyItem());
            break;
            case "balloon":
                LobbyItemdata.put(player,new BalloonLobbyItem());
            break;
            default:
                LobbyItemdata.put(player,new UnknownLobbyItem());
            break;
        }
        if(LobbyItemdata.get(player) instanceof AppearLobbyItem){
            ((AppearLobbyItem)LobbyItemdata.get(player)).select(player);
        }
    }

    public static String getLobbyItems(Player player){
        Map<String, Boolean> map = lobbyitemData.get(player.getName().toLowerCase());
        return new GsonBuilder().setPrettyPrinting().create().toJson(map);
    }

    public static void setLobbyItems(Player player, String str){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        HashMap<String, Boolean> map = gson.fromJson(str, new TypeToken<HashMap<String, Boolean>>(){}.getType());
        if(!map.containsKey("unknown")) map.put("unknown",true);
        if(!map.containsKey("fireparticle")) map.put("fireparticle",false);
        if(!map.containsKey("flameparticle")) map.put("flameparticle",false);
        if(!map.containsKey("waterparticle")) map.put("waterparticle",false);
        if(!map.containsKey("musicparticle")) map.put("musicparticle",false);
        if(!map.containsKey("smokeparticle")) map.put("smokeparticle",false);
        if(!map.containsKey("heartparticle")) map.put("heartparticle",false);
        if(!map.containsKey("colorparticle")) map.put("colorparticle",false);
        if(!map.containsKey("rainbowparticle")) map.put("rainbowparticle",false);
        if(!map.containsKey("soteparticle")) map.put("soteparticle",false);
        if(!map.containsKey("enderparticle")) map.put("enderparticle",false);
        if(!map.containsKey("crossparticle")) map.put("crossparticle",false);
        if(!map.containsKey("heartringparticle")) map.put("heartringparticle",false);
        if(!map.containsKey("bomblauncher")) map.put("bomblauncher",false);
        if(!map.containsKey("blocklauncher")) map.put("blocklauncher",false);
        if(!map.containsKey("balloon")) map.put("balloon",false);
        lobbyitemData.put(player.getName().toLowerCase(), map);
    }

    public static void move(Player player){
        if(Main.gamenow.get(player) == false && LobbyItemdata.containsKey(player)) LobbyItemdata.get(player).move(player);
    }

    public static void shot(Player player){
        if(Main.gamenow.get(player) == false && System.currentTimeMillis() - lastshot.get(player) >= 250){
            LobbyItemdata.get(player).shot(player);
            lastshot.put(player, System.currentTimeMillis());
        }
    }

    public static void tick(){
        for (Map.Entry<Player, LobbyItem> e : LobbyItemdata.entrySet()){
            if(Main.gamenow.get(e.getKey()) == false && e.getKey().isOnline()) e.getValue().tick(e.getKey());
        }
    }

    public static String getSellectLobbyItem(PlayerClone player){
        return String.valueOf(sellectLobbyItem.get(player.getName().toLowerCase()));
    }

    public static HashMap<String, HashMap<String, Boolean>> lobbyitemData = new HashMap<String, HashMap<String, Boolean>>();
    public static HashMap<String, String> sellectLobbyItem = new HashMap<String, String>();
    public static HashMap<Player,LobbyItem> LobbyItemdata = new HashMap<Player,LobbyItem>();
    public static HashMap<Player,Long> lastshot = new HashMap<Player,Long>();
    public static File file;

}
class CallbackTick extends Task{

    public CallbackTick(){
    }

    public void onRun(int d){
        go();
    }

    public void go(){
        LobbyItems.tick();
    }
}