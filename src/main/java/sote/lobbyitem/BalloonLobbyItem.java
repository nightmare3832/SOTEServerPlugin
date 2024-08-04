package sote.lobbyitem;

import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.level.Level;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.MoveEntityPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.scheduler.Task;
import sote.Main;

public class BalloonLobbyItem extends LobbyItem implements AppearLobbyItem{

    public BalloonLobbyItem(){
    }

    @Override
    public void move(Player player){
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackMove(this,player,player.x, player.y, player.z, player.getLevel().getName()), 20);
    }

    public void moveTo(Player player, double x, double y, double z, String levelName){
        if(!player.getLevel().getName().equals(levelName)) return;
        Map<Long,Player> players = player.getLevel().getPlayers();
        this.X = x;
        this.Y = y + 4;
        this.Z = z;
        for(Map.Entry<Long, Player> e : players.entrySet()){
            MoveEntityPacket pk = new MoveEntityPacket();
            pk.eid = this.eid;
            pk.x = this.X;
            pk.y = this.Y;
            pk.z = this.Z;
            pk.yaw = 0;
            pk.pitch = 0;
            e.getValue().dataPacket(pk);
            MoveEntityPacket pk2 = new MoveEntityPacket();
            pk2.eid = this.eid2;
            pk2.x = this.X;
            pk2.y = this.Y - 1;
            pk2.z = this.Z;
            pk2.yaw = 0;
            pk2.pitch = 0;
            e.getValue().dataPacket(pk2);
            MoveEntityPacket pk3 = new MoveEntityPacket();
            pk3.eid = this.eid3;
            pk3.x = this.X;
            pk3.y = this.Y - 0.5;
            pk3.z = this.Z;
            pk3.yaw = 0;
            pk3.pitch = 0;
            e.getValue().dataPacket(pk3);
        }
    }

    @Override
    public void select(Player player){
        this.owner = player;
        Map<Long,Player> players = player.getLevel().getPlayers();
        for(Map.Entry<Long, Player> e : players.entrySet()){
            spawnTo(e.getValue());
        }
        HashMap<LobbyItem,Player> map;
        if(Main.lobbyitementities.containsKey(player.getLevel().getName())) map = Main.lobbyitementities.get(player.getLevel().getName());
        else map = new HashMap<LobbyItem,Player>();
        map.put(this,player);
    }

    @Override
    public void unselect(Player player){
        Map<Long,Player> players = player.getLevel().getPlayers();
        for(Map.Entry<Long, Player> e : players.entrySet()){
            despawnTo(e.getValue());
        }
        HashMap<LobbyItem,Player> map;
        if(Main.lobbyitementities.containsKey(player.getLevel().getName())) map = Main.lobbyitementities.get(player.getLevel().getName());
        else map = new HashMap<LobbyItem,Player>();
        map.put(this,player);
    }

    @Override
    public void switchLevel(Player player, Level before, Level after){
        Map<Long,Player> players = before.getPlayers();
        for(Map.Entry<Long, Player> e : players.entrySet()){
            if(!e.getValue().equals(this.owner)) despawnTo(e.getValue());
        }
        players = after.getPlayers();
        for(Map.Entry<Long, Player> e : players.entrySet()){
            spawnTo(e.getValue());
        }
        HashMap<LobbyItem,Player> map;
        if(Main.lobbyitementities.containsKey(before.getName())) map = Main.lobbyitementities.get(before.getName());
        else map = new HashMap<LobbyItem,Player>();
        map.remove(this);
        if(Main.lobbyitementities.containsKey(after.getName())) map = Main.lobbyitementities.get(after.getName());
        else map = new HashMap<LobbyItem,Player>();
        map.put(this,player);
    }

    @Override
    public void spawnTo(Player player){
        AddEntityPacket pk = new AddEntityPacket();
        pk.entityRuntimeId = this.eid;
        pk.entityUniqueId = this.eid;
        pk.yaw = 0;
        pk.pitch = 0;
        pk.x = (float)this.X;
        pk.y = (float)this.Y;
        pk.z = (float)this.Z;
        pk.type = 66;
        int id = 35;
        int damage = (int)(Math.random()*15);
        int flags = 0;
        flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
        pk.metadata = new EntityMetadata()
                .putLong(Entity.DATA_FLAGS,flags)
                .putInt(Entity.DATA_VARIANT, id | damage << 8)
                .putLong(Entity.DATA_LEAD_HOLDER_EID, -1);
        player.dataPacket(pk);
        AddEntityPacket pk2 = new AddEntityPacket();
        pk2.entityRuntimeId = this.eid2;
        pk2.entityUniqueId = this.eid2;
        pk2.yaw = 0;
        pk2.pitch = 0;
        pk2.x = (float)this.X;
        pk2.y = (float)(this.Y - 1);
        pk2.z = (float)this.Z;
        pk2.type = 37;
        int flags2 = 0;
        flags2 |= 1 << Entity.DATA_FLAG_IMMOBILE;
        flags2 |= 1 << Entity.DATA_FLAG_INVISIBLE;
        pk2.metadata = new EntityMetadata()
                .putLong(Entity.DATA_FLAGS,flags2)
                .putLong(Entity.DATA_LEAD_HOLDER_EID, this.owner.getId());
        player.dataPacket(pk2);
        AddEntityPacket pk3 = new AddEntityPacket();
        pk3.entityRuntimeId = this.eid3;
        pk3.entityUniqueId = this.eid3;
        pk3.yaw = 0;
        pk3.pitch = 0;
        pk3.x = (float)this.X;
        pk3.y = (float)(this.Y - 0.5);
        pk3.z = (float)this.Z;
        pk3.type = 88;
        int flags3 = 0;
        flags3 |= 1 << Entity.DATA_FLAG_IMMOBILE;
        pk3.metadata = new EntityMetadata()
                .putLong(Entity.DATA_FLAGS,flags3)
                .putLong(Entity.DATA_LEAD_HOLDER_EID, this.owner.getId());
        player.dataPacket(pk3);
    }

    @Override
    public void despawnTo(Player player){
        RemoveEntityPacket pk = new RemoveEntityPacket();
        pk.eid = this.eid;
        player.dataPacket(pk);
        RemoveEntityPacket pk2 = new RemoveEntityPacket();
        pk2.eid = this.eid2;
        player.dataPacket(pk2);
        RemoveEntityPacket pk3 = new RemoveEntityPacket();
        pk3.eid = this.eid3;
        player.dataPacket(pk3);
    }

    public double X = 0;
    public double Y = 0;
    public double Z = 0;
    public Player owner;
    public long eid = Entity.entityCount++;
    public long eid2 = Entity.entityCount++;
    public long eid3 = Entity.entityCount++;
}
class CallbackMove extends Task{

    public BalloonLobbyItem owner;
    public Player player;
    public double mx;
    public double my;
    public double mz;
    public String levelName;

    public CallbackMove(BalloonLobbyItem o,Player player,double MX,double MY,double MZ, String levelName){
        this.owner = o;
        this.player = player;
        this.mx = MX;
        this.my = MY;
        this.mz = MZ;
        this.levelName = levelName;
    }

    public void onRun(int d){
        this.owner.moveTo(this.player, this.mx, this.my, this.mz, this.levelName);
    }
}