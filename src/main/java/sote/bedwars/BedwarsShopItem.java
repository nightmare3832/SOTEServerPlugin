package sote.bedwars;

import java.util.LinkedHashMap;

import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;

public class BedwarsShopItem{

    public static final int RESOURCE_IRON = 0;
    public static final int RESOURCE_GOLD = 1;
    public static final int RESOURCE_EMERALD = 2;

    public BedwarsShopItem(){
    }

    public static void register(){
        registerArmor();
        registerMelee();
        registerBlocks();
        registerRanged();
        registerTools();
        registerPotions();
        registerUtility();
    }

    public static void registerArmor(){
        Item item;
        item = Item.get(Item.CHAIN_BOOTS, 0, 1);
        armor.put(item, new Integer[]{RESOURCE_IRON, 40, 0, 1});
        item = Item.get(Item.IRON_BOOTS, 0, 1);
        armor.put(item, new Integer[]{RESOURCE_GOLD, 12, 1, 1});
        item = Item.get(Item.DIAMOND_BOOTS, 0, 1);
        armor.put(item, new Integer[]{RESOURCE_EMERALD, 6, 2, 1});
    }

    public static void registerMelee(){
        Item item;
        item = Item.get(Item.STONE_SWORD, 0, 1);
        melee.put(item, new Integer[]{RESOURCE_IRON, 10, 0, 0});
        item = Item.get(Item.IRON_SWORD, 0, 1);
        melee.put(item, new Integer[]{RESOURCE_GOLD, 7, 1, 0});
        item = Item.get(Item.DIAMOND_SWORD, 0, 1);
        melee.put(item, new Integer[]{RESOURCE_EMERALD, 4, 2, 0});
        item = Item.get(Item.STICK, 0, 1);
        Enchantment ench = Enchantment.getEnchantment(Enchantment.ID_KNOCKBACK);
        ench.setLevel(2);
        item.addEnchantment(ench);
        melee.put(item, new Integer[]{RESOURCE_GOLD, 10, 3, 0});
    }

    public static void registerBlocks(){
        Item item;
        item = Item.get(Item.WOOL, 0, 16);
        blocks.put(item, new Integer[]{RESOURCE_IRON, 4, 0, 0});
        item = Item.get(Item.SANDSTONE, 0, 12);
        blocks.put(item, new Integer[]{RESOURCE_IRON, 12, 1, 0});
        item = Item.get(Item.END_STONE, 0, 12);
        blocks.put(item, new Integer[]{RESOURCE_IRON, 36, 2, 0});
        item = Item.get(Item.PLANK, 0, 16);
        blocks.put(item, new Integer[]{RESOURCE_GOLD, 4, 3, 0});
        item = Item.get(Item.OBSIDIAN, 0, 4);
        blocks.put(item, new Integer[]{RESOURCE_EMERALD, 4, 4, 0});
    }

    public static void registerRanged(){
        Item item;
        item = Item.get(Item.ARROW, 0, 8);
        ranged.put(item, new Integer[]{RESOURCE_GOLD, 2, 0, 0});
        item = Item.get(Item.BOW, 0, 1);
        ranged.put(item, new Integer[]{RESOURCE_GOLD, 12, 1, 0});
        item = Item.get(Item.BOW, 0, 1);
        Enchantment ench = Enchantment.getEnchantment(Enchantment.ID_BOW_POWER);
        ench.setLevel(1);
        item.addEnchantment(ench);
        ranged.put(item, new Integer[]{RESOURCE_GOLD, 24, 2, 0});
        item = Item.get(Item.BOW, 0, 1);
        Enchantment  ench2 = Enchantment.getEnchantment(Enchantment.ID_BOW_KNOCKBACK);
        ench.setLevel(1);
        item.addEnchantment(ench, ench2);
        ranged.put(item, new Integer[]{RESOURCE_EMERALD, 6, 3, 0});
    }

    public static void registerTools(){
        Item item;
        item = Item.get(Item.SHEARS, 0, 1);
        tools.put(item, new Integer[]{RESOURCE_IRON, 30, 0, 1});
        item = Item.get(Item.WOODEN_PICKAXE, 0, 1);
        tools.put(item, new Integer[]{RESOURCE_IRON, 10, 1, 0});
        item = Item.get(Item.STONE_PICKAXE, 0, 1);
        Enchantment ench = Enchantment.getEnchantment(Enchantment.ID_EFFICIENCY);
        ench.setLevel(1);
        item.addEnchantment(ench);
        tools.put(item, new Integer[]{RESOURCE_IRON, 20, 2, 0});
        item = Item.get(Item.IRON_PICKAXE, 0, 1);
        Enchantment ench2 = Enchantment.getEnchantment(Enchantment.ID_EFFICIENCY);
        ench.setLevel(2);
        item.addEnchantment(ench2);
        tools.put(item, new Integer[]{RESOURCE_GOLD, 8, 3, 0});
        item = Item.get(Item.DIAMOND_PICKAXE, 0, 1);
        Enchantment ench3 = Enchantment.getEnchantment(Enchantment.ID_EFFICIENCY);
        ench.setLevel(3);
        item.addEnchantment(ench3);
        tools.put(item, new Integer[]{RESOURCE_GOLD, 12, 4, 0});
        item = Item.get(Item.DIAMOND_AXE, 0, 1);
        Enchantment ench4 = Enchantment.getEnchantment(Enchantment.ID_EFFICIENCY);
        ench.setLevel(3);
        item.addEnchantment(ench4);
        tools.put(item, new Integer[]{RESOURCE_GOLD, 12, 5, 0});
    }

    public static void registerPotions(){
        Item item;
        item = Item.get(Item.POTION, 0, 1);
        potions.put(item, new Integer[]{RESOURCE_EMERALD, 1, 0, 0});
        item = Item.get(Item.POTION, 1, 1);
        potions.put(item, new Integer[]{RESOURCE_EMERALD, 1, 1, 0});
        item = Item.get(Item.POTION, 2, 1);
        potions.put(item, new Integer[]{RESOURCE_EMERALD, 1, 2, 0});
    }

    public static void registerUtility(){
        Item item;
        item = Item.get(Item.TNT, 0, 1);
        utility.put(item, new Integer[]{RESOURCE_GOLD, 5, 0, 0});
        item = Item.get(Item.FIRE_CHARGE, 0, 1);
        utility.put(item, new Integer[]{RESOURCE_IRON, 50, 1, 0});
        item = Item.get(Item.SNOWBALL, 0, 1);
        item.addEnchantment();
        utility.put(item, new Integer[]{RESOURCE_IRON, 50, 2, 0});//Gapple, Enderpearl
    }

    public static LinkedHashMap<Item, Integer[]> armor = new LinkedHashMap<>();
    public static LinkedHashMap<Item, Integer[]> melee = new LinkedHashMap<>();
    public static LinkedHashMap<Item, Integer[]> blocks = new LinkedHashMap<>();
    public static LinkedHashMap<Item, Integer[]> ranged = new LinkedHashMap<>();
    public static LinkedHashMap<Item, Integer[]> tools = new LinkedHashMap<>();
    public static LinkedHashMap<Item, Integer[]> potions = new LinkedHashMap<>();
    public static LinkedHashMap<Item, Integer[]> utility = new LinkedHashMap<>();
}