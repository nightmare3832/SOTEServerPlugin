package sote.murder;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.data.Skin;
import sote.Main;
import sote.stat.Stat;

public class MurderSkinManager{

    public static HashMap<Integer, Skin> skins = new HashMap<Integer, Skin>();
    public static HashMap<Integer, BufferedImage> skinCashs = new HashMap<Integer, BufferedImage>();
    public static int max = 28;

    public MurderSkinManager(){
        String url = Main.file.toString()+"/murderskin/";
        for(int i = 0;i < max;i++){
            skins.put(i, new Skin(new File(url+"skin"+(i+1)+".png")));
            skinCashs.put(i, getImageFromPng(new File(url+"skin"+(i+1)+".png")));
        }
    }

    public static Integer[] getRandomSkinID(){
        Integer[] ids = new Integer[max];
        for(int i = 0;i < max;i++){
            ids[i] = i;
        }
        List<Integer> list = Arrays.asList(ids);
        Collections.shuffle(list);
        Integer[] result =(Integer[])list.toArray(new Integer[list.size()]);
        return result;
    }

    public static Skin getSkinByID(Player player, int id){
        return setHat(player, id);
    }

    public static Skin setHat(Player player, int id){
        String hatString = Stat.getMurderHat(player);
        if(hatString.equals("unknown")) return skins.get(id);
        BufferedImage baseImage = skinCashs.get(id);
        BufferedImage hatImage = getImageFromPng(new File(Main.file.toString()+"/murderskin/hat/"+hatString+".png"));
        if(!(hatImage instanceof BufferedImage)) return new Skin(baseImage);
        if(!(baseImage instanceof BufferedImage)){
            Server.getInstance().getLogger().alert(id+" is unknown");
            return skins.get(id);
        }
        int rgb = 0;
        for(int x = 0;x < 32;x++){
            for(int y = 0;y < 16;y++){
                rgb = hatImage.getRGB(x, y);
                baseImage.setRGB(x + 32, y, rgb);
            }
        }
        return new Skin(baseImage);
    }

    public static BufferedImage getImageFromPng(File file){
        try{
            BufferedImage img = ImageIO.read(file);
            return img;
        }catch(IOException e){
            System.out.println(e);
            return null;
        }
    }

    public static BufferedImage getImageFromBytes(byte[] data){
        /*int width = 64;
        int height = 64;
        DataBuffer buffer = new DataBufferByte(data, width*height);
        WritableRaster raster = Raster.
                createInterleavedRaster(buffer, width, height, width,
                        1,new int[1], new Point());
        ColorModel model = new ComponentColorModel(
                ColorSpace.getInstance(ColorSpace.CS_GRAY), false, false,
                Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        return new BufferedImage(model, raster, false, null);*/
        try{
            return ImageIO.read(new ByteArrayInputStream(data));
        }catch(IOException e){
            System.out.println(e);
            return null;
        }
    }

    public static byte[] getBytesFromImage(BufferedImage img){
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "png", baos);
            return baos.toByteArray();
        }catch(IOException e){
            System.out.println(e);
            return null;
        }
    }
}