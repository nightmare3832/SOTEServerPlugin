package sote.inventory;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.ContainerClosePacket;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import cn.nukkit.scheduler.Task;

public abstract class ServerChestInventory{

    public ServerChestInventory(Player player){
        this.player = player;
    }

    public void register(){
        
    }

    public void Function(int slot,Item item){
        
    }

    public void update(){
        
    }

    public void close(){
        if(x == 9999 && y == 9999 && z == 9999) return;
        //player.removeWindow(inv.getInventory());
        UpdateBlockPacket pk = new UpdateBlockPacket();
        pk.x = (int) x;
        pk.y = (int) y;
        pk.z = (int) z;
        pk.blockId = after.getId();
        pk.blockData = after.getDamage();
        pk.flags = UpdateBlockPacket.FLAG_NONE;
        player.dataPacket(pk);
        x = 9999;
        y = 9999;
        z = 9999;
        if(x2 == 9999 && y2 == 9999 && z2 == 9999) return;
        //player.removeWindow(inv.getInventory());
        UpdateBlockPacket pk2 = new UpdateBlockPacket();
        pk2.x = (int) x2;
        pk2.y = (int) y2;
        pk2.z = (int) z2;
        pk2.blockId = after2.getId();
        pk2.blockData = after2.getDamage();
        pk2.flags = UpdateBlockPacket.FLAG_NONE;
        player.dataPacket(pk2);
        x2 = 9999;
        y2 = 9999;
        z2 = 9999;
    }

    public void close2(){
        ContainerClosePacket pk = new ContainerClosePacket();
        pk.windowid = (byte) this.windowid;
        player.dataPacket(pk);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackClose(this), 60);
    }

    public Player player;

    public int x = 9999;
    public int y = 9999;
    public int z = 9999;
    public Block after;

    public int x2 = 9999;
    public int y2 = 9999;
    public int z2 = 9999;
    public Block after2;

    public DoubleServerChestInventory doubleinv;
    public int windowid = 255;
    public int doubletouch = -1;
    public int screen = 0;

}
class CallbackRemove extends Task{

    public Player player;
    public Item item;

    public CallbackRemove(Player p,Item i){
        player = p;
        item = i;
    }

    public void onRun(int d){
        go();
    }
    
    public void go(){
        ServerChestInventorys.remove(player,item);
    }
}
class CallbackClose extends Task{

    public ServerChestInventory o;

    public CallbackClose(ServerChestInventory owner){
        o = owner;
    }

    public void onRun(int d){
        go();
    }
    
    public void go(){
        o.close();
    }
}

