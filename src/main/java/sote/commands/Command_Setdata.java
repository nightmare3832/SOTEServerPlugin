package sote.commands;

import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.permission.Permission;
import sote.Main;
import sote.lobbyitem.LobbyItems;
import sote.stat.Stat;

public class Command_Setdata extends Command{

    public Command_Setdata(Main plugin){
        super("setdata");
        this.setPermission(Permission.DEFAULT_OP);
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String label, String args[]){
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(!Main.getBlock(player)) return false;
            if(args.length == 3){
                setdata(player,args[0],args[1],args[2]);
            }else{
                sender.sendMessage("§f[§dSOTE§f] §cERROR");
            }
        }else{
            sender.sendMessage("§f[§dSOTE§f] §cこのコマンドはゲーム内で実行してください");
        }
        return true;
    }

    public static void setdata(Player player,String data,String data2,String data3){
        Player target = Server.getInstance().getPlayer(data);
        if(target instanceof Player){
            switch(data2){
                case "vip":
                    Stat.setVip(target,Integer.parseInt(data3));
                break;
                case "coin":
                    Stat.setCoin(target,Integer.parseInt(data3));
                break;
                case "lobbyitem":
                    LobbyItems.setSellectLobbyItem(target,data3);
                break;
                default:
                    ((Map<String, String>) Stat.stat.get(target.getName().toLowerCase())).put(data2,String.valueOf(data3));
                break;
            }
            Stat.setNameTag(target);
            player.sendMessage("§f[§dSOTE§f] §a成功");
        }else{
            player.sendMessage("§f[§dSOTE§f] §cERROR");
        }
    }

    Main plugin;
}
