package sote.lobbyitem;

import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.network.protocol.LevelEventPacket;

public class SmokeParticleLobbyItem extends LobbyItem{

    public SmokeParticleLobbyItem(){
    }

    @Override
    public void move(Player player){
        LevelEventPacket pk = new LevelEventPacket();
        pk.evid = LevelEventPacket.EVENT_ADD_PARTICLE_MASK | 8;
        pk.x = (float) player.x;
        pk.y = (float) (player.y+0.2);
        pk.z = (float) player.z;
        Map<Long,Player> players = player.getLevel().getPlayers();
        for (Map.Entry<Long,Player> e : players.entrySet()){
            e.getValue().dataPacket(pk);
            pk.y = (float) (player.y+0.1);
            e.getValue().dataPacket(pk);
            pk.y = (float) (player.y+0.3);
            e.getValue().dataPacket(pk);
            pk.y = (float) (player.y+0.4);
            e.getValue().dataPacket(pk);
        }
    }

    @Override
    public void shot(Player player){
    }
}