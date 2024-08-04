package sote.event;

import cn.nukkit.event.block.BlockIgniteEvent;

public class Event_BlockIgniteEvent{

    public Event_BlockIgniteEvent(){
    }

    public static void onIgnite(BlockIgniteEvent event){
        event.setCancelled();
    }
}