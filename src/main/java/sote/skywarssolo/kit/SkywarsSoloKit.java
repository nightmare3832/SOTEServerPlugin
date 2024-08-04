package sote.skywarssolo.kit;

import cn.nukkit.Player;
import cn.nukkit.item.Item;

public abstract class SkywarsSoloKit{

    public SkywarsSoloKit(Player player){
        this.owner = player;
    }

    public Item[] getItems(){
        return new Item[]{};
    }

    public void onAttack(Player target){
        
    }

    public void onUseItem(){
        
    }

    public Player owner;

}