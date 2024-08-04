package sote.party;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.internal.LinkedTreeMap;

import cn.nukkit.Player;
import cn.nukkit.Server;
import sote.Main;
import sote.PlayerDataManager;

public class Party{

    public Party(){
    }

    public static void addPlayer(Player player){
        LinkedTreeMap<String, String> v1 = new LinkedTreeMap<String, String>();
        v1.put("created","0");
        v1.put("owner","");
        v1.put("member","");
        v1.put("invite","");
        partyData.put(player.getName().toLowerCase(),v1);
    }

    public static void invite(Player player,String name){
        Map<String, String> mapp = partyData.get(player.getName().toLowerCase());
        if(mapp.get("owner").equals(player.getName().toLowerCase()) || mapp.get("owner").equals("")){//自分のpartyを持っているかどうか
            if(partyData.containsKey(name.toLowerCase())){//存在するプレイヤーかどうか
                Map<String, String> map = partyData.get(name.toLowerCase());
                if(map.get("created").equals("0")){//相手がpartyに参加してるかどうか
                    String[] invites = map.get("invite").split(",");
                    Boolean a = false;
                    for(String party:invites){
                        if(party.equals(player.getName().toLowerCase())){
                            a = true;
                        }
                    }
                    if(!a){//既に招待してるかどうか
                        if(map.get("invite").equals("")) map.put("invite",player.getName().toLowerCase());
                        else map.put("invite", map.get("invite")+","+player.getName().toLowerCase());
                        mapp.put("created","1");
                        mapp.put("owner",player.getName().toLowerCase());
                        if(mapp.get("member").equals("")) mapp.put("member",player.getName().toLowerCase());
                        player.sendMessage(Main.getMessage(player, "party.invite",new String[]{PlayerDataManager.getPlayerData(name).getName()}));
                        Player target = Server.getInstance().getPlayer(name);
                        if(target instanceof Player){//オンラインかどうか
                            target.sendMessage(Main.getMessage(player, "party.invite.receive",new String[]{player.getName()}));
                            Main.updateCommandData(target);
                        }
                    }else{
                        player.sendMessage(Main.getMessage(player, "party.already.invite",new String[]{PlayerDataManager.getPlayerData(name).getName()}));
                    }
                }else{
                    player.sendMessage(Main.getMessage(player, "party.already",new String[]{PlayerDataManager.getPlayerData(name).getName()}));
                }
            }else{
                player.sendMessage(Main.getMessage(player, "party.no.player",new String[]{PlayerDataManager.getPlayerData(name).getName()}));
            }
        }else{
            player.sendMessage(Main.getMessage(player, "party.no.have"));
        }
    }

    public static void accept(Player player,String name){
        Map<String, String> mapp = partyData.get(player.getName().toLowerCase());
        String[] invites = mapp.get("invite").split(",");
        Boolean a = false;
        for(String party:invites){
            if(party.equals(name.toLowerCase())){
                a = true;
            }
        }
        if(a){//既に招待してるかどうか
            if(partyData.containsKey(name.toLowerCase())){//存在するpartyか
                Map<String, String> map = partyData.get(name.toLowerCase());
                if(map.get("created").equals("1")){//存在するpartyかどうか
                    mapp.put("created", "0");
                    mapp.put("owner", name.toLowerCase());
                    mapp.put("member", "");
                    mapp.put("invite", "");
                    map.put("member",map.get("member")+","+player.getName().toLowerCase());
                    player.sendMessage(Main.getMessage(player, "party.accept.invite",new String[]{PlayerDataManager.getPlayerData(name).getName()}));
                    Player target = Server.getInstance().getPlayer(name);
                    String member = map.get("member");
                    String[] members = member.split(",");
                    Player t;
                    for(String namee:members){
                        t = Server.getInstance().getPlayer(namee);
                        if(t instanceof Player){
                            Main.updateCommandData(t);
                            t.sendMessage(Main.getMessage(player, "party.join",new String[]{player.getName()}));
                        }
                    }
                }else{
                    String[] invitess = mapp.get("invite").split(",");
                    String t = "";
                    for(String party:invites){
                        if(!party.equals(player.getName().toLowerCase())){
                            if(t.equals("")) t += party;
                            else t += ","+party;
                        }
                    }
                    mapp.put("invite",t);
                    Main.updateCommandData(player);
                    player.sendMessage(Main.getMessage(player, "party.disbanded",new String[]{PlayerDataManager.getPlayerData(name).getName()}));
                }
            }else{
                player.sendMessage(Main.getMessage(player, "party.no.player",new String[]{PlayerDataManager.getPlayerData(name).getName()}));
            }
        }else{
            player.sendMessage(Main.getMessage(player, "party.no.invite",new String[]{PlayerDataManager.getPlayerData(name).getName()}));
        }
    }

    public static void deny(Player player,String name){
        Map<String, String> mapp = partyData.get(player.getName().toLowerCase());
        String[] invites = mapp.get("invite").split(",");
        Boolean a = false;
        for(String party:invites){
            if(party.equals(name.toLowerCase())){
                a = true;
            }
        }
        if(a){//既に招待されているかどうか
            if(partyData.containsKey(name.toLowerCase())){//存在するpartyかどうか
                Map<String, String> map = partyData.get(name.toLowerCase());
                if(map.get("created").equals("1")){//存在するpartyかどうか
                    String[] invitess = mapp.get("invite").split(",");
                    String t = "";
                    for(String party:invites){
                        if(!party.equals(player.getName().toLowerCase())){
                            if(t.equals("")) t += party;
                            else t += ","+party;
                        }
                    }
                    mapp.put("invite",t);
                    player.sendMessage(Main.getMessage(player, "party.deny.invite",new String[]{PlayerDataManager.getPlayerData(name).getName()}));
                    Main.updateCommandData(player);
                    Player target = Server.getInstance().getPlayer(name);
                }else{
                    String[] invitess = mapp.get("invite").split(",");
                    String t = "";
                    for(String party:invites){
                        if(!party.equals(player.getName().toLowerCase())){
                            if(t.equals("")) t += party;
                            else t += ","+party;
                        }
                    }
                    mapp.put("invite",t);
                    player.sendMessage(Main.getMessage(player, "party.disbanded",new String[]{PlayerDataManager.getPlayerData(name).getName()}));
                    Main.updateCommandData(player);
                }
            }else{
                player.sendMessage(Main.getMessage(player, "party.no.player",new String[]{PlayerDataManager.getPlayerData(name).getName()}));
            }
        }else{
            player.sendMessage(Main.getMessage(player, "party.no.invite",new String[]{PlayerDataManager.getPlayerData(name).getName()}));
        }
    }

    public static void list(Player player){
        Map<String, String> mapp = partyData.get(player.getName().toLowerCase());
        if(!mapp.get("owner").equals("")){//partyに参加しているかどうか
            Map<String, String> map = partyData.get(mapp.get("owner"));
            String member = map.get("member");
            String[] members = member.split(",");
            Player target;
            HashMap<Player,Boolean> targets = new HashMap<Player,Boolean>();
            HashMap<String,Boolean> offt = new HashMap<String,Boolean>();
            for(String name:members){
                target = Server.getInstance().getPlayer(name);
                if(target instanceof Player){
                    targets.put(target,Main.gamenow.get(target));
                }else{
                    offt.put(PlayerDataManager.getPlayerData(name).getName(),false);
                }
            }
            player.sendMessage(Main.getMessage(player,"party.list.line"));
            String game = "";
            for(Map.Entry<Player,Boolean> e : targets.entrySet()){
                if(e.getValue()) game = Main.getMessage(player,"party.list.game");
                else game = Main.getMessage(player,"party.list.not.game");
                player.sendMessage(Main.getMessage(player,"party.list.online",new String[]{e.getKey().getName(),game}));
            }
            for(Map.Entry<String,Boolean> e : offt.entrySet()){
                player.sendMessage(Main.getMessage(player,"party.list.offline",new String[]{e.getKey()}));
            }
            player.sendMessage(Main.getMessage(player,"party.list.line2"));
        }else{
            player.sendMessage(Main.getMessage(player, "party.no.join"));
        }
    }

    public static void leave(Player player){
        Map<String, String> mapp = partyData.get(player.getName().toLowerCase());
        if(!mapp.get("owner").equals("")){//partyに参加しているかどうか
            if(!mapp.get("owner").equals(player.getName().toLowerCase())){//ownerかどうか
                Map<String, String> map = partyData.get(mapp.get("owner"));
                String member = map.get("member");
                String[] members = member.split(",");
                String r = "";
                for(String name:members){
                    if(!name.equals(player.getName().toLowerCase())){
                        if(r.equals("")) r += name;
                        else r += ","+name;
                    }
                }
                player.sendMessage(Main.getMessage(player,"party.leave",new String[]{PlayerDataManager.getPlayerData(mapp.get("owner")).getName()}));
                map.put("member",r);
                mapp.put("created","0");
                mapp.put("owner","");
                mapp.put("member","");
                mapp.put("invite","");
                Main.updateCommandData(player);
                Player t;
                for(String namee:members){
                    t = Server.getInstance().getPlayer(namee);
                    if(t instanceof Player){
                        Main.updateCommandData(t);
                        t.sendMessage(Main.getMessage(player, "party.leave.player",new String[]{player.getName()}));
                    }
                }
            }else{
                disband(player);
            }
        }else{
            player.sendMessage(Main.getMessage(player, "party.no.join"));
        }
    }

    public static void disband(Player player){
        Map<String, String> mapp = partyData.get(player.getName().toLowerCase());
        if(!mapp.get("owner").equals("")){//partyに参加しているかどうか
            if(mapp.get("owner").equals(player.getName().toLowerCase())){//ownerかどうか
                String member = mapp.get("member");
                String[] members = member.split(",");
                Player target;
                Map<String, String> map;
                for(String name:members){
                    target = Server.getInstance().getPlayer(name);
                    map = partyData.get(name.toLowerCase());
                    map.put("created","0");
                    map.put("owner","");
                    map.put("member","");
                    map.put("invite","");
                    if(target instanceof Player){
                        Main.updateCommandData(target);
                        target.sendMessage(Main.getMessage(player, "party.disband",new String[]{player.getName()}));
                    }
                }
            }else{
                player.sendMessage(Main.getMessage(player, "party.no.have"));
            }
        }else{
            player.sendMessage(Main.getMessage(player, "party.no.join"));
        }
    }

    public static void kick(Player player,String name){
        Map<String, String> mapp = partyData.get(player.getName().toLowerCase());
        if(!mapp.get("owner").equals("")){//partyに参加しているかどうか
            if(mapp.get("owner").equals(player.getName().toLowerCase())){//ownerかどうか
                String member = mapp.get("member");
                String[] members = member.split(",");
                Boolean a = false;
                for(String namee:members){
                    if(namee.equals(name.toLowerCase())){
                        a = true;
                    }
                }
                if(a){
                    String r = "";
                    for(String namee:members){
                        if(!namee.equals(name.toLowerCase())){
                            if(r.equals("")) r += namee;
                            else r += ","+namee;
                        }
                    }
                    mapp.put("member", r);
                    Map<String, String> map = partyData.get(name.toLowerCase());
                    player.sendMessage(Main.getMessage(player,"party.kick.player",new String[]{PlayerDataManager.getPlayerData(name).getName()}));
                    Player target = Server.getInstance().getPlayer(name);
                    if(target instanceof Player){
                        target.sendMessage(Main.getMessage(player, "party.kick",new String[]{player.getName()}));
                    }
                    map.put("created","0");
                    map.put("owner","");
                    map.put("member","");
                    map.put("invite","");
                    Main.updateCommandData(target);
                    Player t;
                    for(String namee:members){
                        t = Server.getInstance().getPlayer(namee);
                        if(t instanceof Player){
                            Main.updateCommandData(t);
                            t.sendMessage(Main.getMessage(player, "party.leave.player",new String[]{player.getName()}));
                        }
                    }
                }else{
                    player.sendMessage(Main.getMessage(player, "party.no.player.in",new String[]{PlayerDataManager.getPlayerData(name).getName()}));
                }
            }else{
            	player.sendMessage(Main.getMessage(player, "party.no.have"));
            }
        }else{
            player.sendMessage(Main.getMessage(player, "party.no.join"));
        }
    }

    public static void promote(Player player,String name){
        Map<String, String> mapp = partyData.get(player.getName().toLowerCase());
        if(!mapp.get("owner").equals("")){//partyに参加しているかどうか
            if(mapp.get("owner").equals(player.getName().toLowerCase())){//ownerかどうか
                String member = mapp.get("member");
                String[] members = member.split(",");
                Boolean a = false;
                for(String namee:members){
                    if(namee.equals(name.toLowerCase())){
                        a = true;
                    }
                }
                if(a){
                    Map<String, String> map;
                    map = partyData.get(name.toLowerCase());
                    map.put("created",mapp.get("created"));
                    map.put("member",mapp.get("member"));
                    map.put("invite",mapp.get("invite"));
                    mapp.put("created", "0");
                    mapp.put("owner", name.toLowerCase());
                    mapp.put("member", "");
                    mapp.put("invite", "");
                    for(String namee:members){
                        map = partyData.get(namee.toLowerCase());
                        map.put("owner",name.toLowerCase());
                    }
                    Main.updateCommandData(player);
                    Player t;
                    for(String namee:members){
                        t = Server.getInstance().getPlayer(namee);
                        if(t instanceof Player){
                            Main.updateCommandData(t);
                            t.sendMessage(Main.getMessage(player, "party.promote.player",new String[]{PlayerDataManager.getPlayerData(name).getName()}));
                        }
                    }
                }else{
                    player.sendMessage(Main.getMessage(player, "party.no.player.in",new String[]{PlayerDataManager.getPlayerData(name).getName()}));
                }
            }else{
                player.sendMessage(Main.getMessage(player, "party.no.have"));
            }
        }else{
            player.sendMessage(Main.getMessage(player, "party.no.join"));
        }
    }

    public static LinkedHashMap<String, LinkedTreeMap<String, String>> partyData = new LinkedHashMap<String, LinkedTreeMap<String, String>>();
    public static LinkedHashMap<String, String> homegame = new LinkedHashMap<String, String>();
    public static File file;

}