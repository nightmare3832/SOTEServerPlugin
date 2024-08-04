package sote.inventory.murder;

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
import sote.murder.armor.Armors;
import sote.murder.hat.MurderHats;
import sote.murder.upgrade.MurderUpgrades;
import sote.murder.weapon.Weapons;
import sote.stat.Stat;

public class MurderShopChestInventory extends ServerChestInventory{

    public static final int CHEST_SIZE = 54;

    public final int PURCHASE_HATS_INDEX = 11;
    public final int PURCHASE_UPGRADES_INDEX = 12;
    public final int COIN_INDEX = 15;
    public final int HELMET_INDEX = 29;
    public final int LEGGINGS_INDEX = 30;
    public final int BOOTS_INDEX = 31;
    public final int WEAPON_INDEX = 33;
    public final int HATS_BACK_INDEX = 18;
    public final int HATS_NEXT_INDEX = 26;
    public final int HATS_COIN_INDEX = 49;
    public final int[] HATS_PAGE_INDEX = new int[]{
            10,11,12,13,14,15,16,
            19,20,21,22,23,24,25,
            28,29,30,31,32,33,34,
            37,38,39,40,41,42,43
    };
    public final int CONFIRM_HAT_INDEX = 22;
    public final int UPGRADES_BACK_INDEX = 9;
    public final int UPGRADES_COIN_INDEX = 49;
    public final int UPGRADES_EMERALD_INDEX = 11;
    public final int UPGRADES_GUN_INDEX = 12;
    public final int UPGRADES_KNIFE_INDEX = 13;

    public final int POCKET_PURCHASE_HATS_INDEX = 7;
    public final int POCKET_PURCHASE_UPGRADES_INDEX = 8;
    public final int POCKET_COIN_INDEX = 10;
    public final int POCKET_HELMET_INDEX = 19;
    public final int POCKET_LEGGINGS_INDEX = 20;
    public final int POCKET_BOOTS_INDEX = 21;
    public final int POCKET_WEAPON_INDEX = 23;
    public final int POCKET_HATS_BACK_INDEX = 1;
    public final int POCKET_HATS_NEXT_INDEX = 4;
    public final int POCKET_HATS_COIN_INDEX = 5;
    public final int[] POCKET_HATS_PAGE_INDEX = new int[]{
            6,7,8,9,10,11,
            12,13,14,15,16,17,
            18,19,20,21,22,23,
            24,25,26,27,28,29,
            30,31,32,33,34,35,
            36,37,38,39,40,41
    };
    public final int POCKET_CONFIRM_HAT_INDEX = 2;
    public final int POCKET_UPGRADES_BACK_INDEX = 1;
    public final int POCKET_UPGRADES_COIN_INDEX = 5;
    public final int POCKET_UPGRADES_EMERALD_INDEX = 11;
    public final int POCKET_UPGRADES_GUN_INDEX = 12;
    public final int POCKET_UPGRADES_KNIFE_INDEX = 13;

    public final String LETHER = "Lether";
    public final String STONE = "Stone";
    public final String CHAIN = "Chain";
    public final String IRON = "Iron";
    public final String GOLD = "Gold";
    public final String DIAMOND = "Diamond";

    public final int SCREEN_TOP = 0;
    public final int SCREEN_HATS = 1;
    public final int SCREEN_CONFIRM_HAT = 2;
    public final int SCREEN_UPGRADES = 3;

    public final int HATS_PAGE_MAX = 28;
    public final int POCKET_HATS_PAGE_MAX = 36;

    public String confirmHat;
    public HashMap<Integer, Item> shopItem = new HashMap<Integer, Item>();
    public HashMap<Integer, String> shopHat = new HashMap<Integer, String>();
    //0: Helmet 1: Leggings 2: Boots 3: Weapon
    public int screen = 0;
    public int page = 1;

    public MurderShopChestInventory(Player player){
        super(player);
    }

    @Override
    public void register(){
        this.spawnChestBlock();
        this.SpawnBlockEntity();
        HashMap<Integer, Item> items = new HashMap<Integer, Item>();
        Map<String, Boolean> mapWeapon = (Map<String, Boolean>) Weapons.weaponData.get(player.getName().toLowerCase());
        Map<String, Boolean> mapArmor = (Map<String, Boolean>) Armors.armorData.get(player.getName().toLowerCase());
        Item item;
        item = Item.get(Item.ENDER_CHEST,0,1);
        item.setCustomName(Main.getMessage(player,"item.murder.shop.purchase.hats"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(PURCHASE_HATS_INDEX, item);
        else items.put(POCKET_PURCHASE_HATS_INDEX, item);
        item = Item.get(Item.REDSTONE_DUST,0,1);
        item.setCustomName(Main.getMessage(player,"item.murder.shop.purchase.upgrades"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(PURCHASE_UPGRADES_INDEX, item);
        else items.put(POCKET_PURCHASE_UPGRADES_INDEX, item);
        int coin = Stat.getCoin(player);
        item = Item.get(Item.EMERALD,0,1);
        item.setCustomName(Main.getMessage(player,"item.murder.shop.coin", new String[]{String.valueOf(coin)}));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(COIN_INDEX, item);
        else items.put(POCKET_COIN_INDEX, item);
        item = Item.get(Item.LEATHER_CAP,0,1);
        if(coin < Armors.PRICE_LETHER) item.setCustomName(Main.getMessage(player,"item.murder.shop.helmet.no", new String[]{LETHER, String.valueOf(Armors.PRICE_LETHER), String.valueOf(Armors.PRICE_LETHER - coin)}));
        else item.setCustomName(Main.getMessage(player,"item.murder.shop.helmet.yes", new String[]{LETHER, String.valueOf(Armors.PRICE_LETHER)}));
        if(mapArmor.get("298:0")){
            item = Item.get(Item.CHAIN_HELMET,0,1);
            if(coin < Armors.PRICE_CHAIN) item.setCustomName(Main.getMessage(player,"item.murder.shop.helmet.no", new String[]{CHAIN, String.valueOf(Armors.PRICE_CHAIN), String.valueOf(Armors.PRICE_CHAIN - coin)}));
            else item.setCustomName(Main.getMessage(player,"item.murder.shop.helmet.yes", new String[]{CHAIN, String.valueOf(Armors.PRICE_CHAIN)}));
        }
        if(mapArmor.get("302:0")){
            item = Item.get(Item.IRON_HELMET,0,1);
            if(coin < Armors.PRICE_IRON) item.setCustomName(Main.getMessage(player,"item.murder.shop.helmet.no", new String[]{IRON, String.valueOf(Armors.PRICE_IRON), String.valueOf(Armors.PRICE_IRON - coin)}));
            else item.setCustomName(Main.getMessage(player,"item.murder.shop.helmet.yes", new String[]{IRON, String.valueOf(Armors.PRICE_IRON)}));
        }
        if(mapArmor.get("306:0")){
            item = Item.get(Item.GOLD_HELMET,0,1);
            if(coin < Armors.PRICE_GOLD) item.setCustomName(Main.getMessage(player,"item.murder.shop.helmet.no", new String[]{GOLD, String.valueOf(Armors.PRICE_GOLD), String.valueOf(Armors.PRICE_GOLD - coin)}));
            else item.setCustomName(Main.getMessage(player,"item.murder.shop.helmet.yes", new String[]{GOLD, String.valueOf(Armors.PRICE_GOLD)}));
        }
        if(mapArmor.get("314:0")){
            item = Item.get(Item.DIAMOND_HELMET,0,1);
            if(coin < Armors.PRICE_DIAMOND) item.setCustomName(Main.getMessage(player,"item.murder.shop.helmet.no", new String[]{DIAMOND, String.valueOf(Armors.PRICE_DIAMOND), String.valueOf(Armors.PRICE_DIAMOND - coin)}));
            else item.setCustomName(Main.getMessage(player,"item.murder.shop.helmet.yes", new String[]{DIAMOND, String.valueOf(Armors.PRICE_DIAMOND)}));
        }
        if(mapArmor.get("310:0")){
            item = Item.get(Item.STONE,0,1);
            item.setCustomName(Main.getMessage(player,"item.murder.shop.helmet.noo"));
        }
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HELMET_INDEX, item);
        else items.put(POCKET_HELMET_INDEX, item);
        shopItem.put(0, item);
        item = Item.get(Item.LEATHER_PANTS,0,1);
        if(coin < Armors.PRICE_LETHER) item.setCustomName(Main.getMessage(player,"item.murder.shop.leggings.no", new String[]{LETHER, String.valueOf(Armors.PRICE_LETHER), String.valueOf(Armors.PRICE_LETHER - coin)}));
        else item.setCustomName(Main.getMessage(player,"item.murder.shop.leggings.yes", new String[]{LETHER, String.valueOf(Armors.PRICE_LETHER)}));
        if(mapArmor.get("300:0")){
            item = Item.get(Item.CHAIN_LEGGINGS,0,1);
            if(coin < Armors.PRICE_CHAIN) item.setCustomName(Main.getMessage(player,"item.murder.shop.leggings.no", new String[]{CHAIN, String.valueOf(Armors.PRICE_CHAIN), String.valueOf(Armors.PRICE_CHAIN - coin)}));
            else item.setCustomName(Main.getMessage(player,"item.murder.shop.leggings.yes", new String[]{CHAIN, String.valueOf(Armors.PRICE_CHAIN)}));
        }
        if(mapArmor.get("304:0")){
            item = Item.get(Item.IRON_LEGGINGS,0,1);
            if(coin < Armors.PRICE_IRON) item.setCustomName(Main.getMessage(player,"item.murder.shop.leggings.no", new String[]{IRON, String.valueOf(Armors.PRICE_IRON), String.valueOf(Armors.PRICE_IRON - coin)}));
            else item.setCustomName(Main.getMessage(player,"item.murder.shop.leggings.yes", new String[]{IRON, String.valueOf(Armors.PRICE_IRON)}));
        }
        if(mapArmor.get("308:0")){
            item = Item.get(Item.GOLD_LEGGINGS,0,1);
            if(coin < Armors.PRICE_GOLD) item.setCustomName(Main.getMessage(player,"item.murder.shop.leggings.no", new String[]{GOLD, String.valueOf(Armors.PRICE_GOLD), String.valueOf(Armors.PRICE_GOLD - coin)}));
            else item.setCustomName(Main.getMessage(player,"item.murder.shop.leggings.yes", new String[]{GOLD, String.valueOf(Armors.PRICE_GOLD)}));
        }
        if(mapArmor.get("316:0")){
            item = Item.get(Item.DIAMOND_LEGGINGS,0,1);
            if(coin < Armors.PRICE_DIAMOND) item.setCustomName(Main.getMessage(player,"item.murder.shop.leggings.no", new String[]{DIAMOND, String.valueOf(Armors.PRICE_DIAMOND), String.valueOf(Armors.PRICE_DIAMOND - coin)}));
            else item.setCustomName(Main.getMessage(player,"item.murder.shop.leggings.yes", new String[]{DIAMOND, String.valueOf(Armors.PRICE_DIAMOND)}));
        }
        if(mapArmor.get("312:0")){
            item = Item.get(Item.STONE,0,1);
            item.setCustomName(Main.getMessage(player,"item.murder.shop.leggings.noo"));
        }
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(LEGGINGS_INDEX, item);
        else items.put(POCKET_LEGGINGS_INDEX, item);
        shopItem.put(1, item);
        item = Item.get(Item.LEATHER_BOOTS,0,1);
        if(coin < Armors.PRICE_LETHER) item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.no", new String[]{LETHER, String.valueOf(Armors.PRICE_LETHER), String.valueOf(Armors.PRICE_LETHER - coin)}));
        else item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.yes", new String[]{LETHER, String.valueOf(Armors.PRICE_LETHER)}));
        if(mapArmor.get("301:0")){
            item = Item.get(Item.CHAIN_BOOTS,0,1);
            if(coin < Armors.PRICE_CHAIN) item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.no", new String[]{CHAIN, String.valueOf(Armors.PRICE_CHAIN), String.valueOf(Armors.PRICE_CHAIN - coin)}));
            else item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.yes", new String[]{CHAIN, String.valueOf(Armors.PRICE_CHAIN)}));
        }
        if(mapArmor.get("305:0")){
            item = Item.get(Item.IRON_BOOTS,0,1);
            if(coin < Armors.PRICE_IRON) item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.no", new String[]{IRON, String.valueOf(Armors.PRICE_IRON), String.valueOf(Armors.PRICE_IRON - coin)}));
            else item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.yes", new String[]{IRON, String.valueOf(Armors.PRICE_IRON)}));
        }
        if(mapArmor.get("309:0")){
            item = Item.get(Item.GOLD_BOOTS,0,1);
            if(coin < Armors.PRICE_GOLD) item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.no", new String[]{GOLD, String.valueOf(Armors.PRICE_GOLD), String.valueOf(Armors.PRICE_GOLD - coin)}));
            else item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.yes", new String[]{GOLD, String.valueOf(Armors.PRICE_GOLD)}));
        }
        if(mapArmor.get("317:0")){
            item = Item.get(Item.DIAMOND_BOOTS,0,1);
            if(coin < Armors.PRICE_DIAMOND) item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.no", new String[]{DIAMOND, String.valueOf(Armors.PRICE_DIAMOND), String.valueOf(Armors.PRICE_DIAMOND - coin)}));
            else item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.yes", new String[]{DIAMOND, String.valueOf(Armors.PRICE_DIAMOND)}));
        }
        if(mapArmor.get("313:0")){
            item = Item.get(Item.STONE,0,1);
            item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.noo"));
        }
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(BOOTS_INDEX, item);
        else items.put(POCKET_BOOTS_INDEX, item);
        shopItem.put(2, item);
        item = Item.get(Item.STONE_SWORD,0,1);
        if(coin < Weapons.PRICE_STONE) item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.no", new String[]{STONE, String.valueOf(Weapons.PRICE_STONE), String.valueOf(Weapons.PRICE_STONE - coin)}));
        else item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.yes", new String[]{STONE, String.valueOf(Weapons.PRICE_STONE)}));
        if(mapWeapon.get("Stone")){
            item = Item.get(Item.IRON_SWORD,0,1);
            if(coin < Weapons.PRICE_IRON) item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.no", new String[]{IRON, String.valueOf(Weapons.PRICE_IRON), String.valueOf(Weapons.PRICE_IRON - coin)}));
            else item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.yes", new String[]{IRON, String.valueOf(Weapons.PRICE_IRON)}));
        }
        if(mapWeapon.get("Iron")){
            item = Item.get(Item.GOLD_SWORD,0,1);
            if(coin < Weapons.PRICE_GOLD) item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.no", new String[]{GOLD, String.valueOf(Weapons.PRICE_GOLD), String.valueOf(Weapons.PRICE_GOLD - coin)}));
            else item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.yes", new String[]{GOLD, String.valueOf(Weapons.PRICE_GOLD)}));
        }
        if(mapWeapon.get("Gold")){
            item = Item.get(Item.DIAMOND_SWORD,0,1);
            if(coin < Weapons.PRICE_DIAMOND) item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.no", new String[]{DIAMOND, String.valueOf(Weapons.PRICE_DIAMOND), String.valueOf(Weapons.PRICE_DIAMOND - coin)}));
            else item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.yes", new String[]{DIAMOND, String.valueOf(Weapons.PRICE_DIAMOND)}));
        }
        if(mapWeapon.get("Diamond")){
            item = Item.get(Item.STONE,0,1);
            item.setCustomName(Main.getMessage(player,"item.murder.shop.weapon.noo"));
        }
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(WEAPON_INDEX, item);
        else items.put(POCKET_WEAPON_INDEX, item);
        shopItem.put(3, item);
        this.doubleinv = new DoubleServerChestInventory(items);
        this.screen = 0;
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackOpenMurderShopChestInventory(this), 5);
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
                Map<String, Boolean> mapWeapon = (Map<String, Boolean>) Weapons.weaponData.get(player.getName().toLowerCase());
                Map<String, Boolean> mapArmor = (Map<String, Boolean>) Armors.armorData.get(player.getName().toLowerCase());
                item = Item.get(Item.ENDER_CHEST,0,1);
                item.setCustomName(Main.getMessage(player,"item.murder.shop.purchase.hats"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(PURCHASE_HATS_INDEX, item);
                else items.put(POCKET_PURCHASE_HATS_INDEX, item);
                item = Item.get(Item.REDSTONE_DUST,0,1);
                item.setCustomName(Main.getMessage(player,"item.murder.shop.purchase.upgrades"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(PURCHASE_UPGRADES_INDEX, item);
                else items.put(POCKET_PURCHASE_UPGRADES_INDEX, item);
                int coin = Stat.getCoin(player);
                item = Item.get(Item.EMERALD,0,1);
                item.setCustomName(Main.getMessage(player,"item.murder.shop.coin", new String[]{String.valueOf(coin)}));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(COIN_INDEX, item);
                else items.put(POCKET_COIN_INDEX, item);
                item = Item.get(Item.LEATHER_CAP,0,1);
                if(coin < Armors.PRICE_LETHER) item.setCustomName(Main.getMessage(player,"item.murder.shop.helmet.no", new String[]{LETHER, String.valueOf(Armors.PRICE_LETHER), String.valueOf(Armors.PRICE_LETHER - coin)}));
                else item.setCustomName(Main.getMessage(player,"item.murder.shop.helmet.yes", new String[]{LETHER, String.valueOf(Armors.PRICE_LETHER)}));
                if(mapArmor.get("298:0")){
                    item = Item.get(Item.CHAIN_HELMET,0,1);
                    if(coin < Armors.PRICE_CHAIN) item.setCustomName(Main.getMessage(player,"item.murder.shop.helmet.no", new String[]{CHAIN, String.valueOf(Armors.PRICE_CHAIN), String.valueOf(Armors.PRICE_CHAIN - coin)}));
                    else item.setCustomName(Main.getMessage(player,"item.murder.shop.helmet.yes", new String[]{CHAIN, String.valueOf(Armors.PRICE_CHAIN)}));
                }
                if(mapArmor.get("302:0")){
                    item = Item.get(Item.IRON_HELMET,0,1);
                    if(coin < Armors.PRICE_IRON) item.setCustomName(Main.getMessage(player,"item.murder.shop.helmet.no", new String[]{IRON, String.valueOf(Armors.PRICE_IRON), String.valueOf(Armors.PRICE_IRON - coin)}));
                    else item.setCustomName(Main.getMessage(player,"item.murder.shop.helmet.yes", new String[]{IRON, String.valueOf(Armors.PRICE_IRON)}));
                }
                if(mapArmor.get("306:0")){
                    item = Item.get(Item.GOLD_HELMET,0,1);
                    if(coin < Armors.PRICE_GOLD) item.setCustomName(Main.getMessage(player,"item.murder.shop.helmet.no", new String[]{GOLD, String.valueOf(Armors.PRICE_GOLD), String.valueOf(Armors.PRICE_GOLD - coin)}));
                    else item.setCustomName(Main.getMessage(player,"item.murder.shop.helmet.yes", new String[]{GOLD, String.valueOf(Armors.PRICE_GOLD)}));
                }
                if(mapArmor.get("314:0")){
                    item = Item.get(Item.DIAMOND_HELMET,0,1);
                    if(coin < Armors.PRICE_DIAMOND) item.setCustomName(Main.getMessage(player,"item.murder.shop.helmet.no", new String[]{DIAMOND, String.valueOf(Armors.PRICE_DIAMOND), String.valueOf(Armors.PRICE_DIAMOND - coin)}));
                    else item.setCustomName(Main.getMessage(player,"item.murder.shop.helmet.yes", new String[]{DIAMOND, String.valueOf(Armors.PRICE_DIAMOND)}));
                }
                if(mapArmor.get("310:0")){
                    item = Item.get(Item.STONE,0,1);
                    item.setCustomName(Main.getMessage(player,"item.murder.shop.helmet.noo"));
                }
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HELMET_INDEX, item);
                else items.put(POCKET_HELMET_INDEX, item);
                shopItem.put(0, item);
                item = Item.get(Item.LEATHER_PANTS,0,1);
                if(coin < Armors.PRICE_LETHER) item.setCustomName(Main.getMessage(player,"item.murder.shop.leggings.no", new String[]{LETHER, String.valueOf(Armors.PRICE_LETHER), String.valueOf(Armors.PRICE_LETHER - coin)}));
                else item.setCustomName(Main.getMessage(player,"item.murder.shop.leggings.yes", new String[]{LETHER, String.valueOf(Armors.PRICE_LETHER)}));
                if(mapArmor.get("300:0")){
                    item = Item.get(Item.CHAIN_LEGGINGS,0,1);
                    if(coin < Armors.PRICE_CHAIN) item.setCustomName(Main.getMessage(player,"item.murder.shop.leggings.no", new String[]{CHAIN, String.valueOf(Armors.PRICE_CHAIN), String.valueOf(Armors.PRICE_CHAIN - coin)}));
                    else item.setCustomName(Main.getMessage(player,"item.murder.shop.leggings.yes", new String[]{CHAIN, String.valueOf(Armors.PRICE_CHAIN)}));
                }
                if(mapArmor.get("304:0")){
                    item = Item.get(Item.IRON_LEGGINGS,0,1);
                    if(coin < Armors.PRICE_IRON) item.setCustomName(Main.getMessage(player,"item.murder.shop.leggings.no", new String[]{IRON, String.valueOf(Armors.PRICE_IRON), String.valueOf(Armors.PRICE_IRON - coin)}));
                    else item.setCustomName(Main.getMessage(player,"item.murder.shop.leggings.yes", new String[]{IRON, String.valueOf(Armors.PRICE_IRON)}));
                }
                if(mapArmor.get("308:0")){
                    item = Item.get(Item.GOLD_LEGGINGS,0,1);
                    if(coin < Armors.PRICE_GOLD) item.setCustomName(Main.getMessage(player,"item.murder.shop.leggings.no", new String[]{GOLD, String.valueOf(Armors.PRICE_GOLD), String.valueOf(Armors.PRICE_GOLD - coin)}));
                    else item.setCustomName(Main.getMessage(player,"item.murder.shop.leggings.yes", new String[]{GOLD, String.valueOf(Armors.PRICE_GOLD)}));
                }
                if(mapArmor.get("316:0")){
                    item = Item.get(Item.DIAMOND_LEGGINGS,0,1);
                    if(coin < Armors.PRICE_DIAMOND) item.setCustomName(Main.getMessage(player,"item.murder.shop.leggings.no", new String[]{DIAMOND, String.valueOf(Armors.PRICE_DIAMOND), String.valueOf(Armors.PRICE_DIAMOND - coin)}));
                    else item.setCustomName(Main.getMessage(player,"item.murder.shop.leggings.yes", new String[]{DIAMOND, String.valueOf(Armors.PRICE_DIAMOND)}));
                }
                if(mapArmor.get("312:0")){
                    item = Item.get(Item.STONE,0,1);
                    item.setCustomName(Main.getMessage(player,"item.murder.shop.leggings.noo"));
                }
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(LEGGINGS_INDEX, item);
                else items.put(POCKET_LEGGINGS_INDEX, item);
                shopItem.put(1, item);
                item = Item.get(Item.LEATHER_BOOTS,0,1);
                if(coin < Armors.PRICE_LETHER) item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.no", new String[]{LETHER, String.valueOf(Armors.PRICE_LETHER), String.valueOf(Armors.PRICE_LETHER - coin)}));
                else item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.yes", new String[]{LETHER, String.valueOf(Armors.PRICE_LETHER)}));
                if(mapArmor.get("301:0")){
                    item = Item.get(Item.CHAIN_BOOTS,0,1);
                    if(coin < Armors.PRICE_CHAIN) item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.no", new String[]{CHAIN, String.valueOf(Armors.PRICE_CHAIN), String.valueOf(Armors.PRICE_CHAIN - coin)}));
                    else item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.yes", new String[]{CHAIN, String.valueOf(Armors.PRICE_CHAIN)}));
                }
                if(mapArmor.get("305:0")){
                    item = Item.get(Item.IRON_BOOTS,0,1);
                    if(coin < Armors.PRICE_IRON) item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.no", new String[]{IRON, String.valueOf(Armors.PRICE_IRON), String.valueOf(Armors.PRICE_IRON - coin)}));
                    else item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.yes", new String[]{IRON, String.valueOf(Armors.PRICE_IRON)}));
                }
                if(mapArmor.get("309:0")){
                    item = Item.get(Item.GOLD_BOOTS,0,1);
                    if(coin < Armors.PRICE_GOLD) item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.no", new String[]{GOLD, String.valueOf(Armors.PRICE_GOLD), String.valueOf(Armors.PRICE_GOLD - coin)}));
                    else item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.yes", new String[]{GOLD, String.valueOf(Armors.PRICE_GOLD)}));
                }
                if(mapArmor.get("317:0")){
                    item = Item.get(Item.DIAMOND_BOOTS,0,1);
                    if(coin < Armors.PRICE_DIAMOND) item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.no", new String[]{DIAMOND, String.valueOf(Armors.PRICE_DIAMOND), String.valueOf(Armors.PRICE_DIAMOND - coin)}));
                    else item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.yes", new String[]{DIAMOND, String.valueOf(Armors.PRICE_DIAMOND)}));
                }
                if(mapArmor.get("313:0")){
                    item = Item.get(Item.STONE,0,1);
                    item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.noo"));
                }
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(BOOTS_INDEX, item);
                else items.put(POCKET_BOOTS_INDEX, item);
                shopItem.put(2, item);
                item = Item.get(Item.STONE_SWORD,0,1);
                if(coin < Weapons.PRICE_STONE) item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.no", new String[]{STONE, String.valueOf(Weapons.PRICE_STONE), String.valueOf(Weapons.PRICE_STONE - coin)}));
                else item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.yes", new String[]{STONE, String.valueOf(Weapons.PRICE_STONE)}));
                if(mapWeapon.get("Stone")){
                    item = Item.get(Item.IRON_SWORD,0,1);
                    if(coin < Weapons.PRICE_IRON) item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.no", new String[]{IRON, String.valueOf(Weapons.PRICE_IRON), String.valueOf(Weapons.PRICE_IRON - coin)}));
                    else item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.yes", new String[]{IRON, String.valueOf(Weapons.PRICE_IRON)}));
                }
                if(mapWeapon.get("Iron")){
                    item = Item.get(Item.GOLD_SWORD,0,1);
                    if(coin < Weapons.PRICE_GOLD) item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.no", new String[]{GOLD, String.valueOf(Weapons.PRICE_GOLD), String.valueOf(Weapons.PRICE_GOLD - coin)}));
                    else item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.yes", new String[]{GOLD, String.valueOf(Weapons.PRICE_GOLD)}));
                }
                if(mapWeapon.get("Gold")){
                    item = Item.get(Item.DIAMOND_SWORD,0,1);
                    if(coin < Weapons.PRICE_DIAMOND) item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.no", new String[]{DIAMOND, String.valueOf(Weapons.PRICE_DIAMOND), String.valueOf(Weapons.PRICE_DIAMOND - coin)}));
                    else item.setCustomName(Main.getMessage(player,"item.murder.shop.boots.yes", new String[]{DIAMOND, String.valueOf(Weapons.PRICE_DIAMOND)}));
                }
                if(mapWeapon.get("Diamond")){
                    item = Item.get(Item.STONE,0,1);
                    item.setCustomName(Main.getMessage(player,"item.murder.shop.weapon.noo"));
                }
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(WEAPON_INDEX, item);
                else items.put(POCKET_WEAPON_INDEX, item);
                shopItem.put(3, item);
            break;
            case SCREEN_HATS:
                Map<String, Boolean> mapHat = (Map<String, Boolean>) MurderHats.hatData.get(player.getName().toLowerCase());
                item = Item.get(Item.ARROW,0,1);
                item.setCustomName(Main.getMessage(player,"item.murder.shop.hats.backpage"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HATS_BACK_INDEX, item);
                else items.put(POCKET_HATS_BACK_INDEX, item);
                boolean containsNextPage = false;
                int count = 0;
                int pageMax;
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) pageMax = HATS_PAGE_MAX;
                else pageMax = POCKET_HATS_PAGE_MAX;
                for (Map.Entry<String,Item> e : MurderHats.item.entrySet()){
                    if(e.getKey().equals("unknown")) continue;
                    if(count >= (pageMax * (this.page - 1)) && count < (pageMax * this.page)){
                        if(mapHat.get(e.getKey())) continue;
                        if(MurderHats.canReveal(player, e.getKey())){
                            item = e.getValue();
                            item.setCustomName(MurderHats.getShopName(player, e.getKey()));
                        }else{
                            item = Item.get(Item.GLASS_PANEL);
                            item.setCustomName(Main.getMessage(player, "item.murder.shop.hats.no.reveal"));
                        }
                        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC){
                            items.put(HATS_PAGE_INDEX[count - (pageMax * (this.page - 1))], item);
                            shopHat.put(HATS_PAGE_INDEX[count - (pageMax * (this.page - 1))], e.getKey());
                        }else{
                            items.put(POCKET_HATS_PAGE_INDEX[count - (pageMax * (this.page - 1))], item);
                            shopHat.put(POCKET_HATS_PAGE_INDEX[count - (pageMax * (this.page - 1))], e.getKey());
                        }
                    }else if(count >= (pageMax * this.page)){
                        containsNextPage = true;
                    }
                    count++;
                }
                if(containsNextPage){
                    item = Item.get(Item.ARROW,0,1);
                    item.setCustomName(Main.getMessage(player,"item.murder.shop.hats.nextpage"));
                    if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HATS_NEXT_INDEX, item);
                    else items.put(POCKET_HATS_NEXT_INDEX, item);
                }
                item = Item.get(Item.EMERALD,0,1);
                item.setCustomName(Main.getMessage(player,"item.murder.shop.coin", new String[]{String.valueOf(Stat.getCoin(player))}));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(HATS_COIN_INDEX, item);
                else items.put(POCKET_HATS_COIN_INDEX, item);
                this.shopItem.putAll(items);
            break;
            case SCREEN_CONFIRM_HAT:
                item = MurderHats.item.get(this.confirmHat);
                item.setCustomName(MurderHats.getConfirmName(player, this.confirmHat));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(CONFIRM_HAT_INDEX, item);
                else items.put(POCKET_CONFIRM_HAT_INDEX, item);
            break;
            case SCREEN_UPGRADES:
                item = Item.get(Item.ARROW,0,1);
                item.setCustomName(Main.getMessage(player,"item.back"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(UPGRADES_BACK_INDEX, item);
                else items.put(POCKET_UPGRADES_BACK_INDEX, item);
                item = Item.get(Item.REDSTONE_DUST,0,1);
                item.setCustomName(MurderUpgrades.getShopName(player, "Emerald Upgrade"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(UPGRADES_EMERALD_INDEX, item);
                else items.put(POCKET_UPGRADES_EMERALD_INDEX, item);
                item = Item.get(Item.REDSTONE_DUST,0,1);
                item.setCustomName(MurderUpgrades.getShopName(player, "Gun Upgrade"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(UPGRADES_GUN_INDEX, item);
                else items.put(POCKET_UPGRADES_GUN_INDEX, item);
                item = Item.get(Item.REDSTONE_DUST,0,1);
                item.setCustomName(MurderUpgrades.getShopName(player, "Knife Upgrade"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(UPGRADES_KNIFE_INDEX, item);
                else items.put(POCKET_UPGRADES_KNIFE_INDEX, item);
                item = Item.get(Item.EMERALD,0,1);
                item.setCustomName(Main.getMessage(player,"item.murder.shop.coin", new String[]{String.valueOf(Stat.getCoin(player))}));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(UPGRADES_COIN_INDEX, item);
                else items.put(POCKET_UPGRADES_COIN_INDEX, item);
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
                            case PURCHASE_HATS_INDEX:
                                this.screen = SCREEN_HATS;
                                this.page = 1;
                            break;
                            case PURCHASE_UPGRADES_INDEX:
                                this.screen = SCREEN_UPGRADES;
                            break;
                            case HELMET_INDEX:
                                if(shopItem.get(0).getId() == Item.STONE) break;
                                Armors.buyItem(player, shopItem.get(0).getId()+":"+shopItem.get(0).getDamage());
                            break;
                            case LEGGINGS_INDEX:
                                if(shopItem.get(1).getId() == Item.STONE) break;
                                Armors.buyItem(player, shopItem.get(1).getId()+":"+shopItem.get(1).getDamage());
                            break;
                            case BOOTS_INDEX:
                                if(shopItem.get(2).getId() == Item.STONE) break;
                                Armors.buyItem(player, shopItem.get(2).getId()+":"+shopItem.get(2).getDamage());
                            break;
                            case WEAPON_INDEX:
                                if(shopItem.get(3).getId() == Item.STONE) break;
                                String buy = "";
                                switch(shopItem.get(3).getId()){
                                    case Item.STONE_SWORD:
                                        buy = "Stone";
                                    break;
                                    case Item.IRON_SWORD:
                                        buy = "Iron";
                                        break;
                                    case Item.GOLD_SWORD:
                                        buy = "Gold";
                                        break;
                                    case Item.DIAMOND_SWORD:
                                        buy = "Diamond";
                                        break;
                                }
                                if(buy.equals("")) return;
                                Weapons.buyItem(player, buy);
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
                                if(shopHat.containsKey(slot)){
                                    if(!mapHat.get(shopHat.get(slot)) && MurderHats.canReveal(player, shopHat.get(slot))){
                                        this.confirmHat = shopHat.get(slot);
                                        this.screen = SCREEN_CONFIRM_HAT;
                                    }
                                }
                            break;
                        }
                    break;
                    case SCREEN_CONFIRM_HAT:
                        switch(slot){
                            case CONFIRM_HAT_INDEX:
                                MurderHats.buyItem(player, this.confirmHat);
                            break;
                        }
                    break;
                    case SCREEN_UPGRADES:
                        switch(slot){
                            case UPGRADES_BACK_INDEX:
                                this.screen = SCREEN_TOP;
                            break;
                            case UPGRADES_EMERALD_INDEX:
                                MurderUpgrades.buyItem(player, "Emerald Upgrade");
                            break;
                            case UPGRADES_GUN_INDEX:
                                MurderUpgrades.buyItem(player, "Gun Upgrade");
                            break;
                            case UPGRADES_KNIFE_INDEX:
                                MurderUpgrades.buyItem(player, "Knife Upgrade");
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
                            case POCKET_PURCHASE_HATS_INDEX:
                                this.screen = SCREEN_HATS;
                            break;
                            case POCKET_PURCHASE_UPGRADES_INDEX:
                                this.screen = SCREEN_UPGRADES;
                            break;
                            case POCKET_HELMET_INDEX:
                                if(shopItem.get(0).getId() == Item.STONE) break;
                                Armors.buyItem(player, shopItem.get(0).getId()+":"+shopItem.get(0).getDamage());
                            break;
                            case POCKET_LEGGINGS_INDEX:
                                if(shopItem.get(1).getId() == Item.STONE) break;
                                Armors.buyItem(player, shopItem.get(1).getId()+":"+shopItem.get(1).getDamage());
                            break;
                            case POCKET_BOOTS_INDEX:
                                if(shopItem.get(2).getId() == Item.STONE) break;
                                Armors.buyItem(player, shopItem.get(2).getId()+":"+shopItem.get(2).getDamage());
                            break;
                            case POCKET_WEAPON_INDEX:
                                if(shopItem.get(3).getId() == Item.STONE) break;
                                String buy = "";
                                switch(shopItem.get(3).getId()){
                                    case Item.STONE_SWORD:
                                        buy = "Stone";
                                    break;
                                    case Item.IRON_SWORD:
                                        buy = "Iron";
                                        break;
                                    case Item.GOLD_SWORD:
                                        buy = "Gold";
                                        break;
                                    case Item.DIAMOND_SWORD:
                                        buy = "Diamond";
                                        break;
                                }
                                if(buy.equals("")) return;
                                Weapons.buyItem(player, buy);
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
                                if(shopHat.containsKey(slot)){
                                    if(!mapHat.get(shopHat.get(slot)) && MurderHats.canReveal(player, shopHat.get(slot))){
                                        this.confirmHat = shopHat.get(slot);
                                        this.screen = SCREEN_CONFIRM_HAT;
                                    }
                                }
                            break;
                        }
                    break;
                    case SCREEN_CONFIRM_HAT:
                        switch(slot){
                            case POCKET_CONFIRM_HAT_INDEX:
                                MurderHats.buyItem(player, this.confirmHat);
                            break;
                        }
                    break;
                    case SCREEN_UPGRADES:
                        switch(slot){
                            case UPGRADES_BACK_INDEX:
                                this.screen = SCREEN_TOP;
                            break;
                            case UPGRADES_EMERALD_INDEX:
                                MurderUpgrades.buyItem(player, "Emerald Upgrade");
                            break;
                            case UPGRADES_GUN_INDEX:
                                MurderUpgrades.buyItem(player, "Gun Upgrade");
                            break;
                            case UPGRADES_KNIFE_INDEX:
                                MurderUpgrades.buyItem(player, "Knife Upgrade");
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
class CallbackOpenMurderShopChestInventory extends Task{

    public MurderShopChestInventory owner;

    public CallbackOpenMurderShopChestInventory(MurderShopChestInventory o){
        owner = o;
    }

    public void onRun(int d){
        owner.open();
    }
}
