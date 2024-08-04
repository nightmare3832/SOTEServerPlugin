package sote.bedwars.stage;

import cn.nukkit.math.Vector3;

public class Library extends BedwarsStage{

    public Library(){
        this.red = new Vector3(518.5, 51, 568.5);
        this.blue = new Vector3(483.5, 51, 568.5);
        this.green = new Vector3(432.5, 51, 517.5);
        this.yellow = new Vector3(432.5, 51, 483.5);
        this.aqua = new Vector3(483.5, 51, 432.5);
        this.white = new Vector3(518.5, 51, 432.5);
        this.pink = new Vector3(569.5, 51, 483.5);
        this.gray = new Vector3(569.5, 51, 517.5);

        this.redForge = new Vector3(518.5, 51, 572.5);
        this.blueForge = new Vector3(483.5, 51, 572.5);
        this.greenForge = new Vector3(428.5, 51, 517.5);
        this.yellowForge = new Vector3(428.5, 51, 483.5);
        this.aquaForge = new Vector3(483.5, 51, 428.5);
        this.whiteForge = new Vector3(518.5, 51, 428.5);
        this.pinkForge = new Vector3(573.5, 51, 483.5);
        this.grayForge = new Vector3(573.5, 51, 517.5);

        this.redForge2 = new Vector3(518.5, 51, 572.5);
        this.blueForge2 = new Vector3(483.5, 51, 572.5);
        this.greenForge2 = new Vector3(428.5, 51, 517.5);
        this.yellowForge2 = new Vector3(428.5, 51, 483.5);
        this.aquaForge2 = new Vector3(483.5, 51, 428.5);
        this.whiteForge2 = new Vector3(518.5, 51, 428.5);
        this.pinkForge2 = new Vector3(573.5, 51, 483.5);
        this.grayForge2 = new Vector3(573.5, 51, 517.5);

        this.redBedTop = new Vector3(518, 50, 559);
        this.blueBedTop = new Vector3(483, 50, 559);
        this.greenBedTop = new Vector3(441, 50, 517);
        this.yellowBedTop = new Vector3(441, 50, 483);
        this.aquaBedTop = new Vector3(483, 50, 441);
        this.whiteBedTop = new Vector3(518, 50, 441);
        this.pinkBedTop = new Vector3(560, 50, 483);
        this.grayBedTop = new Vector3(560, 50, 517);

        this.redBedBottom = new Vector3(518, 50, 560);
        this.blueBedBottom = new Vector3(483, 50, 560);
        this.greenBedBottom = new Vector3(440, 50, 517);
        this.yellowBedBottom = new Vector3(440, 50, 483);
        this.aquaBedBottom = new Vector3(483, 50, 440);
        this.whiteBedBottom = new Vector3(518, 50, 440);
        this.pinkBedBottom = new Vector3(561, 50, 483);
        this.grayBedBottom = new Vector3(561, 50, 517);

        this.redItemShop = new Vector3(514.5, 51, 570.5);
        this.blueItemShop = new Vector3(487.5, 51, 570.5);
        this.greenItemShop = new Vector3(430.5, 51, 513.5);
        this.yellowItemShop = new Vector3(430.5, 51, 487.5);
        this.aquaItemShop = new Vector3(487.5, 51, 430.5);
        this.whiteItemShop = new Vector3(514.5, 51, 430.5);
        this.pinkItemShop = new Vector3(571.5, 51, 487.5);
        this.grayItemShop = new Vector3(571.5, 51, 513.5);

        this.redUpgrader = new Vector3(522.5, 51, 570.5);
        this.blueUpgrader = new Vector3(479.5, 51, 570.5);
        this.greenUpgrader = new Vector3(430.5, 51, 521.5);
        this.yellowUpgrader = new Vector3(430.5, 51, 479.5);
        this.aquaUpgrader = new Vector3(479.5, 51, 430.5);
        this.whiteUpgrader = new Vector3(522.5, 51, 430.5);
        this.pinkUpgrader = new Vector3(571.5, 51, 479.5);
        this.grayUpgrader = new Vector3(571.5, 51, 521.5);

        this.diamondGenerator = new Vector3(500.5, 52, 542.5);
        this.diamondGenerator2 = new Vector3(458.5, 52, 500.5);
        this.diamondGenerator3 = new Vector3(501.5, 52, 458.5);
        this.diamondGenerator4 = new Vector3(543.5, 52, 500.5);

        this.emeraldGenerator = new Vector3(500.5, 67, 500.5);
        this.emeraldGenerator2 = new Vector3(500.5, 52, 500.5);
        this.emeraldGenerator3 = new Vector3(487.5, 52, 513.5);
        this.emeraldGenerator4 = new Vector3(513.5, 52, 487.5);
    }

    @Override
    public String getName(){
        return "Library";
    }

    @Override
    public Vector3[] getRedData(){
        return new Vector3[]{red, redForge, redForge2, redBedTop, redBedBottom, redItemShop, redUpgrader};
    }

    @Override
    public Vector3[] getBlueData(){
        return new Vector3[]{blue, blueForge, blueForge2, blueBedTop, blueBedBottom, blueItemShop, blueUpgrader};
    }

    @Override
    public Vector3[] getGreenData(){
        return new Vector3[]{green, greenForge, greenForge2, greenBedTop, greenBedBottom, greenItemShop, greenUpgrader};
    }

    @Override
    public Vector3[] getYellowData(){
        return new Vector3[]{yellow, yellowForge, yellowForge2, yellowBedTop, yellowBedBottom, yellowItemShop, yellowUpgrader};
    }

    @Override
    public Vector3[] getAquaData(){
        return new Vector3[]{aqua, aquaForge, aquaForge2, aquaBedTop, aquaBedBottom, aquaItemShop, aquaUpgrader};
    }

    @Override
    public Vector3[] getWhiteData(){
        return new Vector3[]{white, whiteForge, whiteForge2, whiteBedTop, whiteBedBottom, whiteItemShop, whiteUpgrader};
    }

    @Override
    public Vector3[] getPinkData(){
        return new Vector3[]{pink, pinkForge, pinkForge2, pinkBedTop, pinkBedBottom, pinkItemShop, pinkUpgrader};
    }

    @Override
    public Vector3[] getGrayData(){
        return new Vector3[]{gray, grayForge, grayForge2, grayBedTop, grayBedBottom, grayItemShop, grayUpgrader};
    }

    @Override
    public Vector3[] getDiamondGeneratorPosition(){
        return new Vector3[]{diamondGenerator, diamondGenerator2, diamondGenerator3, diamondGenerator4};
    }

    @Override
    public Vector3[] getEmeraldGeneratorPosition(){
        return new Vector3[]{emeraldGenerator, emeraldGenerator2, emeraldGenerator3, emeraldGenerator4};
    }

    public Vector3 red = new Vector3();
    public Vector3 blue = new Vector3();
    public Vector3 green = new Vector3();
    public Vector3 yellow = new Vector3();
    public Vector3 aqua = new Vector3();
    public Vector3 white = new Vector3();
    public Vector3 pink = new Vector3();
    public Vector3 gray = new Vector3();

    public Vector3 redForge = new Vector3();
    public Vector3 blueForge = new Vector3();
    public Vector3 greenForge = new Vector3();
    public Vector3 yellowForge = new Vector3();
    public Vector3 aquaForge = new Vector3();
    public Vector3 whiteForge = new Vector3();
    public Vector3 pinkForge = new Vector3();
    public Vector3 grayForge = new Vector3();

    public Vector3 redForge2 = new Vector3();
    public Vector3 blueForge2 = new Vector3();
    public Vector3 greenForge2 = new Vector3();
    public Vector3 yellowForge2 = new Vector3();
    public Vector3 aquaForge2 = new Vector3();
    public Vector3 whiteForge2 = new Vector3();
    public Vector3 pinkForge2 = new Vector3();
    public Vector3 grayForge2 = new Vector3();

    public Vector3 redBedTop = new Vector3();
    public Vector3 blueBedTop = new Vector3();
    public Vector3 greenBedTop = new Vector3();
    public Vector3 yellowBedTop = new Vector3();
    public Vector3 aquaBedTop = new Vector3();
    public Vector3 whiteBedTop = new Vector3();
    public Vector3 pinkBedTop = new Vector3();
    public Vector3 grayBedTop = new Vector3();

    public Vector3 redBedBottom = new Vector3();
    public Vector3 blueBedBottom = new Vector3();
    public Vector3 greenBedBottom = new Vector3();
    public Vector3 yellowBedBottom = new Vector3();
    public Vector3 aquaBedBottom = new Vector3();
    public Vector3 whiteBedBottom = new Vector3();
    public Vector3 pinkBedBottom = new Vector3();
    public Vector3 grayBedBottom = new Vector3();

    public Vector3 redItemShop = new Vector3();
    public Vector3 blueItemShop = new Vector3();
    public Vector3 greenItemShop = new Vector3();
    public Vector3 yellowItemShop = new Vector3();
    public Vector3 aquaItemShop = new Vector3();
    public Vector3 whiteItemShop = new Vector3();
    public Vector3 pinkItemShop = new Vector3();
    public Vector3 grayItemShop = new Vector3();

    public Vector3 redUpgrader = new Vector3();
    public Vector3 blueUpgrader = new Vector3();
    public Vector3 greenUpgrader = new Vector3();
    public Vector3 yellowUpgrader = new Vector3();
    public Vector3 aquaUpgrader = new Vector3();
    public Vector3 whiteUpgrader = new Vector3();
    public Vector3 pinkUpgrader = new Vector3();
    public Vector3 grayUpgrader = new Vector3();

    public Vector3 diamondGenerator = new Vector3();
    public Vector3 diamondGenerator2 = new Vector3();
    public Vector3 diamondGenerator3 = new Vector3();
    public Vector3 diamondGenerator4 = new Vector3();

    public Vector3 emeraldGenerator = new Vector3();
    public Vector3 emeraldGenerator2 = new Vector3();
    public Vector3 emeraldGenerator3 = new Vector3();
    public Vector3 emeraldGenerator4 = new Vector3();

}