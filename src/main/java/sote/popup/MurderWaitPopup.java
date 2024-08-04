package sote.popup;

import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import sote.Game;
import sote.GameProvider;
import sote.inventory.Inventorys;
import sote.murder.Murder;
import sote.stat.Stat;

public class MurderWaitPopup extends Popup{

    public MurderWaitPopup(Player player) {
        super(player);
        update();
    }

    @Override
    public void update(){
        String tag = "";
        String space = "                                                        ";
        Game game = GameProvider.getPlayingGame(owner);
        if(!(game instanceof Murder)) return;
        Murder murder = (Murder) game;
        tag += space+"§l§ePlayers§r\n";
        if(game.getGameDataAsInt("count")  < Murder.MIN_PLAYERS){
            tag += space+"§c"+game.getGameDataAsInt("count")+" §7/ "+Murder.MAX_PLAYERS+"\n\n";
        }else{
            tag += space+"§a"+game.getGameDataAsInt("count")+" §7/ "+Murder.MAX_PLAYERS+"\n\n";
        }
        tag += space+"§l§eYour Stats§r\n";
        tag += space+"§fCoins: §a"+Stat.getCoin(this.owner)+"\n";
        tag += space+"§fKarma: §a"+Stat.getMurderKarma(this.owner)+"\n\n";
        tag += space+"§l§eRole Chance§r\n";
        double[] chances = getRoleChance();
        tag += space+"§fMurderer: §a"+getResult(chances[0])+"%\n";
        tag += space+"§fDetective: §a"+getResult(chances[1])+"%\n";
        tag += space+"§fBystander: §a"+getResult(chances[2])+"%\n\n\n\n\n\n\n";
        if(Inventorys.getGUI(this.owner) == Inventorys.GUI_CLASSIC) tag += "\n\n\n\n\n";
        this.txt = tag;
    }

    public double[] getRoleChance(){
        Game game = GameProvider.getPlayingGame(owner);
        Murder murder = (Murder) game;
        int all = 0;
        int min  = 1000;
        int ms = 0;
        for(Map.Entry<String,Player> e : murder.Players.entrySet()){
            if(min >= (int)Math.floor(Stat.getMurderKarma(e.getValue()) / 3)){
                min = (int)Math.floor(Stat.getMurderKarma(e.getValue()) / 3);
            }
        }
        ms = 10 - min;
        if(ms < 0) ms = 0;
        HashMap<Player,Integer> map = new HashMap<Player,Integer>();
        for(Map.Entry<String,Player> e : murder.Players.entrySet()){
             map.put(e.getValue(),(int)Math.floor(Stat.getMurderKarma(e.getValue()) / 3)+ms);
             all += ((int)Math.floor(Stat.getMurderKarma(e.getValue()) / 3)+ms);
        }
        double murdererChance = ((double)((int)Math.floor(Stat.getMurderKarma(owner) / 3)+ms) * 100 / all);
        double detectiveChance = 0;
        double targetMurdererChance = 0;
        for(Map.Entry<Player, Integer> e : map.entrySet()){
            if(e.getKey().equals(owner)) continue;
            targetMurdererChance = (double)e.getValue() / all;
            int all2 = 0;
            for(Map.Entry<Player,Integer> ee : map.entrySet()){
                if(!ee.getKey().getName().equals(e.getKey().getName())){
                    all2 += (400 - Math.abs(map.get(e.getKey()) - ee.getValue()));
                }
            }
            detectiveChance += targetMurdererChance * ((double)((400 - Math.abs(e.getValue() - map.get(owner))) * 100) / all2);
        }
        double bystanderChance = 100 - murdererChance - detectiveChance;
        return new double[]{murdererChance, detectiveChance, bystanderChance};
    }

    public double getResult(double a){
        int b = (int)(a * 1000);
        int c = b % 10;
        b -= c;
        if( c >= 5) b += 10;
        return ((double)b)/1000;
    }
}