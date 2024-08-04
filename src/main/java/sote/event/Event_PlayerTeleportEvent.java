package sote.event;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.BossEventPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.network.protocol.SetEntityDataPacket;
import cn.nukkit.scheduler.Task;
import sote.Main;

public class Event_PlayerTeleportEvent{

    public Event_PlayerTeleportEvent(){
    }

    public static void onTeleport(PlayerTeleportEvent event){
        System.out.println("PlayerTeleportEvent");
        Player player = event.getPlayer();
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackBoss(player),10);
    }

    public static void run(Player player){
        RemoveEntityPacket pkk = new RemoveEntityPacket();
        pkk.eid = Main.BossGauge;
        player.dataPacket(pkk);
        AddEntityPacket pk = new AddEntityPacket();
        pk.entityUniqueId = Main.BossGauge;
        pk.entityRuntimeId = Main.BossGauge;
        pk.type = 37;
        pk.x = (float)player.x;
        pk.y = (float)player.y;
        pk.z = (float)player.z;
        pk.yaw = 0;
        pk.pitch = 0;
        int flags = 0;
        flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
        flags |= 1 << Entity.DATA_FLAG_INVISIBLE;
        flags |= 1 << Entity.DATA_FLAG_SILENT;
        pk.metadata = new EntityMetadata()
                .putLong(Entity.DATA_FLAGS,flags)
                .putString(Entity.DATA_NAMETAG,Main.BossName.get(player))
                .putFloat(Entity.DATA_SCALE,(float)0.1)
                .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1);
        player.dataPacket(pk);
        BossEventPacket boss = new BossEventPacket();
        boss.eid = Main.BossGauge;
        boss.type = 0;
        player.dataPacket(boss);
        Main.sendBossGaugeValue(player);
    }

    public static SetEntityDataPacket sp = new SetEntityDataPacket();

}
class CallbackBoss extends Task{

    public Player player;

    public CallbackBoss(Player p){
        player = p;
    }

    public void onRun(int d){
        go();
    }

    public void go(){
        Event_PlayerTeleportEvent.run(player);
    }
}