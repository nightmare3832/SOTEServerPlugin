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

public class MiniwallsBuilderInventory extends MiniwallsInventory{

    public MiniwallsBuilderInventory(){
    }

    @Override
    public void register(Player player){
        this.ChargeSpeed = 3;
        this.ChargeMax = 300;
        this.StackMax = 3;
        if(Stat.getMiniwallsBuilderLevel(player) >= 1) this.helmetEnchantLevel = 1;
        PlayerInventory inventory = ((EntityHuman)player).getInventory();
        Main.canArmor.put(player,true);
        inventory.clearAll();
        ItemColorArmor helmet = (ItemColorArmor)Item.get(Item.LEATHER_CAP);
        ItemColorArmor chestplate = (ItemColorArmor)Item.get(Item.LEATHER_TUNIC);
        ItemColorArmor leggings = (ItemColorArmor)Item.get(Item.LEATHER_PANTS);
        Item boots = Item.get(Item.DIAMOND_BOOTS);
        if(this.helmetEnchantLevel > 0){
            Enchantment enchantment = Enchantment.getEnchantment(Enchantment.ID_PROTECTION_ALL);
            enchantment.setLevel(this.helmetEnchantLevel);
            helmet.addEnchantment(enchantment);
        }
        if(this.chestplateEnchantLevel > 0){
            Enchantment enchantment = Enchantment.getEnchantment(Enchantment.ID_PROTECTION_ALL);
            enchantment.setLevel(this.chestplateEnchantLevel);
            chestplate.addEnchantment(enchantment);
        }
        if(this.leggingsEnchantLevel > 0){
            Enchantment enchantment = Enchantment.getEnchantment(Enchantment.ID_PROTECTION_ALL);
            enchantment.setLevel(this.leggingsEnchantLevel);
            leggings.addEnchantment(enchantment);
        }
        if(this.bootsEnchantLevel > 0){
            Enchantment enchantment = Enchantment.getEnchantment(Enchantment.ID_PROTECTION_ALL);
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
        int count = 0;
        Item item = Item.get(Item.WOODEN_SWORD,0,1);
        inventory.setHotbarSlotIndex(count,count);
        inventory.setItem(count,item);
        count++;
        Item item2 = Item.get(Item.BOW,0,1);
        inventory.setHotbarSlotIndex(count,count);
        inventory.setItem(count,item2);
        count++;
        Item item3 = Item.get(Item.ARROW,0,2);
        inventory.setHotbarSlotIndex(count,count);
        inventory.setItem(count,item3);
        count++;
        Item item4 = Item.get(Item.STONE_PICKAXE,0,1);
        inventory.setHotbarSlotIndex(count,count);
        inventory.setItem(count,item4);
        count++;
        if(Stat.getMiniwallsBuilderLevel(player) >= 2){
            Item addItem = Item.get(Item.IRON_PICKAXE,0,1);
            inventory.setHotbarSlotIndex(count,count);
            inventory.setItem(count,addItem);
            count++;
        }
        if(Stat.getMiniwallsBuilderLevel(player) >= 4){
            Item addItem = Item.get(Item.STONE_AXE,0,1);
            inventory.setHotbarSlotIndex(count,count);
            inventory.setItem(count,addItem);
            count++;
        }
        if(Stat.getMiniwallsSoldierLevel(player) >= 6){
            Item addItem = Item.get(Item.STONE_SHOVEL,0,1);
            inventory.setHotbarSlotIndex(count,count);
            inventory.setItem(count,addItem);
            count++;
        }
        Item item5 = Item.get(Item.WOOL,0,miniwalls.wools.get(player));
        if(color == 0) item5.setDamage(14);
        else if(color == 1) item5.setDamage(11);
        else if(color == 2) item5.setDamage(13);
        else if(color == 3) item5.setDamage(4);
        inventory.setHotbarSlotIndex(count,count);
        inventory.setItem(count,item5);
        count++;
        Item item6 = Item.get(Item.COMPASS,0,1);
        inventory.setHotbarSlotIndex(count,count);
        inventory.setItem(count,item6);
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