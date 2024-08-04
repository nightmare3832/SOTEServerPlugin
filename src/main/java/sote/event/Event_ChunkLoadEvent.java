package sote.event;

import cn.nukkit.event.Listener;
import cn.nukkit.event.level.ChunkLoadEvent;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.plugin.PluginBase;

public class Event_ChunkLoadEvent extends PluginBase implements Listener{

    public Event_ChunkLoadEvent(){
    }

    public static void onChunk(ChunkLoadEvent event){
        FullChunk chunk = event.getChunk();
        Level level = chunk.getProvider().getLevel();
            if(level.getName().equals("skywars") || level.getName().equals("teamskywars") || level.getName().equals("skyyy")){
                for (int Z = 0; Z < 16; ++Z) {
                    for (int X = 0; X < 16; ++X) {
                        chunk.setBiomeColor(X,Z,146,188,89);
                    }
                }
            }
    }
}