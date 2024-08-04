package sote.miniwalls.stage;

import java.util.HashMap;

import cn.nukkit.math.Vector3;

public class Castle extends MiniwallsStage{

    public Castle(){
        this.red = new Vector3(512.5, 51, -515.5);
        this.blue = new Vector3(478.5, 51, -515.5);
        this.green = new Vector3(478.5, 51, -481.5);
        this.yellow = new Vector3(512.5, 51, -481.5);
        this.redwither = new Vector3(516.5, 51, -519.5);
        this.bluewither = new Vector3(474.5, 51, -519.5);
        this.greenwither = new Vector3(474.5, 51, -477.5);
        this.yellowwither = new Vector3(516.5, 51, -477.5);
        this.wall.put(new Vector3(494, 51, -534), new Vector3(496, 68, -462));
        this.wall.put(new Vector3(459, 51, -499), new Vector3(531, 68, -497));
        this.wall.put(new Vector3(494, 50, -462), new Vector3(496, 50, -462));
        this.wall.put(new Vector3(459, 50, -499), new Vector3(459, 50, -497));
        this.wall.put(new Vector3(494, 50, -534), new Vector3(496, 50, -534));
        this.wall.put(new Vector3(531, 50, -499), new Vector3(531, 50, -497));
    }

    @Override
    public String getName(){
        return "Castle";
    }

    @Override
    public Vector3 getRedSpawn(){
        return red;
    }

    @Override
    public Vector3 getBlueSpawn(){
        return blue;
    }

    @Override
    public Vector3 getGreenSpawn(){
        return green;
    }

    @Override
    public Vector3 getYellowSpawn(){
        return yellow;
    }

    @Override
    public Vector3 getRedWitherSpawn(){
        return redwither;
    }

    @Override
    public Vector3 getBlueWitherSpawn(){
        return bluewither;
    }

    @Override
    public Vector3 getGreenWitherSpawn(){
        return greenwither;
    }

    @Override
    public Vector3 getYellowWitherSpawn(){
        return yellowwither;
    }

    @Override
    public HashMap<Vector3, Vector3> getWall(){
        return wall;
    }

    public Vector3 red = new Vector3();
    public Vector3 blue = new Vector3();
    public Vector3 green = new Vector3();
    public Vector3 yellow = new Vector3();
    public Vector3 redwither = new Vector3();
    public Vector3 bluewither = new Vector3();
    public Vector3 greenwither = new Vector3();
    public Vector3 yellowwither = new Vector3();
    public HashMap<Vector3, Vector3> wall = new HashMap<Vector3, Vector3>();

}