package sote.event;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.scheduler.Task;
import sote.Game;
import sote.GameProvider;
import sote.Main;
import sote.MySQL;
import sote.OPStatue;
import sote.login.LoginSystem;
import sote.party.Party;
import sote.serverlist.ServerListAPI;

public class Event_PlayerQuitEvent{

    public Event_PlayerQuitEvent(){
    }

    public static void onPlayerQuit(PlayerQuitEvent event){
        ServerListAPI.updatePlayers("quit");
        Player player = event.getPlayer();
        if(player.getName().split(" ").length >= 2){
            return;
        }
        Game game = GameProvider.getPlayingGame(player);
        if(game instanceof Game){
            game.Quit(player);
        }
        event.setQuitMessage("");
        if(player.loggedIn && LoginSystem.auth.containsKey(player.getName().toLowerCase()) && LoginSystem.auth.get(player.getName().toLowerCase()) != 1){
            MySQL.sendAllData(player);
            Party.leave(player);
        }
        Main.gen.finish(player);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackPacketQ(player),60);
    }

    public static void packet(Player player){
        OPStatue.updateStatue(player);
    }
}
class CallbackPacketQ extends Task{

    public static Player player;

    public CallbackPacketQ(Player p){
        player = p;
    }

    public void onRun(int d){
        go();
    }

    public void go(){
        Event_PlayerQuitEvent.packet(player);
    }
}