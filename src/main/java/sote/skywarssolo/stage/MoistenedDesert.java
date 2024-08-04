package sote.skywarssolo.stage;

import java.util.HashMap;

import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;

public class MoistenedDesert extends SkywarsSoloStage{

    public MoistenedDesert(){
        register();
    }

    public void register(){
        Vector3[] pos = new Vector3[12];
        pos[0] = new Vector3(-430.5,71.2,-386.5);
        pos[1] = new Vector3(-436.5,71.2,-407.5);
        pos[2] = new Vector3(-430.5,71.2,-429.5);
        pos[3] = new Vector3(-405.5,71.2,-462.5);
        pos[4] = new Vector3(-383.5,71.2,-468.5);
        pos[5] = new Vector3(-361.5,71.2,-462.5);
        pos[6] = new Vector3(-328.5,71.2,-438.5);
        pos[7] = new Vector3(-322.5,71.2,-416.5);
        pos[8] = new Vector3(-328.5,71.2,-394.5);
        pos[9] = new Vector3(-353.5,71.2,-361.5);
        pos[10] = new Vector3(-375.5,71.2,-355.5);
        pos[11] = new Vector3(-397.5,71.2,-361.5);
        spawn = pos;
        /*chest = new HashMap<Vector3,Integer>();
        chest.put(new Vector3(-354,70,-365),1);
        chest.put(new Vector3(-358,75,-360),1);
        chest.put(new Vector3(-361,80,-362),1);
        chest.put(new Vector3(-376,70,-359),1);
        chest.put(new Vector3(-380,75,-354),1);
        chest.put(new Vector3(-383,80,-356),1);
        chest.put(new Vector3(-398,70,-365),1);
        chest.put(new Vector3(-402,75,-360),1);
        chest.put(new Vector3(-405,80,-362),1);
        chest.put(new Vector3(-426,70,-386),1);
        chest.put(new Vector3(-431,75,-390),1);
        chest.put(new Vector3(-429,80,-393),1);
        chest.put(new Vector3(-432,70,-408),1);
        chest.put(new Vector3(-437,75,-412),1);
        chest.put(new Vector3(-435,80,-415),1);
        chest.put(new Vector3(-426,70,-430),1);
        chest.put(new Vector3(-431,75,-434),1);
        chest.put(new Vector3(-429,80,-437),1);
        chest.put(new Vector3(-404,70,-458),1);
        chest.put(new Vector3(-400,75,-463),1);
        chest.put(new Vector3(-397,80,-461),1);
        chest.put(new Vector3(-382,70,-464),1);
        chest.put(new Vector3(-378,75,-469),1);
        chest.put(new Vector3(-375,80,-467),1);
        chest.put(new Vector3(-360,70,-458),1);
        chest.put(new Vector3(-356,75,-463),1);
        chest.put(new Vector3(-353,80,-461),1);
        chest.put(new Vector3(-332,70,-437),1);
        chest.put(new Vector3(-327,75,-433),1);
        chest.put(new Vector3(-329,80,-430),1);
        chest.put(new Vector3(-326,70,-415),1);
        chest.put(new Vector3(-321,75,-411),1);
        chest.put(new Vector3(-323,80,-408),1);
        chest.put(new Vector3(-332,70,-393),1);
        chest.put(new Vector3(-327,75,-389),1);
        chest.put(new Vector3(-329,80,-386),1);
        
        chest.put(new Vector3(-334,77,-458),2);
        chest.put(new Vector3(-332,77,-367),2);
        chest.put(new Vector3(-424,77,-365),2);
        chest.put(new Vector3(-426,77,-456),2);
        
        chest.put(new Vector3(-348,75,-401),2);
        chest.put(new Vector3(-347,76,-422),2);
        chest.put(new Vector3(-368,75,-422),2);
        chest.put(new Vector3(-389,76,-443),2);
        chest.put(new Vector3(-410,75,-422),2);
        chest.put(new Vector3(-411,76,-401),2);
        chest.put(new Vector3(-390,75,-381),2);
        chest.put(new Vector3(-369,76,-380),2);
        
        chest.put(new Vector3(-376,75,-408),2);
        chest.put(new Vector3(-384,75,-416),2);
        
        chest.put(new Vector3(-380,68,-412),2);*/
    }

    @Override
    public String getName(){
        return "Moistened Desert";
    }

    @Override
    public Item getItem(){
        return Item.get(Item.SANDSTONE);
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