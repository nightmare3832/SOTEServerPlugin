package sote.murder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.data.IntPositionEntityData;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.inventory.InventoryPickupItemEvent;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemColorArmor;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.particle.DestroyBlockParticle;
import cn.nukkit.level.particle.Particle;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.AddPlayerPacket;
import cn.nukkit.network.protocol.ContainerSetContentPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.InteractPacket;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.network.protocol.MobArmorEquipmentPacket;
import cn.nukkit.network.protocol.MovePlayerPacket;
import cn.nukkit.network.protocol.PlayerListPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.network.protocol.SetEntityDataPacket;
import cn.nukkit.network.protocol.TakeItemEntityPacket;
import cn.nukkit.potion.Effect;
import cn.nukkit.scheduler.Task;
import sote.Game;
import sote.GameProvider;
import sote.Main;
import sote.PlayerData;
import sote.PlayerDataManager;
import sote.home.Home;
import sote.inventory.Inventorys;
import sote.inventory.ServerChestInventorys;
import sote.inventory.home.HomeInventory;
import sote.inventory.murder.BystanderInventory;
import sote.inventory.murder.MurderDeathInventory;
import sote.inventory.murder.MurderInventory;
import sote.inventory.murder.MurderLobbyInventory;
import sote.inventory.murder.MurderWaitInventory;
import sote.murder.achievements.MurderAchievements;
import sote.murder.armor.Armors;
import sote.murder.stage.MurderStage;
import sote.murder.stage.ObsoleteMine;
import sote.murder.stage.THEStrangeMansion;
import sote.murder.stage.WildernessTowns;
import sote.murder.upgrade.MurderUpgrades;
import sote.murder.weapon.Weapons;
import sote.particle.Firework;
import sote.party.Party;
import sote.popup.LobbyPopup;
import sote.popup.MurderDeathPopup;
import sote.popup.MurderGamePopup;
import sote.popup.MurderWaitPopup;
import sote.popup.Popups;
import sote.setting.Setting;
import sote.stat.Stat;

public class Murder extends Game{

    public static final int MAX_PLAYERS = 15;
    public static final int MIN_PLAYERS = 3;
    public static final int VIP_PLAYERS = 15;

    public static final int WAIT_TIME = 60;
    public static final int WAIT_HOME_TIME = 30;
    public static final int GAME_TIME = 400;
    public static final int STAGE_SET_TIME = 10;

    public Murder(int number, boolean isHome){
        super(number, isHome);
        new MurderSignManager();
        new MurderSkinManager();
        stagelist = new MurderStage[]{new WildernessTowns(),new THEStrangeMansion(),new ObsoleteMine()};
        stagelist2 = new MurderStage[]{new WildernessTowns(),new THEStrangeMansion(),new ObsoleteMine()};
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
        GameData.put("voted","1");
        Players = new HashMap<String,Player>();
        Spectators = new HashMap<String,Player>();
        AllPlayers = new HashMap<String,Player>();
        stage = new THEStrangeMansion();
        emerald = new MurderEmeraldManager(this);
        canOpenChest = true;
        stages = new MurderStage[]{};
        setting = new HashMap<String, String>();
        setting.put("map", "default");
        //MurderSignManager.updataSign(number);
        String url = Server.getInstance().getDataPath()+"/worlds/murder";
        File newdir = new File(url+number);
        newdir.mkdir();
        File newdir2 = new File(url+number+"/region");
        newdir2.mkdir();
        directoryCopy(new File(url),new File(url+number));
        Server.getInstance().loadLevel("murder"+number);
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
        stage = new THEStrangeMansion();
        stages = new MurderStage[]{};
        playerdata.clear();
        humandata.clear();
        humanitem.clear();
        humanskin.clear();
        disguisedCount.clear();
        disguisedIdentity.clear();
        loseKarmaCount.clear();
        stabbingCount.clear();
        canOpenChest = true;
        stages = new MurderStage[]{};
        setting = new HashMap<String, String>();
        setting.put("map", "default");
        MurderSignManager.updataSign(number);
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
            stage = map;
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
        String ff = f.replace("murder/","");
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
        if(!party && mapp.get("owner").equals(player.getName().toLowerCase()) && !isHome){
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
        Players.put(player.getName(),player);
        AllPlayers.put(player.getName(),player);
        Inventorys.setData(player,new MurderWaitInventory());
        if(!isHome) Teleport(player);
        if(setting.get("map").equals("default") && stages.length <= 0){
            List<MurderStage> list=Arrays.asList(stagelist);
            Collections.shuffle(list);
            MurderStage[] ss =(MurderStage[])list.toArray(new MurderStage[list.size()]);
            MurderStage[]ssss = new MurderStage[3];
            int count = 0;
                for(int i = 0;i < ss.length;i++){
                    if(count <= 2){
                        ssss[count] = ss[i];
                        count++;
                    }
                }
            stages = ssss;
        }
        if(getGameDataAsBoolean("voted")){
            player.sendMessage(Main.getMessage(player,"murder.can.vote"));
            for(int i = 0;i < stages.length;i++){
                player.sendMessage(Main.getMessage(player,"murder.vote.stage",new String[]{stages[i].getName()}));
            }
        }else{
            player.sendMessage(Main.getMessage(player,"murder.vote.next",new String[]{stage.getName()}));
        }
        Popups.setData(player, new MurderWaitPopup(player));
        player.setAllowFlight(false);
        Main.gamenow.put(player, true);
    }

    @Override
    public void LookJoin(Player player){
        Spectators.put(player.getName(),player);
        AllPlayers.put(player.getName(),player);
        Inventorys.setData(player,new MurderLobbyInventory());
        if(!isHome) Teleport(player);
        if(stages.length <= 0){
            List<MurderStage> list=Arrays.asList(stagelist);
            Collections.shuffle(list);
            MurderStage[] ss =(MurderStage[])list.toArray(new MurderStage[list.size()]);
            MurderStage[]ssss = new MurderStage[3];
            if(ss.length == 2) ssss = new MurderStage[2];
            int count = 0;
                for(int i = 0;i < ss.length;i++){
                    if(count <= 2){
                        ssss[count] = ss[i];
                        count++;
                    }
                }
            stages = ssss;
        }
        if(getGameDataAsBoolean("voted")){
            player.sendMessage(Main.getMessage(player,"murder.can.vote"));
            for(int i = 0;i < stages.length;i++){
                player.sendMessage(Main.getMessage(player,"murder.vote.stage",new String[]{stages[i].getName()}));
            }
        }else{
            player.sendMessage(Main.getMessage(player,"murder.vote.next",new String[]{stage.getName()}));
        }
        player.setAllowFlight(false);
        Main.gamenow.put(player, true);
    }

    public void vote(Player player,int w){
        if(getGameDataAsBoolean("voted")){
            vote.put(player, w);
            player.sendMessage(Main.getMessage(player,"murder.vote",new String[]{stages[w].getName()}));
        }else{
            player.sendMessage(Main.getMessage(player,"murder.vote.already.finish"));
        }
    }

    @Override
    public void Quit(Player player){
        if(!Players.containsValue(player)){
            LookQuit(player);
            return;
        }
        setGameDataAsInt("count", getGameDataAsInt("count") - 1, true);
        if(player.getGamemode() == 2 && getGameDataAsBoolean("gamenow2") == true){
            death(player,player);
            checkMember();
        }
        if(jobs.containsKey(player) && jobs.get(player) == 0 && getGameDataAsBoolean("gamenow2")){
            changeMurder();
        }
        if(!isHome){
            player.teleport(new Position(-246,6,-234,Server.getInstance().getLevelByName("murder")));
            Inventorys.setData(player,new MurderLobbyInventory());
        }else{
             player.teleport(Server.getInstance().getLevelByName("home"+Home.getId(number)).getSafeSpawn());
             Inventorys.setData(player,new HomeInventory());
        }
        player.setDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG, true);
        player.setDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_CAN_SHOW_NAMETAG, true);
        Popups.setData(player, new LobbyPopup(player));
        jobs.remove(player);
        Players.remove(player.getName());
        AllPlayers.remove(player.getName());
        Main.gamenow.put(player, false);
        GameProvider.quitGame(player);
    }

    @Override
    public void LookQuit(Player player){
        if(!Players.containsValue(player)) return;
        if(!isHome){
            player.teleport(new Position(-246,6,-234,Server.getInstance().getLevelByName("murder")));
            Inventorys.setData(player,new MurderLobbyInventory());
        }else{
             player.teleport(Server.getInstance().getLevelByName("home"+Home.getId(number)).getSafeSpawn());
             Inventorys.setData(player,new HomeInventory());
        }
        jobs.remove(player);
        Spectators.remove(player.getName());
        AllPlayers.remove(player.getName());
        Main.gamenow.put(player, false);
    }

    public void Teleport(Player player){
        Level level = Server.getInstance().getLevelByName("murder"+number);
        player.teleport(new Position(131.5,12,146.5,level));
    }

    public void startTime(){
        if(!isHome){
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
        }else if(setting.get("map").equals("default") && getGameDataAsInt("time") == STAGE_SET_TIME){
            closeChests();
            canOpenChest = false;
            for (Map.Entry<String,Player> e : Players.entrySet()){
                e.getValue().sendMessage(Main.getMessage(e.getValue(),"murder.vote.finish"));
            }
            int zero = 0;
            int one = 0;
            int two = 0;
            for (Map.Entry<String,Player> e : Players.entrySet()){
                if(vote.containsKey(e.getValue())){
                    if(vote.get(e.getValue()) == 0) zero++;
                    if(vote.get(e.getValue()) == 1) one++;
                    if(vote.get(e.getValue()) == 2) two++;
                }
            }
            int result = 0;
            if(two >= zero && two >= one) result = 2;
            if(one >= zero && one >= two) result = 1;
            if(zero >= one && zero >= two) result = 0;
            for (Map.Entry<String,Player> e : Players.entrySet()){
                e.getValue().sendMessage(Main.getMessage(e.getValue(),"murder.vote.next",new String[]{stages[result].getName()}));
            }
            stage = stages[result];
            setGameDataAsBoolean("voted", false, true);
        }else if(getGameDataAsInt("time") == 5){
            Vector3[] poss = stage.getSpawn();
            for(Vector3 pos : poss){
                //world.loadChunk((int) pos.x >> 4, (int) pos.x >> 4);
            }
        }
        if(getGameDataAsInt("time") % 10 == 0 || getGameDataAsInt("time") <= 5){
            for (Map.Entry<String,Player> e : AllPlayers.entrySet()){
                LevelEventPacket pk = new LevelEventPacket();
                pk.evid = LevelEventPacket.EVENT_SOUND_BUTTON_CLICK;
                pk.x = (float) e.getValue().x;
                pk.y = (float) e.getValue().y;
                pk.z = (float) e.getValue().z;
                pk.data = 0;
                e.getValue().dataPacket(pk);
            }
        }
        for (Map.Entry<String,Player> e : Players.entrySet()){
            //e.getValue().sendActionBar(Main.getMessage(e.getValue(),"murder.wait.count",new String[]{String.valueOf(getGameDataAsInt("time"))}));
            sendTip(e.getValue(), Main.getMessage(e.getValue(),"murder.wait.count",new String[]{String.valueOf(getGameDataAsInt("time"))}), 1);
        }
        setGameDataAsInt("time", getGameDataAsInt("time") - 1, true);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackTime(this), 20);
    }

    public void startGameTime(){
        setGameDataAsInt("gametime", GAME_TIME, false);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackGameTime(this),20);
    }

    public void GameTime(){
        if(!getGameDataAsBoolean("gamenow2")) return;
        setGameDataAsInt("gametime", getGameDataAsInt("gametime") - 1, false);
        int time = getGameDataAsInt("gametime");
        if(time <= 0){
            finish(0);
            return;
        }
        if(time % 60 == 0 || (time <= 30 && time % 15 == 0) || time == 10 || time <= 5){
            for (Map.Entry<String,Player> e : Players.entrySet()){
                e.getValue().sendMessage(Main.getMessage(e.getValue(),"murder.game.count",new String[]{String.valueOf(time)}));
            }
        }
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackGameTime(this),20);
    }

    public void Start(){
        if(getGameDataAsInt("count") < MIN_PLAYERS){
            setGameDataAsBoolean("timenow", false, true);
            setGameDataAsInt("time", WAIT_TIME, true);
            if(number >= 8) Home.notStart(number);
            return;
        }
        int c = 0;
        world = Server.getInstance().getLevelByName("murder"+number);
        Vector3[] pos = stage.getSpawn();
        Player[] job = getMurder();
        String[] names = getRandomName();
        Integer[] skinIds = MurderSkinManager.getRandomSkinID();
        String[] colors = getRandomColor();
        Item item;
        ItemColorArmor item2;
        countplayer = getGameDataAsInt("count");
        humandata = new HashMap<PlayerData ,HashMap<String, String>>();
        humanitem = new HashMap<PlayerData, HashMap<String,Item>>();
        humanskin = new HashMap<PlayerData, Skin>();
        eiddata = new HashMap<Long, Integer>();
        namelist = new HashMap<PlayerData, String>();
        nexteiddata = 0;
        HashMap<String, String> map;
        HashMap<Player,HashMap<String, String>> mapp;
        canOpenChest = true;
        boolean withStaff = false;
        for (Map.Entry<String, Player> e : Players.entrySet()){
            e.getValue().teleport(new Position(pos[c].x, pos[c].y, pos[c].z, world));
            c++;
        }
        c = 0;
        for (Map.Entry<String,Player> e : Players.entrySet()){
            Main.setHide.put(e.getValue(), false);
            Map<Long,Player> players = e.getValue().getLevel().getPlayers();
            //for (Map.Entry<Long,Player> ee : players.entrySet()){
            //    e.getValue().showPlayer(ee.getValue());
            //}
            PlayerData playerData = PlayerDataManager.getPlayerData(e.getValue());
            map = new HashMap<String,String>();
            e.getValue().setHealth(20);
            e.getValue().setDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG, false);
            e.getValue().setDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_CAN_SHOW_NAMETAG, false);
            LevelEventPacket pk = new LevelEventPacket();
            pk.evid = LevelEventPacket.EVENT_SOUND_GHAST;
            pk.x = (float)e.getValue().x;
            pk.y = (float)e.getValue().y;
            pk.z = (float)e.getValue().z;
            pk.data = 0;
            e.getValue().dataPacket(pk);
            Effect effect = Effect.getEffect(15);
            effect.setDuration(5*20);
            effect.setAmplifier(0);
            e.getValue().addEffect(effect);
            color.put(e.getValue(),colors[c]);
            randomname.put(e.getValue(),names[c]);
            e.getValue().setNameTag("§"+color.get(e.getValue())+names[c]);
            e.getValue().sendMessage(Main.getMessage(e.getValue(),"murder.secret.name",new String[]{color.get(e.getValue())+names[c]}));
            bskin.put(e.getValue(),e.getValue().getSkin());
            Skin skin = MurderSkinManager.getSkinByID(e.getValue(), skinIds[c]);
            askin.put(e.getValue(),skin);
            //askin.put(e.getValue(),e.getValue().getSkin());
            e.getValue().setSkin(skin);
            for (Map.Entry<String,Player> ee : Players.entrySet()){
                //ee.getValue().hidePlayer(e.getValue());
                //ee.getValue().showPlayer(e.getValue());
                Main.mustHidePlayerLater(ee.getValue(), e.getValue(), 5);
                Main.mustShowPlayerLater(ee.getValue(), e.getValue(), 10);
            }
            c++;
            item = Item.get(299);
            item2 = (ItemColorArmor) item;
            chestplate.put(e.getValue(), getArmorColor(color.get(e.getValue()),item2));
            item = Armors.getSellectHelmet(e.getValue());
            if(item.getId() == 298){
                item2 = (ItemColorArmor) item;
                item = getArmorColor(color.get(e.getValue()),item2);
            }
            if(!Stat.getMurderHat(e.getValue()).equals("unknown")) item = Item.get(0);
            helmet.put(e.getValue(),item);
            item = Armors.getSellectLeggings(e.getValue());
            if(item.getId() == 300){
                item2 = (ItemColorArmor) item;
                item = getArmorColor(color.get(e.getValue()),item2);
            }
            leggings.put(e.getValue(),item);
            item = Armors.getSellectBoots(e.getValue());
            if(item.getId() == 301){
                item2 = (ItemColorArmor) item;
                item = getArmorColor(color.get(e.getValue()),item2);
            }
            boots.put(e.getValue(),item);
            Popups.setData(e.getValue(), new MurderGamePopup(e.getValue()));
            emeralds.put(e.getValue(),0);
            canpickup.put(e.getValue(),true);
            loseKarmaCount.put(e.getValue(), 0);
            playerdata.put(e.getValue(),map);
            Weapons.reset(e.getValue(), true);
            if(e.getValue().getName().equals(job[0].getName())){
                registerMurder(e.getValue());
            }else if(e.getValue().getName().equals(job[1].getName())){
                registerGun(e.getValue());
            }else{
                registerBystander(e.getValue());
            }
            map.put("name","§"+color.get(e.getValue())+randomname.get(e.getValue()));
            map.put("job",jobs.get(e.getValue()).toString());
            map.put("death","0");
            Stat.setMurderPlays(e.getValue(), Stat.getMurderPlays(e.getValue()) + 1);
            namelist.put(playerData, "§"+color.get(e.getValue())+randomname.get(e.getValue()));
            if(Stat.getVip(e.getValue()) >= 10){
                withStaff = true;
            }
        }
        for (Map.Entry<String,Player> e : Players.entrySet()){
            if(withStaff){
                MurderAchievements.setLevel(e.getValue(), "Being_Watched", 2);
            }
        }
        c = 0;
        for (Map.Entry<String,Player> e : Spectators.entrySet()){
            Main.setHide.put(e.getValue(),false);
            for (Map.Entry<String,Player> ee : Players.entrySet()){
                ee.getValue().hidePlayer(e.getValue());
            }
            jobs.put(e.getValue(),2);
            canpickup.put(e.getValue(),false);
            e.getValue().setGamemode(0);
            e.getValue().setAllowFlight(true);
            e.getValue().getInventory().clearAll();
            Inventorys.setData(e.getValue(),new MurderDeathInventory());
            e.getValue().teleport(new Position(pos[c].x, pos[c].y, pos[c].z, world));
            c++;
        }
        setGameDataAsBoolean("gamenow2", true, true);
        setGameDataAsBoolean("gamenow", true, true);
        startGameTime();
        resetEmeraldManager();
        for(Entity ent : world.getEntities()){
            if(ent instanceof EntityItem){
                ent.kill();
            }
        }
    }

    public static Item getArmorColor(String code, ItemColorArmor item){
        if(code.equals("0")) return item.setColor(1,1,1);
        if(code.equals("1")) return item.setColor(0,1,168);
        if(code.equals("2")) return item.setColor(0,170,3);
        if(code.equals("3")) return item.setColor(1,169,170);
        if(code.equals("4")) return item.setColor(171,1,1);
        if(code.equals("5")) return item.setColor(170,0,167);
        if(code.equals("6")) return item.setColor(255,171,4);
        if(code.equals("7")) return item.setColor(170,170,170);
        if(code.equals("8")) return item.setColor(85,85,85);
        if(code.equals("9")) return item.setColor(86,84,255);
        if(code.equals("a")) return item.setColor(84,255,88);
        if(code.equals("b")) return item.setColor(84,255,255);
        if(code.equals("c")) return item.setColor(255,80,82);
        if(code.equals("d")) return item.setColor(255,85,252);
        if(code.equals("e")) return item.setColor(255,255,85);
        if(code.equals("f")) return item.setColor(250,250,250);
        return item;
    }

    public void registerMurder(Player player){
        player.setSubtitle(Main.getMessage(player,"murder.job.murderer.sub"));
        player.sendTitle(Main.getMessage(player,"murder.job.murderer"));
        int e = getGameDataAsInt("count") - 1;
        if(e < 0) e = 0;
        player.setAttribute(Attribute.getAttribute(Attribute.EXPERIENCE_LEVEL).setValue(e));
        player.setAttribute(Attribute.getAttribute(Attribute.EXPERIENCE).setValue(1));
        player.setSprinting(false);
        player.setMovementSpeed((float)0.1);
        player.setAttribute(Attribute.getAttribute(Attribute.FOOD).setValue(20));
        jobs.put(player, 0);
        onemurder.put(player, 8);
        twomurder.put(player, 8);
        haveknife.put(player, 1);
        knifecount.put(player, 1);
        stabbingCount.put(player,  0);
        disguisedCount.put(player, 0);
        disguisedIdentity.put(player, new HashMap<Integer, HashMap<String, String>>());
        Inventorys.setData(player,new MurderInventory());
        Inventorys.data2.get(player).register(player);
        player.setSprinting(true);
    }

    public void registerGun(Player player){
        player.setSubtitle(Main.getMessage(player,"murder.job.detective.sub"));
        player.sendTitle(Main.getMessage(player,"murder.job.detective"));
        player.setAttribute(Attribute.getAttribute(Attribute.EXPERIENCE).setValue(1));
        player.setSprinting(false);
        player.setMovementSpeed((float)0.1);
        player.setAttribute(Attribute.getAttribute(Attribute.FOOD).setValue(6));
        jobs.put(player, 1);
        havegun.put(player, true);
        defgun.put(player, true);
        Inventorys.setData(player,new BystanderInventory());
        Inventorys.data2.get(player).register(player);
        player.setSprinting(false);
    }

    public void registerBystander(Player player){
        player.setSubtitle(Main.getMessage(player,"murder.job.bystander.sub"));
        player.sendTitle(Main.getMessage(player,"murder.job.bystander"));
        player.setAttribute(Attribute.getAttribute(Attribute.EXPERIENCE).setValue(1));
        player.setSprinting(false);
        player.setMovementSpeed((float)0.1);
        player.setAttribute(Attribute.getAttribute(Attribute.FOOD).setValue(6));
        jobs.put(player, 1);
        havegun.put(player, false);
        defgun.put(player, false);
        Inventorys.setData(player,new BystanderInventory());
        Inventorys.data2.get(player).register(player);
        player.setSprinting(false);
    }

    public Player[] getMurder(){
        Player murder = null;
        Player gun = null;
        int all = 0;
        int min  = 100;
        int ms = 0;
            for(Map.Entry<String,Player> e : Players.entrySet()){
                if(min >= (int)Math.floor(Stat.getMurderKarma(e.getValue()) / 3)){
                    min = (int)Math.floor(Stat.getMurderKarma(e.getValue()) / 3);
                }
            }
        ms = 10 - min;
        if(ms < 0) ms = 0;
        HashMap<Player,Integer> map = new HashMap<Player,Integer>();
            for(Map.Entry<String,Player> e : Players.entrySet()){
                map.put(e.getValue(),(int)Math.floor(Stat.getMurderKarma(e.getValue()) / 3)+ms);
                all += ((int)Math.floor(Stat.getMurderKarma(e.getValue()) / 3)+ms);
            }
        int rand = (int)(Math.random() * all);
        int c = 0;
            for(Map.Entry<Player,Integer> e : map.entrySet()){
                if(rand >= c && rand <= (c + (int)e.getValue()) && !(murder instanceof Player)){
                    murder = e.getKey();
                }
                c += (int)e.getValue();
            }
        all = 0;
        HashMap<Player,Integer> distance = new HashMap<Player,Integer>();
            for(Map.Entry<Player,Integer> e : map.entrySet()){
                if(!e.getKey().getName().equals(murder.getName())){
                    distance.put(e.getKey(), (400 - Math.abs(map.get(murder) - e.getValue())));
                    all += (400 - Math.abs(map.get(murder) - e.getValue()));
                }
            }
        rand = (int)(Math.random() * all);
        c = 0;
            for(Map.Entry<Player,Integer> e : distance.entrySet()){
                if(rand >= c && rand <= (c + (int)e.getValue())){
                    gun = e.getKey();
                }
                c += (int)e.getValue();
            }
        return new Player[]{murder,gun};
    }

    public void Pickup(InventoryPickupItemEvent event){
        Item item = event.getItem().getItem();
        int id = item.getId();
            if(event.getInventory().getHolder() instanceof Player && Players.containsValue(event.getInventory().getHolder())){
                Player player = (Player)event.getInventory().getHolder();
                if(lastcantpickup.containsKey(player)){
                    if(System.currentTimeMillis() - lastcantpickup.get(player) < 5000){
                        event.setCancelled();
                        return;
                    }else{
                        lastcantpickup.remove(player);
                    }
                }
                if(canpickup.containsKey(player) && canpickup.get(player) == false){
                    event.setCancelled();
                    return;
                }
                if(player.getGamemode() == 0) event.setCancelled();
                if(id == 268 || id == 272 || id == 267 || id == 283 || id == 276){
                    if(jobs.containsKey(player) && jobs.get(player) == 1){
                        event.setCancelled();
                    }else{
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
                        knifecount.put(player,knifecount.get(player)+1);
                        if(Inventorys.data2.get(player) instanceof MurderInventory) Inventorys.data2.get(player).register(player);
                        Weapons.pickupKnife(event.getItem().getItem().getDamage());
                    }
                }
                if(id == 290 || id == 291 || id == 292 || id == 294 || id == 293){
                    if(jobs.containsKey(player) && jobs.get(player) == 0){
                        event.setCancelled();
                    }else{
                        event.setCancelled();
                        if(havegun.get(player)) return;
                        TakeItemEntityPacket pk = new TakeItemEntityPacket();
                        pk.entityId = player.getId();
                        pk.target = event.getItem().getId();
                        Server.broadcastPacket(event.getItem().getViewers().values(), pk);
                        pk = new TakeItemEntityPacket();
                        pk.entityId = 0;
                        pk.target = event.getItem().getId();
                        player.dataPacket(pk);
                        event.getItem().kill();
                        havegun.put(player,true);
                        Weapons.reset(player, false);
                        if(Inventorys.data2.get(player) instanceof BystanderInventory) Inventorys.data2.get(player).register(player);
                    }
                }
                if(id == 388){
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
                    pickupEmerald(player, event.getItem(), false);
                    if(MurderUpgrades.getResult(player, "Emerald Upgrade")) pickupEmerald(player, null, true);
                }
            }
    }

    public void pickupEmerald(Player player, EntityItem item, boolean isBonus){
        int eme = emeralds.get(player)+1;
        emeralds.put(player,eme);
        Stat.setMurderEmeraldCollected(player, Stat.getMurderEmeraldCollected(player) + 1);
        if(jobs.containsKey(player) && jobs.get(player) == 0){
        	if(Inventorys.data2.get(player) instanceof MurderInventory) Inventorys.data2.get(player).register(player);
            player.sendMessage(Main.getMessage(player,"murder.murder.find.emerald"));
                if(emeralds.get(player) >= 3){
                    onemurder.put(player, 13);
                    if(Inventorys.data2.get(player) instanceof MurderInventory) Inventorys.data2.get(player).register(player);
                }
                if(emeralds.get(player) >= 5){
                    twomurder.put(player, 10);
                    if(Inventorys.data2.get(player) instanceof MurderInventory) Inventorys.data2.get(player).register(player);
                }
        }
        if(jobs.containsKey(player) && jobs.get(player) == 1){
            int need = 5;
            if(Stat.getMurderKarma(player) <= -300) need = 6;
            if(Stat.getMurderKarma(player) <= -500) need = 7;
            if(Inventorys.data2.get(player) instanceof BystanderInventory) Inventorys.data2.get(player).register(player);
            if(emeralds.get(player) >= need){
                if(havegun.get(player) == false){
                    player.sendMessage(Main.getMessage(player,"murder.bystander.get.gun"));
                    Stat.setMurderWeaponTraded(player, Stat.getMurderWeaponTraded(player) + 1);
                    havegun.put(player,true);
                    emeralds.put(player,0);
                    if(Inventorys.data2.get(player) instanceof BystanderInventory) Inventorys.data2.get(player).register(player);
                }else{
                    if(Weapons.weaponsGun.get(player).getLevel() < 3){
                        player.sendMessage(Main.getMessage(player,"murder.bystander.gun.level.up"));
                        emeralds.put(player,0);
                        Weapons.weaponsGun.get(player).setLevel(Weapons.weaponsGun.get(player).getLevel()+1);
                        if(Inventorys.data2.get(player) instanceof BystanderInventory) Inventorys.data2.get(player).register(player);
                    }else{
                        player.sendMessage(Main.getMessage(player,"murder.bystander.gun.not.level"));
                    }
                }
            }else{
                player.sendMessage(Main.getMessage(player,"murder.bystander.find.emerald",new String[]{String.valueOf(emeralds.get(player)),String.valueOf(need)}));
            }
        }
        if(!isBonus) emerald.pickup(player,item);
    }

    public void resetEmeraldManager(){
        emerald.reset();
    }

    public static boolean isNumber(String num){
        try {
            Integer.parseInt(num);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public void finish(int type){
        finish(type,null);
    }

    public void finish(int type, Player winner){
        if(!getGameDataAsBoolean("gamenow2")) return;
        if(type == 0) finishByTimeover();
        if(type == 1) finishByKillall();
        if(type == 2) finishByKillmurder(winner);
        Entity[] items = world.getEntities();
            for(Entity entity: items){
                if(entity instanceof EntityItem){
                    entity.kill();
                }
            }
            for (Map.Entry<String,Player> e : Players.entrySet()){
                for (Map.Entry<String,Player> ee : Players.entrySet()){
                    ee.getValue().showPlayer(e.getValue());
                    e.getValue().showPlayer(ee.getValue());
                }
                e.getValue().setDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG, true);
                e.getValue().setDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_CAN_SHOW_NAMETAG, true);
                e.getValue().setSkin(bskin.get(e.getValue()));
                Stat.setNameTag(e.getValue());
            }
        canOpenChest = false;
        closeChests();
        setGameDataAsBoolean("gamenow2", false, true);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackFinish(this),200);
    }

    public void finishLater(){
        for (Map.Entry<String,Player> e : Players.entrySet()){
            if(e.getValue() instanceof Player){
                for (Map.Entry<String,Player> ee : Players.entrySet()){
                    ee.getValue().showPlayer(e.getValue());
                }
                int c = 0;
                for(Map.Entry<Long,Integer> ee : eiddata.entrySet()){
                    if(ee.getKey() instanceof Long){
                        RemoveEntityPacket pk = new RemoveEntityPacket();
                        pk.eid = ee.getKey();
                        e.getValue().dataPacket(pk);
                        c++;
                    }
                }
                Weapons.weaponsGun.get(e.getValue()).setLevel(1);
                e.getValue().setGamemode(2);
                Stat.setNameTag(e.getValue());
                jobs.remove(e.getValue());
                e.getValue().setAttribute(Attribute.getAttribute(Attribute.MAX_HUNGER).setValue(20));
                e.getValue().setAttribute(Attribute.getAttribute(Attribute.EXPERIENCE).setValue(0));
                e.getValue().setAttribute(Attribute.getAttribute(Attribute.EXPERIENCE_LEVEL).setValue(0));
                e.getValue().setAllowFlight(false);
                e.getValue().setDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG, true);
                e.getValue().setDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_CAN_SHOW_NAMETAG, true);
                GameProvider.quitGame(e.getValue());
            }
        }
        for (Map.Entry<String,Player> e : Players.entrySet()){
            if(e.getValue() instanceof Player){
                if(!isHome){
                    e.getValue().teleport(new Position(-246,6,-234,Server.getInstance().getLevelByName("murder")));
                    Inventorys.setData(e.getValue(),new MurderLobbyInventory());
                }else{
                     e.getValue().teleport(Server.getInstance().getLevelByName("home"+Home.getId(number)).getSafeSpawn());
                     Inventorys.setData(e.getValue(),new HomeInventory());
                }
                Popups.setData(e.getValue(), new LobbyPopup(e.getValue()));
                Main.gamenow.put(e.getValue(), false);
            }
        }
        reset();
        if(isHome) Home.finish(number);
    }

    public void finishByTimeover(){
        Player murder = null;
        for (Map.Entry<String,Player> e : Players.entrySet()){
            for (Map.Entry<Player,HashMap<String,String>> ee : playerdata.entrySet()){
                if(playerdata.get(ee.getKey()).get("job").equals("1")){
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"murder.result.bystander",new String[]{playerdata.get(ee.getKey()).get("name"),ee.getKey().getName()}));
                }else if(playerdata.get(ee.getKey()).get("job").equals("0")){
                    murder = ee.getKey();
                }
            }
            String murderNames = "";
            if(disguisedCount.get(murder) > 0){
                murderNames = "§"+color.get(murder)+randomname.get(murder);
                for(int i = 1;i < disguisedCount.get(murder);i++){
                    murderNames += " → §"+disguisedIdentity.get(murder).get(i).get("color")+disguisedIdentity.get(murder).get(i).get("name");
                }
            }else{
                murderNames = "§"+color.get(murder)+randomname.get(murder);
            }
            e.getValue().sendMessage(Main.getMessage(e.getValue(),"murder.result.murder",new String[]{murderNames,murder.getName()}));
            e.getValue().sendMessage(Main.getMessage(e.getValue(),"murder.result.timeover"));
            if(jobs.get(e.getValue()) == 0){
            }else if(jobs.get(e.getValue()) == 1){
                e.getValue().sendTitle(Main.getMessage(e.getValue(),"murder.survived"));
            }
        }
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackFinishTimeover(this),199);
    }

    public void finishLaterTimeover(){
        Player murder = null;
        for (Map.Entry<String,Player> e : Players.entrySet()){
            for (Map.Entry<Player,HashMap<String,String>> ee : playerdata.entrySet()){
                if(playerdata.get(ee.getKey()).get("job").equals("0")){
                    murder = ee.getKey();
                }
            }
            if(jobs.get(e.getValue()) == 0){
                Stat.setMurderMurderLose(e.getValue(),Stat.getMurderMurderLose(e.getValue())+1);
                int coin = (int)(Math.random() * 10)+1;
                int exp = (int)(Math.random() * 10)+1;
                Stat.addCoin(e.getValue(), coin);
                Stat.addExp(e.getValue(),exp);
                e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line"));
                e.getValue().sendMessage(Main.getMessage(e.getValue(),"murder.result"));
                e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.coin",new String[]{String.valueOf(coin)}));
                e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.exp",new String[]{String.valueOf(exp)}));
                e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line2"));
            }else if(jobs.get(e.getValue()) == 1){
                if(e.getValue().getGamemode() == 2){
                    int coin = (countplayer*5)+(int)(Math.random() * 10)+5;
                    int exp = (int)(Math.random() * 60)+40;
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line"));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"murder.result"));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.coin",new String[]{String.valueOf(coin)}));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.exp",new String[]{String.valueOf(exp)}));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.karma",new String[]{"10"}));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line2"));
                    Stat.addCoin(e.getValue(), coin);
                    Stat.addExp(e.getValue(),exp);
                    Stat.setMurderKarma(e.getValue(),Stat.getMurderKarma(e.getValue())+10);
                    Stat.setMurderBystanderWin(e.getValue(),Stat.getMurderBystanderWin(e.getValue())+1);
                }else{
                    int coin = (countplayer*5)+(int)(Math.random() * 10)+5;
                    int exp = (int)(Math.random() * 60)+40;
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line"));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"murder.result"));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.coin",new String[]{String.valueOf(coin)}));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.exp",new String[]{String.valueOf(exp)}));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line2"));
                    Stat.addCoin(e.getValue(), coin);
                    Stat.addExp(e.getValue(),exp);
                    Stat.setMurderBystanderWin(e.getValue(),Stat.getMurderBystanderWin(e.getValue())+1);
                }
            }
        }
    }

    public void finishByKillall(){
        Player murder = null;
        for (Map.Entry<String,Player> e : Players.entrySet()){
            for (Map.Entry<Player,HashMap<String,String>> ee : playerdata.entrySet()){
                if(playerdata.get(ee.getKey()).get("job").equals("1")){
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"murder.result.bystander",new String[]{playerdata.get(ee.getKey()).get("name"),ee.getKey().getName()}));
                }else if(playerdata.get(ee.getKey()).get("job").equals("0")){
                    murder = ee.getKey();
                    if(stabbingCount.get(ee.getKey()) == 0){
                        MurderAchievements.setLevel(ee.getKey(), "Clean_Hands", 2);
                    }
                }
            }
            String murderNames = "";
            if(disguisedCount.get(murder) > 0){
                murderNames = "§"+color.get(murder)+randomname.get(murder);
                for(int i = 1;i < disguisedCount.get(murder);i++){
                    murderNames += " → §"+disguisedIdentity.get(murder).get(i).get("color")+disguisedIdentity.get(murder).get(i).get("name");
                }
            }else{
                murderNames = "§"+color.get(murder)+randomname.get(murder);
            }
            e.getValue().sendMessage(Main.getMessage(e.getValue(),"murder.result.murder",new String[]{murderNames,murder.getName()}));
            e.getValue().sendMessage(Main.getMessage(e.getValue(),"murder.result.killall"));
            if(jobs.get(e.getValue()) == 0){
                e.getValue().sendTitle(Main.getMessage(e.getValue(),"murder.won"));
            }else if(jobs.get(e.getValue()) == 1){
            }
        }
        firework(murder);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackFinishKillall(this),199);
    }

    public void finishLaterKillall(){
        Player murder = null;
        for (Map.Entry<String,Player> e : Players.entrySet()){
            for (Map.Entry<Player,HashMap<String,String>> ee : playerdata.entrySet()){
                if(playerdata.get(ee.getKey()).get("job").equals("0")){
                    murder = ee.getKey();
                }
            }
            if(jobs.get(e.getValue()) == 0){
                int coin = (countplayer*5)+(int)(Math.random() * 10)+25;
                int exp = (int)(Math.random() * 60)+40;
                e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line"));
                e.getValue().sendMessage(Main.getMessage(e.getValue(),"murder.result"));
                e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.coin",new String[]{String.valueOf(coin)}));
                e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.exp",new String[]{String.valueOf(exp)}));
                e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.karma",new String[]{"15"}));
                e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line2"));
                Stat.addExp(e.getValue(),exp);
                Stat.addCoin(e.getValue(), coin);
                Stat.setMurderKarma(e.getValue(),Stat.getMurderKarma(e.getValue())+15);
                Stat.setMurderMurderWin(e.getValue(),Stat.getMurderMurderWin(e.getValue())+1);
            }else if(jobs.get(e.getValue()) == 1){
                Stat.setMurderBystanderLose(e.getValue(),Stat.getMurderBystanderLose(e.getValue())+1);
                int coin = (int)(Math.random() * 10)+1;
                int exp = (int)(Math.random() * 10)+1;
                Stat.addCoin(e.getValue(), coin);
                Stat.addExp(e.getValue(),exp);
                e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line"));
                e.getValue().sendMessage(Main.getMessage(e.getValue(),"murder.result"));
                e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.coin",new String[]{String.valueOf(coin)}));
                e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.exp",new String[]{String.valueOf(exp)}));
                e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line2"));
            }
        }
    }

    public void finishByKillmurder(Player winner){
        Player murder = null;
        for (Map.Entry<String,Player> e : Players.entrySet()){
            for (Map.Entry<Player,HashMap<String,String>> ee : playerdata.entrySet()){
                if(!winner.getName().equals(ee.getKey().getName()) && playerdata.get(ee.getKey()).get("job").equals("1")){
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"murder.result.bystander",new String[]{playerdata.get(ee.getKey()).get("name"),ee.getKey().getName()}));
                }else if(playerdata.get(ee.getKey()).get("job").equals("0")){
                    murder = ee.getKey();
                }
            }
            e.getValue().sendMessage(Main.getMessage(e.getValue(),"murder.result.bystander.win",new String[]{"§"+color.get(winner)+randomname.get(winner),winner.getName()}));
            String murderNames = "";
            if(disguisedCount.get(murder) > 0){
                murderNames = "§"+color.get(murder)+randomname.get(murder);
                for(int i = 1;i < disguisedCount.get(murder);i++){
                    murderNames += " → §"+disguisedIdentity.get(murder).get(i).get("color")+disguisedIdentity.get(murder).get(i).get("name");
                }
            }else{
                murderNames = "§"+color.get(murder)+randomname.get(murder);
            }
            e.getValue().sendMessage(Main.getMessage(e.getValue(),"murder.result.murder",new String[]{murderNames,murder.getName()}));
            e.getValue().sendMessage(Main.getMessage(e.getValue(),"murder.result.killmurder",new String[]{color.get(winner)+randomname.get(winner)}));
            if(jobs.get(e.getValue()) == 0){
                //sendTip(e.getValue(),Main.getMessage(e.getValue(),"murder.won"),5);
            }else if(jobs.get(e.getValue()) == 1){
                if(winner.getName().equals(e.getValue().getName())){
                    e.getValue().sendTitle(Main.getMessage(e.getValue(),"murder.won"));
                    Stat.setMurderBystanderKills(e.getValue(), Stat.getMurderBystanderKills(e.getValue()) + 1);
                }else{
                    if(e.getValue().getGamemode() == 2){
                        e.getValue().sendTitle(Main.getMessage(e.getValue(),"murder.survived"));
                    }else{
                    }
                }
            }
        }
        firework(winner);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackFinishKillmurder(this, winner),199);
    }

    public void finishLaterKillmurder(Player winner){
        Player murder = null;
        for (Map.Entry<String,Player> e : Players.entrySet()){
            for (Map.Entry<Player,HashMap<String,String>> ee : playerdata.entrySet()){
                if(playerdata.get(ee.getKey()).get("job").equals("0")){
                    murder = ee.getKey();
                }
            }
            if(jobs.get(e.getValue()) == 0){
                Stat.setMurderMurderLose(e.getValue(),Stat.getMurderMurderLose(e.getValue())+1);
                int coin = (int)(Math.random() * 10)+1;
                int exp = (int)(Math.random() * 10)+1;
                Stat.addCoin(e.getValue(), coin);
                Stat.addExp(e.getValue(),exp);
                e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line"));
                e.getValue().sendMessage(Main.getMessage(e.getValue(),"murder.result"));
                e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.coin",new String[]{String.valueOf(coin)}));
                e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.exp",new String[]{String.valueOf(exp)}));
                e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line2"));
            }else if(jobs.get(e.getValue()) == 1){
                if(winner.getName().equals(e.getValue().getName())){
                    Stat.setMurderBystanderWin(e.getValue(),Stat.getMurderBystanderWin(e.getValue())+1);
                    int coin = (countplayer*5)+(int)(Math.random() * 10)+25;
                    int exp = (int)(Math.random() * 60)+40;
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line"));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"murder.result"));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.coin",new String[]{String.valueOf(coin)}));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.exp",new String[]{String.valueOf(exp)}));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.karma",new String[]{"15"}));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line2"));
                    Stat.addExp(e.getValue(),exp);
                    Stat.addCoin(e.getValue(), coin);
                    Stat.setMurderKarma(e.getValue(),Stat.getMurderKarma(e.getValue())+15);
                    Stat.setMurderMisskills(e.getValue(),0);
                }else{
                    if(e.getValue().getGamemode() == 2){
                        Stat.setMurderBystanderWin(e.getValue(),Stat.getMurderBystanderWin(e.getValue())+1);
                        int coin = (countplayer*5)+(int)(Math.random() * 10)+5;
                        int exp = (int)(Math.random() * 60)+40;
                        e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line"));
                        e.getValue().sendMessage(Main.getMessage(e.getValue(),"murder.result"));
                        e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.coin",new String[]{String.valueOf(coin)}));
                        e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.exp",new String[]{String.valueOf(exp)}));
                        e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.karma",new String[]{"10"}));
                        e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line2"));
                        Stat.addExp(e.getValue(),exp);
                        Stat.addCoin(e.getValue(), coin);
                        Stat.setMurderKarma(e.getValue(),Stat.getMurderKarma(e.getValue())+10);
                    }else{
                        Stat.setMurderBystanderWin(e.getValue(),Stat.getMurderBystanderWin(e.getValue())+1);
                        int coin = (int)(Math.random() * 10)+1;
                        int exp = (int)(Math.random() * 10)+1;
                        Stat.addCoin(e.getValue(), coin);
                        Stat.addExp(e.getValue(),exp);
                        e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line"));
                        e.getValue().sendMessage(Main.getMessage(e.getValue(),"murder.result"));
                        e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.coin",new String[]{String.valueOf(coin)}));
                        e.getValue().sendMessage(Main.getMessage(e.getValue(),"status.add.exp",new String[]{String.valueOf(exp)}));
                        e.getValue().sendMessage(Main.getMessage(e.getValue(),"game.result.line2"));
                    }
                }
            }
        }
    }

    public void hit(Player damager, Player entity){
        if(entity.getGamemode() == 0) return;
        if(!getGameDataAsBoolean("gamenow2")) return;
        if(jobs.containsKey(damager) && jobs.containsKey(entity)){
            if((int)damager.distance(entity) >= 25) MurderAchievements.setLevel(damager, "Long_Shot", 2);
            death(entity,damager);
            if(jobs.get(damager) == 0 && jobs.get(entity) == 1){
                Stat.setMurderMurderKills(damager, Stat.getMurderMurderKills(damager) + 1);
                MurderAchievements.setLevel(damager, "Not_So_Personal", 2);
                checkMember();
            }else if(jobs.get(damager) == 1 && jobs.get(entity) == 0){
                if(loseKarmaCount.get(damager) == 0){
                    MurderAchievements.setLevel(damager, "Kill_Yourself", 2);
                }
                finish(2, damager);
            }else if(jobs.get(damager) == 1 && jobs.get(entity) == 1){
                misskill(damager,entity);
            }
        }
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
        if(AllPlayers.containsValue(player)){
            for (Map.Entry<String,Player> e : AllPlayers.entrySet()){
                if(e.getValue() instanceof Player){
                    Vector3 winner = new Vector3(player.x,player.y+1,player.z);
                    Firework.start(winner,e.getValue());
                }
            }
        }
    }

    public void checkMember(){
        Player p = null;
        int count = 0;
        for (Map.Entry<String,Player> e : Players.entrySet()){
            if(e.getValue().getGamemode() == 2){
                p = e.getValue();
                count++;
            }
        }
        setGameDataAsInt("lifecount", count, false);
        if(count == 1){
            if(jobs.get(p) == 0){
                finish(1);
            }else if(jobs.get(p) == 1){
                finish(2, p);
            }
        }else if(count == 0){
            reset();
            if(isHome) Home.finish(number);
        }
    }

    public void misskill(Player player, Player entity){
        lastcantpickup.put(player,  System.currentTimeMillis());
        if(havegun.get(player) == true){
            Vector3 motion = player.getDirectionVector().multiply(0.4);
            player.level.dropItem(player.add(0, 1.3, 0), Weapons.getItemGun(player), motion, 40);
            havegun.put(player,false);
        }
        if(Inventorys.data2.get(player) instanceof BystanderInventory) Inventorys.data2.get(player).register(player);
        Stat.setMurderInnocentsShot(player, Stat.getMurderInnocentsShot(player) + 1);
        Effect effect = Effect.getEffect(15);
        effect.setDuration(5*20);
        effect.setAmplifier(0);
        player.addEffect(effect);
        for (Map.Entry<String,Player> e : Players.entrySet()){
            e.getValue().sendMessage(Main.getMessage(e.getValue(),"murder.misskill",new String[]{color.get(player)+randomname.get(player),color.get(entity)+randomname.get(entity)}));
        }
        int karma = 0;
        if(Stat.getMurderMisskills(player) >= 2){
            karma = (Stat.getMurderMisskills(player)-1)*60;
            if(karma >= 300) karma = 300;
            player.sendMessage(Main.getMessage(player,"status.remove.karma",new String[]{String.valueOf(karma)}));
            Stat.setMurderKarma(player,Stat.getMurderKarma(player)-karma);
            loseKarmaCount.put(player, loseKarmaCount.get(player) + 1);
        }
        Stat.setMurderMisskills(player,Stat.getMurderMisskills(player)+1);
    }

    public void death(Player player,Player damager){
        if(player.getGamemode() == 0) return;
        if(jobs.get(player) == 1 && havegun.get(player) == true){
            Vector3 motion = player.getDirectionVector().multiply(0.4);
            player.level.dropItem(player.add(0, 1.3, 0), Weapons.getItemGun(player), motion, 40);
        }
        if(jobs.get(player) == 1){
            Stat.setMurderBystanderDeaths(player, Stat.getMurderBystanderDeaths(player) + 1);
        }else if(jobs.get(player) == 0){
            Stat.setMurderMurderDeaths(player, Stat.getMurderMurderDeaths(player) + 1);
        }
        player.setSubtitle(Main.getMessage(player,"murder.death.sub" ,new String[]{color.get(damager)+randomname.get(damager)}));
        player.sendTitle(Main.getMessage(player,"murder.death"));
        canpickup.put(player,false);
        setGameDataAsInt("deadcount", getGameDataAsInt("deadcount") + 1, false);
        Effect effect = Effect.getEffect(15);
        effect.setDuration(5*20);
        effect.setAmplifier(0);
        player.addEffect(effect);
        player.setGamemode(0);
        player.setAllowFlight(true);
        player.getInventory().clearAll();
        player.setSkin(bskin.get(player));
        player.getFoodData().setFoodLevel(20);
        Popups.setData(player, new MurderDeathPopup(player));
        Inventorys.setData(player,new MurderDeathInventory());
        ContainerSetContentPacket containerSetContentPacket = new ContainerSetContentPacket();
        containerSetContentPacket.windowid = ContainerSetContentPacket.SPECIAL_CREATIVE;
        containerSetContentPacket.slots = new Item[]{};
        player.dataPacket(containerSetContentPacket);
        UUID uuid = UUID.randomUUID();
        Long eid = Entity.entityCount++;
        HashMap<PlayerData ,HashMap<String, String>> map;
        HashMap<String, String> mapp = new HashMap<String, String>();
        map = humandata;
        mapp.put("eid",eid.toString());
        eiddata.put(eid, 0);
        mapp.put("uuid",uuid.toString());
        Vector3 ppp = player.getLevel().getSafeSpawn(player);//TODO
        ppp.x = Math.floor(ppp.x);
        ppp.y = Math.floor(ppp.y);
        ppp.z = Math.floor(ppp.z);
        mapp.put("pos",(int)ppp.x+":"+(int)ppp.y+":"+(int)ppp.z);
        mapp.put("name",player.getNameTag());
        mapp.put("color",color.get(player));
        PlayerData playerData = PlayerDataManager.getPlayerData(player);
        humandata.put(playerData, mapp);
        HashMap<String, HashMap<String, Item>> map2;
        HashMap<String, Item> mapp2 = new HashMap<String, Item>();
        mapp2.put("helmet",helmet.get(player));
        mapp2.put("leggings",leggings.get(player));
        mapp2.put("chestplate",chestplate.get(player));
        mapp2.put("boots",boots.get(player));
        humanitem.put(playerData, mapp2);
        HashMap<PlayerData, Skin> map3;
        map3 = humanskin;
        map3.put(playerData, askin.get(player));
        AddPlayerPacket pk = new AddPlayerPacket();
        pk.entityUniqueId = eid;
        pk.entityRuntimeId = eid;
        pk.uuid = uuid;
        pk.username = "death";
        pk.x = (float)ppp.x;
        pk.y = (float)ppp.y;
        pk.z = (float)ppp.z;
        pk.speedX = 0;
        pk.speedY = 0;
        pk.speedZ = 0;
        pk.yaw = 0;
        pk.pitch = 0;
        int flags = 0;
        flags ^= 1 << 1;
        pk.metadata = new EntityMetadata()
                .putString(Entity.DATA_NAMETAG, "")
                .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1)
                .putByte(Player.DATA_PLAYER_FLAGS,flags)
                .put(new IntPositionEntityData(29,(int)ppp.x,(int)ppp.y,(int)ppp.z));
        PlayerListPacket pkk = new PlayerListPacket();
        pkk.type = PlayerListPacket.TYPE_ADD;
        String name = uuid.toString();
        pkk.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(uuid, eid, name,askin.get(player))};
        PlayerListPacket pkkk = new PlayerListPacket();
        pkkk.type = PlayerListPacket.TYPE_REMOVE;
        pkkk.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(uuid)};
        MovePlayerPacket pkkkk = new MovePlayerPacket();
        pkkkk.eid = eid;
        pkkkk.x = (float)((int)ppp.x);
        pkkkk.y = (float)((int)ppp.y+0.5);
        pkkkk.z = (float)((int)ppp.z);
        pkkkk.yaw = 0;
        pkkkk.pitch = 0;
        pkkkk.mode = MovePlayerPacket.MODE_NORMAL;
        MobArmorEquipmentPacket pkkkkk = new MobArmorEquipmentPacket();
        pkkkkk.eid = eid;
        pkkkkk.slots = new Item[]{helmet.get(player),chestplate.get(player),leggings.get(player),boots.get(player)};
        int count = 0;
        for (Map.Entry<String,Player> e : Players.entrySet()){
            e.getValue().hidePlayer(player);
            e.getValue().dataPacket(pkk);
            e.getValue().dataPacket(pk);
            //e.getValue().dataPacket(pkkk);
            e.getValue().dataPacket(pkkkk);
            e.getValue().dataPacket(pkkkkk);
            if(e.getValue().getGamemode() == 2){
                count++;
            }else if(e.getValue().getGamemode() == 1){
                if(ServerChestInventorys.isOpen.containsKey(e.getValue()) && ServerChestInventorys.isOpen.get(e.getValue())){
                    ServerChestInventorys.data2.get(e.getValue()).update();
                }
            }
        }
        for (Map.Entry<String,Player> e : Players.entrySet()){
            if(jobs.get(e.getValue()) == 0){
                count -= 1;
                if(count - 1 < 0) count = 0;
                e.getValue().setAttribute(Attribute.getAttribute(Attribute.EXPERIENCE_LEVEL).setValue(count));
                e.getValue().setAttribute(Attribute.getAttribute(Attribute.EXPERIENCE).setValue(1));
            }
        }
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackHuman(this, pkkkk),2);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackHuman(this, pkkk),2);
        player.getLevel().addParticle(new DestroyBlockParticle(player,Block.get(152,0)));
    }

    public void HumanMove(DataPacket pk){
        for (Map.Entry<String,Player> e : Players.entrySet()){
            e.getValue().dataPacket(pk);
        }
    }

    public Player[] getChangeMurder(){
        Player murder = null;
        int all = 0;
        int min  = 999;
        int ms = 0;
            for(Map.Entry<String,Player> e : Players.entrySet()){
                if(e.getValue().getGamemode() == 2 && e.getValue() instanceof Player){
                    if(min >= Stat.getMurderKarma(e.getValue())){
                        min = Stat.getMurderKarma(e.getValue());
                    }
                }
            }
        ms = 10 - min;
        HashMap<Player,Integer> map = new HashMap<Player,Integer>();
            for(Map.Entry<String,Player> e : Players.entrySet()){
                if(e.getValue().getGamemode() == 2 && e.getValue() instanceof Player){
                    map.put(e.getValue(),Stat.getMurderKarma(e.getValue())+ms);
                    all += (Stat.getMurderKarma(e.getValue())+ms);
                }
            }
        int rand = (int)(Math.random() * all);
        int c = 0;
            for(Map.Entry<Player,Integer> e : map.entrySet()){
                if(rand >= c && rand <= (c + (int)e.getValue()) && !(murder instanceof Player)){
                    murder = e.getKey();
                }
                c += (int)e.getValue();
            }
        return new Player[]{murder};
    }

    public void changeMurder(){
        for(Map.Entry<String,Player> e : AllPlayers.entrySet()){
            e.getValue().sendMessage(Main.getMessage(e.getValue(), "murder.murder.quit"));
        }
        Player[] murder = getChangeMurder();
        registerMurder(murder[0]);
        playerdata.get(murder[0]).put("job","0");
        checkMember();
    }

    public String[] getRandomName(){
        String[] names = new String[]{};
        String[] name = new String[]{"Dominus","SkythekidRS","Oracle","DarkTwister","BdoubleO100","AquasArt","GalaxySttars",
        "TheTamedSlime","Flutsmeister","YoshiTheGamerYT","XiNFiNiTYXD","Dizbey","PopCultures",
        "_BlazingPvP_","Animal_Lover_04","Arujann","Colin_Rocks","crafter8769","emotionaI","EnderAssassin",
        "Evil_Farts","kitty62319","MarleyTF","NuclearMars","onsies_are_life","sarbearmines","SwiftHop",
        "TheFakePinkPony","_HypedAnxiety","NashGamingYT","Pickledoodle","JnicornSwagg",
        "EckoCraft","Kit_Kat650","Calico_Kat","Hoshidan","Gekryptex","RocksAreHumans","AtlanticFrost",
        "Sp00pyLoo","PinkPixelPower","Arujann","Commander_vespa","MrTiglet","EddySaurus101","Holga947","legominecraft07",
        "Opalmmur","rainbowx1994","stevens2453","Stqrbucksss","CakeInducedComa","VanillaHasNoLife",
        "Alex_Is_lyf","DragonOfTime64","PerfectIntention","TheMineKing504","Jackairborn","OpTicBearYT",
        "UniverseMacker","Video_Gamer","SweetGrimHD","Inzuki","Chasn555","Ticlin45","CaptainSparklez",
        "thijsmonster","Aalis","TommasoC04","HoloYolo","KiwiChibi","mister112","BeanPlaysMC",
        "snowbuttie","Dead_Zone866","ZombieGamer63","_xEatinGx_","FireGirl_284","kamikaze83",
        "iiDuffleDuck"};
        List<String> list=Arrays.asList(name);
        Collections.shuffle(list);
        names =(String[])list.toArray(new String[list.size()]);
        return names;
    }

    public String[] getRandomColor(){
        String[] colors = new String[]{"1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};
        List<String> list=Arrays.asList(colors);
        Collections.shuffle(list);
        String[] result =(String[])list.toArray(new String[list.size()]);
        return result;
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
                    if(player.getGamemode() == 2 && !Setting.getChatHide(e.getValue())){
                        e.getValue().sendMessage("<"+player.getNameTag()+"§f> "+event.getMessage());
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

    public void attack(Player entity, Player damager, EntityDamageEvent event){
        if(Players.containsKey(entity.getName()) && Players.containsKey(damager.getName()) && jobs.containsKey(entity) && jobs.containsKey(damager)){
            if(jobs.get(damager) == 0 && jobs.get(entity) == 1){
                int id = damager.getInventory().getItemInHand().getId();
                if(id == 268 || id == 272 || id == 267 || id == 283 || id == 276){
                    if(getGameDataAsBoolean("gamenow2")){
                        Stat.setMurderMurderKills(damager, Stat.getMurderMurderKills(damager) + 1);
                        stabbingCount.put(damager, stabbingCount.get(damager) + 1);
                        death(entity, damager);
                        checkMember();
                    }
                }
            }
        }
    }

    public void attack2(DataPacketReceiveEvent event){
        Player player = event.getPlayer();
        DataPacket packet = event.getPacket();
        if(packet instanceof InteractPacket){
            Long eid = ((InteractPacket)packet).target;
            if(jobs.containsKey(player) && jobs.get(player) == 0){
                Item item = player.getInventory().getItemInHand();
                int id = item.getId();
                if(id == 351 && item.getDamage() == 13){
                    for (Map.Entry<PlayerData, String> e : namelist.entrySet()){
                        if(humandata.containsKey(e.getKey()) && humandata.get(e.getKey()).get("eid").equals(eid.toString()) && !eid.equals(0)){
                            emeralds.put(player, emeralds.get(player) - 3);
                            if(emeralds.get(player) >= 3){
                                onemurder.put(player, 13);
                            }else{
                                onemurder.put(player, 8);
                            }
                            if(emeralds.get(player) >= 5){
                                twomurder.put(player, 10);
                            }else{
                                twomurder.put(player, 8);
                            }
                            if(Inventorys.data2.get(player) instanceof MurderInventory) Inventorys.data2.get(player).register(player);
                            UUID uuid = UUID.fromString(humandata.get(e.getKey()).get("uuid"));
                            RemoveEntityPacket pk = new RemoveEntityPacket();
                            pk.eid = eid;
                            PlayerListPacket pkk = new PlayerListPacket();
                            pkk.type = PlayerListPacket.TYPE_ADD;
                            String name = uuid.toString();
                            pkk.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(uuid, eid, name,askin.get(player))};
                            PlayerListPacket pkkk = new PlayerListPacket();
                            pkkk.type = PlayerListPacket.TYPE_REMOVE;
                            pkkk.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(uuid)};
                            MobArmorEquipmentPacket pkkkkk = new MobArmorEquipmentPacket();
                            pkkkkk.eid = eid;
                            pkkkkk.slots = new Item[]{helmet.get(player),chestplate.get(player),leggings.get(player),boots.get(player)};
                            AddPlayerPacket pkkkk = new AddPlayerPacket();
                            pkkkk.entityUniqueId = eid;
                            pkkkk.entityRuntimeId = eid;
                            pkkkk.uuid = uuid;
                            pkkkk.username = "nightmare38382";
                            int hx = Integer.parseInt(humandata.get(e.getKey()).get("pos").split(":")[0]);
                            int hy = Integer.parseInt(humandata.get(e.getKey()).get("pos").split(":")[1]);
                            int hz = Integer.parseInt(humandata.get(e.getKey()).get("pos").split(":")[2]);
                            pkkkk.x = (float)hx;
                            pkkkk.y = (float)hy;
                            pkkkk.z = (float)hz;
                            pkkkk.speedX = 0;
                            pkkkk.speedY = 0;
                            pkkkk.speedZ = 0;
                            pkkkk.yaw = 0;
                            pkkkk.pitch = 0;
                            int flags = 0;
                            flags ^= 1 << 1;
                            pkkkk.metadata = new EntityMetadata()
                                    .putString(Entity.DATA_NAMETAG, "")
                                    .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1)
                                    .putByte(Player.DATA_PLAYER_FLAGS,flags)
                                    .put(new IntPositionEntityData(29,(int)hx,(int)hy,(int)hz));
                            MovePlayerPacket pkkkkkk = new MovePlayerPacket();
                            pkkkkkk.eid = eid;
                            pkkkkkk.x = (float)hx;
                            pkkkkkk.y = (float)(hy+0.5);
                            pkkkkkk.z = (float)hz;
                            pkkkkkk.yaw = 0;
                            pkkkkkk.pitch = 0;
                            pkkkkkk.mode = MovePlayerPacket.MODE_NORMAL;
                            Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackHuman(this, pkkkkkk),2);
                            Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackHuman(this, pkkk),3);
                            chestplate.put(player,humanitem.get(e.getKey()).get("chestplate"));
                            helmet.put(player,humanitem.get(e.getKey()).get("helmet"));
                            leggings.put(player,humanitem.get(e.getKey()).get("leggings"));
                            boots.put(player,humanitem.get(e.getKey()).get("boots"));
                            askin.put(player,humanskin.get(e.getKey()));
                            disguisedCount.put(player, disguisedCount.get(player) + 1);
                            HashMap<String, String> Identity = new HashMap<String, String>();
                            Identity.put("name", humandata.get(e.getKey()).get("name"));
                            Identity.put("color", humandata.get(e.getKey()).get("color"));
                            disguisedIdentity.get(player).put(disguisedCount.get(player), Identity);
                            MurderAchievements.setLevel(player, "Not_So_Secret_Identity", 2);
                            player.setNameTag(humandata.get(e.getKey()).get("name"));
                            player.sendMessage(Main.getMessage(e.getKey().getName(),"murder.secret.name",new String[]{humandata.get(e.getKey()).get("name")}));
                            player.setSkin(humanskin.get(e.getKey()));
                            Inventorys.data2.get(player).register(player);
                            for (Map.Entry<String,Player> ee : Players.entrySet()){
                                if(ee.getValue() instanceof Player){
                                    if(player instanceof Player){
                                        ee.getValue().hidePlayer(player);
                                        ee.getValue().showPlayer(player);
                                    }
                                    ee.getValue().dataPacket(pk);
                                    ee.getValue().dataPacket(pkkkk);
                                    ee.getValue().dataPacket(pkk);
                                    //ee.getValue().dataPacket(pkkk);
                                    ee.getValue().dataPacket(pkkkkk);
                                    if(ee.getValue().getGamemode() == 0){
                                        Inventorys.data2.get(ee.getValue()).register(ee.getValue());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean canSeeNameTag(Player player, Player target){
        double a = target.x;
        double b = target.y + target.getEyeHeight();
        double c = target.z;
        double mx = a - player.x;
        double mz = c - (player.z + player.getEyeHeight());
        double my = b - player.y;
        double mxz = Math.sqrt(mx * mx + mz * mz);
        double yaw = getYaw(mx, mz);
        double pitch = getPitch(mxz, my);
        double MX = -Math.sin(yaw / 180 * Math.PI) * Math.cos(pitch / 180 * Math.PI) * 0.2;
        double MZ = Math.cos(yaw / 180 * Math.PI) * Math.cos(pitch / 180 * Math.PI) * 0.2;
        double MY = -Math.sin(pitch / 180 * Math.PI) * 0.2;
        Position pos = new Position(player.x, player.y + player.getEyeHeight(), player.z);
        HashMap<String, Boolean> isGot = new HashMap<String, Boolean>();
        double dis = player.distance(target);
        while(true){
            pos.x += MX;
            pos.y += MY;
            pos.z += MZ;
            int x = (int)Math.floor(pos.x);
            int y = (int)Math.floor(pos.y);
            int z = (int)Math.floor(pos.z);
            if(!isGot.containsKey(x+":"+y+":"+z)){
                if(isInvisibleBlock(world.getBlockIdAt(x,y,z))){
                    return false;
                }
                isGot.put(x+":"+y+":"+z, true);
            }
            if(pos.distance(player) >= dis) return true;
        }
    }

    public boolean isInvisibleBlock(int id){
        switch(id){
            case 0:
            case 6:
            case 8:
            case 9:
            case 10:
            case 11:
            case 18:
            case 20:
            case 27:
            case 28:
            case 30:
            case 31:
            case 32:
            case 37:
            case 38:
            case 39:
            case 40:
            case 50:
            case 51:
            case 55:
            case 59:
            case 65:
            case 66:
            case 69:
            case 70:
            case 72:
            case 75:
            case 76:
            case 77:
            case 78:
            case 83:
            case 85:
            case 93:
            case 94:
            case 95:
            case 96:
            case 101:
            case 102:
            case 104:
            case 105:
            case 106:
            case 107:
            case 111:
            case 113:
            case 115:
            case 127:
            case 131:
            case 132:
            case 140:
            case 141:
            case 142:
            case 143:
            case 147:
            case 148:
            case 160:
            case 199:
            case 208:
            case 244:
                return false;
            default:
                return true;
        }
    }

    public static double getYaw(double mx,double mz) {
        double yaw = 0;
        if (mz == 0) {
            if (mx < 0) {
                yaw = -90;
            }else {
                yaw = 90;
            }
        }else {
            if (mx >= 0 && mz > 0) {
                double atan = Math.atan(mx/mz);
                yaw = rad2deg(atan);
            }else if (mx >= 0 && mz < 0) {
                double atan = Math.atan(mx/Math.abs(mz));
                yaw = 180 - rad2deg(atan);
            }else if (mx < 0 && mz < 0) {
                double atan = Math.atan(mx/mz);
                yaw = -(180 - rad2deg(atan));
            }else if (mx < 0 && mz > 0) {
                double atan = Math.atan(Math.abs(mx)/mz);
                yaw = -(rad2deg(atan));
            }
        }

        yaw = - yaw;
        return yaw;
    }

    public static double getPitch(double mxz,double my) {
        double pitch = 0;
        if(my == 0){
            pitch = 0;
        }else {
            if (my > 0 && mxz > 0) {
                double atan = Math.atan(mxz/my);
                pitch = -(rad2deg(atan) - 90);
            }else if (my < 0 && mxz > 0) {
                double atan = Math.atan(Math.abs(mxz)/my);
                pitch = -(rad2deg(atan)) - 90;
            }
        }
        pitch = - pitch;
        return pitch;
    }

    public static double rad2deg(double radian) {
        return radian * (180f / Math.PI);
    }

    public void move(Player player){
        if(Players.containsKey(player.getName()) && getGameDataAsBoolean("gamenow2")){
            for (Map.Entry<String,Player> e : Players.entrySet()){
                if(jobs.get(e.getValue()) == 0 && jobs.get(player) != 0 && player.getGamemode() == 2){
                    LevelEventPacket pk = new LevelEventPacket();
                    pk.evid = LevelEventPacket.EVENT_ADD_PARTICLE_MASK | Particle.TYPE_DRIP_LAVA;
                    pk.x = (float) player.x;
                    pk.y = (float) (player.y+0.5);
                    pk.z = (float) player.z;
                    e.getValue().dataPacket(pk);
                }
                if(!player.equals(e.getValue())){
                    if(canSeeNameTag(player, e.getValue())){
                        SetEntityDataPacket pk = new SetEntityDataPacket();
                        pk.eid = player.getId();
                        long flags = player.getDataPropertyLong(Entity.DATA_FLAGS);
                        long flagss = player.getDataPropertyLong(Entity.DATA_FLAGS);
                        flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;
                        flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
                        pk.metadata = player.getDataProperties()
                                .putLong(Entity.DATA_FLAGS,flags);
                        e.getValue().dataPacket(pk);
                        player.getDataProperties().putLong(Entity.DATA_FLAGS,flagss);
                        SetEntityDataPacket pkk = new SetEntityDataPacket();
                        pkk.eid = e.getValue().getId();
                        flags = e.getValue().getDataPropertyLong(Entity.DATA_FLAGS);
                        flagss = e.getValue().getDataPropertyLong(Entity.DATA_FLAGS);
                        flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;
                        flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
                        pkk.metadata = e.getValue().getDataProperties()
                                .putLong(Entity.DATA_FLAGS,flags);
                        player.dataPacket(pkk);
                        e.getValue().getDataProperties().putLong(Entity.DATA_FLAGS,flagss);
                    }else{
                        player.sendData(e.getValue());
                        e.getValue().sendData(player);
                    }
                }
            }
        }
    }

    public HashMap<PlayerData, HashMap<String, String>> humandata = new HashMap<PlayerData, HashMap<String, String>>();
    public HashMap<PlayerData, HashMap<String, Item>> humanitem = new HashMap<PlayerData, HashMap<String, Item>>();
    public HashMap<PlayerData, Skin> humanskin = new HashMap<PlayerData, Skin>();
    public HashMap<Long, Integer> eiddata = new HashMap<Long, Integer>();
    public int nexteiddata = 0;
    public HashMap<Player, HashMap<String, String>> playerdata = new HashMap<Player, HashMap<String, String>>();
    public HashMap<PlayerData, String> namelist = new HashMap<PlayerData, String>();
    public MurderStage stage;
    public MurderStage[] stages = new MurderStage[]{};
    public HashMap<Player, Integer> vote = new HashMap<Player, Integer>();
    public int countplayer = 0;
    public HashMap<Player, Integer> jobs = new HashMap<Player, Integer>();
    public HashMap<Player, Integer> emeralds = new HashMap<Player, Integer>();
    public HashMap<Player, Integer> onemurder = new HashMap<Player, Integer>();
    public HashMap<Player, Integer> twomurder = new HashMap<Player, Integer>();
    public HashMap<Player, Boolean> havegun = new HashMap<Player, Boolean>();
    public HashMap<Player, Integer> haveknife = new HashMap<Player, Integer>();
    public HashMap<Player, Boolean> defgun = new HashMap<Player, Boolean>();
    public HashMap<Player, String> randomname = new HashMap<Player, String>();
    public HashMap<Player, String> color = new HashMap<Player, String>();
    public MurderEmeraldManager emerald;
    public HashMap<Player, Boolean> canpickup = new HashMap<Player, Boolean>();
    public HashMap<Player, Long> lastcantpickup = new HashMap<Player, Long>();
    public HashMap<Player, Integer> knifecount = new HashMap<Player, Integer>();
    public HashMap<Player, Item> chestplate = new HashMap<Player, Item>();
    public HashMap<Player, Item> helmet = new HashMap<Player, Item>();
    public HashMap<Player, Item> leggings = new HashMap<Player, Item>();
    public HashMap<Player, Item> boots = new HashMap<Player, Item>();
    public HashMap<Player, Skin> bskin = new HashMap<Player, Skin>();
    public HashMap<Player, Skin> askin = new HashMap<Player, Skin>();
    public HashMap<Player, Integer> disguisedCount = new HashMap<Player, Integer>();
    public HashMap<Player, HashMap<Integer, HashMap<String, String>>> disguisedIdentity = new HashMap<Player, HashMap<Integer, HashMap<String, String>>>();
    /////////////////////////////////////////////For Achievements
    public HashMap<Player, Integer> loseKarmaCount = new HashMap<Player, Integer>();
    public HashMap<Player, Integer> stabbingCount = new HashMap<Player, Integer>();
    public File file;
    public MurderStage[] stagelist;
    public static MurderStage[] stagelist2;
}
class CallbackTime extends Task{

    public Murder owner;

    public CallbackTime(Murder owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.Time();
    }
}
class CallbackGameTime extends Task{

    public Murder owner;

    public CallbackGameTime(Murder owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.GameTime();
    }
}
class CallbackFinish extends Task{

    public Murder owner;

    public CallbackFinish(Murder owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.finishLater();
    }
}
class CallbackHuman extends Task{

    public Murder owner;
    public DataPacket pk;

    public CallbackHuman(Murder owner, DataPacket pk){
        this.owner = owner;
        this.pk = pk;
    }

    public void onRun(int d){
        this.owner.HumanMove(pk);
    }
}
class CallbackFinishTimeover extends Task{

    public Murder owner;

    public CallbackFinishTimeover(Murder owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.finishLaterTimeover();
    }
}
class CallbackFinishKillall extends Task{

    public Murder owner;

    public CallbackFinishKillall(Murder owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.finishLaterKillall();
    }
}
class CallbackFinishKillmurder extends Task{

    public Murder owner;
    public Player winner;

    public CallbackFinishKillmurder(Murder owner, Player winner){
        this.owner = owner;
        this.winner = winner;
    }

    public void onRun(int d){
        this.owner.finishLaterKillmurder(this.winner);
    }
}
class CallbackFirework extends Task{

    public Murder owner;
    public Player player;

    public CallbackFirework(Murder owner,Player player){
        this.owner = owner;
        this.player = player;
    }

    public void onRun(int d){
        this.owner.fireworkRun(this.player);
    }
}