package sote.miniwalls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.entity.ProjectileHitEvent;
import cn.nukkit.event.inventory.InventoryPickupItemEvent;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.MovingObjectPosition;
import cn.nukkit.level.Position;
import cn.nukkit.level.particle.DestroyBlockParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.AddPlayerPacket;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.network.protocol.MobArmorEquipmentPacket;
import cn.nukkit.network.protocol.PlayerListPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.network.protocol.TakeItemEntityPacket;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import cn.nukkit.potion.Effect;
import cn.nukkit.scheduler.Task;
import sote.Game;
import sote.GameProvider;
import sote.Main;
import sote.home.Home;
import sote.inventory.Inventorys;
import sote.inventory.home.HomeInventory;
import sote.inventory.miniwalls.MiniwallsArcherInventory;
import sote.inventory.miniwalls.MiniwallsBuilderInventory;
import sote.inventory.miniwalls.MiniwallsDeathInventory;
import sote.inventory.miniwalls.MiniwallsInventory;
import sote.inventory.miniwalls.MiniwallsLobbyInventory;
import sote.inventory.miniwalls.MiniwallsRespawnInventory;
import sote.inventory.miniwalls.MiniwallsSoldierInventory;
import sote.inventory.miniwalls.MiniwallsWaitInventory;
import sote.miniwalls.stage.Castle;
import sote.miniwalls.stage.Church;
import sote.miniwalls.stage.Farm;
import sote.miniwalls.stage.Japanese;
import sote.miniwalls.stage.MiniwallsStage;
import sote.particle.Firework;
import sote.party.Party;
import sote.popup.MiniwallsGamePopup;
import sote.popup.MiniwallsWaitPopup;
import sote.popup.Popups;
import sote.popup.WallsPopup;
import sote.setting.Setting;
import sote.stat.Stat;

public class Miniwalls extends Game{

    public static int MAX_PLAYERS = 4;

    public static final int WAIT_TIME = 10;
    public static final int WAIT_HOME_TIME = 10;
    public static final int FALL_WALL_TIME = 15;
    public static final int DEATH_MATCH_TIME = 300;

    public static int TEAM_PLAYERS  = 1;

    public Miniwalls(int number, boolean isHome){
        super(number, isHome);
        new MiniwallsSignManager();
        stagelist = new MiniwallsStage[]{new Farm(), new Japanese(), new Church(), new Castle()};
        Server.getInstance().getScheduler().scheduleRepeatingTask(new CallbackArrow(this),1);
        addRoom();
    }

    @Override
    public void addRoom(){
        GameData = new HashMap<String,String>();
        GameData.put("count","0");
        GameData.put("time","0");
        GameData.put("gametime","0");
        GameData.put("timenow","0");
        GameData.put("gamenow","0");
        GameData.put("gamenow2","0");
        GameData.put("deadcount","0");
        GameData.put("lifecount","0");
        GameData.put("walltime","0");
        GameData.put("wallnow","0");
        GameData.put("deathmatchnow","0");
        Players = new HashMap<String,Player>();
        Spectators = new HashMap<String,Player>();
        AllPlayers = new HashMap<String,Player>();
        wither = new HashMap<Integer,MiniwallsWitherManager>();
        canOpenChest = true;
        //MiniwallsSignManager.updataSign(number);
        String url = Server.getInstance().getDataPath()+"/worlds/miniwalls";
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
        GameData.put("gametime","0");
        GameData.put("walltime","0");
        GameData.put("timenow","0");
        GameData.put("gamenow","0");
        GameData.put("gamenow2","0");
        GameData.put("wallnow","0");
        GameData.put("deathmatchnow","0");
        Players = new HashMap<String,Player>();
        Spectators = new HashMap<String,Player>();
        AllPlayers = new HashMap<String,Player>();
        wither = new HashMap<Integer,MiniwallsWitherManager>();
        canOpenChest = true;
        MiniwallsSignManager.updataSign(number);
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
        String ff = f.replace("miniwalls/","");
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

    public void Join(Player player, Boolean party){
        if(getGameDataAsBoolean("gamenow") || getGameDataAsBoolean("gamenow2") || (getGameDataAsBoolean("timenow") && getGameDataAsInt("time") <= 0)){
            return;
        }
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
        if(getGameDataAsInt("count") >= MAX_PLAYERS){
            if(number >= 8) LookJoin(player);
            return;
        }
        GameProvider.joinGame(player, this);
        setGameDataAsInt("count", getGameDataAsInt("count") + 1, true);
        Players.put(player.getName(),player);
        AllPlayers.put(player.getName(),player);
        job.put(player, 0);
        Inventorys.setData(player,new MiniwallsWaitInventory());
        Popups.setData(player, new MiniwallsWaitPopup(player));
        if(number <= 7) Teleport(player);
        player.setAllowFlight(false);
        Main.gamenow.put(player, true);
        if(getGameDataAsInt("count") == MAX_PLAYERS){
            startTime(number);
        }
    }

    @Override
    public void LookJoin(Player player){
        Spectators.put(player.getName(),player);
        AllPlayers.put(player.getName(),player);
        Inventorys.setData(player,new MiniwallsWaitInventory());
        if(number <= 7) Teleport(player);
        Main.gamenow.put(player, true);
    }

    @Override
    public void Quit(Player player){
        if(!AllPlayers.containsValue(player)) return;
        if(!Players.containsValue(player)) LookQuit(player);
        setGameDataAsInt("count", getGameDataAsInt("count") - 1, true);
        if(player.getGamemode() == 0 && getGameDataAsBoolean("gamenow")){
            death(player);
            removePlayerFromGame(player);
        }
        if(number <= 7){
            player.teleport(new Position(2.5,7,-39.5,Server.getInstance().getLevelByName("walls")));
            Inventorys.setData(player,new MiniwallsLobbyInventory());
            Popups.setData(player, new WallsPopup(player));
        }else{
            player.teleport(Server.getInstance().getLevelByName("home"+Home.getId(number)).getSafeSpawn());
            Inventorys.setData(player,new HomeInventory());
        }
        Players.remove(player.getName());
        AllPlayers.remove(player.getName());
        Stat.setNameTag(player);
        Inventorys.setData(player, new MiniwallsLobbyInventory());
        player.setGamemode(2);
        player.setMaxHealth(20);
        player.setAllowFlight(false);
        Main.gamenow.put(player, false);
        GameProvider.quitGame(player);
    }

    @Override
    public void LookQuit(Player player){
        if(!AllPlayers.containsValue(player)) return;
        if(number <= 7){
            player.teleport(new Position(-1.5,56,-1.5,Server.getInstance().getLevelByName("miniwalls")));
            Inventorys.setData(player,new MiniwallsLobbyInventory());
        }else{
             player.teleport(Server.getInstance().getLevelByName("home"+Home.getId(number)).getSafeSpawn());
             Inventorys.setData(player,new HomeInventory());
        }
        Spectators.remove(player.getName());
        AllPlayers.remove(player.getName());
        Main.gamenow.put(player, false);
    }

    public void Teleport(Player player){
        if(!Server.getInstance().isLevelLoaded("miniwalls"+number)){
            world();
            Server.getInstance().loadLevel("miniwalls"+number);
        }
        Level level = Server.getInstance().getLevelByName("miniwalls"+number);
        player.teleport(new Position(-1.5,56,-1.5,level));
    }

    public void startTime(int number){
        if(number <= 7){
            setGameDataAsInt("time", WAIT_TIME, true);
        }else{
            setGameDataAsInt("time", WAIT_HOME_TIME, true);
        }
        for (Map.Entry<String,Player> e : Players.entrySet()){
            e.getValue().sendTip(Main.getMessage(e.getValue(),"miniwalls.wait.count",new String[]{String.valueOf(getGameDataAsInt("time"))}));
        }
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackTime(this),20);
    }

    public void Time(){
        if(getGameDataAsInt("time") <= 0){
            Start();
            return;
        }
        setGameDataAsInt("time", getGameDataAsInt("time") - 1, true);
        for (Map.Entry<String,Player> e : Players.entrySet()){
            e.getValue().sendTip(Main.getMessage(e.getValue(),"miniwalls.wait.count",new String[]{String.valueOf(getGameDataAsInt("time"))}));
        }
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackTime(this),20);
    }

    public void startBreakWallTime(){
        setGameDataAsInt("waatime", FALL_WALL_TIME, false);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackBreakWallTime(this),20);
    }

    public void BreakWallTime(){
        if(!getGameDataAsBoolean("gamenow")) return;
        if(getGameDataAsInt("walltime") <= 0){
            setGameDataAsBoolean("wallnow", false, false);
            BreakWall();
            startDeathMatchTime();
            return;
        }
        setGameDataAsInt("walltime", getGameDataAsInt("walltime") - 1, false);
        if(getGameDataAsInt("walltime") <= 5){
            for (Map.Entry<String,Player> e : AllPlayers.entrySet()){
                e.getValue().sendTip(Main.getMessage(e.getValue(),"miniwalls.wall.count",new String[]{String.valueOf(getGameDataAsInt("walltime"))}));
            }
        }
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackBreakWallTime(this),20);
    }

    public void startDeathMatchTime(){
        setGameDataAsInt("gametime", DEATH_MATCH_TIME, false);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackDeathMatchTime(this),20);
    }

    public void DeathMatchTime(){
        if(!getGameDataAsBoolean("gamenow")) return;
        if(getGameDataAsInt("gametime") <= 0){
            startDeathMatch();
            return;
        }
        setGameDataAsInt("gametime", getGameDataAsInt("gametime") - 1, false);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackDeathMatchTime(this),20);
    }

    public void startWitherGauge(){
        WitherGauge(0);
    }

    public void WitherGauge(int count){
        if(!getGameDataAsBoolean("gamenow")) return;
        for (Map.Entry<String,Player> e : AllPlayers.entrySet()){
            wither.get(count).sendGauge();
        }
        count++;
        if(count >= 4) count = 0;
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackWitherGauge(this, count),20);
    }

    public void Start(){
        world();
        stage = stagelist[(int)(Math.random()*(stagelist.length))];
        Server.getInstance().loadLevel("miniwalls"+number);
        world = Server.getInstance().getLevelByName("miniwalls"+number);
        int redcount = 0;
        int bluecount = 0;
        int greencount = 0;
        int yellowcount = 0;
        HashMap<Player,Boolean> already = new HashMap<Player,Boolean>();
        int count = 0;
        for (Map.Entry<String,Player> e : Players.entrySet()){
            //if(!team.containsKey(e.getValue())){
                /*Map<String, String> mapp = (Map<String, String>) Party.partyData.get(e.getValue().getName().toLowerCase());
                Map<String, String> map = (Map<String, String>) Party.partyData.get(mapp.get("owner"));
                HashMap<Player,Integer> chunk = new HashMap<Player,Integer>();
                chunk.put(e.getValue(), 0);
                int clancount = 1;
                if(mapp.get("created").equals("1")){
                    String member = map.get("member");
                    String[] members = member.split(",");
                    List<String> list = Arrays.asList(members);
                    String[] rmembers =(String[])list.toArray(new String[list.size()]);
                    Player target;
                    for(String name : rmembers){
                        if(name.equals(e.getValue().getName().toLowerCase())){
                            if(clancount < 4){
                                target = Server.getInstance().getPlayer(name);
                                if(target instanceof Player){
                                    for (Map.Entry<String,Player> ee : data2.get(number).entrySet()){
                                        if(ee.getValue().equals(target)){
                                            already.put(target, true);
                                            chunk.put(target, 0);
                                            clancount++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if((redcount + clancount) <= TEAM_PLAYERS){
                    redcount += clancount;
                    for (Map.Entry<Player, Integer> eee : chunk.entrySet()){
                        team.put(eee.getKey(), 0);
                    }
                    continue;
                }else if((bluecount + clancount) <= TEAM_PLAYERS){
                    bluecount += clancount;
                    for (Map.Entry<Player, Integer> eee : chunk.entrySet()){
                        team.put(eee.getKey(), 1);
                    }
                    continue;
                }else if((greencount + clancount) <= TEAM_PLAYERS){
                    greencount += clancount;
                    for (Map.Entry<Player, Integer> eee : chunk.entrySet()){
                        team.put(eee.getKey(), 2);
                    }
                    continue;
                }else if((yellowcount + clancount) <= TEAM_PLAYERS){
                    yellowcount += clancount;
                    for (Map.Entry<Player, Integer> eee : chunk.entrySet()){
                        team.put(eee.getKey(), 3);
                    }
                    continue;
                }else{
                    for (Map.Entry<Player, Integer> eee : chunk.entrySet()){
                        if(redcount < TEAM_PLAYERS){
                            redcount++;
                            team.put(eee.getKey(), 0);
                            continue;
                        }else if(bluecount < TEAM_PLAYERS){
                            bluecount++;
                            team.put(eee.getKey(), 1);
                            continue;
                        }else if(greencount < TEAM_PLAYERS){
                            greencount++;
                            team.put(eee.getKey(), 2);
                            continue;
                        }else if(yellowcount < TEAM_PLAYERS){
                            yellowcount++;
                            team.put(eee.getKey(), 3);
                            continue;
                        }
                    }
                }*/
            //}
            if(count == 0){
                redcount++;
                team.put(e.getValue(), 0);
                count++;
            }else if(count == 1){
                bluecount++;
                team.put(e.getValue(), 1);
                count++;
            }else if(count == 2){
                greencount++;
                team.put(e.getValue(), 2);
                count++;
            }else if(count == 3){
                yellowcount++;
                team.put(e.getValue(), 3);
                count = 0;
            }
        }
        for (Map.Entry<String,Player> e : Players.entrySet()){
            Map<UUID,Player> players = Server.getInstance().getOnlinePlayers();
            for (Map.Entry<UUID,Player> ee : players.entrySet()){
                e.getValue().showPlayer(ee.getValue());
            }
            killcount.put(e.getValue(),0);
            isRemoved.put(e.getValue(),false);
            wools.put(e.getValue(), 10);
            Main.setHide.put(e.getValue(),false);
            e.getValue().setGamemode(0);
            e.getValue().setFoodEnabled(false);
            e.getValue().getFoodData().setFoodLevel(20);
            Effect effect = Effect.getEffect(Effect.REGENERATION);
            effect.setAmplifier(0);
            effect.setDuration(99999);
            effect.setVisible(false);
            e.getValue().addEffect(effect);
            Vector3 pos = new Vector3();
            if(team.get(e.getValue()) == 0){
                pos = stage.getRedSpawn();
                sendTip(e.getValue(), Main.getMessage(e.getValue(), "miniwalls.you.are.red"), 5);
            }
            if(team.get(e.getValue()) == 1){
                pos = stage.getBlueSpawn();
                sendTip(e.getValue(), Main.getMessage(e.getValue(), "miniwalls.you.are.blue"), 5);
            }
            if(team.get(e.getValue()) == 2){
                pos = stage.getGreenSpawn();
                sendTip(e.getValue(), Main.getMessage(e.getValue(), "miniwalls.you.are.green"), 5);
            }
            if(team.get(e.getValue()) == 3){
                pos = stage.getYellowSpawn();
                sendTip(e.getValue(), Main.getMessage(e.getValue(), "miniwalls.you.are.yellow"), 5);
            }
            setNameTag(e.getValue());
            e.getValue().teleport(new Position(pos.x, pos.y, pos.z, world));
            if(job.get(e.getValue()) == 0) Inventorys.setData(e.getValue(),new MiniwallsSoldierInventory());
            else if(job.get(e.getValue()) == 1) Inventorys.setData(e.getValue(),new MiniwallsArcherInventory());
            else if(job.get(e.getValue()) == 2){
                Inventorys.setData(e.getValue(),new MiniwallsBuilderInventory());
                if(Stat.getMiniwallsBuilderLevel(e.getValue()) >= 3) wools.put(e.getValue(), 15);
                if(Stat.getMiniwallsBuilderLevel(e.getValue()) >= 3) wools.put(e.getValue(), 25);
            }
            MiniwallsInventory inv = (MiniwallsInventory) Inventorys.data2.get(e.getValue());
            arrow.put(e.getValue(), inv.StackMax);
            arrowcharge.put(e.getValue(), 0);
            int health = 20;
            health += inv.healthBonus;
            e.getValue().setMaxHealth(health);
            e.getValue().setHealth(health);
            Stat.setMiniwallsPlays(e.getValue(), Stat.getMiniwallsPlays(e.getValue()) + 1);
        }
        wither.put(0, new MiniwallsWitherManager(0, stage.getRedWitherSpawn(), this));
        wither.put(1, new MiniwallsWitherManager(1, stage.getBlueWitherSpawn(), this));
        wither.put(2, new MiniwallsWitherManager(2, stage.getGreenWitherSpawn(), this));
        wither.put(3, new MiniwallsWitherManager(3, stage.getYellowWitherSpawn(), this));
        wither.get(0).Start();
        wither.get(1).Start();
        wither.get(2).Start();
        wither.get(3).Start();
        for (Map.Entry<String,Player> e : Spectators.entrySet()){
            if(!Players.containsValue(e.getValue())){
                Main.setHide.put(e.getValue(),false);
                for (Map.Entry<String,Player> ee : Players.entrySet()){
                    ee.getValue().hidePlayer(e.getValue());
                }
                canpickup.put(e.getValue(),false);
                e.getValue().setGamemode(2);
                e.getValue().setAllowFlight(true);
                e.getValue().getInventory().clearAll();
                Inventorys.setData(e.getValue(),new MiniwallsDeathInventory());
                //e.getValue().teleport(new Position(pos[c].x,pos[c].y,pos[c].z,level));
            }
            Popups.setData(e.getValue(), new MiniwallsGamePopup(e.getValue()));
        }
        setGameDataAsBoolean("gamenow", true, false);
        setGameDataAsBoolean("gamenow2", true, true);
        setGameDataAsBoolean("wallnow", true, false);
        startWitherGauge();
        startBreakWallTime();
    }

    public void startDeathMatch(){
        setGameDataAsBoolean("deathmatchnow", true, false);
        for (Map.Entry<String,Player> e : AllPlayers.entrySet()){
            sendTip(e.getValue(), Main.getMessage(e.getValue(), "miniwalls.deathmatch"), 5);
        }
        wither.get(0).Stop();
        wither.get(1).Stop();
        wither.get(2).Stop();
        wither.get(3).Stop();
    }

    public void setNameTag(Player player){
        int color = team.get(player);
        String tag = "";
        if(color == 0) tag += "§c";
        if(color == 1) tag += "§b";
        if(color == 2) tag += "§a";
        if(color == 3) tag += "§e";
        tag += player.getName()+"\n";
        tag += "§f"+player.getHealth()+"§c❤";
        player.setNameTag(tag);
    }

    public boolean isWall(Vector3 v){
        HashMap<Vector3,Vector3> wall = stage.getWall();
        for (Map.Entry<Vector3,Vector3> e : wall.entrySet()){
            if(e.getKey().x <= v.x && e.getValue().x >= v.x &&
               e.getKey().y <= v.y && e.getValue().y >= v.y &&
               e.getKey().z <= v.z && e.getValue().z >= v.z){
                return true;
            }
        }
        return false;
    }

    public void BreakWall(){
        HashMap<Vector3,Vector3> wall = stage.getWall();
        int count = 0;
        for (Map.Entry<Vector3,Vector3> e : wall.entrySet()){
            Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackBreakWall(this, e), count);
            Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackBreakWall2(this, e), count + 4);
            count += 2;
        }
        for (Map.Entry<String,Player> e : AllPlayers.entrySet()){
            sendTip(e.getValue(), Main.getMessage(e.getValue(), "miniwalls.fall.wall"), 5);
        }
    }

    public void BreakWall2(Map.Entry<Vector3,Vector3> e){
        for(int x = (int)e.getKey().x;x <= e.getValue().x;x++){
            for(int y = (int)e.getKey().y;y <= e.getValue().y;y++){
                for(int z = (int)e.getKey().z;z <= e.getValue().z;z++){
                    world.setBlock(new Vector3(x,y,z), Block.get(0));
                }
            }
        }
    }

    public void BreakWall3(Map.Entry<Vector3,Vector3> e){
        for(int x = (int)e.getKey().x;x <= e.getValue().x;x++){
            for(int y = (int)e.getKey().y;y <= e.getValue().y;y++){
                for(int z = (int)e.getKey().z;z <= e.getValue().z;z++){
                    UpdateBlockPacket pk = new UpdateBlockPacket();
                    pk.x = x;
                    pk.y = y;
                    pk.z = z;
                    pk.blockId = 0;
                    pk.blockData = 0;
                    pk.flags = UpdateBlockPacket.FLAG_NONE;
                    for (Map.Entry<String,Player> ee : AllPlayers.entrySet()){
                        ee.getValue().dataPacket(pk);
                    }
                }
            }
        }
    }

    public void onplace(BlockPlaceEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Vector3 witherpos = stage.getRedWitherSpawn();
        Vector3 witherpos2 = stage.getBlueWitherSpawn();
        Vector3 witherpos3 = stage.getGreenWitherSpawn();
        Vector3 witherpos4 = stage.getYellowWitherSpawn();
        if(block.distance(witherpos) <= 4 || block.distance(witherpos2) <= 4 || block.distance(witherpos3) <= 4 || block.distance(witherpos4) <= 4){
            event.setCancelled();
        }else{
            if(block.getId() == Item.WOOL){
                wools.put(player,wools.get(player) - 1);
                event.setCancelled(false);
            }
        }
    }

    public void onbreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
        int id = block.getId();
        Vector3 witherpos = stage.getRedWitherSpawn();
        Vector3 witherpos2 = stage.getBlueWitherSpawn();
        Vector3 witherpos3 = stage.getGreenWitherSpawn();
        Vector3 witherpos4 = stage.getYellowWitherSpawn();
        if(block.distance(witherpos) <= 4 || block.distance(witherpos2) <= 4 || block.distance(witherpos3) <= 4 || block.distance(witherpos4) <= 4){
            event.setCancelled();
        }else{
            event.setCancelled(false);
            Item item = Item.get(Item.WOOL);
            event.setDrops(new Item[]{item});
            if(id == Item.WOOL){
                if(isWall(block)){
                    event.setCancelled();
                }
            }
        }
    }

    public void ArrowHit(ProjectileHitEvent event){
        MovingObjectPosition pos = event.getMovingObjectPosition();
        Entity entity = event.getEntity();
        EntityArrow arrow = null;
        if(entity instanceof EntityArrow){
            arrow = (EntityArrow) entity;
        }else return;
        Vector3 vec = pos.hitVector;
        if(!(arrow.shootingEntity instanceof Player)) return;
        Player player = (Player) arrow.shootingEntity;
        if(!Players.containsValue(player)) return;
        Block block = Block.get(0);

        Vector3 startVec = vec.add(-1, 1, 1);
        Vector3 endVec = vec.add(1, -1, -1);

        int startX = Math.min(startVec.getFloorX(), endVec.getFloorX());
        int startY = Math.min(startVec.getFloorY(), endVec.getFloorY());
        int startZ = Math.min(startVec.getFloorZ(), endVec.getFloorZ());

        int endX = Math.max(startVec.getFloorX(), endVec.getFloorX());
        int endY = Math.max(startVec.getFloorY(), endVec.getFloorY());
        int endZ = Math.max(startVec.getFloorZ(), endVec.getFloorZ());
        for(int x = startX; x <= endX; ++x){
            for(int y = startY; y <= endY; ++y){
                for(int z = startZ; z <= endZ; ++z){
                    Vector3 v = new Vector3(x, y, z);
                    if(!(arrow.getLevel().getBlock(v).getId() == 35) || isWall(v)) continue;
                    arrow.getLevel().addParticle(new DestroyBlockParticle(v, arrow.getLevel().getBlock(v)));
                    arrow.level.setBlock(v, block);
                }
            }
        }
    }

    public void setJob(Player player, int j){
        job.put(player, j);
        if(j == 0) player.sendMessage(Main.getMessage(player, "miniwalls.select.soldier"));
        else if(j == 1) player.sendMessage(Main.getMessage(player, "miniwalls.select.archer"));
        else if(j == 2) player.sendMessage(Main.getMessage(player, "miniwalls.select.builder"));
    }

    public void kill(Player player, Player death){
        String color = "";
        if(team.get(player) == 0) color = "§c";
        if(team.get(player) == 1) color = "§b";
        if(team.get(player) == 2) color = "§a";
        if(team.get(player) == 3) color = "§e";
        String color2 = "";
        if(team.get(death) == 0) color2 = "§c";
        if(team.get(death) == 1) color2 = "§b";
        if(team.get(death) == 2) color2 = "§a";
        if(team.get(death) == 3) color2 = "§e";
        killcount.put(player,killcount.get(player)+1);
        Stat.setMiniwallsKills(player, Stat.getMiniwallsKills(player) + 1);
        player.heal(6);
        for (Map.Entry<String,Player> e : AllPlayers.entrySet()){
            if(death.equals(e.getValue())) e.getValue().sendMessage(Main.getMessage(e.getValue(), "miniwalls.you.were.killed.by", new String[]{color+player.getName()}));
            else if(player.equals(e.getValue())) e.getValue().sendMessage(Main.getMessage(e.getValue(), "miniwalls.you.killed", new String[]{color2+death.getName()}));
            else e.getValue().sendMessage(Main.getMessage(e.getValue(), "miniwalls.who.killed.by", new String[]{color2+death.getName(),color+player.getName()}));
        }
    }

    public void kill(String player, Player death){
        String color = "";
        if(team.get(death) == 0) color = "§c";
        if(team.get(death) == 1) color = "§b";
        if(team.get(death) == 2) color = "§a";
        if(team.get(death) == 3) color = "§e";
        for (Map.Entry<String,Player> e : AllPlayers.entrySet()){
            if(death.equals(e.getValue())) e.getValue().sendMessage(Main.getMessage(e.getValue(), "miniwalls.you.were.killed.by", new String[]{player}));
            else e.getValue().sendMessage(Main.getMessage(e.getValue(), "miniwalls.who.killed.by", new String[]{color+death.getName(),player}));
        }
    }

    public void death(Player player){
        if(Players.containsValue(player)){
            Stat.setMiniwallsDeaths(player, Stat.getMiniwallsDeaths(player) + 1);
            PlayerInventory inventory = player.getInventory();
            AddPlayerPacket pk = new AddPlayerPacket();
            long eid = Entity.entityCount++;
            pk.entityRuntimeId = eid;
            pk.entityUniqueId = eid;
            pk.uuid = UUID.randomUUID();
            pk.username = player.getName();
            pk.x = (float)player.x;
            pk.y = (float)player.y;
            pk.z = (float)player.z;
            pk.yaw = (float)player.yaw;
            pk.pitch = (float)player.pitch;
            pk.item = inventory.getItemInHand();
            int flags = 0;
            flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
            pk.metadata = new EntityMetadata()
                    .putLong(Entity.DATA_FLAGS,flags)
                    .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1);
            PlayerListPacket pk2 = new PlayerListPacket();
            pk2.type = PlayerListPacket.TYPE_ADD;
            pk2.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(pk.uuid, eid, player.getName(),player.getSkin())};
            PlayerListPacket pk3 = new PlayerListPacket();
            pk3.type = PlayerListPacket.TYPE_REMOVE;
            pk3.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(pk.uuid)};
            MobArmorEquipmentPacket pk4 = new MobArmorEquipmentPacket();
            pk4.eid = eid;
            pk4.slots = new Item[]{inventory.getHelmet(),inventory.getChestplate(),inventory.getLeggings(),inventory.getBoots()};
            EntityEventPacket pk5 = new EntityEventPacket();
            pk5.eid = eid;
            pk5.event = EntityEventPacket.DEATH_ANIMATION;
            for (Map.Entry<String,Player> e : AllPlayers.entrySet()){
                e.getValue().dataPacket(pk);
                e.getValue().dataPacket(pk2);
                e.getValue().dataPacket(pk3);
                e.getValue().dataPacket(pk4);
                e.getValue().dataPacket(pk5);
            }
            Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackRemoveDeath(this, eid),20);
            if(wither.get(team.get(player)).isDeathed || getGameDataAsBoolean("deathmatchnow")){
                removePlayerFromGame(player);
            }else{
                Inventorys.setData(player,new MiniwallsRespawnInventory());
                player.setGamemode(3);
                respawnPlayerCount(player, 6);
            }
        }
    }

    public void RemoveDeath(long eid){
        RemoveEntityPacket pk = new RemoveEntityPacket();
        pk.eid = eid;
        for (Map.Entry<String,Player> e : AllPlayers.entrySet()){
            e.getValue().dataPacket(pk);
        }
    }

    public void WitherDeath(int teamnumber, Player player){
        String tag = "";
        if(teamnumber == 0) tag = "§cRed Mini Wither";
        if(teamnumber == 1) tag = "§bBlue Mini Wither";
        if(teamnumber == 2) tag = "§aGreen Mini Wither";
        if(teamnumber == 3) tag = "§eYellow Mini Wither";
        String color = "";
        if(team.get(player) == 0) color = "§c";
        if(team.get(player) == 1) color = "§b";
        if(team.get(player) == 2) color = "§a";
        if(team.get(player) == 3) color = "§e";
        for (Map.Entry<String,Player> e : AllPlayers.entrySet()){
            if(teamnumber == team.get(e.getValue())){
                sendTip(e.getValue(), Main.getMessage(e.getValue(), "miniwalls.your.miniwither.dead"), 5);
            }else{
                sendTip(e.getValue(), Main.getMessage(e.getValue(), "miniwalls.miniwither.dead", new String[]{tag}), 5);
            }
            e.getValue().sendMessage(Main.getMessage(e.getValue(), "miniwalls.miniwither.killed", new String[]{color+player.getName(),tag}));
        }
    }

    public void WitherLowHP(int teamnumber){
        String msg = "";
        msg = "§cYour wither is at low HP!";
        for (Map.Entry<String,Player> e : Players.entrySet()){
            if(team.get(e.getValue()) == teamnumber){
                sendTip(e.getValue(), msg, 5);
            }
        }
    }

    public void respawnPlayerCount(Player player,int count){
        if(!Players.containsValue(player) || !getGameDataAsBoolean("gamenow")) return;
        if(count <= 0){
            respawnPlayerLater(player);
            return;
        }
        sendTip(player, Main.getMessage(player, "miniwalls.dead.respawning", new String[]{String.valueOf(count)}), 1);
        count--;
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackRespawn(this, player, count),20);
    }

    public void respawnPlayerLater(Player player){
        if(!Players.containsValue(player)) return;
        Vector3 pos = new Vector3();
        if(team.get(player) == 0) pos = stage.getRedSpawn();
        if(team.get(player) == 1) pos = stage.getBlueSpawn();
        if(team.get(player) == 2) pos = stage.getGreenSpawn();
        if(team.get(player) == 3) pos = stage.getYellowSpawn();
        player.teleport(player.getLevel().getSafeSpawn(pos));
        if(job.get(player) == 0) Inventorys.setData(player,new MiniwallsSoldierInventory());
        else if(job.get(player) == 1) Inventorys.setData(player,new MiniwallsArcherInventory());
        else if(job.get(player) == 2) Inventorys.setData(player,new MiniwallsBuilderInventory());
        int health = 20;
        if(Inventorys.data2.get(player) instanceof MiniwallsInventory){
            MiniwallsInventory inv = (MiniwallsInventory) Inventorys.data2.get(player);
            arrow.put(player, inv.StackMax);
            health += inv.healthBonus;
        }
        player.setMaxHealth(health);
        player.setHealth(health);
        player.removeAllEffects();
        player.setOnFire(0);
        player.setGamemode(0);
        Effect effect = Effect.getEffect(Effect.REGENERATION);
        effect.setAmplifier(0);
        effect.setDuration(99999);
        effect.setVisible(false);
        player.addEffect(effect);
        setNameTag(player);
    }

    public void removePlayerFromGame(Player player){
        Inventorys.setData(player,new MiniwallsDeathInventory());
        player.setGamemode(3);
        isRemoved.put(player, true);
        sendTip(player, Main.getMessage(player, "miniwalls.dead.cant.respawn"), 1);
        checkMember();
    }

    public void Attack(Player player,long eid){
        if(player.getGamemode() == 3) return;
        int damage = 0;
        damage = player.getInventory().getItemInHand().getAttackDamage();
        if(wither.containsKey(0) && wither.get(0).EntityId == eid && team.get(player) != 0) wither.get(0).Damage(damage,player);
        if(wither.containsKey(1) && wither.get(1).EntityId == eid && team.get(player) != 1) wither.get(1).Damage(damage,player);
        if(wither.containsKey(2) && wither.get(2).EntityId == eid && team.get(player) != 2) wither.get(2).Damage(damage,player);
        if(wither.containsKey(3) && wither.get(3).EntityId == eid && team.get(player) != 3) wither.get(3).Damage(damage,player);
    }

    public void checkMember(){
        int winteam = Integer.MAX_VALUE;
        int r = 0;
        int b = 0;
        int g = 0;
        int y = 0;
        boolean isFinished = true;
        for (Map.Entry<String,Player> e : Players.entrySet()){
            if(!isRemoved.get(e.getValue())){
                if(team.get(e.getValue()) == 0) r++;
                if(team.get(e.getValue()) == 1) b++;
                if(team.get(e.getValue()) == 2) g++;
                if(team.get(e.getValue()) == 3) y++;
                if(winteam == Integer.MAX_VALUE){
                    winteam = team.get(e.getValue());
                }else{
                    if(winteam != team.get(e.getValue())){
                        isFinished = false;
                    }
                }
            }
        }
        if(r <= 0 && wither.get(0).HitPoint > 0) wither.get(0).Stop();
        if(b <= 0 && wither.get(1).HitPoint > 0) wither.get(1).Stop();
        if(g <= 0 && wither.get(2).HitPoint > 0) wither.get(2).Stop();
        if(y <= 0 && wither.get(3).HitPoint > 0) wither.get(3).Stop();
        if(isFinished) finish(winteam);
    }

    public void finish(int winteam){
        Entity[] items = world.getEntities();
            for(Entity entity: items){
                if(entity instanceof EntityItem){
                    entity.kill();
                }
            }
        HashMap<Player,Integer> map = new HashMap<Player,Integer>();
            for(Map.Entry<String,Player> eee : Players.entrySet()){
                map.put(eee.getValue(),killcount.get(eee.getValue()));
            }
        List<Map.Entry<Player,Integer>> sort = Sort(map);
        String winname = "";
        if(winteam == 0) winname = "§cRed";
        if(winteam == 1) winname = "§bBlue";
        if(winteam == 2) winname = "§aGreen";
        if(winteam == 3) winname = "§eYellow";
        for (Map.Entry<String,Player> e : Players.entrySet()){
            e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line"));
            e.getValue().sendMessage(Main.getMessage(e.getValue(),"miniwalls.result.win",new String[]{winname}));
            e.getValue().sendMessage(Main.getMessage(e.getValue(),"miniwalls.result.stage",new String[]{stage.getName()}));
            if(sort.size() >= 1) e.getValue().sendMessage(Main.getMessage(e.getValue(),"miniwalls.result.win1",new String[]{sort.get(0).getKey().getName(),String.valueOf(sort.get(0).getValue())}));
            if(sort.size() >= 2) e.getValue().sendMessage(Main.getMessage(e.getValue(),"miniwalls.result.win2",new String[]{sort.get(1).getKey().getName(),String.valueOf(sort.get(1).getValue())}));
            if(sort.size() >= 3) e.getValue().sendMessage(Main.getMessage(e.getValue(),"miniwalls.result.win3",new String[]{sort.get(2).getKey().getName(),String.valueOf(sort.get(2).getValue())}));
            e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line2"));
            e.getValue().removeAllEffects();
            e.getValue().setMaxHealth(20);
            e.getValue().setHealth(20);
            e.getValue().setOnFire(0);
            e.getValue().setFoodEnabled(false);
            e.getValue().getFoodData().setFoodLevel(20);
            if(team.get(e.getValue()) == winteam){
                firework(e.getValue());
                sendTip(e.getValue(), Main.getMessage(e.getValue(), "miniwalls.win"), 5);
            }else{
                sendTip(e.getValue(), Main.getMessage(e.getValue(), "miniwalls.lose"), 5);
            }
        }
        canOpenChest = false;
        closeChests();
        setGameDataAsBoolean("gamenow2", false, true);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackFinish(this, winteam),200);
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
            for (Map.Entry<String,Player> e : Players.entrySet()){
                if(e.getValue() instanceof Player){
                    Vector3 winner = new Vector3(player.x,player.y+1,player.z);
                    Firework.start(winner,e.getValue());
                }
            }
        }
    }

    public void finishLater(int winteam){
        wither.get(0).Stop();
        wither.get(1).Stop();
        wither.get(2).Stop();
        wither.get(3).Stop();
        for (Map.Entry<String,Player> e : Players.entrySet()){
            if(e.getValue() instanceof Player && Players.containsValue(e.getValue())){
                if(winteam == team.get(e.getValue())){
                    int coin = (int)(Math.random() * 30)+10;
                    int exp = (int)(Math.random() * 30)+10;
                    Stat.addCoin(e.getValue(), coin);
                    Stat.addExp(e.getValue(),exp);
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line"));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"miniwalls.result"));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.coin",new String[]{String.valueOf(coin)}));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.exp",new String[]{String.valueOf(exp)}));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line2"));
                    Stat.setMiniwallsWin(e.getValue(),Stat.getMiniwallsWin(e.getValue())+1);
                }else{
                    int coin = (int)(Math.random() * 5)+1;
                    int exp = (int)(Math.random() * 5)+1;
                    Stat.addCoin(e.getValue(), coin);
                    Stat.addExp(e.getValue(),exp);
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line"));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"miniwalls.result"));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.coin",new String[]{String.valueOf(coin)}));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.exp",new String[]{String.valueOf(exp)}));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line2"));
                    Stat.setMiniwallsLose(e.getValue(),Stat.getMiniwallsLose(e.getValue())+1);
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
            e.getValue().removeAllEffects();
            Stat.setNameTag(e.getValue());
            team.remove(e.getValue());
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
                e.getValue().teleport(new Position(2.5,7,-39.5,Server.getInstance().getLevelByName("walls")));
                Inventorys.setData(e.getValue(),new MiniwallsLobbyInventory());
                Popups.setData(e.getValue(), new WallsPopup(e.getValue()));
            }else{
                 e.getValue().teleport(Server.getInstance().getLevelByName("home"+Home.getId(number)).getSafeSpawn());
                 Inventorys.setData(e.getValue(),new HomeInventory());
            }
            Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackAllow(this, e.getValue()),1);
            Main.gamenow.put(e.getValue(), false);
        }
        reset();
        if(Server.getInstance().isLevelLoaded("miniwalls"+number)) Server.getInstance().unloadLevel(Server.getInstance().getLevelByName("miniwalls"+number));
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackWorld(this),40);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackNow(this),100);
        if(number >= 8) Home.finish(number);
    }

    public void allow(Player player){
        player.setAllowFlight(false);
    }

    public void world(){
        String url = Server.getInstance().getDataPath()+"/worlds/miniwalls";
        directoryCopy(new File(url),new File(url+number));
    }

    public void now(){
        setGameDataAsBoolean("gamenow2", false, true);
    }

    public void Pickup(InventoryPickupItemEvent event){
        Item item = event.getItem().getItem();
        int id = item.getId();
            if(event.getInventory().getHolder() instanceof Player && Players.containsValue(event.getInventory().getHolder())){
                Player player = (Player)event.getInventory().getHolder();
                if(id == Item.WOOL){
                    event.setCancelled();
                    TakeItemEntityPacket pk = new TakeItemEntityPacket();
                    pk.entityId = player.getId();
                    pk.target = event.getItem().getId();
                    Server.broadcastPacket(event.getItem().getViewers().values(), pk);
                    pk = new TakeItemEntityPacket();
                    pk.entityId = 0;
                    pk.target = event.getItem().getId();
                    player.dataPacket(pk);
                    event.getItem().kill();
                    Item item2;
                    item2 = Item.get(Item.WOOL);
                    int color = team.get(player);
                    if(color == 0) item2.setDamage(14);
                    else if(color == 1) item2.setDamage(11);
                    else if(color == 2) item2.setDamage(13);
                    else if(color == 3) item2.setDamage(4);
                    player.getInventory().addItem(item2);
                    wools.put(player, wools.get(player) + 1);
                    return;
                }
            }
    }

    public void chat(PlayerChatEvent event){
        Player player = event.getPlayer();
        if(Players.containsValue(player.getName())){
            event.setCancelled();
            if(Main.Chat.get(player.getName()) <= 0){
                player.sendMessage(Main.getMessage(player,"chat.wait"));
                return;
            }
            for (Map.Entry<String,Player> e : Players.entrySet()){
                if(getGameDataAsBoolean("gamenow2")){
                    String color = "";
                    if(team.get(player) == 0) color = "§c";
                    if(team.get(player) == 1) color = "§b";
                    if(team.get(player) == 2) color = "§a";
                    if(team.get(player) == 3) color = "§e";
                    if(player.getGamemode() == 0 && !Setting.getChatHide(e.getValue())){
                        e.getValue().sendMessage("<"+color+player.getName()+"§f> "+event.getMessage());
                    }else if(player.getGamemode() == 3 && !Setting.getChatHide(e.getValue())){
                        if(e.getValue().getGamemode() == 3){
                            e.getValue().sendMessage("<"+player.getName()+"> §8"+event.getMessage());
                        }
                    }
                }else{
                    e.getValue().sendMessage("<"+player.getDisplayName()+"§f> "+event.getMessage());
                }
            }
            int ch = Main.Chat.get(player.getName());
            Main.Chat.put(player.getName(),ch-1);
            Server.getInstance().getLogger().info("<"+player.getDisplayName()+"§f> "+event.getMessage());
        }
    }

    public void Arrow(){
        for (Map.Entry<String, Player> e : Players.entrySet()){
            if(getGameDataAsBoolean("gamenow")){
                setNameTag(e.getValue());
                if(Inventorys.data2.get(e.getValue()) instanceof MiniwallsInventory){
                    MiniwallsInventory inv = (MiniwallsInventory) Inventorys.data2.get(e.getValue());
                    if(inv.StackMax > arrow.get(e.getValue())){
                        arrowcharge.put(e.getValue(), arrowcharge.get(e.getValue()) + inv.ChargeSpeed);
                        if(arrowcharge.get(e.getValue()) >= inv.ChargeMax){
                            e.getValue().setAttribute(Attribute.getAttribute(Attribute.EXPERIENCE).setValue(1));
                            e.getValue().getInventory().addItem(Item.get(Item.ARROW));
                            arrow.put(e.getValue(), arrow.get(e.getValue()) + 1);
                            arrowcharge.put(e.getValue(), 0);
                        }else{
                            float exp = ((float)arrowcharge.get(e.getValue()) / (float)inv.ChargeMax);
                            e.getValue().setAttribute(Attribute.getAttribute(Attribute.EXPERIENCE).setValue(exp));
                        }
                    }else{
                        e.getValue().setAttribute(Attribute.getAttribute(Attribute.EXPERIENCE).setValue(0));
                    }
                }else{
                    e.getValue().setAttribute(Attribute.getAttribute(Attribute.EXPERIENCE).setValue(0));
                }
            }
        }
    }

    public int[] getMember(){
        int red = 0;
        int blue = 0;
        int green = 0;
        int yellow = 0;
        for (Map.Entry<String,Player> e : Players.entrySet()){
            if(!isRemoved.get(e.getValue())){
                if(team.get(e.getValue()) == 0){
                    red++;
                }
                if(team.get(e.getValue()) == 1){
                    blue++;
                }
                if(team.get(e.getValue()) == 2){
                    green++;
                }
                if(team.get(e.getValue()) == 3){
                    yellow++;
                }
            }
        }
        return new int[]{red,blue,green,yellow};
    }

    public List Sort(Map<Player,Integer> map){
        Map<Player, Integer> hashMap = new HashMap<Player, Integer>();
        for (Map.Entry<Player,Integer> e : map.entrySet()){
            hashMap.put(e.getKey(),e.getValue());
        }
        List<Map.Entry<Player,Integer>> entries =
              new ArrayList<Map.Entry<Player,Integer>>(hashMap.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<Player,Integer>>() {

            @Override
            public int compare(
                  Entry<Player,Integer> entry1, Entry<Player,Integer> entry2) {
                return ((Integer)entry2.getValue()).compareTo((Integer)entry1.getValue());
            }
        });
        return entries;
    }

    public HashMap<String,Integer> data3 = new HashMap<String,Integer>();
    public MiniwallsStage stage;
    public HashMap<Integer,Integer> countplayer = new HashMap<Integer,Integer>();
    public HashMap<Player,Boolean> canpickup = new HashMap<Player,Boolean>();
    public HashMap<Player,Integer> killcount = new HashMap<Player,Integer>();
    public HashMap<Player,Integer> wools = new HashMap<Player,Integer>();
    public HashMap<Player,Integer> team = new HashMap<Player,Integer>();//0: Red  1: Blue  2: Green  3: Yellow
    public HashMap<Player,Integer> job = new HashMap<Player,Integer>();//0: Fencer(剣)  1: Archer(弓)  2: Builder(ピッケル)
    public HashMap<Integer,MiniwallsWitherManager> wither = new HashMap<Integer,MiniwallsWitherManager>();
    public HashMap<Player,Boolean> isRemoved = new HashMap<Player,Boolean>();
    public HashMap<Player,Integer> arrow = new HashMap<Player,Integer>();
    public HashMap<Player,Integer> arrowcharge = new HashMap<Player,Integer>();
    public MiniwallsStage[] stagelist;
}
class CallbackTime extends Task{

    public Miniwalls owner;

    public CallbackTime(Miniwalls owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.Time();
    }
}
class CallbackDeathMatchTime extends Task{

    public Miniwalls owner;

    public CallbackDeathMatchTime(Miniwalls owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.DeathMatchTime();
    }
}
class CallbackWitherGauge extends Task{

    public Miniwalls owner;
    public int count;

    public CallbackWitherGauge(Miniwalls owner, int count){
        this.owner = owner;
        this.count = count;
    }

    public void onRun(int d){
        this.owner.WitherGauge(this.count);
    }
}
class CallbackFinish extends Task{

    public Miniwalls owner;
    public int winteam;

    public CallbackFinish(Miniwalls owner,int winteam){
        this.owner = owner;
        this.winteam = winteam;
    }

    public void onRun(int d){
        this.owner.finishLater(this.winteam);
    }
}
class CallbackWorld extends Task{

    public Miniwalls owner;

    public CallbackWorld(Miniwalls owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.world();
    }
}
class CallbackAllow extends Task{

    public Miniwalls owner;
    public Player player;

    public CallbackAllow(Miniwalls owner, Player player){
        this.owner = owner;
        this.player = player;
    }

    public void onRun(int d){
        this.owner.allow(this.player);
    }
}
class CallbackNow extends Task{

    public Miniwalls owner;

    public CallbackNow(Miniwalls owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.now();
    }
}
class CallbackFirework extends Task{

    public Miniwalls owner;
    public Player player;

    public CallbackFirework(Miniwalls owner, Player player){
        this.owner = owner;
        this.player = player;
    }

    public void onRun(int d){
        this.owner.fireworkRun(player);
    }
}
class CallbackRespawn extends Task{

    public Miniwalls owner;
    public Player player;
    public int count;

    public CallbackRespawn(Miniwalls owner, Player player,int count){
        this.owner = owner;
        this.player = player;
        this.count = count;
    }

    public void onRun(int d){
        this.owner.respawnPlayerCount(this.player,this.count);
    }
}
class CallbackBreakWallTime extends Task{

    public Miniwalls owner;

    public CallbackBreakWallTime(Miniwalls owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.BreakWallTime();
    }
}
class CallbackArrow extends Task{

    public Miniwalls owner;

    public CallbackArrow(Miniwalls owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.Arrow();
    }
}
class CallbackBreakWall extends Task{

    public Miniwalls owner;
    public Map.Entry<Vector3,Vector3> e;

    public CallbackBreakWall(Miniwalls owner, Map.Entry<Vector3,Vector3> e){
        this.owner = owner;
        this.e = e;
    }

    public void onRun(int d){
        this.owner.BreakWall2(this.e);
    }
}
class CallbackBreakWall2 extends Task{

    public Miniwalls owner;
    public Map.Entry<Vector3,Vector3> e;

    public CallbackBreakWall2(Miniwalls owner, Map.Entry<Vector3,Vector3> e){
        this.owner = owner;
        this.e = e;
    }

    public void onRun(int d){
        this.owner.BreakWall3(this.e);
    }
}
class CallbackRemoveDeath extends Task{

    public Miniwalls owner;
    public long eid;

    public CallbackRemoveDeath(Miniwalls owner, long eid){
        this.owner = owner;
        this.eid = eid;
    }

    public void onRun(int d){
        this.owner.RemoveDeath(this.eid);
    }
}
