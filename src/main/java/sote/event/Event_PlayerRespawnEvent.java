package sote.event;

import cn.nukkit.Server;
import cn.nukkit.event.player.PlayerRespawnEvent;

public class Event_PlayerRespawnEvent{

    public Event_PlayerRespawnEvent(){
    }

    public static void onRespawn(PlayerRespawnEvent event){
        System.out.println("PlayerRespawnEvent");
        //event.setRespawnPosition(new Position(-246,6,-234,Server.getInstance().getLevelByName("murder")));
        //event.setRespawnPosition(new Position(140,71,159,Server.getInstance().getLevelByName("blockhunt")));
        event.setRespawnPosition(Server.getInstance().getLevelByName("lobby").getSafeSpawn());
    }
}