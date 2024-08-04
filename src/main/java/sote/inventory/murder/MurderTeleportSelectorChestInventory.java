package sote.inventory.murder;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemColorArmor;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.BlockEntityDataPacket;
import cn.nukkit.network.protocol.ContainerOpenPacket;
import cn.nukkit.network.protocol.ContainerSetContentPacket;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.BlockColor;
import sote.Game;
import sote.GameProvider;
import sote.inventory.DoubleServerChestInventory;
import sote.inventory.Inventorys;
import sote.inventory.ServerChestInventory;
import sote.murder.Murder;

public class MurderTeleportSelectorChestInventory extends ServerChestInventory{

    public static final int CHEST_SIZE = 27;

    public MurderTeleportSelectorChestInventory(Player player){
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
        String playerName;
        String colorName;
        String jobName;
        Game game = GameProvider.getPlayingGame(player);
        if(!(game instanceof Murder)) return;
        Murder murder = (Murder) game;
        for (Map.Entry<String,Player> e : game.Players.entrySet()){
            if(e.getValue().getGamemode() == 2){
                if(murder.jobs.get(e.getValue()) == 0){
                    item = murder.chestplate.get(e.getValue());
                    if(murder.disguisedCount.get(e.getValue()) == 0){
                        playerName = "§"+murder.color.get(e.getValue())+murder.randomname.get(e.getValue());
                    }else{
                        playerName = "§"+murder.disguisedIdentity.get(e.getValue()).get(murder.disguisedCount.get(e.getValue())).get("color")+murder.disguisedIdentity.get(e.getValue()).get(murder.disguisedCount.get(e.getValue())).get("name");
                    }
                    BlockColor color = ((ItemColorArmor)murder.chestplate.get(e.getValue())).getColor();
                    colorName = "#"+toHexString(color.getRed(), color.getGreen(), color.getBlue());
                    jobName = "§cMurderer";
                    item.setCustomName(playerName+"\n"+
                                       "§7Color: "+colorName+"\n"+
                                       jobName+"\n"+
                                       "§7Weapons: §a"+murder.knifecount.get(e.getValue())+"\n"+
                                       "§7Emeralds: §a"+murder.emeralds.get(e.getValue())+"\n");
                    items.put(count, item);
                    teleportor.put(count,  e.getValue());
                }else{
                    item = murder.chestplate.get(e.getValue());
                    playerName = "§"+murder.color.get(e.getValue())+murder.randomname.get(e.getValue());
                    BlockColor color = ((ItemColorArmor)murder.chestplate.get(e.getValue())).getColor();
                    colorName = "#"+toHexString(color.getRed(), color.getGreen(), color.getBlue());
                    jobName = "§bBystander";
                    item.setCustomName(playerName+"\n"+
                                       "§7Color: "+colorName+"\n"+
                                       jobName+"\n"+
                                       "§7Weapons: §a"+(murder.havegun.get(e.getValue()) ? 1 : 0)+"\n"+
                                       "§7Emeralds: §a"+murder.emeralds.get(e.getValue())+"\n");
                    items.put(count, item);
                    teleportor.put(count,  e.getValue());
                }
                count++;
            }
        }
        this.doubleinv = new DoubleServerChestInventory(items);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackOpenMurderTeleportSelectorChestInventory(this), 5);
    }

    public String toHexString(int r, int g, int b) {
        StringBuffer buf = new StringBuffer();
        if (r < 16) {
            buf.append("0").append(Integer.toHexString(r));
        } else {
            buf.append(Integer.toHexString(r));
        }
        if (g < 16) {
            buf.append("0").append(Integer.toHexString(g));
        } else {
            buf.append(Integer.toHexString(g));
        }
        if (b < 16) {
            buf.append("0").append(Integer.toHexString(b));
        } else {
            buf.append(Integer.toHexString(b));
        }
        return buf.toString().toUpperCase();
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
        String playerName;
        String colorName;
        String jobName;
        Game game = GameProvider.getPlayingGame(player);
        if(!(game instanceof Murder)) return;
        Murder murder = (Murder) game;
        for (Map.Entry<String,Player> e : game.Players.entrySet()){
            if(e.getValue().getGamemode() == 2){
                if(murder.jobs.get(e.getValue()) == 0){
                    item = murder.chestplate.get(e.getValue());
                    if(murder.disguisedCount.get(e.getValue()) == 0){
                        playerName = "§"+murder.color.get(e.getValue())+murder.randomname.get(e.getValue());
                    }else{
                        playerName = "§"+murder.disguisedIdentity.get(e.getValue()).get(murder.disguisedCount.get(e.getValue())).get("color")+murder.disguisedIdentity.get(e.getValue()).get(murder.disguisedCount.get(e.getValue())).get("name");
                    }
                    BlockColor color = ((ItemColorArmor)murder.chestplate.get(e.getValue())).getColor();
                    colorName = "#"+toHexString(color.getRed(), color.getGreen(), color.getBlue());
                    jobName = "§cMurderer";
                    item.setCustomName(playerName+"\n"+
                                       "§7Color: "+colorName+"\n"+
                                       jobName+"\n"+
                                       "§7Weapons: §a"+murder.knifecount.get(e.getValue())+"\n"+
                                       "§7Emeralds: §a"+murder.emeralds.get(e.getValue())+"\n");
                    items.put(count, item);
                    teleportor.put(count,  e.getValue());
                }else{
                    item = murder.chestplate.get(e.getValue());
                    playerName = "§"+murder.color.get(e.getValue())+murder.randomname.get(e.getValue());
                    BlockColor color = ((ItemColorArmor)murder.chestplate.get(e.getValue())).getColor();
                    colorName = "#"+toHexString(color.getRed(), color.getGreen(), color.getBlue());
                    jobName = "§bBystander";
                    item.setCustomName(playerName+"\n"+
                                       "§7Color: "+colorName+"\n"+
                                       jobName+"\n"+
                                       "§7Weapons: §a"+(murder.havegun.get(e.getValue()) ? 1 : 0)+"\n"+
                                       "§7Emeralds: §a"+murder.emeralds.get(e.getValue())+"\n");
                    items.put(count, item);
                    teleportor.put(count,  e.getValue());
                }
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
                    if(!(game2 instanceof Game)) return;
                    if(game2.Players.containsValue(teleportor.get(slot))){
                        if(game2.getGameDataAsBoolean("gamenow2")){
                            this.close2();
                            Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackTeleportMurderTeleportSelectorChestInventory(this,teleportor.get(slot)), 5);
                        }
                    }
                }
            }
        }else{
            if(item.getId() == Item.AIR){
                if(teleportor.containsKey(slot)){
                    Game game2 = GameProvider.getPlayingGame(teleportor.get(slot));
                    if(!(game2 instanceof Game)) return;
                    if(game2.Players.containsValue(teleportor.get(slot))){
                        if(game2.getGameDataAsBoolean("gamenow2")){
                            this.close2();
                            Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackTeleportMurderTeleportSelectorChestInventory(this,teleportor.get(slot)), 5);
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
class CallbackOpenMurderTeleportSelectorChestInventory extends Task{

    public MurderTeleportSelectorChestInventory owner;

    public CallbackOpenMurderTeleportSelectorChestInventory(MurderTeleportSelectorChestInventory o){
        owner = o;
    }

    public void onRun(int d){
        owner.open();
    }
}
class CallbackTeleportMurderTeleportSelectorChestInventory extends Task{

    public MurderTeleportSelectorChestInventory owner;
    public Player target;

    public CallbackTeleportMurderTeleportSelectorChestInventory(MurderTeleportSelectorChestInventory o, Player t){
        owner = o;
        target = t;
    }

    public void onRun(int d){
        owner.tp(target);
    }
}
