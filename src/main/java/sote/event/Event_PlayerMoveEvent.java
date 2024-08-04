package sote.event;

import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerMoveEvent;
import sote.Game;
import sote.GameProvider;
import sote.lobbyitem.LobbyItems;
import sote.murder.Murder;

public class Event_PlayerMoveEvent{

    public Event_PlayerMoveEvent(){
    }

    public static void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        Game game = GameProvider.getPlayingGame(player);
        if(game instanceof Murder){
            Murder murder = (Murder) game;
            murder.move(player);
        }
        LobbyItems.move(player);
            //if(player.getGamemode() == 3) player.boundingBox = new AxisAlignedBB(0, 0, 0, 0, 0, 0);
    }
}