package sote.lobbyitem;

import cn.nukkit.Player;
import cn.nukkit.level.particle.HeartParticle;
import cn.nukkit.math.Vector3;

public class HeartRingParticleLobbyItem extends LobbyItem{

    public HeartRingParticleLobbyItem(){
    }

    @Override
    public void tick(Player player){
        //if(count >= 1){
            this.ringParticle(player, 2.2);
            if(this.angle >= 360){
                this.angle = 0;
            }else{
                this.angle += 18;
            }
        //    this.count = 0;
        //}else{
        //    this.count++;
        //}
    }

    public void ringParticle(Player player, double plusY){
        double yaw = deg2rad(this.angle);
        double motionX = -Math.sin(yaw);
        double motionZ = Math.cos(yaw);
        double scale = 0.6;
        Vector3 pos1 = new Vector3(player.x + (motionX * scale), player.y + plusY, player.z + (motionZ * scale));
        player.getLevel().addParticle(new HeartParticle(pos1));
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

    //public int count = 0;
    public int angle = 0;
}
