package sote.skywarssolo.kit;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemPotion;

public class SkywarsSoloOdinKit extends SkywarsSoloKit{

    public SkywarsSoloOdinKit(Player player){
        super(player);
    }

    @Override
    public Item[] getItems(){
        Item netherBrick = Item.get(Item.NETHER_BRICK, 0, 1);//TODO potion
        netherBrick.setCustomName("Odin's gungnir");
        Item potionStrength = Item.get(Item.POTION, ItemPotion.STRENGTH, 1);
        return new Item[]{netherBrick, potionStrength};
    }

    @Override
    public void onAttack(Player target){
        
    }

    @Override
    public void onUseItem(){
        
    }
}