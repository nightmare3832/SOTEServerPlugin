package sote;

import java.io.File;
import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.level.Position;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.AddPlayerPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import sote.inventory.Inventorys;
import sote.inventory.skywars.SkywarsSoloLobbyInventory;

public class GameMob{

    public GameMob(){
        skywars = Entity.entityCount++;
        buildbattle = Entity.entityCount++;
        murder = Entity.entityCount++;
        skywarstext = Entity.entityCount++;
        buildbattletext = Entity.entityCount++;
        murdertext = Entity.entityCount++;
        arcadetext = Entity.entityCount++;
        classictext = Entity.entityCount++;
    }

    public static void spawnMob(Player player){
        AddEntityPacket pk = new AddEntityPacket();
        pk.entityUniqueId = skywars;
        pk.entityRuntimeId = skywars;
        pk.x = (float)136.5;
        pk.y = (float)49;
        pk.z = (float)73.5;
        pk.type = 47;
        pk.yaw = 90;
        pk.pitch = 0;
        int flags = 0;
        flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
        pk.metadata = new EntityMetadata()
                .putLong(Entity.DATA_FLAGS,flags)
                .putString(Entity.DATA_NAMETAG, "§bSkyWars")
                .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1);
        player.dataPacket(pk);
        pk.entityUniqueId = buildbattle;
        pk.entityRuntimeId = buildbattle;
        pk.x = (float)130.5;
        pk.y = (float)52;
        pk.z = (float)180.5;
        pk.yaw = 0;
        pk.pitch = 0;
        pk.type = 46;
        flags = 0;
        flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
        pk.metadata = new EntityMetadata()
                .putLong(Entity.DATA_FLAGS,flags)
                .putString(Entity.DATA_NAMETAG, "§6BuildBattle")
                .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1);
        player.dataPacket(pk);
        pk.entityUniqueId = murder;
        pk.entityRuntimeId = murder;
        pk.x = (float)1360.5;
        pk.y = (float)49;
        pk.z = (float)73.5;
        pk.yaw = 90;
        pk.pitch = 0;
        pk.type = 46;
        flags = 0;
        flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
        pk.metadata = new EntityMetadata()
                .putLong(Entity.DATA_FLAGS,flags)
                .putString(Entity.DATA_NAMETAG, "§cMurder")
                .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1);
        player.dataPacket(pk);
        AddPlayerPacket pkk = new AddPlayerPacket();
        pkk.entityUniqueId = arcadetext;
        pkk.entityRuntimeId = arcadetext;
        pkk.x = (float)132.5;
        pkk.y = (float)54;
        pkk.z = (float)184.5;
        pkk.yaw = 0;
        pkk.pitch = 0;
        pkk.username = "";
        pkk.uuid = UUID.randomUUID();
        flags = 0;
        flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
        flags |= 1 << Entity.DATA_FLAG_INVISIBLE;
        pkk.metadata = new EntityMetadata()
                .putLong(Entity.DATA_FLAGS,flags)
                .putString(Entity.DATA_NAMETAG,Main.getMessage(player,"arcadetext"))
                .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1);
        player.dataPacket(pkk);
        pkk.entityUniqueId = classictext;
        pkk.entityRuntimeId = classictext;
        pkk.x = (float)122.5;
        pkk.y = (float)54;
        pkk.z = (float)184.5;
        pkk.yaw = 0;
        pkk.pitch = 0;
        pkk.username = "";
        pkk.uuid = UUID.randomUUID();
        flags = 0;
        flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
        flags |= 1 << Entity.DATA_FLAG_INVISIBLE;
        pkk.metadata = new EntityMetadata()
                .putLong(Entity.DATA_FLAGS,flags)
                .putString(Entity.DATA_NAMETAG,Main.getMessage(player,"classictext"))
                .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1);
        player.dataPacket(pkk);
    }

    public static void despawnMob(Player player){
        RemoveEntityPacket pk = new RemoveEntityPacket();
        pk.eid = skywars;
        player.dataPacket(pk);
        pk.eid = skywarstext;
        player.dataPacket(pk);
        pk.eid = buildbattle;
        player.dataPacket(pk);
        pk.eid = buildbattletext;
        player.dataPacket(pk);
        pk.eid = murder;
        player.dataPacket(pk);
        pk.eid = murdertext;
        player.dataPacket(pk);
        pk.eid = arcadetext;
        player.dataPacket(pk);
        pk.eid = classictext;
        player.dataPacket(pk);
    }

    public static Boolean touch(Player player,Long eid){
        if(eid.equals(skywars)){
            Inventorys.setData(player, new SkywarsSoloLobbyInventory());
            player.teleport(new Position(-32,57,13,Server.getInstance().getLevelByName("skywars")));
            return true;
        }/*else if(eid.equals(buildbattle)){
            Inventorys.setData(player, new BuildbattleLobbyInventory());
            player.teleport(new Position(88.5,16,85.5,Server.getInstance().getLevelByName("buildbattle")));
            return true;
        }else if(eid.equals(murder)){
            Inventorys.setData(player, new MurderLobbyInventory());
            player.teleport(new Position(-246,6,-234,Server.getInstance().getLevelByName("murder")));
            return true;
        }*/
        return false;
    }

    public static File file;
    public static Long skywars;
    public static Long skywarstext;
    public static Long buildbattle;
    public static Long buildbattletext;
    public static Long murder;
    public static Long murdertext;
    public static Long arcadetext;
    public static Long classictext;

}