package sote.commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.permission.Permission;
import sote.Main;
import sote.ban.Ban;

public class Command_Shut extends Command{

    public Command_Shut(Main plugin){
        super("shut");
        this.setPermission(Permission.DEFAULT_OP);
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String label, String args[]){
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(args.length == 1){
                shut(player,args[0]);
            }else{
                sender.sendMessage("§f[§dSOTE§f] §cERROR");
            }
        }else{
            if(args.length == 1){
                shut(sender,args[0]);
            }else{
                sender.sendMessage("§f[§dSOTE§f] §cERROR");
            }
        }
        return true;
    }

    public static void shut(Player player,String data){
        Player target = Server.getInstance().getPlayer(data);
        if(target instanceof Player){
            Ban.setChatBan(target,true);
            player.sendMessage("§f[§dSOTE§f] §a"+data+"をshutしました");
        }else{
            player.sendMessage("§f[§dSOTE§f] §cERROR");
        }
    }

    public static void shut(CommandSender player,String data){
        Player target = Server.getInstance().getPlayer(data);
        if(target instanceof Player){
            Ban.setChatBan(target,true);
            player.sendMessage("§f[§dSOTE§f] §a"+data+"をshutしました");
        }else{
            player.sendMessage("§f[§dSOTE§f] §cERROR");
        }
    }

    Main plugin;
}
