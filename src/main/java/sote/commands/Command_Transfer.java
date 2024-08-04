package sote.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.network.protocol.TransferPacket;
import cn.nukkit.permission.Permission;
import sote.Main;

public class Command_Transfer extends Command{

    public Command_Transfer(Main plugin){
        super("transfer");
        this.setPermission(Permission.DEFAULT_OP);
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String label, String args[]){
        if(sender instanceof Player){
            Player player = (Player)sender;
            TransferPacket pk = new TransferPacket();
            pk.address = "soteserver.net";
            pk.port = 19132;
            player.dataPacket(pk);
            player.sendMessage("o");
        }else{
            sender.sendMessage("§f[§dSOTE§f] §cサーバー内で実行してください");
        }
        return true;
    }

    Main plugin;
}
