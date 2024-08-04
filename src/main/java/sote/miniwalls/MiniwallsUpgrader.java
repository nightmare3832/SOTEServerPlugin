package sote.miniwalls;

import cn.nukkit.Player;
import sote.Main;
import sote.inventory.ServerChestInventorys;
import sote.stat.Stat;

public class MiniwallsUpgrader{

    public static final int TYPE_SOLDIER = 0;
    public static final int TYPE_ARCHER = 1;
    public static final int TYPE_BUILDER = 2;

    public static final int[] PRICE = new int[]{
        0,
        100,//Level1
        1500,//Level2
        5000,//Level3
        10000,//Level4
        20000,//Level5
        50000//Level6
    };

    public static final int MAX_LEVEL = 6;

    public MiniwallsUpgrader(){
    }

    public static void Upgrade(Player player, int type, int level){
        int nowlevel = 0;
        if(type == TYPE_SOLDIER) nowlevel = Stat.getMiniwallsSoldierLevel(player);
        else if(type == TYPE_ARCHER) nowlevel = Stat.getMiniwallsArcherLevel(player);
        else if(type == TYPE_BUILDER) nowlevel = Stat.getMiniwallsBuilderLevel(player);
        if(level > MAX_LEVEL){
            player.sendTip(Main.getMessage(player, "miniwalls.upgrader.maxlevel"));
        }else if(level <= nowlevel){
            player.sendTip(Main.getMessage(player, "miniwalls.upgrader.already.buy"));
        }else if(nowlevel + 1 < level){
            player.sendTip(Main.getMessage(player, "miniwalls.upgrader.cant.buy"));
        }else if(nowlevel + 1 == level){
            int price = getPrice(level);
            int coin = Stat.getCoin(player);
            if(coin >= price){
                if(type == TYPE_SOLDIER) Stat.setMiniwallsSoldierLevel(player, level);
                else if(type == TYPE_ARCHER) Stat.setMiniwallsArcherLevel(player, level);
                else if(type == TYPE_BUILDER) Stat.setMiniwallsBuilderLevel(player, level);
                Stat.setCoin(player, coin - price);
                player.sendTip(Main.getMessage(player, "miniwalls.upgrader.success."+level));
                ServerChestInventorys.data2.get(player).update();
            }else{
                player.sendTip(Main.getMessage(player, "miniwalls.upgrader.coin.not.enough"));
            }
        }
    }

    public static int getPrice(int level){
        return PRICE[level];
    }
}