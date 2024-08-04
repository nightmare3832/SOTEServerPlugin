package sote.skywarssolo.kit;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemColorArmor;
import cn.nukkit.utils.DyeColor;

public class SkywarsSoloBomberKit extends SkywarsSoloKit{

    public SkywarsSoloBomberKit(Player player){
        super(player);
    }

    @Override
    public Item[] getItems(){
        Item letherCap = Item.get(Item.LEATHER_CAP, 0, 1);
        ((ItemColorArmor)letherCap).setColor(DyeColor.RED);
        Item letherTunic = Item.get(Item.LEATHER_TUNIC, 0, 1);
        ((ItemColorArmor)letherTunic).setColor(DyeColor.YELLOW);
        Item tnt = Item.get(Item.TNT, 0, 8);
        Item redstoneBlock = Item.get(Item.REDSTONE_BLOCK, 0, 8);
        return new Item[]{letherCap, letherTunic, tnt, redstoneBlock};
    }

    @Override
    public void onAttack(Player target){
        
    }

    @Override
    public void onUseItem(){
        
    }
}