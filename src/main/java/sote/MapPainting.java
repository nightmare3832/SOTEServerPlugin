package sote;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.BlockEntityDataPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.scheduler.Task;

public class MapPainting{

    public MapPainting(){
        register();
    }

    public static void addPlayer(Player player){
        HashMap<String, Boolean> data = new HashMap<String, Boolean>();
        data.put("MurderPainting",  false);
        isSend.put(player,  data);
    }

    public static void sendMurderPainting(Player player){
        int count = 10;
        for(Map.Entry<Vector3, DataPacket[]> e : MapPaintings.get("MurderPainting").entrySet()){
            Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackMap(player, e.getValue()[0]), count*2);
            if(!isSend.get(player).get("MurderPainting")){
                Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackMap(player, e.getValue()[1]), count*2+1);
            }
            count++;
        }
        isSend.get(player).put("MurderPainting", true);
    }

    public static void sendPacket(Player player, DataPacket pk){
        player.dataPacket(pk);
    }

    public static void register(){
        Vector3 base = new Vector3(-252,7,-259);
        LinkedHashMap<Vector3,DataPacket[]> murderPaintings = new LinkedHashMap<Vector3,DataPacket[]>();
        int sizeX = 7;
        int sizeY = 3;
        int Px = 0;
        int Py = 0;
        for(int i = 1;i <= sizeX * sizeY;i++){
            Long eid = Entity.entityCount++;
            MapInfo mapInfo = new MapInfo();
            mapInfo.MapId = eid;
            mapInfo.Decorators = new Decorator[]{};
            mapInfo.Col = 128;
            mapInfo.Row = 128;
            mapInfo.Scale = 0;
            mapInfo.XOffset = 0;
            mapInfo.ZOffset = 0;
            mapInfo.Data = getByteImage(new File(Main.file.toString()+"/MurderPainting.png"), Px*128, Py*128, 128, 128);
            Item item = Item.get(358,0,1);
            CompoundTag tag;
            if (!item.hasCompoundTag()) {
                tag = new CompoundTag();
            } else {
                tag = item.getNamedTag();
            }
            tag.putString("map_uuid",eid.toString());
            item.setNamedTag(tag);
            CompoundTag nbt = new CompoundTag("")
                    .putList(new ListTag<>("Items"))
                    .putString("id", BlockEntity.ITEM_FRAME)
                    .putInt("x", (int)(base.x + Px))
                    .putInt("y", (int)(base.y - Py))
                    .putInt("z", (int)(base.z))
                    .putCompound("Item", NBTIO.putItemHelper(item))
                    .putByte("ItemRotation", 0);
            BlockEntityDataPacket pk = new BlockEntityDataPacket();
            pk.x = (int)(base.x + Px);
            pk.y = (int)(base.y - Py);
            pk.z = (int)(base.z);
            try {
                pk.namedTag = NBTIO.write(nbt, ByteOrder.LITTLE_ENDIAN, true);
            } catch (IOException ee) {
                throw new RuntimeException(ee);
            }
            ClientboundMapItemDataPacket pk2 = new ClientboundMapItemDataPacket();
            pk2.updateType = ClientboundMapItemDataPacket.BITFLAG_TEXTURE_UPDATE;
            pk2.mapInfo = mapInfo;
            murderPaintings.put(new Vector3(base.x + Px, base.y - Py, base.z), new DataPacket[]{pk, pk2});
            Px++;
            if(Px >= sizeX){
                Px = 0;
                Py++;
            }
        }
        MapPaintings.put("MurderPainting", murderPaintings);
    }

    public static int[] getByteImage(File file, int x, int y, int w, int h) {
        BufferedImage image;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return parseBufferedImage(image, x, y, w, h);
    }

    public static int[] parseBufferedImage(BufferedImage image, int x, int y, int w, int h) {
        image = image.getSubimage(x, y, w, h);
        int[] result = new int[image.getHeight()*image.getWidth()*4];
        int count = 0;
        for (int yy = 0; yy < image.getHeight(); yy++) {
            for (int xx = 0; xx < image.getWidth(); xx++) {
                Color color = new Color(image.getRGB(xx, yy), true);
                result[count++] = color.getRed();
                result[count++] = color.getGreen();
                result[count++] = color.getBlue();
                result[count++] = 0xff;//color.getAlpha();
            }
        }
        image.flush();
        return result;
    }

    public static HashMap<String, HashMap<Vector3, DataPacket[]>> MapPaintings = new HashMap<String, HashMap<Vector3, DataPacket[]>>();
    public static HashMap<Player, HashMap<String, Boolean>> isSend = new HashMap<Player, HashMap<String, Boolean>>();
}
class CallbackMap extends Task{

    public Player player;
    public DataPacket pk;

    public CallbackMap(Player player, DataPacket pk){
        this.player = player;
        this.pk = pk;
    }

    public void onRun(int d){
        MapPainting.sendPacket(player, pk);
    }
}