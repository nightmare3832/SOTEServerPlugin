package sote.inventory.bedwars;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemColorArmor;
import cn.nukkit.item.enchantment.Enchantment;
import sote.Game;
import sote.GameProvider;
import sote.Main;
import sote.PlayerDataManager;
import sote.TeamUtil;
import sote.bedwars.Bedwars;
import sote.bedwars.BedwarsUpgrader;
import sote.inventory.Inventory;

public class BedwarsInventory extends Inventory{

    public BedwarsInventory(){
    }

    @Override
    public void register(Player player){
        PlayerInventory inventory = ((EntityHuman)player).getInventory();
        Main.canArmor.put(player,true);
        inventory.clearAll();
        Main.canArmor.put(player,false);
        Item item;
        Game game = GameProvider.getPlayingGame(player);
        if(!(game instanceof Bedwars)) return;
        Bedwars bedwars = (Bedwars) game;
        item = Item.get(Item.WOODEN_SWORD, 0, 1);
        if(bedwars.upgraders.get(bedwars.team.get(PlayerDataManager.getPlayerData(player))).levels.get(BedwarsUpgrader.UPGRADE_SHARPENED_SOWRDS) == 1){
            Enchantment ench = Enchantment.getEnchantment(Enchantment.ID_DAMAGE_ALL);
            ench.setLevel(1);
            item.addEnchantment(ench);
        }
        inventory.setHotbarSlotIndex(0,0);
        inventory.setItem(0,item);
        if(bedwars.isHaveShears.get(PlayerDataManager.getPlayerData(player))){
            item = Item.get(Item.SHEARS, 0, 1);
            inventory.setHotbarSlotIndex(1,1);
            inventory.setItem(1,item);
        }
        int team = bedwars.team.get(PlayerDataManager.getPlayerData(player));
        Main.canArmor.put(player,true);
        item = Item.get(Item.LEATHER_CAP, 0, 1);
        TeamUtil.getArmorColor(TeamUtil.getColorCodeByNumber(team), (ItemColorArmor)item);
        inventory.setHelmet(item);
        TeamUtil.getArmorColor(TeamUtil.getColorCodeByNumber(team), (ItemColorArmor)item);
        item = Item.get(Item.LEATHER_TUNIC, 0, 1);
        inventory.setChestplate(item);
        switch(bedwars.haveArmor.get(PlayerDataManager.getPlayerData(player))){
            case Bedwars.ARMOR_LETHER:
                item = Item.get(Item.LEATHER_PANTS, 0, 1);
                TeamUtil.getArmorColor(TeamUtil.getColorCodeByNumber(team), (ItemColorArmor)item);
                inventory.setLeggings(item);
                item = Item.get(Item.LEATHER_BOOTS, 0, 1);
                TeamUtil.getArmorColor(TeamUtil.getColorCodeByNumber(team), (ItemColorArmor)item);
                inventory.setBoots(item);
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
        Main.canArmor.put(player,false);
        inventory.sendContents(player);
        inventory.sendArmorContents(player);
    }
}