package sote.skywarssolo.kit;

import cn.nukkit.Player;
import cn.nukkit.item.Item;

public class SkywarsSoloArcherKit extends SkywarsSoloKit{

    public SkywarsSoloArcherKit(Player player){
        super(player);
    }

    @Override
    public Item[] getItems(){
        Item bow = Item.get(Item.BOW,0 ,1);
        Item arrow = Item.get(Item.ARROW, 0, 16);
        return new Item[]{bow, arrow};
    }

    @Override
    public void onAttack(Player target){
        
    }

    @Override
    public void onUseItem(){
        
    }
}