package sote.login;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.scheduler.Task;
import sote.Main;
import sote.PlayerClone;

public class LoginSystem{

    public LoginSystem(){
    }

    /*public static long getClientId(Player player) {
        Class<? extends Player> reflect =  player.getClass();
        Field var;
        long clientId = 0;
        try {
            var = reflect.getDeclaredField("randomClientId");
            var.setAccessible(true);
            try {
                clientId = var.getLong(player);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                 e.printStackTrace();
            }
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        return clientId;
    }*/

    public static void addAddress(Player player){
        String address = getLoginAddress(player);
        String[] addresss = address.split(",");
        Boolean a = false;
        for(String ad:addresss){
            if(ad.equals(player.getAddress())) a = true;
        }
        if(address.equals("")) ((Map<String, String>) accountDB.get(player.getName().toLowerCase())).put("address",player.getAddress());
        else if(!a) ((Map<String, String>) accountDB.get(player.getName().toLowerCase())).put("address",address+","+player.getAddress());
    }

    public static Boolean checkAddress(Player player){
        String address = getLoginAddress(player);
        String[] addresss = address.split(",");
        Boolean a = false;
        for(String ad:addresss){
            if(ad.equals(player.getAddress())) a = true;
        }
        return a;
    }

    public static void addPlayer(Player player){
        auth.put(player.getName().toLowerCase(), 0);
        if(!accountDB.containsKey(player.getName().toLowerCase())){
            Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackMessage(player, Main.getMessage(player,"login.please.register")), 10);
            lock(player,"register");
        }else if(!checkAddress(player)){
            Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackMessage(player, Main.getMessage(player,"login.please.login")), 10);
            lock(player,"login");
        }else{
            Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackMessage(player, Main.getMessage(player,"login.auto.clear")), 10);
        }
    }

    public static void sendMessage(Player player, String msg){
        player.sendMessage(msg);
    }

    public static void lock(Player player,String type){
        switch(type){
            case "register":
                auth.put(player.getName().toLowerCase(),1);
                player.setDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_NO_AI, true);
            break;
            case "login":
                auth.put(player.getName().toLowerCase(),1);
                player.setDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_NO_AI, true);
            break;
        }
    }

    public static void unlock(Player player){
        auth.put(player.getName().toLowerCase(),0);
        player.setDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_NO_AI, false);
    }

    public static void register(Player player,String passw){
        if(accountDB.containsKey(player.getName().toLowerCase())){
            player.sendMessage(Main.getMessage(player,"login.already.register"));
            player.sendMessage(Main.getMessage(player,"login.please.login"));
            return;
        }
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("password","");
        map.put("address","");
        accountDB.put(player.getName().toLowerCase(),map);
        addAddress(player);
        unlock(player);
        player.sendMessage(Main.getMessage(player,"login.clear.register",new String[]{passw}));
        passw = getSha256(getSha256(player.getName().toLowerCase())+passw);
        ((Map<String, String>) accountDB.get(player.getName().toLowerCase())).put("password",passw);
    }

    public static void login(Player player,String passw){
        if(!accountDB.containsKey(player.getName().toLowerCase())){
            player.sendMessage(Main.getMessage(player,"login.not.already.register"));
            player.sendMessage(Main.getMessage(player,"login.please.register"));
            return;
        }
        passw = getSha256(getSha256(player.getName().toLowerCase())+passw);
        if(!passw.equals(getLoginPassword(player))){
            player.sendMessage(Main.getMessage(player,"login.not.password"));
            return;
        }
        addAddress(player);
        unlock(player);
        ((Map<String, String>) accountDB.get(player.getName().toLowerCase())).put("password",passw);
        player.sendMessage(Main.getMessage(player,"login.clear.login"));
    }

    private static String getSha256(String target) {
        MessageDigest md = null;
        StringBuffer buf = new StringBuffer();
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(target.getBytes());
            byte[] digest = md.digest();
            for (int i = 0; i < digest.length; i++){
                buf.append(String.format("%02x", digest[i]));
            }
        }catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return buf.toString();
    }

    public static String getLoginPassword(Player player){
        return ((Map<String, String>) accountDB.get(player.getName().toLowerCase())).get("password");
    }

    public static void setLoginPassword(Player player, String pass){
        ((Map<String, String>) accountDB.get(player.getName().toLowerCase())).put("password", pass);
    }

    public static String getLoginAddress(Player player){
        return ((Map<String, String>) accountDB.get(player.getName().toLowerCase())).get("address");
    }

    public static void setLoginAddress(Player player, String adr){
        ((Map<String, String>) accountDB.get(player.getName().toLowerCase())).put("address", adr);
    }

    public static String getLoginAddress(String player){
        return ((Map<String, String>) accountDB.get(player.toLowerCase())).get("address");
    }

    public static String getLoginPassword(PlayerClone player){
        return ((Map<String, String>) accountDB.get(player.getName().toLowerCase())).get("password");
    }

    public static String getLoginAddress(PlayerClone player){
        return ((Map<String, String>) accountDB.get(player.getName().toLowerCase())).get("address");
    }

    public static HashMap<String,Integer> auth = new HashMap<String,Integer>();
    public static LinkedHashMap<String, Object> accountDB = new LinkedHashMap<String, Object>();
    public static File file;

}
class CallbackMessage extends Task{

    public Player player;
    public String msg;

    public CallbackMessage(Player p, String msg){
        this.player = p;
        this.msg = msg;
    }

    public void onRun(int d){
        go();
    }

    public void go(){
        LoginSystem.sendMessage(player, msg);
    }
}
