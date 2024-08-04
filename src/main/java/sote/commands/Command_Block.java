package sote.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import sote.Main;

public class Command_Block extends Command{

    public Command_Block(Main plugin){
        super("block");
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String label, String args[]){
        if(sender instanceof Player){
            Player player = (Player)sender;
                sender.sendMessage("§f[§dSYSTEM§f] §cこの機能は準備中です");
        }else{
            sender.sendMessage("§f[§dSYSTEM§f] §cこのコマンドはゲーム内で実行してください");
        }
        return true;
    }

    Main plugin;
}