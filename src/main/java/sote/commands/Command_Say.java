package sote.commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.permission.Permission;
import sote.Main;

public class Command_Say extends Command{

    public Command_Say(Main plugin){
        super("say");
        this.setPermission(Permission.DEFAULT_OP);
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String label, String args[]){
        if(sender instanceof Player){
            Player player = (Player)sender;
        }else{
            if(args.length >= 1){
                String msg = "";
                for(String arg : args){
                    msg += arg + " ";
                }
                if (msg.length() > 0) {
                    msg = msg.substring(0, msg.length() - 1);
                }
                Server.getInstance().broadcastMessage("§d[Server] "+args[0]);
            }
        }
        return true;
    }

    Main plugin;
}
