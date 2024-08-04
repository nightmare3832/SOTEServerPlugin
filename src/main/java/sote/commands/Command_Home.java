package sote.commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import sote.Main;
import sote.home.Home;
import sote.inventory.Inventorys;
import sote.inventory.lobby.StartInventory;
import sote.stat.Stat;

public class Command_Home extends Command{

    public Command_Home(Main plugin){
        super("home");
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String label, String args[]){
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(args.length >= 1){
                switch(args[0]){
                    case "add":
                        if(Stat.getVip(player) < 4){
                            player.sendMessage(Main.getMessage(player,"command.not.legend"));
                            return false;
                        }
                        if(args.length >= 2) Home.addHome(player,args[1]);
                        else player.sendMessage(Main.getMessage(player,"home.please.id"));
                    break;
                    case "remove":
                        if(Stat.getVip(player) < 4){
                            player.sendMessage(Main.getMessage(player,"command.not.legend"));
                            return false;
                        }
                        Home.removeHome(player);
                    break;
                    case "join":
                        if(Main.gamenow.get(player)) return false;
                        if(args.length >= 2) Home.go(player,args[1]);
                        else player.sendMessage(Main.getMessage(player,"home.please.id"));
                    break;
                    case "quit":
                        if(player.getLevel().getFolderName().split("home").length >= 2){
                            Inventorys.setData(player, new StartInventory());
                            player.teleport(Server.getInstance().getLevelByName("lobby").getSafeSpawn());
                        }
                    break;
                    case "help":
                        player.sendMessage(Main.getMessage(player,"home.command.help"));
                    break;
                }
            }else{
                player.sendMessage(Main.getMessage(player,"home.command.help"));
            }
        }else{
            sender.sendMessage("§f[§dSOTE§f] §cこのコマンドはゲーム内で実行してください");
        }
        return true;
    }

    Main plugin;
}
