package sote.event;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.event.entity.ProjectileHitEvent;
import sote.Game;
import sote.GameProvider;
import sote.miniwalls.Miniwalls;
import sote.murder.weapon.Weapons;

public class Event_ProjectileHitEvent{

    public Event_ProjectileHitEvent(){
    }

    public static void onHit(ProjectileHitEvent event){
        System.out.println("ProjectileHitEvent");
        Weapons.hit(event);
        Entity entity = event.getEntity();
        if(entity instanceof EntityArrow){
            EntityArrow arrow = (EntityArrow) entity;
            if(arrow.shootingEntity instanceof Player){
                Player player = (Player) arrow.shootingEntity;
                Game game = GameProvider.getPlayingGame(player);
                if(game instanceof Miniwalls){
                    Miniwalls miniwalls = (Miniwalls) game;
                    miniwalls.ArrowHit(event);
                }
            }
        }
    }
}