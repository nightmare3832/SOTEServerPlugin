package sote.event;

import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.item.EntityPrimedTNT;
import cn.nukkit.entity.projectile.EntityLargeFireball;
import cn.nukkit.event.entity.EntityExplodeEvent;
import sote.Game;
import sote.GameProvider;
import sote.Main;
import sote.bedwars.Bedwars;

public class Event_EntityExplodeEvent{

    public Event_EntityExplodeEvent(){
    }

    public static void onExplosion(EntityExplodeEvent event){
        if(event.getEntity().getLevel().getFolderName().split("bedwars").length >= 2){
            if(event.getEntity() instanceof EntityLargeFireball){
                List<Block> blocks = event.getBlockList();
                Player player = null;
                if(Main.gameEntity.get(event.getEntity()) instanceof Player){
                    player = (Player) Main.gameEntity.get(event.getEntity());
                }
                Bedwars bedwars = null;
                if(player != null){
                    Game game = GameProvider.getPlayingGame(player);
                    if(!(game instanceof Bedwars)) return;
                    bedwars = (Bedwars) game;
                }
                for(Block block : blocks){
                    if(block.getId() == Block.SANDSTONE || block.getId() == Block.END_STONE || block.getId() == Block.OBSIDIAN){
                        blocks.remove(block);
                    }
                    if(bedwars != null){
                        if(!bedwars.isPlacedByPlayer(block)){
                            System.out.println("remove");
                            blocks.remove(block);
                        }
                    }
                }
                event.setBlockList(blocks);
            }
            if(event.getEntity() instanceof EntityPrimedTNT){
                List<Block> blocks = event.getBlockList();
                Player player = null;
                if(Main.gameEntity.get(event.getEntity()) instanceof Player){
                    player = (Player) Main.gameEntity.get(event.getEntity());
                }
                Bedwars bedwars = null;
                if(player != null){
                    Game game = GameProvider.getPlayingGame(player);
                    if(!(game instanceof Bedwars)) return;
                    bedwars = (Bedwars) game;
                }
                for(Block block : blocks){
                    if(block.getId() == Block.OBSIDIAN){
                        blocks.remove(block);
                    }
                    if(bedwars != null){
                        if(!bedwars.isPlacedByPlayer(block)){
                            System.out.println("remove");
                            blocks.remove(block);
                        }
                    }
                }
                event.setBlockList(blocks);
            }
        }
    }
}