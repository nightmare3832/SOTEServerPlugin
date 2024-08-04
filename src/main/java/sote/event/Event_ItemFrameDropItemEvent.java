package sote.event;

import cn.nukkit.Player;
import cn.nukkit.event.block.ItemFrameDropItemEvent;
import sote.Main;

public class Event_ItemFrameDropItemEvent{

    public Event_ItemFrameDropItemEvent(){
    }

    public static void onItemFrame(ItemFrameDropItemEvent event){
        Player player = event.getPlayer();
            if(Main.Blocker.get(player.getName()) == false){
                event.setCancelled();
            }else{
                event.setCancelled(false);
            }
    }
}