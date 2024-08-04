package sote.skywarssolo.kit;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemColorArmor;
import cn.nukkit.item.ItemPotion;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.AddPlayerPacket;
import cn.nukkit.network.protocol.MobArmorEquipmentPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.network.protocol.SetEntityDataPacket;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.DyeColor;
import sote.Game;
import sote.GameProvider;
import sote.Main;
import sote.skywarssolo.SkywarsSolo;
import sote.stat.Stat;

public class SkywarsSoloKits{

    public SkywarsSoloKits(){
    }

    public static void buyItem(Player player,String str){
        int coin = getCost(str);
        Map<String, Boolean> map = (Map<String, Boolean>) kitData.get(player.getName().toLowerCase());
        if(map.get(str) == true){
            player.sendMessage(Main.getMessage(player,"skywars.set.kit",new String[]{Main.getMessage(player,"skywars.kit."+str)}));
            setSellectKit(player,str);
            setSellectKitData(player);
        }else{
            if(!count.containsKey(player) || count.get(player) == false){
                player.sendMessage(Main.getMessage(player,"shop.touch.more.kit"));
                count.put(player, true);
                Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackBuy(player),100);
            }else{
                if(Stat.getCoin(player) >= coin){
                    ((Map<String, Boolean>) kitData.get(player.getName().toLowerCase())).put(str,true);
                    Stat.setCoin(player,Stat.getCoin(player)-coin);
                    player.sendMessage(Main.getMessage(player,"shop.buy.kit"));
                    updateKit(player);
                }else{
                    player.sendMessage(Main.getMessage(player,"shop.no.coin"));
                }
            }
        }
    }

    public static int getCost(String str){
        int coin = 0;
        switch(str){
            case "soldier":
                coin = 1500;
            break;
            case "builder":
                coin = 3000;
            break;
            case "archer":
                coin = 4000;
            break;
            case "toughness":
                coin = 5500;
            break;
            case "bomber":
                coin = 6000;
            break;
            case "snowman":
                coin = 7000;
            break;
            case "enderman":
                coin = 11500;
            break;
            case "healer":
                coin = 12000;
            break;
            case "zephyros":
                coin = 13000;
            break;
            case "indira":
                coin = 14500;
            break;
            case "odin":
                coin = 21500;
            break;
            case "artemis":
                coin = 22500;
            break;
            //case "hades":
            //    coin = 24000;
            //break;
            case "hades":
                coin = 26000;
            break;
            case "dione":
                coin = 100000;
            break;
        }
        return coin;
    }

    public static void count(Player player){
        count.put(player, false);
    }

    public static void addPlayer(Player player){
        HashMap<String, Boolean> v1 = new HashMap<String, Boolean>();
        v1.put("unknown",true);
        v1.put("soldier",false);
        v1.put("builder",false);
        v1.put("archer",false);
        v1.put("toughness",false);
        v1.put("bomber",false);
        v1.put("snowman",false);
        v1.put("enderman",false);
        v1.put("healer",false);
        v1.put("zephyros",false);
        v1.put("indira",false);
        v1.put("odin",false);
        v1.put("artemis",false);
        //v1.put("hades",false);
        v1.put("hades",false);
        v1.put("dione",false);
        kitData.put(player.getName().toLowerCase(),v1);
    }

    public static String getKits(Player player){
        Map<String, Boolean> map = kitData.get(player.getName().toLowerCase());
        return new GsonBuilder().setPrettyPrinting().create().toJson(map);
    }

    public static void setKits(Player player, String str){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        HashMap<String, Boolean> map = gson.fromJson(str, new TypeToken<HashMap<String, Boolean>>(){}.getType());
        if(!map.containsKey("unknown")) map.put("unknown",true);
        if(!map.containsKey("soldier")) map.put("soldier",false);
        if(!map.containsKey("builder")) map.put("builder",false);
        if(!map.containsKey("archer")) map.put("archer",false);
        if(!map.containsKey("toughness")) map.put("toughness",false);
        if(!map.containsKey("bomber")) map.put("bomber",false);
        if(!map.containsKey("snowman")) map.put("snowman",false);
        if(!map.containsKey("enderman")) map.put("enderman",false);
        if(!map.containsKey("healer")) map.put("healer",false);
        if(!map.containsKey("zephyros")) map.put("zephyros",false);
        if(!map.containsKey("indira")) map.put("indira",false);
        if(!map.containsKey("odin")) map.put("odin",false);
        if(!map.containsKey("artemis")) map.put("artemis",false);
        //if(!map.containsKey("hades")) map.put("hades",false);
        if(!map.containsKey("hades")) map.put("hades",false);
        if(!map.containsKey("dione")) map.put("dione",false);
        kitData.put(player.getName().toLowerCase(), map);
    }

    public static void setSellectKit(Player player,String str){
        sellectKit.put(player.getName().toLowerCase(),str);
        setSellectKitData(player);
    }

    public static SkywarsSoloKit getSellectKit(Player player){
        return Kitdata.get(player);
    }

    public static String getSellectKitString(Player player){
        return (String)sellectKit.get(player.getName().toLowerCase());
    }

    public static void setSellectKitData(Player player){
        String str = sellectKit.get(player.getName().toLowerCase()).toString();
        switch(str){
            case "unknown":
                Kitdata.put(player,new SkywarsSoloUnknownKit(player));
            break;
            case "soldier":
                Kitdata.put(player,new SkywarsSoloSoldierKit(player));
            break;
            case "builder":
                Kitdata.put(player,new SkywarsSoloBuilderKit(player));
            break;
            case "archer":
                Kitdata.put(player,new SkywarsSoloArcherKit(player));
            break;
            case "toughness":
                Kitdata.put(player,new SkywarsSoloToughnessKit(player));
            break;
            case "bomber":
                Kitdata.put(player,new SkywarsSoloBomberKit(player));
            break;
            case "snowman":
                Kitdata.put(player,new SkywarsSoloSnowmanKit(player));
            break;
            case "enderman":
                Kitdata.put(player,new SkywarsSoloEndermanKit(player));
            break;
            case "healer":
                Kitdata.put(player,new SkywarsSoloSoldierKit(player));
            break;
            case "zephyros":
                Kitdata.put(player,new SkywarsSoloZephyrosKit(player));
            break;
            case "indira":
                Kitdata.put(player,new SkywarsSoloIndiraKit(player));
            break;
            case "odin":
                Kitdata.put(player,new SkywarsSoloOdinKit(player));
            break;
            case "artemis":
                Kitdata.put(player,new SkywarsSoloArtemisKit(player));
            break;
            case "hades":
                Kitdata.put(player,new SkywarsSoloHadesKit(player));
            break;
            case "dione":
                Kitdata.put(player,new SkywarsSoloDioneKit(player));
            break;
            default:
                Kitdata.put(player,new SkywarsSoloUnknownKit(player));
            break;
        }
    }

    public static void spawnKit(){
        AddPlayerPacket pk = new AddPlayerPacket();
        pk.username = "";
        kititems = new LinkedHashMap<String,Item[]>();
        kititems.put("soldier",new Item[]{Item.get(Item.STONE_SWORD,0,1),Item.get(0,0,0),Item.get(0,0,0),Item.get(0,0,0),Item.get(0,0,0)});
        kititems.put("builder",new Item[]{Item.get(Item.WOOD,0,16),Item.get(0,0,0),Item.get(Item.LEATHER_TUNIC,0,0),Item.get(0,0,0),Item.get(0,0,0)});
        kititems.put("archer",new Item[]{Item.get(Item.BOW,0,1),Item.get(0,0,0),Item.get(0,0,0),Item.get(0,0,0),Item.get(0,0,0)});
        kititems.put("toughness",new Item[]{Item.get(0,0,0),Item.get(Item.GOLD_HELMET,0,0),Item.get(Item.GOLD_CHESTPLATE,0,0),Item.get(Item.GOLD_LEGGINGS,0,0),Item.get(0,0,0)});
        Item letherCap = Item.get(Item.LEATHER_CAP, 0, 1);
        ((ItemColorArmor)letherCap).setColor(DyeColor.RED);
        Item letherTunic = Item.get(Item.LEATHER_TUNIC, 0, 1);
        ((ItemColorArmor)letherTunic).setColor(DyeColor.YELLOW);
        kititems.put("bomber",new Item[]{Item.get(Item.TNT,0,1),letherCap,letherTunic,Item.get(0,0,0),Item.get(0,0,0)});
        Item letherCap2 = Item.get(Item.LEATHER_CAP, 0, 1);
        ((ItemColorArmor)letherCap2).setColor(DyeColor.WHITE);
        Item letherTunic2 = Item.get(Item.LEATHER_TUNIC, 0, 1);
        ((ItemColorArmor)letherTunic2).setColor(DyeColor.WHITE);
        kititems.put("snowman",new Item[]{Item.get(Item.SNOWBALL,0,1),letherCap2,letherTunic2,Item.get(0,0,0),Item.get(0,0,0)});
        Item letherCap3 = Item.get(Item.LEATHER_CAP, 0, 1);
        ((ItemColorArmor)letherCap3).setColor(DyeColor.BLACK);
        Item letherPants3 = Item.get(Item.LEATHER_PANTS, 0, 1);
        ((ItemColorArmor)letherPants3).setColor(DyeColor.PURPLE);
        kititems.put("enderman",new Item[]{Item.get(Item.ENDER_PEARL,0,1),letherCap3,Item.get(0,0,1),letherPants3,Item.get(0,0,0)});
        Item letherPants4 = Item.get(Item.LEATHER_PANTS, 0, 1);
        ((ItemColorArmor)letherPants4).setColor(DyeColor.PINK);
        Item letherBoots4 = Item.get(Item.LEATHER_BOOTS, 0, 1);
        ((ItemColorArmor)letherBoots4).setColor(DyeColor.PINK);
        kititems.put("healer",new Item[]{Item.get(Item.POTION,ItemPotion.INSTANT_HEALTH_II,1),Item.get(0,0,1),Item.get(0,0,1),letherPants4,letherBoots4});
        /*kititems.put("zephyros",new Item[]{Item.get(Item.GOLD_HOE,0,1),Item.get(0,0,0),Item.get(0,0,0),Item.get(0,0,0),Item.get(0,0,0)});
        kititems.put("indira",new Item[]{Item.get(Item.GOLD_SWORD,0,1),Item.get(0,0,0),Item.get(0,0,0),Item.get(0,0,0),Item.get(0,0,0)});
        kititems.put("odin",new Item[]{Item.get(Item.BONE,0,1),Item.get(0,0,0),Item.get(0,0,0),Item.get(0,0,0),Item.get(0,0,0)});
        kititems.put("artemis",new Item[]{Item.get(Item.BOW,0,1),Item.get(0,0,0),Item.get(0,0,0),Item.get(0,0,0),Item.get(0,0,0)});
        //kititems.put("unknown",new Item[]{Item.get(0,0,1),Item.get(0,0,0),Item.get(0,0,0),Item.get(0,0,0),Item.get(0,0,0)});
        kititems.put("hades",new Item[]{Item.get(Item.BONE,0,1),Item.get(0,0,0),Item.get(0,0,0),Item.get(0,0,0),Item.get(0,0,0)});
        kititems.put("dione",new Item[]{Item.get(Item.FEATHER,0,1),Item.get(0,0,0),Item.get(0,0,0),Item.get(0,0,0),Item.get(0,0,0)});*/
        for (Map.Entry<String,Item[]> e : kititems.entrySet()){
            Long eid = Entity.entityCount++;
            kithuman.put(eid,e.getKey());
            kithuman2.put(e.getKey(),eid);
        }
        kitpos = new HashMap<String,Vector3>();
        kitpos.put("soldier",new Vector3(-32.5,58.5,5.5));
        kitpos.put("builder",new Vector3(-29.5,58.5,3.5));
        kitpos.put("archer",new Vector3(-26.5,58.5,2.5));
        kitpos.put("toughness",new Vector3(-23.5,58.5,1.5));
        kitpos.put("bomber",new Vector3(-20.5,58.5,1.5));
        kitpos.put("snowman",new Vector3(-17.5,58.5,2.5));
        kitpos.put("enderman",new Vector3(-14.5,58.5,3.5));
        kitpos.put("healer",new Vector3(-11.5,58.5,5.5));
        kitpos.put("zephyros",new Vector3(-9.5,58.5,8.5));
        kitpos.put("indira",new Vector3(-8.5,58.5,11.5));
        kitpos.put("odin",new Vector3(-9.5,58.5,14.5));
        kitpos.put("artemis",new Vector3(-11.5,58.5,17.5));
        kitpos.put("unknown",new Vector3(-14.5,58.5,19.5));
        kitpos.put("hades",new Vector3(-17.5,58.5,20.5));
        kitpos.put("dione",new Vector3(-0.5,62.5,-1.5));
        kityaw = new HashMap<String,Integer>();
        kityaw.put("soldier",300);
        kityaw.put("builder",315);
        kityaw.put("archer",335);
        kityaw.put("toughness",350);
        kityaw.put("bomber",5);
        kityaw.put("snowman",25);
        kityaw.put("enderman",40);
        kityaw.put("healer",60);
        kityaw.put("zephyros",75);
        kityaw.put("indira",90);
        kityaw.put("odin",105);
        kityaw.put("artemis",120);
        kityaw.put("unknown",140);
        kityaw.put("hades",150);
        kityaw.put("dione",50);
    }

    public static void spawnKit(Player player){
        AddPlayerPacket pk = new AddPlayerPacket();
        pk.username = "";
        MobArmorEquipmentPacket pkk = new MobArmorEquipmentPacket();
        for (Map.Entry<String,Item[]> e : kititems.entrySet()){
            UUID uuid = UUID.randomUUID();
            Long eid = kithuman2.get(e.getKey());
            pk.entityUniqueId = eid;
            pk.entityRuntimeId = eid;
            pk.uuid = uuid;
            pk.x = (float)kitpos.get(e.getKey()).x;
            pk.y = (float)kitpos.get(e.getKey()).y;
            pk.z = (float)kitpos.get(e.getKey()).z;
            pk.yaw = kityaw.get(e.getKey());
            pk.pitch = 0;
            pk.item = e.getValue()[0];
            int flags = 0;
            flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;
            flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
            flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
            pk.metadata = new EntityMetadata()
                    .putLong(Entity.DATA_FLAGS,flags)
                    .putString(Entity.DATA_NAMETAG,"")
                    .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1);
            player.dataPacket(pk);
            pkk.eid = eid;
            pkk.slots = new Item[]{e.getValue()[1],e.getValue()[2],e.getValue()[3],e.getValue()[4]};
            player.dataPacket(pkk);
        }
        updateKit(player);
    }

    public static void despawnKit(Player player){
        for (Map.Entry<Long,String> e : SkywarsSoloKits.kithuman.entrySet()){
            RemoveEntityPacket pk = new RemoveEntityPacket();
            pk.eid = e.getKey();
            player.dataPacket(pk);
        }
    }

    public static void updateKit(Player player){
        for (Map.Entry<Long,String> e : SkywarsSoloKits.kithuman.entrySet()){
            String tag = "";
            Map<String, Boolean> map = (Map<String, Boolean>) SkywarsSoloKits.kitData.get(player.getName().toLowerCase());
            if(map.get(e.getValue())) Main.getMessage(player, "skywars.kit.tag", new String[]{Main.getMessage(player, "skywars.kit."+e.getValue()), Main.getMessage(player, "skywars.kit.tag.sold")});
            else tag = Main.getMessage(player, "skywars.kit.tag", new String[]{Main.getMessage(player, "skywars.kit."+e.getValue()), Main.getMessage(player, "skywars.kit.tag.cost", new String[]{String.valueOf(getCost(e.getValue()))})});
            SetEntityDataPacket pk = new SetEntityDataPacket();
            pk.eid = e.getKey();
            int flags = 0;
            flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;
            flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
            flags |= 0 << Entity.DATA_FLAG_INVISIBLE;
            pk.metadata = new EntityMetadata()
                    .putByte(Entity.DATA_FLAGS, flags)
                    .putString(Entity.DATA_NAMETAG, tag);
            player.dataPacket(pk);
        }
    }

    public static void touch(Player player,Long eid){
        if(kithuman.containsKey(eid)){
            buyItem(player,kithuman.get(eid));
            for (Map.Entry<Long,String> e : kithuman.entrySet()){
                String tag = "";
                Map<String, Boolean> map = (Map<String, Boolean>) SkywarsSoloKits.kitData.get(player.getName().toLowerCase());
                if(map.get(e.getValue())) Main.getMessage(player, "skywars.kit.tag", new String[]{Main.getMessage(player, "skywars.kit."+e.getValue()), Main.getMessage(player, "skywars.kit.tag.sold")});
                else tag = Main.getMessage(player, "skywars.kit.tag", new String[]{Main.getMessage(player, "skywars.kit."+e.getValue()), Main.getMessage(player, "skywars.kit.tag.cost", new String[]{String.valueOf(getCost(e.getValue()))})});
                SetEntityDataPacket pk = new SetEntityDataPacket();
                pk.eid = e.getKey();
                int flags = 0;
                flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;
                flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
                flags |= 0 << Entity.DATA_FLAG_INVISIBLE;
                pk.metadata = new EntityMetadata()
                        .putByte(Entity.DATA_FLAGS, flags)
                        .putString(Entity.DATA_NAMETAG, tag);
                player.dataPacket(pk);
            }
        }
    }

    public static void attack(EntityDamageEvent event){
        Entity entity = event.getEntity();
        if(!(entity instanceof Player)) return;
        Player player = (Player) entity;
        Game game = GameProvider.getPlayingGame(player);
        if(!(game instanceof SkywarsSolo)) return;
        if(!game.getGameDataAsBoolean("gamenow2")) return;
        if(event instanceof EntityDamageByEntityEvent){
            EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent)event;
            Entity damager = ev.getDamager();
                if(damager instanceof Player && entity instanceof Player){
                    Player pentity = (Player) entity;
                    Player dplayer = (Player) damager;
                    Kitdata.get(dplayer).onAttack(pentity);
                }
        }
    }

    public static void shot(Player player,Item item){
        Game game = GameProvider.getPlayingGame(player);
        if(!(game instanceof SkywarsSolo)) return;
        if(!game.getGameDataAsBoolean("gamenow2")) return;
        Kitdata.get(player).onUseItem();
    }

    public static HashMap<String, HashMap<String, Boolean>> kitData = new HashMap<String, HashMap<String, Boolean>>();
    public static HashMap<String, String> sellectKit = new HashMap<String, String>();
    public static HashMap<Player,SkywarsSoloKit> Kitdata = new HashMap<Player,SkywarsSoloKit>();
    public static HashMap<Player,Boolean> count = new HashMap<Player,Boolean>();
    public static HashMap<Player,Boolean> canJump = new HashMap<Player,Boolean>();
    public static HashMap<Long,String> kithuman = new HashMap<Long,String>();
    public static HashMap<String,Long> kithuman2 = new HashMap<String,Long>();
    public static LinkedHashMap<String,Item[]> kititems = new LinkedHashMap<String,Item[]>();
    public static HashMap<String,Vector3> kitpos;
    public static HashMap<String,Integer> kityaw;
    public static HashMap<String,Block> change = new HashMap<String,Block>();

}
class CallbackBuy extends Task{

    public Player player;

    public CallbackBuy(Player player){
        this.player = player;
    }

    public void onRun(int d){
        SkywarsSoloKits.count(this.player);
    }
}