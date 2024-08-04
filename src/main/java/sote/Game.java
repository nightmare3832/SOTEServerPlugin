package sote;

import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.scheduler.Task;
import sote.inventory.ServerChestInventorys;
import sote.murder.MurderSignManager;

public abstract class Game{

    public Game(int number, boolean isHome){
        this.number = number;
        this.isHome = isHome;
    }

    public void addRoom(){
    }

    public void reset(){
    }

    public void Join(Player player){
    }

    public void LookJoin(Player player){
    }

    public void Quit(Player player){
    }

    public void LookQuit(Player player){
    }

    public void setSetting(String key, String value){
    }

    public int getGameDataAsInt(String key){
        return Integer.parseInt(GameData.get(key));
    }

    public void setGameDataAsInt(String key, int value, boolean update){
        GameData.put(key,String.valueOf(value));
        if(update) MurderSignManager.updataSign(number);
    }

    public boolean getGameDataAsBoolean(String key){
        return GameData.get(key).equals("1");
    }

    public void setGameDataAsBoolean(String key, boolean value, boolean update){
        String n = "0";
        if(value) n = "1";
        else n = "0";
        GameData.put(key, String.valueOf(n));
        if(update) MurderSignManager.updataSign(number);
    }

    public void closeChests(){
        for (Map.Entry<String,Player> e : Players.entrySet()){
            if(ServerChestInventorys.isOpen.containsKey(e.getValue()) && ServerChestInventorys.isOpen.get(e.getValue())){
                ServerChestInventorys.data2.get(e.getValue()).close2();
            }
        }
    }

    public void sendTip(Player player,String msg,int time){
        for(int i = 0;i <= time;i++){
            Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackTip(this, player, msg),i*20);
        }
    }

    public void sendTip(Player player,String msg){
        if(AllPlayers.containsValue(player) && player instanceof Player) player.sendTip(msg);
    }

    public int number = 0;
    public boolean isHome = false;
    public HashMap<String, String> GameData = new HashMap<String, String>();
    public HashMap<String, Player> Players = new HashMap<String, Player>();
    public HashMap<String, Player> Spectators = new HashMap<String, Player>();
    public HashMap<String, Player> AllPlayers = new HashMap<String, Player>();
    public HashMap<String, String> setting = new HashMap<String, String>();
    public Level world;
    public boolean canOpenChest = true;

}
class CallbackTip extends Task{

    public Game owner;
    public Player player;
    public String msg;

    public CallbackTip(Game owner, Player player,String msg){
        this.owner = owner;
        this.player = player;
        this.msg = msg;
    }

    public void onRun(int d){
        this.owner.sendTip(this.player,this.msg);
    }
}