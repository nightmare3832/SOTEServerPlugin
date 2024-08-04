package sote.skywarssolo;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityChest;
import cn.nukkit.inventory.BaseInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemPotion;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;

public class SkywarsSoloChestManager{

    public SkywarsSolo owner;
    public Level level;
    public HashMap<Integer, Vector3> middleChests;
    public HashMap<Integer, Vector3[]> firstChests;
    public HashMap<Item,Integer> nomal;
    public HashMap<Item,Integer> rare;
    public HashMap<String, HashMap<Item[], Integer>> firstItems;

    public SkywarsSoloChestManager(SkywarsSolo owner){
        this.owner = owner;
        this.level = owner.world;
        this.firstChests = owner.stage.getFirstChests();
        this.middleChests = owner.stage.getMiddleChests();
        Item item;
        Item item2;
        Enchantment enchantment;
        this.firstItems = new HashMap<>();
        HashMap<Item[], Integer> sword = new HashMap<Item[], Integer>();
            item = Item.get(Item.STONE_SWORD, 0, 1);
            enchantment = Enchantment.get(Enchantment.ID_DAMAGE_ALL);
            enchantment.setLevel(1);
            item.addEnchantment(enchantment);
            sword.put(new Item[]{item}, 25);
            item = Item.get(Item.DIAMOND_SWORD, 0, 1);
            sword.put(new Item[]{item}, 100);
            item = Item.get(Item.DIAMOND_SWORD, 0, 1);
            enchantment = Enchantment.get(Enchantment.ID_DAMAGE_ALL);
            enchantment.setLevel(1);
            item.addEnchantment(enchantment);
            sword.put(new Item[]{item}, 75);
        firstItems.put("sword", sword);
        HashMap<Item[], Integer> helmet = new HashMap<Item[], Integer>();
            item = Item.get(Item.IRON_HELMET, 0, 1);
            helmet.put(new Item[]{item}, 100);
            item = Item.get(Item.DIAMOND_HELMET, 0, 1);
            helmet.put(new Item[]{item}, 100);
            item = Item.get(Item.AIR, 0, 1);
            helmet.put(new Item[]{item}, 20);
        firstItems.put("helmet", helmet);
        HashMap<Item[], Integer> chestplate = new HashMap<Item[], Integer>();
            item = Item.get(Item.IRON_CHESTPLATE, 0, 1);
            chestplate.put(new Item[]{item}, 100);
            item = Item.get(Item.DIAMOND_CHESTPLATE, 0, 1);
            chestplate.put(new Item[]{item}, 100);
            item = Item.get(Item.AIR, 0, 1);
            chestplate.put(new Item[]{item}, 20);
        firstItems.put("chestplate", chestplate);
        HashMap<Item[], Integer> leggings = new HashMap<Item[], Integer>();
            item = Item.get(Item.IRON_LEGGINGS, 0, 1);
            leggings.put(new Item[]{item}, 100);
            item = Item.get(Item.DIAMOND_LEGGINGS, 0, 1);
            leggings.put(new Item[]{item}, 100);
            item = Item.get(Item.AIR, 0, 1);
            leggings.put(new Item[]{item}, 20);
        firstItems.put("leggings", leggings);
        HashMap<Item[], Integer> boots = new HashMap<Item[], Integer>();
            item = Item.get(Item.IRON_BOOTS, 0, 1);
            boots.put(new Item[]{item}, 100);
            item = Item.get(Item.DIAMOND_BOOTS, 0, 1);
            boots.put(new Item[]{item}, 100);
            item = Item.get(Item.AIR, 0, 1);
            boots.put(new Item[]{item}, 20);
        firstItems.put("boots", boots);
        HashMap<Item[], Integer> block = new HashMap<Item[], Integer>();
            item = Item.get(Item.STONE, 0, 8);
            block.put(new Item[]{item}, 100);
            item = Item.get(Item.STONE, 0, 16);
            block.put(new Item[]{item}, 50);
            item = Item.get(Item.STONE, 0, 20);
            block.put(new Item[]{item}, 25);
            item = Item.get(Item.PLANK, 0, 8);
            block.put(new Item[]{item}, 100);
            item = Item.get(Item.PLANK, 0, 16);
            block.put(new Item[]{item}, 50);
            item = Item.get(Item.PLANK, 0, 20);
            block.put(new Item[]{item}, 25);
        firstItems.put("block",  block);
        HashMap<Item[], Integer> others = new HashMap<Item[], Integer>();
            item = Item.get(Item.BOW, 0, 1);
            enchantment = Enchantment.get(Enchantment.ID_BOW_POWER);
            enchantment.setLevel(1);
            item.addEnchantment(enchantment);
            item2 = Item.get(Item.ARROW, 0, 20);
            others.put(new Item[]{item, item2}, 5);
            item = Item.get(Item.BOW, 0, 1);
            enchantment = Enchantment.get(Enchantment.ID_BOW_POWER);
            enchantment.setLevel(3);
            item.addEnchantment(enchantment);
            item2 = Item.get(Item.ARROW, 0, 15);
            others.put(new Item[]{item, item2}, 5);
            item = Item.get(Item.FISHING_ROD, 0, 1);
            others.put(new Item[]{item}, 20);
            item = Item.get(Item.EXPERIENCE_BOTTLE, 0, 32);
            others.put(new Item[]{item}, 30);
            item = Item.get(Item.STEAK, 0, 16);
            others.put(new Item[]{item}, 20);
            item = Item.get(Item.BUCKET, 8, 1);
            others.put(new Item[]{item}, 5);
            item = Item.get(Item.BUCKET, 10, 1);
            others.put(new Item[]{item}, 5);
            item = Item.get(Item.EGG, 0, 16);
            others.put(new Item[]{item}, 30);
            item = Item.get(Item.SNOWBALL, 0, 16);
            others.put(new Item[]{item}, 30);
            item = Item.get(Item.SPLASH_POTION, ItemPotion.SPEED_II, 1);
            others.put(new Item[]{item}, 20);
            item = Item.get(Item.SPLASH_POTION, ItemPotion.REGENERATION_II, 1);
            others.put(new Item[]{item}, 20);
            item = Item.get(Item.SPLASH_POTION, ItemPotion.POISON_II, 1);
            others.put(new Item[]{item}, 20);
            item = Item.get(Item.ENCHANT_TABLE, 0, 1);
            //others.put(new Item[]{item}, 10);
            item = Item.get(Item.DIAMOND_PICKAXE, 0, 1);
            others.put(new Item[]{item}, 30);
            item = Item.get(Item.DIAMOND_AXE, 0, 1);
            others.put(new Item[]{item}, 30);
            item = Item.get(Item.AIR, 0, 1);
            others.put(new Item[]{item}, 500);
        firstItems.put("others",  others);
        HashMap<Item[], Integer> others2 = new HashMap<Item[], Integer>();
            item = Item.get(Item.BOW, 0, 1);
            enchantment = Enchantment.get(Enchantment.ID_BOW_POWER);
            enchantment.setLevel(1);
            item.addEnchantment(enchantment);
            item2 = Item.get(Item.ARROW, 0, 20);
            others2.put(new Item[]{item, item2}, 5);
            item = Item.get(Item.TNT, 0, 10);
            others2.put(new Item[]{item}, 10);
            item = Item.get(Item.DIAMOND_HELMET, 0, 1);
            enchantment = Enchantment.get(Enchantment.ID_PROTECTION_ALL);
            enchantment.setLevel(4);
            item.addEnchantment(enchantment);
            others2.put(new Item[]{item}, 10);
            item = Item.get(Item.DIAMOND_CHESTPLATE, 0, 1);
            enchantment = Enchantment.get(Enchantment.ID_PROTECTION_ALL);
            enchantment.setLevel(4);
            item.addEnchantment(enchantment);
            others2.put(new Item[]{item}, 10);
            item = Item.get(Item.DIAMOND_LEGGINGS, 0, 1);
            enchantment = Enchantment.get(Enchantment.ID_PROTECTION_ALL);
            enchantment.setLevel(5);
            item.addEnchantment(enchantment);
            others2.put(new Item[]{item}, 10);
            item = Item.get(Item.DIAMOND_BOOTS, 0, 1);
            enchantment = Enchantment.get(Enchantment.ID_PROTECTION_ALL);
            enchantment.setLevel(5);
            item.addEnchantment(enchantment);
            others2.put(new Item[]{item}, 10);
            item = Item.get(Item.FLINT_STEEL, 0, 1);
            others2.put(new Item[]{item}, 10);
            item = Item.get(Item.FISHING_ROD, 0, 1);
            enchantment = Enchantment.get(Enchantment.ID_KNOCKBACK);
            enchantment.setLevel(3);
            item.addEnchantment(enchantment);
            others2.put(new Item[]{item}, 10);
            item = Item.get(Item.GOLDEN_APPLE, 0, 5);
            others2.put(new Item[]{item}, 10);
            item = Item.get(Item.BUCKET, 10, 1);
            others2.put(new Item[]{item}, 10);
            item = Item.get(Item.EXPERIENCE_BOTTLE, 0, 64);
            others2.put(new Item[]{item}, 10);
            item = Item.get(Item.SNOWBALL, 0, 64);
            others2.put(new Item[]{item}, 10);
            item = Item.get(Item.DIAMOND_AXE, 0, 1);
            enchantment = Enchantment.get(Enchantment.ID_EFFICIENCY);
            enchantment.setLevel(3);
            item.addEnchantment(enchantment);
            others2.put(new Item[]{item}, 10);
            item = Item.get(Item.DIAMOND_PICKAXE, 0, 1);
            enchantment = Enchantment.get(Enchantment.ID_EFFICIENCY);
            enchantment.setLevel(3);
            item.addEnchantment(enchantment);
            others2.put(new Item[]{item}, 10);
            item = Item.get(Item.PLANK, 0, 64);
            others2.put(new Item[]{item}, 10);
            item = Item.get(Item.ARROW, 0, 64);
            others2.put(new Item[]{item}, 10);
            item = Item.get(Item.ENDER_PEARL, 0, 5);
            others2.put(new Item[]{item}, 1);
            //item = Item.get(Item.COMPASS, 0, 1);
            //others2.put(new Item[]{item}, 1);
            //item = Item.get(Item.SPAWN_EGG, 34, 1);//Skelton
            //others2.put(new Item[]{item}, 1);
            //item = Item.get(Item.SPAWN_EGG, 43, 1);//Blaze
            //others2.put(new Item[]{item}, 1);
            item = Item.get(Item.AIR, 0, 1);
            others2.put(new Item[]{item}, 50);
        firstItems.put("others2",  others2);
        setFirstChests();
        setMiddleChests();
    }

    public void setFirstChests(){
        String[] randoms = new String[]{"sword", "helmet", "chestplate", "leggings", "boots",
                                        "block", "block", "block", "block", "block",
                                        "others", "others", "others", "others", "others", "others",
                                        "others", "others", "others", "others", "others", "others",
                                        "others", "others", "others", "others", "others", "others"};
        int all = 0;
        for(Map.Entry<Integer, Vector3[]> e : this.firstChests.entrySet()){
            Item[] items = new Item[27*3];
            int f = 0;
            for(String random : randoms){
                all = 0;
                for (Map.Entry<Item[], Integer> v : firstItems.get(random).entrySet()){
                    all += v.getValue();
                }
                int rand = (int)(Math.random() * all);
                int c = 0;
                for (Map.Entry<Item[], Integer> vv : this.firstItems.get(random).entrySet()){
                    if(rand >= c && rand < c + vv.getValue()){
                        for(Item i : vv.getKey()){
                            items[f] = i;
                            f++;
                        }
                    }
                    c += vv.getValue();
                }
            }
            for(int blank = f;blank < 27*3;blank++){
                items[f] = Item.get(Item.AIR);
                f++;
            }
            List<Item> list = Arrays.asList(items);
            Collections.shuffle(list);
            items = (Item[])list.toArray(new Item[list.size()]);
            for(int cc = 0;cc < 3;cc++){
                BlockEntity blockentity = this.level.getBlockEntity(e.getValue()[cc]);
                if(blockentity instanceof BlockEntityChest){
                    BaseInventory inventory = ((BlockEntityChest)blockentity).getInventory();
                    for(int i = cc*27;i < (cc+1)*27;i++){
                        inventory.setItem(i - cc*27, items[i]);
                    }
                }
            }
        }
    }

    public void setMiddleChests(){
        String[] randoms = new String[]{"others2", "others2", "others2"};
        int all = 0;
        for(Map.Entry<Integer, Vector3> e : this.middleChests.entrySet()){
            Item[] items = new Item[27];
            int f = 0;
            for(String random : randoms){
                all = 0;
                for (Map.Entry<Item[], Integer> v : firstItems.get(random).entrySet()){
                    all += v.getValue();
                }
                int rand = (int)(Math.random() * all);
                int c = 0;
                for (Map.Entry<Item[], Integer> vv : this.firstItems.get(random).entrySet()){
                    if(rand >= c && rand < c + vv.getValue()){
                        for(Item i : vv.getKey()){
                            items[f] = i;
                            f++;
                        }
                    }
                    c += vv.getValue();
                }
            }
            for(int blank = f;blank < 27;blank++){
                items[f] = Item.get(Item.AIR);
                f++;
            }
            List<Item> list = Arrays.asList(items);
            Collections.shuffle(list);
            items = (Item[])list.toArray(new Item[list.size()]);
            BlockEntity blockentity = this.level.getBlockEntity(e.getValue());
            if(blockentity instanceof BlockEntityChest){
                BaseInventory inventory = ((BlockEntityChest)blockentity).getInventory();
                for(int i = 0;i < 27;i++){
                    inventory.setItem(i, items[i]);
                }
            }
        }
    }
}