package sote.event;

import cn.nukkit.entity.item.EntityPrimedTNT;
import cn.nukkit.event.entity.EntityExplosionPrimeEvent;

public class Event_EntityExplosionPrimeEvent{

    public Event_EntityExplosionPrimeEvent(){
    }

    public static void onExplosion(EntityExplosionPrimeEvent event){
        if(event.getEntity().getLevel().getFolderName().split("buildbattle").length >= 2){
            event.setCancelled();
        }
        if(event.getEntity().getLevel().getFolderName().split("bedwars").length >= 2){
            if(event.getEntity() instanceof EntityPrimedTNT){
                event.setForce(2);
            }
        }
    }
}