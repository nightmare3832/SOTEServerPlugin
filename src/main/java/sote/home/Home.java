package sote.home;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import cn.nukkit.Player;
import cn.nukkit.Server;
import sote.Game;
import sote.GameProvider;
import sote.Main;
import sote.MySQLData;
import sote.buildbattle.Buildbattle;
import sote.inventory.Inventorys;
import sote.inventory.home.HomeInventory;
import sote.murder.Murder;
import sote.skywarssolo.SkywarsSolo;

public class Home{

    public Home(){
    }

    public static void addPlayer(Player player){
        HashMap<String, String> v1 = new HashMap<String, String>();
        v1.put("id","");
        v1.put("number","1");
        v1.put("created","0");
        v1.put("block","");
        homeData.put(player.getName().toLowerCase(),v1);
    }

    public static void addHome(Player player,String id){
        if(MySQLData.isCreatedHomeId(id)){
            player.sendMessage(Main.getMessage(player,"home.error.used",new String[]{id}));
            return;
        }
        Map<String, String> map = (Map<String, String>) homeData.get(player.getName().toLowerCase());
        if(map.get("created").equals("1")){
            player.sendMessage(Main.getMessage(player,"home.error.created"));
            return;
        }
        int number = MySQLData.getNextHomeId();
        String url = Server.getInstance().getDataPath()+"/worlds/home";
        File newdir = new File(url+id);
        newdir.delete();
        newdir.mkdir();
        File newdir2 = new File(url+id+"/region");
        newdir2.delete();
        newdir2.mkdir();
        int color = (int)((Math.random() * 12)+1);
        directoryCopy(new File(url+color),new File(url+id));
        Server.getInstance().loadLevel("home"+id);
        map.put("id",id);
        map.put("number",String.valueOf(number));
        map.put("created",String.valueOf(1));
        HashMap<String, String> v1 = new HashMap<String, String>();
        v1.put("owner",player.getName().toLowerCase());
        v1.put("number",String.valueOf(number));
        v1.put("id",String.valueOf(id));
        v1.put("color",String.valueOf(color));
        MySQLData.addHome(v1);
        homeInfo.put(id,v1);
        homeid.put(String.valueOf(number),id);
        GameProvider.register(number, true);
        player.sendMessage(Main.getMessage(player,"home.add",new String[]{id}));
    }

    public static void removeHome(Player player){
        Map<String, String> map = (Map<String, String>) homeData.get(player.getName().toLowerCase());
        if(map.get("created").equals("0")){
            player.sendMessage(Main.getMessage(player,"home.error.not.have"));
            return;
        }
        String id = map.get("id");
        if(!homeInfo.containsKey(id)){
            player.sendMessage(Main.getMessage(player,"home.error.no",new String[]{id}));
            return;
        }
        String number = map.get("number");
        String url = Server.getInstance().getDataPath()+"/worlds/home";
        File newdir = new File(url+id);
        deleteFile(newdir);
        File newdir2 = new File(url+id+".old");
        deleteFile(newdir2);
        map.put("id","");
        map.put("number","1");
        map.put("created","0");
        MySQLData.removeHome(player.getName().toLowerCase());
        homeInfo.remove(id);
        homeid.remove(number);
        player.sendMessage(Main.getMessage(player,"home.remove",new String[]{id}));
    }

    public static void deleteFile(final File path) {
        for (File f : path.listFiles()) {
            if (f.isDirectory()) {
                deleteFile(f);
            }else{
                f.delete();
            }
        }
        path.delete();
    }

    public static void go(Player player,String id){
        if(!homeInfo.containsKey(id)){
            player.sendMessage(Main.getMessage(player,"home.error.no",new String[]{id}));
            return;
        }
        Map<String, String> map = (Map<String, String>) homeInfo.get(id);
        if(!Server.getInstance().isLevelLoaded("home"+id)){
            Server.getInstance().loadLevel("home"+id);
        }else{
            if(Server.getInstance().getLevelByName("home"+id).getPlayers().size() >= 24 && !(homeInfo.get(id)).get("owner").equals(player.getName().toLowerCase())){
                player.sendMessage(Main.getMessage(player,"home.max.players"));
                return;
            }
        }
        player.teleport(Server.getInstance().getLevelByName("home"+id)
        		.getSafeSpawn());
        Inventorys.setData(player, new HomeInventory());
        int number = Integer.parseInt(map.get("number"));
        if(homegame.containsKey(id)){
            homegame.get(id).Join(player);
        }
    }

    public static Boolean directoryCopy(File dirFrom, File dirTo){
        File[] fromFile = dirFrom.listFiles();
        dirTo = new File(dirTo.getPath() + "/" + dirFrom.getName());
        dirTo.mkdir();
        if(fromFile != null) {
           for(File f : fromFile) {
              if (f.isFile()){
                 if(!fileCopy(f, dirTo)){
                    return false;
                 }
              }else{
                 if(!directoryCopy(f, dirTo)){
                    return false;
                 }
              }
           }
         }
        return true;
    }

    public static Boolean fileCopy(File file, File dir){
        String f = dir.getPath() + "/" + file.getName();
        String ff= f;
        for(int i = 1;i<= 12;i++){
            if(ff.equals(f)) ff = f.replace("home"+i+"/","");
        }
        File copyFile = new File(ff);
        FileChannel channelFrom = null;
        FileChannel channelTo = null;
        try{
           copyFile.createNewFile();
           channelFrom = new FileInputStream(file).getChannel();
           channelTo = new FileOutputStream(copyFile).getChannel();
           channelFrom.transferTo(0, channelFrom.size(), channelTo);
           return true;
        }
        catch(Exception e){
           return false;
        }finally{
           try{
              if (channelFrom != null) { channelFrom.close(); }
              if (channelTo != null) { channelTo.close(); }
              copyFile.setLastModified(file.lastModified());
           }
           catch (Exception e){
              return false;
           }
        }
    }

    public static boolean isNumber(String num) {
        try {
            Integer.parseInt(num);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String getId(int number){
        return (String)homeid.get(String.valueOf(number));
    }

    public static void startGame(String id,String game,Player player, HashMap<String, String> setting){
        if(homegame.containsKey(id)) return;
        int need = 0;
        int max = 0;
        String tag = "";
        Map<String, String> map = (Map<String, String>) homeInfo.get(id);
        int number = Integer.parseInt(map.get("number"));
        switch(game){
            case GameProvider.MURDER:
                need = Murder.MIN_PLAYERS;
                max = Murder.MAX_PLAYERS;
                tag = "Murder";
            break;
            case GameProvider.SKYWARS_SOLO:
                need = SkywarsSolo.MIN_PLAYERS;
                max = SkywarsSolo.MAX_PLAYERS;
                tag = "SkyWars";
            break;
            case GameProvider.BUILDBATTLE:
                need = Buildbattle.MIN_PLAYERS;
                max = Buildbattle.MAX_PLAYERS;
                tag = "BuildBattle";
            break;
        }
        Game gameObj = GameProvider.Games.get(number).get(game);
        for(Map.Entry<String, String> e : setting.entrySet()){
            gameObj.setSetting(e.getKey(), e.getValue());
        }
        Map<Long,Player> players = Server.getInstance().getLevelByName("home"+id).getPlayers();
        int c = 0;
        Player[] ps = new Player[players.size()];
        for (Map.Entry<Long,Player> e : players.entrySet()){
            ps[c] = e.getValue();
            c++;
        }
        List<Player> list=Arrays.asList(ps);
        Collections.shuffle(list);
        ps =(Player[])list.toArray(new Player[list.size()]);
        c = 0;
        for (Map.Entry<Long,Player> e : players.entrySet()){
            e.getValue().sendMessage(Main.getMessage(e.getValue(),"home.game.selected"));
            e.getValue().sendMessage(Main.getMessage(e.getValue(),"home.game.line"));
            e.getValue().sendMessage(Main.getMessage(e.getValue(),"home.game.name",new String[]{tag}));
            e.getValue().sendMessage(Main.getMessage(e.getValue(),"home.game.line2"));
        }
        for (Player p : ps){
            c++;
            gameObj.Join(p);
        }
        homegame.put(id, gameObj);
    }

    public static void notStart(int number){
        String id = getId(number);
        Map<Long,Player> players = Server.getInstance().getLevelByName("home"+id).getPlayers();
        for (Map.Entry<Long,Player> e : players.entrySet()){
            homegame.get(id).Quit(e.getValue());
            e.getValue().sendMessage(Main.getMessage(e.getValue(),"home.not.enought.player"));
        }
        homegame.get(id).reset();
        homegame.remove(getId(number));
    }

    public static void finish(int number){
        homegame.remove(getId(number));
    }

    public static String getPlayerHomeData(Player player){
        Map<String, String> map = homeData.get(player.getName().toLowerCase());
        return new GsonBuilder().setPrettyPrinting().create().toJson(map);
    }

    public static void setPlayerHomeData(Player player, String str){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        HashMap<String, String> map = gson.fromJson(str, new TypeToken<HashMap<String, String>>(){}.getType());
        if(!map.containsKey("number")) map.put("number","1");
        if(!map.containsKey("id")) map.put("id","");
        if(!map.containsKey("created")) map.put("created","");
        if(!map.containsKey("block")) map.put("block","");
        homeData.put(player.getName().toLowerCase(), map);
    }

    public static void setHomeData(ResultSet r){
        try{
            HashMap<String, String> v1 = new HashMap<String, String>();
            v1.put("owner",r.getString("owner"));
            v1.put("number",r.getString("number"));
            v1.put("id",r.getString("id"));
            v1.put("color",r.getString("color"));
            homeInfo.put(r.getString("id"),v1);
            homeid.put(r.getString("number"), r.getString("id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static LinkedHashMap<String, HashMap<String, String>> homeData = new LinkedHashMap<String, HashMap<String, String>>();
    public static LinkedHashMap<String, HashMap<String, String>> homeInfo = new LinkedHashMap<String, HashMap<String, String>>();
    public static LinkedHashMap<String, String> homeid = new LinkedHashMap<String, String>();
    public static LinkedHashMap<String, Game> homegame = new LinkedHashMap<String, Game>();

}
