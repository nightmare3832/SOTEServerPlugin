package sote.murder.weapon;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.ProjectileHitEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;
import cn.nukkit.scheduler.Task;
import sote.Game;
import sote.GameProvider;
import sote.Main;
import sote.inventory.Inventorys;
import sote.murder.Murder;
import sote.murder.upgrade.MurderUpgrades;
import sote.stat.Stat;

public class Weapons{

    public static final int PRICE_WOODEN = 0;
    public static final int PRICE_STONE = 15000;
    public static final int PRICE_IRON = 30000;
    public static final int PRICE_GOLD = 50000;
    public static final int PRICE_DIAMOND = 100000;

    public Weapons(){
    }

    public static void buyItem(Player player,String str){
        int coin = 0;
            switch(str){
                case "Wooden":
                    coin = PRICE_WOODEN;
                break;
                case "Stone":
                    coin = PRICE_STONE;
                break;
                case "Iron":
                    coin = PRICE_IRON;
                break;
                case "Gold":
                    coin = PRICE_GOLD;
                break;
                case "Diamond":
                    coin = PRICE_DIAMOND;
                break;
            }
        Map<String, Boolean> map = weaponData.get(player.getName().toLowerCase());
        if(!map.containsKey(str)) map.put(str, false);
        if(map.get(str)) return;
            if(Stat.getCoin(player) >= coin){
                ((Map<String, Boolean>) weaponData.get(player.getName().toLowerCase())).put(str,true);
                Stat.setCoin(player,Stat.getCoin(player)-coin);
                player.sendMessage(Main.getMessage(player,"shop.buy.item"));
            }else{
                player.sendMessage(Main.getMessage(player,"shop.no.coin"));
            }
    }

    public static void addPlayer(Player player){
        HashMap<String, Boolean> v1 = new HashMap<String, Boolean>();
        setSelectWeapon(player,"Wooden");
        v1.put("Wooden",true);
        v1.put("Stone",false);
        v1.put("Iron",false);
        v1.put("Gold",false);
        v1.put("Diamond",false);
        weaponData.put(player.getName().toLowerCase(),v1);
        setSelectKnifeData(player);
        setSelectGunData(player);
    }

    public static String getSelectWeaponString(Player player){
        return String.valueOf(selectWeapon.get(player.getName().toLowerCase()));
    }

    public static Weapon getSelectKnife(Player player){
        return weaponsKnife.get(player);
    }

    public static void setSelectWeapon(Player player,String str){
        selectWeapon.put(player.getName().toLowerCase(),str);
        setSelectGunData(player);
        setSelectKnifeData(player);
    }

    public static Weapon getSelectGun(Player player){
        return weaponsGun.get(player);
    }

    public static void setSelectKnifeData(Player player){
        String str = selectWeapon.get(player.getName().toLowerCase()).toString();
        switch(str){
            case "Wooden":
                weaponsKnife.put(player,new WoodenSword());
            break;
            case "Stone":
                weaponsKnife.put(player,new StoneSword());
            break;
            case "Iron":
                weaponsKnife.put(player,new IronSword());
            break;
            case "Gold":
                weaponsKnife.put(player,new GoldenSword());
            break;
            case "Diamond":
                weaponsKnife.put(player,new DiamondSword());
            break;
        }
    }

    public static void setSelectGunData(Player player){
        String str = selectWeapon.get(player.getName().toLowerCase()).toString();
        switch(str){
            case "Wooden":
                weaponsGun.put(player,new WoodenHoe());
            break;
            case "Stone":
                weaponsGun.put(player,new StoneHoe());
            break;
            case "Iron":
                weaponsGun.put(player,new IronHoe());
            break;
            case "Gold":
                weaponsGun.put(player,new GoldenHoe());
            break;
            case "Diamond":
                weaponsGun.put(player,new DiamondHoe());
            break;
        }
    }

    public static void hit(ProjectileHitEvent event){
        EntityProjectile projectile = (EntityProjectile)event.getEntity();
        Entity entity = projectile.shootingEntity;
            if(entity instanceof Player){
                Player player = (Player) entity;
                if(projectile instanceof EntityArrow){
                    projectile.kill();
                }
                Game game = GameProvider.getPlayingGame(player);
                if(!(game instanceof Murder)) return;
                Murder murder = (Murder) game;
                if(murder.jobs.containsKey(player) && murder.jobs.get(player) == 1){
                    weaponsGun.get(player).despawn(player, projectile);
                }
                if(murder.jobs.containsKey(player) && murder.jobs.get(player) == 0){
                    weaponsKnife.get(player).despawn(player, projectile);
                }
            }
    }

    public static void shot(PlayerInteractEvent event){
        if(event.getBlock().getId() != 0) return;
        Player player = event.getPlayer();
        Item item = event.getItem();
        Game game = GameProvider.getPlayingGame(player);
        if(!(game instanceof Murder)) return;
        Murder murder = (Murder) game;
            if(weaponsGun.get(player).getItem(player).getId() == item.getId() && murder.jobs.containsKey(player) && murder.jobs.get(player) == 1){
                if(weaponsGun.get(player).getCharged()){
                    weaponsGun.get(player).Shot(player);
                    Charge(player,weaponsGun.get(player).getChargeTime());
                    weaponsGun.get(player).setCharged(false);
                }
            }
            if(weaponsKnife.get(player).getItem(player).getId() == item.getId() && murder.jobs.containsKey(player) && murder.jobs.get(player) == 0){
                weaponsKnife.get(player).Shot(player);
            }
    }

    public static void shot(Player player,Item item){
        Game game = GameProvider.getPlayingGame(player);
        if(!(game instanceof Murder)) return;
        Murder murder = (Murder) game;
        if(weaponsGun.get(player).getItem(player).getId() == item.getId() && murder.jobs.containsKey(player) && murder.jobs.get(player) == 1){
            if(weaponsGun.get(player).getCharged()){
                weaponsGun.get(player).Shot(player);
                Charge(player,weaponsGun.get(player).getChargeTime());
                weaponsGun.get(player).setCharged(false);
            }
        }
        if(weaponsKnife.get(player).getItem(player).getId() == item.getId() && murder.jobs.containsKey(player) && murder.jobs.get(player) == 0){
            weaponsKnife.get(player).Shot(player);
        }
    }

    public static void Charge(Player player,int time){
        if(time == 0) return;
        if(MurderUpgrades.getResult(player, "Gun Upgrade")) time = (int)(time / 2);
        double exp = 0;
        for(int i = 0;i <= time*20;i++){
            exp = i/(double)(time*20);
            Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackCharge(player,exp),i);
        }
    }

    public static void ChargeExp(Player player,double exp){
        if(exp <= 1) player.setAttribute(Attribute.getAttribute(Attribute.EXPERIENCE).setValue((float)exp));
        if(exp == 1) weaponsGun.get(player).setCharged(true);
    }

    public static void Return(Player player,int time,int damage){
        if(MurderUpgrades.getResult(player, "Knife Upgrade")) time = (int)(time / 2);
        Task task = new CallbackReturn(player);
        int id = new CallbackReturn(player).getTaskId();
        Server.getInstance().getScheduler().scheduleDelayedTask(task,time*20);
        taskid.put(damage, id);
    }

    public static void ReturnItem(Player player){
        EntityItem entityitem = weaponsKnife.get(player).getDropItem();
        Game game = GameProvider.getPlayingGame(player);
        if(!(game instanceof Murder)) return;
        Murder murder = (Murder) game;
        if(taskid.containsKey(entityitem.getItem().getDamage())){
            murder.knifecount.put(player, murder.knifecount.get(player) + 1);
            Inventorys.data2.get(player).register(player);
            entityitem.kill();
        }
    }

    public static Item getItemGun(Player player){
        return weaponsGun.get(player).getItem(player);
    }

    public static Item getItemKnife(Player player){
        return weaponsKnife.get(player).getItem(player);
    }

    public static void pickupKnife(int damage){
        if(taskid.containsKey(damage)) taskid.remove(damage);
    }

    public static void reset(Player player, boolean levelReset){
        Weapon gun = getSelectGun(player);
        gun.setCharged(true);
        if(levelReset) gun.setLevel(1);
    }

    public static String getWeapons(Player player){
        Map<String, Boolean> map = weaponData.get(player.getName().toLowerCase());
        return new GsonBuilder().setPrettyPrinting().create().toJson(map);
    }

    public static void setWeapons(Player player, String str){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        HashMap<String, Boolean> map = gson.fromJson(str, new TypeToken<HashMap<String, Boolean>>(){}.getType());
        if(!map.containsKey("Wooden")) map.put("Wooden",true);
        if(!map.containsKey("Stone")) map.put("Stone",false);
        if(!map.containsKey("Iron")) map.put("Iron",false);
        if(!map.containsKey("Gold")) map.put("Gold",false);
        if(!map.containsKey("Diamond")) map.put("Diamond",false);
        weaponData.put(player.getName().toLowerCase(), map);
    }

    public static HashMap<String, HashMap<String, Boolean>> weaponData = new HashMap<String, HashMap<String, Boolean>>();
    public static LinkedHashMap<String, String> selectWeapon = new LinkedHashMap<String, String>();
    public static HashMap<Player,Weapon> weaponsKnife = new HashMap<Player,Weapon>();
    public static HashMap<Player,Weapon> weaponsGun = new HashMap<Player,Weapon>();
    public static HashMap<Integer,Integer> taskid = new HashMap<Integer,Integer>();
    public static File file;

}
class CallbackCharge extends Task{

    public Player player;
    public double time;

    public CallbackCharge(Player player,double time){
        this.player = player;
        this.time = time;
    }

    public void onRun(int d){
        Weapons.ChargeExp(this.player,this.time);
    }
}
class CallbackReturn extends Task{

    public Player player;

    public CallbackReturn(Player player){
        this.player = player;
    }

    public void onRun(int d){
        Weapons.ReturnItem(this.player);
    }
}