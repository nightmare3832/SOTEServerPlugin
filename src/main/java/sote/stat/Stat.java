package sote.stat;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import sote.Main;
import sote.PlayerClone;
import sote.clan.Clan;
import sote.murder.achievements.MurderAchievements;

public class Stat{

    public Stat(){
    }

    public static void addGeneralPlayer(Player player){
        HashMap<String, String> v1;
        if(stat.containsKey(player.getName().toLowerCase())) v1 = (HashMap<String,String>)stat.get(player.getName().toLowerCase());
        else v1 = new HashMap<String, String>();
        v1.put("warn","0");
        v1.put("vip","0");
        v1.put("youtube","0");
        v1.put("coin","0");
        v1.put("medal","0");
        v1.put("level","1");
        v1.put("exp","0");
        stat.put(player.getName().toLowerCase(), v1);
    }

    public static void addMurderPlayer(Player player){
        HashMap<String, String> v1;
        if(stat.containsKey(player.getName().toLowerCase())) v1 = (HashMap<String,String>)stat.get(player.getName().toLowerCase());
        else v1 = new HashMap<String, String>();
        v1.put("murder_karma","100");
        v1.put("murder_misskills","0");
        v1.put("murder_innocentsshot","0");
        v1.put("murder_emeraldcollected","0");
        v1.put("murder_weapontraded","0");
        v1.put("murder_plays","0");
        v1.put("murder_hat","unknown");
        stat.put(player.getName().toLowerCase(), v1);
    }

    public static void addMurderMurderPlayer(Player player){
        HashMap<String, String> v1;
        if(stat.containsKey(player.getName().toLowerCase())) v1 = (HashMap<String,String>)stat.get(player.getName().toLowerCase());
        else v1 = new HashMap<String, String>();
        v1.put("murder_murder_win","0");
        v1.put("murder_murder_lose","0");
        v1.put("murder_murder_kills","0");
        v1.put("murder_murder_deaths","0");
        stat.put(player.getName().toLowerCase(), v1);
    }

    public static void addMurderBystanderPlayer(Player player){
        HashMap<String, String> v1;
        if(stat.containsKey(player.getName().toLowerCase())) v1 = (HashMap<String,String>)stat.get(player.getName().toLowerCase());
        else v1 = new HashMap<String, String>();
        v1.put("murder_bystander_win","0");
        v1.put("murder_bystander_lose","0");
        v1.put("murder_bystander_kills","0");
        v1.put("murder_bystander_deaths","0");
        stat.put(player.getName().toLowerCase(), v1);
    }

    public static void addSkywarsPlayer(Player player){
        HashMap<String, String> v1;
        if(stat.containsKey(player.getName().toLowerCase())) v1 = (HashMap<String,String>)stat.get(player.getName().toLowerCase());
        else v1 = new HashMap<String, String>();
        v1.put("skywars_kills","0");
        v1.put("skywars_deaths","0");
        v1.put("skywars_win","0");
        v1.put("skywars_lose","0");
        v1.put("skywars_plays","0");
        stat.put(player.getName().toLowerCase(), v1);
    }

    public static void addMiniwallsPlayer(Player player){
        HashMap<String, String> v1;
        if(stat.containsKey(player.getName().toLowerCase())) v1 = (HashMap<String,String>)stat.get(player.getName().toLowerCase());
        else v1 = new HashMap<String, String>();
        v1.put("miniwalls_kills","0");
        v1.put("miniwalls_deaths","0");
        v1.put("miniwalls_win","0");
        v1.put("miniwalls_lose","0");
        v1.put("miniwalls_soldierlevel","0");
        v1.put("miniwalls_archerlevel","0");
        v1.put("miniwalls_builderlevel","0");
        v1.put("miniwalls_plays","0");
        stat.put(player.getName().toLowerCase(), v1);
    }

    public static void addBuildbattlePlayer(Player player){
        HashMap<String, String> v1;
        if(stat.containsKey(player.getName().toLowerCase())) v1 = (HashMap<String,String>)stat.get(player.getName().toLowerCase());
        else v1 = new HashMap<String, String>();
        v1.put("buildbattle_win","0");
        v1.put("buildbattle_lose","0");
        v1.put("buildbattle_plays","0");
        stat.put(player.getName().toLowerCase(), v1);
    }

    public static int getWarn(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("warn"));
    }

    public static void setWarn(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("warn",String.valueOf(s));
    }

    public static int getVip(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("vip"));
    }

    public static void setVip(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("vip",String.valueOf(s));
        /*if(s != 10){
            if(s >= 1){
                Map<String, Boolean> map = (Map<String, Boolean>) Kits.kitData.get(player.getName().toLowerCase());
                ((Map<String, Boolean>) Kits.kitData.get(player.getName().toLowerCase())).put("novice",true);
                ((Map<String, Boolean>) Kits.kitData.get(player.getName().toLowerCase())).put("builder",true);
                ((Map<String, Boolean>) Kits.kitData.get(player.getName().toLowerCase())).put("healer",true);
                ((Map<String, Boolean>) Kits.kitData.get(player.getName().toLowerCase())).put("novice",true);
            }
            if(s >= 2){
                Map<String, Boolean> map = (Map<String, Boolean>) Kits.kitData.get(player.getName().toLowerCase());
                ((Map<String, Boolean>) Kits.kitData.get(player.getName().toLowerCase())).put("knight",true);
                ((Map<String, Boolean>) Kits.kitData.get(player.getName().toLowerCase())).put("snowboy",true);
                ((Map<String, Boolean>) Kits.kitData.get(player.getName().toLowerCase())).put("undead",true);
            }
            if(s >= 3){
                Map<String, Boolean> map = (Map<String, Boolean>) Kits.kitData.get(player.getName().toLowerCase());
                ((Map<String, Boolean>) Kits.kitData.get(player.getName().toLowerCase())).put("applemaster",true);
                ((Map<String, Boolean>) Kits.kitData.get(player.getName().toLowerCase())).put("bommper",true);
                ((Map<String, Boolean>) Kits.kitData.get(player.getName().toLowerCase())).put("clown",true);
                ((Map<String, Boolean>) Kits.kitData.get(player.getName().toLowerCase())).put("hunter",true);
            }
            if(s >= 4){
                Map<String, Boolean> map = (Map<String, Boolean>) Kits.kitData.get(player.getName().toLowerCase());
                ((Map<String, Boolean>) Kits.kitData.get(player.getName().toLowerCase())).put("indira",true);
                ((Map<String, Boolean>) Kits.kitData.get(player.getName().toLowerCase())).put("zephyros",true);
                ((Map<String, Boolean>) Kits.kitData.get(player.getName().toLowerCase())).put("hades",true);
                ((Map<String, Boolean>) Kits.kitData.get(player.getName().toLowerCase())).put("artemis",true);
                ((Map<String, Boolean>) Kits.kitData.get(player.getName().toLowerCase())).put("dione",true);
            }
        }*/
    }

    public static int getYoutube(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("youtube"));
    }

    public static void setYoutube(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("youtube",String.valueOf(s));
    }

    public static int getMurderKarma(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("murder_karma"));
    }

    public static void setMurderKarma(Player player,int s){
        if(s <= -500) s = -500;
        if(s >= 1000){
            MurderAchievements.setLevel(player, "Saint", 2);
            s = 1000;
        }
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("murder_karma",String.valueOf(s));
    }

    public static int getMurderMisskills(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("murder_misskills"));
    }

    public static void setMurderMisskills(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("murder_misskills",String.valueOf(s));
    }

    public static void setMurderMurderWin(Player player,int s){
        if(s != 0 && s >= 20){
            int level = (int) Math.floor(s / 20) + 1;
            MurderAchievements.setLevel(player, "Win_As_The_Murderer", level);
        }
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("murder_murder_win",String.valueOf(s));
    }

    public static int getMurderMurderWin(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("murder_murder_win"));
    }

    public static void setMurderMurderLose(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("murder_murder_lose",String.valueOf(s));
    }

    public static int getMurderMurderLose(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("murder_murder_lose"));
    }

    public static void setMurderBystanderWin(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("murder_bystander_win",String.valueOf(s));
    }

    public static int getMurderBystanderWin(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("murder_bystander_win"));
    }

    public static void setMurderBystanderLose(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("murder_bystander_lose",String.valueOf(s));
    }

    public static int getMurderBystanderLose(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("murder_bystander_lose"));
    }

    public static void setMurderMurderKills(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("murder_murder_kills",String.valueOf(s));
    }

    public static int getMurderMurderKills(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("murder_murder_kills"));
    }

    public static void setMurderBystanderKills(Player player,int s){
        if(s != 0 && s >= 50){
            int level = (int) Math.floor(s / 50) + 1;
            MurderAchievements.setLevel(player, "Shoot_The_Murderer", level);
        }
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("murder_bystander_kills",String.valueOf(s));
    }

    public static int getMurderBystanderKills(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("murder_bystander_kills"));
    }

    public static void setMurderMurderDeaths(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("murder_murder_deaths",String.valueOf(s));
    }

    public static int getMurderMurderDeaths(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("murder_murder_deaths"));
    }

    public static void setMurderBystanderDeaths(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("murder_bystander_deaths",String.valueOf(s));
    }

    public static int getMurderBystanderDeaths(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("murder_bystander_deaths"));
    }

    public static void setMurderInnocentsShot(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("murder_innocentsshot",String.valueOf(s));
    }

    public static int getMurderInnocentsShot(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("murder_innocentsshot"));
    }

    public static void setMurderEmeraldCollected(Player player,int s){
        if(s != 0 && s >= 400){
            int level = (int) Math.floor(s / 400) + 1;
            MurderAchievements.setLevel(player, "Picking_Up_The_Pieces", level);
        }
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("murder_emeraldcollected",String.valueOf(s));
    }

    public static int getMurderEmeraldCollected(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("murder_emeraldcollected"));
    }

    public static void setMurderWeaponTraded(Player player,int s){
        if(s != 0 && s >= 40){
            int level = (int) Math.floor(s / 40) + 1;
            MurderAchievements.setLevel(player, "Trading_With_The_Villagers", level);
        }
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("murder_weapontraded",String.valueOf(s));
    }

    public static int getMurderWeaponTraded(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("murder_weapontraded"));
    }

    public static void setMurderPlays(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("murder_plays",String.valueOf(s));
    }

    public static int getMurderPlays(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("murder_plays"));
    }

    public static void setMurderHat(Player player,String s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("murder_hat",s);
    }

    public static String getMurderHat(Player player){
        return ((Map<String, String>) stat.get(player.getName().toLowerCase())).get("murder_hat");
    }

    public static void setSkywarsKills(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("skywars_kills",String.valueOf(s));
    }

    public static int getSkywarsKills(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("skywars_kills"));
    }

    public static void setSkywarsDeaths(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("skywars_deaths",String.valueOf(s));
    }

    public static int getSkywarsDeaths(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("skywars_deaths"));
    }

    public static void setSkywarsWin(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("skywars_win",String.valueOf(s));
    }

    public static int getSkywarsWin(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("skywars_win"));
    }

    public static void setSkywarsLose(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("skywars_lose",String.valueOf(s));
    }

    public static int getSkywarsLose(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("skywars_lose"));
    }

    public static void setSkywarsPlays(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("skywars_plays",String.valueOf(s));
    }

    public static int getSkywarsPlays(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("skywars_plays"));
    }

    public static void setBuildbattleWin(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("buildbattle_win",String.valueOf(s));
    }

    public static int getBuildbattleWin(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("buildbattle_win"));
    }

    public static void setBuildbattleLose(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("buildbattle_lose",String.valueOf(s));
    }

    public static int getBuildbattleLose(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("buildbattle_lose"));
    }

    public static void setBuildbattlePlays(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("buildbattle_plays",String.valueOf(s));
    }

    public static int getBuildbattlePlays(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("buildbattle_plays"));
    }

    public static void setMiniwallsKills(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("miniwalls_kills",String.valueOf(s));
    }

    public static int getMiniwallsKills(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("miniwalls_kills"));
    }

    public static void setMiniwallsDeaths(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("miniwalls_deaths",String.valueOf(s));
    }

    public static int getMiniwallsDeaths(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("miniwalls_deaths"));
    }

    public static void setMiniwallsWin(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("miniwalls_win",String.valueOf(s));
    }

    public static int getMiniwallsWin(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("miniwalls_win"));
    }

    public static void setMiniwallsLose(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("miniwalls_lose",String.valueOf(s));
    }

    public static int getMiniwallsLose(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("miniwalls_lose"));
    }

    public static void setMiniwallsSoldierLevel(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("miniwalls_soldierlevel",String.valueOf(s));
    }

    public static int getMiniwallsSoldierLevel(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("miniwalls_soldierlevel"));
    }

    public static void setMiniwallsArcherLevel(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("miniwalls_archerlevel",String.valueOf(s));
    }

    public static int getMiniwallsArcherLevel(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("miniwalls_archerlevel"));
    }

    public static void setMiniwallsBuilderLevel(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("miniwalls_builderlevel",String.valueOf(s));
    }

    public static int getMiniwallsBuilderLevel(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("miniwalls_builderlevel"));
    }

    public static void setMiniwallsPlays(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("miniwalls_plays",String.valueOf(s));
    }

    public static int getMiniwallsPlays(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("miniwalls_plays"));
    }

    public static void setEggwarsKills(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("eggwars_kills",String.valueOf(s));
    }

    public static int getEggwarsKills(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("eggwars_kills"));
    }

    public static void setEggwarsDeaths(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("eggwars_deaths",String.valueOf(s));
    }

    public static int getEggwarsDeaths(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("eggwars_deaths"));
    }

    public static void setEggwarsWin(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("eggwars_win",String.valueOf(s));
    }

    public static int getEggwarsWin(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("eggwars_win"));
    }

    public static void setEggwarsLose(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("eggwars_lose",String.valueOf(s));
    }

    public static int getEggwarsLose(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("eggwars_lose"));
    }

    public static void setEggwarsPlays(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("eggwars_plays",String.valueOf(s));
    }

    public static int getEggwarsPlays(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("eggwars_plays"));
    }

    public static void addCoin(Player player,int s){
        setCoin(player, getCoin(player) + s);
        Clan.onPlayerGetCoin(player, s);
    }

    public static void setCoin(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("coin",String.valueOf(s));
    }

    public static int getCoin(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("coin"));
    }

    public static void setMedal(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("medal",String.valueOf(s));
    }

    public static int getMedal(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("medal"));
    }

    public static void setLevel(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("level",String.valueOf(s));
    }

    public static int getLevel(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("level"));
    }

    public static void setExp(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("exp",String.valueOf(s));
    }

    public static int getExp(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("exp"));
    }

    public static void setPopupSide(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("popupside",String.valueOf(s));
    }

    public static int getPopupSide(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("popupside"));
    }

    public static void setPopupSpace(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("popupspace",String.valueOf(s));
    }

    public static int getPopupSpace(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("popupspace"));
    }

    public static void setPopupUp(Player player,int s){
        ((Map<String, String>) stat.get(player.getName().toLowerCase())).put("popupup",String.valueOf(s));
    }

    public static int getPopupUp(Player player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("popupup"));
    }

    public static void addExp(Player player,int e){
        int exp = getExp(player)+e;
        int need = getLevel(player)*getLevel(player)*10+100;
        setExp(player,exp);
            if(exp >= need){
                setLevel(player,getLevel(player)+1);
                setExp(player,exp-need);
                player.sendMessage(Main.getMessage(player,"level.up",new String[]{String.valueOf(getLevel(player)-1),String.valueOf(getLevel(player))}));
            }
    }

    public static void setNameTag(Player player){
        player.setNameTag("§f"+getWarnTag(player)+getVipTag(player)+player.getName()+"§f");
        player.setDisplayName("§f"+getWarnTag(player)+getVipTag(player)+player.getName()+"§f");
        Server.getInstance().updatePlayerListData(player.getUniqueId(), player.getId(), player.getName(), player.getSkin());
    }

    public static String getYoutubeTag(Player player){
        switch(getYoutube(player)){
            case 0:
                return "";
            case 1:
                return "§4YT";
        }
        return "";
    }

    public static String getVipTag(Player player){
        switch(getVip(player)){
            case 1:
                return "§fVIP";
            case 2:
                return "§fMVP";
            case 3:
                return "§fULTRA";
            case 4:
                return "§fLEGEND";
            case 10:
                return "§c[STAFF]§f";
            case 11:
                return "§6[Builder]§f";
            case 12:
                return "§d[Dev]§f";
            case 13:
                return "§4[Owner]§f";
        }
        return "";
    }

    public static String getWarnTag(Player player){
        switch(getWarn(player)){
            case 1:
                return "§e⚠§f";
            case 2:
                return "§c⚠§f";
        }
        return "";
    }

    public static void sendStat(Player player){
        int need = getLevel(player)*getLevel(player)*10+100;
        player.sendMessage(Main.getMessage(player, "status.lobby",new String[]{String.valueOf(getLevel(player)),String.valueOf((need-getExp(player))),String.valueOf(getCoin(player)),String.valueOf(getMedal(player))}));
    }

    public static void sendMurderStat(Player player){
        //int need = getLevel(player)*getLevel(player)*10+100;
        //player.sendMessage(Main.getMessage(player, "status.murder",new String[]{String.valueOf(getLevel(player)),String.valueOf((need-getExp(player))),String.valueOf(getCoin(player)),String.valueOf(getMedal(player)),String.valueOf(getMurderKarma(player)),String.valueOf(getMurderWon(player)),String.valueOf(getMurderLoss(player))}));
    }

    public static void sendSkywarsStat(Player player){
        int need = getLevel(player)*getLevel(player)*10+100;
        player.sendMessage(Main.getMessage(player, "status.skywars",new String[]{String.valueOf(getLevel(player)),String.valueOf((need-getExp(player))),String.valueOf(getCoin(player)),String.valueOf(getMedal(player)),String.valueOf(getSkywarsWin(player)),String.valueOf(getSkywarsLose(player))}));
    }

    public static String getPopupSpaceString(Player player){
        int time = getPopupSpace(player);
        String result = "";
        for(int i = 0;i < time;i++){
            result += "\n";
        }
        return result;
    }

    public static String getPopupUpString(Player player,String target){
        String item = "\n";
        int level = 0;
        int s = 0;
        while (s < target.length()) {
            int index = target.indexOf(item, s);
            s += (index + item.length());
            level++;
        }
        int time = 7;
        time += Math.floor(level / 5);
        String result = "";
        for(int i = 0;i < time;i++){
            result += "\n";
        }
        return result;
    }

    public static int getWarn(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("warn"));
    }

    public static int getVip(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("vip"));
    }

    public static int getYoutube(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("youtube"));
    }

    public static int getMurderKarma(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("murderkarma"));
    }

    public static int getMurderMisskill(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("murdermisskill"));
    }

    public static int getMurderMurderWon(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("murdermurderwon"));
    }

    public static int getMurderMurderLoss(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("murdermurderloss"));
    }

    public static int getMurderBystanderWon(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("murderbystanderwon"));
    }

    public static int getMurderBystanderLoss(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("murderbystanderloss"));
    }

    public static int getMurderMurderKill(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("murdermurderkill"));
    }

    public static int getMurderBystanderKill(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("murderbystanderkill"));
    }

    public static int getMurderMurderDeath(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("murdermurderdeath"));
    }

    public static int getMurderBystanderDeath(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("murderbystanderdeath"));
    }

    public static int getMurderInnocentsShot(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("murderinnocentsshot"));
    }

    public static int getMurderEmeraldCollect(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("murderemeraldcollect"));
    }

    public static int getMurderWeaponTrade(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("murderweapontrade"));
    }

    public static int getMurderPlay(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("murderplay"));
    }

    public static String getMurderHat(PlayerClone player){
        return ((Map<String, String>) stat.get(player.getName().toLowerCase())).get("murderhat");
    }

    public static int getSkywarsKill(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("skywarskill"));
    }

    public static int getSkywarsDeath(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("skywarsdeath"));
    }

    public static int getSkywarsWon(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("skywarswon"));
    }

    public static int getSkywarsLoss(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("skywarsloss"));
    }

    public static int getSkywarsPlay(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("skywarsplay"));
    }

    public static int getBuildbattleWon(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("buildbattlewon"));
    }

    public static int getBuildbattleLoss(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("buildbattleloss"));
    }

    public static int getBuildbattlePlay(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("buildbattleplay"));
    }

    public static int getMiniwallsKill(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("miniwallskill"));
    }

    public static int getMiniwallsDeath(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("miniwallsdeath"));
    }

    public static int getMiniwallsWon(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("miniwallswon"));
    }

    public static int getMiniwallsLoss(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("miniwallsloss"));
    }

    public static int getMiniwallsSoldierLevel(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("miniwallssoldierlevel"));
    }

    public static int getMiniwallsArcherLevel(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("miniwallsarcherlevel"));
    }

    public static int getMiniwallsBuilderLevel(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("miniwallsbuilderlevel"));
    }

    public static int getMiniwallsPlay(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("miniwallsplay"));
    }

    public static int getCoin(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("coin"));
    }

    public static int getMedal(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("medal"));
    }

    public static int getLevel(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("level"));
    }

    public static int getExp(PlayerClone player){
        return Integer.parseInt(((Map<String, String>) stat.get(player.getName().toLowerCase())).get("exp"));
    }

    public static HashMap<Player,Integer> auth = new HashMap<Player,Integer>();
    public static LinkedHashMap<String, Object> stat = new LinkedHashMap<String, Object>();

}
