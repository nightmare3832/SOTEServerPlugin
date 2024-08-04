package sote.popup;

import cn.nukkit.Player;
import cn.nukkit.item.Item;

public abstract class Popup{

    public Popup(Player player){
        this.owner = player;
        this.txt = "";
    }

    public void update(){
    }

    public String getPopup(){
        return this.txt;
    }

    public Player owner;
    public String txt;
}