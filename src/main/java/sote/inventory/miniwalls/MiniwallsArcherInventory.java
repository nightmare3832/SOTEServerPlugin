package sote.inventory.miniwalls;

import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemColorArmor;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.Position;
import cn.nukkit.level.particle.Particle;
import cn.nukkit.network.protocol.LevelEventPacket;
import sote.Game;
import sote.GameProvider;
import sote.Main;
import sote.miniwalls.Miniwalls;
import sote.stat.Stat;

public class MiniwallsArcherInventory extends MiniwallsInventory{

    public MiniwallsArcherInventory(){
    }

    @Override
    public void register(Player player){
        this.ChargeSpeed = 15;
        this.ChargeMax = 1200;
        this.StackMax = 3;
        if(Stat.getMiniwallsArcherLevel(player) >= 1) this.helmetEnchantLevel = 1;
        if(Stat.getMiniwallsArcherLevel(player) >= 2) this.ChargeSpeed = 20;
        if(Stat.getMiniwallsArcherLevel(player) >= 3) this.leggingsEnchantLevel = 1;
        if(Stat.getMiniwallsArcherLevel(player) >= 4) this.ChargeSpeed = 30;
        if(Stat.getMiniwallsArcherLevel(player) >= 5) this.chestplateEnchantLevel = 1;
        if(Stat.getMiniwallsArcherLevel(player) >= 6){this.isPrestige = true;this.ChargeSpeed = 60;}
        PlayerInventory inventory = ((EntityHuman)player).getInventory();
        Main.canArmor.put(player,true);
        inventory.clearAll();
        ItemColorArmor helmet = (ItemColorArmor)Item.get(Item.LEATHER_CAP);
        ItemColorArmor chestplate = (ItemColorArmor)Item.get(Item.LEATHER_TUNIC);
        ItemColorArmor leggings = (ItemColorArmor)Item.get(Item.LEATHER_PANTS);
        Item boots = Item.get(Item.DIAMOND_BOOTS);
        if(this.helmetEnchantLevel > 0){
            Enchantment enchantment = Enchantment.getEnchantment(Enchantment.ID_PROTECTION_PROJECTILE);
            enchantment.setLevel(this.helmetEnchantLevel);
            helmet.addEnchantment(enchantment);
        }
        if(this.chestplateEnchantLevel > 0){
            Enchantment enchantment = Enchantment.getEnchantment(Enchantment.ID_PROTECTION_PROJECTILE);
            enchantment.setLevel(this.chestplateEnchantLevel);
            chestplate.addEnchantment(enchantment);
        }
        if(this.leggingsEnchantLevel > 0){
            Enchantment enchantment = Enchantment.getEnchantment(Enchantment.ID_PROTECTION_PROJECTILE);
            enchantment.setLevel(this.leggingsEnchantLevel);
            leggings.addEnchantment(enchantment);
        }
        if(this.bootsEnchantLevel > 0){
            Enchantment enchantment = Enchantment.getEnchantment(Enchantment.ID_PROTECTION_PROJECTILE);
            enchantment.setLevel(this.bootsEnchantLevel);
            boots.addEnchantment(enchantment);
        }
        Game game = GameProvider.getPlayingGame(player);
        if(!(game instanceof Miniwalls)) return;
        Miniwalls miniwalls = (Miniwalls) game;
        int color = miniwalls.team.get(player);
        int r = 0;
        int g = 0;
        int b = 0;
        if(color == 0){
            r = 204;
        }
        if(color == 1){
            b = 204;
            
        }
        if(color == 2){
            g = 255;
            b = 65;
        }
        if(color == 3){
            r = 255;
            g = 255;
        }
        helmet.setColor(r, g, b);
        chestplate.setColor(r, g, b);
        leggings.setColor(r, g, b);
        inventory.setHelmet(helmet);
        inventory.setChestplate(chestplate);
        inventory.setLeggings(leggings);
        inventory.setBoots(boots);
        Main.canArmor.put(player,false);
        Item item = Item.get(Item.WOODEN_SWORD,0,1);
        Item item2 = Item.get(Item.BOW,0,1);
        if(this.isPrestige){
            Enchantment enchantment = Enchantment.getEnchantment(Enchantment.ID_BOW_POWER);
            enchantment.setLevel(1);
            item2.addEnchantment(enchantment);
        }
        Item item3 = Item.get(Item.WOODEN_PICKAXE,0,1);
        Item item4 = Item.get(Item.ARROW,0,3);
        Item item5 = Item.get(Item.WOOL,0,miniwalls.wools.get(player));
        if(color == 0) item5.setDamage(14);
        else if(color == 1) item5.setDamage(11);
        else if(color == 2) item5.setDamage(13);
        else if(color == 3) item5.setDamage(4);
        Item item6 = Item.get(Item.COMPASS,0,1);
        int set = 0;
        int set2 = 1;
        int set3 = 2;
        int set4 = 3;
        int set5 = 4;
        int set6 = 5;
        inventory.setHotbarSlotIndex(set,set);
        inventory.setItem(set,item);
        inventory.setHotbarSlotIndex(set2,set2);
        inventory.setItem(set2,item2);
        inventory.setHotbarSlotIndex(set3,set3);
        inventory.setItem(set3,item3);
        inventory.setHotbarSlotIndex(set4,set4);
        inventory.setItem(set4,item4);
        inventory.setHotbarSlotIndex(set5,set5);
        inventory.setItem(set5,item5);
        inventory.setHotbarSlotIndex(set6,set6);
        inventory.setItem(set6,item6);
        inventory.sendContents(player);
    }

    @Override
    public void Function(Player player,Item item){
        switch(item.getId()+":"+item.getDamage()){
            case "345:0":
                Player p = null;
                double c = Double.MAX_VALUE;
                Game game = GameProvider.getPlayingGame(player);
                if(!(game instanceof Miniwalls)) return;
                Miniwalls miniwalls = (Miniwalls) game;
                for (Map.Entry<Long,Player> e : miniwalls.world.getPlayers().entrySet()){
                    if(e.getValue().getGamemode() == 0 && miniwalls.team.get(e.getValue()) != miniwalls.team.get(player) && player.distance(e.getValue()) <= c && !player.getName().equals(e.getValue().getName())){
                        p = e.getValue();
                        c = player.distance(e.getValue());
                    }
                }
                if(p == null) return;
                double mx = p.x - player.x;
                double mz = p.z - player.z;
                double yaw = getYaw(mx, mz);
                double MX = -Math.sin(yaw / 180 * Math.PI) * Math.cos(90 / 180 * Math.PI);
                double MZ = Math.cos(yaw / 180 * Math.PI) * Math.cos(90 / 180 * Math.PI);
                double MY = -Math.sin(90 / 180 * Math.PI);
                Position pos = new Position(player.x, player.y + player.getEyeHeight(), player.z);
                for(int i=0;i<=20;i++){
                    LevelEventPacket pk = new LevelEventPacket();
                    pk.evid = LevelEventPacket.EVENT_ADD_PARTICLE_MASK | Particle.TYPE_DRIP_WATER;
                    pk.x = (float) pos.x;
                    pk.y = (float) pos.y;
                    pk.z = (float) pos.z;
                    player.dataPacket(pk);
                    pos.x += MX;
                    pos.y += MY;
                    pos.z += MZ;
                }
            break;
        }
    }

    public static double getYaw(double mx,double mz) {
        double yaw = 0;
        if (mz == 0) {
            if (mx < 0) {
                yaw = -90;
            }else {
                yaw = 90;
            }
        }else {
            if (mx >= 0 && mz > 0) {
                double atan = Math.atan(mx/mz);
                yaw = rad2deg(atan);
            }else if (mx >= 0 && mz < 0) {
                double atan = Math.atan(mx/Math.abs(mz));
                yaw = 180 - rad2deg(atan);
            }else if (mx < 0 && mz < 0) {
                double atan = Math.atan(mx/mz);
                yaw = -(180 - rad2deg(atan));
            }else if (mx < 0 && mz > 0) {
                double atan = Math.atan(Math.abs(mx)/mz);
                yaw = -(rad2deg(atan));
            }
        }

        yaw = - yaw;
        return yaw;
    }

    public static double rad2deg(double radian) {
        return radian * (180f / Math.PI);
    }
}