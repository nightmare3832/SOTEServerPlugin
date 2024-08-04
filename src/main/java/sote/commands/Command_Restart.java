package sote.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.permission.Permission;
import sote.Main;

public class Command_Restart extends Command{

    public Command_Restart(Main plugin){
        super("restart");
        this.setPermission(Permission.DEFAULT_OP);
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String label, String args[]){
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(Main.getBlock(player))Main.count = 295;
            sender.sendMessage("§f[§dSOTE§f] §a再起動までの時間を短縮しました");
        }else{
            Main.count = 295;
            sender.sendMessage("§f[§dSOTE§f] §a再起動までの時間を短縮しました");
        }
        return true;
    }

    Main plugin;
}