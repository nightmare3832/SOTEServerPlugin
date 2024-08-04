package sote.miniwalls.stage;

import java.util.HashMap;

import cn.nukkit.math.Vector3;

public class Church extends MiniwallsStage{

    public Church(){
        this.red = new Vector3(656.5, 63, 653.5);
        this.blue = new Vector3(655.5, 63, 690.5);
        this.green = new Vector3(692.5, 63, 691.5);
        this.yellow = new Vector3(693.5, 63, 654.5);
        this.redwither = new Vector3(652.5, 64, 649.5);
        this.bluewither = new Vector3(651.5, 64, 694.5);
        this.greenwither = new Vector3(696.5, 64, 695.5);
        this.yellowwither = new Vector3(697.5, 64, 650.5);
        this.wall.put(new Vector3(673, 63, 636), new Vector3(675, 80, 708));
        this.wall.put(new Vector3(638, 63, 671), new Vector3(710, 80, 673));
    }

    @Override
    public String getName(){
        return "Church";
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