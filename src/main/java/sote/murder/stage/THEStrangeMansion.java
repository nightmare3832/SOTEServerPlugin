package sote.murder.stage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;

public class THEStrangeMansion extends MurderStage{

    public THEStrangeMansion(){
        Server.getInstance().loadLevel("murder");
        register();
    }

    public static void register(){
    }

    @Override
    public String getName(){
        return "THE Strange Mansion";
    }

    @Override
    public Item getItem(){
        return Item.get(Item.PLANK, 3);
    }

    @Override
    public Vector3[] getSpawn(){
        Vector3[] pos = new Vector3[15];
        pos[0] = new Vector3(235.5,13,237.5);
        pos[1] = new Vector3(261.5,13,230.5);
        pos[2] = new Vector3(258.5,13,248.5);
        pos[3] = new Vector3(235.5,7,261.5);
        pos[4] = new Vector3(257.5,7,281.5);
        pos[5] = new Vector3(221.5,9,282.5);
        pos[6] = new Vector3(213.5,8,239.5);
        pos[7] = new Vector3(252.5,7,215.5);
        pos[8] = new Vector3(285.5,8,248.5);
        pos[9] = new Vector3(258.5,3,273.5);
        pos[10] = new Vector3(254.5,3,239.5);
        pos[11] = new Vector3(250.5,13,242.5);
        pos[12] = new Vector3(253.5,17,231.5);
        pos[13] = new Vector3(243.5,25,240.5);
        pos[14] = new Vector3(247.5,21,239.5);
        List<Vector3> list=Arrays.asList(pos);
        Collections.shuffle(list);
        spawn =(Vector3[])list.toArray(new Vector3[list.size()]);
        return spawn;
    }

    @Override
    public Vector3[] getEmerald(){
        Vector3[] poss = new Vector3[35];
        poss[0] = new Vector3(242.5,21,239.5);
        poss[1] = new Vector3(255.5,21,238.5);
        poss[2] = new Vector3(251.5,21,230.5);
        poss[3] = new Vector3(249.5,24,228.5);
        poss[4] = new Vector3(252.5,25,224.5);
        poss[5] = new Vector3(260.5,25,239.5);
        poss[6] = new Vector3(241.5,25,237.5);
        poss[7] = new Vector3(243.5,17,238.5);
        poss[8] = new Vector3(250.5,18,244.5);
        poss[9] = new Vector3(250.5,13,248.5);
        poss[10] = new Vector3(258.5,13,237.5);
        poss[11] = new Vector3(250.5,10,239.5);
        poss[12] = new Vector3(260.5,3,253.5);
        poss[13] = new Vector3(246.5,3,253.5);
        poss[14] = new Vector3(260.5,3,267.5);
        poss[15] = new Vector3(240.5,3,245.5);
        poss[16] = new Vector3(227.5,3,232.5);
        poss[17] = new Vector3(227.5,7,228.5);
        poss[18] = new Vector3(222.5,7,241.5);
        poss[19] = new Vector3(235.5,7,217.5);
        poss[20] = new Vector3(212.5,7,259.5);
        poss[21] = new Vector3(216.5,9,277.5);
        poss[22] = new Vector3(229.5,6,286.5);
        poss[23] = new Vector3(244.5,7,289.5);
        poss[24] = new Vector3(244.5,7,308.5);
        poss[25] = new Vector3(276.5,7,291.5);
        poss[26] = new Vector3(268.5,5,274.5);
        poss[27] = new Vector3(279.5,9,262.5);
        poss[28] = new Vector3(254.5,7,265.5);
        poss[29] = new Vector3(276.5,7,246.5);
        poss[30] = new Vector3(280.5,7,233.5);
        poss[31] = new Vector3(267.5,10,224.5);
        poss[32] = new Vector3(252.5,10,220.5);
        poss[33] = new Vector3(248.5,13,230.5);
        poss[34] = new Vector3(249.5,13,223.5);
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