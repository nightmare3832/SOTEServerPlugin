package sote.inventory.home;

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
import sote.Main;
import sote.inventory.DoubleServerChestInventory;
import sote.inventory.Inventorys;
import sote.inventory.ServerChestInventory;

public class HomeOptionChestInventory extends ServerChestInventory{

    public static final int CHEST_SIZE = 54;

    public final int MURDER_INDEX = 10;
    public final int SKYWARS_INDEX = 11;
    public final int MINIWALLS_INDEX = 12;
    public final int BUILDBATTLE_INDEX = 13;

    public final int POCKET_MURDER_INDEX = 0;
    public final int POCKET_SKYWARS_INDEX = 1;
    public final int POCKET_MINIWALLS_INDEX = 2;
    public final int POCKET_BUILDBATTLE_INDEX = 3;

    public final int SCREEN_TOP = 0;
    public final int SCREEN_MURDER = 1;
    public final int SCREEN_SKYWARS = 2;
    public final int SCREEN_MINIWALLS = 3;
    public final int SCREEN_BUILDBATTLE = 4;

    public int screen = 0;

    public HomeOptionChestInventory(Player player){
        super(player);
    }

    @Override
    public void register(){
        this.spawnChestBlock();
        this.SpawnBlockEntity();
        HashMap<Integer, Item> items = new HashMap<Integer, Item>();
        Item item;
        item = Item.get(Item.REDSTONE_DUST,0,1);
        item.setCustomName(Main.getMessage(player,"item.game.select.murder"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(MURDER_INDEX, item);
        else items.put(POCKET_MURDER_INDEX, item);
        item = Item.get(Item.FEATHER,0,1);
        item.setCustomName(Main.getMessage(player,"item.game.select.skywars"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(SKYWARS_INDEX, item);
        else items.put(POCKET_SKYWARS_INDEX, item);
        item = Item.get(Item.SKULL,0,1);
        item.setCustomName(Main.getMessage(player,"item.game.select.miniwalls"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(MINIWALLS_INDEX, item);
        else items.put(POCKET_MINIWALLS_INDEX, item);
        item = Item.get(Item.PLANK,0,1);
        item.setCustomName(Main.getMessage(player,"item.game.select.buildbattle"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(BUILDBATTLE_INDEX, item);
        else items.put(POCKET_BUILDBATTLE_INDEX, item);
        this.doubleinv = new DoubleServerChestInventory(items);
        //int cnt = player.getWindowId(this.doubleinv);
        //this.windowid = cnt;
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackOpenHomeOptionChestInventory(this), 5);
    }

    public void spawnChestBlock(){
        this.x = (int) player.x;
        this.y = (int) player.y - 3;
        this.z = (int) player.z;
        this.x2 = (int) player.x + 1;
        this.y2 = (int) player.y - 3;
        this.z2 = (int) player.z;
        this.after = player.getLevel().getBlock(new Vector3(x, y, z));
        UpdateBlockPacket pk = new UpdateBlockPacket();
        pk.x = x;
        pk.y = y;
        pk.z = z;
        pk.blockId = 54;
        pk.blockData = 0;
        pk.flags = UpdateBlockPacket.FLAG_NONE;
        player.dataPacket(pk);
        this.after2 = player.getLevel().getBlock(new Vector3(x + 1, y, z));
        UpdateBlockPacket pk2 = new UpdateBlockPacket();
        pk2.x = x + 1;
        pk2.y = y;
        pk2.z = z;
        pk2.blockId = 54;
        pk2.blockData = 0;
        pk2.flags = UpdateBlockPacket.FLAG_NONE;
        player.dataPacket(pk2);
    }

    public void SpawnBlockEntity(){
        CompoundTag nbt = new CompoundTag("")
                .putList(new ListTag<>("Items"))
                .putString("id", BlockEntity.CHEST)
                .putInt("x", x)
                .putInt("y", y)
                .putInt("z", z)
                .putInt("pairx", x + 1)
                .putInt("pairz", z);
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
        CompoundTag nbt2 = new CompoundTag("")
                .putList(new ListTag<>("Items"))
                .putString("id", BlockEntity.CHEST)
                .putInt("x", x + 1)
                .putInt("y", y)
                .putInt("z", z)
                .putInt("pairx", x)
                .putInt("pairz", z);
        BlockEntityDataPacket pk2 = new BlockEntityDataPacket();
        pk2.x = x + 1;
        pk2.y = y;
        pk2.z = z;
        try {
            pk2.namedTag = NBTIO.write(nbt2, ByteOrder.LITTLE_ENDIAN, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        player.dataPacket(pk2);
    }

    public void open(){
        ContainerOpenPacket pk = new ContainerOpenPacket();
        pk.windowid = (byte) windowid;
        pk.type = (byte) 0;
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
        Item item;
        switch(this.screen){
            case SCREEN_TOP:
                item = Item.get(Item.REDSTONE_DUST,0,1);
                item.setCustomName(Main.getMessage(player,"item.game.select.murder"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(MURDER_INDEX, item);
                else items.put(POCKET_MURDER_INDEX, item);
                item = Item.get(Item.FEATHER,0,1);
                item.setCustomName(Main.getMessage(player,"item.game.select.skywars"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(SKYWARS_INDEX, item);
                else items.put(POCKET_SKYWARS_INDEX, item);
                item = Item.get(Item.SKULL,0,1);
                item.setCustomName(Main.getMessage(player,"item.game.select.miniwalls"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(MINIWALLS_INDEX, item);
                else items.put(POCKET_MINIWALLS_INDEX, item);
                item = Item.get(Item.PLANK,0,1);
                item.setCustomName(Main.getMessage(player,"item.game.select.buildbattle"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(BUILDBATTLE_INDEX, item);
                else items.put(POCKET_BUILDBATTLE_INDEX, item);
            break;
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
                switch(this.screen){
                    case SCREEN_TOP:
                        switch(slot){
                            case MURDER_INDEX:
                                this.screen = SCREEN_MURDER;
                            break;
                            case SKYWARS_INDEX:
                                this.screen = SCREEN_SKYWARS;
                            break;
                            case MINIWALLS_INDEX:
                                this.screen = SCREEN_MINIWALLS;
                            break;
                            case BUILDBATTLE_INDEX:
                                this.screen = SCREEN_BUILDBATTLE;
                            break;
                        }
                    break;
                }
                update();
            }
        }else{
            if(item.getId() == Item.AIR){
                switch(this.screen){
                    case SCREEN_TOP:
                        switch(slot){
                            case POCKET_MURDER_INDEX:
                                this.screen = SCREEN_MURDER;
                            break;
                            case POCKET_SKYWARS_INDEX:
                                this.screen = SCREEN_SKYWARS;
                            break;
                            case POCKET_MINIWALLS_INDEX:
                                this.screen = SCREEN_MINIWALLS;
                            break;
                            case POCKET_BUILDBATTLE_INDEX:
                                this.screen = SCREEN_BUILDBATTLE;
                            break;
                        }
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
}
class CallbackOpenHomeOptionChestInventory extends Task{

    public HomeOptionChestInventory owner;

    public CallbackOpenHomeOptionChestInventory(HomeOptionChestInventory o){
        owner = o;
    }

    public void onRun(int d){
        owner.open();
    }
}

