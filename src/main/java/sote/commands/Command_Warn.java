package sote.commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.permission.Permission;
import sote.Main;
import sote.ban.Ban;
import sote.stat.Stat;

public class Command_Warn extends Command{

    public Command_Warn(Main plugin){
        super("warn");
        this.setPermission(Permission.DEFAULT_OP);
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String label, String args[]){
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(!(Stat.getVip(player) >= 1)) return false;
            if(args.length == 1){
                warn(player,args[0]);
            }else{
                sender.sendMessage("§f[§dSOTE§f] §cERROR");
            }
        }else{
            if(args.length == 1){
                warn(sender,args[0]);
            }else{
                sender.sendMessage("§f[§dSOTE§f] §cERROR");
            }
        }
        return true;
    }

    public static void warn(Player player,String data){
        Player target = Server.getInstance().getPlayer(data);
        if(target instanceof Player){
            int w = Stat.getWarn(target);
            Stat.setWarn(target,w+1);
            Stat.setNameTag(target);
            if(Stat.getWarn(target) >= 3) Ban.Nban(target, "unknown");
            player.sendMessage("§f[§dSOTE§f] §a"+data+"をwarnしました");
        }else{
            player.sendMessage("§f[§dSOTE§f] §cERROR");
        }
    }

    public static void warn(CommandSender player,String data){
        Player target = Server.getInstance().getPlayer(data);
        if(target instanceof Player){
            int w = Stat.getWarn(target);
            Stat.setWarn(target,w+1);
            Stat.setNameTag(target);
            if(Stat.getWarn(target) >= 3) Ban.Nban(target, "unknown");
            player.sendMessage("§f[§dSOTE§f] §a"+data+"をwarnしました");
        }else{
            player.sendMessage("§f[§dSOTE§f] §cERROR");
        }
    }

    Main plugin;
}
