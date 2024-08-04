package sote;

import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.ProtocolInfo;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ClientboundMapItemDataPacket extends DataPacket {
    public static final byte NETWORK_ID = ProtocolInfo.CLIENTBOUND_MAP_ITEM_DATA_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public MapInfo mapInfo;
    public byte updateType;

    public static final byte BITFLAG_TEXTURE_UPDATE = 0x02;
    public static final byte BITFLAG_DECORATION_UPDATE = 0x04;
    public static final byte BITFLAG_ENTITY_UPDATE = 0x08;

    @Override
    public void decode() {

    }

    @Override
    public void encode(){
        this.reset();
 
        this.putVarLong(mapInfo.MapId);
        this.putByte(updateType);
        if((updateType & BITFLAG_TEXTURE_UPDATE) == BITFLAG_TEXTURE_UPDATE || (updateType & BITFLAG_DECORATION_UPDATE) == BITFLAG_DECORATION_UPDATE){
            this.putByte(mapInfo.Scale);
        }
        if((updateType & BITFLAG_DECORATION_UPDATE) == BITFLAG_DECORATION_UPDATE){
            this.putDecorators();
        }
        if((updateType & BITFLAG_TEXTURE_UPDATE) == BITFLAG_TEXTURE_UPDATE){
            this.putVarInt(mapInfo.Col);
            this.putVarInt(mapInfo.Row);
            this.putVarInt(mapInfo.XOffset);
            this.putVarInt(mapInfo.ZOffset);
            int i = 0;
            for (int y = 0; y < mapInfo.Col; y++){
                for (int x = 0; x < mapInfo.Row; x++){
                    int r = mapInfo.Data[i++];
                    int g = mapInfo.Data[i++];
                    int b = mapInfo.Data[i++];
                    int a = mapInfo.Data[i++];
                    long color = toInt32_2(new int[]{r,g,b,0xff});
                    this.putUnsignedVarInt(color);
                }
            }
        }
    }

    public long toInt32_2(int[] bytes){
        return (long)((long)(0xff & bytes[0]) | (long)(0xff & bytes[1]) << 8 | (long)(0xff & bytes[2]) << 16 | (long)(0xff & bytes[3]) << 24) & 0xFFFFFFFFL;
    }

    public void putDecorators(){
        this.putVarInt(mapInfo.Decorators.length);
        for(Decorator decorator : mapInfo.Decorators){
            this.putVarInt((decorator.rotation & 0x0f) | (decorator.icon << 4));
            this.putByte(decorator.x);
            this.putByte(decorator.z);
            this.putString(decorator.label);
            this.putUnsignedVarInt(decorator.color);

        }

    }
}
