package sote.event;

import cn.nukkit.event.block.BlockBurnEvent;

public class Event_BlockBurnEvent{

    public Event_BlockBurnEvent(){
    }

    public static void onBurn(BlockBurnEvent event){
        event.setCancelled();
    }
}