package sote.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.permission.Permission;
import cn.nukkit.scheduler.Task;
import sote.Main;
import sote.MySQL;
import sote.ban.Ban;
import sote.home.Home;

public class Command_Yaw extends Command{

    public Command_Yaw(Main plugin){
        super("yaw");
        this.setPermission(Permission.DEFAULT_OP);
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String label, String args[]){
        if(sender instanceof Player){
            Player player = (Player)sender;
            sender.sendMessage(player.yaw+":");
            sender.sendMessage(MySQL.getNextId()+"");
            System.out.println(Home.homeData.get(player.getName().toLowerCase()).toString());
        }else{
            sender.sendMessage("§f[§dSOTE§f] §cこのコマンドはゲーム内で実行してください");
            if(args.length == 1) Ban.clearAll();
        }
        return true;
    }

    public static void callback(Player player, DataPacket pk){
        player.dataPacket(pk);
    }

    Main plugin;
}
class CallbackYaw extends Task{

    public Player player;
    public DataPacket pk;

    public CallbackYaw(Player player, DataPacket pk){
        this.player = player;
        this.pk = pk;
    }

    public void onRun(int d){
        Command_Yaw.callback(this.player, this.pk);
    }
}
