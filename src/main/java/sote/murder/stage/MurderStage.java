package sote.murder.stage;

import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;

public abstract class MurderStage{

    public MurderStage() {
    }

    public String getName() {
        return "";
    }

    public Item getItem() {
        return Item.get(0);
    }

    public Vector3[] getSpawn() {
        return new Vector3[]{};
    }

    public Vector3[] getEmerald() {
        return new Vector3[]{};
    }

}
