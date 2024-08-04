package sote.event;

import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerLoginEvent;
import sote.Main;

public class Event_PlayerLoginEvent{

    public Event_PlayerLoginEvent(){
    }

    public static void onPlayerLogin(PlayerLoginEvent event){
        System.out.println("PlayerLoginEvent");
        Player player = event.getPlayer();
        Main.lastAttack.put(player, (long)0);
        Main.setHide.put(player, false);
        Main.gamenow.put(player, false);
        Main.PopupString.put(player, "");
        Main.updateCommandData(player);
        if(!Main.Chat.containsKey(player.getName())) Main.Chat.put(player.getName(),3);
    }

}