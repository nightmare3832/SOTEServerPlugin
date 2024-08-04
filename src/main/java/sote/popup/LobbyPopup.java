package sote.popup;

import cn.nukkit.Player;
import sote.Main;
import sote.inventory.Inventorys;
import sote.stat.Stat;

public class LobbyPopup extends Popup{

    public LobbyPopup(Player player) {
        super(player);
        update();
    }

    @Override
    public void update(){
        String tag = "";
        String space = "                                                        ";
        tag += space+Main.SERVER_NAME+"\n";
        tag += space+"§6coin§8: §e"+Stat.getCoin(this.owner)+"\n";
        tag += space+"§2level§8: §a"+Stat.getLevel(this.owner)+"§f\n\n\n\n\n\n\n";//2//7
        if(Inventorys.getGUI(this.owner) == Inventorys.GUI_CLASSIC) tag += "\n\n\n\n\n";
        this.txt = tag;
    }
}