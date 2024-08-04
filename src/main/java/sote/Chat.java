package sote;

import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;

public class Chat{

    public Chat(){
    }

    public static void chat(Player player, String msg){
        for(Map.Entry<Player, Boolean> e : channelPlayers.get(channel.get(player)).entrySet()){
            if(e.getKey() instanceof Player){
                e.getKey().sendMessage(displayName.get(player)+"§7: §f"+msg);
            }else{
                channelPlayers.get(channel.get(player)).remove(e.getKey());
            }
        }
    }

    public static void transferChannel(Player player, String name, String display){
        if(channelPlayers.containsKey(name)) createChannel(name);
        if(channel.containsKey(player)){
            channelPlayers.get(channel.get(player)).remove(player);
        }
        channel.put(player, name);
        displayName.put(player, display);
    }

    public static void createChannel(String name){
        channelPlayers.put(name, new HashMap<Player, Boolean>());
    }

    public static HashMap<Player, String> channel = new HashMap<Player, String>();
    public static HashMap<Player, String> displayName = new HashMap<Player, String>();
    public static HashMap<String, HashMap<Player, Boolean>> channelPlayers = new HashMap<String, HashMap<Player, Boolean>>();
}
