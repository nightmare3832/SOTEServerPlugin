package sote;

import java.util.HashMap;

import cn.nukkit.Player;
import sote.bedwars.Bedwars;
import sote.blockhunt.Blockhunt;
import sote.buildbattle.Buildbattle;
import sote.miniwalls.Miniwalls;
import sote.murder.Murder;
import sote.skywarssolo.SkywarsSolo;

public class GameProvider{

    public static final String MURDER = "MD";
    public static final String SKYWARS_SOLO = "SW Solo";
    public static final String SKYWARS_TEAM = "SW Team";
    public static final String MINIWALLS = "MW";
    public static final String BUILDBATTLE = "BB";
    public static final String BLOCKHUNT = "BH";
    public static final String BEDWARS = "BW";
    public static final String EGGWARS_SOLO = "EW Solo";
    public static final String EGGWARS_TEAM = "EW Team";

    public GameProvider(){
        registerGame();
    }

    public static void registerGame(){
        Game.put(MURDER, Murder.class);
        Game.put(SKYWARS_SOLO, SkywarsSolo.class);
        Game.put(MINIWALLS, Miniwalls.class);
        Game.put(BUILDBATTLE, Buildbattle.class);
        Game.put(BLOCKHUNT, Blockhunt.class);
        Game.put(BEDWARS, Bedwars.class);
    }

    public static void register(int number, boolean isHome){
        HashMap<String, Game> games = new HashMap<String, Game>();
        Games.put(number, games);
        games.put(MURDER, new Murder(number, isHome));
        games.put(SKYWARS_SOLO, new SkywarsSolo(number, isHome));
        games.put(MINIWALLS, new Miniwalls(number, isHome));
        games.put(BUILDBATTLE, new Buildbattle(number, isHome));
        games.put(BLOCKHUNT, new Blockhunt(number, isHome));
        games.put(BEDWARS, new Bedwars(number, isHome));
    }

    public static void joinGame(Player player, Game game){
        PlayerGames.put(player,  game);
    }

    public static void quitGame(Player player){
        PlayerGames.put(player, null);
    }

    public static Game getPlayingGame(Player player){
        if(!PlayerGames.containsKey(player)) return null;
        return PlayerGames.get(player);
    }

    public static HashMap<String, Class<? extends Game>> Game = new HashMap<String, Class<? extends Game>>();
    public static HashMap<Integer, HashMap<String, Game>> Games = new HashMap<Integer, HashMap<String, Game>>();
    public static HashMap<Player, Game> PlayerGames = new HashMap<Player, Game>();
}