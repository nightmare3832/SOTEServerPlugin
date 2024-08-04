package sote.popup;

import cn.nukkit.Player;
import sote.Main;
import sote.inventory.Inventorys;
import sote.stat.Stat;

public class WallsPopup extends Popup{

    public WallsPopup(Player player) {
        super(player);
        update();
    }

    @Override
    public void update(){
        String tag = "";
        String space = "                                                        ";
        tag += space+Main.SERVER_NAME+"\n";
        tag += space+"§6coin§8: §e"+Stat.getCoin(this.owner)+"\n";
        tag += space+"§2level§8: §a"+Stat.getLevel(this.owner)+"§f\n\n\n\n";//2//7
        tag += "現在はSkyWarsをテスト中のためSkyWars以外のゲームは公開していません\nまた、テスト中のため再起動が多いです\n\n\n";
        if(Inventorys.getGUI(this.owner) == Inventorys.GUI_CLASSIC) tag += "\n\n\n\n\n";
        this.txt = tag;
    }
}