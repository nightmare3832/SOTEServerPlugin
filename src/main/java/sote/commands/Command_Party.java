package sote.commands;

import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import sote.Main;
import sote.PlayerDataManager;
import sote.party.Party;

public class Command_Party extends Command{

    public Command_Party(Main plugin){
        super("party");
        this.isSpecial = true;
        this.plugin = plugin;
        this.commandParameters.clear();
        this.commandParameters.put("defaultInvite", new CommandParameter[]{
                new CommandParameter("arg1", CommandParameter.ARG_TYPE_RAW_TEXT, false),
                new CommandParameter("player", CommandParameter.ARG_TYPE_TARGET, false)
        });
        this.commandParameters.put("defaultAccept", new CommandParameter[]{
                new CommandParameter("arg1", CommandParameter.ARG_TYPE_RAW_TEXT, false),
                new CommandParameter("player", CommandParameter.ARG_TYPE_STRING_ENUM, false)
        });
        this.commandParameters.put("defaultDeny", new CommandParameter[]{
                new CommandParameter("arg1", CommandParameter.ARG_TYPE_RAW_TEXT, false),
                new CommandParameter("player", CommandParameter.ARG_TYPE_STRING_ENUM, false)
        });
        this.commandParameters.put("defaultList", new CommandParameter[]{
                new CommandParameter("arg1", CommandParameter.ARG_TYPE_RAW_TEXT, false)
        });
        this.commandParameters.put("defaultLeave", new CommandParameter[]{
                new CommandParameter("arg1", CommandParameter.ARG_TYPE_RAW_TEXT, false)
        });
        this.commandParameters.put("defaultDisband", new CommandParameter[]{
                new CommandParameter("arg1", CommandParameter.ARG_TYPE_RAW_TEXT, false)
        });
        this.commandParameters.put("defaultKick", new CommandParameter[]{
                new CommandParameter("arg1", CommandParameter.ARG_TYPE_RAW_TEXT, false),
                new CommandParameter("player", CommandParameter.ARG_TYPE_STRING_ENUM, false)
        });
        this.commandParameters.put("defaultPromote", new CommandParameter[]{
                new CommandParameter("arg1", CommandParameter.ARG_TYPE_RAW_TEXT, false),
                new CommandParameter("player", CommandParameter.ARG_TYPE_STRING_ENUM, false)
        });
        this.commandParameters.put("defaultHelp", new CommandParameter[]{
                new CommandParameter("arg1", CommandParameter.ARG_TYPE_RAW_TEXT, false)
        });
    }

    public boolean execute(CommandSender sender, String label, String args[]){
        String o = "";
        for(String a : args){
            o += " "+a;
        }
        System.out.println(label+" "+o);
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(args.length >= 1){
                switch(args[0]){
                    case "invite":
                        if(args.length >= 2) Party.invite(player,args[1]);
                        else player.sendMessage(Main.getMessage(player,"party.please.username"));
                    break;
                    case "accept":
                        if(args.length >= 2) Party.accept(player,args[1]);
                        else player.sendMessage(Main.getMessage(player,"party.please.username"));
                    break;
                    case "deny":
                        if(args.length >= 2) Party.deny(player,args[1]);
                        else player.sendMessage(Main.getMessage(player,"party.please.username"));
                    break;
                    case "list":
                        Party.list(player);
                    break;
                    case "leave":
                        Party.leave(player);
                    break;
                    case "disband":
                        Party.disband(player);
                    break;
                    case "kick":
                        if(args.length >= 2) Party.kick(player,args[1]);
                        else player.sendMessage(Main.getMessage(player,"party.please.username"));
                    break;
                    case "promote":
                        if(args.length >= 2) Party.promote(player,args[1]);
                        else player.sendMessage(Main.getMessage(player,"party.please.username"));
                    break;
                    case "help":
                    default:
                        player.sendMessage(Main.getMessage(player,"party.command.help"));
                    break;
                }
            }else{
                player.sendMessage(Main.getMessage(player,"party.command.help"));
            }
        }else{
            sender.sendMessage("§f[§dSOTE§f] §cこのコマンドはゲーム内で実行してください");
        }
        return true;
    }

    @Override
    public String getCommandDataString(Player player){
        String owners = "";
        String members = "";
        if(Party.partyData.containsKey(player.getName().toLowerCase())){
            Map<String, String> mapp = Party.partyData.get(player.getName().toLowerCase());
            String[] invites = mapp.get("invite").split(",");
            boolean isFirst = true;
            for(String party : invites){
                if(party.equals("")) continue;
                if(isFirst) isFirst = false;
                else owners += ",";
                owners += "\""+PlayerDataManager.getPlayerData(party).getName()+"\"";
            }
            if(mapp.get("owner").equals(player.getName().toLowerCase())){
                String[] member = mapp.get("member").split(",");
                isFirst = true;
                for(String party :member){
                    if(!party.equals(player.getName().toLowerCase())){
                        if(isFirst) isFirst = false;
                        else members += ",";
                        members += "\""+PlayerDataManager.getPlayerData(party).getName()+"\"";
                    }
                }
            }
        }
        String invite = "\"party invite\":{\"versions\":[{\"description\":\"\",\"overloads\":{\"defaultInvite\":{\"input\":{\"parameters\":[{\"name\":\"player\",\"type\":\"target\"}]},\"output\":{\"format_strings\":[]}}},\"permission\":\"any\"}]}";
        String accept = "\"party accept\":{\"versions\":[{\"description\":\"\",\"overloads\":{\"defaultAccept\":{\"input\":{\"parameters\":[{\"name\":\"player\",\"type\":\"stringenum\",\"enum_values\":["+owners+"]}]},\"output\":{\"format_strings\":[]}}},\"permission\":\"any\"}]}";
        String deny = "\"party deny\":{\"versions\":[{\"description\":\"\",\"overloads\":{\"defaultDeny\":{\"input\":{\"parameters\":[{\"name\":\"player\",\"type\":\"stringenum\",\"enum_values\":["+owners+"]}]},\"output\":{\"format_strings\":[]}}},\"permission\":\"any\"}]}";
        String list = "\"party list\":{\"versions\":[{\"description\":\"\",\"overloads\":{\"defaultList\":{\"input\":{\"parameters\":[]},\"output\":{\"format_strings\":[]}}},\"permission\":\"any\"}]}";
        String leave = "\"party leave\":{\"versions\":[{\"description\":\"\",\"overloads\":{\"defaultLeave\":{\"input\":{\"parameters\":[]},\"output\":{\"format_strings\":[]}}},\"permission\":\"any\"}]}";
        String disband = "\"party disband\":{\"versions\":[{\"description\":\"\",\"overloads\":{\"defaultDisband\":{\"input\":{\"parameters\":[]},\"output\":{\"format_strings\":[]}}},\"permission\":\"any\"}]}";
        String kick = "\"party kick\":{\"versions\":[{\"description\":\"\",\"overloads\":{\"defaultKick\":{\"input\":{\"parameters\":[{\"name\":\"player\",\"type\":\"stringenum\",\"enum_values\":["+members+"]}]},\"output\":{\"format_strings\":[]}}},\"permission\":\"any\"}]}";
        String promote = "\"party promote\":{\"versions\":[{\"description\":\"\",\"overloads\":{\"defaultPromote\":{\"input\":{\"parameters\":[{\"name\":\"player\",\"type\":\"stringenum\",\"enum_values\":["+members+"]}]},\"output\":{\"format_strings\":[]}}},\"permission\":\"any\"}]}";
        String help = "\"party help\":{\"versions\":[{\"description\":\"\",\"overloads\":{\"defaultHelp\":{\"input\":{\"parameters\":[]},\"output\":{\"format_strings\":[]}}},\"permission\":\"any\"}]}";
        if(Party.partyData.containsKey(player.getName().toLowerCase())){
            Map<String, String> mapp = Party.partyData.get(player.getName().toLowerCase());
            if(mapp.get("owner").equals(player.getName().toLowerCase())){
                return ","+invite+","+list+","+leave+","+disband+","+promote+","+kick+","+help;
            }else if(!mapp.get("owner").equals("")){
                return ","+list+","+leave+","+help;
            }
        }
        return ","+invite+","+accept+","+deny+","+help;
    }

    Main plugin;
}
