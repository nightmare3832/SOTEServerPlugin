package sote.bedwars;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.entity.item.EntityPrimedTNT;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.sound.TNTPrimeSound;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.network.protocol.SetEntityDataPacket;
import cn.nukkit.scheduler.Task;
import sote.Game;
import sote.GameProvider;
import sote.Main;
import sote.PlayerData;
import sote.PlayerDataManager;
import sote.TeamUtil;
import sote.bedwars.finalkilleffect.BedwarsFinalkillEffects;
import sote.bedwars.stage.BedwarsStage;
import sote.bedwars.stage.Library;
import sote.event.Event_EntityDamageEvent;
import sote.home.Home;
import sote.inventory.Inventorys;
import sote.inventory.ServerChestInventorys;
import sote.inventory.bedwars.BedwarsInventory;
import sote.inventory.bedwars.BedwarsItemShopChestInventory;
import sote.inventory.bedwars.BedwarsRespawningInventory;
import sote.inventory.bedwars.BedwarsUpgraderChestInventory;
import sote.inventory.home.HomeInventory;
import sote.inventory.lobby.StartInventory;
import sote.party.Party;
import sote.popup.LobbyPopup;
import sote.popup.Popups;
import sote.popup.bedwars.BedwarsGamePopup;
import sote.setting.Setting;
import sote.stat.Stat;

public class Bedwars extends Game{

    public static final int MAX_PLAYERS = 15;
    public static final int MIN_PLAYERS = 1;
    public static final int VIP_PLAYERS = 15;

    public static final int WAIT_TIME = 10;
    public static final int WAIT_HOME_TIME = 30;
    public static final int GAME_TIME = 400;
    public static final int STAGE_SET_TIME = 10;

    public final int FINISH_TYPE_ALL_KILL = 0;
    public final int FINISH_TYPE_TIMEOVER = 1;

    public static final int MODE_SOLO = 0;
    public static final int MODE_DOUBLES = 1;
    public static final int MODE_THREES = 2;
    public static final int MODE_FOURS = 3;

    public static final int ARMOR_LETHER = 0;
    public static final int ARMOR_CHAIN = 1;
    public static final int ARMOR_IRON = 2;
    public static final int ARMOR_DIAMOND = 3;

    public Bedwars(int number, boolean isHome){
        super(number, isHome);
        new BedwarsSignManager();
        stage = new Library();
        //stagelist = new MurderStage[]{new WildernessTowns(),new THEStrangeMansion(),new ObsoleteMine()};
        //stagelist2 = new MurderStage[]{new WildernessTowns(),new THEStrangeMansion(),new ObsoleteMine()};
        addRoom();
        for(int t = 0;t < 16;t++){
            shopKeeperEid[t] = Entity.entityCount++;
        }
        for(int t = 0;t < 8;t++){
            bedNameTag[t] = Entity.entityCount++;
        }
        for(int t = 0;t < 8;t++){
            bedItem[t] = Entity.entityCount++;
        }
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
        //BedwarsSignManager.updataSign(number);
        String url = Server.getInstance().getDataPath()+"/worlds/bedwars";
        File newdir = new File(url+number);
        newdir.mkdir();
        File newdir2 = new File(url+number+"/region");
        newdir2.mkdir();
        directoryCopy(new File(url),new File(url+number));
        Server.getInstance().loadLevel("bedwars"+number);
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
        team.clear();
        placedBlocks.clear();
        isBedBroken.clear();
        isEliminated.clear();
        isTeamEliminated.clear();
        canOpenChest = true;
        setting = new HashMap<String, String>();
        setting.put("map", "default");
        BedwarsSignManager.updataSign(number);
    }

    public int getMode(){
        return this.mode;
    }

    @Override
    public void setSetting(String key, String value){
        setting.put(key, value);
        if(key.equals("map") && !setting.get("map").equals("default")){
            BedwarsStage map = null;
            switch(setting.get("map")){
                case "Library":
                    map = new Library();
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
        String ff = f.replace("bedwars/","");
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
            death(player, false, true);
        }
        if(number <= 7){
            player.teleport(Server.getInstance().getLevelByName("lobby").getSafeSpawn());
            //Inventorys.setData(player,new MurderLobbyInventory());
        }else{
             player.teleport(Server.getInstance().getLevelByName("home"+Home.getId(number)).getSafeSpawn());
             //Inventorys.setData(player,new HomeInventory());
        }
        player.setDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG, true);
        player.setDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_CAN_SHOW_NAMETAG, true);
        Popups.setData(player, new LobbyPopup(player));
        Players.remove(player.getName());
        AllPlayers.remove(player.getName());
        Main.gamenow.put(player, false);
        GameProvider.quitGame(player);
    }

    @Override
    public void LookQuit(Player player){
        if(!Players.containsValue(player)) return;
        if(number <= 7){
            player.teleport(Server.getInstance().getLevelByName("lobby").getSafeSpawn());
            //Inventorys.setData(player,new MurderLobbyInventory());
        }else{
            player.teleport(Server.getInstance().getLevelByName("home"+Home.getId(number)).getSafeSpawn());
            //Inventorys.setData(player,new HomeInventory());
        }
        Spectators.remove(player.getName());
        AllPlayers.remove(player.getName());
        Main.gamenow.put(player, false);
    }

    public void Teleport(Player player){
        Level level = Server.getInstance().getLevelByName("bedwars"+number);
        player.teleport(new Position(1000,52,1000,level));
    }

    public void startTime(){
        if(number <= 7){
            setGameDataAsInt("time", WAIT_TIME, true);
        }else{
            setGameDataAsInt("time", WAIT_HOME_TIME, true);
        }
        for (Map.Entry<String,Player> e : Players.entrySet()){
            e.getValue().sendTip(Main.getMessage(e.getValue(),"bedwars.wait.count",new String[]{String.valueOf(getGameDataAsInt("time"))}));
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
            sendTip(e.getValue(), Main.getMessage(e.getValue(),"bedwars.wait.count",new String[]{String.valueOf(getGameDataAsInt("time"))}), 1);
        }
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackTime(this), 20);
    }

    public void Start(){
        if(getGameDataAsInt("count") < MIN_PLAYERS){
            setGameDataAsBoolean("time", false, true);
            setGameDataAsInt("time", WAIT_TIME, true);
            if(number >= 8) Home.notStart(number);
            return;
        }
        int c = 0;
        world = Server.getInstance().getLevelByName("bedwars"+number);
        BedwarsShopKeeper.spawnShopKeeper(number);
        spawnGenerator();
        countplayer = getGameDataAsInt("count");
        canOpenChest = true;
        registerForge();
        startEventTimer();
        setGameDataAsBoolean("gamenow2", true, true);
        c = 8;
        if(getMode() == MODE_THREES || getMode() == MODE_FOURS) c = 4;
        for(int t = 0; t < c;t++){
            this.isBedBroken.put(t, false);
            this.isTeamEliminated.put(t, false);
            this.upgraders.put(t, new BedwarsUpgrader(this, t));
        }
        c = 0;
        int testTeam = 0;
        for (Map.Entry<String,Player> e : Players.entrySet()){
            Main.setHide.put(e.getValue(),false);
            this.isEliminated.put(PlayerDataManager.getPlayerData(e.getValue()), false);
            e.getValue().setGamemode(0);
            e.getValue().setHealth(20);
            killcount.put(PlayerDataManager.getPlayerData(e.getValue()), 0);
            this.team.put(PlayerDataManager.getPlayerData(e.getValue()), testTeam);
            spawnBedNameTag(e.getValue());
            this.isHaveShears.put(PlayerDataManager.getPlayerData(e.getValue()), false);
            this.haveArmor.put(PlayerDataManager.getPlayerData(e.getValue()), ARMOR_LETHER);
            Vector3[] pos = getTeamDataByNumber(team.get(PlayerDataManager.getPlayerData(e.getValue())));
            //e.getValue().teleport(new Position(pos[c].x, pos[c].y, pos[c].z, world));
            e.getValue().teleport(pos[0]);
            Inventorys.setData(e.getValue(), new BedwarsInventory());
            Popups.setData(e.getValue(), new BedwarsGamePopup(e.getValue()));
            //Stat.setBedwarsPlays(e.getValue(), Stat.getBedwarsPlays(e.getValue()) + 1);
            testTeam++;
        }
        c = 0;
        for (Map.Entry<String,Player> e : Spectators.entrySet()){
            Main.setHide.put(e.getValue(),false);
            for (Map.Entry<String,Player> ee : Players.entrySet()){
                ee.getValue().hidePlayer(e.getValue());
            }
            e.getValue().setGamemode(0);
            e.getValue().setAllowFlight(true);
            e.getValue().getInventory().clearAll();
            //Inventorys.setData(e.getValue(),new MurderDeathInventory());
            //e.getValue().teleport(new Position(pos[c].x, pos[c].y, pos[c].z, world));
            c++;
        }
        setGameDataAsBoolean("gamenow", true, true);
        for(Entity ent : world.getEntities()){
            if(ent instanceof EntityItem){
                ent.kill();
            }
        }
    }

    public void spawnGenerator(){
        Vector3[] pos = this.stage.getDiamondGeneratorPosition();
        generatorsD = new BedwarsGenerator[pos.length];
        int c = 0;
        for(Vector3 p : pos){
            generatorsD[c] = new BedwarsGenerator(this, p, BedwarsGenerator.GENERATOR_DIAMOND);
            c++;
        }
        Vector3[] pos2 = this.stage.getEmeraldGeneratorPosition();
        generatorsE = new BedwarsGenerator[pos.length];
        int c2 = 0;
        for(Vector3 p : pos2){
            generatorsE[c2] = new BedwarsGenerator(this, p, BedwarsGenerator.GENERATOR_EMERALD);
            c2++;
        }
    }

    public void registerForge(){
        int c = 8;
        if(getMode() == MODE_THREES || getMode() == MODE_FOURS) c = 4;
        for(int t = 0; t < c;t++){
            BedwarsForge[] forges = new BedwarsForge[getMode() + 1];
            Vector3[] pos = getTeamDataByNumber(t);
            for(int a = 0;a < getMode() + 1;a++){
                forges[a] = new BedwarsForge(this, pos[2 + a]);
            }
            forge.put(t, forges);
        }
    }

    public void stopAllGenerators(){
        for(BedwarsGenerator g : generatorsD){
            g.stopGenerator();
        }
        for(BedwarsGenerator g : generatorsE){
            g.stopGenerator();
        }
    }

    public void reinforcedArmor(Player player){
        int level = this.upgraders.get(this.team.get(PlayerDataManager.getPlayerData(player))).levels.get(BedwarsUpgrader.UPGRADE_REINFORCED_ARMOR);
        if(level == 0) return;
        Enchantment ench = Enchantment.getEnchantment(Enchantment.ID_PROTECTION_ALL);
        ench.setLevel(level);
        Item item;
        
        item = player.getInventory().getHelmet();
        item.addEnchantment(ench);
        player.getInventory().setHelmet(item);
        
        item = player.getInventory().getChestplate();
        item.addEnchantment(ench);
        player.getInventory().setChestplate(item);
        
        item = player.getInventory().getLeggings();
        item.addEnchantment(ench);
        player.getInventory().setLeggings(item);
        
        item = player.getInventory().getBoots();
        item.addEnchantment(ench);
        player.getInventory().setBoots(item);
        
        player.getInventory().sendContents(player);
        player.getInventory().sendArmorContents(player);
    }

    public void finish(int type){
        if(type == 0){}
            for (Map.Entry<String,Player> e : Players.entrySet()){
                for (Map.Entry<String,Player> ee : Players.entrySet()){
                    ee.getValue().showPlayer(e.getValue());
                    e.getValue().showPlayer(ee.getValue());
                }
                e.getValue().setGamemode(3);
                Stat.setNameTag(e.getValue());
                e.getValue().sendMessage("finish!");
            }
        stopAllGenerators();
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
                e.getValue().setAttribute(Attribute.getAttribute(Attribute.MAX_HUNGER).setValue(20));
                e.getValue().setAttribute(Attribute.getAttribute(Attribute.EXPERIENCE).setValue(0));
                e.getValue().setAttribute(Attribute.getAttribute(Attribute.EXPERIENCE_LEVEL).setValue(0));
                e.getValue().setAllowFlight(false);
            }
        }
        for (Map.Entry<String,Player> e : Players.entrySet()){
            if(e.getValue() instanceof Player){
                if(number <= 7){
                    e.getValue().teleport(Server.getInstance().getLevelByName("lobby").getSafeSpawn());
                    Inventorys.setData(e.getValue(),new StartInventory());
                }else{
                    e.getValue().teleport(Server.getInstance().getLevelByName("home"+Home.getId(number)).getSafeSpawn());
                    Inventorys.setData(e.getValue(),new HomeInventory());
                }
                Popups.setData(e.getValue(), new LobbyPopup(e.getValue()));
                Main.gamenow.put(e.getValue(), false);
            }
        }
        reset();
        if(Server.getInstance().isLevelLoaded("bedwars"+number)) Server.getInstance().unloadLevel(Server.getInstance().getLevelByName("bedwars"+number));
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackWorld(this),40);
        if(number >= 8) Home.finish(number);
    }

    public void world(){
        String url = Server.getInstance().getDataPath()+"/worlds/bedwars";
        directoryCopy(new File(url),new File(url+number));
    }

    public void onPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();
        if(Players.containsValue(player)){
            Block block = event.getBlock();
            if(block.getId() == Block.TNT){
                player.getInventory().removeItem(Item.get(Item.TNT, 0, 1));
                event.setCancelled();
                double mot = (new NukkitRandom()).nextSignedFloat() * Math.PI * 2;
                CompoundTag nbt = new CompoundTag()
                        .putList(new ListTag<DoubleTag>("Pos")
                                .add(new DoubleTag("", block.x + 0.5))
                                .add(new DoubleTag("", block.y))
                                .add(new DoubleTag("", block.z + 0.5)))
                        .putList(new ListTag<DoubleTag>("Motion")
                                .add(new DoubleTag("", -Math.sin(mot) * 0.02))
                                .add(new DoubleTag("", 0.2))
                                .add(new DoubleTag("", -Math.cos(mot) * 0.02)))
                        .putList(new ListTag<FloatTag>("Rotation")
                                .add(new FloatTag("", 0))
                                .add(new FloatTag("", 0)))
                        .putByte("Fuse", 80);
                Entity tnt = new EntityPrimedTNT(
                        this.world.getChunk(block.getFloorX() >> 4, block.getFloorZ() >> 4),
                        nbt
                );
                Main.gameEntity.put(tnt, player);
                tnt.spawnToAll();
                this.world.addSound(new TNTPrimeSound(block));
                return;
            }
            placedBlocks.put(block.getFloorX()+":"+block.getFloorY()+":"+block.getFloorZ(), block);
        }
    }

    public void onBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        if(Players.containsValue(player)){
            Block block = event.getBlock();
            if(placedBlocks.containsKey(block.getFloorX()+":"+block.getFloorY()+":"+block.getFloorZ())){
                event.setCancelled(false);
                return;
            }else{
                int c = 8;
                if(getMode() == MODE_THREES || getMode() == MODE_FOURS) c = 4;
                for(int t = 0; t < c;t++){
                    Vector3[] pos = getTeamDataByNumber(t);
                    int a = 3;
                    if(c == 4) a = 5;
                    if(block.getFloorX() == pos[a].getFloorX() && block.getFloorY() == pos[a].getFloorY() && block.getFloorZ() == pos[a].getFloorZ() ||
                       block.getFloorX() == pos[a + 1].getFloorX() && block.getFloorY() == pos[a + 1].getFloorY() && block.getFloorZ() == pos[a + 1].getFloorZ()){
                        if(team.get(PlayerDataManager.getPlayerData(player)) == t){
                            event.setCancelled();
                            return;
                        }else{
                            event.setCancelled();
                            bedDestory(t, player, false);
                            return;
                        }
                    }
                }
            }
            event.setCancelled();
        }
    }

    public boolean isPlacedByPlayer(Block block){
        return placedBlocks.containsKey(block.getFloorX()+":"+block.getFloorY()+":"+block.getFloorZ());
    }

    public void onInteract(PlayerInteractEvent event){

    }

    public void attack(Player player, long eid){
        for(int t = 0;t < 8;t++){
            if(this.shopKeeperEid[t] == eid){
                ServerChestInventorys.setData(player, new BedwarsItemShopChestInventory(player));
            }
        }
        for(int t = 8;t < 16;t++){
            if(this.shopKeeperEid[t] == eid){
                ServerChestInventorys.setData(player, new BedwarsUpgraderChestInventory(player));
            }
        }
    }

    public void checkMember(){
        HashMap<Integer, Integer> alives = new HashMap<Integer, Integer>();
        int c = 8;
        if(getMode() == MODE_THREES || getMode() == MODE_FOURS) c = 4;
        for(int t = 0; t < c;t++){
            alives.put(t, 0);
        }
        for (Map.Entry<String,Player> e : Players.entrySet()){
            if(e.getValue() instanceof Player){
                if(!isEliminated.get(PlayerDataManager.getPlayerData(e.getValue()))){
                    alives.put(team.get(PlayerDataManager.getPlayerData(e.getValue())), alives.get(team.get(PlayerDataManager.getPlayerData(e.getValue()))) + 1);
                }
            }
        }
        int aliveTeams = 0;
        for(int t = 0; t < c;t++){
            if(alives.get(t) == 0){
                if(!this.isTeamEliminated.get(t)) teamEliminate(t);
            }else{
                aliveTeams++;
            }
        }
        if(aliveTeams == 1) finish(FINISH_TYPE_ALL_KILL);
    }

    public void bedDestory(int team, Player player, boolean isEvent){
        Vector3[] pos = getTeamDataByNumber(team);
        int c = 3;
        if(getMode() == MODE_THREES || getMode() == MODE_FOURS) c = 5;
        this.world.setBlock(pos[c], Block.get(Block.AIR));
        this.world.setBlock(pos[c + 1], Block.get(Block.AIR));
        this.isBedBroken.put(team, true);
        String teamName = TeamUtil.getColorCodeByNumber(team)+TeamUtil.getNameByNumber(team) + " Bed";
        for (Map.Entry<String,Player> e : Players.entrySet()){
            if(!isEvent){
                if(this.team.get(PlayerDataManager.getPlayerData(e.getValue())) == team){
                    SetEntityDataPacket pk = new SetEntityDataPacket();
                    pk.eid = this.bedNameTag[team];
                    this.tagMetadata.putString(Entity.DATA_NAMETAG, Main.getMessage(e.getValue(), "bedwars.bed.nametag.death", new String[]{TeamUtil.getColorCodeByNumber(this.team.get(PlayerDataManager.getPlayerData(player)))+player.getName()}));
                    pk.metadata = this.tagMetadata;
                    e.getValue().dataPacket(pk);
                    e.getValue().setSubtitle(Main.getMessage(e.getValue(), "bedwars.bed.destroyed.title.sub"));
                    e.getValue().sendTitle(Main.getMessage(e.getValue(), "bedwars.bed.destroyed.title"));
                    e.getValue().sendMessage(Main.getMessage(e.getValue(), "bedwars.bed.destruction")+Main.getMessage(e.getValue(), "bedwars.bed.destroyed", new String[]{player.getName()}));
                }else{
                    e.getValue().sendMessage("\n"+Main.getMessage(e.getValue(), "bedwars.bed.destruction")+Main.getMessage(e.getValue(), "bedwars.bed.destroyed.other", new String[]{teamName, player.getName()})+"\n");
                }
            }
        }//TODO Drop Bed Item
    }

    public void spawnBedNameTag(Player player){
        int team = this.team.get(PlayerDataManager.getPlayerData(player));
        Vector3[] poss = this.getTeamDataByNumber(team);
        int c = 3;
        if(getMode() == MODE_THREES || getMode() == MODE_FOURS) c = 5;
        AddEntityPacket pkk = new AddEntityPacket();
        pkk.entityUniqueId = bedNameTag[team];
        pkk.entityRuntimeId = bedNameTag[team];
        pkk.x = (float)((poss[c].x + poss[c+1].x)/2 + 0.5);
        pkk.y = (float)(poss[c].y + 1);
        pkk.z = (float)((poss[c].z + poss[c+1].z)/2 + 0.5);
        pkk.yaw = 0;
        pkk.pitch = 0;
        pkk.type = EntityItem.NETWORK_ID;
        int flags = 0;
        flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
        //flags |= 1 << Entity.DATA_FLAG_INVISIBLE;
        flags |= 1 << Entity.DATA_FLAG_SHOWBASE;
        this.tagMetadata = new EntityMetadata()
                .putLong(Entity.DATA_FLAGS, flags)
                .putString(Entity.DATA_NAMETAG, Main.getMessage(player, "bedwars.bed.nametag.alive"))
                .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1);
        pkk.metadata = this.tagMetadata;
        player.dataPacket(pkk);
    }

    public void teamEliminate(int team){
        String teamName = TeamUtil.getColorCodeByNumber(team) + TeamUtil.getNameByNumber(team) + "Team";
        for (Map.Entry<String,Player> e : Players.entrySet()){
            e.getValue().sendMessage("\n"+Main.getMessage(e.getValue(), "bedwars.team.eliminated") + Main.getMessage(e.getValue(), "bedwars.team.eliminate", new String[]{teamName})+"\n");
        }
        this.isTeamEliminated.put(team, true);
    }

    public void kill(Player player, Player death){
        int coin = (int)(Math.random() * 20)+5;
        int exp = (int)(Math.random() * 10)+5;
        Stat.addCoin(player, coin);
        Stat.addExp(player,exp);
        //Stat.setBedwarsKills(player, Stat.getBedwarsKills(player) + 1);
        player.sendMessage(Main.getMessage(player,"status.add.coin",new String[]{String.valueOf(coin)}));
        player.sendMessage(Main.getMessage(player,"status.add.exp",new String[]{String.valueOf(exp)}));
        killcount.put(PlayerDataManager.getPlayerData(player),killcount.get(player)+1);
        boolean isAddItem = false;
        boolean isFinalKill = false;
        if(isBedBroken.get(team.get(PlayerDataManager.getPlayerData(death)))){
            isFinalKill = true;
        }
        String finalkillMessage = "";
        if(isFinalKill){
            finalkillMessage = "bedwars.death.finalkill";
            BedwarsFinalkillEffects.run(player, death);
        }
        String playerTeam = TeamUtil.getColorCodeByNumber(team.get(PlayerDataManager.getPlayerData(player)));
        String deathTeam = TeamUtil.getColorCodeByNumber(team.get(PlayerDataManager.getPlayerData(death)));
        EntityDamageEvent.DamageCause c = Event_EntityDamageEvent.lastcause.get(death).getCause();
        if(c == EntityDamageEvent.DamageCause.VOID){
            for (Map.Entry<String,Player> e : Players.entrySet()){
                e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.void.by", new String[]{deathTeam+death.getName(),playerTeam+player.getName()}) + Main.getMessage(e.getValue(), finalkillMessage));
            }
            isAddItem = true;
        }else if(c == EntityDamageEvent.DamageCause.FIRE || c == EntityDamageEvent.DamageCause.FIRE_TICK){
            for (Map.Entry<String,Player> e : Players.entrySet()){
                e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.born.by", new String[]{deathTeam+death.getName(),playerTeam+player.getName()}) + Main.getMessage(e.getValue(), finalkillMessage));
            }
        }else if(c == EntityDamageEvent.DamageCause.FALL){
            for (Map.Entry<String,Player> e : Players.entrySet()){
                e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.fall.by", new String[]{deathTeam+death.getName(),playerTeam+player.getName()}) + Main.getMessage(e.getValue(), finalkillMessage));
            }
        }else if(c == EntityDamageEvent.DamageCause.LAVA){
            for (Map.Entry<String,Player> e : Players.entrySet()){
                e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.lava.by", new String[]{deathTeam+death.getName(),playerTeam+player.getName()}) + Main.getMessage(e.getValue(), finalkillMessage));
            }
        /*}else if(c == EntityDamageEvent.CAUSE_ENTITY_EXPLOSION){
            for (Map.Entry<String,Player> e : data2.get(data4.get(player)).entrySet()){
                if(e.getValue().getName().equals(player.getName())) e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.explosion.by", new String[]{"§6"+death.getName(),"§b"+player.getName()}) + Main.getMessage(e.getValue(), finalkillMessage));
                else if(e.getValue().getName().equals(death.getName())) e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.explosion.by", new String[]{"§6"+death.getName(),"§7"+player.getName()}) + Main.getMessage(e.getValue(), finalkillMessage));
                else e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.explosion.by", new String[]{"§7"+death.getName(),"§7"+player.getName()}) + Main.getMessage(e.getValue(), finalkillMessage));
            }*/
        }else{
            for (Map.Entry<String,Player> e : Players.entrySet()){
                e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.kill.by", new String[]{deathTeam+death.getName(),playerTeam+player.getName()}) + Main.getMessage(e.getValue(), finalkillMessage));
            }
            isAddItem = true;
        }
        if(isAddItem){
            addItem(player, death);
        }else{
            dropItem(death);
        }
    }

    public void death(Player player, boolean isKilledByPlayer, boolean isLeft){
        if(Players.containsValue(player)){
            boolean isFinalKill = false;
            if(isBedBroken.get(team.get(PlayerDataManager.getPlayerData(player)))){
                isFinalKill = true;
            }
            String finalkillMessage = "";
            if(isFinalKill) finalkillMessage = "bedwars.death.finalkill";
            String playerTeam = TeamUtil.getColorCodeByNumber(team.get(PlayerDataManager.getPlayerData(player)));
            if(isLeft){
                for (Map.Entry<String,Player> e : Players.entrySet()){
                    e.getValue().sendMessage(Main.getMessage(player, "skywars.death.left", new String[]{"§7"+player.getName()}) + Main.getMessage(e.getValue(), finalkillMessage));
                }
                dropItem(player);
                this.isEliminated.put(PlayerDataManager.getPlayerData(player), true);
            }else if(!isKilledByPlayer){
                EntityDamageEvent.DamageCause c = Event_EntityDamageEvent.lastcause.get(player).getCause();
                if(c == EntityDamageEvent.DamageCause.VOID){
                    for (Map.Entry<String,Player> e : Players.entrySet()){
                        e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.void", new String[]{playerTeam+player.getName()}) + Main.getMessage(e.getValue(), finalkillMessage));
                    }
                }else if(c == EntityDamageEvent.DamageCause.FIRE || c == EntityDamageEvent.DamageCause.FIRE_TICK){
                    for (Map.Entry<String,Player> e : Players.entrySet()){
                        e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.born", new String[]{playerTeam+player.getName()}) + Main.getMessage(e.getValue(), finalkillMessage));
                    }
                }else if(c == EntityDamageEvent.DamageCause.FALL){
                    for (Map.Entry<String,Player> e : Players.entrySet()){
                        e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.fall", new String[]{playerTeam+player.getName()}) + Main.getMessage(e.getValue(), finalkillMessage));
                    }
                }else if(c == EntityDamageEvent.DamageCause.LAVA){
                    for (Map.Entry<String,Player> e : Players.entrySet()){
                        e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.lava", new String[]{playerTeam+player.getName()}) + Main.getMessage(e.getValue(), finalkillMessage));
                    }
                /*}else if(c == EntityDamageEvent.CAUSE_ENTITY_EXPLOSION){
                    for (Map.Entry<String,Player> e : data2.get(data4.get(player)).entrySet()){
                        if(e.getValue().getName().equals(player.getName())) e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.explosion.fall", new String[]{"§6"+player.getName()}) + Main.getMessage(e.getValue(), finalkillMessage));
                        else e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.explosion.fall", new String[]{"§7"+player.getName()}) + Main.getMessage(e.getValue(), finalkillMessage));
                    }*/
                }else{//TODO more reason
                    for (Map.Entry<String,Player> e : Players.entrySet()){
                        e.getValue().sendMessage(Main.getMessage(e.getValue(), "skywars.death.void", new String[]{playerTeam+player.getName()}) + Main.getMessage(e.getValue(), finalkillMessage));
                    }
                }
            }
            //Stat.setBedwarsDeaths(player, Stat.getBedwarsDeaths(player) + 1);
            player.setGamemode(3);
            player.setAllowFlight(true);
            /*Map<Integer,Item> items = player.getInventory().getContents();
                for (Map.Entry<Integer,Item> e : items.entrySet()){
                    player.getLevel().dropItem(new Vector3(player.x,player.y,player.z),e.getValue());
                }
            player.getInventory().clearAll();*/
            player.teleport(new Vector3(player.x,128,player.z));
            player.setMaxHealth(20);
            player.setHealth(20);
            player.removeAllEffects();
            player.setOnFire(0);
            if(isBedBroken.get(team.get(PlayerDataManager.getPlayerData(player)))){
                eliminate(player);
            }else{
                Inventorys.setData(player,new BedwarsRespawningInventory());
                respawnPlayerCount(player, 5);
            }
            /*Inventorys.setData(player,new SkywarsSoloDeathInventory());
            for (Map.Entry<String,Player> e : Players.entrySet()){
                if(e.getValue().getGamemode() == 3){
                    Inventorys.data2.get(e.getValue()).register(e.getValue());
                }
            }*/
            checkMember();
        }
    }

    public void addItem(Player killer, Player death){
        //TODO
    }

    public void dropItem(Player death){
        //TODO
    }

    public void eliminate(Player player){
        this.isEliminated.put(PlayerDataManager.getPlayerData(player), true);
    }

    public void respawnPlayerCount(Player player,int count){
        if(!Players.containsValue(player) || !getGameDataAsBoolean("gamenow")) return;
        if(count <= 0){
            respawnPlayerLater(player);
            return;
        }
        player.setSubtitle(Main.getMessage(player, "bedwars.dead.respawning.title.sub", new String[]{String.valueOf(count)}));
        player.sendTitle(Main.getMessage(player, "bedwars.dead.respawning.title"));
        player.sendMessage(Main.getMessage(player, "bedwars.dead.respawning.title.sub", new String[]{String.valueOf(count)}));
        count--;
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackRespawn(this, player, count),20);
    }

    public void respawnPlayerLater(Player player){
        if(!Players.containsValue(player)) return;//TODO Spactator Position
        Vector3[] pos = getTeamDataByNumber(team.get(PlayerDataManager.getPlayerData(player)));
        player.teleport(player.getLevel().getSafeSpawn(pos[0]));
        player.sendTitle(Main.getMessage(player, "bedwars.dead.respawned.title"));
        player.sendMessage(Main.getMessage(player, "bedwars.dead.respawned"));
        Inventorys.setData(player,new BedwarsInventory());
        player.setMaxHealth(20);
        player.setHealth(20);
        player.removeAllEffects();
        player.setOnFire(0);
        player.setGamemode(0);
        setNameTag(player);
    }

    public void despawnAll(Player player){
        int team = this.team.get(PlayerDataManager.getPlayerData(player));
        RemoveEntityPacket pk = new RemoveEntityPacket();
        pk.eid = bedNameTag[team];
        player.dataPacket(pk);
        for(BedwarsGenerator g : generatorsD){
            g.stopGenerator(player);
        }
        for(BedwarsGenerator g : generatorsE){
            g.stopGenerator(player);
        }
    }

    public void setNameTag(Player player){

    }

    public Vector3[] getTeamDataByNumber(int team){
        Vector3[] p = null;
        switch(team){
            case 0:
                p = stage.getRedData();
            break;
            case 1:
                p = stage.getBlueData();
            break;
            case 2:
                p = stage.getGreenData();
            break;
            case 3:
                p = stage.getYellowData();
            break;
            case 4:
                p = stage.getAquaData();
            break;
            case 5:
                p = stage.getWhiteData();
            break;
            case 6:
                p = stage.getPinkData();
            break;
            case 7:
                p = stage.getGrayData();
            break;
        }
        return p;
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
                    if(!Setting.getChatHide(e.getValue())){
                        if(team.get(PlayerDataManager.getPlayerData(e.getValue())) == team.get(PlayerDataManager.getPlayerData(player))){
                            e.getValue().sendMessage("<"+player.getNameTag()+"§f> "+event.getMessage());
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

    public void startEventTimer(){
        eventCount = 0;
        nextEventTime = 360;
        nextEventName = "Diamond Upgrade";
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackEventTime(this), 20);
    }

    public void eventTimer(){
        this.nextEventTime--;
        if(this.nextEventTime == 0) this.eventTimeTable();
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackEventTime(this), 20);
    }

    public void eventTimeTable(){
        switch(this.eventCount){
            case 0:
                for(BedwarsGenerator g : generatorsD){
                    g.increaseTire();
                }
                this.nextEventName = "Emerald Upgrade";
                this.nextEventTime = 360;
            break;
            case 1:
                for(BedwarsGenerator g : generatorsE){
                    g.increaseTire();
                }
                this.nextEventName = "Diamond Maxed";
                this.nextEventTime = 360;
            break;
            case 2:
                for(BedwarsGenerator g : generatorsD){
                    g.increaseTire();
                }
                this.nextEventName = "Emerald Maxed";
                this.nextEventTime = 360;
            break;
            case 3:
                for(BedwarsGenerator g : generatorsE){
                    g.increaseTire();
                }
                this.nextEventName = "Bed Destruction";
                this.nextEventTime = 360;
            break;
            case 4:
                //Bed Destruction
                this.nextEventName = "Sudden Deaath";
                this.nextEventTime = 600;
            break;
            case 5:
                //Sudden Death
                this.nextEventName = "Game End";
                this.nextEventTime = 600;
            break;
            case 6:
                //Game End
            break;
        }
        this.eventCount++;
    }

    public BedwarsStage stage;
    public int mode = MODE_SOLO;
    public int countplayer = 0;
    public int eventCount;
    public int nextEventTime;
    public String nextEventName;
    public HashMap<Integer, Boolean> isBedBroken = new HashMap<Integer, Boolean>();
    public HashMap<Integer, BedwarsUpgrader> upgraders = new HashMap<Integer, BedwarsUpgrader>();
    public HashMap<PlayerData, Integer> team = new HashMap<PlayerData, Integer>();
    public HashMap<PlayerData, Boolean> isEliminated = new HashMap<PlayerData, Boolean>();
    public HashMap<PlayerData, Boolean> isHaveShears = new HashMap<PlayerData, Boolean>();
    public HashMap<PlayerData, Integer> haveArmor = new HashMap<PlayerData, Integer>();
    public HashMap<Integer, Boolean> isTeamEliminated = new HashMap<Integer, Boolean>();
    public HashMap<Integer, Integer> aiveCount = new HashMap<Integer, Integer>();
    public HashMap<Integer, BedwarsForge[]> forge = new HashMap<Integer, BedwarsForge[]>();
    public HashMap<PlayerData, Integer> killcount = new HashMap<PlayerData,Integer>();
    public HashMap<String, Block> placedBlocks = new HashMap<String, Block>();
    public long[] shopKeeperEid = new long[16];
    public long[] bedNameTag = new long[8];
    public long[] bedItem = new long[8];
    public EntityMetadata tagMetadata;
    public BedwarsGenerator[] generatorsD = new BedwarsGenerator[4];
    public BedwarsGenerator[] generatorsE = new BedwarsGenerator[4];
    public File file;
    //public BlockhuntStage[] stagelist;
    //public static BlockhuntStage[] stagelist2;
}
class CallbackTime extends Task{

    public Bedwars owner;

    public CallbackTime(Bedwars owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.Time();
    }
}
class CallbackFinish extends Task{

    public Bedwars owner;

    public CallbackFinish(Bedwars owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.finishLater();
    }
}
class CallbackRespawn extends Task{

    public Bedwars owner;
    public Player player;
    public int count;

    public CallbackRespawn(Bedwars owner, Player player,int count){
        this.owner = owner;
        this.player = player;
        this.count = count;
    }

    public void onRun(int d){
        this.owner.respawnPlayerCount(this.player,this.count);
    }
}
class CallbackWorld extends Task{

    public Bedwars owner;

    public CallbackWorld(Bedwars owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.world();
    }
}
class CallbackEventTime extends Task{

    public Bedwars owner;

    public CallbackEventTime(Bedwars owner){
        this.owner = owner;
    }

    public void onRun(int d){
        this.owner.eventTimer();
    }
}