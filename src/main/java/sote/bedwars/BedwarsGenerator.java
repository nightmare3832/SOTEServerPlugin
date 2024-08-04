package sote.bedwars;

import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.item.Item;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.MoveEntityPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.network.protocol.SetEntityDataPacket;
import cn.nukkit.scheduler.Task;

public class BedwarsGenerator{

    public Bedwars owner;
    public Vector3 pos;
    public HashMap<Player, Player>  player = new HashMap<>();

    public long eid;
    public long eid2;

    public int count = 0;

    public int spawnCount = 0;

    public int spawnCountMax = 20;

    public int tire = 1;

    public EntityMetadata tagMetadata;

    public float lastYaw = 0;

    public boolean bool = false;

    public DataPacket[] packets = new DataPacket[4];

    public int type = GENERATOR_DIAMOND;

    public static final int GENERATOR_DIAMOND = 0;
    public static final int GENERATOR_EMERALD = 1;

    public Vector3 motion = new Vector3();

    public int taskId = 0;

    public AxisAlignedBB boundingBox;

    public BedwarsGenerator(Bedwars o, Vector3 pos, int type) {
        this.owner = o;
        this.pos = pos.add(0, 5, 0);
        this.type = type;
        this.spawnCount = this.spawnCountMax;
        this.eid = Entity.entityCount++;
        this.eid2 = Entity.entityCount++;
        this.boundingBox = new AxisAlignedBB(this.pos.x, this.pos.y, this.pos.z, this.pos.x, this.pos.y, this.pos.z).grow(5, 5, 5);
        this.packetRegister();
        this.changeTire(1);
        startGenerator();
        startSpawner();
        startMotion();
    }

    public void startGenerator(){
        sendPacket(packets[0]);
        ((AddEntityPacket)packets[1]).metadata = this.tagMetadata;
        sendPacket(packets[1]);
    }

    public void stopGenerator(){
        sendPacket(packets[2]);
        sendPacket(packets[3]);
        Server.getInstance().getScheduler().cancelTask(taskId);
    }

    public void stopGenerator(Player player){
        player.dataPacket(packets[2]);
        player.dataPacket(packets[3]);
    }

    public void startMotion(){
        Task task = new CallbackMotion(this);
        taskId = task.getTaskId();
        Server.getInstance().getScheduler().scheduleRepeatingTask(task,1);
    }

    public void increaseTire(){
        this.tire++;
        changeTire(this.tire);
    }

    public void changeTire(int tire){
        switch(this.type){
            case GENERATOR_DIAMOND:
                switch(tire){
                    case 1:
                        this.spawnCountMax = 30;
                    break;
                    case 2:
                        this.spawnCountMax = 20;
                    break;
                    case 3:
                        this.spawnCountMax = 10;
                    break;
                }
            break;
            case GENERATOR_EMERALD:
                switch(tire){
                    case 1:
                        this.spawnCountMax = 60;
                    break;
                    case 2:
                        this.spawnCountMax = 40;
                    break;
                    case 3:
                        this.spawnCountMax = 30;
                    break;
                }
            break;
        }
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
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackSpawn(this),20);
    }

    public void spawnItemTask(){
        if(!this.owner.getGameDataAsBoolean("gamenow")) return;
        if(this.spawnCount == 0){
            this.spawnItem();
        }
        SetEntityDataPacket pk = new SetEntityDataPacket();
        pk.eid = this.eid2;
        this.tagMetadata.putString(Entity.DATA_NAMETAG, "       §eTire §c"+getTireStringByNumber(this.tire)+"\n      §b§lDiamond§r\n§eSpawns in §c"+this.spawnCount+" §eseconds");
        pk.metadata = this.tagMetadata;
        this.sendPacket(pk);
        this.spawnCount--;
        if(this.spawnCount < 0) this.spawnCount = this.spawnCountMax;
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackSpawn(this),20);
    }

    public void spawnItem(){
        if(!checkAroundResource()) return;
        Item item = null;
        if(type == GENERATOR_DIAMOND) item = Item.get(Item.DIAMOND, 0, 1);
        if(type == GENERATOR_EMERALD) item = Item.get(Item.EMERALD, 0, 1);
        this.owner.world.dropItem(pos, item, motion);
    }

    public void sendPacket(DataPacket pk){
        for(Map.Entry<String, Player> e : this.owner.Players.entrySet()){
            e.getValue().dataPacket(pk);
        }
    }

    public boolean checkAroundResource(){
        int id = 0;
        if(type == GENERATOR_DIAMOND) id = Item.DIAMOND;
        if(type == GENERATOR_EMERALD) id = Item.EMERALD;
        int count = 0;
        for (Entity entity : this.owner.world.getNearbyEntities(this.boundingBox, (Entity)null)) {
            if (entity instanceof EntityItem){
                EntityItem entityItem = (EntityItem) entity;
                if(entityItem.getItem().getId() == id){
                    count += entityItem.getItem().getCount();
                }
            }
        }
        return count < 4;
    }

    public String getTireStringByNumber(int number){
        switch(number){
            case 1:
                return "Ⅰ";
            case 2:
                return "Ⅱ";
            case 3:
                return "Ⅲ";
        }
        return "";
    }

    public void packetRegister(){
        AddEntityPacket packet = new AddEntityPacket();
        if(type == GENERATOR_DIAMOND) packet.type = 35;
        if(type == GENERATOR_EMERALD) packet.type = 40;
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
                .putFloat(Entity.DATA_SCALE, (float)1)
                .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1);
        if(type == GENERATOR_EMERALD) packet.metadata.putFloat(Entity.DATA_SCALE, (float)1.2);
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

    public BedwarsGenerator owner;

    public CallbackMotion(BedwarsGenerator owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.motion();
    }
}
class CallbackSpawn extends Task{

    public BedwarsGenerator owner;

    public CallbackSpawn(BedwarsGenerator owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.spawnItemTask();
    }
}