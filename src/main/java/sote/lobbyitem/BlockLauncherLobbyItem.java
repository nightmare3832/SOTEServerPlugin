package sote.lobbyitem;

import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.network.protocol.MoveEntityPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.scheduler.Task;
import sote.Main;;

public class BlockLauncherLobbyItem extends LobbyItem{

    public BlockLauncherLobbyItem(){
    }

    @Override
    public void move(Player player){
    }

    @Override
    public void shot(Player player){
        double MX = -Math.sin(player.yaw / 180 * Math.PI) * Math.cos(player.pitch / 180 * Math.PI)*2;
        double MY = -Math.sin(player.pitch / 180 * Math.PI)*5;
        double MZ = Math.cos(player.yaw / 180 * Math.PI) * Math.cos(player.pitch / 180 * Math.PI)*2;
        Position pos = new Position(player.x, player.y + player.getEyeHeight(), player.z);
        Map<Long,Player> players = player.getLevel().getPlayers();
        AddEntityPacket pk = new AddEntityPacket();
        Long eid = Entity.entityCount++;
        pk.entityUniqueId = eid;
        pk.entityRuntimeId = eid;
        pk.type = 66;
        pk.x = (float)player.x;
        pk.y = (float)player.y + player.getEyeHeight();
        pk.z = (float)player.z;
        int[] ids = new int[]{1,2,3,4,58,61};
        int id = ids[(int)(Math.random()*ids.length)];
        int damage = 0;
        pk.metadata = new EntityMetadata()
                .putInt(Entity.DATA_VARIANT, id | damage << 8)
                .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1);
                //.putBoolean(Entity.DATA_NO_AI, true);
        for (Map.Entry<Long,Player> e : players.entrySet()){
            e.getValue().dataPacket(pk);
        }
        HashMap<Long,Player> map;
        if(Main.lobbyitemeids.containsKey(player.getLevel())) map = Main.lobbyitemeids.get(player.getLevel());
        else map = new HashMap<Long,Player>();
        map.put(eid,player);
        Main.lobbyitemeids.put(player.getLevel(),map);
        pos.x += MX;
        pos.y += MY;
        pos.z += MZ;
        Boolean a = false;
            for (Map.Entry<Long,Player> e : players.entrySet()){
                if(pos.distance(e.getValue()) <= 1.5 || player.getLevel().getBlock(pos).getId() != 0){
                    explode(pos,players);
                    a = true;
                }
            }
        if(a){
            RemoveEntityPacket pk2 = new RemoveEntityPacket();
            pk2.eid = eid;
            for (Map.Entry<Long,Player> e : players.entrySet()){
                e.getValue().dataPacket(pk2);
            }
            HashMap<Long,Player> mapp = Main.lobbyitemeids.get(player.getLevel());
            mapp.remove(eid);
            Main.lobbyitemeids.put(player.getLevel(),mapp);
            return;
        }
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackBlock(this,player,MX,MY,MZ,0,pos,eid,player.getLevel()),1);
    }

    public void shot2(Player player,double MX,double MY,double MZ,int i,Vector3 pos,Long eid,Level level){
        Map<Long,Player> players = player.getLevel().getPlayers();
        MoveEntityPacket pk = new MoveEntityPacket();
        pk.eid = eid;
        pk.x = (float) pos.x;
        pk.y = (float) pos.y;
        pk.z = (float) pos.z;
        for (Map.Entry<Long,Player> e : players.entrySet()){
            e.getValue().dataPacket(pk);
        }
        pos.x += MX;
        pos.y += MY-i*0.1;
        pos.z += MZ;
        Boolean a = false;
            for (Map.Entry<Long,Player> e : players.entrySet()){
                if(pos.distance(e.getValue()) <= 1.5 || player.getLevel().getBlock(pos).getId() != 0){
                    a = true;
                }
            }
        i++;
        if(i >= 80 || a){
            explode(pos,players);
            RemoveEntityPacket pk2 = new RemoveEntityPacket();
            pk2.eid = eid;
            for (Map.Entry<Long,Player> e : players.entrySet()){
                e.getValue().dataPacket(pk2);
            }
            HashMap<Long,Player> map = Main.lobbyitemeids.get(level);
            map.remove(eid);
            Main.lobbyitemeids.put(player.getLevel(),map);
            return;
        }
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackBlock(this,player,MX,MY,MZ,i,pos,eid,level),1);
    }

    public void explode(Vector3 pos,Map<Long,Player> players){
        LevelEventPacket pk = new LevelEventPacket();
        pk.evid = LevelEventPacket.EVENT_ADD_PARTICLE_MASK | 14;
        pk.x = (float) pos.x;
        pk.y = (float) (pos.y+0.2);
        pk.z = (float) pos.z;
        for (Map.Entry<Long,Player> e : players.entrySet()){
            e.getValue().dataPacket(pk);
        }
    }
}
class CallbackBlock extends Task{

    public BlockLauncherLobbyItem owner;
    public Player player;
    public double mx;
    public double my;
    public double mz;
    public int ii;
    public Vector3 poss;
    public Long eidd;
    public Level level;

    public CallbackBlock(BlockLauncherLobbyItem o,Player player,double MX,double MY,double MZ,int i,Vector3 pos,Long eid,Level l){
        this.owner = o;
        this.player = player;
        this.mx = MX;
        this.my = MY;
        this.mz = MZ;
        this.ii = i;
        this.poss = pos;
        this.eidd = eid;
        this.level = l;
    }

    public void onRun(int d){
        this.owner.shot2(this.player,this.mx,this.my,this.mz,this.ii,this.poss,this.eidd,this.level);
    }
}