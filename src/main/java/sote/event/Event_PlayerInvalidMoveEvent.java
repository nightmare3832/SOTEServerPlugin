package sote.event;

import cn.nukkit.event.player.PlayerInvalidMoveEvent;

public class Event_PlayerInvalidMoveEvent{

    public Event_PlayerInvalidMoveEvent(){
    }

    public static void onNotMove(PlayerInvalidMoveEvent event){
        event.setCancelled();
    }
}