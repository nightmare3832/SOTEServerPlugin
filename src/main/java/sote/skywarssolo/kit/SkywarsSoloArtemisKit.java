package sote.skywarssolo.kit;

import cn.nukkit.Player;
import cn.nukkit.item.Item;

public class SkywarsSoloArtemisKit extends SkywarsSoloKit{

    public SkywarsSoloArtemisKit(Player player){
        super(player);
    }

    @Override
    public Item[] getItems(){
        Item bow = Item.get(Item.BOW,0 ,1);
        bow.setCustomName("Artemis's bow");
        Item arrow = Item.get(Item.ARROW, 0, 4);
        return new Item[]{bow, arrow};
    }

    @Override
    public void onAttack(Player target){
        
    }

    @Override
    public void onUseItem(){
        
    }
}