package sote.murder;

import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;
import cn.nukkit.scheduler.Task;

public class MurderEmeraldManager{

    public Murder owner;
    public int number;
    public HashMap<Integer, Boolean> isSpawned = new HashMap<Integer, Boolean>();
    public int next = 0;
    public int max;
    public Vector3[] pos;
    public final int MAX_EMERALDS = 10;

    public MurderEmeraldManager(Murder owner){
        this.owner = owner;
        this.max = owner.stage.getEmerald().length - 1;
        this.pos = owner.stage.getEmerald();
        for(int i = 1;i <= MAX_EMERALDS;i++){
            isSpawned.put(i, false);
        }
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackLater(this),5);
    }

    public void reset(){
        for(int i = 1;i <= MAX_EMERALDS;i++){
            isSpawned.put(i, false);
        }
        this.next = 0;
        this.max = owner.stage.getEmerald().length - 1;
        this.pos = owner.stage.getEmerald();
    }

    public void later(){
        Server.getInstance().getScheduler().scheduleRepeatingTask(new CallbackTick(this),200);
    }

    public void pickup(Player player,EntityItem itementity){
        Item item = itementity.getItem();
        this.isSpawned.put(item.getDamage(), false);
    }

    public void tick(){
        pos = this.pos;
        boolean dropped = false;
        for(int i = 1;i <= MAX_EMERALDS;i++){
            if(!isSpawned.get(i) && !dropped){
                owner.world.dropItem(pos[this.next],new Item(388,i,1));
                this.isSpawned.put(i, false);
                this.next++;
                if(this.next > this.max) this.next = 0;
                dropped = true;
            }
        }
    }
}
class CallbackLater extends Task{
	
    public MurderEmeraldManager owner;

    public CallbackLater(MurderEmeraldManager owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.later();
    }
}
class CallbackTick extends Task{
	
    public MurderEmeraldManager owner;

    public CallbackTick(MurderEmeraldManager owner){
        this.owner = owner;
    }

    public void onRun(int d){
        if(this.owner.owner.getGameDataAsBoolean("gamenow2")) this.owner.tick();
    }
}