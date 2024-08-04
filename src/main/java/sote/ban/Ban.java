package sote.ban;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.internal.LinkedTreeMap;

import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerPreLoginEvent;
import sote.Main;
import sote.MySQL;
import sote.MySQLData;

public class Ban{

    public Ban(){
    }

    public static LinkedTreeMap<String,Boolean> castMapB(Object ob){
        LinkedTreeMap<String,Boolean> map = (LinkedTreeMap<String,Boolean>)ob;
        return map;
    }

    public static LinkedTreeMap<Double,Boolean> castMapD(Object ob){
        LinkedTreeMap<Double,Boolean> map = (LinkedTreeMap<Double,Boolean>)ob;
        return map;
    }

    public static LinkedTreeMap<String,String> castMapS(Object ob){
        LinkedTreeMap<String,String> map = (LinkedTreeMap<String,String>)ob;
        return map;
    }

    public static LinkedTreeMap<String,Double> castMapSD(Object ob){
        LinkedTreeMap<String,Double> map = (LinkedTreeMap<String,Double>)ob;
        return map;
    }

    public static void UnNban(String str){
        if(!bandata.containsKey(str.toLowerCase())) return;
        castMapB(ban.get("name")).remove(str.toLowerCase());
        castMapB(ban.get("ip")).remove(castMapB(bandata.get(str.toLowerCase())).get("ip"));
        bandata.remove(str);
        MySQLData.removeBan(str);
    }

    public static void Nban(Player player, String reason){
        String name = player.getName().toLowerCase();
        String ip = MySQL.getGeneralField(player, "ip");
        String time = "";
        castMapS(ban.get("name")).put(name, name);
        castMapS(ban.get("ip")).put(ip, name);
        bandata.put(name,new LinkedTreeMap<String,Object>());
        castMapS(bandata.get(name)).put("name",name);
        castMapS(bandata.get(name)).put("ip",ip);
        castMapS(bandata.get(name)).put("time",time);
        castMapS(bandata.get(name)).put("reason",reason);
        castMapS(bandata.get(name)).put("id",String.valueOf(MySQLData.getBanId()));
        reason = Main.getMessage(player, "ban.reason."+reason);
        player.kick(Main.getMessage(player, "ban.ban", new String[]{castMapS(bandata.get(name)).get("id"), reason, Main.getMessage(player, "ban.nolimit")}), false);
        MySQLData.addBan(castMapS(bandata.get(name)));
    }

    public static void Nban(String player, String reason){
        String name = player.toLowerCase();
        String ip = MySQL.getGeneralField(player, "ip");
        String time = "";
        castMapS(ban.get("name")).put(name, name);
        castMapS(ban.get("ip")).put(ip, name);
        bandata.put(name,new LinkedTreeMap<String,Object>());
        castMapS(bandata.get(name)).put("name",name);
        castMapS(bandata.get(name)).put("ip",ip);
        castMapS(bandata.get(name)).put("time",time);
        castMapS(bandata.get(name)).put("reason",reason);
        castMapS(bandata.get(name)).put("id",String.valueOf(MySQLData.getBanId()));
        MySQLData.addBan(castMapS(bandata.get(name)));
    }

    public static void Nban(String player, int field, int amount, String reason){
        String name = player.toLowerCase();
        String ip = MySQL.getGeneralField(player, "ip");
        String time = "";
        Calendar now = Calendar.getInstance();
        now.add(field, amount);
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        int date = now.get(Calendar.DATE);
        int hour = now.get(now.HOUR_OF_DAY);
        int minutes = now.get(now.MINUTE);
        int second = now.get(now.SECOND);
        time = year+":"+month+":"+date+":"+hour+":"+minutes+":"+second;
        castMapS(ban.get("name")).put(name, name);
        castMapS(ban.get("ip")).put(ip, name);
        bandata.put(name,new LinkedTreeMap<String,Object>());
        castMapS(bandata.get(name)).put("name",name);
        castMapS(bandata.get(name)).put("ip",ip);
        castMapS(bandata.get(name)).put("time",time);
        castMapS(bandata.get(name)).put("reason",reason);
        castMapS(bandata.get(name)).put("id",String.valueOf(MySQLData.getBanId()));
        MySQLData.addBan(castMapS(bandata.get(name)));
    }

    public static void Nban(Player player, int field, int amount, String reason){
        String name = player.getName().toLowerCase();
        String ip = MySQL.getGeneralField(player, "ip");
        String time = "";
        Calendar now = Calendar.getInstance();
        now.add(field, amount);
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        int date = now.get(Calendar.DATE);
        int hour = now.get(now.HOUR_OF_DAY);
        int minutes = now.get(now.MINUTE);
        int second = now.get(now.SECOND);
        time = year+":"+month+":"+date+":"+hour+":"+minutes+":"+second;
        castMapS(ban.get("name")).put(name, name);
        castMapS(ban.get("ip")).put(ip, name);
        bandata.put(name,new LinkedTreeMap<String,Object>());
        castMapS(bandata.get(name)).put("name",name);
        castMapS(bandata.get(name)).put("ip",ip);
        castMapS(bandata.get(name)).put("time",time);
        castMapS(bandata.get(name)).put("reason",reason);
        castMapS(bandata.get(name)).put("id",String.valueOf(MySQLData.getBanId()));
        String[] timeS = time.split(":");
        Calendar date1 = Calendar.getInstance();
        date1.set(Integer.parseInt(timeS[0]), Integer.parseInt(timeS[1]), Integer.parseInt(timeS[2]), Integer.parseInt(timeS[3]), Integer.parseInt(timeS[4]), Integer.parseInt(timeS[5]));
        Calendar date2 = Calendar.getInstance();
        int d1 = (int)date1.getTime().getTime();
        int d2 = (int)date2.getTime().getTime();
        int s = (d1 - d2) / 1000;
        reason = Main.getMessage(player, "ban.reason."+reason);
        player.kick(Main.getMessage(player, "ban.ban", new String[]{castMapS(bandata.get(name)).get("id"), reason, getTimeString(player, s)}), false);
        MySQLData.addBan(castMapS(bandata.get(name)));
    }

    public static void setBanData(String name){
        if(MySQLData.isBanned(name)){
            try{
                ResultSet r = MySQLData.getBanData(name);
                castMapS(ban.get("name")).put(name, name);
                castMapS(ban.get("ip")).put(r.getString("ip"), name);
                bandata.put(name,new LinkedTreeMap<String,Object>());
                castMapS(bandata.get(name)).put("name",name);
                castMapS(bandata.get(name)).put("ip",r.getString("ip"));
                castMapS(bandata.get(name)).put("time",r.getString("time"));
                castMapS(bandata.get(name)).put("reason",r.getString("reason"));
                castMapS(bandata.get(name)).put("id",r.getString("id"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setBanData(ResultSet r){
        try{
            castMapS(ban.get("name")).put(r.getString("name"), r.getString("name"));
            castMapS(ban.get("ip")).put(r.getString("ip"), r.getString("name"));
            bandata.put(r.getString("name"),new LinkedTreeMap<String,Object>());
            castMapS(bandata.get(r.getString("name"))).put("name",r.getString("name"));
            castMapS(bandata.get(r.getString("name"))).put("ip",r.getString("ip"));
            castMapS(bandata.get(r.getString("name"))).put("time",r.getString("time"));
            castMapS(bandata.get(r.getString("name"))).put("reason",r.getString("reason"));
            castMapS(bandata.get(r.getString("name"))).put("id",r.getString("id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setChatBan(Player player,Boolean b){
        chatban.put(player.getName().toLowerCase(),b);
    }

    public static Boolean canChat(Player player){
        if(!chatban.containsKey(player.getName().toLowerCase())) return true;
        return Boolean.parseBoolean(chatban.get(player.getName().toLowerCase()).toString());
    }

    public static void checkLogin(PlayerPreLoginEvent event, Player player){
        String name = player.getName().toLowerCase();
        String ip = player.getAddress();
        if(castMapB(ban.get("name")).containsKey(name)){
            String timeString = castMapS(bandata.get(name)).get("time");
            String reason = Main.getMessage(player, "ban.reason."+castMapS(bandata.get(name)).get("reason"));
            if(timeString.equals("")){
                player.close("", Main.getMessage(player, "ban.banned", new String[]{reason, Main.getMessage(player, "ban.nolimit")}));
            }
            String[] timeS = timeString.split(":");
            Calendar date1 = Calendar.getInstance();
            date1.set(Integer.parseInt(timeS[0]), Integer.parseInt(timeS[1]), Integer.parseInt(timeS[2]), Integer.parseInt(timeS[3]), Integer.parseInt(timeS[4]), Integer.parseInt(timeS[5]));
            Calendar date2 = Calendar.getInstance();
            int d1 = (int)date1.getTime().getTime();
            int d2 = (int)date2.getTime().getTime();
            if(d2 - d1 >= 0){
                UnNban(name);
            }else{
                int s = (d1 - d2) / 1000;
                player.close("", Main.getMessage(player, "ban.banned", new String[]{reason, getTimeString(player, s)}));
            }
        }
        for (Map.Entry<String, String> e : castMapS(ban.get("ip")).entrySet()){
            String[] adrs = e.getKey().split(",");
            for(String adr : adrs){
                if(adr.equals(ip)){
                    String timeString = castMapS(bandata.get(e.getValue())).get("time");
                    String reason = Main.getMessage(player, "ban.reason."+castMapS(bandata.get(e.getValue())).get("reason"));
                    if(timeString.equals("")){
                        player.close("", Main.getMessage(player, "ban.banned", new String[]{reason, Main.getMessage(player, "ban.nolimit")}));
                    }
                    String[] timeS = timeString.split(":");
                    Calendar date1 = Calendar.getInstance();
                    date1.set(Integer.parseInt(timeS[0]), Integer.parseInt(timeS[1]), Integer.parseInt(timeS[2]), Integer.parseInt(timeS[3]), Integer.parseInt(timeS[4]), Integer.parseInt(timeS[5]));
                    Calendar date2 = Calendar.getInstance();
                    int d1 = (int)date1.getTime().getTime();
                    int d2 = (int)date2.getTime().getTime();
                    if(d2 - d1 >= 0){
                        UnNban(e.getValue());
                    }else{
                        int s = (d1 - d2) / 1000;
                        player.close("", Main.getMessage(player, "ban.banned", new String[]{reason, getTimeString(player, s)}));
                    }
                }
            }
        }
    }

    public static String getTimeString(Player player, int s){
        if(s >= 60 * 60 * 24 * 30){
            int day = (int)Math.floor(s / (60 * 60 * 24));
            int hour = (int)Math.floor(s / (60 * 60)) - (day * 24);
            int minutes = (int)Math.floor(s / 60) - (hour * 60) - (day * 60 * 24);
            int second = s - (minutes * 60) - (hour * 60 * 60) - (day * 60 * 60 * 24);
            return day+Main.getMessage(player, "ban.day")+hour+Main.getMessage(player, "ban.hour")+minutes+Main.getMessage(player, "ban.minute")+second+Main.getMessage(player, "ban.second");
        }else if(s >= 60 * 60 * 24){
            int day = (int)Math.floor(s / (60 * 60 * 24));
            int hour = (int)Math.floor(s / (60 * 60)) - (day * 24);
            int minutes = (int)Math.floor(s / 60) - (hour * 60) - (day * 60 * 24);
            int second = s - (minutes * 60) - (hour * 60 * 60) - (day * 60 * 60 * 24);
            return day+Main.getMessage(player, "ban.day")+hour+Main.getMessage(player, "ban.hour")+minutes+Main.getMessage(player, "ban.minute")+second+Main.getMessage(player, "ban.second");
        }else if(s >= 60 * 60){
            int hour = (int)Math.floor(s / (60 * 60));
            int minutes = (int)Math.floor(s / 60) - (hour * 60);
            int second = s - (minutes * 60) - (hour * 60 * 60);
            return hour+Main.getMessage(player, "ban.hour")+minutes+Main.getMessage(player, "ban.minute")+second+Main.getMessage(player, "ban.second");
        }else if(s >= 60){
            int minutes = (int)Math.floor(s / 60);
            int second = s - (minutes * 60);
            return minutes+Main.getMessage(player, "ban.minute")+second+Main.getMessage(player, "ban.second");
        }else{
            return s+Main.getMessage(player, "ban.second");
        }
    }

    public static void clearAll(){
        ban = new LinkedHashMap<String, Object>();
        bandata = new LinkedHashMap<String, Object>();
        if(!ban.containsKey("name")){
            ban.put("name",new LinkedTreeMap<String,Boolean>());
        }
        if(!ban.containsKey("ip")){
            ban.put("ip",new LinkedTreeMap<String,Boolean>());
        }
    }

    public static LinkedHashMap<String, Object> ban = new LinkedHashMap<String, Object>();
    public static LinkedHashMap<String, Object> chatban = new LinkedHashMap<String, Object>();
    public static LinkedHashMap<String, Object> bandata = new LinkedHashMap<String, Object>();

}
