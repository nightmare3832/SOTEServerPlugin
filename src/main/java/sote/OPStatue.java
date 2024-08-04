package sote;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.AddPlayerPacket;
import cn.nukkit.network.protocol.PlayerListPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;

public class OPStatue{

    public OPStatue(File pfile){
        file = pfile;
        String url = file.toString()+"/skin/";
        String name;
        name = "Kactly";
        eids.put(name,Entity.entityCount++);
        skins.put(name,new Skin[]{new Skin(new File(url+name+"-online.png")),new Skin(new File(url+name+"-offline.png"))});
        poss.put(name,new Vector3(127.5,54,185.5));
        name = "nightmare3832";
        eids.put(name,Entity.entityCount++);
        skins.put(name,new Skin[]{new Skin(new File(url+name+"-online.png")),new Skin(new File(url+name+"-offline.png"))});
        poss.put(name,new Vector3(128.5,54,185.5));
        name = "ryu119200";
        eids.put(name,Entity.entityCount++);
        skins.put(name,new Skin[]{new Skin(new File(url+name+"-online.png")),new Skin(new File(url+name+"-offline.png"))});
        poss.put(name,new Vector3(126.5,54,185.5));
        name = "RiNGo4696";
        eids.put(name,Entity.entityCount++);
        skins.put(name,new Skin[]{new Skin(new File(url+name+"-online.png")),new Skin(new File(url+name+"-offline.png"))});
        poss.put(name,new Vector3(128.5,55,186.5));
        name = "ryokucha0813";
        eids.put(name,Entity.entityCount++);
        skins.put(name,new Skin[]{new Skin(new File(url+name+"-online.png")),new Skin(new File(url+name+"-offline.png"))});
        poss.put(name,new Vector3(126.5,55,186.5));
        name = "pastoon";
        eids.put(name,Entity.entityCount++);
        skins.put(name,new Skin[]{new Skin(new File(url+name+"-online.png")),new Skin(new File(url+name+"-offline.png"))});
        poss.put(name,new Vector3(127.5,55,186.5));
        name = "komuten";
        eids.put(name,Entity.entityCount++);
        skins.put(name,new Skin[]{new Skin(new File(url+name+"-online.png")),new Skin(new File(url+name+"-offline.png"))});
        poss.put(name,new Vector3(127.5,56,187.5));
        name = "ItsKiRi";
        eids.put(name,Entity.entityCount++);
        skins.put(name,new Skin[]{new Skin(new File(url+name+"-online.png")),new Skin(new File(url+name+"-offline.png"))});
        poss.put(name,new Vector3(128.5,56,187.5));
        name = "sakaty";
        eids.put(name,Entity.entityCount++);
        skins.put(name,new Skin[]{new Skin(new File(url+name+"-online.png")),new Skin(new File(url+name+"-offline.png"))});
        poss.put(name,new Vector3(126.5,57,188.5));
        name = "tamu";
        eids.put(name,Entity.entityCount++);
        skins.put(name,new Skin[]{new Skin(new File(url+name+"-online.png")),new Skin(new File(url+name+"-offline.png"))});
        poss.put(name,new Vector3(127.5,57,188.5));
        name = "kaito0506";
        eids.put(name,Entity.entityCount++);
        skins.put(name,new Skin[]{new Skin(new File(url+name+"-online.png")),new Skin(new File(url+name+"-offline.png"))});
        poss.put(name,new Vector3(128.5,57,188.5));
        name = "syuutaMC";
        eids.put(name,Entity.entityCount++);
        skins.put(name,new Skin[]{new Skin(new File(url+name+"-online.png")),new Skin(new File(url+name+"-offline.png"))});
        poss.put(name,new Vector3(129.5,54.5,186.5));
        name = "Yuk1Nek0";
        eids.put(name,Entity.entityCount++);
        skins.put(name,new Skin[]{new Skin(new File(url+name+"-online.png")),new Skin(new File(url+name+"-offline.png"))});
        poss.put(name,new Vector3(129.5,55.5,187.5));
        name = "new4649";
        eids.put(name,Entity.entityCount++);
        skins.put(name,new Skin[]{new Skin(new File(url+name+"-online.png")),new Skin(new File(url+name+"-offline.png"))});
        poss.put(name,new Vector3(129.5,56.5,188.5));
        name = "mfmfneko";
        eids.put(name,Entity.entityCount++);
        skins.put(name,new Skin[]{new Skin(new File(url+name+"-online.png")),new Skin(new File(url+name+"-offline.png"))});
        poss.put(name,new Vector3(125.5,54.5,186.5));
        name = "Rasuku59";
        eids.put(name,Entity.entityCount++);
        skins.put(name,new Skin[]{new Skin(new File(url+name+"-online.png")),new Skin(new File(url+name+"-offline.png"))});
        poss.put(name,new Vector3(125.5,55.5,187.5));
        name = "daicon1007";
        eids.put(name,Entity.entityCount++);
        skins.put(name,new Skin[]{new Skin(new File(url+name+"-online.png")),new Skin(new File(url+name+"-offline.png"))});
        poss.put(name,new Vector3(125.5,56.5,188.5));
        name = "bear";
        eids.put(name,Entity.entityCount++);
        skins.put(name,new Skin[]{new Skin(new File(url+name+"-online.png")),new Skin(new File(url+name+"-offline.png"))});
        poss.put(name,new Vector3(126.5,56,187.5));
    }

    public static void spawnStatue(Player player){
        UUID uuid;
        Vector3 pos;
        Skin skin;
        Player p;
        AddPlayerPacket pk = new AddPlayerPacket();
        for (Map.Entry<String,Long> e : eids.entrySet()){
            uuid = UUID.randomUUID();
            pos = poss.get(e.getKey());
            p = Server.getInstance().getPlayer(e.getKey());
            if(p instanceof Player && e.getKey().equals(p.getName())) skin = skins.get(e.getKey())[0];
            else skin = skins.get(e.getKey())[1];
            pk.entityUniqueId = e.getValue();
            pk.entityRuntimeId = e.getValue();
            pk.uuid = uuid;
            pk.username = e.getKey();
            pk.x = (float)pos.x;
            pk.y = (float)pos.y;
            pk.z = (float)pos.z;
            pk.yaw = 180;
            pk.pitch = 0;
            pk.metadata = new EntityMetadata()
                    .putString(Entity.DATA_NAMETAG, "")
                    .putFloat(Entity.DATA_SCALE,(float)0.8)
                    .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1);
            PlayerListPacket pkk = new PlayerListPacket();
            pkk.type = PlayerListPacket.TYPE_ADD;
            pkk.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(uuid, e.getValue(), e.getKey(),skin)};
            PlayerListPacket pkkk = new PlayerListPacket();
            pkkk.type = PlayerListPacket.TYPE_REMOVE;
            pkkk.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(uuid)};
            player.dataPacket(pk);
            player.dataPacket(pkk);
            player.dataPacket(pkkk);
        }
    }

    public static void updateStatue(Player player){
        if(!eids.containsKey(player.getName())) return;
        RemoveEntityPacket pkkkk = new RemoveEntityPacket();
        pkkkk.eid = eids.get(player.getName());
        UUID uuid;
        Vector3 pos;
        Skin skin;
        Player p;
        AddPlayerPacket pk = new AddPlayerPacket();
        uuid = UUID.randomUUID();
        pos = poss.get(player.getName());
        p = Server.getInstance().getPlayer(player.getName());
        if(p instanceof Player && player.getName().equals(p.getName())) skin = skins.get(player.getName())[0];
        else skin = skins.get(player.getName())[1];
        pk.entityUniqueId = eids.get(player.getName());
        pk.entityRuntimeId = eids.get(player.getName());
        pk.uuid = uuid;
        pk.username = player.getName();
        pk.x = (float)pos.x;
        pk.y = (float)pos.y;
        pk.z = (float)pos.z;
        pk.yaw = 180;
        pk.pitch = 0;
        pk.metadata = new EntityMetadata()
                .putString(Entity.DATA_NAMETAG, "")
                .putFloat(Entity.DATA_SCALE,(float)1)
                .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1);
        PlayerListPacket pkk = new PlayerListPacket();
        pkk.type = PlayerListPacket.TYPE_ADD;
        pkk.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(uuid, eids.get(player.getName()), player.getName(),skin)};
        PlayerListPacket pkkk = new PlayerListPacket();
        pkkk.type = PlayerListPacket.TYPE_REMOVE;
        pkkk.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(uuid)};
        Map<Long,Player> players = Server.getInstance().getLevelByName("lobby").getPlayers();
        for (Map.Entry<Long,Player> e : players.entrySet()){
            if(e.getValue() instanceof Player){
                e.getValue().dataPacket(pkkkk);
                e.getValue().dataPacket(pk);
                e.getValue().dataPacket(pkk);
                e.getValue().dataPacket(pkkk);
            }
        }
    }

    public static void despawnStatue(Player player){
        RemoveEntityPacket pk = new RemoveEntityPacket();
        for (Map.Entry<String,Long> e : eids.entrySet()){
            pk.eid = e.getValue();
            player.dataPacket(pk);
        }
    }

    public static File file;
    public static HashMap<String,Skin[]> skins = new HashMap<String,Skin[]>();
    public static HashMap<String,Long> eids = new HashMap<String,Long>();
    public static HashMap<String,Vector3> poss = new HashMap<String,Vector3>();

}