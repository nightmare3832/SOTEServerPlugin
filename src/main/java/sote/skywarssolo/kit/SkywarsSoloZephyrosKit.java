package sote.skywarssolo.kit;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemPotion;
import cn.nukkit.item.enchantment.Enchantment;

public class SkywarsSoloZephyrosKit extends SkywarsSoloKit{

    public SkywarsSoloZephyrosKit(Player player){
        super(player);
    }

    @Override
    public Item[] getItems(){
        Item goldHoe = Item.get(Item.GOLD_HOE, 0, 1);
        Enchantment enchantment = Enchantment.get(Enchantment.ID_KNOCKBACK);
        enchantment.setLevel(2);
        goldHoe.addEnchantment(enchantment);
        goldHoe.setCustomName("Zephyros's wand");
        Item potionLeaping = Item.get(Item.POTION, ItemPotion.LEAPING_II, 1);
        return new Item[]{goldHoe, potionLeaping};
    }

    @Override
    public void onAttack(Player target){
        
    }

    @Override
    public void onUseItem(){
        
    }
}