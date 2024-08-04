package sote.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import sote.Main;
import sote.login.LoginSystem;
public class Command_Register extends Command{

    public Command_Register(Main plugin){
        super("register");
        this.plugin = plugin;
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("passward", CommandParameter.ARG_TYPE_RAW_TEXT, false)
        });
    }

    public boolean execute(CommandSender sender, String label, String args[]){
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(args.length >= 1){
                register(player,args[0]);
            }else{
                sender.sendMessage("§f[§dLOGIN§f] §cパスワードを入力してください");
            }
        }else{
            sender.sendMessage("§f[§dLOGIN§f] §cこのコマンドはゲーム内で実行してください");
        }
        return true;
    }

    public static void register(Player player,String data){
        LoginSystem.register(player,data);
    }

    Main plugin;
}
