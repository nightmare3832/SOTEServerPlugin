package sote.lobbyitem;

import cn.nukkit.Player;
import cn.nukkit.level.Level;

public interface AppearLobbyItem{

    public void select(Player player);

    public void unselect(Player player);

    public void spawnTo(Player player);

    public void despawnTo(Player player);

    public void switchLevel(Player player, Level before, Level after);

}