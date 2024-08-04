package sote.lobbyitem;

import cn.nukkit.Player;
import cn.nukkit.level.particle.FlameParticle;
import cn.nukkit.math.Vector3;

public class CrossParticleLobbyItem extends LobbyItem{

    public CrossParticleLobbyItem(){
    }

    @Override
    public void tick(Player player){
        if(count >= 5){
            this.crossParticle(player, 0.4);
            this.crossParticle(player, 0.8);
            this.crossParticle(player, 1.2);
            this.count = 0;
        }else{
            this.count++;
        }
    }

    public void crossParticle(Player player, double plusY){
        double scale = 1.2;

        double yaw = deg2rad(player.yaw);
        double pitch = deg2rad(player.pitch);
        double motionX = -Math.sin(yaw);
        double motionZ = Math.cos(yaw);
        Vector3 pos = new Vector3(player.x - (motionX * 0.5), player.y + plusY, player.z - (motionZ * 0.5));

        double yaw2 = yaw + Math.PI/2;
        double motionX2 = -Math.sin(yaw2);
        double motionZ2 = Math.cos(yaw2);
        Vector3 p1 = pos.add(new Vector3(scale * motionX2, scale, scale * motionZ2));

        double yaw3 = yaw - Math.PI/2;
        double motionX3 = -Math.sin(yaw3);
        double motionZ3 = Math.cos(yaw3);
        Vector3 p2 = pos.add(new Vector3(-scale * motionX3, -scale , -scale * motionZ3));

        for(double a = 0;a <= scale * 2;a += scale/10){
            Vector3 pos1 = p1.add(new Vector3(-a * motionX2, -a, -a * motionZ2));
            player.getLevel().addParticle(new FlameParticle(pos1));
        }

        for(double b = 0;b <= scale * 2;b += scale/10){
            Vector3 pos2 = p2.add(new Vector3(b * motionX3, b, b * motionZ3));
            player.getLevel().addParticle(new FlameParticle(pos2));
        }
    }

    public static double rad2deg(double radian) {
        return radian * (180f / Math.PI);
    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    @Override
    public void shot(Player player){
    }

    public int count = 0;
}
