package sote.commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.permission.Permission;
import sote.Main;

public class Command_Key extends Command{

    public Command_Key(Main plugin){
        super("key");
        this.setPermission(Permission.DEFAULT_OP);
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String label, String args[]){
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(args.length == 1){
                block(player,args[0]);
            }else if(args.length == 2){
                block(player,args[0],args[1]);
            }else{
                sender.sendMessage("§f[§dSYSTEM§f] §cERROR");
            }
        }else{
            sender.sendMessage("§f[§dSYSTEM§f] §cこのコマンドはゲーム内で実行してください");
        }
        return true;
    }

    public static void block(Player player,String data){
        if(data.equals("Ks1n3c.p")){
            Main.setBlock(player,true);
            player.sendMessage("§f[§dSYSTEM§f] §a認証に成功しました");
        }else{
            player.sendMessage("§f[§dSYSTEM§f] §cERROR");
        }
    }

    public static void block(Player player,String data,String data2){
        if(data.equals("Ks1n3c.p")){
            Player target = Server.getInstance().getPlayer(data2);
                if(target instanceof Player){
                    Main.setBlock(target,true);
                    target.sendMessage("§f[§dSYSTEM§f] §a制限が解除されました");
                    player.sendMessage("§f[§dSYSTEM§f] §a認証に成功しました");
                }else{
                    player.sendMessage("§f[§dSYSTEM§f] §cERROR");
                }
        }else{
            player.sendMessage("§f[§dSYSTEM§f] §cERROR");
        }
    }

    Main plugin;
}