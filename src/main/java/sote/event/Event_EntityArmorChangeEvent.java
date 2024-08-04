package sote.event;


import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityArmorChangeEvent;
import sote.Game;
import sote.GameProvider;
import sote.Main;
import sote.bedwars.Bedwars;
import sote.skywarssolo.SkywarsSolo;

public class Event_EntityArmorChangeEvent{

    public Event_EntityArmorChangeEvent(){
    }

    public static void onArmor(EntityArmorChangeEvent event){
        Entity entity = event.getEntity();
            if(entity instanceof Player){
                Player player = (Player) entity;
                if(Main.canArmor.containsKey(player) && !Main.canArmor.get(player)) event.setCancelled();
                Game game = GameProvider.getPlayingGame(player);
                if(game instanceof SkywarsSolo){
                    if(game.Players.containsValue(player) && game.getGameDataAsBoolean("gamenow")){
                        event.setCancelled(false);
                    }
                }
                if(game instanceof Bedwars){
                    if(game.Players.containsValue(player) && game.getGameDataAsBoolean("gamenow")){
                        event.setCancelled(false);
                    }
                }
            }
    }
}