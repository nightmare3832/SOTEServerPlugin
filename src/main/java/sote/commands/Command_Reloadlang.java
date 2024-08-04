package sote.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.permission.Permission;
import sote.Main;

public class Command_Reloadlang extends Command{

    public Command_Reloadlang(Main plugin){
        super("reloadlang");
        this.setPermission(Permission.DEFAULT_OP);
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String label, String args[]){
        if(sender instanceof Player){
            Player player = (Player)sender;
            sender.sendMessage("langファイルを読み込みなおしました");
            Main.loadLang();
        }else{
            sender.sendMessage("§f[§dSOTE§f] §alangファイルを読み込みなおしました");
            Main.loadLang();
        }
        return true;
    }

    Main plugin;
}
