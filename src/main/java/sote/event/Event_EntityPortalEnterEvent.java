package sote.event;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityPortalEnterEvent;

public class Event_EntityPortalEnterEvent{

    public Event_EntityPortalEnterEvent(){
    }

    public static void onNether(EntityPortalEnterEvent event){
        Entity entity = event.getEntity();
        entity.inPortalTicks = 0;
        if(entity instanceof Player){
            Player player = (Player) entity;
            int px = (int)player.x;
            if(px >= 131 && px <= 133){
                //Inventorys.setData(player, new ArcadeInventory());
                //player.teleport(Server.getInstance().getLevelByName("arcade").getSafeSpawn());
            }else if(px <= 130 && px >= 124){
                
            }else if(px <= 123 && px >= 121){
                //Inventorys.setData(player, new ClassicInventory());
                //player.teleport(Server.getInstance().getLevelByName("classic").getSafeSpawn());
            }
        }
    }
}