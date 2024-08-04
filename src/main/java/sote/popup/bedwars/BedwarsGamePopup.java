package sote.popup.bedwars;

import java.util.Map;

import cn.nukkit.Player;
import sote.Game;
import sote.GameProvider;
import sote.PlayerDataManager;
import sote.TeamUtil;
import sote.bedwars.Bedwars;
import sote.popup.Popup;

public class BedwarsGamePopup extends Popup{

    public BedwarsGamePopup(Player player) {
        super(player);
        update();
    }

    @Override
    public void update(){
        String tag = "";
        String space = "                                                                            ";
        Game game = GameProvider.getPlayingGame(owner);
        if(!(game instanceof Bedwars)) return;
        Bedwars bedwars = (Bedwars) game;
        tag += space + "§l§eBEDWARS§r\n\n";
        tag += space + "§f"+bedwars.nextEventName+":§r\n";
        tag += space + "§a"+getTimeString(bedwars.nextEventTime)+"§r\n\n";
        int team = bedwars.team.get(PlayerDataManager.getPlayerData(owner));
        int c = 8;
        if(bedwars.getMode() == bedwars.MODE_THREES || bedwars.getMode() == bedwars.MODE_FOURS) c = 4;
        for(int t = 0; t < c;t++){
            tag += space + TeamUtil.getColorCodeByNumber(t) + "§l" + TeamUtil.getShortNameByNumber(t) + " §r§f" + TeamUtil.getNameByNumber(t) + ": ";
            if(bedwars.isTeamEliminated.get(t)){
                tag += "§c✘";
            }else if(bedwars.isBedBroken.get(t)){
                int a = 0;
                for (Map.Entry<String,Player> e : bedwars.Players.entrySet()){
                    if(bedwars.team.get(PlayerDataManager.getPlayerData(e.getValue())) == t){
                        if(!bedwars.isEliminated.get(PlayerDataManager.getPlayerData(e.getValue()))){
                            a++;
                        }
                    }
                }
                tag += "§a" + a;
            }else{
                tag += "§a✔";
            }
            if(t == team) tag += "§7(You)";
            tag += "\n";
        }
        tag += "\n\n\n\n\n\n\n\n\n\n\n\n";
        this.txt = tag;
    }

    public String getTimeString(int time){
        int seconds = time % 60;
        int minutes = (int) ((time - seconds) / 60);
        String str = minutes + ":";
        if(seconds < 10) str += "0"+seconds;
        else str += seconds;
        if(minutes < 10) str = "0" + str;
        return str;
    }
}