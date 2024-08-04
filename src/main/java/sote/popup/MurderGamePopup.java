package sote.popup;

import cn.nukkit.Player;
import sote.inventory.Inventorys;

public class MurderGamePopup extends Popup{

    public MurderGamePopup(Player player) {
        super(player);
        update();
    }

    @Override
    public void update(){
        String tag = "";
        String space = "                                                        ";
        tag += space+"§cMurder\n\n";
        tag += space+"§bSecretIdentity\n";
        tag += space+this.owner.getNameTag()+"§f\n\n\n\n\n\n\n";//2//7
        if(Inventorys.getGUI(this.owner) == Inventorys.GUI_CLASSIC) tag += "\n\n\n\n\n";
        this.txt = tag;
    }
}