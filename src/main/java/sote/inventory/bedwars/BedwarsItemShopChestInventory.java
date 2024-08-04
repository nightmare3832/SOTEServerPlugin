package sote.inventory.bedwars;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.inventory.PlayerInventory;
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
import sote.bedwars.BedwarsForge;
import sote.bedwars.BedwarsShopItem;
import sote.inventory.DoubleServerChestInventory;
import sote.inventory.Inventorys;
import sote.inventory.ServerChestInventory;

public class BedwarsItemShopChestInventory extends ServerChestInventory{

    public static final int CHEST_SIZE = 54;

    public final int PURCHASE_ARMOR_INDEX = 10;
    public final int PURCHASE_MELEE_INDEX = 11;
    public final int PURCHASE_BLOCKS_INDEX = 12;
    public final int PURCHASE_RANGED_INDEX = 13;
    public final int PURCHASE_TOOLS_INDEX = 14;
    public final int PURCHASE_POTIONS_INDEX = 15;
    public final int PURCHASE_UTILITY_INDEX = 16;
    public final int SHOP_BACK_INDEX = 22;
    public final int[] SHOP_ITEM_INDEX = new int[]{
            10,11,12,13,14,15,16,
            19,20,21,22,23,24,25,
            28,29,30,31,32,33,34,
            37,38,39,40,41,42,43
    };

    public final int POCKET_PURCHASE_ARMOR_INDEX = 10;
    public final int POCKET_PURCHASE_MELEE_INDEX = 11;
    public final int POCKET_PURCHASE_BLOCKS_INDEX = 12;
    public final int POCKET_PURCHASE_RANGED_INDEX = 13;
    public final int POCKET_PURCHASE_TOOLS_INDEX = 14;
    public final int POCKET_PURCHASE_POTIONS_INDEX = 15;
    public final int POCKET_PURCHASE_UTILITY_INDEX = 16;
    public final int POCKET_SHOP_BACK_INDEX = 22;
    public final int[] POCKET_SHOP_ITEM_INDEX = new int[]{
            10,11,12,13,14,15,16,
            19,20,21,22,23,24,25,
            28,29,30,31,32,33,34,
            37,38,39,40,41,42,43
    };

    public final int SCREEN_TOP = 0;
    public final int SCREEN_ARMOR = 1;
    public final int SCREEN_MELEE = 2;
    public final int SCREEN_BLOCKS = 3;
    public final int SCREEN_RANGED = 4;
    public final int SCREEN_TOOLS = 5;
    public final int SCREEN_POTIONS = 6;
    public final int SCREEN_UTILITY = 7;

    public HashMap<Integer, Map.Entry<Item, Integer[]>> shopItem = new HashMap<>();
    public int screen = 0;
    public Map<Integer, Item> contents;
    public Item[] contentsArmor;

    public BedwarsItemShopChestInventory(Player player){
        super(player);
    }

    @Override
    public void register(){
        this.spawnChestBlock();
        this.SpawnBlockEntity();
        HashMap<Integer, Item> items = new HashMap<Integer, Item>();
        Item item;
        item = Item.get(Item.IRON_BOOTS,0,1);
        item.setCustomName(Main.getMessage(player,"item.bedwars.itemshop.purchase.armor"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(PURCHASE_ARMOR_INDEX, item);
        else items.put(POCKET_PURCHASE_ARMOR_INDEX, item);
        item = Item.get(Item.GOLD_SWORD,0,1);
        item.setCustomName(Main.getMessage(player,"item.bedwars.itemshop.purchase.melee"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(PURCHASE_MELEE_INDEX, item);
        else items.put(POCKET_PURCHASE_MELEE_INDEX, item);
        item = Item.get(Item.SANDSTONE,0,1);
        item.setCustomName(Main.getMessage(player,"item.bedwars.itemshop.purchase.blocks"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(PURCHASE_BLOCKS_INDEX, item);
        else items.put(POCKET_PURCHASE_BLOCKS_INDEX, item);
        item = Item.get(Item.BOW,0,1);
        item.setCustomName(Main.getMessage(player,"item.bedwars.itemshop.purchase.ranged"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(PURCHASE_RANGED_INDEX, item);
        else items.put(POCKET_PURCHASE_RANGED_INDEX, item);
        item = Item.get(Item.STONE_PICKAXE,0,1);
        item.setCustomName(Main.getMessage(player,"item.bedwars.itemshop.purchase.tools"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(PURCHASE_TOOLS_INDEX, item);
        else items.put(POCKET_PURCHASE_TOOLS_INDEX, item);
        item = Item.get(Item.BREWING_STAND,0,1);
        item.setCustomName(Main.getMessage(player,"item.bedwars.itemshop.purchase.potions"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(PURCHASE_POTIONS_INDEX, item);
        else items.put(POCKET_PURCHASE_POTIONS_INDEX, item);
        item = Item.get(Item.TNT,0,1);
        item.setCustomName(Main.getMessage(player,"item.bedwars.itemshop.purchase.utility"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(PURCHASE_UTILITY_INDEX, item);
        else items.put(POCKET_PURCHASE_UTILITY_INDEX, item);
        this.contents = this.player.getInventory().getContents();
        this.contentsArmor = this.player.getInventory().getArmorContents();
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
                .putInt("pairz", z)
                .putString("CustomName", "Item Shop");
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
                .putString("CustomName", "Item Shop");
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

    /* (非 Javadoc)
     * @see sote.inventory.ServerChestInventory#update()
     */
    @Override
    public void update(){
        HashMap<Integer, Item> items = new HashMap<Integer, Item>();
        Item item;
        switch(this.screen){
            case SCREEN_TOP:
                item = Item.get(Item.IRON_BOOTS,0,1);
                item.setCustomName(Main.getMessage(player,"item.bedwars.itemshop.purchase.armor"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(PURCHASE_ARMOR_INDEX, item);
                else items.put(POCKET_PURCHASE_ARMOR_INDEX, item);
                item = Item.get(Item.GOLD_SWORD,0,1);
                item.setCustomName(Main.getMessage(player,"item.bedwars.itemshop.purchase.melee"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(PURCHASE_MELEE_INDEX, item);
                else items.put(POCKET_PURCHASE_MELEE_INDEX, item);
                item = Item.get(Item.SANDSTONE,0,1);
                item.setCustomName(Main.getMessage(player,"item.bedwars.itemshop.purchase.blocks"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(PURCHASE_BLOCKS_INDEX, item);
                else items.put(POCKET_PURCHASE_BLOCKS_INDEX, item);
                item = Item.get(Item.BOW,0,1);
                item.setCustomName(Main.getMessage(player,"item.bedwars.itemshop.purchase.ranged"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(PURCHASE_RANGED_INDEX, item);
                else items.put(POCKET_PURCHASE_RANGED_INDEX, item);
                item = Item.get(Item.STONE_PICKAXE,0,1);
                item.setCustomName(Main.getMessage(player,"item.bedwars.itemshop.purchase.tools"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(PURCHASE_TOOLS_INDEX, item);
                else items.put(POCKET_PURCHASE_TOOLS_INDEX, item);
                item = Item.get(Item.BREWING_STAND,0,1);
                item.setCustomName(Main.getMessage(player,"item.bedwars.itemshop.purchase.potions"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(PURCHASE_POTIONS_INDEX, item);
                else items.put(POCKET_PURCHASE_POTIONS_INDEX, item);
                item = Item.get(Item.TNT,0,1);
                item.setCustomName(Main.getMessage(player,"item.bedwars.itemshop.purchase.utility"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(PURCHASE_UTILITY_INDEX, item);
                else items.put(POCKET_PURCHASE_UTILITY_INDEX, item);
            break;
            case SCREEN_ARMOR:
                int c = 0;
                shopItem.clear();
                for(Map.Entry<Item, Integer[]> e : BedwarsShopItem.armor.entrySet()){
                    Item item2 = e.getKey().clone();
                    item2.setCustomName(getShopItemName(player, "armor", e.getValue()));
                    if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC){
                        items.put(SHOP_ITEM_INDEX[c], item2);
                        shopItem.put(SHOP_ITEM_INDEX[c], e);
                    }else{
                        items.put(POCKET_SHOP_ITEM_INDEX[c], item2);
                        shopItem.put(POCKET_SHOP_ITEM_INDEX[c], e);
                    }
                    c++;
                }
            break;
            case SCREEN_MELEE:
                c = 0;
                shopItem.clear();
                for(Map.Entry<Item, Integer[]> e : BedwarsShopItem.melee.entrySet()){
                    Item item2 = e.getKey().clone();
                    item2.setCustomName(getShopItemName(player, "melee", e.getValue()));
                    if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC){
                        items.put(SHOP_ITEM_INDEX[c], item2);
                        shopItem.put(SHOP_ITEM_INDEX[c], e);
                    }else{
                        items.put(POCKET_SHOP_ITEM_INDEX[c], item2);
                        shopItem.put(POCKET_SHOP_ITEM_INDEX[c], e);
                    }
                    c++;
                }
            break;
            case SCREEN_BLOCKS:
                c = 0;
                shopItem.clear();
                for(Map.Entry<Item, Integer[]> e : BedwarsShopItem.blocks.entrySet()){
                    Item item2 = e.getKey().clone();
                    item2.setCustomName(getShopItemName(player, "blocks", e.getValue()));
                    if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC){
                        items.put(SHOP_ITEM_INDEX[c], item2);
                        shopItem.put(SHOP_ITEM_INDEX[c], e);
                    }else{
                        items.put(POCKET_SHOP_ITEM_INDEX[c], item2);
                        shopItem.put(POCKET_SHOP_ITEM_INDEX[c], e);
                    }
                    c++;
                }
            break;
            case SCREEN_RANGED:
                c = 0;
                shopItem.clear();
                for(Map.Entry<Item, Integer[]> e : BedwarsShopItem.ranged.entrySet()){
                    Item item2 = e.getKey().clone();
                    item2.setCustomName(getShopItemName(player, "ranged", e.getValue()));
                    if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC){
                        items.put(SHOP_ITEM_INDEX[c], item2);
                        shopItem.put(SHOP_ITEM_INDEX[c], e);
                    }else{
                        items.put(POCKET_SHOP_ITEM_INDEX[c], item2);
                        shopItem.put(POCKET_SHOP_ITEM_INDEX[c], e);
                    }
                    c++;
                }
            break;
            case SCREEN_POTIONS:
                c = 0;
                shopItem.clear();
                for(Map.Entry<Item, Integer[]> e : BedwarsShopItem.potions.entrySet()){
                    Item item2 = e.getKey().clone();
                    item2.setCustomName(getShopItemName(player, "potions", e.getValue()));
                    if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC){
                        items.put(SHOP_ITEM_INDEX[c], item2);
                        shopItem.put(SHOP_ITEM_INDEX[c], e);
                    }else{
                        items.put(POCKET_SHOP_ITEM_INDEX[c], item2);
                        shopItem.put(POCKET_SHOP_ITEM_INDEX[c], e);
                    }
                    c++;
                }
            break;
            case SCREEN_UTILITY:
                c = 0;
                shopItem.clear();
                for(Map.Entry<Item, Integer[]> e : BedwarsShopItem.utility.entrySet()){
                    Item item2 = e.getKey().clone();
                    item2.setCustomName(getShopItemName(player, "utility", e.getValue()));
                    if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC){
                        items.put(SHOP_ITEM_INDEX[c], item2);
                        shopItem.put(SHOP_ITEM_INDEX[c], e);
                    }else{
                        items.put(POCKET_SHOP_ITEM_INDEX[c], item2);
                        shopItem.put(POCKET_SHOP_ITEM_INDEX[c], e);
                    }
                    c++;
                }
            break;
        }
        doubleinv.setContents(items);
        sendContents();
        player.getInventory().setContents(contents);
        player.getInventory().setArmorContents(contentsArmor);
        player.getInventory().sendContents(player);
    }

    public String getShopItemName(Player player, String type, Integer[] data){
        boolean canBuy = true;
        int id = 0;
        if(data[0] == BedwarsForge.RESOURCE_IRON) id = Item.IRON_INGOT;
        if(data[0] == BedwarsForge.RESOURCE_GOLD) id = Item.GOLD_INGOT;
        if(data[0] == BedwarsForge.RESOURCE_EMERALD) id = Item.EMERALD;
        Item item = Item.get(id, 0, data[1]);
        if(!this.player.getInventory().contains(item)){
            canBuy = false;
        }
        String name = Main.getMessage(player, "item.bedwars.itemshop.purchase."+type+"."+data[2]+".name");
        if(canBuy) name = "§a" + name;
        else name = "§c" + name;
        String items = Main.getMessage(player, "item.bedwars.itemshop.purchase."+type+"."+data[2]+".items");
        String cost = Main.getMessage(player, "item.bedwars.itemshop.cost."+data[0], new String[]{String.valueOf(data[1])});
        String notLose = "";
        if(data[3] == 1) notLose = Main.getMessage(player, "item.bedwars.itemshop.not.lose");
        String click;
        if(canBuy) click = Main.getMessage(player, "item.bedwars.itemshop.enough.resource");
        else click = Main.getMessage(player, "item.bedwars.itemshop.not.enough.resource", new String[]{Main.getMessage(player, "item.bedwars.itemshop.resurce."+data[0])});
        return Main.getMessage(player, "item.bedwars.itemshop.purchase", new String[]{name, items, cost, notLose, click});
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
                            case PURCHASE_ARMOR_INDEX:
                                this.screen = SCREEN_ARMOR;
                            break;
                            case PURCHASE_MELEE_INDEX:
                                this.screen = SCREEN_MELEE;
                            break;
                            case PURCHASE_BLOCKS_INDEX:
                                this.screen = SCREEN_BLOCKS;
                            break;
                            case PURCHASE_RANGED_INDEX:
                                this.screen = SCREEN_RANGED;
                            break;
                            case PURCHASE_POTIONS_INDEX:
                                this.screen = SCREEN_POTIONS;
                            break;
                            case PURCHASE_UTILITY_INDEX:
                                this.screen = SCREEN_UTILITY;
                            break;
                        }
                    break;
                    case SCREEN_MELEE:
                    case SCREEN_BLOCKS:
                    case SCREEN_RANGED:
                    case SCREEN_POTIONS:
                    case SCREEN_UTILITY:
                        switch(slot){
                            case SHOP_BACK_INDEX:
                                this.screen = SCREEN_TOP;
                            break;
                            default:
                                purchaseItem(slot);
                            break;
                        }
                    break;
                    case SCREEN_ARMOR:
                        switch(slot){
                            case SHOP_BACK_INDEX:
                                this.screen = SCREEN_TOP;
                            break;
                            default:
                                purchaseArmor(slot);
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
                            case POCKET_PURCHASE_ARMOR_INDEX:
                                this.screen = SCREEN_ARMOR;
                            break;
                            case POCKET_PURCHASE_MELEE_INDEX:
                                this.screen = SCREEN_MELEE;
                            break;
                            case POCKET_PURCHASE_BLOCKS_INDEX:
                                this.screen = SCREEN_BLOCKS;
                            break;
                            case POCKET_PURCHASE_RANGED_INDEX:
                                this.screen = SCREEN_RANGED;
                            break;
                            case POCKET_PURCHASE_POTIONS_INDEX:
                                this.screen = SCREEN_POTIONS;
                            break;
                            case POCKET_PURCHASE_UTILITY_INDEX:
                                this.screen = SCREEN_UTILITY;
                            break;
                        }
                    break;
                    case SCREEN_MELEE:
                    case SCREEN_BLOCKS:
                    case SCREEN_RANGED:
                    case SCREEN_POTIONS:
                    case SCREEN_UTILITY:
                        switch(slot){
                            case POCKET_SHOP_BACK_INDEX:
                                this.screen = SCREEN_TOP;
                            break;
                            default:
                                purchaseItem(slot);
                            break;
                        }
                    break;
                    case SCREEN_ARMOR:
                        switch(slot){
                            case POCKET_SHOP_BACK_INDEX:
                                this.screen = SCREEN_TOP;
                            break;
                            default:
                                purchaseArmor(slot);
                            break;
                        }
                    break;
                }
                update();
            }
        }
    }

    public void purchaseItem(int slot){
        if(!shopItem.containsKey(slot)) return;
        boolean canBuy = true;
        int id = 0;
        if(shopItem.get(slot).getValue()[0] == BedwarsForge.RESOURCE_IRON) id = Item.IRON_INGOT;
        if(shopItem.get(slot).getValue()[0] == BedwarsForge.RESOURCE_GOLD) id = Item.GOLD_INGOT;
        if(shopItem.get(slot).getValue()[0] == BedwarsForge.RESOURCE_EMERALD) id = Item.EMERALD;
        Item item = Item.get(id, 0, shopItem.get(slot).getValue()[1]);
        if(!this.player.getInventory().contains(item)){
            canBuy = false;
        }
        if(!canBuy) return;
        if(shopItem.get(slot).getValue()[0] == BedwarsForge.RESOURCE_IRON) id = Item.IRON_INGOT;
        if(shopItem.get(slot).getValue()[0] == BedwarsForge.RESOURCE_GOLD) id = Item.GOLD_INGOT;
        if(shopItem.get(slot).getValue()[0] == BedwarsForge.RESOURCE_EMERALD) id = Item.EMERALD;
        item = Item.get(id, 0, shopItem.get(slot).getValue()[1]);
        this.player.getInventory().removeItem(item);
        this.player.getInventory().addItem(this.shopItem.get(slot).getKey());
        this.contents = this.player.getInventory().getContents();
        this.player.getInventory().sendContents(player);
    }

    public void purchaseArmor(int slot){
        if(!shopItem.containsKey(slot)) return;
        boolean canBuy = true;
        int id = 0;
        if(shopItem.get(slot).getValue()[0] == BedwarsForge.RESOURCE_IRON) id = Item.IRON_INGOT;
        if(shopItem.get(slot).getValue()[0] == BedwarsForge.RESOURCE_GOLD) id = Item.GOLD_INGOT;
        if(shopItem.get(slot).getValue()[0] == BedwarsForge.RESOURCE_EMERALD) id = Item.EMERALD;
        Item item = Item.get(id, 0, shopItem.get(slot).getValue()[1]);
        if(!this.player.getInventory().contains(item)){
            canBuy = false;
        }
        if(!canBuy) return;
        if(shopItem.get(slot).getValue()[0] == BedwarsForge.RESOURCE_IRON) id = Item.IRON_INGOT;
        if(shopItem.get(slot).getValue()[0] == BedwarsForge.RESOURCE_GOLD) id = Item.GOLD_INGOT;
        if(shopItem.get(slot).getValue()[0] == BedwarsForge.RESOURCE_EMERALD) id = Item.EMERALD;
        item = Item.get(id, 0, shopItem.get(slot).getValue()[1]);
        this.player.getInventory().removeItem(item);
        Game game = GameProvider.getPlayingGame(player);
        if(!(game instanceof Bedwars)) return;
        Bedwars bedwars = (Bedwars) game;
        if(this.shopItem.get(slot).getKey().getId() == Item.CHAIN_BOOTS) bedwars.haveArmor.put(PlayerDataManager.getPlayerData(player), Bedwars.ARMOR_CHAIN);
        if(this.shopItem.get(slot).getKey().getId() == Item.IRON_BOOTS) bedwars.haveArmor.put(PlayerDataManager.getPlayerData(player), Bedwars.ARMOR_IRON);
        if(this.shopItem.get(slot).getKey().getId() == Item.DIAMOND_BOOTS) bedwars.haveArmor.put(PlayerDataManager.getPlayerData(player), Bedwars.ARMOR_DIAMOND);
        PlayerInventory inventory = ((EntityHuman)player).getInventory();
        switch(bedwars.haveArmor.get(PlayerDataManager.getPlayerData(player))){
            case Bedwars.ARMOR_LETHER:
                inventory.setLeggings(Item.get(Item.LEATHER_PANTS, 0, 1));
                inventory.setBoots(Item.get(Item.LEATHER_BOOTS, 0, 1));
            break;
            case Bedwars.ARMOR_CHAIN:
                inventory.setLeggings(Item.get(Item.CHAIN_LEGGINGS, 0, 1));
                inventory.setBoots(Item.get(Item.CHAIN_BOOTS, 0, 1));
            break;
            case Bedwars.ARMOR_IRON:
                inventory.setLeggings(Item.get(Item.IRON_LEGGINGS, 0, 1));
                inventory.setBoots(Item.get(Item.IRON_BOOTS, 0, 1));
            break;
            case Bedwars.ARMOR_DIAMOND:
                inventory.setLeggings(Item.get(Item.DIAMOND_LEGGINGS, 0, 1));
                inventory.setBoots(Item.get(Item.DIAMOND_BOOTS, 0, 1));
            break;
        }
        bedwars.reinforcedArmor(player);
        this.contents = this.player.getInventory().getContents();
        this.player.getInventory().sendContents(player);
        this.contentsArmor = this.player.getInventory().getArmorContents();
        inventory.sendArmorContents(player);
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
class CallbackOpenMurderShopChestInventory extends Task{

    public BedwarsItemShopChestInventory owner;

    public CallbackOpenMurderShopChestInventory(BedwarsItemShopChestInventory o){
        owner = o;
    }

    public void onRun(int d){
        owner.open();
    }
}
