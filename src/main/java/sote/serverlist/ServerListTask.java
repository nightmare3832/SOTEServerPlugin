package sote.serverlist;

import cn.nukkit.scheduler.Task;

public class ServerListTask extends Task {
    private int time = 0;

    @Override
    public void onRun(int currentTick) {
        time++;
        if(time >= 60){
            ServerListAPI.updateTime();
            time = 0;
        }
    }
}
