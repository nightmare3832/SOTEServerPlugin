package sote.murder.stage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;

public class WildernessTowns extends MurderStage{

    public WildernessTowns(){
        Server.getInstance().loadLevel("murder");
        register();
    }

    public static void register(){
    }

    @Override
    public String getName(){
        return "Wilderness Towns";
    }

    @Override
    public Item getItem(){
        return Item.get(Item.CLAY, 1);
    }

    @Override
    public Vector3[] getSpawn(){
        Vector3[] pos = new Vector3[16];
        pos[0] = new Vector3(300.5,4,-248.5);
        pos[1] = new Vector3(314.5,5,-267.5);
        pos[2] = new Vector3(337.5,4,-273.5);
        pos[3] = new Vector3(313.5,9,-252.5);
        pos[4] = new Vector3(290.5,5,-269.5);
        pos[5] = new Vector3(284.5,11,-264.5);
        pos[6] = new Vector3(243.5,4,-307.5);
        pos[7] = new Vector3(267.5,3,-316.5);
        pos[8] = new Vector3(309.5,2,-337.5);
        pos[9] = new Vector3(293.5,4,-363.5);
        pos[10] = new Vector3(302.5,3,-313.5);
        pos[11] = new Vector3(342.5,4,-307.5);
        pos[12] = new Vector3(344.5,14,-325.5);
        pos[13] = new Vector3(333.5,8,-323.5);
        pos[14] = new Vector3(265.5,4,-265.5);
        pos[15] = new Vector3(285.5,5,-296.5);
        List<Vector3> list=Arrays.asList(pos);
        Collections.shuffle(list);
        spawn =(Vector3[])list.toArray(new Vector3[list.size()]);
        return spawn;
    }

    @Override
    public Vector3[] getEmerald(){
        Vector3[] poss = new Vector3[36];
        poss[0] = new Vector3(269.5,8,-323.5);
        poss[1] = new Vector3(275.5,4,-333.5);
        poss[2] = new Vector3(295.5,2,-340.5);
        poss[3] = new Vector3(299.5,4,-358.5);
        poss[4] = new Vector3(310.5,4,-346.5);
        poss[5] = new Vector3(316.5,4,-330.5);
        poss[6] = new Vector3(323.5,4,-330.5);
        poss[7] = new Vector3(332.5,4,-323.5);
        poss[8] = new Vector3(306.5,3,-320.5);
        poss[9] = new Vector3(326.5,4,-316.5);
        poss[10] = new Vector3(343.5,14,-307.5);
        poss[11] = new Vector3(356.5,17,-326.5);
        poss[12] = new Vector3(334.5,4,-294.5);
        poss[13] = new Vector3(346.5,4,-290.5);
        poss[14] = new Vector3(344.5,3,-303.5);
        poss[15] = new Vector3(332.5,4,-266.5);
        poss[16] = new Vector3(321.5,9,-244.5);
        poss[17] = new Vector3(319.5,4,-257.5);
        poss[18] = new Vector3(308.5,4,-262.5);
        poss[19] = new Vector3(292.5,4,-256.5);
        poss[20] = new Vector3(301.5,4,-276.5);
        poss[21] = new Vector3(293.5,5,-264.5);
        poss[22] = new Vector3(277.5,5,-269.5);
        poss[23] = new Vector3(282.5,5,-264.5);
        poss[24] = new Vector3(277.5,11,-264.5);
        poss[25] = new Vector3(281.5,10,-270.5);
        poss[26] = new Vector3(257.5,4,-267.5);
        poss[27] = new Vector3(256.5,9,-267.5);
        poss[28] = new Vector3(299.5,4,-297.5);
        poss[29] = new Vector3(318.5,4,-306.5);
        poss[30] = new Vector3(320.5,4,-298.5);
        poss[31] = new Vector3(262.5,5,-292.5);
        poss[32] = new Vector3(262.5,5,-292.5);
        poss[33] = new Vector3(313.5,10,-272.5);
        poss[34] = new Vector3(324.5,10,-264.5);
        poss[35] = new Vector3(324.5,5,-274.5);
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