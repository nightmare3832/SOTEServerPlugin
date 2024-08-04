package sote.event;

import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerDropItemEvent;
import sote.Game;
import sote.GameProvider;
import sote.skywarssolo.SkywarsSolo;

public class Event_PlayerDropItemEvent{

    public Event_PlayerDropItemEvent(){
    }

    public static void onUpdate(PlayerDropItemEvent event){
        event.setCancelled();
        Player player = event.getPlayer();
        Game game = GameProvider.getPlayingGame(player);
        if(game instanceof SkywarsSolo){
            SkywarsSolo skywars = (SkywarsSolo) game;
            if(game.Players.containsValue(player) && game.getGameDataAsBoolean("gamenow")){
                event.setCancelled(false);
            }
        }
    }
}