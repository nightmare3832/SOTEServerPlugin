package sote.murder.weapon;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import sote.Game;
import sote.GameProvider;
import sote.Main;
import sote.inventory.Inventorys;
import sote.murder.Murder;

public class IronSword extends Weapon{

    public IronSword(){
    }

    @Override
    public Item getItem(Player player){
        Item item = Item.get(267,0,1);
        item.setCustomName(Main.getMessage(player, "item.murder.knife"));
        return item;
    }

    public void Shot(Player player){
        Item item = player.getInventory().getItemInHand();
        Game game = GameProvider.getPlayingGame(player);
        if(!(game instanceof Murder)) return;
        Murder murder = (Murder) game;
        murder.knifecount.put(player,murder.knifecount.get(player)-1);
        Inventorys.data2.get(player).register(player);
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
            .putShort("Fire", 0);
        double f = 3;
        EntityArrow arrow = new EntityArrow(player.chunk, nbt, player, false);
        arrow.setMotion(arrow.getMotion().multiply(f));
        arrow.spawnToAll();
        //player.getLevel().addSound(new LaunchSound(player), player.getViewers().values());
        ProjectileLaunchEvent projectileLaunchEvent = new ProjectileLaunchEvent(arrow);
        Server.getInstance().getPluginManager().callEvent(projectileLaunchEvent);
    }

    public void despawn(Player player,EntityProjectile projectile){
        Level level = projectile.getLevel();
        //projectile.getLevel().dropItem(projectile,getItem());
        Item i = getItem(player);
        i.setDamage((int)(Math.random() * 10000));
        CompoundTag itemTag = NBTIO.putItemHelper(i);
        itemTag.setName("Item");
        EntityItem itemEntity = new EntityItem(
            level.getChunk((int) projectile.getX() >> 4, (int) projectile.getZ() >> 4, true),
                new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", projectile.getX()))
                    .add(new DoubleTag("", projectile.getY())).add(new DoubleTag("", projectile.getZ())))
                    .putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", 0))
                        .add(new DoubleTag("", 0)).add(new DoubleTag("", 0)))
                    .putList(new ListTag<FloatTag>("Rotation")
                        .add(new FloatTag("", new java.util.Random().nextFloat() * 360))
                        .add(new FloatTag("", 0)))
                    .putShort("Health", 5).putCompound("Item", itemTag).putShort("PickupDelay", 10));
        itemEntity.spawnToAll();
        setDropItem(itemEntity);
        Weapons.Return(player,getReturnTime(),i.getDamage());
    }

    @Override
    public int getReturnTime(){
        return 5;
    }
}