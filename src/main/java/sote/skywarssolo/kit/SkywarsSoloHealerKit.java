package sote.skywarssolo.kit;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemColorArmor;
import cn.nukkit.item.ItemPotion;
import cn.nukkit.utils.DyeColor;

public class SkywarsSoloHealerKit extends SkywarsSoloKit{

    public SkywarsSoloHealerKit(Player player){
        super(player);
    }

    @Override
    public Item[] getItems(){
        Item letherPants = Item.get(Item.LEATHER_PANTS, 0, 1);
        ((ItemColorArmor)letherPants).setColor(DyeColor.PINK);
        Item letherBoots = Item.get(Item.LEATHER_BOOTS, 0, 1);
        ((ItemColorArmor)letherBoots).setColor(DyeColor.PINK);
        Item potionRegeneration = Item.get(Item.POTION, ItemPotion.REGENERATION_II, 1);
        Item splashPotionInstantHealth = Item.get(Item.SPLASH_POTION, ItemPotion.INSTANT_HEALTH_II, 1);
        return new Item[]{letherPants, letherBoots, potionRegeneration, splashPotionInstantHealth};
    }

    @Override
    public void onAttack(Player target){
        
    }

    @Override
    public void onUseItem(){
        
    }
}