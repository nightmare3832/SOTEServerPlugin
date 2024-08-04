package sote.particle;

import cn.nukkit.Player;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.LevelEventPacket;

public class Firework{

    public Firework() {
    }

    public static void start(Vector3 pos,Player player){
        LevelEventPacket pk = new LevelEventPacket();
        //pk.evid = LevelEventPacket.EVENT_PARTICLE_SPLASH;
        pk.evid = LevelEventPacket.EVENT_PARTICLE_SPLASH;
        pk.x = (float) pos.x;
        pk.y = (float) pos.y+1;
        pk.z = (float) pos.z;
        for(int i = 0; i <= 5; i++){
            pk.data = ((0x01 & 0xff) << 24) | (((int)(Math.random() * 255) & 0xff) << 16) | (((int)(Math.random() * 255) & 0xff) << 8) | ((int)(Math.random() * 255) & 0xff);
            pk.x = (float)((Math.random() * 4) + pos.x - 2);
            pk.y = (float)((Math.random() * 4) + pos.y);
            pk.z = (float)((Math.random() * 4) + pos.z - 2);
            player.dataPacket(pk);
        }
    }
}
