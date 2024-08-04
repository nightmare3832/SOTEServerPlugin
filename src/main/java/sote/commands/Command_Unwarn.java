package sote.commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.permission.Permission;
import sote.Main;
import sote.stat.Stat;

public class Command_Unwarn extends Command{

    public Command_Unwarn(Main plugin){
        super("unwarn");
        this.setPermission(Permission.DEFAULT_OP);
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String label, String args[]){
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(!(Stat.getVip(player) >= 1)) return false;
            if(args.length == 1){
                unwarn(player,args[0]);
            }else{
                sender.sendMessage("§f[§dSOTE§f] §cERROR");
            }
        }else{
            if(args.length == 1){
               unwarn(sender,args[0]);
            }else{
                sender.sendMessage("§f[§dSOTE§f] §cERROR");
            }
        }
        return true;
    }

    public static void unwarn(Player player,String data){
        Player target = Server.getInstance().getPlayer(data);
        if(target instanceof Player){
            int w = Stat.getWarn(target);
            if(w-1 >= 0) Stat.setWarn(target,w-1);
            Stat.setNameTag(target);
            player.sendMessage("§f[§dSOTE§f] §a"+data+"をunwarnしました");
        }else{
            player.sendMessage("§f[§dSOTE§f] §cERROR");
        }
    }

    public static void unwarn(CommandSender player,String data){
        Player target = Server.getInstance().getPlayer(data);
        if(target instanceof Player){
            int w = Stat.getWarn(target);
            if(w-1 >= 0) Stat.setWarn(target,w-1);
            Stat.setNameTag(target);
            player.sendMessage("§f[§dSOTE§f] §a"+data+"をunwarnしました");
        }else{
            player.sendMessage("§f[§dSOTE§f] §cERROR");
        }
    }

    Main plugin;
}
