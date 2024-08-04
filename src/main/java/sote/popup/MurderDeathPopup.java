package sote.popup;

import cn.nukkit.Player;
import sote.Game;
import sote.GameProvider;
import sote.inventory.Inventorys;
import sote.murder.Murder;

public class MurderDeathPopup extends Popup{

    public MurderDeathPopup(Player player) {
        super(player);
        update();
    }

    @Override
    public void update(){
        String tag = "";
        String space = "                                                        ";
        Game game = GameProvider.getPlayingGame(this.owner);
        if(!(game instanceof Murder)) return;
        tag += space+"§4§lMurder§r\n\n";
        tag += space+"§9§lTime Left§r\n";
        tag += space+"§c"+game.getGameDataAsInt("gametime")+"\n";
        tag += space+"§2§lAlive§r\n";
        tag += space+"§a"+game.getGameDataAsInt("lifecount")+"\n";
        tag += space+"§4§lDead§r\n";
        tag += space+"§c"+game.getGameDataAsInt("deadcount")+"\n\n\n\n\n\n\n\n";
        if(Inventorys.getGUI(this.owner) == Inventorys.GUI_CLASSIC) tag += "\n\n\n\n\n";
        this.txt = tag;
    }
}