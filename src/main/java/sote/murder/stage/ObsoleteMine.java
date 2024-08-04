package sote.murder.stage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;

public class ObsoleteMine extends MurderStage{

    public ObsoleteMine(){
        Server.getInstance().loadLevel("murder");
        register();
    }

    public static void register(){
    }

    @Override
    public String getName(){
        return "Obsolete Mine";
    }

    @Override
    public Item getItem(){
        return Item.get(Item.COAL);
    }

    @Override
    public Vector3[] getSpawn(){
        Vector3[] pos = new Vector3[15];
        pos[0] = new Vector3(-251.5,8,249.5);
        pos[1] = new Vector3(-231.5,9,269.5);
        pos[2] = new Vector3(-223.5,13,256.5);
        pos[3] = new Vector3(-230.5,16,253.5);
        pos[4] = new Vector3(-223.5,15,242.5);
        pos[5] = new Vector3(-265.5,9,231.5);
        pos[6] = new Vector3(-271.5,8,244.5);
        pos[7] = new Vector3(-247.5,4,239.5);
        pos[8] = new Vector3(-252.5,4,259.5);
        pos[9] = new Vector3(-264.5,9,264.5);
        pos[10] = new Vector3(-247.5,16,223.5);
        pos[11] = new Vector3(-239.5,7,217.5);
        pos[12] = new Vector3(-276.5,13,218.5);
        pos[13] = new Vector3(-269.5,16,232.5);
        pos[14] = new Vector3(-276.5,9,264.5);
        List<Vector3> list=Arrays.asList(pos);
        Collections.shuffle(list);
        spawn =(Vector3[])list.toArray(new Vector3[list.size()]);
        return spawn;
    }

    @Override
    public Vector3[] getEmerald(){
        Vector3[] poss = new Vector3[31];
        poss[0] = new Vector3(-244.5,7,214.5);
        poss[1] = new Vector3(-245.5,8,223.5);
        poss[2] = new Vector3(-246.5,8,231.5);
        poss[3] = new Vector3(-270.5,9,231.5);
        poss[4] = new Vector3(-273.5,8,249.5);
        poss[5] = new Vector3(-258.5,8,241.5);
        poss[6] = new Vector3(-251.5,8,267.5);
        poss[7] = new Vector3(-262.5,10,265.5);
        poss[8] = new Vector3(-278.5,9,264.5);
        poss[9] = new Vector3(-277.5,12,255.5);
        poss[10] = new Vector3(-277.5,14,243.5);
        poss[11] = new Vector3(-277.5,14,222.5);
        poss[12] = new Vector3(-272.5,13,216.5);
        poss[13] = new Vector3(-262.5,13,216.5);
        poss[14] = new Vector3(-253.5,12,220.5);
        poss[15] = new Vector3(-244.5,17,222.5);
        poss[16] = new Vector3(-261.5,11,211.5);
        poss[17] = new Vector3(-252.5,6,228.5);
        poss[18] = new Vector3(-257.5,4,258.5);
        poss[19] = new Vector3(-260.5,4,248.5);
        poss[20] = new Vector3(-254.5,4,249.5);
        poss[21] = new Vector3(-241.5,4,249.5);
        poss[22] = new Vector3(-243.5,8,256.5);
        poss[23] = new Vector3(-239.5,9,268.5);
        poss[24] = new Vector3(-231.5,8,260.5);
        poss[25] = new Vector3(-230.5,8,245.5);
        poss[26] = new Vector3(-230.5,9,232.5);
        poss[27] = new Vector3(-277.5,5,236.5);
        poss[28] = new Vector3(-252.5,4,239.5);
        poss[29] = new Vector3(-262.5,16,230.5);
        poss[30] = new Vector3(-261.5,16,238.5);
        List<Vector3> lists=Arrays.asList(poss);
        Collections.shuffle(lists);
        emerald =(Vector3[])lists.toArray(new Vector3[lists.size()]);
        return emerald;
    }

    public static Vector3[] spawn;
    public static Vector3[] emerald;
    public static Vector3[] pos;
    public static Vector3[] poss;

}