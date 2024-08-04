package sote.clan;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import cn.nukkit.Player;
import cn.nukkit.Server;
import sote.Main;
import sote.MySQL;
import sote.PlayerDataManager;

public class Clan{

    public Clan(){
    }

    public static void addPlayer(Player player){
        HashMap<String, String> v1 = new HashMap<String, String>();
        v1.put("created","0");
        v1.put("owner","");
        v1.put("member","");
        v1.put("invite","");
        v1.put("clancoin","0");
        v1.put("tag","");
        clanData.put(player.getName().toLowerCase(),v1);
    }

    public static void invite(Player player,String name){
        Map<String, String> mapp = (Map<String, String>) clanData.get(player.getName().toLowerCase());
        if(mapp.get("owner").equals(player.getName().toLowerCase()) || mapp.get("owner").equals("")){//自分のclanを持っているかどうか
            if(clanData.containsKey(name.toLowerCase())){//存在するプレイヤーかどうか
                Map<String, String> map = (Map<String, String>) clanData.get(name.toLowerCase());
                if(map.get("created").equals("0")){//相手がclanに参加してるかどうか
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
                            target.sendMessage(Main.getMessage(player, "clan.invite.receive",new String[]{player.getName()}));
                        }
                        //MySQLData.sendClanData();
                    }else{
                        player.sendMessage(Main.getMessage(player, "clan.already.invite",new String[]{PlayerDataManager.getPlayerData(name).getName()}));
                    }
                }else{
                    player.sendMessage(Main.getMessage(player, "clan.already",new String[]{PlayerDataManager.getPlayerData(name).getName()}));
                }
            }else{
                player.sendMessage(Main.getMessage(player, "clan.no.player",new String[]{PlayerDataManager.getPlayerData(name).getName()}));
            }
        }else{
            player.sendMessage(Main.getMessage(player, "clan.no.have"));
        }
    }

    public static void accept(Player player,String name){
        Map<String, String> mapp = (Map<String, String>) clanData.get(player.getName().toLowerCase());
        String[] invites = mapp.get("invite").split(",");
        Boolean a = false;
        for(String party:invites){
            if(party.equals(name.toLowerCase())){
                a = true;
            }
        }
        if(a){//既に招待してるかどうか
            if(clanData.containsKey(name.toLowerCase())){//存在するclanか
                Map<String, String> map = (Map<String, String>) clanData.get(name.toLowerCase());
                if(map.get("created").equals("1")){//存在するclanかどうか
                    mapp.put("created", "0");
                    mapp.put("owner", name.toLowerCase());
                    mapp.put("member", "");
                    mapp.put("invite", "");
                    mapp.put("clancoin", "0");
                    map.put("member",map.get("member")+","+player.getName().toLowerCase());
                    player.sendMessage(Main.getMessage(player, "clan.accept.invite",new String[]{PlayerDataManager.getPlayerData(name).getName()}));
                    Player target = Server.getInstance().getPlayer(name);
                    String member = map.get("member");
                    String[] members = member.split(",");
                    Player t;
                    for(String namee:members){
                        t = Server.getInstance().getPlayer(namee);
                        if(t instanceof Player){
                            t.sendMessage(Main.getMessage(player, "clan.join",new String[]{player.getName()}));
                        }
                    }
                    //MySQLData.sendClanData();
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
                    player.sendMessage(Main.getMessage(player, "clan.disbanded",new String[]{PlayerDataManager.getPlayerData(name).getName()}));
                    //MySQLData.sendClanData();
                }
            }else{
                player.sendMessage(Main.getMessage(player, "clan.no.player",new String[]{PlayerDataManager.getPlayerData(name).getName()}));
            }
        }else{
            player.sendMessage(Main.getMessage(player, "clan.no.invite",new String[]{PlayerDataManager.getPlayerData(name).getName()}));
        }
    }

    public static void deny(Player player,String name){
        Map<String, String> mapp = (Map<String, String>) clanData.get(player.getName().toLowerCase());
        String[] invites = mapp.get("invite").split(",");
        Boolean a = false;
        for(String party:invites){
            if(party.equals(name.toLowerCase())){
                a = true;
            }
        }
        if(a){//既に招待されているかどうか
            if(clanData.containsKey(name.toLowerCase())){//存在するclanかどうか
                Map<String, String> map = (Map<String, String>) clanData.get(name.toLowerCase());
                if(map.get("created").equals("1")){//存在するclanかどうか
                    String[] invitess = mapp.get("invite").split(",");
                    String t = "";
                    for(String party:invites){
                        if(!party.equals(player.getName().toLowerCase())){
                            if(t.equals("")) t += party;
                            else t += ","+party;
                        }
                    }
                    mapp.put("invite",t);
                    player.sendMessage(Main.getMessage(player, "clan.deny.invite",new String[]{PlayerDataManager.getPlayerData(name).getName()}));
                    Player target = Server.getInstance().getPlayer(name);
                    //MySQLData.sendClanData();
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
                    player.sendMessage(Main.getMessage(player, "clan.disbanded",new String[]{PlayerDataManager.getPlayerData(name).getName()}));
                    //MySQLData.sendClanData();
                }
            }else{
                player.sendMessage(Main.getMessage(player, "clan.no.player",new String[]{PlayerDataManager.getPlayerData(name).getName()}));
            }
        }else{
            player.sendMessage(Main.getMessage(player, "clan.no.invite",new String[]{PlayerDataManager.getPlayerData(name).getName()}));
        }
    }

    public static void list(Player player){
        Map<String, String> mapp = (Map<String, String>) clanData.get(player.getName().toLowerCase());
        if(!mapp.get("owner").equals("")){//clanに参加しているかどうか
            Map<String, String> map = (Map<String, String>) clanData.get(mapp.get("owner"));
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
            player.sendMessage(Main.getMessage(player,"clan.list.line"));
            String game = "";
            for(Map.Entry<Player,Boolean> e : targets.entrySet()){
                if(e.getValue()) game = Main.getMessage(player,"clan.list.game");
                else game = Main.getMessage(player,"clan.list.not.game");
                player.sendMessage(Main.getMessage(player,"clan.list.online",new String[]{e.getKey().getName(),game}));
            }
            for(Map.Entry<String,Boolean> e : offt.entrySet()){
                player.sendMessage(Main.getMessage(player,"clan.list.offline",new String[]{e.getKey()}));
            }
            player.sendMessage(Main.getMessage(player,"clan.list.line2"));
        }else{
            player.sendMessage(Main.getMessage(player, "clan.no.join"));
        }
    }

    public static void leave(Player player){
        Map<String, String> mapp = (Map<String, String>) clanData.get(player.getName().toLowerCase());
        if(!mapp.get("owner").equals("")){//clanに参加しているかどうか
            if(!mapp.get("owner").equals(player.getName().toLowerCase())){//ownerかどうか
                Map<String, String> map = (Map<String, String>) clanData.get(mapp.get("owner"));
                String member = map.get("member");
                String[] members = member.split(",");
                String r = "";
                for(String name:members){
                    if(!name.equals(player.getName().toLowerCase())){
                        if(r.equals("")) r += name;
                        else r += ","+name;
                    }
                }
                player.sendMessage(Main.getMessage(player,"clan.leave",new String[]{PlayerDataManager.getPlayerData(mapp.get("owner")).getName()}));
                map.put("member",r);
                mapp.put("created","0");
                mapp.put("owner","");
                mapp.put("member","");
                mapp.put("invite","");
                mapp.put("clancoin", "0");
                Player t;
                for(String namee:members){
                    t = Server.getInstance().getPlayer(namee);
                    if(t instanceof Player){
                        t.sendMessage(Main.getMessage(player, "clan.leave.player",new String[]{player.getName()}));
                    }
                }
                //MySQLData.sendClanData();
            }else{
                disband(player);
            }
        }else{
            player.sendMessage(Main.getMessage(player, "clan.no.join"));
        }
    }

    public static void disband(Player player){
        Map<String, String> mapp = (Map<String, String>) clanData.get(player.getName().toLowerCase());
        if(!mapp.get("owner").equals("")){//clanに参加しているかどうか
            if(mapp.get("owner").equals(player.getName().toLowerCase())){//ownerかどうか
                String member = mapp.get("member");
                String[] members = member.split(",");
                Player target;
                Map<String, String> map;
                for(String name:members){
                    target = Server.getInstance().getPlayer(name);
                    if(target instanceof Player){
                        target.sendMessage(Main.getMessage(player, "clan.disband",new String[]{player.getName()}));
                    }
                    map = (Map<String, String>) clanData.get(name.toLowerCase());
                    map.put("created","0");
                    map.put("owner","");
                    map.put("member","");
                    map.put("invite","");
                    map.put("clancoin", "0");
                }
                //MySQLData.sendClanData();
            }else{
                player.sendMessage(Main.getMessage(player, "clan.no.have"));
            }
        }else{
            player.sendMessage(Main.getMessage(player, "clan.no.join"));
        }
    }

    public static void kick(Player player,String name){
        Map<String, String> mapp = (Map<String, String>) clanData.get(player.getName().toLowerCase());
        if(!mapp.get("owner").equals("")){//clanに参加しているかどうか
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
                    Map<String, String> map = (Map<String, String>) clanData.get(name.toLowerCase());
                    player.sendMessage(Main.getMessage(player,"clan.kick.player",new String[]{PlayerDataManager.getPlayerData(name).getName()}));
                    Player target = Server.getInstance().getPlayer(name);
                    if(target instanceof Player){
                        target.sendMessage(Main.getMessage(player, "clan.kick",new String[]{player.getName()}));
                    }
                    map.put("created","0");
                    map.put("owner","");
                    map.put("member","");
                    map.put("invite","");
                    map.put("clancoin", "0");
                    Player t;
                    for(String namee:members){
                        t = Server.getInstance().getPlayer(namee);
                        if(t instanceof Player){
                            t.sendMessage(Main.getMessage(player, "clan.leave.player",new String[]{player.getName()}));
                        }
                    }
                    //MySQLData.sendClanData();
                }else{
                    player.sendMessage(Main.getMessage(player, "clan.no.player.in",new String[]{PlayerDataManager.getPlayerData(name).getName()}));
                }
            }else{
            	player.sendMessage(Main.getMessage(player, "clan.no.have"));
            }
        }else{
            player.sendMessage(Main.getMessage(player, "clan.no.join"));
        }
    }

    public static void promote(Player player,String name){
        Map<String, String> mapp = (Map<String, String>) clanData.get(player.getName().toLowerCase());
        if(!mapp.get("owner").equals("")){//clanに参加しているかどうか
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
                    for(String namee:members){
                        map = (Map<String, String>) clanData.get(namee.toLowerCase());
                        map.put("owner",name.toLowerCase());
                    }
                    map = (Map<String, String>) clanData.get(name.toLowerCase());
                    map.put("created",mapp.get("created"));
                    map.put("mwmber",mapp.get("mwmber"));
                    map.put("invite",mapp.get("invite"));
                    map.put("clancoin", mapp.get("clancoin"));
                    mapp.put("created", "0");
                    mapp.put("owner", name.toLowerCase());
                    mapp.put("member", "");
                    mapp.put("invite", "");
                    mapp.put("clancoin", "0");
                    Player t;
                    for(String namee:members){
                        t = Server.getInstance().getPlayer(namee);
                        if(t instanceof Player){
                            t.sendMessage(Main.getMessage(player, "clan.promote.player",new String[]{PlayerDataManager.getPlayerData(name).getName()}));
                        }
                    }
                    //MySQLData.sendClanData();
                }else{
                    player.sendMessage(Main.getMessage(player, "clan.no.player.in",new String[]{PlayerDataManager.getPlayerData(name).getName()}));
                }
            }else{
                player.sendMessage(Main.getMessage(player, "clan.no.have"));
            }
        }else{
            player.sendMessage(Main.getMessage(player, "clan.no.join"));
        }
    }

    public static void onPlayerGetCoin(Player player, int coin){
        Map<String, String> mapp = (Map<String, String>) clanData.get(player.getName().toLowerCase());
        if(!mapp.get("owner").equals("")){//clanに参加しているかどうか
            int clancoin = (int)Math.floor(coin * 0.1);
            if(clancoin > 0){
                player.sendMessage(Main.getMessage(player, "status.add.clancoin", new String[]{String.valueOf(clancoin)}));
                addClanCoin(player, clancoin);
            }
        }
    }

    public static void addClanCoin(Player player, int clancoin){
        setClanCoin(player, getClanCoin(player) + clancoin);
    }

    public static int getClanCoin(Player player){
        Map<String, String> mapp = (Map<String, String>) clanData.get(player.getName().toLowerCase());
        if(!mapp.get("owner").equals("")){//clanに参加しているかどうか
            Map<String, String> map = (Map<String, String>) clanData.get(mapp.get("owner"));
            return Integer.parseInt(map.get("clancoin"));
        }
        return 0;
    }

    public static void setClanCoin(Player player, int clancoin){
        Map<String, String> mapp = (Map<String, String>) clanData.get(player.getName().toLowerCase());
        if(!mapp.get("owner").equals("")){//clanに参加しているかどうか
            Map<String, String> map = (Map<String, String>) clanData.get(mapp.get("owner"));
            map.put("clancoin", String.valueOf(clancoin));
            //MySQLData.sendClanData();
        }
    }

    public static void setOwnerData(Player player){
        String owner = ((Map<String, String>) clanData.get(player.getName().toLowerCase())).get("owner");
        if(clanData.containsKey(owner) || owner.equals("")) return;
        setClanDataString(owner, MySQL.getGeneralField(owner, "clandata"));
    }

    public static String getClanDataString(Player player){
        Map<String, String> map = clanData.get(player.getName().toLowerCase());
        return new GsonBuilder().setPrettyPrinting().create().toJson(map);
    }

    public static void setClanDataString(Player player, String str){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        HashMap<String, String> map = gson.fromJson(str, new TypeToken<HashMap<String, String>>(){}.getType());
        if(!map.containsKey("created")) map.put("created","0");
        if(!map.containsKey("owner")) map.put("owner","");
        if(!map.containsKey("member")) map.put("member","");
        if(!map.containsKey("invite")) map.put("invite","");
        if(!map.containsKey("clancoin")) map.put("clancoin","0");
        if(!map.containsKey("tag")) map.put("tag","");
        clanData.put(player.getName().toLowerCase(), map);
        setOwnerData(player);
    }

    public static void setClanDataString(String player, String str){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        HashMap<String, String> map = gson.fromJson(str, new TypeToken<HashMap<String, String>>(){}.getType());
        if(!map.containsKey("created")) map.put("created","0");
        if(!map.containsKey("owner")) map.put("owner","");
        if(!map.containsKey("member")) map.put("member","");
        if(!map.containsKey("invite")) map.put("invite","");
        if(!map.containsKey("clancoin")) map.put("clancoin","0");
        if(!map.containsKey("tag")) map.put("tag","");
        clanData.put(player, map);
    }

    public static HashMap<String, HashMap<String, String>> clanData = new HashMap<String, HashMap<String, String>>();
    public static File file;

}