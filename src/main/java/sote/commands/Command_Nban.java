package sote.commands;

import java.util.Calendar;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.permission.Permission;
import sote.Main;
import sote.ban.Ban;
import sote.stat.Stat;

public class Command_Nban extends Command{

    public Command_Nban(Main plugin){
        super("nban");
        this.setPermission(Permission.DEFAULT_OP);
        this.plugin = plugin;
        this.commandParameters.put("nolimit", new CommandParameter[]{
                new CommandParameter("target", CommandParameter.ARG_TYPE_RAW_TEXT),
                new CommandParameter("reason", new String[]{"fly","speed","chat","testvideo"})
        });
        this.commandParameters.put("limit", new CommandParameter[]{
                new CommandParameter("target", CommandParameter.ARG_TYPE_RAW_TEXT),
                new CommandParameter("amount", CommandParameter.ARG_TYPE_INT),
                new CommandParameter("unit", new String[]{"years","months","days","hours","minutes","seconds"}),
                new CommandParameter("reason", new String[]{"fly","speed","chat","testvideo"})
        });
    }

    public boolean execute(CommandSender sender, String label, String args[]){
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(!(Stat.getVip(player) >= 10)) return false;
            if(args.length == 2) nban(player, args[0], args[1]);
            else if(args.length == 4) nban(player, args[0], args[1], args[2], args[3]);
            else sender.sendMessage("§f[§dSOTE§f] §cERROR");
        }else{
            if(args.length == 2) nban(sender, args[0], args[1]);
            else if(args.length == 4) nban(sender, args[0], args[1], args[2], args[3]);
            else sender.sendMessage("§f[§dSOTE§f] §cERROR");
        }
        return true;
    }

    public static void nban(CommandSender player, String data, String reason){
        Player target = Server.getInstance().getPlayer(data);
        if(target instanceof Player){
            Ban.Nban(target, reason);
            player.sendMessage("§f[§dSOTE§f] §a"+data+"をnbanしました");
        }else{
            Ban.Nban(data, reason);
            player.sendMessage("§f[§dSOTE§f] §a"+data+"をnbanしました");
        }
    }

    public static void nban(CommandSender player, String data, String amount, String field, String reason){
        Player target = Server.getInstance().getPlayer(data);
        int f = 0;
        switch(field){
            case "years":
                f = Calendar.YEAR;
                break;
            case "months":
                f = Calendar.MONTH;
                break;
            case "days":
                f = Calendar.DATE;
                break;
            case "hours":
                f = Calendar.HOUR_OF_DAY;
                break;
            case "minutes":
                f = Calendar.MINUTE;
                break;
            case "seconds":
                f = Calendar.SECOND;
                break;
        }
        int a = Integer.valueOf(amount);
        if(target instanceof Player){
            Ban.Nban(target, f, a, reason);
            player.sendMessage("§f[§dSOTE§f] §a"+data+"をnbanしました");
        }else{
            Ban.Nban(data, f, a, reason);
            player.sendMessage("§f[§dSOTE§f] §a"+data+"をnbanしました");
        }
    }

    Main plugin;
}
