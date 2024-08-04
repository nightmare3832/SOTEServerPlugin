package sote.commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.permission.Permission;
import sote.Main;
import sote.lobbyitem.LobbyItems;
import sote.stat.Stat;

public class Command_Adddata extends Command{

    public Command_Adddata(Main plugin){
        super("adddata");
        this.setPermission(Permission.DEFAULT_OP);
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String label, String args[]){
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(!Main.getBlock(player)) return false;
            if(args.length == 3){
                adddata(player,args[0],args[1],args[2]);
            }else{
                sender.sendMessage("§f[§dSOTE§f] §cERROR");
            }
        }else{
            sender.sendMessage("§f[§dSOTE§f] §cこのコマンドはゲーム内で実行してください");
        }
        return true;
    }

    public static void adddata(Player player,String data,String data2,String data3){
        Player target = Server.getInstance().getPlayer(data);
        int mm;
        if(target instanceof Player){
            switch(data2){
                case "karma":
                    mm = Integer.parseInt(data3);
                    Stat.setMurderKarma(target,Stat.getMurderKarma(target)+mm);
                break;
                case "coin":
                    mm = Integer.parseInt(data3);
                    Stat.setCoin(target,Stat.getCoin(target)+mm);
                break;
                case "lobbyitem":
                    LobbyItems.addLobbyItem(player,data3);
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
