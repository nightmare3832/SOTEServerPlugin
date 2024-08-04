package sote.bedwars.stage;

import cn.nukkit.math.Vector3;

public abstract class BedwarsStage{

    public BedwarsStage() {
    }

    public String getName() {
        return "";
    }

    public Vector3[] getRedData() {
        return new Vector3[]{};
    }

    public Vector3[] getBlueData() {
        return new Vector3[]{};
    }

    public Vector3[] getGreenData() {
        return new Vector3[]{};
    }

    public Vector3[] getYellowData() {
        return new Vector3[]{};
    }

    public Vector3[] getAquaData() {
        return new Vector3[]{};
    }

    public Vector3[] getWhiteData() {
        return new Vector3[]{};
    }

    public Vector3[] getPinkData() {
        return new Vector3[]{};
    }

    public Vector3[] getGrayData() {
        return new Vector3[]{};
    }

    public Vector3[] getDiamondGeneratorPosition(){
        return null;
    }

    public Vector3[] getEmeraldGeneratorPosition(){
        return null;
    }

}
