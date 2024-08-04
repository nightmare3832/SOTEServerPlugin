package sote.murder.weapon;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import sote.Main;

public class StoneHoe extends Weapon{

    public StoneHoe(){
    }

    @Override
    public Item getItem(Player player){
        Item item = Item.get(291,0,1);
        item.setCustomName(Main.getMessage(player, "item.murder.gun", new String[]{String.valueOf(getLevel())}));
        return item;
    }

    @Override
    public void Shot(Player player){
        Vector3 direc = player.getDirectionVector();
        CompoundTag nbt = new CompoundTag()
            .putList(new ListTag<DoubleTag>("Pos")
                .add(new DoubleTag("", player.x+direc.x*0.1))
                .add(new DoubleTag("", player.y + player.getEyeHeight()))
                .add(new DoubleTag("", player.z+direc.z*0.1)))
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

    @Override
    public int getChargeTime(){
        return 5-(getLevel()-1);
    }
}