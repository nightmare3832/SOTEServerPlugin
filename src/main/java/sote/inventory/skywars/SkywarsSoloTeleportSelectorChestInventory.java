package sote.inventory.skywars;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

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
import sote.skywarssolo.SkywarsSolo;

public class SkywarsSoloTeleportSelectorChestInventory extends ServerChestInventory{

    public static final int CHEST_SIZE = 27;

    public SkywarsSoloTeleportSelectorChestInventory(Player player){
        super(player);
    }

    @Override
    public void register(){
        this.spawnChestBlock();
        this.SpawnBlockEntity();
        HashMap<Integer, Item> items = new HashMap<Integer, Item>();
        teleportor = new HashMap<Integer, Player>();
        Item item;
        int count = 0;
        Game game = GameProvider.getPlayingGame(player);
        if(!(game instanceof SkywarsSolo)) return;
        for (Map.Entry<String,Player> e : game.Players.entrySet()){
            if(e.getValue().getGamemode() == 0){
                item = Item.get(Item.SKULL, 3, 1);
                item.setCustomName(e.getValue().getName());
                items.put(count, item);
                teleportor.put(count,  e.getValue());
                count++;
            }
        }
        this.doubleinv = new DoubleServerChestInventory(items);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackOpenSkywarsTeleportSelectorChestInventory(this), 5);
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
        pk.blockId = 54;
        pk.blockData = 0;
        pk.flags = UpdateBlockPacket.FLAG_NONE;
        player.dataPacket(pk);
    }

    public void SpawnBlockEntity(){
        CompoundTag nbt = new CompoundTag("")
                .putList(new ListTag<>("Items"))
                .putString("id", BlockEntity.CHEST)
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
        teleportor = new HashMap<Integer, Player>();
        Item item;
        int count = 0;
        Game game = GameProvider.getPlayingGame(player);
        if(!(game instanceof SkywarsSolo)) return;
        for (Map.Entry<String,Player> e : game.Players.entrySet()){
            if(e.getValue().getGamemode() == 0){
                item = Item.get(Item.SKULL, 3, 1);
                item.setCustomName(e.getValue().getName());
                items.put(count, item);
                teleportor.put(count,  e.getValue());
                count++;
            }
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
                if(teleportor.containsKey(slot)){
                    Game game2 = GameProvider.getPlayingGame(teleportor.get(slot));
                    if(game2.Players.containsValue(teleportor.get(slot))){
                        if(game2.getGameDataAsBoolean("gamenow2")){
                            this.close2();
                            Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackTeleportSkywarsTeleportSelectorChestInventory(this,teleportor.get(slot)), 5);
                        }
                    }
                }
            }
        }else{
            if(item.getId() == Item.AIR){
                if(teleportor.containsKey(slot)){
                    Game game2 = GameProvider.getPlayingGame(teleportor.get(slot));
                    if(game2.Players.containsValue(teleportor.get(slot))){
                        if(game2.getGameDataAsBoolean("gamenow2")){
                            this.close2();
                            Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackTeleportSkywarsTeleportSelectorChestInventory(this,teleportor.get(slot)), 5);
                        }
                    }
                }
            }
        }
    }

    public void tp(Player target){
        player.teleport(target);
    }

    @Override
    public void close(){
        super.close();
        Inventorys.data2.get(player).register(player);
    }

    public HashMap<Integer, Player> teleportor = new HashMap<Integer, Player>();
}
class CallbackOpenSkywarsTeleportSelectorChestInventory extends Task{

    public SkywarsSoloTeleportSelectorChestInventory owner;

    public CallbackOpenSkywarsTeleportSelectorChestInventory(SkywarsSoloTeleportSelectorChestInventory o){
        owner = o;
    }

    public void onRun(int d){
        owner.open();
    }
}
class CallbackTeleportSkywarsTeleportSelectorChestInventory extends Task{

    public SkywarsSoloTeleportSelectorChestInventory owner;
    public Player target;

    public CallbackTeleportSkywarsTeleportSelectorChestInventory(SkywarsSoloTeleportSelectorChestInventory o,Player t){
        owner = o;
        target = t;
    }

    public void onRun(int d){
        owner.tp(target);
    }
}
