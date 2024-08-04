package sote.inventory.miniwalls;

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
import sote.miniwalls.MiniwallsUpgrader;
import sote.stat.Stat;

public class MiniwallsUpgraderChestInventory extends ServerChestInventory{

    public static final int CHEST_SIZE = 54;

    public final int SOLDIER_INDEX = 10;
    public final int ARCHER_INDEX = 19;
    public final int BUILDER_INDEX = 28;
    public final int COIN_INDEX = 49;

    public final int POCKET_SOLDIER_INDEX = 0;
    public final int POCKET_ARCHER_INDEX = 12;
    public final int POCKET_BUILDER_INDEX = 24;
    public final int POCKET_COIN_INDEX = 36;

    public MiniwallsUpgraderChestInventory(Player player){
        super(player);
    }

    @Override
    public void register(){
        this.spawnChestBlock();
        this.SpawnBlockEntity();
        HashMap<Integer, Item> items = new HashMap<Integer, Item>();
        Item item = Item.get(Item.WOODEN_SWORD,0,1);
        item.setCustomName(Main.getMessage(player,"item.miniwalls.upgrader.soldier"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(SOLDIER_INDEX, item);
        else items.put(POCKET_SOLDIER_INDEX, item);
        int count = 0;
        Item levelItem;
        for(int i = 1;i <= 6;i++){
            if(i == MiniwallsUpgrader.MAX_LEVEL && Stat.getMiniwallsSoldierLevel(player) == MiniwallsUpgrader.MAX_LEVEL) levelItem = Item.get(Item.DYE,13,1);
            else if(count < Stat.getMiniwallsSoldierLevel(player)) levelItem = Item.get(Item.DYE,10,1);
            else levelItem = Item.get(Item.DYE,8,1);
            levelItem.setCustomName(getItemName(MiniwallsUpgrader.TYPE_SOLDIER, i));
            if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(i+SOLDIER_INDEX, levelItem);
            else items.put(i+POCKET_SOLDIER_INDEX, levelItem);
            count++;
        }
        item = Item.get(Item.BOW,0,1);
        item.setCustomName(Main.getMessage(player,"item.miniwalls.upgrader.archer"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(ARCHER_INDEX, item);
        else items.put(POCKET_ARCHER_INDEX, item);
        count = 0;
        for(int i = 1;i <= 6;i++){
            if(i == MiniwallsUpgrader.MAX_LEVEL && Stat.getMiniwallsArcherLevel(player) == MiniwallsUpgrader.MAX_LEVEL) levelItem = Item.get(Item.DYE,13,1);
            else if(count < Stat.getMiniwallsArcherLevel(player)) levelItem = Item.get(Item.DYE,10,1);
            else levelItem = Item.get(Item.DYE,8,1);
            levelItem.setCustomName(getItemName(MiniwallsUpgrader.TYPE_ARCHER, i));
            if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(i+ARCHER_INDEX, levelItem);
            else items.put(i+POCKET_ARCHER_INDEX, levelItem);
            count++;
        }
        item = Item.get(Item.WOODEN_PICKAXE,0,1);
        item.setCustomName(Main.getMessage(player,"item.miniwalls.upgrader.builder"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(BUILDER_INDEX, item);
        else items.put(POCKET_BUILDER_INDEX, item);
        count = 0;
        for(int i = 1;i <= 6;i++){
            if(i == MiniwallsUpgrader.MAX_LEVEL && Stat.getMiniwallsBuilderLevel(player) == MiniwallsUpgrader.MAX_LEVEL) levelItem = Item.get(Item.DYE,13,1);
            else if(count < Stat.getMiniwallsBuilderLevel(player)) levelItem = Item.get(Item.DYE,10,1);
            else levelItem = Item.get(Item.DYE,8,1);
            levelItem.setCustomName(getItemName(MiniwallsUpgrader.TYPE_BUILDER, i));
            if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(i+BUILDER_INDEX, levelItem);
            else items.put(i+POCKET_BUILDER_INDEX, levelItem);
            count++;
        }
        item = Item.get(Item.DOUBLE_PLANT,0,1);
        item.setCustomName(Main.getMessage(player,"item.miniwalls.upgrader.coin", new String[]{String.valueOf(Stat.getCoin(player))}));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(COIN_INDEX, item);
        else items.put(POCKET_COIN_INDEX, item);
        this.doubleinv = new DoubleServerChestInventory(items);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackOpenMiniwallsUpgraderChestInventory(this), 5);
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

    public String getItemName(int type, int level){
        String jobname = "";
        if(type == MiniwallsUpgrader.TYPE_SOLDIER) jobname = "soldier";
        else if(type == MiniwallsUpgrader.TYPE_ARCHER) jobname = "archer";
        else if(type == MiniwallsUpgrader.TYPE_BUILDER) jobname = "builder";
        String base = "item.miniwalls.upgrader."+jobname+".level";
        String levelString = String.valueOf(level);
        String priceString = String.valueOf(MiniwallsUpgrader.getPrice(level));
        String additionString = "";
        int nowlevel = 0;
        if(type == MiniwallsUpgrader.TYPE_SOLDIER) nowlevel = Stat.getMiniwallsSoldierLevel(player);
        else if(type == MiniwallsUpgrader.TYPE_ARCHER) nowlevel = Stat.getMiniwallsArcherLevel(player);
        else if(type == MiniwallsUpgrader.TYPE_BUILDER) nowlevel = Stat.getMiniwallsBuilderLevel(player);
        if(level > MiniwallsUpgrader.MAX_LEVEL){
            additionString = "item.miniwalls.upgrader.addition.invalid";
        }else if(level <= nowlevel){
            additionString = "item.miniwalls.upgrader.addition.already.buy";
        }else if(nowlevel + 1 < level){
            additionString = "item.miniwalls.upgrader.addition.cant.buy";
        }else if(nowlevel + 1 == level){
            int coin = Stat.getCoin(player);
            int price = MiniwallsUpgrader.getPrice(level);
            if(coin >= price){
                additionString = "item.miniwalls.upgrader.addition.can.buy";
            }else{
                additionString = "item.miniwalls.upgrader.addition.not.enough.coin";
            }
        }
        additionString = Main.getMessage(player, additionString);
        return Main.getMessage(player,base, new String[]{levelString,priceString,additionString});
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
        Item item = Item.get(Item.WOODEN_SWORD,0,1);
        item.setCustomName(Main.getMessage(player,"item.miniwalls.upgrader.soldier"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(SOLDIER_INDEX, item);
        else items.put(POCKET_SOLDIER_INDEX, item);
        int count = 0;
        Item levelItem;
        for(int i = 1;i <= 6;i++){
            if(i == MiniwallsUpgrader.MAX_LEVEL && Stat.getMiniwallsSoldierLevel(player) == MiniwallsUpgrader.MAX_LEVEL) levelItem = Item.get(Item.DYE,13,1);
            else if(count < Stat.getMiniwallsSoldierLevel(player)) levelItem = Item.get(Item.DYE,10,1);
            else levelItem = Item.get(Item.DYE,8,1);
            levelItem.setCustomName(getItemName(MiniwallsUpgrader.TYPE_SOLDIER, i));
            if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(i+SOLDIER_INDEX, levelItem);
            else items.put(i+POCKET_SOLDIER_INDEX, levelItem);
            count++;
        }
        item = Item.get(Item.BOW,0,1);
        item.setCustomName(Main.getMessage(player,"item.miniwalls.upgrader.archer"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(ARCHER_INDEX, item);
        else items.put(POCKET_ARCHER_INDEX, item);
        count = 0;
        for(int i = 1;i <= 6;i++){
            if(i == MiniwallsUpgrader.MAX_LEVEL && Stat.getMiniwallsArcherLevel(player) == MiniwallsUpgrader.MAX_LEVEL) levelItem = Item.get(Item.DYE,13,1);
            else if(count < Stat.getMiniwallsArcherLevel(player)) levelItem = Item.get(Item.DYE,10,1);
            else levelItem = Item.get(Item.DYE,8,1);
            levelItem.setCustomName(getItemName(MiniwallsUpgrader.TYPE_ARCHER, i));
            if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(i+ARCHER_INDEX, levelItem);
            else items.put(i+POCKET_ARCHER_INDEX, levelItem);
            count++;
        }
        item = Item.get(Item.WOODEN_PICKAXE,0,1);
        item.setCustomName(Main.getMessage(player,"item.miniwalls.upgrader.builder"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(BUILDER_INDEX, item);
        else items.put(POCKET_BUILDER_INDEX, item);
        count = 0;
        for(int i = 1;i <= 6;i++){
            if(i == MiniwallsUpgrader.MAX_LEVEL && Stat.getMiniwallsBuilderLevel(player) == MiniwallsUpgrader.MAX_LEVEL) levelItem = Item.get(Item.DYE,13,1);
            else if(count < Stat.getMiniwallsBuilderLevel(player)) levelItem = Item.get(Item.DYE,10,1);
            else levelItem = Item.get(Item.DYE,8,1);
            levelItem.setCustomName(getItemName(MiniwallsUpgrader.TYPE_BUILDER, i));
            if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(i+BUILDER_INDEX, levelItem);
            else items.put(i+POCKET_BUILDER_INDEX, levelItem);
            count++;
        }
        item = Item.get(Item.DOUBLE_PLANT,0,1);
        item.setCustomName(Main.getMessage(player,"item.miniwalls.upgrader.coin", new String[]{String.valueOf(Stat.getCoin(player))}));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(COIN_INDEX, item);
        else items.put(POCKET_COIN_INDEX, item);
        doubleinv.setContents(items);
        sendContents();
        Inventorys.data2.get(player).register(player);
    }

    @Override
    public void Function(int slot,Item item){
        if(!(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) && this.doubletouch != slot){
            update();
            this.doubletouch = slot;
            return;
        }
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC){
            if(item.getId() != Item.AIR){
                switch(slot){
                    case SOLDIER_INDEX://SoldierItem
                    break;
                    case SOLDIER_INDEX + 1://SoldierLevel1
                    case SOLDIER_INDEX + 2://SoldierLevel2
                    case SOLDIER_INDEX + 3://SoldierLevel3
                    case SOLDIER_INDEX + 4://SoldierLevel4
                    case SOLDIER_INDEX + 5://SoldierLevel5
                    case SOLDIER_INDEX + 6://SoldierLevel6
                        MiniwallsUpgrader.Upgrade(player, MiniwallsUpgrader.TYPE_SOLDIER, slot - SOLDIER_INDEX);
                    break;
                    case ARCHER_INDEX://ArcherItem
                    break;
                    case ARCHER_INDEX + 1://ArcherLevel1
                    case ARCHER_INDEX + 2://ArcherLevel2
                    case ARCHER_INDEX + 3://ArcherLevel3
                    case ARCHER_INDEX + 4://ArcherLevel4
                    case ARCHER_INDEX + 5://ArcherLevel5
                    case ARCHER_INDEX + 6://ArcherLevel6
                        MiniwallsUpgrader.Upgrade(player, MiniwallsUpgrader.TYPE_ARCHER, slot - ARCHER_INDEX);
                    break;
                    case BUILDER_INDEX://BuilderItem
                    break;
                    case BUILDER_INDEX + 1://BuilderLevel1
                    case BUILDER_INDEX + 2://BuilderLevel2
                    case BUILDER_INDEX + 3://BuilderLevel3
                    case BUILDER_INDEX + 4://BuilderLevel4
                    case BUILDER_INDEX + 5://BuilderLevel5
                    case BUILDER_INDEX + 6://BuilderLevel6
                        MiniwallsUpgrader.Upgrade(player, MiniwallsUpgrader.TYPE_BUILDER, slot - BUILDER_INDEX);
                    break;
                }
                update();
            }
        }else{
            if(item.getId() == Item.AIR){
                switch(slot){
                    case POCKET_SOLDIER_INDEX://SoldierItem
                    break;
                    case POCKET_SOLDIER_INDEX + 1://SoldierLevel1
                    case POCKET_SOLDIER_INDEX + 2://SoldierLevel2
                    case POCKET_SOLDIER_INDEX + 3://SoldierLevel3
                    case POCKET_SOLDIER_INDEX + 4://SoldierLevel4
                    case POCKET_SOLDIER_INDEX + 5://SoldierLevel5
                    case POCKET_SOLDIER_INDEX + 6://SoldierLevel6
                        MiniwallsUpgrader.Upgrade(player, MiniwallsUpgrader.TYPE_SOLDIER, slot - POCKET_SOLDIER_INDEX);
                    break;
                    case POCKET_ARCHER_INDEX://ArcherItem
                    break;
                    case POCKET_ARCHER_INDEX + 1://ArcherLevel1
                    case POCKET_ARCHER_INDEX + 2://ArcherLevel2
                    case POCKET_ARCHER_INDEX + 3://ArcherLevel3
                    case POCKET_ARCHER_INDEX + 4://ArcherLevel4
                    case POCKET_ARCHER_INDEX + 5://ArcherLevel5
                    case POCKET_ARCHER_INDEX + 6://ArcherLevel6
                        MiniwallsUpgrader.Upgrade(player, MiniwallsUpgrader.TYPE_ARCHER, slot - POCKET_ARCHER_INDEX);
                    break;
                    case POCKET_BUILDER_INDEX://BuilderItem
                    break;
                    case POCKET_BUILDER_INDEX + 1://BuilderLevel1
                    case POCKET_BUILDER_INDEX + 2://BuilderLevel2
                    case POCKET_BUILDER_INDEX + 3://BuilderLevel3
                    case POCKET_BUILDER_INDEX + 4://BuilderLevel4
                    case POCKET_BUILDER_INDEX + 5://BuilderLevel5
                    case POCKET_BUILDER_INDEX + 6://BuilderLevel6
                        MiniwallsUpgrader.Upgrade(player, MiniwallsUpgrader.TYPE_BUILDER, slot - POCKET_BUILDER_INDEX);
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
class CallbackOpenMiniwallsUpgraderChestInventory extends Task{

    public MiniwallsUpgraderChestInventory owner;

    public CallbackOpenMiniwallsUpgraderChestInventory(MiniwallsUpgraderChestInventory o){
        owner = o;
    }

    public void onRun(int d){
        owner.open();
    }
}
