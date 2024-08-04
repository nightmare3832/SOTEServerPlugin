package sote.skywarssolo.kit;

import cn.nukkit.Player;
import cn.nukkit.item.Item;

public class SkywarsSoloHadesKit extends SkywarsSoloKit{

    public SkywarsSoloHadesKit(Player player){
        super(player);
    }

    @Override
    public Item[] getItems(){
        Item bone = Item.get(Item.BONE,0 ,1);
        return new Item[]{bone};
    }

    @Override
    public void onAttack(Player target){
        
    }

    @Override
    public void onUseItem(){
        
    }
}