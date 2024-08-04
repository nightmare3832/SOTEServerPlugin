package sote.skywarssolo;

import java.util.Map;

import cn.nukkit.Server;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntitySign;
import cn.nukkit.level.Level;
import sote.Game;
import sote.GameProvider;
import sote.home.Home;

public class SkywarsSoloSignManager{

    public SkywarsSoloSignManager(){
        Server.getInstance().loadLevel("skywars");
    }

    public static void updataSign(int number){
    	if(number >= 8) updataSign(number,Server.getInstance().getLevelByName("home"+Home.getId(number)));
        else updataSign(number,Server.getInstance().getLevelByName("skywars"));
    }

    public static void updataSign(int number,Level level){
        String[] text = getText(number);
        for(Map.Entry<Long,BlockEntity> e : level.getBlockEntities().entrySet()){
            BlockEntity blockentity = e.getValue();
            if(blockentity instanceof BlockEntitySign){
                BlockEntitySign sign = (BlockEntitySign) blockentity;
                String[] line = sign.getText();
                String lines = line[0]+line[1]+line[2]+line[3];
                String[] md = lines.split(GameProvider.SKYWARS_SOLO);
                if(md.length >= 2){
                   if(isNumber(md[1].substring(0,md[1].length()-2))){
                       if(Integer.parseInt(md[1].substring(0,md[1].length()-2)) == number){
                           level.loadChunk(((int)blockentity.x) >> 4,((int)blockentity.z) >> 4);
                           sign.setText(text[0],text[1],text[2],text[3]);
                       }
                   }
                }
            }
        }
    }

    public static String[] getText(int number){
        String[] text = new String[4];
        Game game = GameProvider.Games.get(number).get(GameProvider.SKYWARS_SOLO);
            if(game.getGameDataAsBoolean("gamenow")){
                text[0] = "§cCHANGING";
                text[1] = "§0Waiting";
                text[2] = "§c--/--";
            }else{
                if(game.getGameDataAsBoolean("timenow")){
                    text[0] = "§aPLAY §0"+game.getGameDataAsInt("time")+"s";
                }else{
                    text[0] = "§aPLAY";
                }
                text[1] = "§0Waiting";
                text[2] = "§a"+game.getGameDataAsInt("count")+"/"+SkywarsSolo.MAX_PLAYERS;
            }
        text[3] = "§0> "+GameProvider.SKYWARS_SOLO+getStringNumber(number)+" <";
        return text;
    }

    public static String getStringNumber(int number){
        if(number >= 10){
            return String.valueOf(number);
        }else{
            return "0"+String.valueOf(number);
        }
    }

    public static boolean isNumber(String num) {
        try {
            Integer.parseInt(num);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}