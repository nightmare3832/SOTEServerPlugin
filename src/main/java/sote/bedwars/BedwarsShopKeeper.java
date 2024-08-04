package sote.bedwars;

import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.passive.EntityVillager;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.AddEntityPacket;
import sote.Game;
import sote.GameProvider;

public class BedwarsShopKeeper{

    public BedwarsShopKeeper(){
    }

    public static void spawnShopKeeper(int number){
        Game game = GameProvider.Games.get(number).get(GameProvider.BEDWARS);
        if(!(game instanceof Bedwars)) return;
        Bedwars bedwars = (Bedwars) game;
        int c = 8;
        if(bedwars.getMode() == Bedwars.MODE_THREES || bedwars.getMode() == Bedwars.MODE_FOURS) c = 4;
        for(int t = 0; t < c;t++){
            Vector3[] pos = bedwars.getTeamDataByNumber(t);
            AddEntityPacket pk = new AddEntityPacket();
            pk.entityRuntimeId = bedwars.shopKeeperEid[t];
            pk.entityUniqueId = bedwars.shopKeeperEid[t];
            pk.type = EntityVillager.NETWORK_ID;
            int itemShopIndex = 5;
            if(c == 4) itemShopIndex = 7;
            pk.x = (float)pos[itemShopIndex].x;
            pk.y = (float)pos[itemShopIndex].y;
            pk.z = (float)pos[itemShopIndex].z;
            pk.yaw = 0;
            pk.pitch = 0;
            int flags = 0;
            flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;
            flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
            flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
            flags |= 1 << Entity.DATA_FLAG_SHOWBASE;
            pk.metadata = new EntityMetadata()
                    .putLong(Entity.DATA_FLAGS, flags)
                    .putString(Entity.DATA_NAMETAG, "§e§lITEM SHOP");
            AddEntityPacket pk2 = new AddEntityPacket();
            pk2.entityRuntimeId = bedwars.shopKeeperEid[t+8];
            pk2.entityUniqueId = bedwars.shopKeeperEid[t+8];
            pk2.type = EntityVillager.NETWORK_ID;
            pk2.x = (float)pos[itemShopIndex + 1].x;
            pk2.y = (float)pos[itemShopIndex + 1].y;
            pk2.z = (float)pos[itemShopIndex + 1].z;
            pk2.yaw = 0;
            pk2.pitch = 0;
            String n = "§e§lTEAM\nUPGRADES";
            if(bedwars.getMode() == Bedwars.MODE_SOLO) n = "§e§lSOLO\nUPGRADES";
            flags = 0;
            flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;
            flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
            flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
            flags |= 1 << Entity.DATA_FLAG_SHOWBASE;
            pk2.metadata = new EntityMetadata()
                    .putLong(Entity.DATA_FLAGS, flags)
                    .putString(Entity.DATA_NAMETAG, n);
            for(Map.Entry<String, Player> e : bedwars.Players.entrySet()){
                e.getValue().dataPacket(pk);
                e.getValue().dataPacket(pk2);
            }
        }
    }
}