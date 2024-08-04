package sote.gatya;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.particle.Particle;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.scheduler.Task;

public class Gatya{

    public Gatya(){
    }

    public static void start(Player player,Vector3 pos){
        star(player,new Vector3(pos.x+0.5,pos.y-1,pos.z+0.5),2,0);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackLine(player,new Vector3(pos.x+0.5,pos.y+0.5,pos.z+0.5),0),0);
    }

    public static void star(Player player,Vector3 pos,double rabius,int r){
        double d = 72*Math.PI/180;
        Vector3 posA = new Vector3(pos.x,pos.y,pos.z - rabius);
        Vector3 posB = new Vector3(pos.x + rabius*Math.sin(d),pos.y,pos.z - rabius*Math.cos(d));
        Vector3 posC = new Vector3(pos.x + rabius*Math.sin(d*2),pos.y,pos.z - rabius*Math.cos(d*2));
        Vector3 posD = new Vector3(pos.x - rabius*Math.sin(d*2),pos.y,pos.z - rabius*Math.cos(d*2));
        Vector3 posE = new Vector3(pos.x - rabius*Math.sin(d),pos.y,pos.z - rabius*Math.cos(d));
        toPoint(player,posA,posD,(int)(rabius*19));
        toPoint(player,posD,posB,(int)(rabius*19));
        toPoint(player,posB,posE,(int)(rabius*19));
        toPoint(player,posE,posC,(int)(rabius*19));
        toPoint(player,posC,posA,(int)(rabius*19));
        pos.y += 0.0025;
        rabius -= 0.1;
        if(rabius <= 0) return;
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackStar(player,pos,rabius,r),4);
    }

    public static void riseLine(Player player,Vector3 pos,int r){
        Vector3 line = new Vector3(pos.x,pos.y+r*0.05,pos.z);
        Vector3 line2 = new Vector3(pos.x,pos.y+r*0.05,pos.z);
        Vector3 line3 = new Vector3(pos.x,pos.y+r*0.05,pos.z);
        Vector3 line4 = new Vector3(pos.x,pos.y+r*0.05,pos.z);
        int rr = (r*4)%360;
        int rrr = rr+90;
        if(rrr > 360) rrr -= 360;
        int rrrr = rrr+90;
        if(rrrr > 360) rrrr -= 360;
        int rrrrr = rrrr+90;
        if(rrrrr > 360) rrrrr -= 360;
        line.x = 0.5*-Math.sin(rr) + line.x;
        line.z = 0.5*Math.cos(rr) + line.z;
        line2.x = 0.5*-Math.sin(rrr) + line2.x;
        line2.z = 0.5*Math.cos(rrr) + line2.z;
        line3.x = 0.5*-Math.sin(rrrr) + line3.x;
        line3.z = 0.5*Math.cos(rrrr) + line3.z;
        line4.x = 0.5*-Math.sin(rrrrr) + line4.x;
        line4.z = 0.5*Math.cos(rrrrr) + line4.z;
        //System.out.println(line.x+":"+line.y+":"+line2.x+":"+line2.y+":"+line3.x+":"+line3.y+":"+line4.x+":"+line4.y);
        LevelEventPacket pk = new LevelEventPacket();
        pk.evid = LevelEventPacket.EVENT_ADD_PARTICLE_MASK | Particle.TYPE_BUBBLE;
        pk.x = (float) line.x;
        pk.y = (float) line.y;
        pk.z = (float) line.z;
        pk.data = ((0x01 & 0xff) << 24) | ((255 & 0xff) << 16) | ((255 & 0xff) << 8) | (255 & 0xff);
        player.dataPacket(pk);
        pk.x = (float) line2.x;
        pk.y = (float) line2.y;
        pk.z = (float) line2.z;
        pk.data = ((0x01 & 0xff) << 24) | ((0 & 0xff) << 16) | ((0 & 0xff) << 8) | (255 & 0xff);
        player.dataPacket(pk);
        pk.x = (float) line3.x;
        pk.y = (float) line3.y;
        pk.z = (float) line3.z;
        pk.data = ((0x01 & 0xff) << 24) | ((255 & 0xff) << 16) | ((255 & 0xff) << 8) | (0 & 0xff);
        player.dataPacket(pk);
        pk.x = (float) line4.x;
        pk.y = (float) line4.y;
        pk.z = (float) line4.z;
        pk.data = ((0x01 & 0xff) << 24) | ((25 & 0xff) << 16) | ((255 & 0xff) << 8) | (0 & 0xff);
        player.dataPacket(pk);
        r++;
        if(r >= 50) return;
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackLine(player,pos,r),1);
    }

    public static double deg2rad(double deg) {
        //return (deg * Math.PI / 180.0);
        return deg;
    }

    public static void toPoint(Player player,Vector3 pos,Vector3 pos2,int c){
        double mx = pos.x - pos2.x;
        double mz = pos.z - pos2.z;
        double yaw = getYaw(mx, mz);
        double MX = -Math.sin(yaw / 180 * Math.PI)/10;
        double MZ = Math.cos(yaw / 180 * Math.PI)/10;
        Vector3 pos3 = pos.clone();
        for(int i=0;i<=c;i++){
            LevelEventPacket pk = new LevelEventPacket();
            pk.evid = LevelEventPacket.EVENT_ADD_PARTICLE_MASK | 35;
            pk.x = (float) pos3.x;
            pk.y = (float) pos3.y;
            pk.z = (float) pos3.z;
            int rand = (int)(Math.random() * 4)+1;
            if(rand == 1) pk.data = ((0x01 & 0xff) << 24) | ((255 & 0xff) << 16) | ((255 & 0xff) << 8) | (255 & 0xff);
            if(rand == 2) pk.data = ((0x01 & 0xff) << 24) | ((0 & 0xff) << 16) | ((0 & 0xff) << 8) | (255 & 0xff);
            if(rand == 3) pk.data = ((0x01 & 0xff) << 24) | ((255 & 0xff) << 16) | ((255 & 0xff) << 8) | (0 & 0xff);
            if(rand == 4) pk.data = ((0x01 & 0xff) << 24) | ((25 & 0xff) << 16) | ((255 & 0xff) << 8) | (0 & 0xff);
            player.dataPacket(pk);
            pos3.x -= MX;
            pos3.z -= MZ;
        }
    }

    public static double getYaw(double mx,double mz) {
        double yaw = 0;
        if (mz == 0) {
            if (mx < 0) {
                yaw = -90;
            }else {
                yaw = 90;
            }
        }else {
            if (mx >= 0 && mz > 0) {
                double atan = Math.atan(mx/mz);
                yaw = rad2deg(atan);
            }else if (mx >= 0 && mz < 0) {
                double atan = Math.atan(mx/Math.abs(mz));
                yaw = 180 - rad2deg(atan);
            }else if (mx < 0 && mz < 0) {
                double atan = Math.atan(mx/mz);
                yaw = -(180 - rad2deg(atan));
            }else if (mx < 0 && mz > 0) {
                double atan = Math.atan(Math.abs(mx)/mz);
                yaw = -(rad2deg(atan));
            }
        }

        yaw = - yaw;
        return yaw;
    }

    public static double rad2deg(double radian) {
        return radian * (180f / Math.PI);
    }
}
class CallbackStar extends Task{

    public Player player;
    public Vector3 pos;
    public double rabius;
    public int r;

    public CallbackStar(Player p,Vector3 po,double r,int rr){
        player = p;
        pos = po;
        rabius = r;
        this.r = rr;
    }

    public void onRun(int d){
        go();
    }
    
    public void go(){
        Gatya.star(player,pos,rabius,r);
    }
}
class CallbackLine extends Task{

    public Player player;
    public Vector3 pos;
    public int r;

    public CallbackLine(Player p,Vector3 po,int rr){
        player = p;
        pos = po;
        this.r = rr;
    }

    public void onRun(int d){
        go();
    }
    
    public void go(){
        Gatya.riseLine(player,pos,r);
    }
}