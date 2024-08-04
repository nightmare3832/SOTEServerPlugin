package sote.lobbyitem;

import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.network.protocol.LevelEventPacket;

public class MusicParticleLobbyItem extends LobbyItem{

    public MusicParticleLobbyItem(){
    }

    @Override
    public void move(Player player){
        LevelEventPacket pk = new LevelEventPacket();
        pk.evid = LevelEventPacket.EVENT_ADD_PARTICLE_MASK | 34;
        pk.x = (float) player.x;
        pk.y = (float) (player.y+0.2);
        pk.z = (float) player.z;
        Map<Long,Player> players = player.getLevel().getPlayers();
        for (Map.Entry<Long,Player> e : players.entrySet()){
            e.getValue().dataPacket(pk);
        }
    }

    @Override
    public void shot(Player player){
    }
}