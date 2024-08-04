package sote.skywarssolo;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.math.Vector3;

public class SkywarsSoloCageManager{

    public SkywarsSoloCageManager(){
    }

    public static void appearCage(SkywarsSolo owner, Player player, Vector3 vec){
        int[][] blocks = new int[][]{new int[]{0, -1, 0},//floor
                                     new int[]{1, 0, 0}, new int[]{0, 0, 1},new int[]{-1, 0, 0}, new int[]{0, 0, -1},//layer1
                                     new int[]{1, 1, 0}, new int[]{0, 1, 1},new int[]{-1, 1, 0}, new int[]{0, 1, -1},//layer2
                                     new int[]{1, 2, 0}, new int[]{0, 2, 1},new int[]{-1, 2, 0}, new int[]{0, 2, -1},//layer3
                                     new int[]{0, 3, 0}};//top
        for(int[] pos : blocks){
            owner.world.setBlock(vec.add(pos[0], pos[1], pos[2]), Block.get(Block.GLASS));
        }
    }

    public static void disappearCage(SkywarsSolo owner, Player player, Vector3 vec){
        int[][] blocks = new int[][]{new int[]{0, -1, 0},//floor
                                     new int[]{1, 0, 0}, new int[]{0, 0, 1},new int[]{-1, 0, 0}, new int[]{0, 0, -1},//layer1
                                     new int[]{1, 1, 0}, new int[]{0, 1, 1},new int[]{-1, 1, 0}, new int[]{0, 1, -1},//layer2
                                     new int[]{1, 2, 0}, new int[]{0, 2, 1},new int[]{-1, 2, 0}, new int[]{0, 2, -1},//layer3
                                     new int[]{0, 3, 0}};//top
        for(int[] pos : blocks){
            owner.world.setBlock(vec.add(pos[0], pos[1], pos[2]), Block.get(Block.AIR));
        }
    }
}