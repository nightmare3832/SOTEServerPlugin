package sote.lobbyitem;

import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.network.protocol.LevelEventPacket;

public class SoteParticleLobbyItem extends LobbyItem{

    public SoteParticleLobbyItem(){
    }

    @Override
    public void move(Player player){
        LevelEventPacket pk = new LevelEventPacket();
        pk.evid = LevelEventPacket.EVENT_ADD_PARTICLE_MASK | 35;
        pk.x = (float) player.x;
        pk.y = (float) (player.y+0.2);
        pk.z = (float) player.z;
        Map<Long,Player> players = player.getLevel().getPlayers();
        for (Map.Entry<Long,Player> e : players.entrySet()){
            pk.data = ((0x01 & 0xff) << 24) | ((255 & 0xff) << 16) | ((94 & 0xff) << 8) | (25 & 0xff);
            e.getValue().dataPacket(pk);
            pk.data = ((0x01 & 0xff) << 24) | ((255 & 0xff) << 16) | ((94 & 0xff) << 8) | (25 & 0xff);
            e.getValue().dataPacket(pk);
            pk.data = ((0x01 & 0xff) << 24) | ((255 & 0xff) << 16) | ((255 & 0xff) << 8) | (255 & 0xff);
            e.getValue().dataPacket(pk);
            pk.data = ((0x01 & 0xff) << 24) | ((255 & 0xff) << 16) | ((255 & 0xff) << 8) | (255 & 0xff);
            e.getValue().dataPacket(pk);
        }
    }

    @Override
    public void shot(Player player){
    }
}