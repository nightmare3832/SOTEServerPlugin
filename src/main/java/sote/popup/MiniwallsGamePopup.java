package sote.popup;

import cn.nukkit.Player;
import sote.Game;
import sote.GameProvider;
import sote.inventory.Inventorys;
import sote.miniwalls.Miniwalls;

public class MiniwallsGamePopup extends Popup{

    public MiniwallsGamePopup(Player player) {
        super(player);
        update();
    }

    @Override
    public void update(){
        Game game = GameProvider.getPlayingGame(this.owner);
        if(!(game instanceof Miniwalls) || !(this.owner instanceof Player)) return;
        Miniwalls miniwalls = (Miniwalls) game;
        String space = "                                                        ";
        String tag = "";
        int time;
        int[] member = miniwalls.getMember();
        if(!game.getGameDataAsBoolean("wallnow")){
            time = game.getGameDataAsInt("gametime");
            int minutes = (int)Math.floor(time / 60);
            int seconds = time % 60;
            if(seconds < 10) tag += space+"Deathmatch: "+minutes+":0"+seconds+"\n\n";
            else tag += space+"Deathmatch: "+minutes+":"+seconds+"\n\n";
        }else{
            time = game.getGameDataAsInt("walltime");
            int minutes = (int)Math.floor(time / 60);
            int seconds = time % 60;
            if(seconds < 10) tag += space+"Walls Fall: "+minutes+":0"+seconds+"\n\n";
            else tag += space+"Walls Fall: "+minutes+":"+seconds+"\n\n";
        }
        int health;
        String hel;
        health = (int)(miniwalls.wither.get(0).HitPoint * 100 / miniwalls.wither.get(0).MAX_HP);
        hel = "";
        if(health > 50) hel += "§a";
        else if(health <= 50 && health > 10) hel += "§e";
        else hel += "§c";
        hel += ""+health;
        if(health > 0) tag += space+"§cWither Health: "+hel+"%%%%%\n";
        else tag += space+"§cPlayerLeft: "+member[0]+"\n";
        health = (int)(miniwalls.wither.get(1).HitPoint * 100 / miniwalls.wither.get(1).MAX_HP);
        hel = "";
        if(health > 50) hel += "§a";
        else if(health <= 50 && health > 10) hel += "§e";
        else hel += "§c";
        hel += ""+health;
        if(health > 0) tag += space+"§bWither Health: "+hel+"%%%%%\n";
        else tag += space+"§bPlayerLeft: "+member[1]+"\n";
        health = (int)(miniwalls.wither.get(2).HitPoint * 100 / miniwalls.wither.get(2).MAX_HP);
        hel = "";
        if(health > 50) hel += "§a";
        else if(health <= 50 && health > 10) hel += "§e";
        else hel += "§c";
        hel += ""+health;
        if(health > 0) tag += space+"§aWither Health: "+hel+"%%%%%\n";
        else tag += space+"§aPlayerLeft: "+member[2]+"\n";
        health = (int)(miniwalls.wither.get(3).HitPoint * 100 / miniwalls.wither.get(3).MAX_HP);
        hel = "";
        if(health > 50) hel += "§a";
        else if(health <= 50 && health > 10) hel += "§e";
        else hel += "§c";
        hel += ""+health;
        if(health > 0) tag += space+"§eWither Health: "+hel+"%%%%%\n\n";
        else tag += space+"§ePlayerLeft: "+member[3]+"\n\n";
        tag += space+"§fKills: "+miniwalls.killcount.get(this.owner)+"\n\n";
        tag += space+"§fMap: §a"+miniwalls.stage.getName()+"\n\n";
        tag += space+"GameID: §f"+miniwalls.number+"\n\n\n\n\n\n\n\n\n";//14//9
        if(Inventorys.getGUI(this.owner) == Inventorys.GUI_CLASSIC) tag += "\n\n\n\n\n";
        this.txt = tag;
    }
}