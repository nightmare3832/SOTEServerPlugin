package sote.skywarssolo.kit;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemColorArmor;
import cn.nukkit.utils.DyeColor;

public class SkywarsSoloSnowmanKit extends SkywarsSoloKit{

    public SkywarsSoloSnowmanKit(Player player){
        super(player);
    }

    @Override
    public Item[] getItems(){
        Item letherCap = Item.get(Item.LEATHER_CAP, 0, 1);
        ((ItemColorArmor)letherCap).setColor(DyeColor.WHITE);
        Item letherTunic = Item.get(Item.LEATHER_TUNIC, 0, 1);
        ((ItemColorArmor)letherTunic).setColor(DyeColor.WHITE);
        Item snowball = Item.get(Item.SNOWBALL, 0, 8);
        return new Item[]{letherCap, letherTunic, snowball};
    }

    @Override
    public void onAttack(Player target){
        
    }

    @Override
    public void onUseItem(){
        
    }
}