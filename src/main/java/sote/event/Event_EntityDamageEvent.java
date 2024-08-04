package sote.event;

import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityPotion;
import cn.nukkit.event.entity.EntityDamageByChildEntityEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.Position;
import cn.nukkit.level.particle.InstantSpellParticle;
import cn.nukkit.level.particle.Particle;
import cn.nukkit.level.particle.SpellParticle;
import cn.nukkit.potion.Effect;
import cn.nukkit.potion.Potion;
import sote.Game;
import sote.GameProvider;
import sote.Main;
import sote.bedwars.Bedwars;
import sote.blockhunt.Blockhunt;
import sote.miniwalls.Miniwalls;
import sote.murder.Murder;
import sote.skywarssolo.SkywarsSolo;
import sote.skywarssolo.kit.SkywarsSoloKits;

public class Event_EntityDamageEvent{

    public Event_EntityDamageEvent(){
    }

    public static void onHit(EntityDamageEvent event){
        Boolean a = false;
        Entity e = event.getEntity();
        if(e instanceof Player){
            lastcause.put((Player)e, event);
        }
        boolean checkIntervalDamage = false;
        if(event.getCause() != EntityDamageEvent.DamageCause.LAVA && event.getCause() != EntityDamageEvent.DamageCause.CONTACT && event.getCause() != EntityDamageEvent.DamageCause.FIRE && event.getCause() != EntityDamageEvent.DamageCause.FIRE_TICK){
            checkIntervalDamage = true;
        }else if(!event.isCancelled()){
            checkIntervalDamage = true;
        }
        if(event instanceof EntityDamageByChildEntityEvent){
            EntityDamageByChildEntityEvent ev = (EntityDamageByChildEntityEvent)event;
            //ev.setKnockBack((float)(ev.getKnockBack()*0.5));
            Entity damager = ev.getDamager();
            Entity entity = ev.getEntity();
            if(damager instanceof Player && entity instanceof Player){
                Player pentity = (Player) entity;
                Player pdamager = (Player) damager;
                if(!pentity.equals(pdamager)){
                    lastdamage.put(pentity,pdamager);
                    lastdamagetime.put(pentity, System.currentTimeMillis());
                }
                if(ev.getChild() instanceof EntityPotion){
                    Entity po = ev.getChild();
                    EntityPotion pot = (EntityPotion)po;
                    Potion potion = Potion.getPotion(pot.potionId);
                    if (potion == null) return;
                    potion.setSplash(true);
                    Particle particle;
                    int r;
                    int g;
                    int bb;
                    Effect effect = Potion.getEffect(potion.getId(), true);
                    if (effect == null) {
                        r = 40;
                        g = 40;
                        bb = 255;
                    } else {
                        int[] colors = effect.getColor();
                        r = colors[0];
                        g = colors[1];
                        bb = colors[2];
                    }
                    if (Potion.isInstant(potion.getId())) {
                        particle = new InstantSpellParticle(po, r, g, bb);
                    } else {
                        particle = new SpellParticle(po, r, g, bb);
                    }
                    po.getLevel().addParticle(particle);
                    Entity[] entities = po.getLevel().getNearbyEntities(po.getBoundingBox().grow(8.25, 4.24, 8.25));
                    for (Entity anEntity : entities) {
                        potion.applyPotion(anEntity);
                    }
                }
                Game game = GameProvider.getPlayingGame(pdamager);
                if(!(game instanceof SkywarsSolo) && !(game instanceof Miniwalls) && !(game instanceof Bedwars)){
                    Entity projectile = ev.getChild();
                    ev.setKnockBack(0);
                    ev.setDamage(0);
                    if(game instanceof Murder){
                        Murder murder = (Murder) game;
                        murder.hit(pdamager, pentity);
                    }
                    event.setCancelled();
                }
            }
        }else if(event instanceof EntityDamageByEntityEvent){
            EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent)event;
            Entity damager = ev.getDamager();
            Entity entity = ev.getEntity();
            //ev.setKnockBack((float)(ev.getKnockBack()*0.5));
                if(damager instanceof Player && entity instanceof Player){
                    Player pentity = (Player) entity;
                    Player dplayer = (Player) damager;
                    Game game = GameProvider.getPlayingGame(pentity);
                    if(game instanceof Murder){
                        Murder murder = (Murder) game;
                        murder.attack(pentity, dplayer, event);
                    }
                    lastdamage.put(pentity,dplayer);
                    lastdamagetime.put(pentity, System.currentTimeMillis());
                    notPlayer.put(pentity, false);
                    event.setCancelled();
                }
            if(damager instanceof Player){
                Player ddplayer = (Player) damager;
                if(ddplayer.getGamemode() != 1) event.setCancelled();
            }
            a = true;
        }
        SkywarsSoloKits.attack(event);
        if(event.getCause() == EntityDamageEvent.DamageCause.VOID){
            Entity entity = event.getEntity();
            if(entity instanceof Player){
                Player pentity = (Player) entity;
                Game game = GameProvider.getPlayingGame(pentity);
                if(game instanceof SkywarsSolo){
                    SkywarsSolo skywars = (SkywarsSolo) game;
                    if(!game.getGameDataAsBoolean("gamenow")){
                        skywars.Teleport(pentity);
                        event.setCancelled();
                    }
                }else if(pentity.getLevel().getFolderName().equals("skywars")){
                    pentity.teleport(new Position(-32,57,13,Server.getInstance().getLevelByName("skywars")));
                    event.setCancelled();
                }if(pentity.getLevel().getFolderName().split("home").length >= 2){
                    pentity.teleport(pentity.getLevel().getSafeSpawn());
                    event.setCancelled();
                }if(game instanceof Miniwalls){
                    Miniwalls miniwalls = (Miniwalls) game;
                    if(!miniwalls.getGameDataAsBoolean("gamenow")){
                        miniwalls.Teleport(pentity);
                        event.setCancelled();
                    }else{
                        pentity.teleport(pentity.add(0,50,0));
                        if(notPlayer.containsKey(pentity) && notPlayer.get(pentity)){
                            miniwalls.kill(lastName.get(pentity), pentity);
                        }else if(lastdamage.containsKey(pentity)){
                            Game game2 = GameProvider.getPlayingGame(lastdamage.get(pentity));
                            if(game2.Players.containsValue(lastdamage.get(pentity)) && miniwalls.number == game2.number){
                                miniwalls.kill(lastdamage.get(pentity), pentity);
                            }
                        }
                        miniwalls.death(pentity);
                    }
                }
            }
        }
        Entity entityy = event.getEntity();
        String ln = entityy.getLevel().getFolderName();
        if((ln.split("home").length >= 2||
        ln.split("skywars").length >= 2||
        ln.equals("skywars")||
        ln.split("bedwars").length >= 2||
        ln.equals("bedwars")||
        ln.split("murder").length >= 2||
        ln.equals("murder")||
        ln.split("buildbattle").length >= 2||
        ln.equals("buildbattle")||
        ln.split("miniwalls").length >= 2||
        ln.equals("walls")||
        ln.equals("lobby")||
        ln.equals("arcade"))&&
        !a) event.setCancelled();
        Entity entity = event.getEntity();
        if(entity instanceof Player){
            Player player = (Player) entity;
            Game game = GameProvider.getPlayingGame(player);
            if(game instanceof SkywarsSolo && game.Players.containsValue(player) && game.getGameDataAsBoolean("gamenow")){
                SkywarsSolo skywars = (SkywarsSolo) game;
                if(player.getGamemode() != 3){
                    if(checkIntervalDamage)
                    event.setCancelled(false);
                    float damage = event.getFinalDamage();
                    if(event instanceof EntityDamageByEntityEvent){
                        EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent)event;
                        Entity damager = ev.getDamager();
                        Entity entit = ev.getEntity();
                            if(damager instanceof Player && entit instanceof Player){
                                Player dplayer = (Player) damager;
                                //if(!dplayer.onGround) damage *= 1.5;
                                if(System.currentTimeMillis() - Main.lastAttack.get(dplayer) >= ATTACK_INTERVAL){
                                    Main.lastAttack.put(dplayer, System.currentTimeMillis());
                                }else{
                                    event.setCancelled();
                                }
                                if(dplayer.getGamemode() == 3){
                                    event.setCancelled();
                                }
                            }
                    }
                    if((int)(player.getHealth() - damage) <= 0 && !event.isCancelled()){
                        event.setCancelled();
                        if(lastdamage.containsKey(player)){
                            Game game2 = GameProvider.getPlayingGame(lastdamage.get(player));
                            if(game2 instanceof SkywarsSolo && game2.Players.containsValue(lastdamage.get(player)) && game.number == game2.number && 
                                lastdamagetime.containsKey(player) && lastdamagetime.get(player) - System.currentTimeMillis() <= 8000){
                                skywars.kill(lastdamage.get(player), player);
                                skywars.death(player, true, false);
                            }else skywars.death(player, false, false);
                        }else skywars.death(player, false, false);
                    }
                }else{
                    event.setCancelled();
                }
            }
            if(game instanceof Miniwalls && game.Players.containsValue(player) && game.getGameDataAsBoolean("gamenow") && player.getGamemode() == 0){
                Miniwalls miniwalls = (Miniwalls) game;
                event.setCancelled(false);
                float damage = event.getFinalDamage();
                if(event instanceof EntityDamageByEntityEvent){
                    EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent)event;
                    Entity damager = ev.getDamager();
                    Entity entit = ev.getEntity();
                        if(damager instanceof Player && entit instanceof Player){
                            Player dplayer = (Player) damager;
                            //if(!dplayer.onGround) damage *= 1.5;
                            if(miniwalls.team.containsKey(dplayer) && miniwalls.team.containsKey(entit) && miniwalls.team.get(dplayer) == miniwalls.team.get(entit)){
                                event.setCancelled();
                            }
                            if(System.currentTimeMillis() - Main.lastAttack.get(dplayer) >= ATTACK_INTERVAL){
                                Main.lastAttack.put(dplayer, System.currentTimeMillis());
                            }else{
                                event.setCancelled();
                            }
                            if(dplayer.getGamemode() == 3){
                                event.setCancelled();
                            }
                        }
                }
                if(player.getHealth() - damage <= 0 && !event.isCancelled()){
                    event.setCancelled();
                    if(notPlayer.containsKey(player) && notPlayer.get(player)){
                        miniwalls.kill(lastName.get(player), player);
                    }else if(lastdamage.containsKey(player)){
                        Game game2 = GameProvider.getPlayingGame(lastdamage.get(player));
                        if(game2.Players.containsKey(lastdamage.get(player)) && game.number == game2.number){
                            miniwalls.kill(lastdamage.get(player), player);
                        }
                    }
                    miniwalls.death(player);
                }
            }
            if(game instanceof Bedwars && game.Players.containsValue(player) && game.getGameDataAsBoolean("gamenow") && player.getGamemode() == 0){
                Bedwars bedwars = (Bedwars) game;
                event.setCancelled(false);
                float damage = event.getFinalDamage();
                if(event instanceof EntityDamageByEntityEvent){
                    EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent)event;
                    Entity damager = ev.getDamager();
                    Entity entit = ev.getEntity();
                        if(damager instanceof Player && entit instanceof Player){
                            Player dplayer = (Player) damager;
                            //if(!dplayer.onGround) damage *= 1.5;
                            if(bedwars.team.containsKey(dplayer) && bedwars.team.containsKey(entit) && bedwars.team.get(dplayer) == bedwars.team.get(entit)){
                                event.setCancelled();
                            }
                            if(System.currentTimeMillis() - Main.lastAttack.get(dplayer) >= ATTACK_INTERVAL){
                                Main.lastAttack.put(dplayer, System.currentTimeMillis());
                            }else{
                                event.setCancelled();
                            }
                            if(dplayer.getGamemode() == 3){
                                event.setCancelled();
                            }
                        }
                }
                if((int)(player.getHealth() - damage) <= 0 && !event.isCancelled()){
                    event.setCancelled();
                    if(lastdamage.containsKey(player)){
                        Game game2 = GameProvider.getPlayingGame(lastdamage.get(player));
                        if(game2 instanceof Bedwars && game2.Players.containsValue(lastdamage.get(player)) && game.number == game2.number && 
                            lastdamagetime.containsKey(player) && lastdamagetime.get(player) - System.currentTimeMillis() <= 8000){
                            bedwars.kill(lastdamage.get(player), player);
                            bedwars.death(player, true, false);
                        }else bedwars.death(player, false, false);
                    }else bedwars.death(player, false, false);
                }
            }
            if(game instanceof Blockhunt && game.Players.containsValue(player) && game.getGameDataAsBoolean("gamenow")){
                Blockhunt blockhunt = (Blockhunt) game;
                float damage = event.getFinalDamage();
                if(event instanceof EntityDamageByEntityEvent){
                    event.setCancelled(false);
                    EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent)event;
                    Entity damager = ev.getDamager();
                    Entity entit = ev.getEntity();
                        if(damager instanceof Player && entit instanceof Player){
                            Player dplayer = (Player) damager;
                            //if(!dplayer.onGround) damage *= 1.5;
                            if(blockhunt.jobs.get(player) == blockhunt.jobs.get(dplayer)){
                                event.setCancelled();
                            }
                            if(System.currentTimeMillis() - Main.lastAttack.get(dplayer) >= ATTACK_INTERVAL){
                                Main.lastAttack.put(dplayer, System.currentTimeMillis());
                            }else{
                                event.setCancelled();
                            }
                            if(player.getHealth() - damage <= 0 && !event.isCancelled()){
                                event.setCancelled();
                                blockhunt.death(player, dplayer);
                            }
                        }else{
                            event.setCancelled();
                        }
                }else{
                    event.setCancelled();
                }
            }
        }
    }

    public static void reset(Player player){
        lastdamage.remove(player);
        lastdamagetime.remove(player);
        lastcause.remove(player);
        notPlayer.remove(player);
        lastName.remove(player);
    }

    public static final long ATTACK_INTERVAL = 300;

    public static HashMap<Player,Player> lastdamage = new HashMap<Player,Player>();
    public static HashMap<Player,Long> lastdamagetime = new HashMap<Player,Long>();
    public static HashMap<Player,EntityDamageEvent> lastcause = new HashMap<Player,EntityDamageEvent>();
    public static HashMap<Player,Boolean> notPlayer = new HashMap<Player,Boolean>();
    public static HashMap<Player,String> lastName = new HashMap<Player,String>();
}