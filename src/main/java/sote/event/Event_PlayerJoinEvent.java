package sote.event;

import java.util.Map;
import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.BossEventPacket;
import cn.nukkit.scheduler.Task;
import sote.GameMob;
import sote.Main;
import sote.MapPainting;
import sote.MySQL;
import sote.OPStatue;
import sote.PacketManager;
import sote.PlayerDataManager;
import sote.inventory.Inventorys;
import sote.lobbyitem.AppearLobbyItem;
import sote.lobbyitem.LobbyItem;
import sote.lobbyitem.LobbyItems;
import sote.login.LoginSystem;
import sote.party.Party;
import sote.popup.Popups;
import sote.serverlist.ServerListAPI;
import sote.skywarssolo.kit.SkywarsSoloKits;
import sote.stat.Stat;

public class Event_PlayerJoinEvent{

    public Event_PlayerJoinEvent(){
    }

    public static void onPlayerJoin(PlayerJoinEvent event){
        ServerListAPI.updatePlayers("join");
        Player player = event.getPlayer();
        PlayerDataManager.addPlayerData(player);
        MySQL.setAllPlayerStats(player);
        LobbyItems.lastshot.put(player, (long) 0);
        LoginSystem.addPlayer(player);
        Stat.setNameTag(player);
        Main.setBlock(player,false);
        Popups.addPlayer(player);
        Party.addPlayer(player);
        MapPainting.addPlayer(player);
        SkywarsSoloKits.setSellectKitData(player);
        event.setJoinMessage("");
        player.setGamemode(2);
        Inventorys.addPlayer(player);
        player.setFoodEnabled(false);
        player.getFoodData().sendFoodLevel(20);
        player.setExperience(0, 0);
        player.setEnableClientCommand(true);
        player.setAttribute(Attribute.getAttribute(Attribute.FOOD).setValue(20));
        int flags = 0;
        flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
        flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
        flags |= 1 << Entity.DATA_FLAG_INVISIBLE;
        EntityMetadata metadata = new EntityMetadata()
                .putLong(Entity.DATA_FLAGS,flags)
                .putString(Entity.DATA_NAMETAG,Main.getMessage(player,"warntext"))
                .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1);
        PacketManager.sendPacket(player,"AddLobbyWarn",metadata);
        player.setHealth(20);
        Main.canArmor.put(player, false);
        Map<UUID,Player> players = Server.getInstance().getOnlinePlayers();
        for (Map.Entry<UUID,Player> e : players.entrySet()){
            if(Main.setHide.get(e.getValue())){
                e.getValue().hidePlayer(player);
            }
        }
        if(Main.lobbyitementities.containsKey(player.getLevel().getName())){
            for (Map.Entry<LobbyItem,Player> e : Main.lobbyitementities.get(player.getLevel().getName()).entrySet()){
               ((AppearLobbyItem)e.getKey()).spawnTo(player);
            }
        }
        OPStatue.spawnStatue(player);
        GameMob.spawnMob(player);
        Main.BossName.put(player, Main.SERVER_NAME+" §8⋙ §aLobby");
        Main.BossValue.put(player, new Integer[]{1,1});
        AddEntityPacket pk12 = new AddEntityPacket();
        pk12.entityUniqueId = Main.BossGauge;
        pk12.entityRuntimeId = Main.BossGauge;
        pk12.type = 37;
        pk12.x = (float)127.5;
        pk12.y = (float)47;
        pk12.z = (float)106.5;
        pk12.yaw = 0;
        pk12.pitch = 0;
        flags = 0;
        flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
        flags |= 1 << Entity.DATA_FLAG_INVISIBLE;
        flags |= 1 << Entity.DATA_FLAG_SILENT;
        pk12.metadata = new EntityMetadata()
                .putLong(Entity.DATA_FLAGS,flags)
                .putString(Entity.DATA_NAMETAG,Main.BossName.get(player))
                .putFloat(Entity.DATA_SCALE,(float)0.1)
                .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1);
        player.dataPacket(pk12);
        BossEventPacket boss = new BossEventPacket();
        boss.eid = Main.BossGauge;
        boss.type = 0;
        /*boss.unknownString = "aaa";boss.unknownFloat = (float)1;boss.unknownShort = (short)1;boss.unknownInt = 1;boss.unknownInt2 = 1;boss.unknownFloat2 = (float)1;boss.unknownString2 = "bbb";*/
        player.dataPacket(boss);
        Main.setBossGaugeValue(player, 1, 1);
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackPacket(player), 50);
        player.sendTitle(Main.SERVER_NAME);
        /* pk13 = new SetEntityLinkPacket();
        pk13.rider = Main.BossGauge;
        pk13.riding = 0;
        pk13.type = SetEntityLinkPacket.TYPE_RIDE;
        player.dataPacket(pk13);*/
        /*AddEntityPacket pk13 = new AddEntityPacket();
        pk13.entityUniqueId = Main.TestEid;
        pk13.entityRuntimeId = Main.TestEid;
        pk13.type = 52;
        pk13.x = (float)2.5;
        pk13.y = (float)8;
        pk13.z = (float)-39.5;
        pk13.yaw = 0;
        pk13.pitch = 0;
        flags = 0;
        pk13.metadata = new EntityMetadata()
                .putLong(Entity.DATA_FLAGS,flags)
                .putString(Entity.DATA_NAMETAG,"")
                .putFloat(Entity.DATA_SCALE,(float)0.5)
                .putFloat(54, 3)
                .putFloat(54, 3)
                .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1)
                .putByte(Entity.DATA_LEAD, 0);
        player.dataPacket(pk13);*/
        //Main.updateCommandData(player);
        Main.gen.start(player);
    }

    public static void packet(Player player){
        if(!(player instanceof Player)) return;
        //SkywarsSoloKits.spawnKit(player);
        //Main.updateRanking();
        OPStatue.updateStatue(player);
    }

}
class CallbackPacket extends Task{

    public Player player;

    public CallbackPacket(Player p){
        player = p;
    }

    public void onRun(int d){
        go();
    }

    public void go(){
        Event_PlayerJoinEvent.packet(player);
    }
}
/*EntityMetadata metadata = new EntityMetadata()
.putByte(Entity.DATA_FLAGS,1 << Entity.DATA_FLAG_INVISIBLE)
.putString(Entity.DATA_NAMETAG,"")
//.putBoolean(Entity.DATA_SHOW_NAMETAG, true)
.putLong(Entity.DATA_LEAD_HOLDER, (long)-1)
.putByte(Entity.DATA_LEAD, 0)
.putLong(17,1)
.putByte(20,(int)(Math.random() * 15));
//.putBoolean(Entity.DATA_NO_AI, true);
PacketManager.sendPacket(player,"AddHomeWolf",metadata);*/
/*AddEntityPacket pk12 = new AddEntityPacket();
long eidd = Entity.entityCount++;
pk12.entityUniqueId = eidd;
pk12.entityRuntimeId = eidd;
pk12.type = 52;
pk12.x = (float)128;
pk12.y = (float)60;
pk12.z = (float)70;
pk12.yaw = 90;
pk12.pitch = 90;
int flags = 0;
flags |= 1 << Entity.DATA_FLAG_CHARGED;
pk12.metadata = new EntityMetadata()
        .putByte(Entity.DATA_FLAGS,flags)
        .putString(Entity.DATA_NAMETAG,"")
        .putFloat(Entity.DATA_SCALE,(float)1)
        .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1)
        .putByte(Entity.DATA_LEAD, 0);
player.dataPacket(pk12);*/
/*Long eid = Entity.entityCount++;
UUID uuid = UUID.randomUUID();
AddPlayerPacket pkkkkk = new AddPlayerPacket();
pkkkkk.eid = eid;
pkkkkk.username = "";
pkkkkk.uuid = uuid;
pkkkkk.x = (float)player.x;
pkkkkk.y = (float)player.y;
pkkkkk.z = (float)player.z;
pkkkkk.yaw = 0;
pkkkkk.pitch = 0;
pkkkkk.metadata = new EntityMetadata()
        .putString(Entity.DATA_NAMETAG,"aaaaaaa")
        .putBoolean(Entity.DATA_SHOW_NAMETAG, false)
        .putLong(Entity.DATA_LEAD_HOLDER, (long)-1)
        .putByte(Entity.DATA_LEAD, 0)
        .putBoolean(Entity.DATA_NO_AI, true);
player.dataPacket(pkkkkk);
Skin skin = new Skin(new File(Main.file+"/skin/test.png"));
skin.setModel("Festive_FestiveTomte");
PlayerListPacket pkkkkkk = new PlayerListPacket();
pkkkkkk.type = PlayerListPacket.TYPE_ADD;
pkkkkkk.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(uuid, eid, "testttttttttt",skin)};
player.dataPacket(pkkkkkk);
AddEntityPacket pkkkkkkk = new AddEntityPacket();
pkkkkkkk.eid = Entity.entityCount++;
pkkkkkkk.type = 38;
pkkkkkkk.x = (float)player.x;
pkkkkkkk.y = (float)player.y;
pkkkkkkk.z = (float)player.z;
pkkkkkkk.yaw = 90;
pkkkkkkk.pitch = 90;
pkkkkkkk.metadata = new EntityMetadata()
        .putByte(Entity.DATA_FLAGS,1 << Entity.DATA_FLAG_INVISIBLE)
        .putString(Entity.DATA_NAMETAG,"")
        .putBoolean(Entity.DATA_SHOW_NAMETAG, false)
        .putLong(Entity.DATA_LEAD_HOLDER, (long)-1)
        .putByte(Entity.DATA_LEAD, 0)
        .putBoolean(Entity.DATA_NO_AI, true);
player.dataPacket(pkkkkkkk);
pkkkkkkk.eid = Entity.entityCount++;
player.dataPacket(pkkkkkkk);
pkkkkkkk.eid = Entity.entityCount++;
player.dataPacket(pkkkkkkk);
pkkkkkkk.eid = Entity.entityCount++;
player.dataPacket(pkkkkkkk);
pkkkkkkk.eid = Entity.entityCount++;
player.dataPacket(pkkkkkkk);*/