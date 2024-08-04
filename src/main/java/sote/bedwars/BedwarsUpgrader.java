package sote.bedwars;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import sote.PlayerDataManager;

public class BedwarsUpgrader {

    public static final int UPGRADE_FORGE = 0;
    public static final int UPGRADE_MANIAC_MINER = 1;
    public static final int UPGRADE_SHARPENED_SOWRDS = 2;
    public static final int UPGRADE_REINFORCED_ARMOR = 3;
    public static final int UPGRADE_TRAP = 4;
    public static final int UPGRADE_MINER_TRAP = 5;
    public static final int UPGRADE_HEAL_POOL = 6;
    public static final int UPGRADE_DRAGON_BUFF = 7;

    public BedwarsUpgrader(Bedwars owner, int team){
        this.owner = owner;
        this.team = team;
        this.levels.put(UPGRADE_FORGE, 0);
        this.levels.put(UPGRADE_MANIAC_MINER, 0);
        this.levels.put(UPGRADE_SHARPENED_SOWRDS, 0);
        this.levels.put(UPGRADE_REINFORCED_ARMOR, 0);
        this.levels.put(UPGRADE_TRAP, 0);
        this.levels.put(UPGRADE_MINER_TRAP, 0);
        this.levels.put(UPGRADE_HEAL_POOL, 0);
        this.levels.put(UPGRADE_DRAGON_BUFF, 0);
        this.register();
    }

    public void register() {
        registerUpgraderItem();
        registerUpgraderPrice();
    }

    public void registerUpgraderItem() {
        Item item;
        item = Item.get(Item.FURNACE, 0, 1);//furnace
        items.put(UPGRADE_FORGE, item);
        maxLevel.put(UPGRADE_FORGE, 4);

        item = Item.get(Item.GOLD_PICKAXE, 0, 1);//gold_pix
        items.put(UPGRADE_MANIAC_MINER, item);
        maxLevel.put(UPGRADE_MANIAC_MINER, 1);

        item = Item.get(Item.IRON_SWORD, 0, 1);//iron_sword
        items.put(UPGRADE_SHARPENED_SOWRDS, item);
        maxLevel.put(UPGRADE_SHARPENED_SOWRDS, 1);

        item = Item.get(Item.IRON_CHESTPLATE, 0, 1);//iron_chestplate
        items.put(UPGRADE_REINFORCED_ARMOR, item);
        maxLevel.put(UPGRADE_REINFORCED_ARMOR, 4);

        item = Item.get(Item.TRIPWIRE_HOOK, 0, 1);//hook
        items.put(UPGRADE_TRAP, item);
        maxLevel.put(UPGRADE_TRAP, 0);

        item = Item.get(Item.IRON_PICKAXE, 0, 1);//iron_pickaxe
        items.put(UPGRADE_MINER_TRAP, item);
        maxLevel.put(UPGRADE_MINER_TRAP, 1);

        item = Item.get(Item.BEACON, 0, 1);//beacon
        items.put(UPGRADE_HEAL_POOL, item);
        maxLevel.put(UPGRADE_HEAL_POOL, 1);

        item = Item.get(Item.DRAGON_EGG, 0, 1);//dragon_egg
        items.put(UPGRADE_DRAGON_BUFF, item);
        maxLevel.put(UPGRADE_DRAGON_BUFF, 1);
    }

    public void registerUpgraderPrice(){
        HashMap<Integer, Integer> price;

        if(this.owner.getMode() == Bedwars.MODE_SOLO || this.owner.getMode() == Bedwars.MODE_DOUBLES){
            price = new HashMap<>();
            price.put(1, 2);
            price.put(2, 4);
            price.put(3, 6);
            price.put(4, 8);
            prices.put(UPGRADE_FORGE, price);

            price = new HashMap<>();
            price.put(1, 2);
            prices.put(UPGRADE_MANIAC_MINER, price);

            price = new HashMap<>();
            price.put(1, 4);
            prices.put(UPGRADE_SHARPENED_SOWRDS, price);

            price = new HashMap<>();
            price.put(1, 2);
            price.put(2, 4);
            price.put(3, 8);
            price.put(4, 16);
            prices.put(UPGRADE_REINFORCED_ARMOR, price);

            price = new HashMap<>();
            price.put(1, 1);
            prices.put(UPGRADE_TRAP, price);

            price = new HashMap<>();
            price.put(1, 2);
            prices.put(UPGRADE_MINER_TRAP, price);

            price = new HashMap<>();
            price.put(1, 1);
            prices.put(UPGRADE_HEAL_POOL, price);

            price = new HashMap<>();
            price.put(1, 3);
            prices.put(UPGRADE_DRAGON_BUFF, price);
        }else{
            price = new HashMap<>();
            price.put(1, 4);
            price.put(2, 8);
            price.put(3, 12);
            price.put(4, 16);
            prices.put(UPGRADE_FORGE, price);

            price = new HashMap<>();
            price.put(1, 4);
            prices.put(UPGRADE_MANIAC_MINER, price);

            price = new HashMap<>();
            price.put(1, 8);
            prices.put(UPGRADE_SHARPENED_SOWRDS, price);

            price = new HashMap<>();
            price.put(1, 5);
            price.put(2, 10);
            price.put(3, 20);
            price.put(4, 30);
            prices.put(UPGRADE_REINFORCED_ARMOR, price);

            price = new HashMap<>();
            price.put(1, 1);
            prices.put(UPGRADE_TRAP, price);

            price = new HashMap<>();
            price.put(1, 4);
            prices.put(UPGRADE_MINER_TRAP, price);

            price = new HashMap<>();
            price.put(1, 3);
            prices.put(UPGRADE_HEAL_POOL, price);

            price = new HashMap<>();
            price.put(1, 5);
            prices.put(UPGRADE_DRAGON_BUFF, price);
        }
    }

    public void upgrade(Player player, int type) {
        if(this.levels.get(type) == this.maxLevel.get(type)) return;
        System.out.println("ok1");
        if(player.getInventory().contains(Item.get(Item.DIAMOND, 0, prices.get(type).get(this.levels.get(type) + 1)))) {
            System.out.println("contains");
            player.getInventory().removeItem(Item.get(Item.DIAMOND, 0, prices.get(type).get(this.levels.get(type) + 1)));
            player.getInventory().sendContents(player);
            //TODO TEAM MESSAGNE
            this.levels.put(type, this.levels.get(type) + 1);
            switch(type) {
                case UPGRADE_FORGE:
                    upgradeForge();
                break;
                case UPGRADE_MANIAC_MINER:
                    upgradeManiacMiner();
                break;
                case UPGRADE_SHARPENED_SOWRDS:
                    upgradeSharpenedSwords();
                break;
                case UPGRADE_REINFORCED_ARMOR:
                    upgradeReinforcedArmor();
                break;
                case UPGRADE_TRAP:
                    upgradeTrap();
                break;
                case UPGRADE_MINER_TRAP:
                    upgradeMinerTrap();
                break;
                case UPGRADE_HEAL_POOL:
                    upgradeHealPool();
                break;
                case UPGRADE_DRAGON_BUFF:
                    upgradeDragonBuff();
                break;
            }
         }else{
              //player.sendMessage();
         }
    }

    public void upgradeForge(){
    }

    public void upgradeManiacMiner(){
    }

    public void upgradeSharpenedSwords(){
        for (Map.Entry<String,Player> e : this.owner.Players.entrySet()){
            if(this.owner.team.get(PlayerDataManager.getPlayerData(e.getValue())) == team){
                for(Map.Entry<Integer, Item> ee : e.getValue().getInventory().getContents().entrySet()){
                    if(ee.getValue().isSword()){
                        Enchantment ench = Enchantment.getEnchantment(Enchantment.ID_DAMAGE_ALL);
                        ench.setLevel(1);
                        ee.getValue().addEnchantment(ench);
                    }
                }
            }
        }
    }

    public void upgradeReinforcedArmor(){
        for (Map.Entry<String,Player> e : this.owner.Players.entrySet()){
            if(this.owner.team.get(PlayerDataManager.getPlayerData(e.getValue())) == team){
                this.owner.reinforcedArmor(e.getValue());
            }
        }
    }

    public void upgradeTrap(){
    }

    public void upgradeMinerTrap(){
    }

    public void upgradeHealPool(){
    }

    public void upgradeDragonBuff(){
    }

    public HashMap<Integer, Integer> levels = new HashMap<>();
    public Bedwars owner;
    public int team;

    public LinkedHashMap<Integer, Item> items = new LinkedHashMap<>();
    public HashMap<Integer, HashMap<Integer, Integer>> prices = new HashMap<>();
    public HashMap<Integer, Integer> maxLevel = new HashMap<>();

}
