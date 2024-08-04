package sote.skywarssolo.stage;

import java.util.HashMap;

import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;

public class TheNature extends SkywarsSoloStage{

    public TheNature(){
        register();
    }

    public void register(){
        Vector3[] pos = new Vector3[12];
        pos[0] = new Vector3(324.5,74,350.5);
        pos[1] = new Vector3(329.5,74,328.5);
        pos[2] = new Vector3(324.5,74,306.5);
        pos[3] = new Vector3(279.5,74,271.5);
        pos[4] = new Vector3(257.5,74,266.5);
        pos[5] = new Vector3(235.5,74,271.5);
        pos[6] = new Vector3(200.5,74,316.5);
        pos[7] = new Vector3(195.5,74,338.5);
        pos[8] = new Vector3(200.5,74,360.5);
        pos[9] = new Vector3(245.5,74,395.5);
        pos[10] = new Vector3(267.5,74,400.5);
        pos[11] = new Vector3(289.5,74,395.5);
        spawn = pos;
        chest = new HashMap<Integer,Vector3>();
        firstChests = new HashMap<Integer, Vector3[]>();
        firstChests.put(1, new Vector3[]{new Vector3(324,56,302), new Vector3(322,59,309), new Vector3(324,48,311)});
        firstChests.put(2, new Vector3[]{new Vector3(329,56,324), new Vector3(327,59,331), new Vector3(329,48,333)});
        firstChests.put(3, new Vector3[]{new Vector3(324,56,346), new Vector3(322,59,353), new Vector3(324,48,355)});
        firstChests.put(4, new Vector3[]{new Vector3(275,56,271), new Vector3(282,59,273), new Vector3(284,48,271)});
        firstChests.put(5, new Vector3[]{new Vector3(253,56,266), new Vector3(260,59,268), new Vector3(262,48,266)});
        firstChests.put(6, new Vector3[]{new Vector3(231,56,271), new Vector3(231,59,271), new Vector3(240,48,271)});
        firstChests.put(7, new Vector3[]{new Vector3(200,56,320), new Vector3(202,59,313), new Vector3(200,48,311)});
        firstChests.put(8, new Vector3[]{new Vector3(195,56,342), new Vector3(197,59,335), new Vector3(195,48,333)});
        firstChests.put(9, new Vector3[]{new Vector3(200,56,364), new Vector3(202,59,357), new Vector3(200,48,355)});
        firstChests.put(10, new Vector3[]{new Vector3(249,56,395), new Vector3(242,59,393), new Vector3(240,48,395)});
        firstChests.put(11, new Vector3[]{new Vector3(271,56,400), new Vector3(264,59,398), new Vector3(262,48,400)});
        firstChests.put(12, new Vector3[]{new Vector3(293,56,395), new Vector3(286,59,393), new Vector3(284,48,395)});

        chest.put(1,new Vector3(294,57,339));
        chest.put(2,new Vector3(256,57,365));
        chest.put(3,new Vector3(230,57,327));
        chest.put(4,new Vector3(268,57,301));

        chest.put(5,new Vector3(262,70,331));
        chest.put(6,new Vector3(260,70,334));
    }

    @Override
    public String getName(){
        return "The Nature";
    }

    @Override
    public Item getItem(){
        return Item.get(Item.GRASS);
    }

    @Override
    public Vector3[] getSpawn(){
        return spawn;
    }

    @Override
    public HashMap<Integer, Vector3> getMiddleChests(){
        return chest;
    }

    @Override
    public HashMap<Integer, Vector3[]> getFirstChests(){
        return firstChests;
    }

    public static Vector3[] spawn;
    public static HashMap<Integer, Vector3> chest;
    public static HashMap<Integer, Vector3[]> firstChests;
    public static Vector3[] pos;
    public static Vector3[] poss;

}