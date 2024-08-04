package sote.miniwalls.stage;

import java.util.HashMap;

import cn.nukkit.math.Vector3;

public abstract class MiniwallsStage{

    public MiniwallsStage() {
    }

    public String getName() {
        return "";
    }

    public Vector3 getRedSpawn() {
        return new Vector3();
    }

    public Vector3 getBlueSpawn() {
        return new Vector3();
    }

    public Vector3 getGreenSpawn() {
        return new Vector3();
    }

    public Vector3 getYellowSpawn() {
        return new Vector3();
    }

    public Vector3 getRedWitherSpawn() {
        return new Vector3();
    }

    public Vector3 getBlueWitherSpawn() {
        return new Vector3();
    }

    public Vector3 getGreenWitherSpawn() {
        return new Vector3();
    }

    public Vector3 getYellowWitherSpawn() {
        return new Vector3();
    }

    public HashMap<Vector3, Vector3> getWall() {
        return new HashMap<Vector3, Vector3>();
    }

}
