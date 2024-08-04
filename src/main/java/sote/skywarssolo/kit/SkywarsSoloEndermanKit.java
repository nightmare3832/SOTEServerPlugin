package sote.skywarssolo.kit;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemColorArmor;
import cn.nukkit.utils.DyeColor;

public class SkywarsSoloEndermanKit extends SkywarsSoloKit{

    public SkywarsSoloEndermanKit(Player player){
        super(player);
    }

    @Override
    public Item[] getItems(){
        Item letherCap = Item.get(Item.LEATHER_CAP, 0, 1);
        ((ItemColorArmor)letherCap).setColor(DyeColor.BLACK);
        Item letherPants = Item.get(Item.LEATHER_PANTS, 0, 1);
        ((ItemColorArmor)letherPants).setColor(DyeColor.PURPLE);
        Item enderPearl = Item.get(Item.ENDER_PEARL, 0, 1);
        return new Item[]{letherCap, letherPants, enderPearl};
    }

    @Override
    public void onAttack(Player target){
        
    }

    @Override
    public void onUseItem(){
        
    }
}