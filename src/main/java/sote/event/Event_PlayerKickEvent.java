package sote.event;

import cn.nukkit.event.player.PlayerKickEvent;

public class Event_PlayerKickEvent{

    public Event_PlayerKickEvent(){
    }

    public static void onKick(PlayerKickEvent event){
        System.out.println("PlayerKickEvent");
        //if(event.getReason().equals("Flying is not enabled on this server")) event.setCancelled();
    }
}