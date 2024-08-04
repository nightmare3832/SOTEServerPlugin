package sote.miniwalls.stage;

import java.util.HashMap;

import cn.nukkit.math.Vector3;

public class Japanese extends MiniwallsStage{

    public Japanese(){
        this.red = new Vector3(759.5, 68, -508.5);
        this.blue = new Vector3(759.5, 68, -475.5);
        this.green = new Vector3(787.5, 68, -480.5);
        this.yellow = new Vector3(787.5, 68, -508.5);
        this.redwither = new Vector3(754.5, 68, -513.5);
        this.bluewither = new Vector3(754.5, 68, -475.5);
        this.greenwither = new Vector3(792.5, 68, -475.5);
        this.yellowwither = new Vector3(792.5, 68, -513.5);
        this.wall.put(new Vector3(772, 68, -530), new Vector3(774, 85, -458));
        this.wall.put(new Vector3(737, 68, -495), new Vector3(809, 85, -493));
        this.wall.put(new Vector3(737, 66, -495), new Vector3(737, 67, -493));
        this.wall.put(new Vector3(772, 66, -530), new Vector3(774, 67, -530));
        this.wall.put(new Vector3(809, 66, -495), new Vector3(809, 67, -493));
        this.wall.put(new Vector3(772, 66, -458), new Vector3(774, 67, -458));
    }

    @Override
    public String getName(){
        return "Japanese";
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