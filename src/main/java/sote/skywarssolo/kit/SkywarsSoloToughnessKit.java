package sote.skywarssolo.kit;

import cn.nukkit.Player;
import cn.nukkit.item.Item;

public class SkywarsSoloToughnessKit extends SkywarsSoloKit{

    public SkywarsSoloToughnessKit(Player player){
        super(player);
    }

    @Override
    public Item[] getItems(){
        Item goldHelmet = Item.get(Item.GOLD_HELMET, 0, 1);
        Item goldChestplate = Item.get(Item.GOLD_CHESTPLATE, 0, 1);
        Item goldLeggings = Item.get(Item.GOLD_LEGGINGS, 0, 1);
        return new Item[]{goldHelmet, goldChestplate, goldLeggings};
    }

    @Override
    public void onAttack(Player target){
        
    }

    @Override
    public void onUseItem(){
        
    }
}