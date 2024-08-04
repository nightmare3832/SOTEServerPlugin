package sote.blockhunt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.inventory.InventoryPickupItemEvent;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.particle.Particle;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.network.protocol.MoveEntityPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import cn.nukkit.potion.Effect;
import cn.nukkit.scheduler.Task;
import sote.Game;
import sote.GameProvider;
import sote.Main;
import sote.home.Home;
import sote.murder.stage.MurderStage;
import sote.murder.stage.ObsoleteMine;
import sote.murder.stage.THEStrangeMansion;
import sote.murder.stage.WildernessTowns;
import sote.particle.Firework;
import sote.party.Party;
import sote.setting.Setting;
import sote.stat.Stat;

public class Blockhunt extends Game{

    public static final int MAX_PLAYERS = 15;
    public static final int MIN_PLAYERS = 2;
    public static final int VIP_PLAYERS = 15;

    public static final int WAIT_TIME = 10;
    public static final int WAIT_HOME_TIME = 30;
    public static final int GAME_TIME = 400;
    public static final int STAGE_SET_TIME = 10;

    public final int JOB_SEEKER = 0;
    public final int JOB_HIDER = 1;

    public final int FINISH_TYPE_ALL_KILL = 0;
    public final int FINISH_TYPE_TIMEOVER = 1;

    public Blockhunt(int number, boolean isHome){
        super(number, isHome);
        new BlockhuntSignManager();
        //stagelist = new MurderStage[]{new WildernessTowns(),new THEStrangeMansion(),new ObsoleteMine()};
        //stagelist2 = new MurderStage[]{new WildernessTowns(),new THEStrangeMansion(),new ObsoleteMine()};
        Server.getInstance().getScheduler().scheduleRepeatingTask(new BlockhuntTick(this), 1);
        addRoom();
    }

    @Override
    public void addRoom(){
        GameData = new HashMap<String,String>();
        GameData.put("count","0");
        GameData.put("deadcount","0");
        GameData.put("lifecount","0");
        GameData.put("time","0");
        GameData.put("gametime","0");
        GameData.put("timenow","0");
        GameData.put("gamenow","0");
        GameData.put("gamenow2","0");
        GameData.put("voted","1");
        Players = new HashMap<String,Player>();
        Spectators = new HashMap<String,Player>();
        AllPlayers = new HashMap<String,Player>();
        //stage = new THEStrangeMansion();
        canOpenChest = true;
        setting = new HashMap<String, String>();
        setting.put("map", "default");
        //MurderSignManager.updataSign(number);
        String url = Server.getInstance().getDataPath()+"/worlds/blockhunt";
        File newdir = new File(url+number);
        newdir.mkdir();
        File newdir2 = new File(url+number+"/region");
        newdir2.mkdir();
        directoryCopy(new File(url),new File(url+number));
        Server.getInstance().loadLevel("blockhunt"+number);
    }

    @Override
    public void reset(){
        GameData = new HashMap<String,String>();
        GameData.put("count","0");
        GameData.put("deadcount","0");
        GameData.put("lifecount","0");
        GameData.put("time","0");
        GameData.put("gametime","0");
        GameData.put("timenow","0");
        GameData.put("gamenow","0");
        GameData.put("gamenow2","0");
        GameData.put("voted","1");
        Players = new HashMap<String,Player>();
        Spectators = new HashMap<String,Player>();
        AllPlayers = new HashMap<String,Player>();
        //stage = new THEStrangeMansion();
        playerdata.clear();
        canOpenChest = true;
        setting = new HashMap<String, String>();
        setting.put("map", "default");
        BlockhuntSignManager.updataSign(number);
    }

    @Override
    public void setSetting(String key, String value){
        setting.put(key, value);
        if(key.equals("map") && !setting.get("map").equals("default")){
            MurderStage map = null;
            switch(setting.get("map")){
                case "Wilderness Towns":
                    map = new WildernessTowns();
                case "THE Strange Mansion":
                    map = new THEStrangeMansion();
                case "Obsolete Mine":
                    map = new ObsoleteMine();
            }
            //stage = map;
            setGameDataAsBoolean("voted", false, false);
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
        String ff = f.replace("blockhunt/","");
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

    public void Join(Player player,Boolean party){
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
                    c++;
                }
            }
            if(Stat.getVip(player) >= 2){
                if(!((getGameDataAsInt("count") + c) >= MAX_PLAYERS)){
                    for(Map.Entry<Player,Boolean> e : targets.entrySet()){
                        if(!e.getValue() && !e.getKey().getName().equals(player.getName())){
                            if(!AllPlayers.containsKey(e.getKey())) Join(e.getKey(), true);
                        }
                    }
                }else{
                    player.sendMessage(Main.getMessage(player,"party.no.join.game"));
                    return;
                }
            }else{
                if(!((getGameDataAsInt("count") >= VIP_PLAYERS))){
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
        }
        if(Stat.getVip(player) < 2 && getGameDataAsInt("count") >= VIP_PLAYERS){
            return;
        }
        if(getGameDataAsBoolean("gamenow2") || (getGameDataAsBoolean("timenow") && getGameDataAsInt("time") <= 0)){
            return;
        }
        if(getGameDataAsInt("count") >= MAX_PLAYERS){
            if(number >= MAX_PLAYERS) LookJoin(player);
            return;
        }
        GameProvider.joinGame(player, this);
        setGameDataAsInt("count", getGameDataAsInt("count") + 1, true);
        setGameDataAsInt("lifecount", getGameDataAsInt("lifecount") + 1, false);
        if((getGameDataAsInt("count") >= MIN_PLAYERS || number >= 8) && !getGameDataAsBoolean("timenow")){
            setGameDataAsBoolean("timenow", true, true);
            startTime();
        }
        linkBlock.put(player, Block.get(Block.SNOW_BLOCK,0));
        Players.put(player.getName(),player);
        AllPlayers.put(player.getName(),player);
        //Inventorys.setData(player,new MurderWaitInventory());
        if(number <= 7) Teleport(player);
        //Popups.setData(player, new MurderWaitPopup(player));
        player.setAllowFlight(false);
        Main.gamenow.put(player, true);
    }

    @Override
    public void LookJoin(Player player){
        Spectators.put(player.getName(),player);
        AllPlayers.put(player.getName(),player);
        //Inventorys.setData(player,new MurderLobbyInventory());
        if(number <= 7) Teleport(player);
        player.setAllowFlight(false);
        Main.gamenow.put(player, true);
    }

    @Override
    public void Quit(Player player){
        if(!Players.containsValue(player)){
            LookQuit(player);
            return;
        }
        setGameDataAsInt("count", getGameDataAsInt("count") - 1, true);
        if(getGameDataAsBoolean("gamenow2")){
            death(player,player);
            checkMember();
        }
        if(number <= 7){
            player.teleport(new Position(140,71,159,Server.getInstance().getLevelByName("blockhunt")));
            //Inventorys.setData(player,new MurderLobbyInventory());
        }else{
             player.teleport(Server.getInstance().getLevelByName("home"+Home.getId(number)).getSafeSpawn());
             //Inventorys.setData(player,new HomeInventory());
        }
        player.setDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG, true);
        player.setDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_CAN_SHOW_NAMETAG, true);
        //Popups.setData(player, new LobbyPopup(player));
        jobs.remove(player);
        Players.remove(player.getName());
        AllPlayers.remove(player.getName());
        Main.gamenow.put(player, false);
        GameProvider.quitGame(player);
    }

    @Override
    public void LookQuit(Player player){
        if(!Players.containsValue(player)) return;
        if(number <= 7){
            player.teleport(new Position(140,71,159,Server.getInstance().getLevelByName("blockhunt")));
            //Inventorys.setData(player,new MurderLobbyInventory());
        }else{
            player.teleport(Server.getInstance().getLevelByName("home"+Home.getId(number)).getSafeSpawn());
            //Inventorys.setData(player,new HomeInventory());
        }
        jobs.remove(player);
        Spectators.remove(player.getName());
        AllPlayers.remove(player.getName());
        Main.gamenow.put(player, false);
    }

    public void Teleport(Player player){
        Level level = Server.getInstance().getLevelByName("blockhunt"+number);
        player.teleport(new Position(140,71,159,level));
    }

    public void startTime(){
        if(number <= 7){
            setGameDataAsInt("time", WAIT_TIME, true);
        }else{
            setGameDataAsInt("time", WAIT_HOME_TIME, true);
        }
        for (Map.Entry<String,Player> e : Players.entrySet()){
            e.getValue().sendTip(Main.getMessage(e.getValue(),"murder.wait.count",new String[]{String.valueOf(getGameDataAsInt("time"))}));
        }
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackTime(this), 20);
    }

    public void Time(){
        if(getGameDataAsInt("time") <= 0){
            Start();
            return;
        }
        setGameDataAsInt("time", getGameDataAsInt("time") - 1, true);
        for (Map.Entry<String,Player> e : Players.entrySet()){
            sendTip(e.getValue(), Main.getMessage(e.getValue(),"murder.wait.count",new String[]{String.valueOf(getGameDataAsInt("time"))}), 1);
        }
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackTime(this), 20);
    }

    public void startGameTime(){
        setGameDataAsInt("gametime", GAME_TIME, false);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackGameTime(this),20);
    }

    public void GameTime(){
        if(!getGameDataAsBoolean("gamenow")) return;
        if(getGameDataAsInt("gametime") <= 0){
            finish(FINISH_TYPE_TIMEOVER);
            return;
        }
        setGameDataAsInt("gametime", getGameDataAsInt("gametime") - 1, false);
        if(getGameDataAsInt("gametime") % 60 == 0 && getGameDataAsInt("gametime") != 0){
            for (Map.Entry<String,Player> e : Players.entrySet()){
                e.getValue().sendMessage(Main.getMessage(e.getValue(),"murder.game.count",new String[]{String.valueOf(getGameDataAsInt("gametime"))}));
            }
        }
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackGameTime(this),20);
    }

    public void setLinkBlock(Player player, Block block){
        linkBlock.put(player, block);
    }

    public void Start(){
        if(getGameDataAsInt("count") < MIN_PLAYERS){
            setGameDataAsBoolean("time", false, true);
            setGameDataAsInt("time", WAIT_TIME, true);
            if(number >= 8) Home.notStart(number);
            return;
        }
        int c = 0;
        world = Server.getInstance().getLevelByName("blockhunt"+number);
        //Vector3[] pos = stage.getSpawn();
        Player job = getSeeker();
        countplayer = getGameDataAsInt("count");
        canOpenChest = true;
        setGameDataAsBoolean("gamenow2", true, true);
        for (Map.Entry<String,Player> e : Players.entrySet()){
            Main.setHide.put(e.getValue(),false);
            e.getValue().setHealth(20);
            BeforePosition.put(e.getValue(), new Vector3());
            isHidden.put(e.getValue(), false);
            isStaying.put(e.getValue(), false);
            linkBlockEid.put(e.getValue(), Entity.entityCount++);
            lastHeartburn.put(e.getValue(), (long)0);
            if(job.equals(e.getValue())){
                jobs.put(e.getValue(), JOB_SEEKER);
                for (Map.Entry<String,Player> ee : Players.entrySet()){
                    ee.getValue().showPlayer(e.getValue());
                }
            }else{
                jobs.put(e.getValue(), JOB_HIDER);
                for (Map.Entry<String,Player> ee : Players.entrySet()){
                    ee.getValue().hidePlayer(e.getValue());
                }
                spawnBlock(e.getValue());
                Main.setBossGaugeName(e.getValue(), "§d§lVisible§r §7⋙ §b§l"+linkBlock.get(e.getValue()).getName()+"§r");
            }
            //e.getValue().teleport(new Position(pos[c].x, pos[c].y, pos[c].z, world));
            e.getValue().teleport(new Position(140,71,159, world));
            //Popups.setData(e.getValue(), new MurderGamePopup(e.getValue()));
            Stat.setMurderPlays(e.getValue(), Stat.getMurderPlays(e.getValue()) + 1);
        }
        c = 0;
        for (Map.Entry<String,Player> e : Spectators.entrySet()){
            Main.setHide.put(e.getValue(),false);
            for (Map.Entry<String,Player> ee : Players.entrySet()){
                ee.getValue().hidePlayer(e.getValue());
            }
            jobs.put(e.getValue(),2);
            e.getValue().setGamemode(0);
            e.getValue().setAllowFlight(true);
            e.getValue().getInventory().clearAll();
            //Inventorys.setData(e.getValue(),new MurderDeathInventory());
            //e.getValue().teleport(new Position(pos[c].x, pos[c].y, pos[c].z, world));
            c++;
        }
        setGameDataAsBoolean("gamenow", true, true);
        startGameTime();
        for(Entity ent : world.getEntities()){
            if(ent instanceof EntityItem){
                ent.kill();
            }
        }
    }

    public Player getSeeker(){
        List<Player> list = new ArrayList(Players.values());
        Collections.shuffle(list);
        Player[] entrys = (Player[])list.toArray(new Player[list.size()]);
        return entrys[0];
    }

    public void Pickup(InventoryPickupItemEvent event){
        Item item = event.getItem().getItem();
        int id = item.getId();
            if(event.getInventory().getHolder() instanceof Player && Players.containsValue(event.getInventory().getHolder())){
                Player player = (Player)event.getInventory().getHolder();
                event.setCancelled();
            }
    }

    public void finish(int type){
        if(type == 0){}
            for (Map.Entry<String,Player> e : Players.entrySet()){
                for (Map.Entry<String,Player> ee : Players.entrySet()){
                    ee.getValue().showPlayer(e.getValue());
                    e.getValue().showPlayer(ee.getValue());
                }
                Stat.setNameTag(e.getValue());
                e.getValue().sendMessage("finish!");
            }
        canOpenChest = false;
        closeChests();
        setGameDataAsBoolean("gamenow2", false, true);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackFinish(this),200);
    }

    public void finishLater(){
        Entity[] items = world.getEntities();
        for(Entity entity: items){
            if(entity instanceof EntityItem){
                entity.kill();
            }
        }
        for (Map.Entry<String,Player> e : Players.entrySet()){
            if(e.getValue() instanceof Player){
                for (Map.Entry<String,Player> ee : Players.entrySet()){
                    ee.getValue().showPlayer(e.getValue());
                }
                e.getValue().setGamemode(2);
                e.getValue().setHealth(20);
                Stat.setNameTag(e.getValue());
                jobs.remove(e.getValue());
                e.getValue().setAttribute(Attribute.getAttribute(Attribute.MAX_HUNGER).setValue(20));
                e.getValue().setAttribute(Attribute.getAttribute(Attribute.EXPERIENCE).setValue(0));
                e.getValue().setAttribute(Attribute.getAttribute(Attribute.EXPERIENCE_LEVEL).setValue(0));
                e.getValue().setAllowFlight(false);
            }
        }
        for (Map.Entry<Player,Long> e : linkBlockEid.entrySet()){
            RemoveEntityPacket pk = new RemoveEntityPacket();
            pk.eid = e.getValue();
            for (Map.Entry<String,Player> ee : Players.entrySet()){
                ee.getValue().dataPacket(pk);
            }
        }
        for (Map.Entry<String,Player> e : Players.entrySet()){
            if(e.getValue() instanceof Player){
                if(number <= 7){
                    e.getValue().teleport(new Position(140,71,159,Server.getInstance().getLevelByName("blockhunt")));
                    //Inventorys.setData(e.getValue(),new MurderLobbyInventory());
                }else{
                    e.getValue().teleport(Server.getInstance().getLevelByName("home"+Home.getId(number)).getSafeSpawn());
                    //Inventorys.setData(e.getValue(),new HomeInventory());
                }
                //Popups.setData(e.getValue(), new LobbyPopup(e.getValue()));
                Main.gamenow.put(e.getValue(), false);
            }
        }
        reset();
        if(number >= 8) Home.finish(number);
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
        if(Players.containsValue(player.getName())){
            for (Map.Entry<String,Player> e : Players.entrySet()){
                if(e.getValue() instanceof Player){
                    Vector3 winner = new Vector3(player.x,player.y+1,player.z);
                    Firework.start(winner,e.getValue());
                }
            }
            for (Map.Entry<String,Player> e : Spectators.entrySet()){
                if(e.getValue() instanceof Player){
                    Vector3 winner = new Vector3(player.x,player.y+1,player.z);
                    Firework.start(winner,e.getValue());
                }
            }
        }
    }

    public void checkMember(){
        int seekerCount = 0;
        int hiderCount = 0;
        for (Map.Entry<String,Player> e : Players.entrySet()){
            if(jobs.get(e.getValue()) == JOB_SEEKER){
                seekerCount++;
            }else if(jobs.get(e.getValue()) == JOB_HIDER){
                hiderCount++;
            }
        }
        setGameDataAsInt("seekercount", seekerCount, false);
        setGameDataAsInt("hidercount", hiderCount, false);
        if(hiderCount <= 0){
            finish(FINISH_TYPE_ALL_KILL);
        }
    }

    public void attack(Player player, Long eid){
        if(!getGameDataAsBoolean("gamenow2")) return;
        for (Map.Entry<String,Player> e : Players.entrySet()){
            if(jobs.get(e.getValue()) == JOB_HIDER){
                if(linkBlockEid.get(e.getValue()).equals(eid)){
                    Item item = player.getInventory().getItemInHand();
                    float itemDamage = item.getAttackDamage();
                    for (Enchantment enchantment : item.getEnchantments()) {
                        itemDamage += enchantment.getDamageBonus(e.getValue());
                    }
                    EntityDamageByEntityEvent entityDamageByEntityEvent = new EntityDamageByEntityEvent(player, e.getValue(), EntityDamageEvent.DamageCause.ENTITY_ATTACK, itemDamage);
                    e.getValue().attack(entityDamageByEntityEvent);
                }
            }
        }
    }

    public void onInteract(Player player, Vector3 target){
        if(getGameDataAsBoolean("gamenow2")){
            if(jobs.get(player) == JOB_SEEKER){
                for (Map.Entry<String,Player> e : Players.entrySet()){
                    if(jobs.get(e.getValue()) == JOB_HIDER){
                        if(isHidden.get(e.getValue())){
                            Vector3 hidePosition = HiddenPosition.get(e.getValue());
                            if(hidePosition.x == target.x && hidePosition.y == target.y && hidePosition.z == target.z){
                                Main.setBossGaugeName(e.getValue(), "§d§lVisible§r §7⋙ §b§l"+linkBlock.get(e.getValue()).getName()+"§r");
                                spawnBlock2(e.getValue());
                                moveBlock(e.getValue());
                                untransformationBlock(e.getValue());
                                Main.setBossGaugeValue(e.getValue(), 1, 1);
                                isHidden.put(e.getValue(), false);
                                isStaying.put(e.getValue(), false);
                                Item item = player.getInventory().getItemInHand();
                                float itemDamage = item.getAttackDamage();
                                for (Enchantment enchantment : item.getEnchantments()) {
                                    itemDamage += enchantment.getDamageBonus(e.getValue());
                                }
                                EntityDamageByEntityEvent entityDamageByEntityEvent = new EntityDamageByEntityEvent(player, e.getValue(), EntityDamageEvent.DamageCause.ENTITY_ATTACK, itemDamage);
                                e.getValue().attack(entityDamageByEntityEvent);
                            }
                        }
                    }
                }
            }
        }
    }

    public void death(Player player,Player damager){
        jobs.put(player, JOB_SEEKER);
        Item item = getItemByBlock(player);
        world.dropItem(player, item);
        despawnBlock(player);
        for (Map.Entry<String,Player> ee : Players.entrySet()){
            ee.getValue().showPlayer(player);
        }
        player.setHealth(20);
        checkMember();
    }

    public Item getItemByBlock(Player player){
        Block block = linkBlock.get(player);
        switch(block.getId()){
            case Block.FLOWER_POT_BLOCK:
                return Item.get(Item.FLOWER_POT, 0);
            default:
                return Item.get(block.getId(), block.getDamage());
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
                    if(player.getGamemode() == 2 && !Setting.getChatHide(e.getValue())){
                        if(jobs.get(e.getValue()) == 0){
                            e.getValue().sendMessage("<"+player.getNameTag()+"§f> "+event.getMessage());
                        }else{
                            e.getValue().sendMessage("<"+player.getNameTag()+"§f> "+event.getMessage());
                        }
                    }else if(player.getGamemode() == 0 && !Setting.getChatHide(e.getValue())){
                        if(e.getValue().getGamemode() == 0){
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
            Server.getInstance().getLogger().info("<"+player.getNameTag()+"§f | "+player.getName()+"> "+event.getMessage());
        }
    }

    public void Tick(){
        if(!getGameDataAsBoolean("gamenow")) return;
        for (Map.Entry<String,Player> e : Players.entrySet()){
            if(getGameDataAsBoolean("gamenow2")){
                for (Map.Entry<String,Player> ee : Players.entrySet()){
                    if(e.getValue().distance(ee.getValue()) <= 5){
                        if(jobs.get(e.getValue()) == JOB_HIDER && jobs.get(ee.getValue()) == JOB_SEEKER){
                            if(isHidden.get(e.getValue())){
                                if(System.currentTimeMillis() - lastHeartburn.get(e.getValue()) >= 1000){
                                    Effect effect = Effect.getEffect(Effect.SPEED);
                                    effect.setDuration(2);
                                    effect.setAmplifier(0);
                                    e.getValue().addEffect(effect);
                                    e.getValue().sendMessage("heartburn");
                                    lastHeartburn.put(e.getValue(), System.currentTimeMillis());
                                }
                            }
                        }
                    }
                }
            }
            if(jobs.get(e.getValue()) == JOB_HIDER){
                Vector3 b = BeforePosition.get(e.getValue());
                int x = Math.round((float)(e.getValue().x - 0.5));
                int y = Math.round((float)(e.getValue().y - 0.1));
                int z = Math.round((float)(e.getValue().z - 0.5));
                if(x == b.x && y == b.y && z == b.z){
                    if(isHidden.get(e.getValue())){
                        //Hidden継続
                    }else{
                        if(isStaying.get(e.getValue())){
                            moveBlock(e.getValue());
                            long distanceTime = System.currentTimeMillis() - StayTime.get(e.getValue());
                            if(distanceTime % 1000 <= 50 && distanceTime >= 500){//一秒ごと
                                long maxTime = 5000;
                                if(distanceTime >= maxTime){
                                    Main.setBossGaugeValue(e.getValue(), 1, Integer.MAX_VALUE);
                                    Vector3 pos = new Vector3(x, y, z);
                                    if(world.getBlock(pos).getId() == 0){
                                        isHidden.put(e.getValue(), true);
                                        HiddenPosition.put(e.getValue(), new Vector3(x, y, z));
                                        transformationBlock(e.getValue());
                                        despawnBlock2(e.getValue());
                                        moveBlock2(e.getValue());
                                        Main.setBossGaugeName(e.getValue(), "§a§lHidden§r §7⋙ §b§l"+linkBlock.get(e.getValue()).getName()+"§r");
                                    }else{
                                        e.getValue().sendMessage("invalid location");
                                    }
                                }else{
                                    Main.setBossGaugeValue(e.getValue(), (int)(maxTime - distanceTime), (int)maxTime);
                                }
                            }
                        }else{
                            StayTime.put(e.getValue(), System.currentTimeMillis());
                            isStaying.put(e.getValue(), true);
                            moveBlock(e.getValue());
                        }
                    }
                }else{
                    if(isHidden.get(e.getValue())){
                        Main.setBossGaugeName(e.getValue(), "§d§lVisible§r §7⋙ §b§l"+linkBlock.get(e.getValue()).getName()+"§r");
                        spawnBlock2(e.getValue());
                        moveBlock(e.getValue());
                        untransformationBlock(e.getValue());
                    }else{
                        moveBlock(e.getValue());
                    }
                    Main.setBossGaugeValue(e.getValue(), 1, 1);
                    isHidden.put(e.getValue(), false);
                    isStaying.put(e.getValue(), false);
                }
                BeforePosition.get(e.getValue()).setComponents(x, y, z);
            }
        }
    }

    public void transformationBlock(Player player){
        UpdateBlockPacket pk = new UpdateBlockPacket();
        Vector3 pos = HiddenPosition.get(player);
        pk.x = (int)pos.x;
        pk.y = (int)pos.y;
        pk.z = (int)pos.z;
        pk.blockId = linkBlock.get(player).getId();
        pk.blockData = linkBlock.get(player).getDamage();
        pk.flags = UpdateBlockPacket.FLAG_NONE;
        for (Map.Entry<String, Player> e : AllPlayers.entrySet()){
            if(!player.equals(e.getValue())) e.getValue().dataPacket(pk);
        }
        HashMap<Integer, Integer[]> colors = new HashMap<Integer, Integer[]>();
        colors.put(0, new Integer[]{250,0,20});//Red
        colors.put(1, new Integer[]{102,204,250});//Light Blue
        colors.put(2, new Integer[]{102,255,0});//Lime
        colors.put(3, new Integer[]{153,250,250});//Aqua
        colors.put(4, new Integer[]{153,0,250});//Purple
        colors.put(5, new Integer[]{250,250,51});//Yellow
        colors.put(6, new Integer[]{51,0,250});//Blue
        double tx = 0;
        double tz = 0;
        double ty = 0;
        double size = getBlockSize(linkBlock.get(player));
        LevelEventPacket pk2 = new LevelEventPacket();
        pk2.evid = (short) (LevelEventPacket.EVENT_ADD_PARTICLE_MASK | Particle.TYPE_DUST);
        for(int s = 0; s <= 180; s += 20){
            double rads = deg2rad(s);
            tz = pos.z + 0.5 + (size * Math.cos(rads));
                for(int t = 0; t < 360; t += 20){
                    double radt = deg2rad(t);
                    tx = pos.x + 0.5 + (size * Math.sin(rads) * Math.cos(radt));
                    ty = pos.y + (size * Math.sin(rads) * Math.sin(radt));
                    Integer[] c = colors.get((int)(Math.random() * 6));
                    pk2.x = (float)tx;
                    pk2.y = (float)ty;
                    pk2.z = (float)tz;
                    pk2.data = ((255 & 0xff) << 24) | ((c[0] & 0xff) << 16) | ((c[1] & 0xff) << 8) | (c[2] & 0xff);
                    for (Map.Entry<String, Player> ee : AllPlayers.entrySet()){
                        ee.getValue().dataPacket(pk2);
                    }
               }
        }
    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public double getBlockSize(Block block){
        switch(block.getId()){
            case Block.FLOWER_POT_BLOCK:
                return 0.3;
            default:
                return 1.3;
        }
    }

    public void untransformationBlock(Player player){
        UpdateBlockPacket pk = new UpdateBlockPacket();
        Vector3 pos = HiddenPosition.get(player);
        pk.x = (int)pos.x;
        pk.y = (int)pos.y;
        pk.z = (int)pos.z;
        pk.blockId = 0;
        pk.blockData = 0;
        pk.flags = UpdateBlockPacket.FLAG_NONE;
        for (Map.Entry<String, Player> e : AllPlayers.entrySet()){
            if(!player.equals(e.getValue())) e.getValue().dataPacket(pk);
        }
    }

    public void spawnBlock(Player player){
        AddEntityPacket pk = new AddEntityPacket();
        Long eid = linkBlockEid.get(player);
        pk.entityUniqueId = eid;
        pk.entityRuntimeId = eid;
        pk.type = 66;
        pk.x = (float) player.x;
        pk.y = (float) (player.y + 0.5);
        pk.z = (float) player.z;
        int flags = 0;
        flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
        pk.metadata = new EntityMetadata()
                .putLong(Entity.DATA_FLAGS,flags)
                .putInt(Entity.DATA_VARIANT, linkBlock.get(player).getId() | linkBlock.get(player).getDamage() << 8)
                .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1);
        for (Map.Entry<String, Player> e : AllPlayers.entrySet()){
            e.getValue().dataPacket(pk);
        }
    }

    public void despawnBlock(Player player){
        RemoveEntityPacket pk = new RemoveEntityPacket();
        Long eid = linkBlockEid.get(player);
        pk.eid = eid;
        for (Map.Entry<String, Player> e : AllPlayers.entrySet()){
            e.getValue().dataPacket(pk);
        }
    }

    public void moveBlock(Player player){
        MoveEntityPacket pk = new MoveEntityPacket();
        Long eid = linkBlockEid.get(player);
        pk.eid = eid;
        pk.x = (float) player.x;
        pk.y = (float) player.y + 0.5;
        pk.z = (float) player.z;
        for (Map.Entry<String, Player> e : AllPlayers.entrySet()){
            e.getValue().dataPacket(pk);
        }
    }

    public void spawnBlock2(Player player){
        AddEntityPacket pk = new AddEntityPacket();
        Long eid = linkBlockEid.get(player);
        pk.entityUniqueId = eid;
        pk.entityRuntimeId = eid;
        pk.type = 66;
        pk.x = (float) player.x;
        pk.y = (float) (player.y + 0.5);
        pk.z = (float) player.z;
        int flags = 0;
        flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
        pk.metadata = new EntityMetadata()
                .putLong(Entity.DATA_FLAGS,flags)
                .putInt(Entity.DATA_VARIANT, linkBlock.get(player).getId() | linkBlock.get(player).getDamage() << 8)
                .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1);
        for (Map.Entry<String, Player> e : AllPlayers.entrySet()){
            if(!player.equals(e.getValue())) e.getValue().dataPacket(pk);
        }
    }

    public void despawnBlock2(Player player){
        RemoveEntityPacket pk = new RemoveEntityPacket();
        Long eid = linkBlockEid.get(player);
        pk.eid = eid;
        for (Map.Entry<String, Player> e : AllPlayers.entrySet()){
            if(!player.equals(e.getValue())) e.getValue().dataPacket(pk);
        }
    }

    public void moveBlock2(Player player){
        MoveEntityPacket pk = new MoveEntityPacket();
        Long eid = linkBlockEid.get(player);
        pk.eid = eid;
        Vector3 pos = HiddenPosition.get(player);
        pk.x = pos.x + 0.5;
        pk.y = pos.y + 0.5;
        pk.z = pos.z + 0.5;
        player.dataPacket(pk);
    }

    public HashMap<Player, HashMap<String, String>> playerdata = new HashMap<Player, HashMap<String, String>>();
    public HashMap<String, String> namelist = new HashMap<String, String>();
    //public BlockhuntStage stage;
    public HashMap<Player, Integer> vote = new HashMap<Player, Integer>();
    public int countplayer = 0;
    public HashMap<Player, Integer> jobs = new HashMap<Player, Integer>();
    public HashMap<Player, Block> linkBlock = new HashMap<Player, Block>();
    public HashMap<Player, Long> linkBlockEid = new HashMap<Player, Long>();
    public HashMap<Player, Boolean> isHidden = new HashMap<Player, Boolean>();
    public HashMap<Player, Boolean> isStaying = new HashMap<Player, Boolean>();
    public HashMap<Player, Vector3> HiddenPosition = new HashMap<Player, Vector3>();
    public HashMap<Player, Vector3> BeforePosition = new HashMap<Player, Vector3>();
    public HashMap<Player, Long> StayTime = new HashMap<Player, Long>();
    public HashMap<Player, Long> lastHeartburn = new HashMap<Player, Long>();
    public File file;
    //public BlockhuntStage[] stagelist;
    //public static BlockhuntStage[] stagelist2;
}
class CallbackTime extends Task{

    public Blockhunt owner;

    public CallbackTime(Blockhunt owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.Time();
    }
}
class CallbackGameTime extends Task{

    public Blockhunt owner;

    public CallbackGameTime(Blockhunt owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.GameTime();
    }
}
class CallbackFinish extends Task{

    public Blockhunt owner;

    public CallbackFinish(Blockhunt owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.finishLater();
    }
}
class CallbackFirework extends Task{

    public Blockhunt owner;
    public Player player;

    public CallbackFirework(Blockhunt owner,Player player){
        this.owner = owner;
        this.player = player;
    }

    public void onRun(int d){
        this.owner.fireworkRun(this.player);
    }
}
class BlockhuntTick extends Task{

    public Blockhunt owner;

    public BlockhuntTick(Blockhunt owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.Tick();
    }
}