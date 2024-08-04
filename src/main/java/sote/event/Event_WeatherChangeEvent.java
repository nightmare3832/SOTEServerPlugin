package sote.event;

import cn.nukkit.event.Listener;
import cn.nukkit.event.level.WeatherChangeEvent;
import cn.nukkit.plugin.PluginBase;

public class Event_WeatherChangeEvent extends PluginBase implements Listener{

    public Event_WeatherChangeEvent(){
    }

    public static void onWeather(WeatherChangeEvent event){
        //event.setCancelled();
    }
}