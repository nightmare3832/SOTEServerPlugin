package sote.commands;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.permission.Permission;
import sote.Main;

public class Command_Skin extends Command{

    public Command_Skin(Main plugin){
        super("skin");
        this.setPermission(Permission.DEFAULT_OP);
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String label, String args[]){
        if(sender instanceof Player){
            Player player = (Player)sender;
            String name = player.getName();
            Skin skin = player.getSkin();
            try{
                BufferedImage image = getImageFromBytes(skin.getData());
                ImageIO.write(image,"PNG", new File(Main.file.toString()+"/skin/"+name+".png"));
            }catch(IOException e){
                
            }
        }
        return true;
    }

    public static BufferedImage getImageFromBytes(byte[] bytes) throws IOException{
        ByteArrayInputStream baos = new ByteArrayInputStream(bytes);
        BufferedImage img = ImageIO.read(baos);
        return img;
    }

    Main plugin;
}