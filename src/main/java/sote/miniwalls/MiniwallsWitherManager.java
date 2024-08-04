package sote.miniwalls;

import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.network.protocol.MoveEntityPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.network.protocol.SetEntityDataPacket;
import cn.nukkit.scheduler.Task;
import sote.Main;
import sote.event.Event_EntityDamageEvent;

public class MiniwallsWitherManager{

    public final int SKULL_DAMAGE = 5;
    public final double EYE_HEIGHT = 1;
    public final double FIND_AREA = 8;
    public final float SCALE = (float)0.5;

    public final int MAX_HP = 500;

    public MiniwallsWitherManager(int teamtype,Vector3 position,Miniwalls owner){
        this.TeamType = teamtype;
        this.HitPoint = MAX_HP;
        this.Position = position;
        this.owner = owner;
        this.EntityId = Entity.entityCount++;
    }

    public void Start(){
        this.running = true;
        this.spawn();
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackTick(this),1);
    }

    public void Stop(){
        this.running = false;
        this.despawn();
        this.HitPoint = 0;
    }

    public void Damage(int damage, Player player){
        if(isDeathed) return;
        int d = this.HitPoint - damage;
        if(d > 0){
            this.HitPoint -= damage;
            EntityEventPacket pk = new EntityEventPacket();
            pk.eid = this.EntityId;
            pk.event = EntityEventPacket.HURT_ANIMATION;
            for (Map.Entry<String,Player> e : this.owner.AllPlayers.entrySet()){
                e.getValue().dataPacket(pk);
            }
        }else{
            this.HitPoint = 0;
            EntityEventPacket pk = new EntityEventPacket();
            pk.eid = this.EntityId;
            pk.event = EntityEventPacket.DEATH_ANIMATION;
            for (Map.Entry<String,Player> e : this.owner.AllPlayers.entrySet()){
                e.getValue().dataPacket(pk);
            }
            this.owner.WitherDeath(this.TeamType, player);
            if(this.running) Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackDeath(this),20);
            this.isDeathed = true;
        }
        updateNameTag();
    }

    public void spawn(){
        AddEntityPacket pk = new AddEntityPacket();
        pk.entityRuntimeId =this.EntityId;
        pk.entityUniqueId = this.EntityId;
        pk.type = 52;
        pk.x = (float)this.Position.x;
        pk.y = (float)this.Position.y;
        pk.z = (float)this.Position.z;
        pk.yaw = (float)this.yaw;
        pk.pitch = (float)this.pitch;
        int flags = 0;
        flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
        pk.metadata = new EntityMetadata()
                .putFloat(Entity.DATA_SCALE, SCALE)
                .putLong(Entity.DATA_FLAGS, flags)
                .putString(Entity.DATA_NAMETAG, this.getNameTag())
                .putFloat(Entity.DATA_BOUNDING_BOX_HEIGHT - 1, 2)
                .putFloat(Entity.DATA_BOUNDING_BOX_WIDTH - 1, 2)
                .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1);
        for (Map.Entry<String,Player> e : this.owner.AllPlayers.entrySet()){
            e.getValue().dataPacket(pk);
        }
    }

    public void Death(){
        this.despawn();
    }

    public void despawn(){
        RemoveEntityPacket pk = new RemoveEntityPacket();
        pk.eid = this.EntityId;
        for (Map.Entry<String,Player> e : this.owner.AllPlayers.entrySet()){
            e.getValue().dataPacket(pk);
        }
    }

    public void despawn(Player player){
        RemoveEntityPacket pk = new RemoveEntityPacket();
        pk.eid = this.EntityId;
        player.dataPacket(pk);
    }

    public void updateNameTag(){
        SetEntityDataPacket pk = new SetEntityDataPacket();
        pk.eid = this.EntityId;
        int flags = 0;
        flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
        pk.metadata = new EntityMetadata()
                .putFloat(Entity.DATA_SCALE, SCALE)
                .putLong(Entity.DATA_FLAGS, flags)
                .putString(Entity.DATA_NAMETAG, this.getNameTag())
                .putFloat(Entity.DATA_BOUNDING_BOX_HEIGHT - 1, 2)
                .putFloat(Entity.DATA_BOUNDING_BOX_WIDTH - 1, 2)
                .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1);
        for (Map.Entry<String,Player> e : this.owner.AllPlayers.entrySet()){
            e.getValue().dataPacket(pk);
        }
    }

    public String getNameTag(){
        String tag = "";
        if(this.TeamType == 0) tag = "§cRed Mini Wither";
        if(this.TeamType == 1) tag = "§bBlue Mini Wither";
        if(this.TeamType == 2) tag = "§aGreen Mini Wither";
        if(this.TeamType == 3) tag = "§eYellow Mini Wither";
        tag += "\n";
        int percent = 0;
        int block = 0;
        percent = (int)(this.HitPoint * 100 /MAX_HP);
        block = (int)(Math.ceil(percent / 10));
        int count = 1;
        for(int i = 0;i < 10;i++){
            if(block <= count) tag += "■";
            else tag += "□";
            count++;
        }
        tag += " " + percent + "%";
        if(percent <= 10 && !this.isLowHP){
            this.isLowHP = true;
            this.owner.WitherLowHP(this.TeamType);
        }
        return tag;
    }

    public void sendGauge(){
        String tag = "";
        if(this.TeamType == 0) tag = "§cRed Mini Wither";
        if(this.TeamType == 1) tag = "§bBlue Mini Wither";
        if(this.TeamType == 2) tag = "§aGreen Mini Wither";
        if(this.TeamType == 3) tag = "§eYellow Mini Wither";
        for (Map.Entry<String,Player> e : this.owner.AllPlayers.entrySet()){
            Main.setBossGaugeName(e.getValue(), tag);
            Main.setBossGaugeValue(e.getValue(), this.HitPoint, MAX_HP);
            Main.sendBossGaugeValue(e.getValue());
        }
    }

    public void Attack(Player player, int damage){
        if(this.owner.team.get(player).equals(this.TeamType)) return;
        if(this.Position.distance(player) >= 8) return;
        EntityDamageEvent entityDamageEvent = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.FALL, damage);
        player.attack(entityDamageEvent);
        Event_EntityDamageEvent.notPlayer.put(player, true);
        String tag = "";
        if(this.TeamType == 0) tag = "§cRed Mini Wither";
        if(this.TeamType == 1) tag = "§bBlue Mini Wither";
        if(this.TeamType == 2) tag = "§aGreen Mini Wither";
        if(this.TeamType == 3) tag = "§eYellow Mini Wither";
        Event_EntityDamageEvent.lastName.put(player, tag);
        double pitch = ((this.pitch + 90) * Math.PI) / 180;
        double yaw = ((this.yaw + 90) * Math.PI) / 180;
        double x = Math.sin(pitch) * Math.cos(yaw);
        double z = Math.sin(pitch) * Math.sin(yaw);
        double y = Math.cos(pitch);
        player.setMotion(new Vector3(x * 2, y * 2, z * 2).normalize());
        if(player.getName().equals("nightmare3832")) player.sendMessage(this.TeamType+"から");
    }

    public void Launch(){
        if(!Server.getInstance().isLevelLoaded("miniwalls"+this.owner.number)) return;
        double motionX = -Math.sin(this.yaw / 180 * Math.PI) * Math.cos(this.pitch / 180 * Math.PI);
        double motionY = -Math.sin(this.pitch / 180 * Math.PI);
        double motionZ = Math.cos(this.yaw / 180 * Math.PI) * Math.cos(this.pitch / 180 * Math.PI);
        Position pos = new Position(this.Position.x, this.Position.y + this.EYE_HEIGHT, this.Position.z);
        AddEntityPacket pk = new AddEntityPacket();
        Long eid = Entity.entityCount++;
        pk.entityUniqueId = eid;
        pk.entityRuntimeId = eid;
        pk.type = 89;
        pk.x = (float) pos.x;
        pk.y = (float) pos.y;
        pk.z = (float) pos.z;
        pk.metadata = new EntityMetadata()
                .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1);
        for (Map.Entry<String,Player> e : this.owner.AllPlayers.entrySet()){
            e.getValue().dataPacket(pk);
        }
        Boolean isExploaded = false;
        if(!Server.getInstance().getLevelByName("miniwalls"+this.owner.number).getBlock(pos).isTransparent()){
            isExploaded = true;
        }
        if(!isExploaded){
            for (Map.Entry<String,Player> e : this.owner.Players.entrySet()){
                if(!this.owner.team.get(e.getValue()).equals(this.TeamType) && e.getValue().getGamemode() != 3){
                    if(e.getValue().add(0,1,0).distance(pos) <= 1){
                        Explode(pos);
                        Attack(e.getValue(), SKULL_DAMAGE);
                        isExploaded = true;
                    }
                }
            }
        }
        if(isExploaded){
            RemoveEntityPacket pk2 = new RemoveEntityPacket();
            pk2.eid = eid;
            for (Map.Entry<String,Player> e : this.owner.AllPlayers.entrySet()){
                e.getValue().dataPacket(pk2);
            }
            return;
        }
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackLaunch(this, motionX, motionY, motionZ, pos, eid, 0),1);
    }

    public void Launch2(double motionX, double motionY, double motionZ, Vector3 pos,long eid, int count){
        pos.x += motionX;
        pos.y += motionY;
        pos.z += motionZ;
        MoveEntityPacket pk = new MoveEntityPacket();
        pk.eid = eid;
        pk.x = (float) pos.x;
        pk.y = (float) pos.y;
        pk.z = (float) pos.z;
        for (Map.Entry<String,Player> e : this.owner.AllPlayers.entrySet()){
            e.getValue().dataPacket(pk);
        }
        Boolean isExploaded = false;
        if(!Server.getInstance().getLevelByName("miniwalls"+this.owner.number).getBlock(pos).isTransparent()){
            isExploaded = true;
        }
        if(!isExploaded){
            for (Map.Entry<String,Player> e : this.owner.Players.entrySet()){
                if(!this.owner.team.get(e.getValue()).equals(this.TeamType) && e.getValue().getGamemode() != 3){
                    if(e.getValue().add(0,1,0).distance(pos) <= 1){
                        Explode(pos);
                        Attack(e.getValue(), SKULL_DAMAGE);
                        isExploaded = true;
                    }
                }
            }
        }
        count++;
        if(count >= 20 || isExploaded){
            Explode(pos);
            RemoveEntityPacket pk2 = new RemoveEntityPacket();
            pk2.eid = eid;
            for (Map.Entry<String,Player> e : this.owner.AllPlayers.entrySet()){
                e.getValue().dataPacket(pk2);
            }
            return;
        }
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackLaunch(this, motionX, motionY, motionZ, pos, eid, count),1);
    }

    public void Explode(Vector3 pos){
        
    }

    public void Tick(){
        if(isDeathed) return;
        double minDistance = Integer.MAX_VALUE;
        double distance;
        Vector3 target = null;
        for (Map.Entry<String,Player> e : this.owner.Players.entrySet()){
            if(e.getValue() instanceof Player){
                if(!this.owner.team.get(e.getValue()).equals(this.TeamType) && e.getValue().getGamemode() != 3){
                    distance = this.Position.distance(e.getValue());
                    if(distance <= FIND_AREA && minDistance > distance){
                        target = e.getValue();
                    }
                }
            }
        }
        if(target instanceof Player){
            double yaw = this.getYaw(target);
            double pitch = this.getPitch(target);
            MoveEntityPacket pk = new MoveEntityPacket();
            pk.eid = this.EntityId;
            pk.x = this.Position.x;
            pk.y = this.Position.y;
            pk.z = this.Position.z;
            pk.headYaw = yaw;
            pk.yaw = yaw;
            pk.pitch = pitch;
            for (Map.Entry<String,Player> e : this.owner.AllPlayers.entrySet()){
                e.getValue().dataPacket(pk);
            }
            this.yaw = yaw;
            this.pitch = pitch;
            this.isLocked = true;
            this.charge++;
            if(this.charge >= 30){
                Launch();
                this.charge = 0;
            }
        }else{
            this.isLocked = false;
        }
        if(this.running) Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackTick(this),1);
    }

    public double getYaw(Vector3 target) {
        double mx = target.x - this.Position.x;
        double mz = target.z - this.Position.z;
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

    public double getPitch(Vector3 target) {
        double mx = target.x - this.Position.x;
        double mz = target.z - this.Position.z;
        double my = target.y - this.Position.y;
        double mxz = Math.sqrt(mx * mx + mz * mz);
        double pitch = 0;
        if(my == 0){
            pitch = 0;
        }else {
            if (my > 0 && mxz > 0) {
                double atan = Math.atan(mxz/my);
                pitch = -(rad2deg(atan) - 90);
            }else if (my < 0 && mxz > 0) {
                double atan = Math.atan(Math.abs(mxz)/my);
                pitch = -(rad2deg(atan)) - 90;
            }
        }
        pitch = - pitch;
        return pitch;
    }

    public static double rad2deg(double radian) {
        return radian * (180f / Math.PI);
    }

    public int TeamType;
    public int HitPoint;
    public long EntityId;
    public Miniwalls owner;
    public double yaw = 0;
    public double pitch = 0;
    public int charge = 0;
    public Vector3 Position;
    public boolean isLocked = false;
    public boolean running = false;
    public boolean isDeathed = false;
    public boolean isLowHP = false;
    public AxisAlignedBB boundingBox = new AxisAlignedBB(0, 0, 0, 0, 0, 0);
}
class CallbackLaunch extends Task{

    public MiniwallsWitherManager owner;
    public double motionx;
    public double motiony;
    public double motionz;
    public int count;
    public Vector3 pos;
    public long eid;
    public Level level;

    public CallbackLaunch(MiniwallsWitherManager owner, double MotionX, double MotionY, double MotionZ, Vector3 pos, long eid, int count){
        this.owner = owner;
        this.motionx = MotionX;
        this.motiony = MotionY;
        this.motionz = MotionZ;
        this.pos = pos;
        this.eid = eid;
        this.count = count;
    }

    public void onRun(int d){
        this.owner.Launch2(this.motionx, this.motiony, this.motionz, this.pos, this.eid, this.count);
    }
}
class CallbackTick extends Task{

    public MiniwallsWitherManager owner;

    public CallbackTick(MiniwallsWitherManager owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.Tick();
    }
}
class CallbackDeath extends Task{

    public MiniwallsWitherManager owner;

    public CallbackDeath(MiniwallsWitherManager owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.Death();
    }
}