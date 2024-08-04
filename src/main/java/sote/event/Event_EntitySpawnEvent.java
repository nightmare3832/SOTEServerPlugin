package sote.event;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityXPOrb;
import cn.nukkit.event.entity.EntitySpawnEvent;

public class Event_EntitySpawnEvent{

    public Event_EntitySpawnEvent(){
    }

    public static void onSpawnEntity(EntitySpawnEvent event){
        Entity entity = event.getEntity();
        if(entity instanceof EntityXPOrb){
            if(!(entity.level.getFolderName().split("skywars").length >= 2)) entity.kill();
        }
    }
}