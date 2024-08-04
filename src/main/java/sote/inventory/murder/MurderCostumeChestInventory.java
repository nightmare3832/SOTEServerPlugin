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
import sote.Main;
import sote.ServerItem;
import sote.inventory.DoubleServerChestInventory;
import sote.inventory.Inventorys;
import sote.inventory.ServerChestInventory;
import sote.murder.armor.Armors;
import sote.murder.hat.MurderHats;
import sote.murder.weapon.Weapons;
import sote.stat.Stat;

public class MurderCostumeChestInventory extends ServerChestInventory{

    public static final int CHEST_SIZE = 54;

    public final int HELMET_GLASS_INDEX = 11;
    public final int HELMET_INDEX = 12;
    public final int CHESTPLATE_INDEX = 21;
    public final int LEGGINGS_GLASS_INDEX = 29;
    public final int LEGGINGS_INDEX = 30;
    public final int BOOTS_GLASS_INDEX = 38;
    public final int BOOTS_INDEX = 39;
    public final int WEAPON_INDEX = 32;
    public final int RANDOM_INDEX = 33;
    public final int HATS_INDEX = 23;
    public final int HATS_BACK_INDEX = 18;
    public final int HATS_NEXT_INDEX = 26;
    public final int[] HATS_PAGE_INDEX = new int[]{
            10,11,12,13,14,15,16,
            19,20,21,22,23,24,25,
            28,29,30,31,32,33,34,
            37,38,39,40,41,42,43
    };

    public final int POCKET_HELMET_GLASS_INDEX = 20;
    public final int POCKET_HELMET_INDEX = 21;
    public final int POCKET_CHESTPLATE_INDEX = 27;
    public final int POCKET_LEGGINGS_GLASS_INDEX = 32;
    public final int POCKET_LEGGINGS_INDEX = 33;
    public final int POCKET_BOOTS_GLASS_INDEX = 38;
    public final int POCKET_BOOTS_INDEX = 39;
    public final int POCKET_WEAPON_INDEX = 9;
    public final int POCKET_RANDOM_INDEX = 10;
    public final int POCKET_HATS_INDEX = 7;
    public final int POCKET_HATS_BACK_INDEX = 1;
    public final int POCKET_HATS_NEXT_INDEX = 4;
    public final int[] POCKET_HATS_PAGE_INDEX = new int[]{
            6,7,8,9,10,11,12,
            13,14,15,16,17,18,
            19,20,21,22,23,24,
            25,26,27,28,29,30,
            31,32,33,34,35,36,
            37,38,39,40,41,42
    };

    public final String LETHER = "Lether";
    public final String STONE = "Stone";
    public final String CHAIN = "Chain";
    public final String IRON = "Iron";
    public final String GOLD = "Gold";
    public final String DIAMOND = "Diamond";

    public final int SCREEN_TOP = 0;
    public final int SCREEN_HATS = 1;
    public final int SCREEN_HELMET = 2;
    public final int SCREEN_LEGGINGS = 3;
    public final int SCREEN_BOOTS = 4;
    public final int SCREEN_WEAPON = 5;

    public final int HATS_PAGE_MAX = 28;
    public final int POCKET_HATS_PAGE_MAX = 36;

    public HashMap<Integer, Item> costumeItem = new HashMap<Integer, Item>();
    public HashMap<Integer, String> costumeHat = new HashMap<Integer, String>();
    //0: Helmet 1: Leggings 2: Boots 3: Weapon
    public int screen = 0;
    public int page = 1;

    public MurderCostumeChestInventory(Player player){
        super(player);
    }

    @Override
    public void register(){
        this.spawnChestBlock();
        this.SpawnBlockEntity();
        HashMap<Integer, Item> items = new HashMap<Integer, Item>();
        Item item;
        ItemColorArmor colorItem;
        item = Item.get(Item.GLASS_PANEL);
        item.setCustomName(Main.getMessage(player,"item.murder.costume.select.helmet.glass"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HELMET_GLASS_INDEX, item);
        else items.put(POCKET_HELMET_GLASS_INDEX, item);
        item = MurderHats.getItem(Stat.getMurderHat(player));
        if(item.getId() == 0){
            item = Armors.getSellectHelmet(player);
            if(item.getId() == Item.AIR){
                item = Item.get(Item.LEATHER_CAP);
                colorItem = (ItemColorArmor) item;
                colorItem.setColor(192, 192, 192);
            }
        }
        item.setCustomName(Main.getMessage(player,"item.murder.costume.select.helmet"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HELMET_INDEX, item);
        else items.put(POCKET_HELMET_INDEX, item);
        item = Item.get(Item.LEATHER_TUNIC);
        item.setCustomName(Main.getMessage(player,"item.murder.costume.select.chestplate"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(CHESTPLATE_INDEX, item);
        else items.put(POCKET_CHESTPLATE_INDEX, item);
        item = Item.get(Item.GLASS_PANEL);
        item.setCustomName(Main.getMessage(player,"item.murder.costume.select.leggings.glass"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(LEGGINGS_GLASS_INDEX, item);
        else items.put(POCKET_LEGGINGS_GLASS_INDEX, item);
        item = Armors.getSellectLeggings(player);
        if(item.getId() == Item.AIR){
            item = Item.get(Item.LEATHER_PANTS);
            colorItem = (ItemColorArmor) item;
            colorItem.setColor(192, 192, 192);
        }
        item.setCustomName(Main.getMessage(player,"item.murder.costume.select.leggings"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(LEGGINGS_INDEX, item);
        else items.put(POCKET_LEGGINGS_INDEX, item);
        item = Item.get(Item.GLASS_PANEL);
        item.setCustomName(Main.getMessage(player,"item.murder.costume.select.boots.glass"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(BOOTS_GLASS_INDEX, item);
        else items.put(POCKET_BOOTS_GLASS_INDEX, item);
        item = Armors.getSellectBoots(player);
        if(item.getId() == Item.AIR){
            item = Item.get(Item.LEATHER_BOOTS);
            colorItem = (ItemColorArmor) item;
            colorItem.setColor(192, 192, 192);
        }
        item.setCustomName(Main.getMessage(player,"item.murder.costume.select.boots"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(BOOTS_INDEX, item);
        else items.put(POCKET_BOOTS_INDEX, item);
        item = Item.get(Item.ENDER_CHEST);
        item.setCustomName(Main.getMessage(player,"item.murder.costume.select.hats"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HATS_INDEX, item);
        else items.put(POCKET_HATS_INDEX, item);
        item = Weapons.getSelectKnife(player).getItem(player);
        item.setCustomName(Main.getMessage(player,"item.murder.costume.select.weapon"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(WEAPON_INDEX, item);
        else items.put(POCKET_WEAPON_INDEX, item);
        item = ServerItem.getServerItemByString("Command_Block");
        item.setCustomName(Main.getMessage(player,"item.murder.costume.random.equipment"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(RANDOM_INDEX, item);
        else items.put(POCKET_RANDOM_INDEX, item);
        this.doubleinv = new DoubleServerChestInventory(items);
        this.screen = 0;
        this.page = 1;
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackOpenMurderCostumeChestInventory(this), 5);
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
        costumeItem = new HashMap<Integer, Item>();
        costumeHat = new HashMap<Integer, String>();
        Map<String, Boolean> mapWeapon = (Map<String, Boolean>) Weapons.weaponData.get(player.getName().toLowerCase());
        Map<String, Boolean> mapArmor = (Map<String, Boolean>) Armors.armorData.get(player.getName().toLowerCase());
        int count = 0;
        switch(this.screen){
            case SCREEN_TOP:
                Item item;
                ItemColorArmor colorItem;
                item = Item.get(Item.GLASS_PANEL);
                item.setCustomName(Main.getMessage(player,"item.murder.costume.select.helmet.glass"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HELMET_GLASS_INDEX, item);
                else items.put(POCKET_HELMET_GLASS_INDEX, item);
                item = MurderHats.getItem(Stat.getMurderHat(player));
                if(item.getId() == 0){
                    item = Armors.getSellectHelmet(player);
                    if(item.getId() == Item.AIR){
                        item = Item.get(Item.LEATHER_CAP);
                        colorItem = (ItemColorArmor) item;
                        colorItem.setColor(192, 192, 192);
                    }
                }
                item.setCustomName(Main.getMessage(player,"item.murder.costume.select.helmet"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HELMET_INDEX, item);
                else items.put(POCKET_HELMET_INDEX, item);
                item = Item.get(Item.LEATHER_TUNIC);
                item.setCustomName(Main.getMessage(player,"item.murder.costume.select.chestplate"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(CHESTPLATE_INDEX, item);
                else items.put(POCKET_CHESTPLATE_INDEX, item);
                item = Item.get(Item.GLASS_PANEL);
                item.setCustomName(Main.getMessage(player,"item.murder.costume.select.leggings.glass"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(LEGGINGS_GLASS_INDEX, item);
                else items.put(POCKET_LEGGINGS_GLASS_INDEX, item);
                item = Armors.getSellectLeggings(player);
                if(item.getId() == Item.AIR){
                    item = Item.get(Item.LEATHER_PANTS);
                    colorItem = (ItemColorArmor) item;
                    colorItem.setColor(192, 192, 192);
                }
                item.setCustomName(Main.getMessage(player,"item.murder.costume.select.leggings"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(LEGGINGS_INDEX, item);
                else items.put(POCKET_LEGGINGS_INDEX, item);
                item = Item.get(Item.GLASS_PANEL);
                item.setCustomName(Main.getMessage(player,"item.murder.costume.select.boots.glass"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(BOOTS_GLASS_INDEX, item);
                else items.put(POCKET_BOOTS_GLASS_INDEX, item);
                item = Armors.getSellectBoots(player);
                if(item.getId() == Item.AIR){
                    item = Item.get(Item.LEATHER_BOOTS);
                    colorItem = (ItemColorArmor) item;
                    colorItem.setColor(192, 192, 192);
                }
                item.setCustomName(Main.getMessage(player,"item.murder.costume.select.boots"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(BOOTS_INDEX, item);
                else items.put(POCKET_BOOTS_INDEX, item);
                item = Item.get(Item.ENDER_CHEST);
                item.setCustomName(Main.getMessage(player,"item.murder.costume.select.hats"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HATS_INDEX, item);
                else items.put(POCKET_HATS_INDEX, item);
                item = Weapons.getSelectKnife(player).getItem(player);
                item.setCustomName(Main.getMessage(player,"item.murder.costume.select.weapon"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(WEAPON_INDEX, item);
                else items.put(POCKET_WEAPON_INDEX, item);
                item = ServerItem.getServerItemByString("Command_Block");
                item.setCustomName(Main.getMessage(player,"item.murder.costume.random.equipment"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(RANDOM_INDEX, item);
                else items.put(POCKET_RANDOM_INDEX, item);
                this.page = 1;
            break;
            case SCREEN_HATS:
                Map<String, Boolean> mapHat = (Map<String, Boolean>) MurderHats.hatData.get(player.getName().toLowerCase());
                item = Item.get(Item.ARROW,0,1);
                item.setCustomName(Main.getMessage(player,"item.murder.shop.hats.backpage"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HATS_BACK_INDEX, item);
                else items.put(POCKET_HATS_BACK_INDEX, item);
                boolean containsNextPage = false;
                count = 0;
                int pageMax;
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) pageMax = HATS_PAGE_MAX;
                else pageMax = POCKET_HATS_PAGE_MAX;
                for (Map.Entry<String,Item> e : MurderHats.item.entrySet()){
                    if(count >= (pageMax * (this.page - 1)) && count < (pageMax * this.page)){
                        if(mapHat.get(e.getKey())){
                            item = e.getValue();
                            item.setCustomName(MurderHats.getCostumeName(player, e.getKey()));
                            if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC){
                                items.put(HATS_PAGE_INDEX[count - (pageMax * (this.page - 1))], item);
                                costumeHat.put(HATS_PAGE_INDEX[count - (pageMax * (this.page - 1))], e.getKey());
                            }else{
                                items.put(POCKET_HATS_PAGE_INDEX[count - (pageMax * (this.page - 1))], item);
                                costumeHat.put(POCKET_HATS_PAGE_INDEX[count - (pageMax * (this.page - 1))], e.getKey());
                            }
                            count++;
                        }
                    }else if(count >= (pageMax * this.page)){
                        containsNextPage = true;
                    }
                }
                if(containsNextPage){
                    item = Item.get(Item.ARROW,0,1);
                    item.setCustomName(Main.getMessage(player,"item.murder.shop.hats.nextpage"));
                    if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HATS_NEXT_INDEX, item);
                    else items.put(POCKET_HATS_NEXT_INDEX, item);
                }
            break;
            case SCREEN_HELMET:
                count = 0;
                item = Item.get(Item.ARROW,0,1);
                item.setCustomName(Main.getMessage(player,"item.murder.shop.helmet.backpage"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HATS_BACK_INDEX, item);
                else items.put(POCKET_HATS_BACK_INDEX, item);
                if(mapArmor.get("298:0")){
                    item = Item.get(Item.LEATHER_CAP,0,1);
                    if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HATS_PAGE_INDEX[count], item);
                    else items.put(POCKET_HATS_PAGE_INDEX[count], item);
                    count++;
                }
                if(mapArmor.get("302:0")){
                    item = Item.get(Item.CHAIN_HELMET,0,1);
                    if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HATS_PAGE_INDEX[count], item);
                    else items.put(POCKET_HATS_PAGE_INDEX[count], item);
                    count++;
                }
                if(mapArmor.get("306:0")){
                    item = Item.get(Item.IRON_HELMET,0,1);
                    if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HATS_PAGE_INDEX[count], item);
                    else items.put(POCKET_HATS_PAGE_INDEX[count], item);
                    count++;
                }
                if(mapArmor.get("314:0")){
                    item = Item.get(Item.GOLD_HELMET,0,1);
                    if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HATS_PAGE_INDEX[count], item);
                    else items.put(POCKET_HATS_PAGE_INDEX[count], item);
                    count++;
                }
                if(mapArmor.get("310:0")){
                    item = Item.get(Item.DIAMOND_HELMET,0,1);
                    if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HATS_PAGE_INDEX[count], item);
                    else items.put(POCKET_HATS_PAGE_INDEX[count], item);
                    count++;
                }
                costumeItem.putAll(items);
            break;
            case SCREEN_LEGGINGS:
                count = 0;
                item = Item.get(Item.ARROW,0,1);
                item.setCustomName(Main.getMessage(player,"item.murder.shop.leggings.backpage"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HATS_BACK_INDEX, item);
                else items.put(POCKET_HATS_BACK_INDEX, item);
                if(mapArmor.get("300:0")){
                    item = Item.get(Item.LEATHER_PANTS,0,1);
                    if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HATS_PAGE_INDEX[count], item);
                    else items.put(POCKET_HATS_PAGE_INDEX[count], item);
                    count++;
                }
                if(mapArmor.get("304:0")){
                    item = Item.get(Item.CHAIN_LEGGINGS,0,1);
                    if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HATS_PAGE_INDEX[count], item);
                    else items.put(POCKET_HATS_PAGE_INDEX[count], item);
                    count++;
                }
                if(mapArmor.get("308:0")){
                    item = Item.get(Item.IRON_LEGGINGS,0,1);
                    if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HATS_PAGE_INDEX[count], item);
                    else items.put(POCKET_HATS_PAGE_INDEX[count], item);
                    count++;
                }
                if(mapArmor.get("316:0")){
                    item = Item.get(Item.GOLD_LEGGINGS,0,1);
                    if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HATS_PAGE_INDEX[count], item);
                    else items.put(POCKET_HATS_PAGE_INDEX[count], item);
                    count++;
                }
                if(mapArmor.get("312:0")){
                    item = Item.get(Item.DIAMOND_LEGGINGS,0,1);
                    if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HATS_PAGE_INDEX[count], item);
                    else items.put(POCKET_HATS_PAGE_INDEX[count], item);
                    count++;
                }
                costumeItem.putAll(items);
            break;
            case SCREEN_BOOTS:
                count = 0;
                item = Item.get(Item.ARROW,0,1);
                item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.backpage"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HATS_BACK_INDEX, item);
                else items.put(POCKET_HATS_BACK_INDEX, item);
                if(mapArmor.get("301:0")){
                    item = Item.get(Item.LEATHER_BOOTS,0,1);
                    if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HATS_PAGE_INDEX[count], item);
                    else items.put(POCKET_HATS_PAGE_INDEX[count], item);
                    count++;
                }
                if(mapArmor.get("305:0")){
                    item = Item.get(Item.CHAIN_BOOTS,0,1);
                    if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HATS_PAGE_INDEX[count], item);
                    else items.put(POCKET_HATS_PAGE_INDEX[count], item);
                    count++;
                }
                if(mapArmor.get("309:0")){
                    item = Item.get(Item.IRON_BOOTS,0,1);
                    if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HATS_PAGE_INDEX[count], item);
                    else items.put(POCKET_HATS_PAGE_INDEX[count], item);
                    count++;
                }
                if(mapArmor.get("317:0")){
                    item = Item.get(Item.GOLD_BOOTS,0,1);
                    if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HATS_PAGE_INDEX[count], item);
                    else items.put(POCKET_HATS_PAGE_INDEX[count], item);
                    count++;
                }
                if(mapArmor.get("313:0")){
                    item = Item.get(Item.DIAMOND_BOOTS,0,1);
                    if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HATS_PAGE_INDEX[count], item);
                    else items.put(POCKET_HATS_PAGE_INDEX[count], item);
                    count++;
                }
                costumeItem.putAll(items);
            break;
            case SCREEN_WEAPON:
                count = 0;
                item = Item.get(Item.ARROW,0,1);
                item.setCustomName(Main.getMessage(player,"item.murder.shop.weapon.backpage"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HATS_BACK_INDEX, item);
                else items.put(POCKET_HATS_BACK_INDEX, item);
                item = Item.get(Item.WOODEN_SWORD);
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HATS_PAGE_INDEX[count], item);
                else items.put(POCKET_HATS_PAGE_INDEX[count], item);
                count++;//TODO ItemNames
                if(mapWeapon.get("Stone")){
                    item = Item.get(Item.STONE_SWORD,0,1);
                    if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HATS_PAGE_INDEX[count], item);
                    else items.put(POCKET_HATS_PAGE_INDEX[count], item);
                    count++;
                }
                if(mapWeapon.get("Iron")){
                    item = Item.get(Item.IRON_SWORD,0,1);
                    if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HATS_PAGE_INDEX[count], item);
                    else items.put(POCKET_HATS_PAGE_INDEX[count], item);
                    count++;
                }
                if(mapWeapon.get("Gold")){
                    item = Item.get(Item.GOLD_SWORD,0,1);
                    if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HATS_PAGE_INDEX[count], item);
                    else items.put(POCKET_HATS_PAGE_INDEX[count], item);
                    count++;
                }
                if(mapWeapon.get("Diamond")){
                    item = Item.get(Item.DIAMOND_SWORD,0,1);
                    if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HATS_PAGE_INDEX[count], item);
                    else items.put(POCKET_HATS_PAGE_INDEX[count], item);
                    count++;
                }
                costumeItem.putAll(items);
            break;
        }
        doubleinv.setContents(items);
        sendContents();
        Inventorys.data2.get(player).register(player);
    }

    public void randomEquipment(){
        int arrayCount = 0;
        HashMap<String, String> mapHats = new HashMap<String, String>();
        Map<String, Boolean> mapHat = (Map<String, Boolean>) MurderHats.hatData.get(player.getName().toLowerCase());
        Map<String, Boolean> mapArmor = (Map<String, Boolean>) Armors.armorData.get(player.getName().toLowerCase());
        Map<String, Boolean> mapWeapon = (Map<String, Boolean>) Weapons.weaponData.get(player.getName().toLowerCase());
        mapHats.put("unknown", "unknown");
        for (Map.Entry<String,Item> e : MurderHats.item.entrySet()){
            if(mapHat.get(e.getKey())){
                mapHats.put(e.getKey(), e.getKey());
                arrayCount++;
            }
        }
        String[] keys = mapHats.keySet().toArray(new String[mapHats.size()]);
        String value = mapHats.get(keys[((int)(Math.random()*keys.length))]);
        Stat.setMurderHat(player, value);
        arrayCount = 0;
        HashMap<String, String> mapHelmets = new HashMap<String, String>();
        mapHelmets.put("0:0",  "0:0");
        arrayCount++;
        if(mapArmor.get("298:0")){
            mapHelmets.put("298:0",  "298:0");
            arrayCount++;
        }
        if(mapArmor.get("302:0")){
            mapHelmets.put("302:0",  "302:0");
            arrayCount++;
        }
        if(mapArmor.get("306:0")){
            mapHelmets.put("306:0",  "306:0");
            arrayCount++;
        }
        if(mapArmor.get("314:0")){
            mapHelmets.put("314:0",  "314:0");
            arrayCount++;
        }
        if(mapArmor.get("310:0")){
            mapHelmets.put("310:0",  "310:0");
            arrayCount++;
        }
        keys = mapHelmets.keySet().toArray(new String[mapHelmets.size()]);
        value = mapHelmets.get(keys[((int)(Math.random()*keys.length))]);
        Armors.setSellectHelmet(player, value);
        arrayCount = 0;
        HashMap<String, String> mapLeggings = new HashMap<String, String>();
        mapLeggings.put("0:0",  "0:0");
        arrayCount++;
        if(mapArmor.get("300:0")){
            mapLeggings.put("300:0",  "300:0");
            arrayCount++;
        }
        if(mapArmor.get("304:0")){
            mapLeggings.put("304:0",  "304:0");
            arrayCount++;
        }
        if(mapArmor.get("308:0")){
            mapLeggings.put("308:0",  "308:0");
            arrayCount++;
        }
        if(mapArmor.get("316:0")){
            mapLeggings.put("316:0",  "316:0");
            arrayCount++;
        }
        if(mapArmor.get("312:0")){
            mapLeggings.put("312:0",  "312:0");
            arrayCount++;
        }
        keys = mapLeggings.keySet().toArray(new String[mapLeggings.size()]);
        value = mapLeggings.get(keys[((int)(Math.random()*keys.length))]);
        Armors.setSellectLeggings(player, value);
        arrayCount = 0;
        HashMap<String, String> mapBoots = new HashMap<String, String>();
        mapBoots.put("0:0",  "0:0");
        arrayCount++;
        if(mapArmor.get("301:0")){
            mapBoots.put("301:0",  "301:0");
            arrayCount++;
        }
        if(mapArmor.get("305:0")){
            mapBoots.put("305:0",  "305:0");
            arrayCount++;
        }
        if(mapArmor.get("309:0")){
            mapBoots.put("309:0",  "309:0");
            arrayCount++;
        }
        if(mapArmor.get("317:0")){
            mapBoots.put("317:0",  "317:0");
            arrayCount++;
        }
        if(mapArmor.get("313:0")){
            mapBoots.put("313:0",  "313:0");
            arrayCount++;
        }
        keys = mapBoots.keySet().toArray(new String[mapBoots.size()]);
        value = mapBoots.get(keys[((int)(Math.random()*keys.length))]);
        Armors.setSellectBoots(player, value);
        arrayCount = 0;
        HashMap<String, String> mapWeaponss = new HashMap<String, String>();
        mapWeaponss.put("Wooden", "Wooden");
        arrayCount++;
        if(mapWeapon.get("Stone")){
            mapWeaponss.put("Stone", "Stone");
            arrayCount++;
        }
        if(mapWeapon.get("Iron")){
            mapWeaponss.put("Iron", "Stone");
            arrayCount++;
        }
        if(mapWeapon.get("Gold")){
            mapWeaponss.put("Gold", "Gold");
            arrayCount++;
        }
        if(mapWeapon.get("Diamond")){
            mapWeaponss.put("Diamond", "Diamond");
            arrayCount++;
        }
        keys = mapWeaponss.keySet().toArray(new String[mapWeaponss.size()]);
        value = mapWeaponss.get(keys[((int)(Math.random()*keys.length))]);
        Weapons.setSelectWeapon(player, value);
        update();
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
                switch(this.screen){//TODO
                    case SCREEN_TOP:
                        switch(slot){
                            case HELMET_INDEX:
                                this.screen = SCREEN_HELMET;
                            break;
                            case HELMET_GLASS_INDEX:
                                Armors.setSellectHelmet(player, "0:0");
                            break;
                            case LEGGINGS_INDEX:
                                this.screen = SCREEN_LEGGINGS;
                            break;
                            case LEGGINGS_GLASS_INDEX:
                                Armors.setSellectLeggings(player, "0:0");
                            break;
                            case BOOTS_INDEX:
                                this.screen = SCREEN_BOOTS;
                            break;
                            case BOOTS_GLASS_INDEX:
                                Armors.setSellectBoots(player, "0:0");
                            break;
                            case WEAPON_INDEX:
                                this.screen = SCREEN_WEAPON;
                            break;
                            case HATS_INDEX:
                                this.screen = SCREEN_HATS;
                            break;
                            case RANDOM_INDEX:
                                this.randomEquipment();
                            break;
                        }
                    break;
                    case SCREEN_HATS:
                        switch(slot){
                            case HATS_BACK_INDEX:
                                if(this.page == 1){
                                    this.screen = SCREEN_TOP;
                                    this.page = 1;
                                }else{
                                    this.page--;
                                }
                            break;
                            case HATS_NEXT_INDEX:
                                this.page++;
                            break;
                            default:
                                Map<String, Boolean> mapHat = (Map<String, Boolean>) MurderHats.hatData.get(player.getName().toLowerCase());
                                if(costumeHat.containsKey(slot)){
                                    if(mapHat.get(costumeHat.get(slot))){
                                        Stat.setMurderHat(player, costumeHat.get(slot));
                                        //player.sendMessage(Main.getMessage(player, "murder.hats.set"));
                                    }
                                }
                            break;
                        }
                    break;
                    case SCREEN_HELMET:
                        switch(slot){
                            case HATS_BACK_INDEX:
                                this.screen = SCREEN_TOP;
                            break;
                            default:
                                if(costumeItem.containsKey(slot)){
                                    Item itemm = costumeItem.get(slot);
                                    String itemString = itemm.getId()+":"+itemm.getDamage();
                                    Armors.setSellectHelmet(player, itemString);
                                    //player.sendMessage(Main.getMessage(player, "murder.hats.set"));
                                }
                            break;
                        }
                    break;
                    case SCREEN_LEGGINGS:
                        switch(slot){
                            case HATS_BACK_INDEX:
                                this.screen = SCREEN_TOP;
                            break;
                            default:
                                if(costumeItem.containsKey(slot)){
                                    Item itemm = costumeItem.get(slot);
                                    String itemString = itemm.getId()+":"+itemm.getDamage();
                                    Armors.setSellectLeggings(player, itemString);
                                    //player.sendMessage(Main.getMessage(player, "murder.hats.set"));
                                }
                            break;
                        }
                    break;
                    case SCREEN_BOOTS:
                        switch(slot){
                            case HATS_BACK_INDEX:
                                this.screen = SCREEN_TOP;
                            break;
                            default:
                                if(costumeItem.containsKey(slot)){
                                    Item itemm = costumeItem.get(slot);
                                    String itemString = itemm.getId()+":"+itemm.getDamage();
                                    Armors.setSellectBoots(player, itemString);
                                    //player.sendMessage(Main.getMessage(player, "murder.hats.set"));
                                }
                            break;
                        }
                    break;
                    case SCREEN_WEAPON:
                        switch(slot){
                            case HATS_BACK_INDEX:
                                this.screen = SCREEN_TOP;
                            break;
                            default:
                                if(costumeItem.containsKey(slot)){
                                    String buyString = "Wooden";
                                    Item itemm = costumeItem.get(slot);
                                    switch(itemm.getId()){
                                        case Item.WOODEN_SWORD:
                                            buyString = "Wooden";
                                        break;
                                        case Item.STONE_SWORD:
                                            buyString = "Stone";
                                        break;
                                        case Item.IRON_SWORD:
                                            buyString = "Iron";
                                        break;
                                        case Item.GOLD_SWORD:
                                            buyString = "Gold";
                                        break;
                                        case Item.DIAMOND_SWORD:
                                            buyString = "Diamond";
                                        break;
                                    }
                                    Weapons.setSelectWeapon(player, buyString);
                                    //player.sendMessage(Main.getMessage(player, "murder.hats.set"));
                                }
                            break;
                        }
                    break;
                }
                update();
            }
        }else{
            if(item.getId() == Item.AIR){
                switch(this.screen){//TODO
                    case SCREEN_TOP:
                        switch(slot){
                            case POCKET_HELMET_INDEX:
                                this.screen = SCREEN_HELMET;
                            break;
                            case POCKET_HELMET_GLASS_INDEX:
                                Armors.setSellectHelmet(player, "0:0");
                            break;
                            case POCKET_LEGGINGS_INDEX:
                                this.screen = SCREEN_LEGGINGS;
                            break;
                            case POCKET_LEGGINGS_GLASS_INDEX:
                                Armors.setSellectLeggings(player, "0:0");
                            break;
                            case POCKET_BOOTS_INDEX:
                                this.screen = SCREEN_BOOTS;
                            break;
                            case POCKET_BOOTS_GLASS_INDEX:
                                Armors.setSellectBoots(player, "0:0");
                            break;
                            case POCKET_WEAPON_INDEX:
                                this.screen = SCREEN_WEAPON;
                            break;
                            case POCKET_HATS_INDEX:
                                this.screen = SCREEN_HATS;
                            break;
                            case POCKET_RANDOM_INDEX:
                                this.randomEquipment();
                            break;
                        }
                    break;
                    case SCREEN_HATS:
                        switch(slot){
                            case POCKET_HATS_BACK_INDEX:
                                if(this.page == 1){
                                    this.screen = SCREEN_TOP;
                                    this.page = 1;
                                }else{
                                    this.page--;
                                }
                            break;
                            case POCKET_HATS_NEXT_INDEX:
                                this.page++;
                            break;
                            default:
                                Map<String, Boolean> mapHat = (Map<String, Boolean>) MurderHats.hatData.get(player.getName().toLowerCase());
                                if(costumeHat.containsKey(slot)){
                                    if(mapHat.get(costumeHat.get(slot))){
                                        Stat.setMurderHat(player, costumeHat.get(slot));
                                        //player.sendMessage(Main.getMessage(player, "murder.hats.set"));
                                    }
                                }
                            break;
                        }
                    break;
                    case SCREEN_HELMET:
                        switch(slot){
                            case POCKET_HATS_BACK_INDEX:
                                this.screen = SCREEN_TOP;
                            break;
                            default:
                                if(costumeItem.containsKey(slot)){
                                    Item itemm = costumeItem.get(slot);
                                    String itemString = itemm.getId()+":"+itemm.getDamage();
                                    Armors.setSellectHelmet(player, itemString);
                                    //player.sendMessage(Main.getMessage(player, "murder.hats.set"));
                                }
                            break;
                        }
                    break;
                    case SCREEN_LEGGINGS:
                        switch(slot){
                            case POCKET_HATS_BACK_INDEX:
                                this.screen = SCREEN_TOP;
                            break;
                            default:
                                if(costumeItem.containsKey(slot)){
                                    Item itemm = costumeItem.get(slot);
                                    String itemString = itemm.getId()+":"+itemm.getDamage();
                                    Armors.setSellectLeggings(player, itemString);
                                    //player.sendMessage(Main.getMessage(player, "murder.hats.set"));
                                }
                            break;
                        }
                    break;
                    case SCREEN_BOOTS:
                        switch(slot){
                            case POCKET_HATS_BACK_INDEX:
                                this.screen = SCREEN_TOP;
                            break;
                            default:
                                if(costumeItem.containsKey(slot)){
                                    Item itemm = costumeItem.get(slot);
                                    String itemString = itemm.getId()+":"+itemm.getDamage();
                                    Armors.setSellectBoots(player, itemString);
                                    //player.sendMessage(Main.getMessage(player, "murder.hats.set"));
                                }
                            break;
                        }
                    break;
                    case SCREEN_WEAPON:
                        switch(slot){
                            case POCKET_HATS_BACK_INDEX:
                                this.screen = SCREEN_TOP;
                            break;
                            default:
                                if(costumeItem.containsKey(slot)){
                                    String buyString = "Wooden";
                                    Item itemm = costumeItem.get(slot);
                                    switch(itemm.getId()){
                                        case Item.WOODEN_SWORD:
                                            buyString = "Wooden";
                                        break;
                                        case Item.STONE_SWORD:
                                            buyString = "Stone";
                                        break;
                                        case Item.IRON_SWORD:
                                            buyString = "Iron";
                                        break;
                                        case Item.GOLD_SWORD:
                                            buyString = "Gold";
                                        break;
                                        case Item.DIAMOND_SWORD:
                                            buyString = "Diamond";
                                        break;
                                    }
                                    Weapons.setSelectWeapon(player, buyString);
                                    //player.sendMessage(Main.getMessage(player, "murder.hats.set"));
                                }
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
class CallbackOpenMurderCostumeChestInventory extends Task{

    public MurderCostumeChestInventory owner;

    public CallbackOpenMurderCostumeChestInventory(MurderCostumeChestInventory o){
        owner = o;
    }

    public void onRun(int d){
        owner.open();
    }
}
