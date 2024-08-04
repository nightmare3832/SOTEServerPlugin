package sote.event;

import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityPotion;
import cn.nukkit.entity.item.EntityVehicle;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.sound.LaunchSound;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.ContainerClosePacket;
import cn.nukkit.network.protocol.ContainerSetSlotPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.network.protocol.InteractPacket;
import cn.nukkit.network.protocol.LoginPacket;
import cn.nukkit.network.protocol.PlayerActionPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.network.protocol.UseItemPacket;
import sote.Game;
import sote.GameMob;
import sote.GameProvider;
import sote.Main;
import sote.bedwars.Bedwars;
import sote.blockhunt.Blockhunt;
//import sote.SendMapPacket;
import sote.inventory.Inventorys;
import sote.inventory.ServerChestInventorys;
import sote.inventory.miniwalls.MiniwallsUpgraderChestInventory;
import sote.lobbyitem.LobbyItems;
import sote.login.LoginSystem;
import sote.miniwalls.Miniwalls;
import sote.murder.Murder;
import sote.murder.weapon.Weapons;
import sote.skywarssolo.kit.SkywarsSoloKits;

public class Event_DataPacketReceiveEvent{

    public Event_DataPacketReceiveEvent(){
    }

    public static void onMobAttackk(DataPacketReceiveEvent event){
        Player player = event.getPlayer();
        Game game = GameProvider.getPlayingGame(player);
        if(game instanceof Murder){
            Murder murder = (Murder) game;
            murder.attack2(event);
        }
        DataPacket packet = event.getPacket();
        if(packet instanceof UseItemPacket){
            if(LoginSystem.auth.get(player.getName().toLowerCase()) == 1){
                return;
            }
            UseItemPacket use = (UseItemPacket) packet;
            LobbyItems.shot(player);
            SkywarsSoloKits.shot(player,use.item);
            if(player.getGamemode() != 0) Weapons.shot(player,use.item);
            Inventorys.Function(player,use.item);
            Vector3 blockVector = new Vector3(use.x, use.y, use.z);
            game = GameProvider.getPlayingGame(player);
            if(game instanceof Blockhunt){
                Blockhunt blockhunt = (Blockhunt) game;
                blockhunt.onInteract(player, blockVector);
            }
            if (use.item.getId() == Item.SPLASH_POTION) {
                CompoundTag nbt = new CompoundTag()
                        .putList(new ListTag<DoubleTag>("Pos")
                                .add(new DoubleTag("", player.x))
                                .add(new DoubleTag("", player.y + player.getEyeHeight()))
                                .add(new DoubleTag("", player.z)))
                        .putList(new ListTag<DoubleTag>("Motion")
                                .add(new DoubleTag("", -Math.sin(player.yaw / 180 * Math.PI) * Math.cos(player.pitch / 180 * Math.PI)))
                                .add(new DoubleTag("", -Math.sin(player.pitch / 180 * Math.PI)))
                                .add(new DoubleTag("", Math.cos(player.yaw / 180 * Math.PI) * Math.cos(player.pitch / 180 * Math.PI))))
                        .putList(new ListTag<FloatTag>("Rotation")
                                .add(new FloatTag("", (float) player.yaw))
                                .add(new FloatTag("", (float) player.pitch)))
                        .putShort("PotionId", use.item.getDamage());
                double f = 1.5;
                Entity bottle = new EntityPotion(player.chunk, nbt, player);
                bottle.setMotion(bottle.getMotion().multiply(f));
                if (player.isSurvival()) {
                    use.item.setCount(use.item.getCount() - 1);
                    player.getInventory().setItemInHand(use.item.getCount() > 0 ? use.item : Item.get(Item.AIR));
                }
                if (bottle instanceof EntityPotion) {
                    EntityPotion bottleEntity = (EntityPotion) bottle;
                    ProjectileLaunchEvent projectileEv = new ProjectileLaunchEvent(bottleEntity);
                    Server.getInstance().getPluginManager().callEvent(projectileEv);
                    if (projectileEv.isCancelled()) {
                        bottle.kill();
                    } else {
                        bottle.spawnToAll();
                        player.level.addSound(new LaunchSound(player), player.getViewers().values());
                    }
                } else {
                    bottle.spawnToAll();
                }
            }
        }else if(packet instanceof InteractPacket){
            Long eid = ((InteractPacket)packet).target;
            game = GameProvider.getPlayingGame(player);
            if(game instanceof Murder){
                Murder murder = (Murder) game;
                if(murder.Players.containsValue(player) && player.getGamemode() == 0){
                    event.setCancelled();
                }
            }else if(game instanceof Miniwalls){
                Miniwalls miniwalls = (Miniwalls) game;
                if(((InteractPacket)packet).action != InteractPacket.ACTION_MOUSEOVER && game.Players.containsValue(player) && miniwalls.getGameDataAsBoolean("gamenow")){
                    miniwalls.Attack(player, eid);
                }
            }else if(game instanceof Bedwars){
                Bedwars bedwars = (Bedwars) game;
                if(((InteractPacket)packet).action != InteractPacket.ACTION_MOUSEOVER && game.Players.containsValue(player) && bedwars.getGameDataAsBoolean("gamenow")){
                    bedwars.attack(player, eid);
                }
            }
            Entity targetEntity = player.getLevel().getEntity(eid);
            if(targetEntity instanceof EntityVehicle && player.getGamemode() != 1) event.setCancelled();
            if(((InteractPacket)packet).action != InteractPacket.ACTION_MOUSEOVER) SkywarsSoloKits.touch(player,eid);
            if(((InteractPacket)packet).action != InteractPacket.ACTION_MOUSEOVER) GameMob.touch(player, eid);
            if(((InteractPacket)packet).action == InteractPacket.ACTION_LEFT_CLICK){
                game = GameProvider.getPlayingGame(player);
                if(game instanceof Blockhunt){
                    Blockhunt blockhunt = (Blockhunt) game;
                    blockhunt.attack(player, eid);
                }
            }
            if(((InteractPacket)packet).action  != InteractPacket.ACTION_MOUSEOVER && eid == Main.wolf){
                if(!lastTame.containsKey(player)) lastTame.put(player,  (long)0);
                if(System.currentTimeMillis() - lastTame.get(player) >= 2000){
                    int random = (int)(Math.random() * 15);
                    if(random <= 3){
                        EntityEventPacket pk = new EntityEventPacket();
                        pk.eid = Main.wolf;
                        pk.event = EntityEventPacket.SHAKE_WET;
                        player.dataPacket(pk);
                    }else if(random >= 4 && random <= 11){
                        EntityEventPacket pk = new EntityEventPacket();
                        pk.eid = Main.wolf;
                        pk.event = EntityEventPacket.TAME_FAIL;
                        player.dataPacket(pk);
                    }else if(random >= 12){
                        EntityEventPacket pk = new EntityEventPacket();
                        pk.eid = Main.wolf;
                        pk.event = EntityEventPacket.TAME_SUCCESS;
                        player.dataPacket(pk);
                    }
                }
            }else if(((InteractPacket)packet).action != InteractPacket.ACTION_MOUSEOVER && eid == Main.MiniwallsUpgrader){
                ServerChestInventorys.setData(player, new MiniwallsUpgraderChestInventory(player));
            }else if(((InteractPacket)packet).action == InteractPacket.ACTION_LEFT_CLICK && eid == Main.TestEid2){
                System.out.println("aaaa");
            }
        }else if(packet instanceof PlayerActionPacket){
            Vector3 pos = new Vector3(((PlayerActionPacket) packet).x, ((PlayerActionPacket) packet).y, ((PlayerActionPacket) packet).z);
            switch (((PlayerActionPacket) packet).action) {
                case 17:
                    game = GameProvider.getPlayingGame(player);
                    if(game instanceof Blockhunt){
                        Blockhunt blockhunt = (Blockhunt) game;
                        blockhunt.onInteract(player, pos);
                    }
                break;
            }
        }
        switch(packet.pid()){
            case ProtocolInfo.LOGIN_PACKET:
                LoginPacket login = (LoginPacket)packet;
                String name = login.username;
                if(!Server.getInstance().isWhitelisted(name)){
                    player.close("","サーバーメンテナンス中です。\nTwitterまたはLobiをご確認ください。");
                }
                Main.Edition.put(player, (int)login.gameEdition);
                login.clientData.remove("SkinData");
                Inventorys.setGUI(login.username, login.clientData.get("UIProfile").getAsInt());
                System.out.println(login.clientData.toString());
                //if(!login.isXbox){
                //    if(OPStatue.eids.containsKey(login.username)) player.close("","このサーバーで遊ぶにはxboxでログインする必要があります");
                //}
                //System.out.println(login.username+"*"+login.gameEdition);
                //login.protocol = 84;
            break;
            case ProtocolInfo.SET_PLAYER_GAME_TYPE_PACKET:
                event.setCancelled();
            break;
            case ProtocolInfo.CONTAINER_SET_SLOT_PACKET:
                ContainerSetSlotPacket cpk = (ContainerSetSlotPacket) packet;
                if(cpk.windowid != 0) ServerChestInventorys.Function(player, cpk.slot, cpk.item, cpk.windowid);
            break;
            case ProtocolInfo.CONTAINER_CLOSE_PACKET:
                ContainerClosePacket ccpk = (ContainerClosePacket) packet;
                ServerChestInventorys.close(player, ccpk.windowid);
            break;
        }
    }

    public static HashMap<Player, Long> lastTame = new HashMap<Player, Long>();

}