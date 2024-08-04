package sote.skywarssolo.stage;

import java.util.HashMap;

import cn.nukkit.math.Vector3;

public class WhiteNature extends SkywarsSoloStage{

    public WhiteNature(){
        register();
    }

    public void register(){
        Vector3[] pos = new Vector3[8];
        pos[0] = new Vector3(240,10,254);
        pos[1] = new Vector3(258,25,239);
        pos[2] = new Vector3(244,21,240);
        pos[3] = new Vector3(254,13,241);
        pos[4] = new Vector3(241,14,241);
        pos[5] = new Vector3(271,7,265);
        pos[6] = new Vector3(228,7,293);
        pos[7] = new Vector3(250,13,224);
        spawn = pos;
    }

    @Override
    public String getName(){
        return "White Nature";
    }

    @Override
    public Vector3[] getSpawn(){
        return spawn;
    }

    @Override
    public HashMap<Integer,Vector3> getMiddleChests(){
        return chest;
    }

    public static Vector3[] spawn;
    public static HashMap<Integer,Vector3> chest;
    public static Vector3[] pos;
    public static Vector3[] poss;

}