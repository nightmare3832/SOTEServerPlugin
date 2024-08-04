package sote.event;

import cn.nukkit.Player;
import cn.nukkit.event.block.BlockPlaceEvent;
import sote.Game;
import sote.GameProvider;
import sote.Main;
import sote.bedwars.Bedwars;
import sote.buildbattle.Buildbattle;
import sote.miniwalls.Miniwalls;
import sote.murder.Murder;
import sote.skywarssolo.SkywarsSolo;

public class Event_BlockPlaceEvent{

    public Event_BlockPlaceEvent(){
    }

    public static void onPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();
            if(Main.Blocker.get(player.getName()) == false){
                event.setCancelled();
            }
            Game game = GameProvider.getPlayingGame(player);
            if(game instanceof Murder){
                Murder murder = (Murder) game;
                if(murder.jobs.containsKey(player) && player.getGamemode() == 1){
                    event.setCancelled();
                }
            }else if(game instanceof SkywarsSolo){
                if(game.Players.containsValue(player) && game.getGameDataAsBoolean("gamenow") && player.getGamemode() == 0){
                    event.setCancelled(false);
                }
            }else if(game instanceof Buildbattle){
                Buildbattle buildbattle = (Buildbattle) game;
                if(buildbattle.Players.containsValue(player) && buildbattle.getGameDataAsBoolean("gamenow")){
                    buildbattle.onplace(event);
                }
            }else if(game instanceof Miniwalls){
                Miniwalls miniwalls = (Miniwalls) game;
                if(miniwalls.Players.containsValue(player) && miniwalls.getGameDataAsBoolean("gamenow")){
                    miniwalls.onplace(event);
                }
            }else if(game instanceof Bedwars){
                Bedwars bedwars = (Bedwars) game;
                if(bedwars.Players.containsValue(player) && bedwars.getGameDataAsBoolean("gamenow")){
                    event.setCancelled(false);
                    bedwars.onPlace(event);
                }
            }
    }
}