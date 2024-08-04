package sote.inventory.bedwars;

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
import sote.Main;
import sote.PlayerDataManager;
import sote.bedwars.Bedwars;
import sote.inventory.DoubleServerChestInventory;
import sote.inventory.Inventorys;
import sote.inventory.ServerChestInventory;

public class BedwarsUpgraderChestInventory extends ServerChestInventory{

    public static final int CHEST_SIZE = 54;

    public final int[] UPGRADE_INDEX = new int[]{
            11,12,13,14,15,
            20,21,22,23,24,
            29,30,31,32,33,
            38,39,40,41,42,
    };

    public final int[] POCKET_UPGRADE_INDEX = new int[]{
            10,11,12,13,14,15,16,
            19,20,21,22,23,24,25,
            28,29,30,31,32,33,34,
            37,38,39,40,41,42,43
    };

    public HashMap<Integer, Integer> upgradeItem = new HashMap<>();
    public Map<Integer, Item> contents;
    public Item[] contentsArmor;

    public BedwarsUpgraderChestInventory(Player player){
        super(player);
    }

    @Override
    public void register(){
        this.spawnChestBlock();
        this.SpawnBlockEntity();
        HashMap<Integer, Item> items = new HashMap<Integer, Item>();
        Game game = GameProvider.getPlayingGame(player);
        if(!(game instanceof Bedwars)) return;
        Bedwars bedwars = (Bedwars) game;
        int c = 0;
        for(Map.Entry<Integer, Item> e : bedwars.upgraders.get(bedwars.team.get(PlayerDataManager.getPlayerData(player))).items.entrySet()){
        	e.getValue().setCustomName(Main.getMessage(player,"bedwars.upgrader."+e.getKey()+".not.0"));
            if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC){
                items.put(UPGRADE_INDEX[c], e.getValue());
                upgradeItem.put(UPGRADE_INDEX[c], e.getKey());
            }else{
                items.put(POCKET_UPGRADE_INDEX[c], e.getValue());
                upgradeItem.put(POCKET_UPGRADE_INDEX[c], e.getKey());
            }
            c++;
        }
        this.contents = this.player.getInventory().getContents();
        this.contentsArmor = this.player.getInventory().getArmorContents();
        this.doubleinv = new DoubleServerChestInventory(items);
        this.screen = 0;
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackOpenBedwarsUpgraderChestInventory(this), 5);
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
                .putInt("pairz", z)
                .putString("CustomName", "Team Upgrades");
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
                .putInt("pairz", z)
                .putString("CustomName", "Team Upgrades");
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
        Game game = GameProvider.getPlayingGame(player);
        if(!(game instanceof Bedwars)) return;
        Bedwars bedwars = (Bedwars) game;
        int c = 0;
        for(Map.Entry<Integer, Item> e : bedwars.upgraders.get(bedwars.team.get(PlayerDataManager.getPlayerData(player))).items.entrySet()){
            if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC){
                items.put(UPGRADE_INDEX[c], e.getValue());
                upgradeItem.put(UPGRADE_INDEX[c], e.getKey());
            }else{
                items.put(POCKET_UPGRADE_INDEX[c], e.getValue());
                upgradeItem.put(POCKET_UPGRADE_INDEX[c], e.getKey());
            }
            c++;
        }
        doubleinv.setContents(items);
        sendContents();
        player.getInventory().setContents(contents);
        player.getInventory().setArmorContents(contentsArmor);
        player.getInventory().sendContents(player);
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
                if(upgradeItem.containsKey(slot)){
                    Game game = GameProvider.getPlayingGame(player);
                    if(!(game instanceof Bedwars)) return;
                    Bedwars bedwars = (Bedwars) game;
                    bedwars.upgraders.get(bedwars.team.get(PlayerDataManager.getPlayerData(player))).upgrade(player, upgradeItem.get(slot));
                    this.contents = player.getInventory().getContents();
                    this.contentsArmor = player.getInventory().getArmorContents();
                }
                update();
            }
        }else{
            if(item.getId() == Item.AIR){
                if(upgradeItem.containsKey(slot)){
                    Game game = GameProvider.getPlayingGame(player);
                    if(!(game instanceof Bedwars)) return;
                    Bedwars bedwars = (Bedwars) game;
                    bedwars.upgraders.get(bedwars.team.get(PlayerDataManager.getPlayerData(player))).upgrade(player, upgradeItem.get(slot));
                    this.contents = player.getInventory().getContents();
                    this.contentsArmor = player.getInventory().getArmorContents();
                }
                update();
            }
        }
    }

    @Override
    public void close(){
        super.close();
        player.getInventory().setContents(contents);
        player.getInventory().setArmorContents(contentsArmor);
        player.getInventory().sendContents(player);
        player.getInventory().sendArmorContents(player);
    }
}
class CallbackOpenBedwarsUpgraderChestInventory extends Task{

    public BedwarsUpgraderChestInventory owner;

    public CallbackOpenBedwarsUpgraderChestInventory(BedwarsUpgraderChestInventory o){
        owner = o;
    }

    public void onRun(int d){
        owner.open();
    }
}
