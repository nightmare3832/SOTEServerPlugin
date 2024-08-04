package sote.skywarssolo.kit;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemPotion;

public class SkywarsSoloIndiraKit extends SkywarsSoloKit{

    public SkywarsSoloIndiraKit(Player player){
        super(player);
    }

    @Override
    public Item[] getItems(){
        Item goldSword = Item.get(Item.GOLD_SWORD, 0, 1);//TODO potion
        goldSword.setCustomName("Indira's sword");
        Item potionSpeed = Item.get(Item.POTION, ItemPotion.SPEED_II, 1);
        return new Item[]{goldSword, potionSpeed};
    }

    @Override
    public void onAttack(Player target){
        
    }

    @Override
    public void onUseItem(){
        
    }
}