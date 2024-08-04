package sote.popup;

import java.util.LinkedHashMap;

import cn.nukkit.Player;

public class Popups{

    public Popups(){
    }

    public static void addPlayer(Player player){
        setData(player,new WallsPopup(player));
    }

    public static void setData(Player player,Popup pop){
        data.put(player,pop);
    }

    public static LinkedHashMap<Player, Popup> data = new LinkedHashMap<Player, Popup>();

}