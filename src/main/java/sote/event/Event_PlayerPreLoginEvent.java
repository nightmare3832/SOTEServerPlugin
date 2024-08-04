package sote.event;

import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerPreLoginEvent;
import sote.ban.Ban;

public class Event_PlayerPreLoginEvent{

    public Event_PlayerPreLoginEvent(){
    }

    public static void onPlayerPreLogin(PlayerPreLoginEvent event){
        Player player = event.getPlayer();
        if(player.getName().split(" ").length >= 2){
            player.close();
            return;
        }
        Ban.setBanData(player.getName().toLowerCase());
        Ban.checkLogin(event, player);
    }
}