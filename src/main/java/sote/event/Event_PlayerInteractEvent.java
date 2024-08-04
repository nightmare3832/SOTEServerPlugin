package sote.event;

import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockButton;
import cn.nukkit.block.BlockDoor;
import cn.nukkit.block.BlockLever;
import cn.nukkit.block.BlockTrapdoor;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntitySign;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import sote.Game;
import sote.GameProvider;
import sote.Main;
import sote.bedwars.Bedwars;
import sote.buildbattle.Buildbattle;
import sote.home.Home;
import sote.inventory.Inventorys;
import sote.inventory.ServerChestInventorys;
import sote.inventory.buildbattle.BuildbattleLobbyInventory;
import sote.inventory.home.HomeGameSelectChestInventory;
import sote.inventory.miniwalls.MiniwallsLobbyInventory;
import sote.inventory.murder.MurderLobbyInventory;
import sote.inventory.skywars.SkywarsSoloLobbyInventory;
import sote.login.LoginSystem;
import sote.murder.Murder;
import sote.murder.achievements.MurderAchievements;
import sote.skywarssolo.SkywarsSolo;

public class Event_PlayerInteractEvent{

    public Event_PlayerInteractEvent(){
    }

    public static void onTouch(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(LoginSystem.auth.get(player.getName().toLowerCase()) == 1){
            event.setCancelled();
            return;
        }
        Item item = event.getItem();
        Block block = event.getBlock();
            if(block.getId() == 63 || block.getId() == 68){
                BlockEntity blockentity = player.getLevel().getBlockEntity(block);
                if(blockentity instanceof BlockEntitySign){
                    BlockEntitySign sign = (BlockEntitySign) blockentity;
                    String[] line = sign.getText();
                    String lines = line[0]+line[1]+line[2]+line[3];
                    for(Map.Entry<String, Class<? extends Game>> e : GameProvider.Game.entrySet()){
                        String[] md = lines.split(e.getKey());
                        if(md.length >= 2){
                            if(isNumber(md[1].substring(0,md[1].length()-2))){
                                if(!GameProvider.Games.containsKey(Integer.parseInt(md[1].substring(0,md[1].length()-2)))){
                                    GameProvider.register(Integer.parseInt(md[1].substring(0,md[1].length()-2)), false);
                                }
                                GameProvider.Games.get(Integer.parseInt(md[1].substring(0,md[1].length()-2))).get(e.getKey()).Join(player);
                            }
                        }
                    }
                }
                touchSign(player,block);
            }
            Game game = GameProvider.getPlayingGame(player);
            if(game instanceof Buildbattle && Main.getBlock(player) == false && player.getGamemode() == 1){
                if(!game.Players.containsValue(player)) event.setCancelled();
            }
        if(player.getGamemode() == 2) event.setCancelled();
        if(player.getGamemode() == 3) event.setCancelled();
        game = GameProvider.getPlayingGame(player);
        if(game instanceof Murder){
            Murder murder = (Murder) game;
            if(murder.jobs.containsKey(player)){
                if(player.getGamemode() == 0) event.setCancelled();
                else if(player.getGamemode() == 2){
                    if(event.getAction() == PlayerInteractEvent.Action.PHYSICAL) event.setCancelled();
                    else if(block instanceof BlockDoor || 
                            block instanceof BlockLever || 
                            block instanceof BlockButton || 
                            block instanceof BlockTrapdoor)
                        event.setCancelled(false);
                    else event.setCancelled();
                }
            }
        }
        if((block.getId() == 145 || block.getId() == 54) && !Main.getBlock(player)) event.setCancelled();
        if(game instanceof SkywarsSolo){
            if(game.Players.containsValue(player) && game.getGameDataAsBoolean("gamenow") && player.getGamemode() == 0){
                event.setCancelled(false);
            }
        }
        if(game instanceof Bedwars){
            if(game.Players.containsValue(player) && game.getGameDataAsBoolean("gamenow") && player.getGamemode() == 0){
                event.setCancelled(false);
                ((Bedwars)game).onInteract(event);
            }
        }
        if(player.getLevel().getFolderName().split("home").length >= 2){
            if(block.getId() != 0) event.setCancelled(false);
            if(block.getId() == 116){
                if((Home.homeInfo.get(player.getLevel().getFolderName().split("home")[1])).get("owner").equals(player.getName().toLowerCase())){
                    ServerChestInventorys.setData(player,new HomeGameSelectChestInventory(player));
                }
                event.setCancelled();
            }
        }
        if(player.getLevel().getFolderName().split("buildbattle").length >= 2 && (item.getId() == 383 || item.getId() == 438)){
            event.setCancelled();
            game = GameProvider.getPlayingGame(player);
            if(game instanceof Buildbattle){
                Buildbattle buildbattle = (Buildbattle) game;
                buildbattle.onInteract(event);
            }
        }
    }

    public static boolean isNumber(String num){
        try {
            Integer.parseInt(num);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public static void touchSign(Player player,Block block){
        BlockEntity blockentity = player.getLevel().getBlockEntity(block);
            if(blockentity instanceof BlockEntitySign){
                BlockEntitySign sign = (BlockEntitySign) blockentity;
                String[] line = sign.getText();
                if(line[1].equals("§cMurder")){
                    Inventorys.setData(player, new MurderLobbyInventory());
                    player.teleport(new Position(-246,6,-234,Server.getInstance().getLevelByName("murder")));
                }
                if(line[1].equals("§bSkyWars")){
                    Inventorys.setData(player, new SkywarsSoloLobbyInventory());
                    player.teleport(new Position(-32,57,13,Server.getInstance().getLevelByName("skywars")));
                }
                if(line[1].equals("§6BuildBattle")){
                    Inventorys.setData(player, new BuildbattleLobbyInventory());
                    player.teleport(new Position(88.5,16,85.5,Server.getInstance().getLevelByName("buildbattle")));
                }
                if(line[1].equals("§aWalls")){
                    Inventorys.setData(player, new MiniwallsLobbyInventory());
                    player.teleport(new Position(2.5,7,-39.5,Server.getInstance().getLevelByName("walls")));
                }
                if(GameProvider.getPlayingGame(player) instanceof Murder){
                    if(sign.x == 128 && sign.y == 10 && sign.z == 127){
                        MurderAchievements.setLevel(player, "Parkour_Around_Murder", 2);
                    }
                }
            }
    }
}