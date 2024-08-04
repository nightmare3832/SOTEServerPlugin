package sote.skywarssolo.kit;

import cn.nukkit.Player;
import cn.nukkit.item.Item;

public class SkywarsSoloSoldierKit extends SkywarsSoloKit{

    public SkywarsSoloSoldierKit(Player player){
        super(player);
    }

    @Override
    public Item[] getItems(){
        Item stoneSword = Item.get(Item.STONE_SWORD, 0, 1);
        return new Item[]{stoneSword};
    }

    @Override
    public void onAttack(Player target){
        
    }

    @Override
    public void onUseItem(){
        
    }
}