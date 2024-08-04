package sote.skywarssolo.stage;

import java.util.HashMap;

import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;

public abstract class SkywarsSoloStage{

    public SkywarsSoloStage() {
    }

    public String getName() {
        return "";
    }

    public Vector3[] getSpawn() {
        return new Vector3[]{};
    }

    public Item getItem() {
        return Item.get(0);
    }

    public HashMap<Integer,Vector3> getMiddleChests() {
        return new HashMap<Integer,Vector3>();
    }

    public HashMap<Integer, Vector3[]> getFirstChests() {
        return new HashMap<Integer, Vector3[]>();
    }

}
