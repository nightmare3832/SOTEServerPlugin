package sote.buildbattle;

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
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.ContainerSetContentPacket;
import cn.nukkit.scheduler.Task;
import sote.Game;
import sote.GameProvider;
import sote.Main;
import sote.home.Home;
import sote.inventory.Inventorys;
import sote.inventory.buildbattle.BuildbattleInventory;
import sote.inventory.buildbattle.BuildbattleLobbyInventory;
import sote.inventory.buildbattle.BuildbattleMarkInventory;
import sote.inventory.buildbattle.BuildbattleWaitInventory;
import sote.inventory.home.HomeInventory;
import sote.particle.Firework;
import sote.party.Party;
import sote.setting.Setting;
import sote.stat.Stat;

public class Buildbattle extends Game{

    public static final int MAX_PLAYERS = 9;
    public static final int MIN_PLAYERS = 2;

    public static final int WAIT_TIME = 60;
    public static final int WAIT_HOME_TIME = 30;
    public static final int GAME_TIME = 300;

    public Buildbattle(int number, boolean isHome){
        super(number, isHome);
        new BuildbattleSignManager();
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
        Players = new HashMap<String,Player>();
        Spectators = new HashMap<String,Player>();
        AllPlayers = new HashMap<String,Player>();
        //BuildbattleSignManager.updataSign(number);
        String url = Server.getInstance().getDataPath()+"/worlds/buildbattle";
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
        GameData.put("time","0");
        GameData.put("gametime","0");
        GameData.put("timenow","0");
        GameData.put("gamenow","0");
        GameData.put("gamenow2","0");
        Players = new HashMap<String,Player>();
        Spectators = new HashMap<String,Player>();
        AllPlayers = new HashMap<String,Player>();
        BuildbattleSignManager.updataSign(number);
    }

    public Boolean directoryCopy(File dirFrom, File dirTo){
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

     public Boolean fileCopy(File file, File dir){
        String f = dir.getPath() + "/" + file.getName();
        String ff = f.replace("buildbattle/","");
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
        Join(player,false);
    }

    public void Join(Player player, Boolean party){
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
        setGameDataAsInt("count", getGameDataAsInt("count") + 1, true);
        if((getGameDataAsInt("count") >= MIN_PLAYERS || number >= 8) && !getGameDataAsBoolean("timenow")){
            setGameDataAsBoolean("timenow", true, true);
            startTime();
        }
        Players.put(player.getName(),player);
        AllPlayers.put(player.getName(),player);
        Inventorys.setData(player,new BuildbattleWaitInventory());
        if(number <= 7) Teleport(player);
        player.setAllowFlight(false);
        Main.gamenow.put(player, true);
    }

    @Override
    public void LookJoin(Player player){
        Spectators.put(player.getName(),player);
        AllPlayers.put(player.getName(),player);
        Inventorys.setData(player,new BuildbattleWaitInventory());
        if(number <= 7) Teleport(player);
        Main.gamenow.put(player, true);
    }

    @Override
    public void Quit(Player player){
        if(!AllPlayers.containsValue(player)) return;
        if(!Players.containsValue(player)) LookQuit(player);
        setGameDataAsInt("count", getGameDataAsInt("count") - 1, true);
        if(number <= 7){
            player.teleport(new Position(88.5,16,85,Server.getInstance().getLevelByName("buildbattle")));
            Inventorys.setData(player,new BuildbattleLobbyInventory());
        }else{
             player.teleport(Server.getInstance().getLevelByName("home"+Home.getId(number)).getSafeSpawn());
             Inventorys.setData(player,new HomeInventory());
        }
        player.setGamemode(2);
        Players.remove(player.getName());
        AllPlayers.remove(player.getName());
        player.setAllowFlight(false);
        Main.gamenow.put(player, false);
        if(getGameDataAsInt("count") <= 1){
            for (Map.Entry<String,Player> e : AllPlayers.entrySet()){
                Quit(e.getValue());
            }
            reset();
        }
        GameProvider.quitGame(player);
    }

    @Override
    public void LookQuit(Player player){
        if(!AllPlayers.containsKey(player)) return;
        if(number <= 7){
            player.teleport(new Position(88.5,16,85,Server.getInstance().getLevelByName("buildbattle")));
            Inventorys.setData(player,new BuildbattleLobbyInventory());
        }else{
             player.teleport(Server.getInstance().getLevelByName("home"+Home.getId(number)).getSafeSpawn());
             Inventorys.setData(player,new HomeInventory());
        }
        Spectators.remove(player.getName());
        AllPlayers.remove(player.getName());
        Main.gamenow.put(player, false);
    }

    public void Teleport(Player player){
        if(!Server.getInstance().isLevelLoaded("buildbattle"+number)){
            world();
            Server.getInstance().loadLevel("buildbattle"+number);
        }
        Level level = Server.getInstance().getLevelByName("buildbattle"+number);
        player.teleport(new Position(336,5,-350,level));
    }

    public void startTime(){
        if(number <= 7){
            setGameDataAsInt("time", WAIT_TIME, true);
        }else{
            setGameDataAsInt("time", WAIT_HOME_TIME, true);
        }
        for (Map.Entry<String,Player> e : AllPlayers.entrySet()){
            e.getValue().sendPopup(Main.getMessage(e.getValue(),"buildbattle.wait.count",new String[]{String.valueOf(getGameDataAsInt("time"))}));
        }
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackTime(this),20);
    }

    public void Time(){
        if(getGameDataAsInt("time") <= 0){
            Start();
            return;
        }
        setGameDataAsInt("time", getGameDataAsInt("time") - 1, true);
        for (Map.Entry<String,Player> e : AllPlayers.entrySet()){
            e.getValue().sendPopup(Main.getMessage(e.getValue(),"buildbattle.wait.count",new String[]{String.valueOf(getGameDataAsInt("time"))}));
        }
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackTime(this),20);
    }

    public void startGameTime(){
        setGameDataAsInt("gametime", GAME_TIME, false);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackGameTime(this),20);
    }

    public void GameTime(){
        if(!getGameDataAsBoolean("gamenow")) return;
        if(getGameDataAsInt("gametime") <= 0){
            startMark();
            return;
        }
        setGameDataAsInt("gametime", getGameDataAsInt("gametime") - 1, false);
        for (Map.Entry<String,Player> e : AllPlayers.entrySet()){
            e.getValue().setAttribute(Attribute.getAttribute(Attribute.EXPERIENCE_LEVEL).setValue(getGameDataAsInt("gametime")));
        }
        if(getGameDataAsInt("gametime") % 3 == 0){
            for (Map.Entry<String,Player> e : AllPlayers.entrySet()){
                e.getValue().sendTip(Main.getMessage(e.getValue(),"buildbattle.title.tip",new String[]{Main.getMessage(e.getValue(),"buildbattle.title").split(",")[data10.get(number)]}));
            }
        }
        if(getGameDataAsInt("gametime") % 60 == 0){
            for (Map.Entry<String,Player> e : AllPlayers.entrySet()){
                e.getValue().sendMessage(Main.getMessage(e.getValue(),"buildbattle.game.count",new String[]{String.valueOf(getGameDataAsInt("gametime"))}));
            }
        }
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackGameTime(this),20);
    }

    public void Start(){
        if(getGameDataAsInt("count") < MIN_PLAYERS){
            setGameDataAsBoolean("timenow", false, true);
            if(number >= 8) Home.notStart(number);
            return;
        }
        int c = 0;
        world();
        Server.getInstance().loadLevel("buildbattle"+number);
        world = Server.getInstance().getLevelByName("buildbattle"+number);
        Vector3[] pos = getSpawn();
        String title = Main.getMessage("jp","buildbattle.title");
        int t = (int)(Math.random()*(title.split(",").length-1));
        data10.put(number,t);
        for (Map.Entry<String,Player> e : Players.entrySet()){
            Map<UUID,Player> players = Server.getInstance().getOnlinePlayers();
            for (Map.Entry<UUID,Player> ee : players.entrySet()){
                e.getValue().showPlayer(ee.getValue());
            }
            Main.setHide.put(e.getValue(),false);
            e.getValue().setGamemode(1);
            e.getValue().setHealth(20);
            ContainerSetContentPacket containerSetContentPacket = new ContainerSetContentPacket();
            containerSetContentPacket.windowid = ContainerSetContentPacket.SPECIAL_CREATIVE;
            containerSetContentPacket.slots = getItems();
            e.getValue().dataPacket(containerSetContentPacket);
            e.getValue().teleport(new Position(pos[c].x, pos[c].y, pos[c].z, world));
            SpawnPoint.put(e.getValue(), pos[c]);
            Inventorys.setData(e.getValue(),new BuildbattleInventory());
            Stat.setBuildbattlePlays(e.getValue(), Stat.getBuildbattlePlays(e.getValue()) + 1);
            c++;
        }
        c = 0;
        for (Map.Entry<String,Player> e : AllPlayers.entrySet()){
            if(!Players.containsValue(e.getValue())){
                Main.setHide.put(e.getValue(),false);
                for (Map.Entry<String,Player> ee : Players.entrySet()){
                    ee.getValue().hidePlayer(e.getValue());
                }
                e.getValue().setGamemode(2);
                e.getValue().setAllowFlight(true);
                e.getValue().getInventory().clearAll();
                Inventorys.setData(e.getValue(),new BuildbattleInventory());
                e.getValue().teleport(new Position(pos[c].x, pos[c].y, pos[c].z, world));
                c++;
            }
            e.getValue().sendTip(Main.getMessage(e.getValue(),"buildbattle.title.tip",new String[]{Main.getMessage(e.getValue(),"buildbattle.title").split(",")[data10.get(number)]}));
        }
        setGameDataAsBoolean("gamenow", true, false);
        setGameDataAsBoolean("gamenow2", true, true);
        startGameTime();
    }

    public static Vector3[] getSpawn(){
        return new Vector3[]{
            new Vector3(300.5,32,350.5),
            new Vector3(346.5,32,350.5),
            new Vector3(392.5,32,350.5),
            new Vector3(300.5,32,396.5),
            new Vector3(346.5,32,396.5),
            new Vector3(392.5,32,396.5),
            new Vector3(300.5,32,442.5),
            new Vector3(346.5,32,442.5),
            new Vector3(392.5,32,442.5)
        };
    }

    public void onplace(BlockPlaceEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Vector3 pos = SpawnPoint.get(player);
        if(Math.abs((pos.x-0.5) - block.x) <= 7 && Math.abs((pos.z-0.5) - block.z) <= 7 && block.y >= 11 && block.y <= 29){
            event.setCancelled(false);
        }else{
            event.setCancelled(true);
            player.teleport(pos);
        }
    }

    public void onbreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Vector3 pos = SpawnPoint.get(player);
        if(Math.abs((pos.x-0.5) - block.x) <= 7 && Math.abs((pos.z-0.5) - block.z) <= 7 && block.y >= 11 && block.y <= 29){
            event.setCancelled(false);
        }else{
            event.setCancelled(true);
            player.teleport(pos);
        }
    }

    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if(block.getId() == 0) return;
        Vector3 pos = SpawnPoint.get(player);
        if(Math.abs((pos.x-0.5) - block.x) <= 7 && Math.abs((pos.z-0.5) - block.z) <= 7 && block.y >= 11 && block.y <= 29){
            event.setCancelled(false);
        }else{
            event.setCancelled(true);
        }
    }

    public void startMark(){
        int c = 0;
        for (Map.Entry<String,Player> e : Players.entrySet()){
            e.getValue().getInventory().close(e.getValue());
            e.getValue().setGamemode(2);
            e.getValue().setAllowFlight(true);
            Points.put(e.getValue(),0);
            Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackMark(this,e.getValue()),c*200);
            c++;
        }
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackMarkFinish(this),c*200);
    }

    public void addMark(Player player,int rank){
        if(player.getName().equals(Marking.getName())) return;//TODO Message
        int mark = Points.get(Marking);
        int before = OldMark.get(player);
        mark = mark - before + rank;
        Points.put(Marking, mark);
        OldMark.put(player, rank);
        player.sendMessage(Main.getMessage(player,"buildbattle.vote."+rank));
    }

    public void Mark(Player player){
        Marking = player;
        for (Map.Entry<String,Player> e : AllPlayers.entrySet()){
            if(Players.containsValue(e.getValue())){
                Inventorys.setData(e.getValue(),new BuildbattleMarkInventory());
                OldMark.put(e.getValue(),0);
            }
            e.getValue().teleport(SpawnPoint.get(player));
            e.getValue().sendTip(Main.getMessage(e.getValue(),"buildbattle.owner",new String[]{player.getName()}));
        }
    }

    public void MarkFinish(){
        Map<Player,Integer> map = new HashMap<Player,Integer>();
        for (Map.Entry<String,Player> e : Players.entrySet()){
            map.put(e.getValue(),Points.get(e.getValue()));
        }
        List<Map.Entry<Player,Integer>> sort = Sort(map);
        finish(sort);
    }

    public void finish(List<Map.Entry<Player,Integer>> sort){
        Entity[] items = world.getEntities();
            for(Entity entity: items){
                if(entity instanceof EntityItem){
                    entity.kill();
                }
            }
        Player win = null;
        Player win2 = null;
        Player win3 = null;
        if(sort.size() >= 1) win = sort.get(0).getKey();
        if(sort.size() >= 2) win2 = sort.get(1).getKey();
        if(sort.size() >= 3) win3 = sort.get(2).getKey();
        for (Map.Entry<String,Player> e : AllPlayers.entrySet()){
            e.getValue().teleport(SpawnPoint.get(win));
            e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line"));
            if(sort.size() >= 1) e.getValue().sendMessage(Main.getMessage(e.getValue(),"buildbattle.result.win1",new String[]{sort.get(0).getKey().getName(),String.valueOf(sort.get(0).getValue())}));
            if(sort.size() >= 2) e.getValue().sendMessage(Main.getMessage(e.getValue(),"buildbattle.result.win2",new String[]{sort.get(1).getKey().getName(),String.valueOf(sort.get(1).getValue())}));
            if(sort.size() >= 3) e.getValue().sendMessage(Main.getMessage(e.getValue(),"buildbattle.result.win3",new String[]{sort.get(2).getKey().getName(),String.valueOf(sort.get(2).getValue())}));
            e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line2"));
            e.getValue().removeAllEffects();
            e.getValue().setHealth(20);
        }
        firework(win);
        setGameDataAsBoolean("gamenow2", false, true);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackFinish(this,win,win2,win3),200);
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

    public void finishLater(Player winner,Player winner2,Player winner3){
        for (Map.Entry<String,Player> e : Players.entrySet()){
            if(Players.containsValue(e.getValue())){
                if(winner.getName().equals(e.getValue().getName())){
                    int coin = (int)(Math.random() * 30)+10;
                    int exp = (int)(Math.random() * 30)+10;
                    Stat.addCoin(e.getValue(), coin);
                    Stat.addExp(e.getValue(),exp);
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line"));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"buildbattle.result"));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.coin",new String[]{String.valueOf(coin)}));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.exp",new String[]{String.valueOf(exp)}));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line2"));
                    Stat.setBuildbattleWin(e.getValue(),Stat.getBuildbattleWin(e.getValue())+1);
                }else{
                    int coin = (int)(Math.random() * 5)+1;
                    int exp = (int)(Math.random() * 5)+1;
                    Stat.addCoin(e.getValue(), coin);
                    Stat.addExp(e.getValue(),exp);
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line"));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"buildbattle.result"));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.coin",new String[]{String.valueOf(coin)}));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.exp",new String[]{String.valueOf(exp)}));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line2"));
                    Stat.setBuildbattleLose(e.getValue(),Stat.getBuildbattleLose(e.getValue())+1);
                }
            }
        }
        for (Map.Entry<String,Player> e : AllPlayers.entrySet()){
            if(!(e.getValue() instanceof Player)) continue;
            for (Map.Entry<String,Player> ee : AllPlayers.entrySet()){
                ee.getValue().showPlayer(e.getValue());
            }
            e.getValue().setGamemode(2);
            e.getValue().setGamemode(1);
            e.getValue().setGamemode(2);
            Stat.setNameTag(e.getValue());
            e.getValue().setAttribute(Attribute.getAttribute(Attribute.MAX_HUNGER).setValue(20));
            e.getValue().setAttribute(Attribute.getAttribute(Attribute.EXPERIENCE).setValue(0));
            e.getValue().setAttribute(Attribute.getAttribute(Attribute.EXPERIENCE_LEVEL).setValue(0));
            e.getValue().setAllowFlight(false);
        }
        for (Map.Entry<String,Player> e : AllPlayers.entrySet()){
            for (Map.Entry<String,Player> ee : AllPlayers.entrySet()){
                ee.getValue().showPlayer(e.getValue());
                e.getValue().showPlayer(ee.getValue());
            }
            if(number <= 7){
                e.getValue().teleport(new Position(88.5,16,85,Server.getInstance().getLevelByName("buildbattle")));
                Inventorys.setData(e.getValue(),new BuildbattleLobbyInventory());
            }else{
                 e.getValue().teleport(Server.getInstance().getLevelByName("home"+Home.getId(number)).getSafeSpawn());
                 Inventorys.setData(e.getValue(),new HomeInventory());
            }
            Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackAllow(this, e.getValue()),1);
            Main.gamenow.put(e.getValue(), false);
        }
        reset();
        if(Server.getInstance().isLevelLoaded("buildbattle"+number)) Server.getInstance().unloadLevel(Server.getInstance().getLevelByName("buildbattle"+number));
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackWorld(this),40);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackNow(this),100);
        if(number >= 8) Home.finish(number);
    }

    public void allow(Player player){
        player.setAllowFlight(false);
    }

    public void world(){
        String url = Server.getInstance().getDataPath()+"/worlds/buildbattle";
        directoryCopy(new File(url),new File(url+number));
    }

    public void now(){
        setGameDataAsBoolean("gamenow2", false, true);
    }

    public Item[] getItems(){
        ArrayList<Item> items = Item.getCreativeItems();
        HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
        map.put(438,0);
        map.put(384,0);
        map.put(373,0);
        map.put(383,0);
        for (int i = 0 ; i < items.size() ; i++){
            Item item = items.get(i);
            if(map.containsKey(item.getId())){
                items.remove(i);
            }
        }
        return items.stream().toArray(Item[]::new);
    }

    public void chat(PlayerChatEvent event){
        Player player = event.getPlayer();
        if(Players.containsKey(player)){
            event.setCancelled();
            if(Main.Chat.get(player.getName()) <= 0){
                player.sendMessage(Main.getMessage(player,"chat.wait"));
                return;
            }
            for (Map.Entry<String,Player> e : Players.entrySet()){
                if(!Setting.getChatHide(e.getValue()))
                e.getValue().sendMessage("<"+player.getNameTag()+"§f> "+event.getMessage());
            }
            int ch = Main.Chat.get(player.getName());
            Main.Chat.put(player.getName(),ch-1);
            Server.getInstance().getLogger().info("<"+player.getNameTag()+"§f> "+event.getMessage());
        }
    }

    public static List Sort(Map<Player,Integer> map){
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
    public HashMap<Player,Vector3> SpawnPoint = new HashMap<Player,Vector3>();
    public HashMap<Player,Integer> Points = new HashMap<Player,Integer>();
    public Player Marking;
    public HashMap<Player,Integer> OldMark = new HashMap<Player,Integer>();
    public HashMap<Integer,Integer> data10 = new HashMap<Integer,Integer>();
}
class CallbackTime extends Task{

    public Buildbattle owner;

    public CallbackTime(Buildbattle owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.Time();
    }
}
class CallbackGameTime extends Task{

    public Buildbattle owner;

    public CallbackGameTime(Buildbattle owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.GameTime();
    }
}
class CallbackFinish extends Task{

    public Buildbattle owner;
    public Player winner;
    public Player winner2;
    public Player winner3;

    public CallbackFinish(Buildbattle owner, Player winner, Player winner2, Player winner3){
        this.owner = owner;
        this.winner = winner;
        this.winner2 = winner2;
        this.winner3 = winner3;
    }

    public void onRun(int d){
        this.owner.finishLater(this.winner,this.winner2,this.winner3);
    }
}
class CallbackWorld extends Task{

    public Buildbattle owner;

    public CallbackWorld(Buildbattle owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.world();
    }
}
class CallbackAllow extends Task{

    public Buildbattle owner;
    public Player player;

    public CallbackAllow(Buildbattle owner, Player player){
        this.owner = owner;
        this.player = player;
    }

    public void onRun(int d){
        this.owner.allow(this.player);
    }
}
class CallbackNow extends Task{

    public Buildbattle owner;

    public CallbackNow(Buildbattle owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.now();
    }
}
class CallbackFirework extends Task{

    public Buildbattle owner;
    public Player player;

    public CallbackFirework(Buildbattle owner, Player player){
        this.owner = owner;
        this.player = player;
    }

    public void onRun(int d){
        this.owner.fireworkRun(player);
    }
}
class CallbackMark extends Task{

    public Buildbattle owner;
    public Player player;

    public CallbackMark(Buildbattle owner,Player player){
        this.owner = owner;
        this.player = player;
    }

    public void onRun(int d){
        this.owner.Mark(player);
    }
}
class CallbackMarkFinish extends Task{

    public Buildbattle owner;

    public CallbackMarkFinish(Buildbattle owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.MarkFinish();
    }
}