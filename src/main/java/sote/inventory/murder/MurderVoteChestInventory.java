package sote.inventory.murder;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.BlockEntityDataPacket;
import cn.nukkit.network.protocol.ContainerOpenPacket;
import cn.nukkit.network.protocol.ContainerSetContentPacket;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import cn.nukkit.scheduler.Task;
import sote.Game;
import sote.GameProvider;
import sote.inventory.DoubleServerChestInventory;
import sote.inventory.Inventorys;
import sote.inventory.ServerChestInventory;
import sote.murder.Murder;

public class MurderVoteChestInventory extends ServerChestInventory{

    public static final int CHEST_SIZE = 5;

    public MurderVoteChestInventory(Player player){
        super(player);
    }

    @Override
    public void register(){
        this.spawnChestBlock();
        this.SpawnBlockEntity();
        HashMap<Integer, Item> items = new HashMap<Integer, Item>();
        Item item;
        Game game = GameProvider.getPlayingGame(player);
        if(!(game instanceof Murder)) return;
        Murder murder = (Murder) game;
        for(int i = 0;i < murder.stages.length;i++){
            item = Item.get(339,i,1);
            item.setCustomName("§a"+murder.stages[i].getName());
            items.put(i * 2, item);
        }
        this.doubleinv = new DoubleServerChestInventory(items);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackOpenMurderVoteChestInventory(this), 5);
    }

    public void spawnChestBlock(){
        this.x = (int) player.x;
        this.y = (int) player.y - 3;
        this.z = (int) player.z;
        this.after = player.getLevel().getBlock(new Vector3(x, y, z));
        UpdateBlockPacket pk = new UpdateBlockPacket();
        pk.x = x;
        pk.y = y;
        pk.z = z;
        pk.blockId = 154;
        pk.blockData = 0;
        pk.flags = UpdateBlockPacket.FLAG_NONE;
        player.dataPacket(pk);
    }

    public void SpawnBlockEntity(){
        CompoundTag nbt = new CompoundTag("")
                .putList(new ListTag<>("Items"))
                .putString("id", BlockEntity.HOPPER)
                .putInt("x", x)
                .putInt("y", y)
                .putInt("z", z);
        BlockEntityDataPacket pk = new BlockEntityDataPacket();
        pk.x = x;
        pk.y = y;
        pk.z = z;
        try {
            pk.namedTag = NBTIO.write(nbt, ByteOrder.LITTLE_ENDIAN, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        player.dataPacket(pk);
    }

    public void open(){
        ContainerOpenPacket pk = new ContainerOpenPacket();
        pk.windowid = (byte) windowid;
        pk.type = (byte) 8;
        //pk.slots = CHEST_SIZE;
        pk.x = x;
        pk.y = y;
        pk.z = z;
        player.dataPacket(pk);
        sendContents();
    }

    public void sendContents(){
        ContainerSetContentPacket pk2 = new ContainerSetContentPacket();
        pk2.slots = new Item[CHEST_SIZE];
        for (int i = 0; i < CHEST_SIZE; ++i) {
            pk2.slots[i] = doubleinv.getItem(i);
        }
        pk2.windowid = (byte) windowid;
        player.dataPacket(pk2);
    }

    public void sendSlot(){
    }

    @Override
    public void update(){
        HashMap<Integer, Item> items = new HashMap<Integer, Item>();
        teleportor = new HashMap<Integer, Player>();
        Item item;
        Game game = GameProvider.getPlayingGame(player);
        if(!(game instanceof Murder)) return;
        Murder murder = (Murder) game;
        for(int i = 0;i < murder.stages.length;i++){
            item = Item.get(339,i,1);
            item.setCustomName("§a"+murder.stages[i].getName());
            items.put(i * 2, item);
        }
        doubleinv.setContents(items);
        sendContents();
        Inventorys.data2.get(player).register(player);
    }

    @Override
    public void Function(int slot,Item item){
        if(Inventorys.getGUI(player) == Inventorys.GUI_POCKET && this.doubletouch != slot){
            update();
            this.doubletouch = slot;
            return;
        }
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC){
            if(item.getId() != Item.AIR){
                Game game = GameProvider.getPlayingGame(player);
                if(!(game instanceof Murder)) return;
                Murder murder = (Murder) game;
                switch(slot){
                    case 0:
                        murder.vote(player, 0);
                    break;
                    case 2:
                        murder.vote(player, 1);
                    break;
                    case 4:
                        murder.vote(player, 2);
                    break;
                }
                update();
            }
        }else{
            if(item.getId() == Item.AIR){
                Game game = GameProvider.getPlayingGame(player);
                if(!(game instanceof Murder)) return;
                Murder murder = (Murder) game;
                switch(slot){
                    case 0:
                        murder.vote(player, 0);
                    break;
                    case 2:
                        murder.vote(player, 1);
                    break;
                    case 4:
                        murder.vote(player, 2);
                    break;
                }
                update();
            }
        }
    }

    @Override
    public void close(){
        super.close();
        Inventorys.data2.get(player).register(player);
    }

    public HashMap<Integer, Player> teleportor = new HashMap<Integer, Player>();
}
class CallbackOpenMurderVoteChestInventory extends Task{

    public MurderVoteChestInventory owner;

    public CallbackOpenMurderVoteChestInventory(MurderVoteChestInventory o){
        owner = o;
    }

    public void onRun(int d){
        owner.open();
    }
}
