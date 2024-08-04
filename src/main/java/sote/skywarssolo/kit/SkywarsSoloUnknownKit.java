package sote.skywarssolo.kit;

import cn.nukkit.Player;
import cn.nukkit.item.Item;

public class SkywarsSoloUnknownKit extends SkywarsSoloKit{

    public SkywarsSoloUnknownKit(Player player){
        super(player);
    }

    @Override
    public Item[] getItems(){
        return new Item[]{};
    }

    @Override
    public void onAttack(Player target){
        
    }

    @Override
    public void onUseItem(){
        
    }
}