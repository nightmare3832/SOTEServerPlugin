package sote.popup;

import cn.nukkit.Player;
import sote.Game;
import sote.GameProvider;
import sote.inventory.Inventorys;
import sote.skywarssolo.SkywarsSolo;

public class SkywarsGamePopup extends Popup{

    public SkywarsGamePopup(Player player) {
        super(player);
        update();
    }

    @Override
    public void update(){
        String tag = "";
        String space = "                                                        ";
        Game game = GameProvider.getPlayingGame(this.owner);
        if(!(game instanceof SkywarsSolo)) return;
        SkywarsSolo skywars = (SkywarsSolo) game;
        if(game.getGameDataAsBoolean("cagenow")){
            tag += space+"§b§lSkyWars§r\n\n";
            tag += space+"§fPlayers: §a"+game.getGameDataAsInt("lifecount")+"\n\n";
            tag += space+"§fStarting in §a"+game.getGameDataAsInt("cagetime")+"s\n\n\n\n\n\n\n\n";
            if(Inventorys.getGUI(this.owner) == Inventorys.GUI_CLASSIC) tag += "\n\n\n\n\n";
        }else{
            tag += space+"§b§lSkyWars§r\n\n";
            tag += space+"§fTime Left\n";
            tag += space+"§a"+game.getGameDataAsInt("gametime")+"\n";
            tag += space+"§fPlayers Left\n";
            tag += space+"§a"+game.getGameDataAsInt("lifecount")+"\n";
            tag += space+"§fKills\n";
            tag += space+"§a"+skywars.getKillCount(this.owner)+"\n\n\n\n\n\n\n\n";
            if(Inventorys.getGUI(this.owner) == Inventorys.GUI_CLASSIC) tag += "\n\n\n\n\n";
        }
        this.txt = tag;
    }
}