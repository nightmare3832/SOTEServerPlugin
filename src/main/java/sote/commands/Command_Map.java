package sote.commands;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.network.protocol.MovePlayerPacket;
import cn.nukkit.network.protocol.PlayerListPacket;
import cn.nukkit.permission.Permission;
import cn.nukkit.scheduler.Task;
import sote.Main;

public class Command_Map extends Command{

    public Command_Map(Main plugin){
        super("map");
        this.setPermission(Permission.DEFAULT_OP);
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String label, String args[]){
        if(sender instanceof Player){
            Player player = (Player)sender;
            LevelEventPacket pk = new LevelEventPacket();
            pk.evid = (short) (LevelEventPacket.EVENT_ADD_PARTICLE_MASK | Integer.parseInt(args[0]));
            pk.x = (float) player.x;
            pk.y = (float) player.y + 2;
            pk.z = (float) player.z;
            pk.data = ((255 & 0xff) << 24) | ((255 & 0xff) << 16) | ((255 & 0xff) << 8) | (255 & 0xff);
            player.dataPacket(pk);
        }else{
            sender.sendMessage("§f[§dSOTE§f] §cこのコマンドはゲーム内で実行してください");
        }
        return true;
    }

    public long toInt32_2(int[] bytes){
        return (long)((long)(0xff & bytes[0]) | (long)(0xff & bytes[1]) << 8 | (long)(0xff & bytes[2]) << 16 | (long)(0xff & bytes[3]) << 24) & 0xFFFFFFFFL;
    }

    public static int[] getByteImage(File file) {
        BufferedImage image;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return parseBufferedImage(image);
    }

    public static int[] parseBufferedImage(BufferedImage image) {
        int[] result = new int[image.getHeight()*image.getWidth()*4];
        int count = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = new Color(image.getRGB(x, y), true);
                result[count++] = color.getRed();
                result[count++] = color.getGreen();
                result[count++] = color.getBlue();
                result[count++] = color.getAlpha();
            }
        }
        image.flush();
        return result;
    }

    public static void HumanMove(DataPacket pk,Player player){
        player.dataPacket(pk);
    }

    Main plugin;
    public static PlayerListPacket pp = new PlayerListPacket();
    public static MovePlayerPacket mp = new MovePlayerPacket();
}

class CallbackHuman extends Task{

    public DataPacket pk;
    public Player player;

    public CallbackHuman(DataPacket pk,Player player){
        this.pk = pk;
        this.player = player;
    }

    public void onRun(int d){
        Command_Map.HumanMove(pk,player);
    }
}