package sote.event;

import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerItemConsumeEvent;

public class Event_PlayerItemConsumeEvent{

    public Event_PlayerItemConsumeEvent(){
    }

    public static void onEat(PlayerItemConsumeEvent event){
        Player player = event.getPlayer();
        //if(Murder.jobs.containsKey(player)) event.setCancelled();
    }
}