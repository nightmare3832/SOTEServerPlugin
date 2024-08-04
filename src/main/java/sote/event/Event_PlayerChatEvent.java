package sote.event;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.scheduler.Task;
import sote.Game;
import sote.GameProvider;
import sote.Main;
import sote.ban.Ban;
import sote.buildbattle.Buildbattle;
import sote.login.LoginSystem;
import sote.miniwalls.Miniwalls;
import sote.murder.Murder;
import sote.setting.Setting;
import sote.skywarssolo.SkywarsSolo;

public class Event_PlayerChatEvent{

    public Event_PlayerChatEvent(){
    }

    public static void run(){
        Server.getInstance().getScheduler().scheduleRepeatingTask(new CallbackChat(),200);
    }

    public static void onChat(PlayerChatEvent event){
        Player player = event.getPlayer();
        if(!Ban.canChat(player)){
            return;
        }
        Game game = GameProvider.getPlayingGame(player);
        if(game instanceof Murder){
            Murder murder = (Murder) game;
            murder.chat(event);
        }else if(game instanceof SkywarsSolo){
            SkywarsSolo skywars = (SkywarsSolo) game;
            skywars.chat(event);
        }else if(game instanceof Buildbattle){
            Buildbattle buildbattle = (Buildbattle) game;
            buildbattle.chat(event);
        }else if(game instanceof Miniwalls){
            Miniwalls miniwalls = (Miniwalls) game;
            miniwalls.chat(event);
        }
        if(event.isCancelled()) return;
        if(LoginSystem.auth.get(player.getName().toLowerCase()) == 1){
            event.setCancelled();
            return;
        }
        if(Main.Chat.get(player.getName()) <= 0){
            player.sendMessage(Main.getMessage(player,"chat.wait"));
            event.setCancelled();
        }else{
            Map<UUID,Player> players = Server.getInstance().getOnlinePlayers();
            for (Map.Entry<UUID,Player> e : players.entrySet()){
                if(player.getLevel().getName().equals(e.getValue().getLevel().getName()) && !Setting.getChatHide(e.getValue())){
                    e.getValue().sendMessage("<"+player.getNameTag()+"§f> "+event.getMessage());
                }
            }
            event.setCancelled();
        }
        int ch = Main.Chat.get(player.getName());
        Main.Chat.put(player.getName(),ch-1);
        Server.getInstance().getLogger().info("<"+player.getNameTag()+"§f> "+event.getMessage());
    }

    public static void addChat(){
        for(Player player : new ArrayList<>(Server.getInstance().getOnlinePlayers().values())){
            if(player instanceof Player){
                if(!Main.Chat.containsKey(player.getName())){
                    Main.Chat.put(player.getName(), 3);
                }
                if(Main.Chat.get(player.getName()) < 3){
                    int ch = Main.Chat.get(player.getName());
                    Main.Chat.put(player.getName(),ch+1);
                }
            }
        }
    }
}
class CallbackChat extends Task{

    public CallbackChat(){
    }

    public void onRun(int d){
        go();
    }

    public void go(){
        Event_PlayerChatEvent.addChat();
    }
}