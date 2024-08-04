package sote.skywarssolo.stage;

import java.util.HashMap;

import cn.nukkit.math.Vector3;

public class MagicPolkaDot extends SkywarsSoloStage{

    public MagicPolkaDot(){
        register();
    }

    public void register(){
        Vector3[] pos = new Vector3[8];
        pos[0] = new Vector3(392,58,343);
        pos[1] = new Vector3(396,61,378);
        pos[2] = new Vector3(372,60,403);
        pos[3] = new Vector3(337,57,400);
        pos[4] = new Vector3(298,53,368);
        pos[5] = new Vector3(301,55,336);
        pos[6] = new Vector3(325,58,311);
        pos[7] = new Vector3(360,55,308);
        spawn = pos;
        /*chest = new HashMap<Vector3,Integer>();
        chest.put(new Vector3(336,64,372),2);
        chest.put(new Vector3(362,71,376),2);
        chest.put(new Vector3(367,71,342),2);
        chest.put(new Vector3(347,61,356),2);
        chest.put(new Vector3(349,61,358),2);
        chest.put(new Vector3(347,61,360),2);
        chest.put(new Vector3(345,61,358),2);
        chest.put(new Vector3(340,55,338),2);
        chest.put(new Vector3(372,56,307),1);
        chest.put(new Vector3(372,59,309),1);
        chest.put(new Vector3(393,56,328),1);
        chest.put(new Vector3(393,59,330),1);
        chest.put(new Vector3(385,63,316),2);
        chest.put(new Vector3(390,58,376),1);
        chest.put(new Vector3(392,58,381),1);
        chest.put(new Vector3(392,61,383),1);
        chest.put(new Vector3(373,56,406),1);
        chest.put(new Vector3(368,57,403),1);
        chest.put(new Vector3(368,60,405),1);
        chest.put(new Vector3(329,55,401),1);
        chest.put(new Vector3(329,58,403),1);
        chest.put(new Vector3(318,62,393),2);
        chest.put(new Vector3(310,55,378),1);
        chest.put(new Vector3(310,58,380),1);
        chest.put(new Vector3(299,55,337),1);
        chest.put(new Vector3(298,55,331),1);
        chest.put(new Vector3(298,58,333),1);
        chest.put(new Vector3(330,58,311),1);
        chest.put(new Vector3(326,58,303),1);
        chest.put(new Vector3(326,61,305),1);*/
    }

    @Override
    public String getName(){
        return "Magic Polka Dot";
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