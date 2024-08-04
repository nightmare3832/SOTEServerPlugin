package sote.skywarssolo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.inventory.InventoryPickupItemEvent;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.scheduler.Task;
import sote.Game;
import sote.GameProvider;
import sote.Main;
import sote.PlayerData;
import sote.PlayerDataManager;
import sote.event.Event_EntityDamageEvent;
import sote.home.Home;
import sote.inventory.Inventorys;
import sote.inventory.home.HomeInventory;
import sote.inventory.skywars.SkywarsSoloCageInventory;
import sote.inventory.skywars.SkywarsSoloDeathInventory;
import sote.inventory.skywars.SkywarsSoloInventory;
import sote.inventory.skywars.SkywarsSoloLobbyInventory;
import sote.inventory.skywars.SkywarsSoloWaitInventory;
import sote.particle.Firework;
import sote.party.Party;
import sote.popup.Popups;
import sote.popup.SkywarsGamePopup;
import sote.popup.SkywarsWaitPopup;
import sote.popup.WallsPopup;
import sote.setting.Setting;
import sote.skywarssolo.stage.MoistenedDesert;
import sote.skywarssolo.stage.SkywarsSoloStage;
import sote.skywarssolo.stage.TheNature;
import sote.stat.Stat;

public class SkywarsSolo extends Game{

    public static final int MAX_PLAYERS = 12;
    public static final int MIN_PLAYERS = 2;
    public static final int VIP_PLAYERS = 10;

    public static final int WAIT_TIME = 10;
    public static final int WAIT_HOME_TIME = 10;
    public static final int GAME_TIME = 400;

    public SkywarsSolo(int number, boolean isHome){
        super(number, isHome);
        new SkywarsSoloSignManager();Object a = new MoistenedDesert();
        stagelist = new SkywarsSoloStage[]{new TheNature()};
        stagelist2 = new SkywarsSoloStage[]{new TheNature()};
        addRoom();
    }

    @Override
    public void addRoom(){
        GameData = new HashMap<String,String>();
        GameData.put("count","0");
        GameData.put("deadcount","0");
        GameData.put("lifecount","0");
        GameData.put("time","0");
        GameData.put("cagetime","0");
        GameData.put("gametime","0");
        GameData.put("timenow","0");
        GameData.put("cagenow","0");
        GameData.put("gamenow","0");
        GameData.put("gamenow2","0");
        Players = new LinkedHashMap<String,Player>();
        Spectators = new LinkedHashMap<String,Player>();
        AllPlayers = new LinkedHashMap<String,Player>();
        canOpenChest = false;
        setting = new HashMap<String, String>();
        setting.put("map", "default");
        //SkywarsSoloSignManager.updataSign(number);
        String url = Server.getInstance().getDataPath()+"/worlds/skywars";
        File newdir = new File(url+number);
        newdir.delete();
        newdir.mkdir();
        File newdir2 = new File(url+number+"/region");
        newdir2.delete();
        newdir2.mkdir();
        directoryCopy(new File(url),new File(url+number));
    }

    @Override
    public void reset(){
        GameData = new HashMap<String,String>();
        GameData.put("count","0");
        GameData.put("deadcount","0");
        GameData.put("lifecount","0");
        GameData.put("time","0");
        GameData.put("cagetime","0");
        GameData.put("gametime","0");
        GameData.put("timenow","0");
        GameData.put("cagenow","0");
        GameData.put("gamenow","0");
        GameData.put("gamenow2","0");
        Players = new LinkedHashMap<String,Player>();
        Spectators = new LinkedHashMap<String,Player>();
        AllPlayers = new LinkedHashMap<String,Player>();
        canOpenChest = false;
        setting = new HashMap<String, String>();
        setting.put("map", "default");
        SkywarsSoloSignManager.updataSign(number);
    }

    @Override
    public void setSetting(String key, String value){
        setting.put(key, value);
        if(key.equals("map") && !setting.get("map").equals("default")){
            SkywarsSoloStage map = null;
            switch(setting.get("map")){
                case "Moistened Desert":
                    map = new MoistenedDesert();
                case "The Nature":
                    map = new TheNature();
            }
            stage = map;
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
              }
              else{
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
        String ff = f.replace("skywars/","");
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

    @Override
    public void Join(Player player){
        Join(player, false);
    }

    public void Join(Player player, boolean party){
        if(Players.containsValue(player)) return;
        Map<String, String> mapp = (Map<String, String>) Party.partyData.get(player.getName().toLowerCase());
        if(!party && mapp.get("owner").equals(player.getName().toLowerCase()) && number <= 7){
            Map<String, String> map = (Map<String, String>) Party.partyData.get(mapp.get("owner"));
            String member = map.get("member");
            String[] members = member.split(",");
            Player target;
            HashMap<Player,Boolean> targets = new HashMap<Player,Boolean>();
            HashMap<String,Boolean> offt = new HashMap<String,Boolean>();
            for(String name:members){
                target = Server.getInstance().getPlayer(name);
                if(target instanceof Player){
                    targets.put(target,Main.gamenow.get(target));
                }
            }
            int c = 0;
            for(Map.Entry<Player,Boolean> e : targets.entrySet()){
                if(!e.getValue()){
                    if(!AllPlayers.containsKey(e.getKey())) c++;
                }
            }
            if(!((getGameDataAsInt("count")+c) >= MAX_PLAYERS)){
                for(Map.Entry<Player,Boolean> e : targets.entrySet()){
                    if(!e.getValue() && !e.getKey().getName().equals(player.getName())){
                        if(!AllPlayers.containsKey(e.getKey())) Join(e.getKey(), true);
                    }
                }
            }else{
                player.sendMessage(Main.getMessage(player,"party.no.join.game"));
                return;
            }
        }
        if(getGameDataAsBoolean("gamenow2") || (getGameDataAsBoolean("timenow") && getGameDataAsInt("time") <= 0)){
            return;
        }
        if(getGameDataAsInt("count") >= MAX_PLAYERS){
            if(number >= 8) LookJoin(player);
            return;
        }
        GameProvider.joinGame(player, this);
        setGameDataAsInt("count", getGameDataAsInt("count") + 1, false);
        setGameDataAsInt("lifecount", getGameDataAsInt("count"), false);
        if((getGameDataAsInt("count") >= MIN_PLAYERS || number >= 8) && !getGameDataAsBoolean("timenow")){
            setGameDataAsBoolean("timenow", true, true);
            startTime();
        }
        Players.put(player.getName(),player);
        AllPlayers.put(player.getName(),player);
        Popups.setData(player, new SkywarsWaitPopup(player));
        Inventorys.setData(player,new SkywarsSoloWaitInventory());
        if(number <= 7) Teleport(player);
        player.setAllowFlight(false);
        Main.gamenow.put(player, true);
    }

    @Override
    public void LookJoin(Player player){
        Spectators.put(player.getName(),player);
        AllPlayers.put(player.getName(),player);
        Inventorys.setData(player,new SkywarsSoloWaitInventory());
        if(number <= 7) Teleport(player);
        Main.gamenow.put(player, true);
    }

    @Override
    public void Quit(Player player){
        if(!Players.containsValue(player)){
            LookQuit(player);
            return;
        }
        setGameDataAsInt("count", getGameDataAsInt("count") - 1, true);
        if(player.getGamemode() == 0 && getGameDataAsBoolean("gamenow")){
            death(player, false, true);
            checkMember();
        }
        if(number <= 7){
            player.teleport(new Position(-32,57,13,Server.getInstance().getLevelByName("skywars")));
            Popups.setData(player, new WallsPopup(player));
            Inventorys.setData(player,new SkywarsSoloLobbyInventory());
        }else{
             player.teleport(Server.getInstance().getLevelByName("home"+Home.getId(number)).getSafeSpawn());
             Inventorys.setData(player,new HomeInventory());
        }
        Players.remove(player.getName());
        AllPlayers.remove(player.getName());
        player.setMaxHealth(20);
        player.setGamemode(2);
        player.setAllowFlight(false);
        Main.gamenow.put(player, false);
        GameProvider.quitGame(player);
    }

    @Override
    public void LookQuit(Player player){
        if(!AllPlayers.containsKey(player)) return;
        if(number <= 7){
            player.teleport(new Position(-32,57,13,Server.getInstance().getLevelByName("skywars")));
            Inventorys.setData(player,new SkywarsSoloLobbyInventory());
        }else{
             player.teleport(Server.getInstance().getLevelByName("home"+Home.getId(number)).getSafeSpawn());
             Inventorys.setData(player,new HomeInventory());
        }
        Spectators.remove(player.getName());
        AllPlayers.remove(player.getName());
        Main.gamenow.put(player, false);
    }

    public void Teleport(Player player){
        if(!Server.getInstance().isLevelLoaded("skywars"+number)){
            world();
            Server.getInstance().loadLevel("skywars"+number);
        }
        Level level = Server.getInstance().getLevelByName("skywars"+number);
        player.teleport(new Position(109,54,158,level));
    }

    public void startTime(){
        if(number <= 7){
            setGameDataAsInt("time", WAIT_TIME, true);
        }else{
            setGameDataAsInt("time", WAIT_HOME_TIME, true);
        }
        for (Map.Entry<String,Player> e : AllPlayers.entrySet()){
            e.getValue().sendTip(Main.getMessage(e.getValue(),"skywars.wait.count",new String[]{String.valueOf(getGameDataAsInt("time"))}));
        }
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackTime(this),20);
    }

    public void Time(){
        if(getGameDataAsInt("time") <= 0){
            for (Map.Entry<String,Player> e : AllPlayers.entrySet()){
                e.getValue().setDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_NO_AI, false);
            }
            Start();
            return;
        }else if(getGameDataAsInt("time") == 10){
            world();
            Server.getInstance().loadLevel("skywars"+number);
            world = Server.getInstance().getLevelByName("skywars"+number);
            if(setting.get("map").equals("default")) stage = stagelist[(int)(Math.random()*(stagelist.length))];
        }else if(getGameDataAsInt("time") == 5){
            Vector3[] poss = stage.getSpawn();
            for(Vector3 pos : poss){
                world.loadChunk((int) pos.x >> 4, (int) pos.x >> 4);
            }
        }
        setGameDataAsInt("time", getGameDataAsInt("time") - 1, true);
        for (Map.Entry<String,Player> e : AllPlayers.entrySet()){
            e.getValue().sendTip(Main.getMessage(e.getValue(),"skywars.wait.count",new String[]{String.valueOf(getGameDataAsInt("time"))}));
        }
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackTime(this),20);
    }

    public void startCageTime(){
        setGameDataAsInt("cagetime", /*CAGE_TIME*/10, false);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackCageTime(this),20);
    }

    public void CageTime(){
        if(!getGameDataAsBoolean("cagenow")) return;
        if(getGameDataAsInt("cagetime") <= 0){
            setGameDataAsBoolean("cagenow", false, false);
            disappearCage();
            return;
        }
        if(getGameDataAsInt("cagetime") <= 5){
            for (Map.Entry<String,Player> e : AllPlayers.entrySet()){
                e.getValue().setSubtitle(Main.getMessage(e.getValue(),"skywars.prepare.to.fight.sub"));
                e.getValue().sendTitle(Main.getMessage(e.getValue(),"skywars.prepare.to.fight",new String[]{String.valueOf(getGameDataAsInt("cagetime"))}));
            }
        }
        setGameDataAsInt("cagetime", getGameDataAsInt("cagetime") - 1, false);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackCageTime(this),20);
    }

    public void startGameTime(){
        setGameDataAsInt("gametime", GAME_TIME, false);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackGameTime(this),20);
    }

    public void GameTime(){
        if(!getGameDataAsBoolean("gamenow")) return;
        if(getGameDataAsInt("gametime") <= 0){
            finish(1);
            return;
        }
        if(getGameDataAsInt("gametime") % 60 == 0){
            for (Map.Entry<String,Player> e : AllPlayers.entrySet()){
                e.getValue().sendMessage(Main.getMessage(e.getValue(),"skywars.game.count",new String[]{String.valueOf(getGameDataAsInt("gametime"))}));
            }
        }
        setGameDataAsInt("gametime", getGameDataAsInt("gametime") - 1, false);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackGameTime(this),20);
    }

    public void Start(){
        if(getGameDataAsInt("count") < MIN_PLAYERS){
            setGameDataAsBoolean("number", false, true);
            if(number >= 8) Home.notStart(number);
            return;
        }
        int c = 0;
        Vector3[] pos = stage.getSpawn();
        new SkywarsSoloChestManager(this);
        for (Map.Entry<String,Player> e : Players.entrySet()){
            Map<UUID,Player> players = Server.getInstance().getOnlinePlayers();
            killcount.put(PlayerDataManager.getPlayerData(e.getValue()), 0);
            Main.setHide.put(e.getValue(), false);
            e.getValue().setGamemode(2);
            e.getValue().setMaxHealth(20);
            e.getValue().setHealth(20);
            e.getValue().setFoodEnabled(true);
            e.getValue().getFoodData().setFoodLevel(20);
            Position posobj = new Position(pos[c].x, pos[c].y, pos[c].z, world);
            SkywarsSoloCageManager.appearCage(this, e.getValue(), posobj);
            cagepos.put(e.getValue(), posobj);
            e.getValue().teleport(posobj);
            Popups.setData(e.getValue(), new SkywarsGamePopup(e.getValue()));
            Inventorys.setData(e.getValue(),new SkywarsSoloCageInventory());
            Stat.setSkywarsPlays(e.getValue(), Stat.getSkywarsPlays(e.getValue()) + 1);
            c++;
        }
        c = 0;
        for (Map.Entry<String,Player> e : Spectators.entrySet()){
            Main.setHide.put(e.getValue(),false);
            for (Map.Entry<String,Player> ee : Players.entrySet()){
                ee.getValue().hidePlayer(e.getValue());
            }
            e.getValue().setGamemode(3);
            e.getValue().setAllowFlight(true);
            e.getValue().getInventory().clearAll();
            Inventorys.setData(e.getValue(),new SkywarsSoloDeathInventory());
            e.getValue().teleport(new Position(pos[c].x, pos[c].y, pos[c].z, world));
            c++;
        }
        canOpenChest = true;
        setGameDataAsBoolean("cagenow", true, false);
        setGameDataAsBoolean("gamenow", true, true);
        startCageTime();
    }

    public void disappearCage(){
        this.world.setTime(Level.TIME_DAY);
        for (Map.Entry<String,Player> e : Players.entrySet()){
            e.getValue().setGamemode(0);
            Inventorys.setData(e.getValue(),new SkywarsSoloInventory());
            SkywarsSoloCageManager.disappearCage(this, e.getValue(), cagepos.get(e.getValue()));
        }
        setGameDataAsBoolean("gamenow2", true, false);
        startGameTime();
    }

    public static boolean isNumber(String num){
        try {
            Integer.parseInt(num);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public void kill(Player player, Player death){
        int coin = (int)(Math.random() * 20)+5;
        int exp = (int)(Math.random() * 10)+5;
        Stat.addCoin(player, coin);
        Stat.addExp(player,exp);
        Stat.setSkywarsKills(player, Stat.getSkywarsKills(player) + 1);
        player.sendMessage(Main.getMessage(player,"status.add.coin",new String[]{String.valueOf(coin)}));
        player.sendMessage(Main.getMessage(player,"status.add.exp",new String[]{String.valueOf(exp)}));
        killcount.put(PlayerDataManager.getPlayerData(player),killcount.get(player)+1);
        LevelEventPacket pk = new LevelEventPacket();
        pk.evid = 1051;
        pk.x = (float)player.x;
        pk.y = (float)player.y;
        pk.z = (float)player.z;
        pk.data = 0;
        player.dataPacket(pk);
        EntityDamageEvent.DamageCause c = Event_EntityDamageEvent.lastcause.get(death).getCause();
        if(c == EntityDamageEvent.DamageCause.VOID){
            for (Map.Entry<String,Player> e : Players.entrySet()){
                if(e.getValue().getName().equals(player.getName())) e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.void.by", new String[]{"§6"+death.getName(),"§b"+player.getName()}));
                else if(e.getValue().getName().equals(death.getName())) e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.void.by", new String[]{"§6"+death.getName(),"§7"+player.getName()}));
                else e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.void.by", new String[]{"§7"+death.getName(),"§7"+player.getName()}));
            }
        }else if(c == EntityDamageEvent.DamageCause.FIRE || c == EntityDamageEvent.DamageCause.FIRE_TICK){
            for (Map.Entry<String,Player> e : Players.entrySet()){
                if(e.getValue().getName().equals(player.getName())) e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.born.by", new String[]{"§6"+death.getName(),"§b"+player.getName()}));
                else if(e.getValue().getName().equals(death.getName())) e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.born.by", new String[]{"§6"+death.getName(),"§7"+player.getName()}));
                else e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.born.by", new String[]{"§7"+death.getName(),"§7"+player.getName()}));
            }
        }else if(c == EntityDamageEvent.DamageCause.FALL){
            for (Map.Entry<String,Player> e : Players.entrySet()){
                if(e.getValue().getName().equals(player.getName())) e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.fall.by", new String[]{"§6"+death.getName(),"§b"+player.getName()}));
                else if(e.getValue().getName().equals(death.getName())) e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.fall.by", new String[]{"§6"+death.getName(),"§7"+player.getName()}));
                else e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.fall.by", new String[]{"§7"+death.getName(),"§7"+player.getName()}));
            }
        }else if(c == EntityDamageEvent.DamageCause.LAVA){
            for (Map.Entry<String,Player> e : Players.entrySet()){
                if(e.getValue().getName().equals(player.getName())) e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.lava.by", new String[]{"§6"+death.getName(),"§b"+player.getName()}));
                else if(e.getValue().getName().equals(death.getName())) e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.lava.by", new String[]{"§6"+death.getName(),"§7"+player.getName()}));
                else e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.lava.by", new String[]{"§7"+death.getName(),"§7"+player.getName()}));
            }
        /*}else if(c == EntityDamageEvent.CAUSE_ENTITY_EXPLOSION){
            for (Map.Entry<String,Player> e : data2.get(data4.get(player)).entrySet()){
                if(e.getValue().getName().equals(player.getName())) e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.explosion.by", new String[]{"§6"+death.getName(),"§b"+player.getName()}));
                else if(e.getValue().getName().equals(death.getName())) e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.explosion.by", new String[]{"§6"+death.getName(),"§7"+player.getName()}));
                else e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.explosion.by", new String[]{"§7"+death.getName(),"§7"+player.getName()}));
            }*/
        }else{
            for (Map.Entry<String,Player> e : Players.entrySet()){
                if(e.getValue().getName().equals(player.getName())) e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.kill.by", new String[]{"§6"+death.getName(),"§b"+player.getName()}));
                else if(e.getValue().getName().equals(death.getName())) e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.kill.by", new String[]{"§6"+death.getName(),"§7"+player.getName()}));
                else e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.kill.by", new String[]{"§7"+death.getName(),"§7"+player.getName()}));
            }
        }
    }

    public void death(Player player, boolean isKilledByPlayer, boolean isLeft){
        if(Players.containsValue(player)){
            if(isLeft){
                for (Map.Entry<String,Player> e : Players.entrySet()){
                    e.getValue().sendMessage(Main.getMessage(player, "skywars.death.left", new String[]{"§7"+player.getName()}));
                }
            }else if(!isKilledByPlayer){
                EntityDamageEvent.DamageCause c = Event_EntityDamageEvent.lastcause.get(player).getCause();
                if(c == EntityDamageEvent.DamageCause.VOID){
                    for (Map.Entry<String,Player> e : Players.entrySet()){
                        if(e.getValue().getName().equals(player.getName())) e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.void", new String[]{"§6"+player.getName()}));
                        else e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.void", new String[]{"§7"+player.getName()}));
                    }
                }else if(c == EntityDamageEvent.DamageCause.FIRE || c == EntityDamageEvent.DamageCause.FIRE_TICK){
                    for (Map.Entry<String,Player> e : Players.entrySet()){
                        if(e.getValue().getName().equals(player.getName())) e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.born", new String[]{"§6"+player.getName()}));
                        else e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.born", new String[]{"§7"+player.getName()}));
                    }
                }else if(c == EntityDamageEvent.DamageCause.FALL){
                    for (Map.Entry<String,Player> e : Players.entrySet()){
                        if(e.getValue().getName().equals(player.getName())) e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.fall", new String[]{"§6"+player.getName()}));
                        else e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.fall", new String[]{"§7"+player.getName()}));
                    }
                }else if(c == EntityDamageEvent.DamageCause.LAVA){
                    for (Map.Entry<String,Player> e : Players.entrySet()){
                        if(e.getValue().getName().equals(player.getName())) e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.lava", new String[]{"§6"+player.getName()}));
                        else e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.lava", new String[]{"§7"+player.getName()}));
                    }
                /*}else if(c == EntityDamageEvent.CAUSE_ENTITY_EXPLOSION){
                    for (Map.Entry<String,Player> e : data2.get(data4.get(player)).entrySet()){
                        if(e.getValue().getName().equals(player.getName())) e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.explosion.fall", new String[]{"§6"+player.getName()}));
                        else e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.explosion.fall", new String[]{"§7"+player.getName()}));
                    }*/
                }else{//TODO more reason
                    for (Map.Entry<String,Player> e : Players.entrySet()){
                        if(e.getValue().getName().equals(player.getName())) e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.void", new String[]{"§6"+player.getName()}));
                        else e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.void", new String[]{"§7"+player.getName()}));
                    }
                }
            }
            Stat.setSkywarsDeaths(player, Stat.getSkywarsDeaths(player) + 1);
            LevelEventPacket pk = new LevelEventPacket();
            pk.evid = 2001;
            pk.x = (float) player.x;
            pk.y = (float) player.y+1;
            pk.z = (float) player.z;
            pk.data = 152 + (0 << 12);
            player.dataPacket(pk);
            for (Map.Entry<String,Player> e : Players.entrySet()){
                e.getValue().dataPacket(pk);
                e.getValue().dataPacket(pk);
                e.getValue().dataPacket(pk);
                e.getValue().hidePlayer(player);
            }
            player.setGamemode(3);
            player.setAllowFlight(true);
            Map<Integer,Item> items = player.getInventory().getContents();
                for (Map.Entry<Integer,Item> e : items.entrySet()){
                    player.getLevel().dropItem(new Vector3(player.x,player.y,player.z),e.getValue());
                }
            player.getInventory().clearAll();
            player.teleport(new Vector3(player.x,128,player.z));
            player.setMaxHealth(20);
            player.setHealth(20);
            player.removeAllEffects();
            player.setOnFire(0);
            Inventorys.setData(player,new SkywarsSoloDeathInventory());
            for (Map.Entry<String,Player> e : Players.entrySet()){
                if(e.getValue().getGamemode() == 3){
                    Inventorys.data2.get(e.getValue()).register(e.getValue());
                }
            }
            checkMember();
        }
    }

    public void finish(int type, Player winner){
        Entity[] items = world.getEntities();
            for(Entity entity: items){
                if(entity instanceof EntityItem){
                    entity.kill();
                }
            }
        List<Map.Entry<PlayerData, Integer>> sort = Sort(killcount);
            if(type == 2){
                for (Map.Entry<String, Player> e : AllPlayers.entrySet()){
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line"));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"skywars.result.win",new String[]{winner.getName()}));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"skywars.result.stage",new String[]{stage.getName()}));
                    if(sort.size() >= 1) e.getValue().sendMessage(Main.getMessage(e.getValue(),"skywars.result.win1",new String[]{sort.get(0).getKey().getName(),String.valueOf(sort.get(0).getValue())}));
                    if(sort.size() >= 2) e.getValue().sendMessage(Main.getMessage(e.getValue(),"skywars.result.win2",new String[]{sort.get(1).getKey().getName(),String.valueOf(sort.get(1).getValue())}));
                    if(sort.size() >= 3) e.getValue().sendMessage(Main.getMessage(e.getValue(),"skywars.result.win3",new String[]{sort.get(2).getKey().getName(),String.valueOf(sort.get(2).getValue())}));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line2"));
                    e.getValue().removeAllEffects();
                    e.getValue().setMaxHealth(20);
                    e.getValue().setHealth(20);
                    e.getValue().setOnFire(0);
                    e.getValue().setFoodEnabled(false);
                    e.getValue().getFoodData().setFoodLevel(20);
                    if(e.getValue().equals(winner)){
                        e.getValue().setSubtitle(Main.getMessage(e.getValue(), "skywars.victory.sub"));
                        e.getValue().sendTitle(Main.getMessage(e.getValue(), "skywars.victory"));
                    }
                }
                firework(winner);
            }else if(type == 1){
                for (Map.Entry<String, Player> e : AllPlayers.entrySet()){
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line"));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"skywars.result.timeover"));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"skywars.result.stage",new String[]{stage.getName()}));
                    if(sort.size() >= 1) e.getValue().sendMessage(Main.getMessage(e.getValue(),"skywars.result.win1",new String[]{sort.get(0).getKey().getName(),String.valueOf(sort.get(0).getValue())}));
                    if(sort.size() >= 2) e.getValue().sendMessage(Main.getMessage(e.getValue(),"skywars.result.win2",new String[]{sort.get(1).getKey().getName(),String.valueOf(sort.get(1).getValue())}));
                    if(sort.size() >= 3) e.getValue().sendMessage(Main.getMessage(e.getValue(),"skywars.result.win3",new String[]{sort.get(2).getKey().getName(),String.valueOf(sort.get(2).getValue())}));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line2"));
                    e.getValue().removeAllEffects();
                    e.getValue().setMaxHealth(20);
                    e.getValue().setHealth(20);
                    e.getValue().extinguish();
                    e.getValue().setFoodEnabled(false);
                    e.getValue().getFoodData().setFoodLevel(20);
                }
            }
        canOpenChest = false;
        setGameDataAsBoolean("gamenow2", false, true);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackFinish(this, type, winner),200);
    }

    public void firework(Player winner){
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackFirework(this, winner),0);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackFirework(this, winner),20);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackFirework(this, winner),40);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackFirework(this, winner),60);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackFirework(this, winner),80);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackFirework(this, winner),100);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackFirework(this, winner),120);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackFirework(this, winner),140);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackFirework(this, winner),160);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackFirework(this, winner),180);
    }

    public void fireworkRun(Player player){
        if(Players.containsValue(player)){
            for (Map.Entry<String, Player> e : Players.entrySet()){
                if(e.getValue() instanceof Player){
                    Vector3 winner = new Vector3(player.x,player.y+1,player.z);
                    Firework.start(winner,e.getValue());
                }
            }
        }
    }

    public void finish(int type){
        finish(type,null);
    }

    public void finishLater(int type, Player winner){
        if(type == 2){
            for (Map.Entry<String,Player> e : Players.entrySet()){
                if(Players.containsValue(e.getValue())){
                    if(winner.getName().equals(e.getValue().getName())){
                        int coin = (int)(Math.random() * 50)+10;
                        int exp = (int)(Math.random() * 50)+10;
                        Stat.addCoin(e.getValue(), coin);
                        Stat.addExp(e.getValue(),exp);
                        e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line"));
                        e.getValue().sendMessage(Main.getMessage(e.getValue(),"skywars.result"));
                        e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.coin",new String[]{String.valueOf(coin)}));
                        e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.exp",new String[]{String.valueOf(exp)}));
                        e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line2"));
                        Stat.setSkywarsWin(e.getValue(),Stat.getSkywarsWin(e.getValue())+1);
                    }else{
                        int coin = (int)(Math.random() * 10)+1;
                        int exp = (int)(Math.random() * 10)+1;
                        Stat.addCoin(e.getValue(), coin);
                        Stat.addExp(e.getValue(),exp);
                        e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line"));
                        e.getValue().sendMessage(Main.getMessage(e.getValue(),"skywars.result"));
                        e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.coin",new String[]{String.valueOf(coin)}));
                        e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.exp",new String[]{String.valueOf(exp)}));
                        e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line2"));
                        Stat.setSkywarsLose(e.getValue(),Stat.getSkywarsLose(e.getValue())+1);
                    }
                }
            }
        }else if(type == 1){
            for (Map.Entry<String,Player> e : Players.entrySet()){
                if(Players.containsValue(e.getValue())){
                    int coin = (int)(Math.random() * 5)+1;
                    int exp = (int)(Math.random() * 5)+1;
                    Stat.addCoin(e.getValue(), coin);
                    Stat.addExp(e.getValue(),exp);
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line"));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"skywars.result"));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.coin",new String[]{String.valueOf(coin)}));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.exp",new String[]{String.valueOf(exp)}));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line2"));
                    Stat.setSkywarsLose(e.getValue(),Stat.getSkywarsLose(e.getValue())+1);
                }
            }
        }
        for (Map.Entry<String,Player> e : Players.entrySet()){
            if(!(e.getValue() instanceof Player)) continue;
            for (Map.Entry<String,Player> ee : Players.entrySet()){
                ee.getValue().showPlayer(e.getValue());
            }
            e.getValue().setGamemode(2);
            e.getValue().setGamemode(1);
            e.getValue().setGamemode(2);
            e.getValue().extinguish();
            e.getValue().removeAllEffects();
            Stat.setNameTag(e.getValue());
            e.getValue().setAttribute(Attribute.getAttribute(Attribute.MAX_HUNGER).setValue(20));
            e.getValue().setAttribute(Attribute.getAttribute(Attribute.EXPERIENCE).setValue(0));
            e.getValue().setAttribute(Attribute.getAttribute(Attribute.EXPERIENCE_LEVEL).setValue(0));
            e.getValue().setAllowFlight(false);
        }
        for (Map.Entry<String,Player> e : Players.entrySet()){
            for (Map.Entry<String,Player> ee : Players.entrySet()){
                ee.getValue().showPlayer(e.getValue());
                e.getValue().showPlayer(ee.getValue());
            }
            if(number <= 7){
                e.getValue().teleport(new Position(-32,57,13,Server.getInstance().getLevelByName("skywars")));
                Popups.setData(e.getValue(), new WallsPopup(e.getValue()));
                Inventorys.setData(e.getValue(),new SkywarsSoloLobbyInventory());
            }else{
                 e.getValue().teleport(Server.getInstance().getLevelByName("home"+Home.getId(number)).getSafeSpawn());
                 Inventorys.setData(e.getValue(),new HomeInventory());
            }
            Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackAllow(this, e.getValue()),1);
            Main.gamenow.put(e.getValue(), false);
        }
        reset();
        if(Server.getInstance().isLevelLoaded("skywars"+number)) Server.getInstance().unloadLevel(Server.getInstance().getLevelByName("skywars"+number));
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackWorld(this),40);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackNow(this),100);
        if(number >= 8) Home.finish(number);
    }

    public void allow(Player player){
        player.setAllowFlight(false);
    }

    public void world(){
        String url = Server.getInstance().getDataPath()+"/worlds/skywars";
        directoryCopy(new File(url),new File(url+number));
    }

    public void now(){
        setGameDataAsBoolean("gamenow2", false, true);
    }

    public void Pickup(InventoryPickupItemEvent event){
        Item item = event.getItem().getItem();
        int id = item.getId();
            if(event.getInventory().getHolder() instanceof Player && AllPlayers.containsKey(event.getInventory().getHolder())){
                Player player = (Player)event.getInventory().getHolder();
                if(player.getGamemode() == 3){
                    event.setCancelled();
                    return;
                }
            }
    }

    public void checkMember(){
        Player p = null;
        int count = 0;
        for (Map.Entry<String,Player> e : Players.entrySet()){
            if(e.getValue().getGamemode() == 0){
                p = e.getValue();
                count++;
            }
        }
        setGameDataAsInt("lifecount", count, false);
        if(count == 1){
            finish(2, p);
        }
    }

    public void chat(PlayerChatEvent event){
        Player player = event.getPlayer();
        if(Players.containsValue(player)){
            event.setCancelled();
            if(Main.Chat.get(player.getName()) <= 0){
                player.sendMessage(Main.getMessage(player,"chat.wait"));
                return;
            }
            for (Map.Entry<String,Player> e : Players.entrySet()){
                if(getGameDataAsBoolean("gamenow2")){
                    if(player.getGamemode() == 0 && !Setting.getChatHide(e.getValue())){
                        e.getValue().sendMessage("<"+player.getNameTag()+"§f> "+event.getMessage());
                    }else if(player.getGamemode() == 3 && !Setting.getChatHide(e.getValue())){
                        if(e.getValue().getGamemode() == 3){
                            e.getValue().sendMessage("§7<"+player.getName()+"> "+event.getMessage());
                        }
                    }
                }else{
                    if(!Setting.getChatHide(e.getValue()))
                    e.getValue().sendMessage("<"+player.getNameTag()+"§f> "+event.getMessage());
                }
            }
            int ch = Main.Chat.get(player.getName());
            Main.Chat.put(player.getName(),ch-1);
            Server.getInstance().getLogger().info("<"+player.getNameTag()+"§f> "+event.getMessage());
        }
    }

    public int getKillCount(Player player){
        if(!killcount.containsKey(player)) return 0;
        return killcount.get(player);
    }

    public List Sort(Map<PlayerData, Integer> map){
        Map<PlayerData, Integer> hashMap = new HashMap<PlayerData, Integer>();
        for (Map.Entry<PlayerData, Integer> e : map.entrySet()){
            hashMap.put(e.getKey(),e.getValue());
        }
        List<Map.Entry<PlayerData, Integer>> entries =
              new ArrayList<Map.Entry<PlayerData, Integer>>(hashMap.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<PlayerData, Integer>>() {

            @Override
            public int compare(
                  Entry<PlayerData, Integer> entry1, Entry<PlayerData, Integer> entry2) {
                return ((Integer)entry2.getValue()).compareTo((Integer)entry1.getValue());
            }
        });
        return entries;
    }

    public SkywarsSoloStage stage;
    public HashMap<PlayerData,Integer> killcount = new HashMap<PlayerData,Integer>();
    public HashMap<Player,Vector3> cagepos = new HashMap<Player,Vector3>();
    public HashMap<String,String> setting = new HashMap<String,String>();
    public SkywarsSoloStage[] stagelist;
    public static SkywarsSoloStage[] stagelist2;
}
class CallbackTime extends Task{

    public SkywarsSolo owner;
    public int number;

    public CallbackTime(SkywarsSolo owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.Time();
    }
}
class CallbackCageTime extends Task{

    public SkywarsSolo owner;

    public CallbackCageTime(SkywarsSolo owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.CageTime();
    }
}
class CallbackGameTime extends Task{

    public SkywarsSolo owner;

    public CallbackGameTime(SkywarsSolo owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.GameTime();
    }
}
class CallbackFinish extends Task{

    public SkywarsSolo owner;
    public int type;
    public Player winner;

    public CallbackFinish(SkywarsSolo owner,int type,Player winner){
        this.owner = owner;
        this.type = type;
        this.winner = winner;
    }

    public void onRun(int d){
        this.owner.finishLater(this.type,this.winner);
    }
}
class CallbackWorld extends Task{

    public SkywarsSolo owner;

    public CallbackWorld(SkywarsSolo owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.world();
    }
}
class CallbackAllow extends Task{

    public SkywarsSolo owner;
    public Player player;

    public CallbackAllow(SkywarsSolo owner, Player player){
        this.owner = owner;
        this.player = player;
    }

    public void onRun(int d){
        this.owner.allow(this.player);
    }
}
class CallbackNow extends Task{

    public SkywarsSolo owner;

    public CallbackNow(SkywarsSolo owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.now();
    }
}
class CallbackFirework extends Task{

    public SkywarsSolo owner;
    public Player player;

    public CallbackFirework(SkywarsSolo owner, Player player){
        this.owner = owner;
        this.player = player;
    }

    public void onRun(int d){
        this.owner.fireworkRun(this.player);
    }
}