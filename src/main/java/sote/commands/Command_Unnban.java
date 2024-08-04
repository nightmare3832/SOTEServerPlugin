package sote.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.permission.Permission;
import sote.Main;
import sote.ban.Ban;
import sote.stat.Stat;

public class Command_Unnban extends Command{

    public Command_Unnban(Main plugin){
        super("unnban");
        this.setPermission(Permission.DEFAULT_OP);
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String label, String args[]){
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(!(Stat.getVip(player) >= 10)) return false;
            if(args.length == 1){
                unnban(player,args[0]);
            }else{
                sender.sendMessage("§f[§dSOTE§f] §cERROR");
            }
        }else{
            if(args.length == 1){
                unnban(sender,args[0]);
            }else{
                sender.sendMessage("§f[§dSOTE§f] §cERROR");
            }
        }
        return true;
    }

    public static void unnban(Player player,String data){
        Ban.UnNban(data);
        player.sendMessage("§f[§dSOTE§f] §a"+data+"をunnbanしました");
    }

    public static void unnban(CommandSender player,String data){
        Ban.UnNban(data);
        player.sendMessage("§f[§dSOTE§f] §a"+data+"をunnbanしました");
    }

    Main plugin;
}
