package sote.event;

import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.event.entity.EntityLevelChangeEvent;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.network.protocol.SetEntityDataPacket;
import cn.nukkit.potion.Effect;
import sote.GameMob;
import sote.Main;
import sote.MapPainting;
import sote.OPStatue;
import sote.PacketManager;
import sote.lobbyitem.AppearLobbyItem;
import sote.lobbyitem.LobbyItem;
import sote.lobbyitem.LobbyItems;
import sote.popup.HomePopup;
import sote.popup.Popups;
import sote.skywarssolo.kit.SkywarsSoloKits;

public class Event_EntityLevelChangeEvent{

    public Event_EntityLevelChangeEvent(){
    }

    public static void onLevelChange(EntityLevelChangeEvent event){
        Entity entity = event.getEntity();
        if(entity instanceof Player){
            Player player = (Player) entity;
            int flags;
            String after = event.getTarget().getFolderName();
            String before = event.getOrigin().getFolderName();
            if(after.equals("lobby")){
                PacketManager.sendPacket(player,"AddLobbyWarn");
                OPStatue.spawnStatue(player);
                GameMob.spawnMob(player);
                Main.gen.start(player);
                player.setGamemode(2);
            }else{// if(before.equals("lobby")){
                PacketManager.sendPacket(player,"RemoveLobbyWarn");
                OPStatue.despawnStatue(player);
                GameMob.despawnMob(player);
                Main.gen.finish(player);
            }
            if(after.equals("skywars")){
                SkywarsSoloKits.spawnKit(player);
                player.setGamemode(2);
            }else{// if(before.equals("skywars")){
                SkywarsSoloKits.despawnKit(player);
            }
            if(after.equals("murder")){
                flags = 0;
                flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;
                flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
                flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
                flags |= 1 << Entity.DATA_FLAG_INVISIBLE;
                EntityMetadata metadata = new EntityMetadata()
                        .putLong(Entity.DATA_FLAGS,flags)
                        .putString(Entity.DATA_NAMETAG,Main.getMessage(player,"murder.ruletext"))
                        .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1);
                PacketManager.sendPacket(player,"AddMurderRule",metadata);
                PacketManager.sendPacket(player,"AddMurderRanking");
                PacketManager.sendPacket(player,"AddMurderKnife2");
                MapPainting.sendMurderPainting(player);
            }else{// if(before.equals("murder")){
                PacketManager.sendPacket(player,"RemoveMurderRule");
                PacketManager.sendPacket(player,"RemoveMurderRanking");
                PacketManager.sendPacket(player,"RemoveMurderKnife2");
            }
            if(after.split("murder").length >= 2){
                flags = 0;
                flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;
                flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
                flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
                flags |= 1 << Entity.DATA_FLAG_INVISIBLE;
                EntityMetadata metadata = new EntityMetadata()
                        .putLong(Entity.DATA_FLAGS,flags)
                        .putString(Entity.DATA_NAMETAG,Main.getMessage("jp","murder.warntext"))
                        .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1);
                PacketManager.sendPacket(player,"AddMurderRule2",metadata);
                PacketManager.sendPacket(player,"AddMurderKnife");
                PacketManager.sendPacket(player,"SetMurderKnife");
            }else{// if(before.split("murder").length >= 2){
                PacketManager.sendPacket(player,"RemoveMurderRule2");
                PacketManager.sendPacket(player,"RemoveMurderKnife");
            }
            if(after.split("home").length >= 2){
                PacketManager.sendPacket(player,"AddHomeWolf");
                Popups.setData(player, new HomePopup(player));
            }else{// if(before.split("home").length >= 2){
                PacketManager.sendPacket(player,"RemoveHomeWolf");
            }
            if(after.equals("walls")){
                PacketManager.sendPacket(player,"AddMiniwallsUpgrader");
            }else{// if(before.equals("walls")){
                PacketManager.sendPacket(player,"RemoveMiniwallsUpgrader");
            }
            Effect effect;
            switch(after){
                case "lobby":
                    Main.setBossGaugeName(player,Main.SERVER_NAME+" §8⋙ §aLobby");
                    player.sendTip("§l§aLobby§r\n\n\n\n");
                    /*effect = Effect.getEffect(15);
                    effect.setDuration(60);
                    effect.setAmplifier(0);
                    player.addEffect(effect);*/
                break;
                case "arcade":
                    Main.setBossGaugeName(player,Main.SERVER_NAME+" §bBETA §8⋙ §cArcade");
                    player.sendTip("§l§cArcade§r\n\n\n\n");
                    /*effect = Effect.getEffect(15);
                    effect.setDuration(60);
                    effect.setAmplifier(0);
                    player.addEffect(effect);*/
                break;
                case "classic":
                    Main.setBossGaugeName(player,Main.SERVER_NAME+" §8⋙ §eClassic");
                    player.sendTip("§l§eClassic§r\n\n\n\n");
                    /*effect = Effect.getEffect(15);
                    effect.setDuration(60);
                    effect.setAmplifier(0);
                    player.addEffect(effect);*/
                break;
                case "murder":
                    Main.setBossGaugeName(player,Main.SERVER_NAME+" §8⋙ §cMurder");
                    player.sendTip("§l§cMurder§r\n\n\n\n");
                    /*effect = Effect.getEffect(15);
                    effect.setDuration(60);
                    effect.setAmplifier(0);
                    player.addEffect(effect);*/
                break;
                case "skywars":
                    Main.setBossGaugeName(player,Main.SERVER_NAME+" §8⋙ §bSkyWars");
                    player.sendTip("§l§bSkyWars§r\n\n\n\n");
                    /*effect = Effect.getEffect(15);
                    effect.setDuration(60);
                    effect.setAmplifier(0);
                    player.addEffect(effect);*/
                break;
                case "buildbattle":
                    Main.setBossGaugeName(player,Main.SERVER_NAME+" §8⋙ §6BuildBattle");
                    player.sendTip("§l§6BuildBattle§r\n\n\n\n");
                    /*effect = Effect.getEffect(15);
                    effect.setDuration(60);
                    effect.setAmplifier(0);
                    player.addEffect(effect);*/
                break;
                case "walls":
                    Main.setBossGaugeName(player,Main.SERVER_NAME+" §8⋙ §aWalls");
                    player.sendTip("§l§aWalls§r\n\n\n\n");
                    /*effect = Effect.getEffect(15);
                    effect.setDuration(60);
                    effect.setAmplifier(0);
                    player.addEffect(effect);*/
                break;
                case "blockhunt":
                    Main.setBossGaugeName(player,Main.SERVER_NAME+" §8⋙ §6BlockHunt");
                    player.sendTip("§l§6BlockHunt§r\n\n\n\n");
                    /*effect = Effect.getEffect(15);
                    effect.setDuration(60);
                    effect.setAmplifier(0);
                    player.addEffect(effect);*/
                break;
            }
            if(after.split("home").length >= 2){
                Main.setBossGaugeName(player,Main.SERVER_NAME+" §8⋙ §aHome");
                player.sendTip("§l§aHome§r\n\n\n\n");
                effect = Effect.getEffect(15);
                effect.setDuration(60);
                effect.setAmplifier(0);
                player.addEffect(effect);
            }
            if(after.split("miniwalls").length >= 2){
                Main.setBossGaugeName(player,Main.SERVER_NAME+" §8⋙ §aMiniWalls");
                player.sendTip("§l§aMiniWalls§r\n\n\n\n");
                effect = Effect.getEffect(15);
                effect.setDuration(60);
                effect.setAmplifier(0);
                player.addEffect(effect);
            }
            if(LobbyItems.getSellectLobbyItemData(player) instanceof AppearLobbyItem){
                ((AppearLobbyItem)LobbyItems.getSellectLobbyItemData(player)).switchLevel(player, event.getOrigin(), event.getTarget());
            }
            if(Main.lobbyitemeids.containsKey(event.getOrigin())){
                for (Map.Entry<Long,Player> e : Main.lobbyitemeids.get(event.getOrigin()).entrySet()){
                    RemoveEntityPacket pk = new RemoveEntityPacket();
                    pk.eid = e.getKey();
                    player.dataPacket(pk);
                }
            }
            if(Main.lobbyitementities.containsKey(event.getOrigin().getName())){
                for (Map.Entry<LobbyItem,Player> e : Main.lobbyitementities.get(event.getOrigin().getName()).entrySet()){
                   ((AppearLobbyItem)e.getKey()).despawnTo(player);
                }
            }
            if(Main.lobbyitementities.containsKey(event.getTarget().getName())){
                for (Map.Entry<LobbyItem,Player> e : Main.lobbyitementities.get(event.getTarget().getName()).entrySet()){
                   ((AppearLobbyItem)e.getKey()).spawnTo(player);
                }
            }
        }
    }

    public static SetEntityDataPacket sp = new SetEntityDataPacket();

}