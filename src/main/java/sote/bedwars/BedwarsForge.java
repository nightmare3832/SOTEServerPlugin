package sote.bedwars;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.item.Item;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.Vector3;
import cn.nukkit.scheduler.Task;

public class BedwarsForge{

    public static final int RESOURCE_IRON = 0;
    public static final int RESOURCE_GOLD = 1;
    public static final int RESOURCE_EMERALD = 2;

    public BedwarsForge(Bedwars o, Vector3 pos){
        this.owner = o;
        this.position = pos;
        this.ironTask = new CallbackTick(this, RESOURCE_IRON);
        this.goldTask = new CallbackTick(this, RESOURCE_GOLD);
        this.emeraldTask = new CallbackTick(this, RESOURCE_EMERALD);
        this.boundingBox = new AxisAlignedBB(this.position.x, this.position.y, this.position.z, this.position.x, this.position.y, this.position.z).grow(4, 4, 4);
        this.start();
    }

    public void start(){
        int[] time = getResourceSpawnTime();
        Server.getInstance().getScheduler().scheduleDelayedTask(ironTask, time[0]);
        Server.getInstance().getScheduler().scheduleDelayedTask(goldTask, time[1]);
        Server.getInstance().getScheduler().scheduleDelayedTask(emeraldTask, time[2]);
    }

    public int[] getResourceSpawnTime(){
        switch(this.level){
            case 1:
                return new int[]{
                    20,
                    140,
                    0
                };
            case 2:
                return new int[]{
                    20,
                    100,
                    0
                };
        }
        return new int[]{20, 20, 20};
    }

    public int[] getMaxResource(){
        return new int[]{
            49,
            16,
            3
        };
    }

    public void spawnResource(int resourceType){
        if(!this.owner.getGameDataAsBoolean("gamenow")) return;
        int[] time = getResourceSpawnTime();
        if(time[resourceType] != 0 && checkAroundResource(resourceType)){
            int id = 0;
            if(resourceType == RESOURCE_IRON) id = Item.IRON_INGOT;
            if(resourceType == RESOURCE_GOLD) id = Item.GOLD_INGOT;
            if(resourceType == RESOURCE_EMERALD) id = Item.EMERALD;
            Item item = Item.get(id, 0, 1);
            this.owner.world.dropItem(this.position, item, this.motion);
        }
        if(resourceType == RESOURCE_IRON) Server.getInstance().getScheduler().scheduleDelayedTask(ironTask, time[0]);
        if(resourceType == RESOURCE_GOLD) Server.getInstance().getScheduler().scheduleDelayedTask(goldTask, time[1]);
        if(resourceType == RESOURCE_EMERALD) Server.getInstance().getScheduler().scheduleDelayedTask(emeraldTask, time[2]);
    }

    public boolean checkAroundResource(int resourceType){
        int id = 0;
        if(resourceType == RESOURCE_IRON) id = Item.IRON_INGOT;
        if(resourceType == RESOURCE_GOLD) id = Item.GOLD_INGOT;
        if(resourceType == RESOURCE_EMERALD) id = Item.EMERALD;
        int count = 0;
        for (Entity entity : this.owner.world.getNearbyEntities(this.boundingBox, (Entity)null)) {
            if (entity instanceof EntityItem){
                EntityItem entityItem = (EntityItem) entity;
                if(entityItem.getItem().getId() == id){
                    count += entityItem.getItem().getCount();
                }
            }
        }
        int[] max = getMaxResource();
        return count < max[resourceType];
    }

    public Bedwars owner;
    public int level = 1;
    public Vector3 position;
    public Task ironTask;
    public Task goldTask;
    public Task emeraldTask;
    public AxisAlignedBB boundingBox;

    public Vector3 motion = new Vector3();
}
class CallbackTick extends Task{

    public BedwarsForge owner;
    public int resourceType;

    public CallbackTick(BedwarsForge owner, int type){
        this.owner = owner;
        this.resourceType = type;
    }

    public void onRun(int d){
        this.owner.spawnResource(this.resourceType);
    }
}