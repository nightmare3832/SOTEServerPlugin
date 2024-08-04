package sote.particle;

import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.AddItemEntityPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.MoveEntityPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.network.protocol.SetEntityDataPacket;
import cn.nukkit.scheduler.Task;

public class Generator{

    public Vector3 pos;
    public HashMap<Player, Player>  player = new HashMap<>();

    public long eid;
    public long eid2;

    public int count = 0;

    public int spawnCount = 0;

    public int spawnCountMax = 20;

    public EntityMetadata tagMetadata;

    public float lastYaw = 0;

    public boolean bool = false;

    public DataPacket[] packets = new DataPacket[4];

    public Generator(Vector3 pos) {
        this.pos = pos;
        this.spawnCount = this.spawnCountMax;
        this.eid = Entity.entityCount++;
        this.eid2 = Entity.entityCount++;
        this.packetRegister();
        startSpawner();
        startMotion();
    }

    public void start(Player player){
        this.player.put(player,  player);
        this.spawnGenerator(player);
    }

    public void finish(Player player){
        this.player.remove(player);
        this.despawnGenerator(player);
    }

    public void spawnGenerator(Player player){
        player.dataPacket(packets[0]);
        ((AddEntityPacket)packets[1]).metadata = this.tagMetadata;
        player.dataPacket(packets[1]);
    }

    public void despawnGenerator(Player player){
        player.dataPacket(packets[2]);
        player.dataPacket(packets[3]);
    }

    public void startMotion(){
        Server.getInstance().getScheduler().scheduleRepeatingTask(new CallbackMotion(this),1);
    }

    public void motion(){
        double c = Math.abs(count - 40) - 20;
        if(c == 0){
            if(!this.bool){
                this.bool = true;
            }else{
                this.bool = false;
            }
        }
        double cc = count + 20;
        if(cc > 80) cc -= 80;
        double nextY = (Math.abs(cc - 40) - 20) / 80;
        if(this.lastYaw < 0) this.lastYaw += 360;
        if(this.lastYaw >= 360) this.lastYaw -= 360;
        this.lastYaw += c;
        MoveEntityPacket pk = new MoveEntityPacket();
        pk.eid = eid;
        pk.x = this.pos.x;
        pk.y = this.pos.y + nextY;
        pk.z = this.pos.z;
        pk.headYaw = this.lastYaw;
        pk.yaw = this.lastYaw;
        pk.pitch = 0;
        this.sendPacket(pk);
        if(!this.bool){
            this.count++;
            if(this.count > 80) this.count = 0;
        }
    }

    public void startSpawner(){
        Server.getInstance().getScheduler().scheduleRepeatingTask(new CallbackSpawn(this),20);
    }

    public void spawnItemTask(){
        if(this.spawnCount == 0){
            this.spawnItem();
        }
        SetEntityDataPacket pk = new SetEntityDataPacket();
        pk.eid = this.eid2;
        this.tagMetadata.putString(Entity.DATA_NAMETAG, "       §eTire §cⅠ\n      §b§lDiamond§r\n§eSpawns in §c"+this.spawnCount+" §eseconds");
        pk.metadata = this.tagMetadata;
        this.sendPacket(pk);
        this.spawnCount--;
        if(this.spawnCount < 0) this.spawnCount = this.spawnCountMax;
    }

    public void spawnItem(){
        AddItemEntityPacket pk = new AddItemEntityPacket();
        long itemEid = Entity.entityCount++;
        pk.entityRuntimeId = itemEid;
        pk.entityUniqueId = itemEid;
        pk.item = Item.get(Item.DIAMOND, 0, 1);
        pk.speedX = 0;
        pk.speedY = 0;
        pk.speedZ = 0;
        pk.x = (float)pos.x;
        pk.y = (float)pos.y - 1;
        pk.z = (float)pos.z;
        this.sendPacket(pk);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackDespawn(this, itemEid),100);
    }

    public void despawnItem(long itemEid){
        RemoveEntityPacket pk = new RemoveEntityPacket();
        pk.eid = itemEid;
        this.sendPacket(pk);
    }

    public void sendPacket(DataPacket pk){
        for(Map.Entry<Player,Player> e : this.player.entrySet()){
            e.getValue().dataPacket(pk);
        }
    }

    public void packetRegister(){
        AddEntityPacket packet = new AddEntityPacket();
        packet.type = 35;
        packet.entityUniqueId = eid;
        packet.entityRuntimeId = eid;
        packet.x = (float) pos.x;
        packet.y = (float) pos.y;
        packet.z = (float) pos.z;
        packet.speedX = (float) 0;
        packet.speedY = (float) 0;
        packet.speedZ = (float) 0;
        packet.yaw = (float) 0;
        packet.pitch = (float) 0;
        int flags = 0;
        flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
        packet.metadata = new EntityMetadata()
                .putLong(Entity.DATA_FLAGS,flags)
                .putString(Entity.DATA_NAMETAG, "")
                .putFloat(Entity.DATA_SCALE, (float)0.8)
                .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1);
        packets[0] = packet;
        AddEntityPacket pkk = new AddEntityPacket();
        pkk.entityUniqueId = eid2;
        pkk.entityRuntimeId = eid2;
        pkk.x = (float)pos.x;
        pkk.y = (float)(pos.y + 1.5);
        pkk.z = (float)pos.z;
        pkk.yaw = 0;
        pkk.pitch = 0;
        pkk.type = EntityItem.NETWORK_ID;
        packets[1] = pkk;
        flags = 0;
        flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
        //flags |= 1 << Entity.DATA_FLAG_INVISIBLE;
        flags |= 1 << Entity.DATA_FLAG_SHOWBASE;
        this.tagMetadata = new EntityMetadata()
                .putLong(Entity.DATA_FLAGS, flags)
                .putString(Entity.DATA_NAMETAG, "       §eTire §cⅠ\n      §b§lDiamond§r\n§eSpawns in §c"+this.spawnCount+" §eseconds")
                .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1);
        RemoveEntityPacket pk = new RemoveEntityPacket();
        pk.eid = eid;
        packets[2] = pk;
        RemoveEntityPacket pk2 = new RemoveEntityPacket();
        pk.eid = eid2;
        packets[3] = pk2;
    }
}
class CallbackMotion extends Task{

    public Generator owner;

    public CallbackMotion(Generator owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.motion();
    }
}
class CallbackSpawn extends Task{

    public Generator owner;

    public CallbackSpawn(Generator owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.spawnItemTask();
    }
}
class CallbackDespawn extends Task{

    public Generator owner;
    public long eid;

    public CallbackDespawn(Generator owner, long eid){
        this.owner = owner;
        this.eid = eid;
    }

    public void onRun(int d){
        this.owner.despawnItem(this.eid);
    }
}
