package sote;

import java.util.HashMap;

import cn.nukkit.Player;

public class PlayerDataManager{

    public static PlayerData getPlayerData(int id){
        if(!playerDataID.containsKey(id)) addPlayerData(id);
        return playerDataID.get(id);
    }

    public static PlayerData getPlayerData(Player player){
        return getPlayerData(player.getName());
    }

    public static PlayerData getPlayerData(String name){
        if(!playerDataName.containsKey(name)) addPlayerData(name);
        return playerDataName.get(name.toLowerCase());
    }

    public static void addPlayerData(Player player){
        int id = Integer.parseInt(MySQL.getGeneralField(player, "id"));
        PlayerData data = new PlayerData(id, player.getName());
        playerDataID.put(id, data);
        playerDataName.put(player.getName().toLowerCase(), data);
    }

    public static void addPlayerData(String name){
        int id = Integer.parseInt(MySQL.getGeneralField(name, "id"));
        String realName = MySQL.getGeneralField(name, "name");
        PlayerData data = new PlayerData(id, realName);
        playerDataID.put(id, data);
        playerDataName.put(name.toLowerCase(), data);
    }

    public static void addPlayerData(int id){
        String name = MySQL.getGeneralField(id, "name");
        PlayerData data = new PlayerData(id, name);
        playerDataID.put(id, data);
        playerDataName.put(name.toLowerCase(), data);
    }

    public static HashMap<Integer, PlayerData> playerDataID = new HashMap<>();
    public static HashMap<String, PlayerData> playerDataName = new HashMap<>();

}
