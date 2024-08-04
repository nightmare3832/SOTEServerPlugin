package sote.skywarssolo.kit;

import cn.nukkit.Player;
import cn.nukkit.block.BlockWood;
import cn.nukkit.item.Item;

public class SkywarsSoloBuilderKit extends SkywarsSoloKit{

    public SkywarsSoloBuilderKit(Player player){
        super(player);
    }

    @Override
    public Item[] getItems(){
        Item stoneAxe = Item.get(Item.STONE_AXE, 0, 1);
        Item wood = Item.get(Item.WOOD, BlockWood.OAK, 16);
        return new Item[]{stoneAxe, wood};
    }

    @Override
    public void onAttack(Player target){
        
    }

    @Override
    public void onUseItem(){
        
    }
}