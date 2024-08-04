package sote;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockBurnEvent;
import cn.nukkit.event.block.BlockIgniteEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.block.ItemFrameDropItemEvent;
import cn.nukkit.event.entity.EntityArmorChangeEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityExplosionPrimeEvent;
import cn.nukkit.event.entity.EntityLevelChangeEvent;
import cn.nukkit.event.entity.EntityMotionEvent;
import cn.nukkit.event.entity.EntityPortalEnterEvent;
import cn.nukkit.event.entity.EntitySpawnEvent;
import cn.nukkit.event.entity.ProjectileHitEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.event.inventory.InventoryCloseEvent;
import cn.nukkit.event.inventory.InventoryOpenEvent;
import cn.nukkit.event.inventory.InventoryPickupItemEvent;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.event.level.ChunkLoadEvent;
import cn.nukkit.event.level.WeatherChangeEvent;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.event.player.PlayerDropItemEvent;
import cn.nukkit.event.player.PlayerFoodLevelChangeEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerInvalidMoveEvent;
import cn.nukkit.event.player.PlayerItemConsumeEvent;
import cn.nukkit.event.player.PlayerItemHeldEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerKickEvent;
import cn.nukkit.event.player.PlayerLoginEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.event.player.PlayerPreLoginEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.player.PlayerRespawnEvent;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.BossEventPacket;
import cn.nukkit.network.protocol.MoveEntityPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.network.protocol.SetEntityDataPacket;
import cn.nukkit.network.protocol.UpdateAttributesPacket;
import cn.nukkit.permission.Permission;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.Task;
import sote.ban.Ban;
import sote.bedwars.BedwarsShopItem;
import sote.bedwars.BedwarsSignManager;
import sote.blockhunt.BlockhuntSignManager;
import sote.buildbattle.BuildbattleSignManager;
import sote.commands.Command_Adddata;
import sote.commands.Command_Block;
import sote.commands.Command_Clan;
import sote.commands.Command_Home;
import sote.commands.Command_Key;
import sote.commands.Command_Login;
import sote.commands.Command_Map;
import sote.commands.Command_Nban;
import sote.commands.Command_Party;
import sote.commands.Command_Register;
import sote.commands.Command_Reloadlang;
import sote.commands.Command_Restart;
import sote.commands.Command_Say;
import sote.commands.Command_Setdata;
import sote.commands.Command_Shut;
import sote.commands.Command_Skin;
import sote.commands.Command_Sound;
import sote.commands.Command_Transfer;
import sote.commands.Command_Unnban;
import sote.commands.Command_Unshut;
import sote.commands.Command_Unwarn;
import sote.commands.Command_Warn;
import sote.commands.Command_World;
import sote.commands.Command_Yaw;
import sote.event.Event_BlockBreakEvent;
import sote.event.Event_BlockBurnEvent;
import sote.event.Event_BlockIgniteEvent;
import sote.event.Event_BlockPlaceEvent;
import sote.event.Event_ChunkLoadEvent;
import sote.event.Event_DataPacketReceiveEvent;
import sote.event.Event_EntityArmorChangeEvent;
import sote.event.Event_EntityDamageEvent;
import sote.event.Event_EntityExplosionPrimeEvent;
import sote.event.Event_EntityLevelChangeEvent;
import sote.event.Event_EntityMotionEvent;
import sote.event.Event_EntityPortalEnterEvent;
import sote.event.Event_EntitySpawnEvent;
import sote.event.Event_InventoryCloseEvent;
import sote.event.Event_InventoryOpenEvent;
import sote.event.Event_InventoryPickupItemEvent;
import sote.event.Event_InventoryTransactionEvent;
import sote.event.Event_ItemFrameDropItemEvent;
import sote.event.Event_PlayerChatEvent;
import sote.event.Event_PlayerCommandPreprocessEvent;
import sote.event.Event_PlayerDropItemEvent;
import sote.event.Event_PlayerFoodLevelChangeEvent;
import sote.event.Event_PlayerInteractEvent;
import sote.event.Event_PlayerInvalidMoveEvent;
import sote.event.Event_PlayerItemConsumeEvent;
import sote.event.Event_PlayerItemHeldEvent;
import sote.event.Event_PlayerJoinEvent;
import sote.event.Event_PlayerKickEvent;
import sote.event.Event_PlayerLoginEvent;
import sote.event.Event_PlayerMoveEvent;
import sote.event.Event_PlayerPreLoginEvent;
import sote.event.Event_PlayerQuitEvent;
import sote.event.Event_PlayerRespawnEvent;
import sote.event.Event_PlayerTeleportEvent;
import sote.event.Event_ProjectileHitEvent;
import sote.event.Event_ProjectileLaunchEvent;
import sote.event.Event_WeatherChangeEvent;
import sote.home.Home;
import sote.lang.Language;
import sote.lobbyitem.LobbyItem;
import sote.lobbyitem.LobbyItems;
import sote.login.LoginSystem;
import sote.miniwalls.MiniwallsSignManager;
import sote.murder.MurderSignManager;
import sote.murder.achievements.MurderAchievements;
import sote.murder.hat.MurderHats;
import sote.murder.upgrade.MurderUpgrades;
import sote.particle.Generator;
import sote.popup.Popups;
import sote.serverlist.ServerListAPI;
import sote.serverlist.ServerListTask;
import sote.skywarssolo.SkywarsSoloSignManager;
import sote.skywarssolo.kit.SkywarsSoloKits;

public class Main extends PluginBase implements Listener{

    public static String SERVER_NAME = "§cS§fOT§cE§f Server §bBETA§f";
    public static Generator 
    gen;

    public Main(){
    }

    public void onEnable(){
        registerCommands();
        getServer().getPluginManager().registerEvents(this, this);
        getDataFolder().mkdirs();
        warn = Entity.entityCount++;
        MurderRanking = Entity.entityCount++;
        MurderRule = Entity.entityCount++;
        MurderRule2 = Entity.entityCount++;
        MiniwallsUpgrader = Entity.entityCount++;
        wolf = Entity.entityCount++;
        knife = Entity.entityCount++;
        knife2 = Entity.entityCount++;
        BossGauge = Entity.entityCount++;
        TestEid = Entity.entityCount++;
        TestEid2 = Entity.entityCount++;
        file = getDataFolder();
        new PacketManager();
        new OPStatue(getDataFolder());
        new GameMob();
        new MurderHats();
        new MurderAchievements();
        new MurderUpgrades();
        new MySQL();
        new MySQLData();
        new GameProvider();
        new MapPainting();
        BedwarsShopItem.register();
        TeamUtil.regster();
        ServerItem.register();
        for(int i = 1;i <= 7;i++){
            GameProvider.register(i, false);
        }
        Ban.clearAll();
        MySQLData.syncAllData();
        for (Map.Entry<String,String> e : Home.homeid.entrySet()){
            int i = Integer.parseInt(e.getKey());
            String id = Home.getId(i);
            String url = Server.getInstance().getDataPath()+"/worlds/home";
            File newdir = new File(url+id);
            if(!newdir.exists()){
                //newdir.delete();
                newdir.mkdir();
                File newdir2 = new File(url+id+"/region");
                //newdir2.delete();
                newdir2.mkdir();
                int color = Integer.parseInt(Home.homeInfo.get(id).get("color"));
                Home.directoryCopy(new File(url+color),new File(url+id));
            }
        }
        LobbyItems.startTick();
        SkywarsSoloKits.spawnKit();
        Server.getInstance().loadLevel("lobby");
        Server.getInstance().loadLevel("arcade");
        Server.getInstance().loadLevel("classic");
        Server.getInstance().loadLevel("murder");
        Server.getInstance().loadLevel("skywars");
        Server.getInstance().loadLevel("buildbattle");
        Server.getInstance().loadLevel("walls");
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackClean(),400);
        Server.getInstance().getScheduler().scheduleRepeatingTask(new CallbackStop(),60*20);
        Server.getInstance().getScheduler().scheduleRepeatingTask(new CallbackPopup(),10);
        Server.getInstance().getScheduler().scheduleRepeatingTask(new CallbackSign(),10);
        Server.getInstance().getScheduler().scheduleRepeatingTask(new ServerListTask(),20);
        Server.getInstance().getScheduler().scheduleRepeatingTask(new CallbackBossGauge(),30);
        Event_PlayerChatEvent.run();
        new Event_DataPacketReceiveEvent();
        ServerListAPI.login();
        loadLang();
        Server.getInstance().getNetwork().registerPacket(ProtocolInfo.CLIENTBOUND_MAP_ITEM_DATA_PACKET,ClientboundMapItemDataPacket.class);
        Item.addCreativeItem(Item.get(403,0,1));
        for (Map.Entry<String,String> e : Home.homeid.entrySet()){
            int number = Integer.parseInt(e.getKey());
            GameProvider.register(number, true);
        }
        gen = new Generator(new Vector3(126.5,52, 69.5));
        super.onEnable();
    }

    public static void loadLang(){
        String[] languages = new String[]{"ja_JP", "en_US", "zn_CN", "pt_PT"};
        for(String lang : languages){
            loadLanguage(lang, file.toString(), "lobby.ini");
            loadLanguage(lang, file.toString(), "clan.ini");
            loadLanguage(lang, file.toString(), "home.ini");
            loadLanguage(lang, file.toString(), "party.ini");
            loadLanguage(lang, file.toString(), "murder.ini");
            loadLanguage(lang, file.toString(), "skywars.ini");
            loadLanguage(lang, file.toString(), "buildbattle.ini");
            loadLanguage(lang, file.toString(), "blockhunt.ini");
            loadLanguage(lang, file.toString(), "miniwalls.ini");
            loadLanguage(lang, file.toString(), "bedwars.ini");
        }
    }

    public static void Popup(){
        Map<UUID,Player> online = Server.getInstance().getOnlinePlayers();
        for (Map.Entry<UUID,Player> e : online.entrySet()){
            if(e.getValue() instanceof Player){
                sendPopup(e.getValue());
            }
        }
    }

    public static void sendPopup(Player player){
        if(!Popups.data.containsKey(player)) return;
        Popups.data.get(player).update();
        //if(!PopupStringBefore.containsKey(player)) PopupStringBefore.put(player, "");
        //if(Popups.data.get(player).getPopup().equals(PopupStringBefore.get(player))) return;
        player.sendActionBarTitle(Popups.data.get(player).getPopup());
        //PopupStringBefore.put(player, Popups.data.get(player).getPopup());
    }

    public static void setBossGaugeName(Player player,String name){
        SetEntityDataPacket pk = new SetEntityDataPacket();
        pk.eid = BossGauge;
        int flags = 0;
        flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
        flags |= 1 << Entity.DATA_FLAG_INVISIBLE;
        flags |= 1 << Entity.DATA_FLAG_SILENT;
        pk.metadata = new EntityMetadata()
                .putLong(Entity.DATA_FLAGS,flags)
                .putString(Entity.DATA_NAMETAG,name)
                .putFloat(Entity.DATA_SCALE,(float)0.1)
                .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1);
        player.dataPacket(pk);
        BossName.put(player, name);
    }

    public static void setBossGaugeValue(Player player,int value,int max){
        UpdateAttributesPacket pk = new UpdateAttributesPacket();
        pk.entityId = BossGauge;
        pk.entries = new Attribute[]{
                Attribute.getAttribute(Attribute.MAX_HEALTH).setMaxValue(max).setValue(value)
        };
        player.dataPacket(pk);
        BossEventPacket boss = new BossEventPacket();
        boss.eid = BossGauge;
        boss.type = 1;
        player.dataPacket(boss);
        BossValue.put(player, new Integer[]{value,max});
    }

    public static void sendBossGaugeValue(Player player){
        UpdateAttributesPacket pk = new UpdateAttributesPacket();
        pk.entityId = BossGauge;
        pk.entries = new Attribute[]{
                Attribute.getAttribute(Attribute.MAX_HEALTH).setMaxValue(BossValue.get(player)[1]).setValue(BossValue.get(player)[0])
        };
        player.dataPacket(pk);
        BossEventPacket boss = new BossEventPacket();
        boss.eid = BossGauge;
        boss.type = 1;
        player.dataPacket(boss);
    }

    public static void boss(){
        Map<UUID,Player> online = Server.getInstance().getOnlinePlayers();
        Player player;
        for (Map.Entry<UUID,Player> e : online.entrySet()){
            if(e.getValue() instanceof Player){
                player = e.getValue();
                MoveEntityPacket pk = new MoveEntityPacket();
                pk.eid = BossGauge;
                pk.x = player.x;
                pk.y = player.y;
                pk.z = player.z;
                pk.pitch = 0;
                pk.yaw = 0;
                pk.headYaw = 0;
                player.dataPacket(pk);
            }
        }
    }

    public static void clean(){
        Level en;
        en = Server.getInstance().getLevelByName("lobby");
        for(Entity ent:en.getEntities()){
            if(ent instanceof EntityItem || ent instanceof EntityArrow){
                ent.kill();
            }
        }
        en = Server.getInstance().getLevelByName("arcade");
        for(Entity ent:en.getEntities()){
            if(ent instanceof EntityItem || ent instanceof EntityArrow){
                ent.kill();
            }
        }
        en = Server.getInstance().getLevelByName("classic");
        for(Entity ent:en.getEntities()){
            if(ent instanceof EntityItem || ent instanceof EntityArrow){
                ent.kill();
            }
        }
        en = Server.getInstance().getLevelByName("murder");
        for(Entity ent:en.getEntities()){
            if(ent instanceof EntityItem || ent instanceof EntityArrow){
                ent.kill();
            }
        }
        en = Server.getInstance().getLevelByName("skywars");
        for(Entity ent:en.getEntities()){
            if(ent instanceof EntityItem || ent instanceof EntityArrow){
                ent.kill();
            }
        }
    }

    public static void loadLanguage(String l, String folder, String fileName){
        try{
            FileReader fr = new FileReader(folder+"/lang/"+l+"/"+fileName);
            BufferedReader br = new BufferedReader(fr);
            String line;
            String[] t;
            HashMap<String,String> map;
            map = new HashMap<String,String>();
            if(lang.containsKey(l)) map = lang.get(l);
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if(line.equals("")) continue;
                t = line.split("=");
                if(t.length < 2) continue;
                /*if(t.length >= 3){
                    t[1] = "";
                    int c = 0;
                    for(int i = 0;i <= t.length;i++){
                        if(i == 0){
                        }else if(i == 1){
                            t[1] += t[i];
                        }else{
                            t[1] += "="+t[i];
                        }
                        c++;
                    }
                }*/
                map.put(t[0].trim(),t[1].trim());
            }
            lang.put(l,map);
            br.close();
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public static String getMessage(Player player,String key){
        return getMessage(player,key,new String[]{});
    }

    public static String getMessage(Player player,String key,String[] set){
        if(!(player instanceof Player)) return "";
        String plang = Language.getLang(player);
        if(!lang.containsKey(plang)) return key;
        if(!lang.get(plang).containsKey(key)) return key;
        String msg = lang.get(plang).get(key);
        String msg2 = msg.replace(";;","\n");
        String msg3 = msg2.replace("{unknown}", "");
        for(int count = 1;count <= set.length;count++){
            msg3 = msg3.replace("{data"+count+"}",set[count-1]);
        }
        return msg3;
    }

    public static String getMessage(String plang,String key){
        return getMessage(plang,key,new String[]{});
    }

    public static String getMessage(String plang,String key,String[] set){
        if(!lang.containsKey(plang)) return key;
        if(!lang.get(plang).containsKey(key)) return key;
        String msg = lang.get(plang).get(key);
        String msg2 = msg.replace(";;","\n");
        String msg3 = msg2.replace("{unknown}", "");
        for(int count = 1;count <= set.length;count++){
            msg3 = msg3.replace("{data"+count+"}",set[count-1]);
        }
        return msg3;
    }

    public void onDisable(){
        MySQL.close();
        MySQLData.close();
        ServerListAPI.login();
        Map<UUID,Player> players = Server.getInstance().getOnlinePlayers();
        for (Map.Entry<UUID,Player> e : players.entrySet()){
            if(e.getValue().loggedIn && LoginSystem.auth.containsKey(e.getValue().getName().toLowerCase()) && LoginSystem.auth.get(e.getValue().getName().toLowerCase()) != 1){
                MySQL.sendAllData(e.getValue());
            }
        }
        super.onDisable();
    }

    public static void sign(){
        for(int i = 1;i <= 7;i++){
            MurderSignManager.updataSign(i);
            SkywarsSoloSignManager.updataSign(i);
            BuildbattleSignManager.updataSign(i);
            MiniwallsSignManager.updataSign(i);
            BlockhuntSignManager.updataSign(i);
            BedwarsSignManager.updataSign(i);
        }
    }

    public void registerCommands(){
        Server.getInstance().getPluginManager().addPermission(new Permission("command.PvE", "§c権限がありません",Permission.DEFAULT_OP));
        getServer().getCommandMap().register("register", new Command_Register(this));
        getServer().getCommandMap().register("login", new Command_Login(this));
        getServer().getCommandMap().register("block",new Command_Block(this));
        getServer().getCommandMap().register("key",new Command_Key(this));
        getServer().getCommandMap().register("warn",new Command_Warn(this));
        getServer().getCommandMap().register("unwarn",new Command_Unwarn(this));
        getServer().getCommandMap().register("nban",new Command_Nban(this));
        getServer().getCommandMap().register("unban",new Command_Unnban(this));
        getServer().getCommandMap().register("wolrd",new Command_World(this));
        getServer().getCommandMap().register("setdata",new Command_Setdata(this));
        getServer().getCommandMap().register("adddata",new Command_Adddata(this));
        getServer().getCommandMap().register("shut",new Command_Shut(this));
        getServer().getCommandMap().register("unshut",new Command_Unshut(this));
        getServer().getCommandMap().register("yaw",new Command_Yaw(this));
        getServer().getCommandMap().register("reloadlang",new Command_Reloadlang(this));
        getServer().getCommandMap().register("transfer",new Command_Transfer(this));
        getServer().getCommandMap().register("map",new Command_Map(this));
        getServer().getCommandMap().register("home",new Command_Home(this));
        getServer().getCommandMap().register("party",new Command_Party(this));
        getServer().getCommandMap().register("clan",new Command_Clan(this));
        getServer().getCommandMap().register("skin",new Command_Skin(this));
        getServer().getCommandMap().register("restart",new Command_Restart(this));
        getServer().getCommandMap().register("sound",new Command_Sound(this));
        getServer().getCommandMap().register("say",new Command_Say(this));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public static void onPlayerJoin(PlayerJoinEvent event){
        Event_PlayerJoinEvent.onPlayerJoin(event);
    }

    @EventHandler
    public static void onHeld(PlayerItemHeldEvent event){
        Event_PlayerItemHeldEvent.onHeld(event);
    }

    @EventHandler
    public static void onLevelChange(EntityLevelChangeEvent event){
        Event_EntityLevelChangeEvent.onLevelChange(event);
    }

    @EventHandler
    public static void onTeleport(PlayerTeleportEvent event){
        Event_PlayerTeleportEvent.onTeleport(event);
    }

    @EventHandler
    public static void onPlayerLogin(PlayerLoginEvent event){
        Event_PlayerLoginEvent.onPlayerLogin(event);
    }

    @EventHandler
    public void onPlayerPreLogin(PlayerPreLoginEvent event){
        Event_PlayerPreLoginEvent.onPlayerPreLogin(event);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Event_PlayerQuitEvent.onPlayerQuit(event);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event){
        Event_PlayerRespawnEvent.onRespawn(event);
    }

    @EventHandler
    public void onEat(PlayerItemConsumeEvent event){
        Event_PlayerItemConsumeEvent.onEat(event);
    }

    @EventHandler
    public void onNether(EntityPortalEnterEvent event){
        Event_EntityPortalEnterEvent.onNether(event);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        Event_BlockBreakEvent.onBreak(event);
    }

    @EventHandler
    public void onItemFrame(ItemFrameDropItemEvent event){
        Event_ItemFrameDropItemEvent.onItemFrame(event);
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event){
        Event_ProjectileHitEvent.onHit(event);
    }

    @EventHandler
    public void onLaunch(ProjectileLaunchEvent event){
        Event_ProjectileLaunchEvent.onLaunch(event);
    }

    @EventHandler
    public void onFood(PlayerFoodLevelChangeEvent event){
        Event_PlayerFoodLevelChangeEvent.onFood(event);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        Event_BlockPlaceEvent.onPlace(event);
    }

    @EventHandler
    public void onTouch(PlayerInteractEvent event){
        Event_PlayerInteractEvent.onTouch(event);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event){
        Event_InventoryCloseEvent.onClose(event);
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event){
        Event_InventoryOpenEvent.onOpen(event);
    }

    @EventHandler
    public void onTransaction(InventoryTransactionEvent event){
        Event_InventoryTransactionEvent.onTransaction(event);
    }

    @EventHandler
    public void onPickup(InventoryPickupItemEvent event){
        Event_InventoryPickupItemEvent.onPickup(event);
    }

    @EventHandler
    public void onHit(EntityDamageEvent event){
        Event_EntityDamageEvent.onHit(event);
    }

    @EventHandler
    public void onNotMove(PlayerInvalidMoveEvent event){
        Event_PlayerInvalidMoveEvent.onNotMove(event);
    }

    @EventHandler
    public void onSpawnEntity(EntitySpawnEvent event){
        Event_EntitySpawnEvent.onSpawnEntity(event);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Event_PlayerMoveEvent.onMove(event);
    }

    @EventHandler
    public void onMove(EntityMotionEvent event){
        Event_EntityMotionEvent.onMove(event);
    }

    @EventHandler
    public static void onKick(PlayerKickEvent event){
        Event_PlayerKickEvent.onKick(event);
    }

    @EventHandler
    public static void onCommand(PlayerCommandPreprocessEvent event){
        Event_PlayerCommandPreprocessEvent.onCommand(event);
    }

    @EventHandler
    public static void onChat(PlayerChatEvent event){
        Event_PlayerChatEvent.onChat(event);
    }

    public static void setBlock(Player player,boolean block){
        Blocker.put(player.getName(),block);
    }

    public static boolean getBlock(Player player){
        if(!Blocker.containsKey(player.getName())) return false;
        return Blocker.get(player.getName());
    }

    public static void stop(){
        count++;
        Server.getInstance().getLogger().info(getMessage("ja_JP", "system.restart",new String[]{String.valueOf(300-count)}));
        Map<UUID,Player> players = Server.getInstance().getOnlinePlayers();
        for (Map.Entry<UUID,Player> e : players.entrySet()){
            e.getValue().sendMessage(getMessage(e.getValue(),"system.restart",new String[]{String.valueOf(300-count)}));
        }
        if(count == 300){
            for (Map.Entry<UUID,Player> e : players.entrySet()){
                if(e.getValue().loggedIn && LoginSystem.auth.containsKey(e.getValue().getName().toLowerCase()) && LoginSystem.auth.get(e.getValue().getName().toLowerCase()) != 1){
                    MySQL.sendAllData(e.getValue());
                }
            }
            Server.getInstance().shutdown();
        }
    }

    @EventHandler
    public static void onBurn(BlockBurnEvent event){
        Event_BlockBurnEvent.onBurn(event);
    }

    @EventHandler
    public static void onBurn(BlockIgniteEvent event){
        Event_BlockIgniteEvent.onIgnite(event);
    }

    @EventHandler
    public static void onWeather(WeatherChangeEvent event){
        Event_WeatherChangeEvent.onWeather(event);
    }

    @EventHandler
    public static void onUpdate(PlayerDropItemEvent event){
        Event_PlayerDropItemEvent.onUpdate(event);
    }

    @EventHandler
    public static void onArmor(EntityArmorChangeEvent event){
        Event_EntityArmorChangeEvent.onArmor(event);
    }

    @EventHandler
    public void onMobAttackk(DataPacketReceiveEvent event){
        Event_DataPacketReceiveEvent.onMobAttackk(event);
    }

    @EventHandler
    public void onExplode(EntityExplosionPrimeEvent event){
        Event_EntityExplosionPrimeEvent.onExplosion(event);
    }


    @EventHandler
    public static void onChunk(ChunkLoadEvent event){
        Event_ChunkLoadEvent.onChunk(event);
    }

    public static void updateRanking(){
        /*Map<String, Integer> hashMap = new HashMap<String, Integer>();
        for (Map.Entry<String, Object> e : Stat.stat.entrySet()){//TODO Ranking
            int won = Integer.parseInt(((Map<String, String>) Stat.stat.get(e.getKey())).get("murder_murder_win"))
                    + Integer.parseInt(((Map<String, String>) Stat.stat.get(e.getKey())).get("murder_bystander_win"));
            hashMap.put(e.getKey(),won);
        }
        List<Map.Entry<String,Integer>> entries =
              new ArrayList<Map.Entry<String,Integer>>(hashMap.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String,Integer>>() {

            @Override
            public int compare(
                  Entry<String,Integer> entry1, Entry<String,Integer> entry2) {
                return ((Integer)entry2.getValue()).compareTo((Integer)entry1.getValue());
            }
        });
        String tag = "";
        Map<UUID,Player> players = Server.getInstance().getOnlinePlayers();
        for (Map.Entry<UUID,Player> e : players.entrySet()){
            tag = "";
            tag += getMessage(e.getValue(),"murder.win.ranking.top");
            int count = 1;
            for (Entry<String,Integer> s : entries) {
                if(count <= 15){
                    tag += getMessage(e.getValue(),"murder.win.ranking.center",new String[]{String.valueOf(count),String.valueOf(s.getValue()),String.valueOf(getRealName(s.getKey()))});
                    count++;
                }
            }
            if(e.getValue().getLevel().getFolderName().equals("murder")){
                SetEntityDataPacket pk = new SetEntityDataPacket();
                pk.eid = MurderRanking;
                int flags = 0;
                flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;
                flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
                flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
                flags |= 1 << Entity.DATA_FLAG_INVISIBLE;
                pk.metadata = new EntityMetadata()
                        .putLong(Entity.DATA_FLAGS,flags)
                        .putString(Entity.DATA_NAMETAG, tag);
                e.getValue().dataPacket(pk);
            }
        }*/
    }

    public static void updateCommandData(Player player){
        player.sendCommandData();
    }

    public static void mustShowPlayerLater(Player player, Player target, int tick){
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackShow(player, target), tick);
    }

    public static void mustHidePlayerLater(Player player, Player target, int tick){
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackHide(player, target), tick);
    }

    public static HashMap<String, Boolean> Blocker = new HashMap<String, Boolean>();
    public static HashMap<String, Integer> Chat = new HashMap<String, Integer>();
    public static HashMap<Player, Boolean> touch = new HashMap<Player, Boolean>();
    public static HashMap<Player, Boolean> canArmor = new HashMap<Player, Boolean>();
    public static HashMap<Player, Long> lastAttack = new HashMap<Player, Long>();
    public static HashMap<Player, Boolean> gamenow = new HashMap<Player, Boolean>();
    public static HashMap<Player, Boolean> setHide = new HashMap<Player, Boolean>();
    public static HashMap<Player, String> BossName = new HashMap<Player, String>();
    public static HashMap<Player, Integer[]> BossValue = new HashMap<Player, Integer[]>();
    public static HashMap<Level, HashMap<Long, Player>> lobbyitemeids = new HashMap<Level, HashMap<Long, Player>>();
    public static HashMap<String, HashMap<LobbyItem, Player>> lobbyitementities = new HashMap<String, HashMap<LobbyItem, Player>>();
    public static HashMap<String, HashMap<String, String>> lang = new HashMap<String,HashMap<String, String>>();
    public static SetEntityDataPacket sp = new SetEntityDataPacket();
    public static int count = 0;
    public static Boolean food = false;
    public static Long warn;
    public static Long MurderRanking;
    public static Long MurderRule;
    public static Long MurderRule2;
    public static Long wolf;
    public static Long knife;
    public static Long knife2;
    public static Long BossGauge;
    public static Long MiniwallsUpgrader;
    public static Long TestEid;
    public static Long TestEid2;
    public static Boolean fire = true;
    public static File file;
    public static HashMap<Player, String> PopupString = new HashMap<Player, String>();
    public static HashMap<Player, String> PopupStringBefore = new HashMap<Player, String>();
    public static HashMap<Player, Integer> Edition = new HashMap<Player, Integer>();
    public static HashMap<Entity, Player> gameEntity = new HashMap<Entity, Player>();

}
class CallbackStop extends Task{

    public CallbackStop(){
    }

    public void onRun(int d){
        go();
    }

    public void go(){
        Main.stop();
    }
}
class CallbackSign extends Task{

    public CallbackSign(){
    }

    public void onRun(int d){
        go();
    }

    public void go(){
        Main.sign();
    }
}
class CallbackClean extends Task{

    public CallbackClean(){
    }

    public void onRun(int d){
        go();
    }

    public void go(){
        Main.clean();
    }
}
class CallbackBossGauge extends Task{

    public CallbackBossGauge(){
    }

    public void onRun(int d){
        go();
    }

    public void go(){
        Main.boss();
    }
}
class CallbackPopup extends Task{

    public CallbackPopup(){
    }

    public void onRun(int d){
        Main.Popup();
    }
}
class CallbackShow extends Task{

    public Player player;
    public Player target;

    public CallbackShow(Player player, Player target){
        this.player = player;
        this.target = target;
    }

    public void onRun(int d){
        player.showPlayer(target);
        target.getInventory().sendArmorContents(player);
        target.getInventory().sendContents(player);
    }
}
class CallbackHide extends Task{

    public Player player;
    public Player target;

    public CallbackHide(Player player, Player target){
        this.player = player;
        this.target = target;
    }

    public void onRun(int d){
        player.hidePlayer(target);
    }
}


