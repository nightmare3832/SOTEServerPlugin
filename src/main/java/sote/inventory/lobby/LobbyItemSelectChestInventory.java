package sote.inventory.lobby;

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
import sote.Main;
import sote.inventory.DoubleServerChestInventory;
import sote.inventory.Inventorys;
import sote.inventory.ServerChestInventory;
import sote.lobbyitem.LobbyItems;

public class LobbyItemSelectChestInventory extends ServerChestInventory{

    public static final int CHEST_SIZE = 54;

    public LobbyItemSelectChestInventory(Player player){
        super(player);
    }

    @Override
    public void register(){
        this.x = (int) player.x;
        this.y = (int) player.y - 3;
        this.z = (int) player.z;
        this.spawnChestBlock();
        this.SpawnBlockEntity();
        HashMap<Integer, Item> items = new HashMap<Integer, Item>();
        lobbyitems = new HashMap<Integer, Item>();
        int count = 0;
        Item item;
        Map<String, Boolean> map = (Map<String, Boolean>) LobbyItems.lobbyitemData.get(player.getName().toLowerCase());
        if(map.get("unknown")){
            item = Item.get(1,0,1);
            item.setCustomName(Main.getMessage(player,"item.unknown"));
            items.put(count, item);
            lobbyitems.put(count,  item);
            count++;
        }
        if(map.get("fireparticle")){
            item = Item.get(377,0,1);
            item.setCustomName(Main.getMessage(player,"item.fireparticle"));
            items.put(count, item);
            lobbyitems.put(count,  item);
            count++;
        }
        if(map.get("flameparticle")){
            item = Item.get(378,0,1);
            item.setCustomName(Main.getMessage(player,"item.flameparticle"));
            items.put(count, item);
            lobbyitems.put(count,  item);
            count++;
        }
        if(map.get("waterparticle")){
            item = Item.get(373,0,1);
            item.setCustomName(Main.getMessage(player,"item.waterparticle"));
            items.put(count, item);
            lobbyitems.put(count,  item);
            count++;
        }
        if(map.get("musicparticle")){
            item = Item.get(25,0,1);
            item.setCustomName(Main.getMessage(player,"item.musicparticle"));
            items.put(count, item);
            lobbyitems.put(count,  item);
            count++;
        }
        if(map.get("smokeparticle")){
            item = Item.get(369,0,1);
            item.setCustomName(Main.getMessage(player,"item.smokeparticle"));
            items.put(count, item);
            lobbyitems.put(count,  item);
            count++;
        }
        if(map.get("heartparticle")){
            item = Item.get(1,0,1);
            item.setCustomName(Main.getMessage(player,"item.heartparticle"));
            items.put(count, item);
            lobbyitems.put(count,  item);
            count++;
        }
        if(map.get("colorparticle")){
            item = Item.get(1,0,1);
            item.setCustomName(Main.getMessage(player,"item.colorparticle"));
            items.put(count, item);
            lobbyitems.put(count,  item);
            count++;
        }
        if(map.get("rainbowparticle")){
            item = Item.get(1,0,1);
            item.setCustomName(Main.getMessage(player,"item.rainbowparticle"));
            items.put(count, item);
            lobbyitems.put(count,  item);
            count++;
        }
        if(map.get("soteparticle")){
            item = Item.get(1,0,1);
            item.setCustomName(Main.getMessage(player,"item.soteparticle"));
            items.put(count, item);
            lobbyitems.put(count,  item);
            count++;
        }
        if(map.get("enderparticle")){
            item = Item.get(1,0,1);
            item.setCustomName(Main.getMessage(player,"item.enderparticle"));
            items.put(count, item);
            lobbyitems.put(count,  item);
            count++;
        }
        if(map.get("bombluncher")){
            item = Item.get(46,0,1);
            item.setCustomName(Main.getMessage(player,"item.bombluncher"));
            items.put(count, item);
            lobbyitems.put(count,  item);
            count++;
        }
        if(map.get("blockluncher")){
            item = Item.get(2,0,1);
            item.setCustomName(Main.getMessage(player,"item.blockluncher"));
            items.put(count, item);
            lobbyitems.put(count,  item);
            count++;
        }
        this.doubleinv = new DoubleServerChestInventory(items);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackOpenLobbyItemSelectChestInventory(this), 5);
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
        lobbyitems = new HashMap<Integer, Item>();
        int count = 0;
        Item item;
        Map<String, Boolean> map = (Map<String, Boolean>) LobbyItems.lobbyitemData.get(player.getName().toLowerCase());
        if(map.get("unknown")){
            item = Item.get(1,0,1);
            item.setCustomName(Main.getMessage(player,"item.unknown"));
            items.put(count, item);
            lobbyitems.put(count,  item);
            count++;
        }
        if(map.get("fireparticle")){
            item = Item.get(377,0,1);
            item.setCustomName(Main.getMessage(player,"item.fireparticle"));
            items.put(count, item);
            lobbyitems.put(count,  item);
            count++;
        }
        if(map.get("flameparticle")){
            item = Item.get(378,0,1);
            item.setCustomName(Main.getMessage(player,"item.flameparticle"));
            items.put(count, item);
            lobbyitems.put(count,  item);
            count++;
        }
        if(map.get("waterparticle")){
            item = Item.get(373,0,1);
            item.setCustomName(Main.getMessage(player,"item.waterparticle"));
            items.put(count, item);
            lobbyitems.put(count,  item);
            count++;
        }
        if(map.get("musicparticle")){
            item = Item.get(25,0,1);
            item.setCustomName(Main.getMessage(player,"item.musicparticle"));
            items.put(count, item);
            lobbyitems.put(count,  item);
            count++;
        }
        if(map.get("smokeparticle")){
            item = Item.get(369,0,1);
            item.setCustomName(Main.getMessage(player,"item.smokeparticle"));
            items.put(count, item);
            lobbyitems.put(count,  item);
            count++;
        }
        if(map.get("heartparticle")){
            item = Item.get(1,0,1);
            item.setCustomName(Main.getMessage(player,"item.heartparticle"));
            items.put(count, item);
            lobbyitems.put(count,  item);
            count++;
        }
        if(map.get("colorparticle")){
            item = Item.get(1,0,1);
            item.setCustomName(Main.getMessage(player,"item.colorparticle"));
            items.put(count, item);
            lobbyitems.put(count,  item);
            count++;
        }
        if(map.get("rainbowparticle")){
            item = Item.get(1,0,1);
            item.setCustomName(Main.getMessage(player,"item.rainbowparticle"));
            items.put(count, item);
            lobbyitems.put(count,  item);
            count++;
        }
        if(map.get("soteparticle")){
            item = Item.get(1,0,1);
            item.setCustomName(Main.getMessage(player,"item.soteparticle"));
            items.put(count, item);
            lobbyitems.put(count,  item);
            count++;
        }
        if(map.get("enderparticle")){
            item = Item.get(1,0,1);
            item.setCustomName(Main.getMessage(player,"item.enderparticle"));
            items.put(count, item);
            lobbyitems.put(count,  item);
            count++;
        }
        if(map.get("bombluncher")){
            item = Item.get(46,0,1);
            item.setCustomName(Main.getMessage(player,"item.bombluncher"));
            items.put(count, item);
            lobbyitems.put(count,  item);
            count++;
        }
        if(map.get("blockluncher")){
            item = Item.get(2,0,1);
            item.setCustomName(Main.getMessage(player,"item.blockluncher"));
            items.put(count, item);
            lobbyitems.put(count,  item);
            count++;
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
                if(lobbyitems.containsKey(slot)){
                    LobbyItems.setSellectLobbyItem(player,lobbyitems.get(slot));
                    player.sendMessage(Main.getMessage(player,"lobbyitem.set"));
                }
            }
        }else{
            if(item.getId() == Item.AIR){
                if(lobbyitems.containsKey(slot)){
                    LobbyItems.setSellectLobbyItem(player,lobbyitems.get(slot));
                    player.sendMessage(Main.getMessage(player,"lobbyitem.set"));
                }
            }
        }
    }

    @Override
    public void close(){
        super.close();
        Inventorys.data2.get(player).register(player);
    }

    public HashMap<Integer, Item> lobbyitems = new HashMap<Integer, Item>();
}
class CallbackOpenLobbyItemSelectChestInventory extends Task{

    public LobbyItemSelectChestInventory owner;

    public CallbackOpenLobbyItemSelectChestInventory(LobbyItemSelectChestInventory o){
        owner = o;
    }

    public void onRun(int d){
        owner.open();
    }
}
