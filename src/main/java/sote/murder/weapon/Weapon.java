package sote.murder.weapon;

import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.item.Item;

public abstract class Weapon{

    public Weapon(){
    }

    public void Shot(Player player){
        
    }

    public void despawn(Player player,EntityProjectile projectile){
        
    }

    public Item getItem(Player player){
        return Item.get(0);
    }

    public EntityItem getDropItem(){
        next = next+1;
        return dropitem.get(next-1);
    }

    public void setDropItem(EntityItem item){
        dropitem.put(dropitem.size(),item);
    }

    public int getChargeTime(){
        return 0;
    }

    public int getReturnTime(){
        return 0;
    }

    public int getLevel(){
        return level;
    }

    public void setLevel(int num){
        level = num;
    }

    public boolean getCharged(){
        return charged;
    }

    public void setCharged(boolean bool){
        charged = bool;
    }

    public static boolean charged = true;;
    public static int level = 1;
    public static int next = 0;
    public static HashMap<Integer,EntityItem> dropitem = new HashMap<Integer,EntityItem>();

}