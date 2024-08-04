package sote;

import java.util.HashMap;
import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.AddPlayerPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;

public class PacketManager{

    public PacketManager(){
        UUID uuid = UUID.randomUUID();
        AddPlayerPacket pk = new AddPlayerPacket();
        pk.entityUniqueId = Main.warn;
        pk.entityRuntimeId = Main.warn;
        pk.username = "";
        pk.uuid = uuid;
        pk.x = (float)127.5;
        pk.y = (float)49;
        pk.z = (float)130.5;
        pk.yaw = 0;
        pk.pitch = 0;
        int flags = 0;
        flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
        flags |= 1 << Entity.DATA_FLAG_INVISIBLE;
        flags |= 1 << 33;
        pk.metadata = new EntityMetadata()
                .putLong(Entity.DATA_FLAGS,flags)
                .putString(Entity.DATA_NAMETAG,Main.getMessage("jp","warntext"))
                .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1);
        pks.put("AddLobbyWarn",pk);
        uuid = UUID.randomUUID();
        AddPlayerPacket pk1 = new AddPlayerPacket();
        pk1.entityUniqueId = Main.MurderRanking;
        pk1.entityRuntimeId = Main.MurderRanking;
        pk1.username = "";
        pk1.uuid = uuid;
        pk1.x = (float)-242.5;
        pk1.y = (float)9;
        pk1.z = (float)-265.5;
        pk1.yaw = 0;
        pk1.pitch = 0;
        flags = 0;
        flags = 0;
        flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
        flags |= 1 << Entity.DATA_FLAG_INVISIBLE;
        pk1.metadata = new EntityMetadata()
                .putLong(Entity.DATA_FLAGS,flags)
                .putString(Entity.DATA_NAMETAG,"")
                .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1);
        pks.put("AddMurderRanking",pk1);
        AddEntityPacket pk2 = new AddEntityPacket();
        pk2.entityUniqueId = Main.wolf;
        pk2.entityRuntimeId = Main.wolf;
        pk2.type = 14;
        pk2.x = (float)119.5;
        pk2.y = (float)60;
        pk2.z = (float)130.5;
        pk2.yaw = 90;
        pk2.pitch = 0;
        flags = 0;
        flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
        flags |= 1 << Entity.DATA_FLAG_TAMED;
        pk2.metadata = new EntityMetadata()
                .putLong(Entity.DATA_FLAGS,flags)
                .putString(Entity.DATA_NAMETAG,"")
                .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1)
                .putLong(Entity.DATA_OWNER_EID,(long)0)
                .putByte(Entity.DATA_COLOUR,(int)(Math.random() * 15));
        pks.put("AddHomeWolf",pk2);
        AddEntityPacket pk3 = new AddEntityPacket();
        pk3.entityUniqueId = Main.knife;
        pk3.entityRuntimeId = Main.knife;
        pk3.type = 45;
        pk3.x = (float)125;
        pk3.y = (float)11.5;
        pk3.z = (float)144;
        pk3.yaw = 90;
        pk3.pitch = 90;
        flags = 0;
        flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
        flags |= 1 << Entity.DATA_FLAG_INVISIBLE;
        pk3.metadata = new EntityMetadata()
                .putLong(Entity.DATA_FLAGS,flags)
                .putString(Entity.DATA_NAMETAG,"Dinnerbone")
                .putFloat(Entity.DATA_SCALE, (float)1.5)
                .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1);
        pks.put("AddMurderKnife",pk3);
        MobEquipmentPacket pkk3 = new MobEquipmentPacket();
        pkk3.eid = Main.knife;
        pkk3.item = Item.get(Item.WOODEN_SWORD);
        pks.put("SetMurderKnife",pkk3);
        RemoveEntityPacket pk4 = new RemoveEntityPacket();
        pk4.eid = Main.warn;
        pks.put("RemoveLobbyWarn",pk4);
        RemoveEntityPacket pk5 = new RemoveEntityPacket();
        pk5.eid = Main.MurderRanking;
        pks.put("RemoveMurderRanking",pk5);
        RemoveEntityPacket pk6 = new RemoveEntityPacket();
        pk6.eid = Main.wolf;
        pks.put("RemoveHomeWolf",pk6);
        RemoveEntityPacket pk7 = new RemoveEntityPacket();
        pk7.eid = Main.knife;
        pks.put("RemoveMurderKnife",pk7);
        uuid = UUID.randomUUID();
        AddPlayerPacket pk8 = new AddPlayerPacket();
        pk8.entityUniqueId = Main.MurderRule;
        pk8.entityRuntimeId = Main.MurderRule;
        pk8.username = "";
        pk8.uuid = uuid;
        pk8.x = (float)-247.5;
        pk8.y = (float)7;
        pk8.z = (float)-244.5;
        pk8.yaw = 0;
        pk8.pitch = 0;
        flags = 0;
        flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
        flags |= 1 << Entity.DATA_FLAG_INVISIBLE;
        flags |= 1 << 33;
        flags |= 1 << 34;
        flags |= 1 << 35;
        pk8.metadata = new EntityMetadata()
                .putLong(Entity.DATA_FLAGS,flags)
                .putString(Entity.DATA_NAMETAG,Main.getMessage("jp","murder.ruletext"))
                .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1)
                .putLong(68, (long)0);
        pks.put("AddMurderRule",pk8);
        uuid = UUID.randomUUID();
        AddPlayerPacket pk9 = new AddPlayerPacket();
        pk9.entityUniqueId = Main.MurderRule2;
        pk9.entityRuntimeId = Main.MurderRule2;
        pk9.username = "";
        pk9.uuid = uuid;
        pk9.x = (float)127.5;
        pk9.y = (float)15;
        pk9.z = (float)141.5;
        pk9.yaw = 0;
        pk9.pitch = 0;
        flags = 0;
        flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
        flags |= 1 << Entity.DATA_FLAG_INVISIBLE;
        pk9.metadata = new EntityMetadata()
                .putLong(Entity.DATA_FLAGS,flags)
                .putString(Entity.DATA_NAMETAG,Main.getMessage("jp","murder.warntext"))
                .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1);
        pks.put("AddMurderRule2",pk9);
        RemoveEntityPacket pk10 = new RemoveEntityPacket();
        pk10.eid = Main.MurderRule;
        pks.put("RemoveMurderRule",pk10);
        RemoveEntityPacket pk11 = new RemoveEntityPacket();
        pk11.eid = Main.MurderRule2;
        pks.put("RemoveMurderRule2",pk11);
        AddEntityPacket pk12 = new AddEntityPacket();
        pk12.entityUniqueId = Main.MiniwallsUpgrader;
        pk12.entityRuntimeId = Main.MiniwallsUpgrader;
        pk12.type = 45;
        pk12.x = (float)9.5;
        pk12.y = (float)4;
        pk12.z = (float)-11.5;
        pk12.yaw = 180;
        pk12.pitch = 0;
        flags = 0;
        flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
        pk12.metadata = new EntityMetadata()
                .putLong(Entity.DATA_FLAGS,flags)
                .putString(Entity.DATA_NAMETAG,"§l§dUPGRADE")
                .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1);
        pks.put("AddMiniwallsUpgrader",pk12);
        RemoveEntityPacket pk13 = new RemoveEntityPacket();
        pk13.eid = Main.MiniwallsUpgrader;
        pks.put("RemoveMiniwallsUpgrader",pk13);
        AddEntityPacket pk14 = new AddEntityPacket();
        pk14.entityUniqueId = Main.knife2;
        pk14.entityRuntimeId = Main.knife2;
        pk14.type = 45;
        pk14.x = (float)-250.5;
        pk14.y = (float)16;
        pk14.z = (float)-251.5;
        pk14.yaw = 90;
        pk14.pitch = 90;
        flags = 0;
        flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
        pk14.metadata = new EntityMetadata()
                .putLong(Entity.DATA_FLAGS,flags)
                .putString(Entity.DATA_NAMETAG,"Dinnerbone")
                .putFloat(Entity.DATA_SCALE, (float)3)
                .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1);
        pks.put("AddMurderKnife2",pk14);
        RemoveEntityPacket pk15 = new RemoveEntityPacket();
        pk15.eid = Main.knife2;
        pks.put("RemoveMurderKnife2",pk15);
    }

    public static void sendPacket(Player player,String str){
        player.dataPacket(pks.get(str));
    }

    public static void sendPacket(Player player,String str,EntityMetadata meta){
        DataPacket packet = pks.get(str);
        if(packet instanceof AddPlayerPacket){
            AddPlayerPacket pk = (AddPlayerPacket) packet;
            pk.metadata = meta;
            player.dataPacket(pk);
        }
        if(packet instanceof AddEntityPacket){
            AddEntityPacket pk = (AddEntityPacket) packet;
            pk.metadata = meta;
            player.dataPacket(pk);
        }
    }

    public static HashMap<String,DataPacket> pks = new HashMap<String,DataPacket>();

}