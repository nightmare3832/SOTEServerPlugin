package sote.commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Level;
import cn.nukkit.permission.Permission;
import sote.Main;

public class Command_World extends Command{

    public Command_World(Main plugin){
        super("world");
        this.setPermission(Permission.DEFAULT_OP);
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String label, String args[]){
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(!Main.getBlock(player)) return false;
            if(args.length == 1){
                world(player,args[0]);
            }else{
                sender.sendMessage("§f[§dSYSTEM§f] §cERROR");
            }
        }else{
            sender.sendMessage("§f[§dSYSTEM§f] §cこのコマンドはゲーム内で実行してください");
        }
        return true;
    }

    public static void world(Player player,String data){
        Server.getInstance().loadLevel(data);
        Level level = Server.getInstance().getLevelByName(data);
        if(!(level instanceof Level)){
            player.sendMessage("§f[§dSYSTEM§f] §cERROR");
            return;
        }
        player.teleport(level.getSafeSpawn());
        player.sendMessage("§f[§dSYSTEM§f] §a移動しました");
    }

    Main plugin;
}
