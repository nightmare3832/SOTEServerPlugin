package sote.event;

import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.scheduler.Task;
import sote.Game;
import sote.GameProvider;
import sote.miniwalls.Miniwalls;
import sote.murder.Murder;

public class Event_ProjectileLaunchEvent{

    public Event_ProjectileLaunchEvent(){
    }

    public static void onLaunch(ProjectileLaunchEvent event){
        System.out.println("ProjectileLaunchEvent");
        EntityProjectile projectile = event.getEntity();
        if(projectile instanceof EntityArrow){
            EntityArrow arrow = (EntityArrow) projectile;
            if(arrow.shootingEntity instanceof Player){
                Player player = (Player) arrow.shootingEntity;
                if(player.getGamemode() == 3) event.setCancelled();
                Game game = GameProvider.getPlayingGame(player);
                if(game instanceof Miniwalls){
                    Miniwalls miniwalls = (Miniwalls) game;
                    if(miniwalls.team.containsKey(player)){
                        if(miniwalls.team.get(player) == 0) arrow.setParticleColor(204,0,0,255,1);//RED
                        else if(miniwalls.team.get(player) == 1) arrow.setParticleColor(0,0,204,255,1);//BLUE
                        else if(miniwalls.team.get(player) == 2) arrow.setParticleColor(0,255,65,255,1);//GREEN
                        else if(miniwalls.team.get(player) == 3) arrow.setParticleColor(255,255,0,255,1);//YELLOW
                        miniwalls.arrow.put(player, miniwalls.arrow.get(player) - 1);
                    }
                }
                if(game instanceof Murder){
                    Murder murder = (Murder) game;
                    arrow.setParticleColor(255, 255, 255, 0xff, 2);
                    for (Map.Entry<String,Player> e : murder.AllPlayers.entrySet()){
                        long eid = Entity.entityCount++;
                        AddEntityPacket pk = new AddEntityPacket();
                        pk.entityUniqueId = eid;
                        pk.entityRuntimeId = eid;
                        pk.type = 33;
                        pk.x = (float) e.getValue().x;
                        pk.y = (float) e.getValue().y;
                        pk.z = (float) e.getValue().z;
                        pk.yaw = 0;
                        pk.pitch = 0;
                        int flags = 0;
                        flags |= 1 << Entity.DATA_FLAG_INVISIBLE;
                        flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
                        pk.metadata = new EntityMetadata()
                                .putLong(Entity.DATA_FLAGS,flags)
                                .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1);
                        e.getValue().dataPacket(pk);
                        EntityEventPacket pk2 = new EntityEventPacket();
                        pk2.eid = eid;
                        pk2.event = EntityEventPacket.HURT_ANIMATION;
                        e.getValue().dataPacket(pk2);
                        RemoveEntityPacket pk3 = new RemoveEntityPacket();
                        pk3.eid = eid;
                        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackSound(e.getValue(), pk), 50);
                    }
                }
            }
        }
    }

    public static void packet(Player player, DataPacket pk){
        player.dataPacket(pk);
    }
}
class CallbackSound extends Task{

    public Player player;
    public DataPacket pk;

    public CallbackSound(Player player, DataPacket pk){
        this.player = player;
        this.pk = pk;
    }

    public void onRun(int d){
    	Event_ProjectileLaunchEvent.packet(player, pk);
    }
}