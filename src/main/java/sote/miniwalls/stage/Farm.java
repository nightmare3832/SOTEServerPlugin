package sote.miniwalls.stage;

import java.util.HashMap;

import cn.nukkit.math.Vector3;

public class Farm extends MiniwallsStage{

    public Farm(){
        this.red = new Vector3(1474.5, 23, 1525.5);
        this.blue = new Vector3(1436.5, 23, 1525.5);
        this.green = new Vector3(1436.5, 23, 1563.5);
        this.yellow = new Vector3(1474.5, 23, 1563.5);
        this.redwither = new Vector3(1477.5, 22, 1522.5);
        this.bluewither = new Vector3(1433.5, 22, 1522.5);
        this.greenwither = new Vector3(1433.5, 22, 1566.5);
        this.yellowwither = new Vector3(1477.5, 22, 1566.5);
        this.wall.put(new Vector3(1419, 22, 1543), new Vector3(1419, 39, 1545));
        this.wall.put(new Vector3(1454, 22, 1508), new Vector3(1456, 39, 1580));
        this.wall.put(new Vector3(1491, 21, 1514), new Vector3(1491, 21, 1545));
        this.wall.put(new Vector3(1454, 21, 1580), new Vector3(1456, 21, 1580));
        this.wall.put(new Vector3(1419, 21, 1543), new Vector3(1419, 21, 1545));
        this.wall.put(new Vector3(1454, 21, 1508), new Vector3(1456, 21, 1508));
        this.wall.put(new Vector3(1420, 22, 1543), new Vector3(1491, 39, 1545));
    }

    @Override
    public String getName(){
        return "Farm";
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