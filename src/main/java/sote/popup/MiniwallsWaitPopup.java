package sote.popup;

import cn.nukkit.Player;
import sote.Game;
import sote.GameProvider;
import sote.inventory.Inventorys;
import sote.miniwalls.Miniwalls;

public class MiniwallsWaitPopup extends Popup{

    public MiniwallsWaitPopup(Player player) {
        super(player);
        update();
    }

    @Override
    public void update(){
        Game game = GameProvider.getPlayingGame(this.owner);
        if(!(game instanceof Miniwalls)) return;
        String space = "                                                        ";
        String tag = "";
        tag += space+"Players: "+game.getGameDataAsInt("count")+"/"+Miniwalls.MAX_PLAYERS+"\n";
        tag += space+"GameID: §f"+game.number+"\n\n\n\n";
        if(Inventorys.getGUI(this.owner) == Inventorys.GUI_CLASSIC) tag += "\n\n\n\n\n";
        this.txt = tag;
    }
}