package sote.event;

import cn.nukkit.Player;
import cn.nukkit.event.player.*;
import cn.nukkit.item.Item;
import sote.Main;

public class Event_PlayerItemHeldEvent{

    public Event_PlayerItemHeldEvent(){
    }

    public static void onHeld(PlayerItemHeldEvent event){
        //Player player = event.getPlayer();
        //Item item = event.getItem();
        //Main.PopupItem(player,item.getName());
    }
}