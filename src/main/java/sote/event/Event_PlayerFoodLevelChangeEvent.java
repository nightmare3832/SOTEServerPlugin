package sote.event;

import cn.nukkit.event.player.PlayerFoodLevelChangeEvent;

public class Event_PlayerFoodLevelChangeEvent{

    public Event_PlayerFoodLevelChangeEvent(){
    }

    public static void onFood(PlayerFoodLevelChangeEvent event){
        event.setCancelled();
    }
}