package sote.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import cn.nukkit.permission.Permission;
import sote.Main;

public class Command_Sound extends Command{

    public Command_Sound(Main plugin){
        super("sound");
        this.setPermission(Permission.DEFAULT_OP);
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String label, String args[]){
        if(sender instanceof Player){
            Player player = (Player)sender;
            LevelSoundEventPacket pk = new LevelSoundEventPacket();
            //pk.type = Byte.valueOf(args[0]);
            pk.x = (float)player.x;
            pk.y = (float)player.y;
            pk.z = (float)player.z;
            player.dataPacket(pk);
        }else{
            sender.sendMessage("§f[§dSOTE§f] §cこのコマンドはゲーム内で実行してください");
        }
        return true;
    }

    Main plugin;
}
