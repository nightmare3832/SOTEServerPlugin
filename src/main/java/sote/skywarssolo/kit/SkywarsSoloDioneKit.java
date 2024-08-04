package sote.skywarssolo.kit;

import cn.nukkit.Player;
import cn.nukkit.item.Item;

public class SkywarsSoloDioneKit extends SkywarsSoloKit{

    public SkywarsSoloDioneKit(Player player){
        super(player);
    }

    @Override
    public Item[] getItems(){
        Item feather = Item.get(Item.FEATHER,0 ,1);
        return new Item[]{feather};
    }

    @Override
    public void onAttack(Player target){
        
    }

    @Override
    public void onUseItem(){
        
    }
}