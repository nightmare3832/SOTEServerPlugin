package sote.event;

import java.util.HashMap;

import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import sote.Main;
import sote.login.LoginSystem;
import sote.stat.Stat;

public class Event_PlayerCommandPreprocessEvent{

    public Event_PlayerCommandPreprocessEvent(){
    }

    public static void onCommand(PlayerCommandPreprocessEvent event){
        String msg = event.getMessage();
        String[] mm = msg.split(" ");
        String cmd = mm[0].substring(1).toLowerCase();
        HashMap<String,Integer> pp = new HashMap<String,Integer>();
        pp.put("gamemode",0);
        pp.put("give",0);
        pp.put("tp",0);
        pp.put("skin",0);
        pp.put("stop",0);
        pp.put("reload",0);
        pp.put("op",0);
        pp.put("deop",0);
        pp.put("kill",0);
        pp.put("nban",0);
        pp.put("kick",0);
        pp.put("world",0);
        pp.put("map",0);
        pp.put("yaw",0);
        pp.put("reloadlang",0);
        pp.put("skin",0);
        pp.put("setdata",0);
        pp.put("adddata",0);
        pp.put("/pos1",0);
        pp.put("/pos2",0);
        pp.put("/set",0);
        pp.put("/cut",0);
        pp.put("/replace",0);
        pp.put("/copy",0);
        pp.put("/paste",0);
        pp.put("/rotate",0);
        pp.put("/move",0);
        if(cmd.equals("help")) event.setCancelled();
        if(LoginSystem.auth.get(event.getPlayer().getName().toLowerCase()) == 1 && !cmd.equals("register") && !cmd.equals("login")) event.setCancelled();
            if(!Main.getBlock(event.getPlayer()) && pp.containsKey(cmd)){
                event.setCancelled();
            }
        if(cmd.equals("block") && Stat.getVip(event.getPlayer()) <= 10) event.setCancelled();
        if((cmd.equals("w") || cmd.equals("tell")) && Main.gamenow.get(event.getPlayer())) event.setCancelled();
    }
}