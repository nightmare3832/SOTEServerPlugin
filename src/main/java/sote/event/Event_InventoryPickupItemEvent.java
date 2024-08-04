package sote.event;

import cn.nukkit.Player;
import cn.nukkit.event.inventory.InventoryPickupItemEvent;
import cn.nukkit.inventory.InventoryHolder;
import sote.Game;
import sote.GameProvider;
import sote.blockhunt.Blockhunt;
import sote.miniwalls.Miniwalls;
import sote.murder.Murder;
import sote.skywarssolo.SkywarsSolo;

public class Event_InventoryPickupItemEvent{

    public Event_InventoryPickupItemEvent(){
    }

    public static void onPickup(InventoryPickupItemEvent event){
        InventoryHolder holder = event.getInventory().getHolder();
        if(!(holder instanceof Player)) return;
        Player player = (Player) holder;
        Game game = GameProvider.getPlayingGame(player);
        if(game instanceof Murder){
            Murder murder = (Murder) game;
            murder.Pickup(event);
        }else if(game instanceof SkywarsSolo){
        	SkywarsSolo skywars = (SkywarsSolo) game;
            skywars.Pickup(event);
        }else if(game instanceof Miniwalls){
            Miniwalls miniwalls = (Miniwalls) game;
            miniwalls.Pickup(event);
        }else if(game instanceof Blockhunt){
            Blockhunt blockhunt = (Blockhunt) game;
            blockhunt.Pickup(event);
        }
    }
}