package sote.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import sote.Main;
import sote.login.LoginSystem;
public class Command_Login extends Command{

    public Command_Login(Main plugin){
        super("login");
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String label, String args[]){
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(args.length >= 1){
                login(player,args[0]);
            }else{
                login(player,"");
                sender.sendMessage("§f[§dLOGIN§f] §cパスワードを入力してください");
            }
        }else{
            sender.sendMessage("§f[§dLOGIN§f] §cこのコマンドはゲーム内で実行してください");
        }
        return true;
    }

    public static void login(Player player,String data){
        LoginSystem.login(player,data);
    }

    Main plugin;
}
