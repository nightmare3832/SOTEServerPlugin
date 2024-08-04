package sote.popup;

import cn.nukkit.Player;
import sote.Main;
import sote.inventory.Inventorys;

public class HomePopup extends Popup{

    public HomePopup(Player player) {
        super(player);
        update();
    }

    @Override
    public void update(){
        String tag = "";
        String space = "                                                        ";
        tag += space+Main.SERVER_NAME+"\n";
        tag += space+"§7Players§8: §e"+this.owner.getLevel().getPlayers().size()+"\n";
        if(Inventorys.getGUI(this.owner) == Inventorys.GUI_CLASSIC) tag += "\n\n\n\n\n";
        this.txt = tag;
    }
}