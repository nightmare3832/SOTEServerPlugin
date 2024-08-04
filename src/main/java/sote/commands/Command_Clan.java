package sote.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import sote.Main;
import sote.clan.Clan;;

public class Command_Clan extends Command{

    public Command_Clan(Main plugin){
        super("clan");
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String label, String args[]){
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(args.length >= 1){
                switch(args[0]){
                    case "invite":
                        if(args.length >= 2) Clan.invite(player,args[1]);
                        else player.sendMessage(Main.getMessage(player,"clan.please.username"));
                    break;
                    case "accept":
                        if(args.length >= 2) Clan.accept(player,args[1]);
                        else player.sendMessage(Main.getMessage(player,"clan.please.username"));
                    break;
                    case "deny":
                        if(args.length >= 2) Clan.deny(player,args[1]);
                        else player.sendMessage(Main.getMessage(player,"clan.please.username"));
                    break;
                    case "list":
                        Clan.list(player);
                    break;
                    case "leave":
                        Clan.leave(player);
                    break;
                    case "disband":
                        Clan.disband(player);
                    break;
                    case "kick":
                        if(args.length >= 2) Clan.kick(player,args[1]);
                        else player.sendMessage(Main.getMessage(player,"clan.please.username"));
                    break;
                    case "promote":
                        if(args.length >= 2) Clan.promote(player,args[1]);
                        else player.sendMessage(Main.getMessage(player,"clan.please.username"));
                    break;
                    case "help":
                    default:
                        player.sendMessage(Main.getMessage(player,"clan.command.help"));
                    break;
                }
            }else{
                player.sendMessage(Main.getMessage(player,"clan.command.help"));
            }
        }else{
            sender.sendMessage("§f[§dSOTE§f] §cこのコマンドはゲーム内で実行してください");
        }
        return true;
    }

    Main plugin;
}
