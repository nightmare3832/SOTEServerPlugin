package sote.inventory.skywars;

import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import sote.Main;
import sote.inventory.Inventorys;
import sote.inventory.Profile;
import sote.inventory.ProfileChestInventory;
import sote.stat.Stat;

public class SkywarsStatsProfile extends Profile{

    public static final int CHEST_SIZE = 54;

    public final int PROFILE_BACK_INDEX = 49;
    public final int ACHIEVEMENTS_INDEX = 11;
    public final int STATS_INDEX = 13;
    public final int GENERAL_INDEX = 14;
    public final int ACHIEVEMENTS_COIN_INDEX = 49;
    public final int ACHIEVEMENTS_BACK_INDEX = 18;
    public final int ACHIEVEMENTS_NEXT_INDEX = 26;
    public final int[] ACHIEVEMENTS_PAGE_INDEX = new int[]{
            10,11,12,13,14,15,16,
            19,20,21,22,23,24,25,
            28,29,30,31,32,33,34
    };

    public final int POCKET_PROFILE_BACK_INDEX = 3;
    public final int POCKET_ACHIEVEMENTS_INDEX = 0;
    public final int POCKET_STATS_INDEX = 1;
    public final int POCKET_GENERAL_INDEX = 2;
    public final int POCKET_ACHIEVEMENTS_COIN_INDEX = 5;
    public final int POCKET_ACHIEVEMENTS_BACK_INDEX = 1;
    public final int POCKET_ACHIEVEMENTS_NEXT_INDEX = 4;
    public final int[] POCKET_ACHIEVEMENTS_PAGE_INDEX = new int[]{
            6,7,8,9,10,11,12,
            13,14,15,16,17,18,
            19,20,21,22,23,24,
            25,26,27,28,29,30,
            31,32,33,34,35,36,
            37,38,39,40,41,42
    };

    public final int SCREEN_TOP = 100;
    public final int SCREEN_ACHIEVEMENTS = 101;

    public final int ACHIEVEMENTS_PAGE_MAX = 28;
    public final int POCKET_ACHIEVEMENTS_PAGE_MAX = 36;

    public int page = 1;

    public SkywarsStatsProfile(ProfileChestInventory owner){
        super(owner);
    }

    @Override
    public HashMap<Integer, Item> update(Player player){
        HashMap<Integer, Item> items = new HashMap<Integer, Item>();
        Item item;
        switch(this.owner.screen){
            case SCREEN_TOP:
                double ratio = 0;
                item = Item.get(Item.NETHER_STAR);
                item.setCustomName(Main.getMessage(player, "item.murder.stats.achievements"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(ACHIEVEMENTS_INDEX, item);
                else items.put(POCKET_ACHIEVEMENTS_INDEX, item);
                item = Item.get(Item.GOLD_SWORD);
                if(Stat.getSkywarsWin(player) == 0 || Stat.getSkywarsLose(player) == 0){
                    ratio = 0;
                }else{
                    ratio = Stat.getSkywarsWin(player) / Stat.getSkywarsLose(player);
                }
                item.setCustomName("§bSkyWars Stats\n"
                                 + "§7Kills: §a"+Stat.getSkywarsKills(player)+"\n"
                                 + "§8Deaths: §2"+Stat.getSkywarsDeaths(player)+"\n"
                                 + "§7Wins: §a"+Stat.getSkywarsWin(player)+"\n"
                                 + "§8Losses: §2"+Stat.getSkywarsLose(player)+"\n"
                                 + "§7Win/Loss Ratio: §a"+ratio);
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(STATS_INDEX, item);
                else items.put(POCKET_STATS_INDEX, item);
                item = Item.get(Item.DIAMOND);
                item.setCustomName("§bGeneral Stats\n"
                                 + "§7Level: §a"+Stat.getLevel(player)+"\n"
                                 + "§8Coin: §2"+Stat.getCoin(player)+"\n"
                                 + "§7Medal: §a"+Stat.getMedal(player));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(GENERAL_INDEX, item);
                else items.put(POCKET_GENERAL_INDEX, item);
                item = Item.get(Item.GHAST_TEAR,0,1);
                item.setCustomName(Main.getMessage(player,"item.back"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(PROFILE_BACK_INDEX, item);
                else items.put(POCKET_PROFILE_BACK_INDEX, item);
            break;
            case SCREEN_ACHIEVEMENTS:
                item = Item.get(Item.ARROW,0,1);
                item.setCustomName(Main.getMessage(player,"item.murder.shop.hats.backpage"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(ACHIEVEMENTS_BACK_INDEX, item);
                else items.put(POCKET_ACHIEVEMENTS_BACK_INDEX, item);
                /*boolean containsNextPage = false;
                int count = 0;
                int pageMax;
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) pageMax = ACHIEVEMENTS_PAGE_MAX;
                else pageMax = POCKET_ACHIEVEMENTS_PAGE_MAX;
                for (Map.Entry<String,Item> e : MurderAchievements.item.entrySet()){
                    if(count >= (pageMax * (this.page - 1)) && count < (pageMax * this.page)){
                        if(MurderAchievements.getLevel(player, e.getKey()) >= 2){
                            item = e.getValue();
                            item.setCustomName(MurderAchievements.getItemName(player, e.getKey()));
                        }else{
                            item = Item.get(Item.DYE);
                            item.setCustomName(MurderAchievements.getItemName(player, e.getKey()));
                        }
                        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC){
                            items.put(ACHIEVEMENTS_PAGE_INDEX[count], item);
                        }else{
                            items.put(POCKET_ACHIEVEMENTS_PAGE_INDEX[count], item);
                        }
                        count++;
                    }else if(count >= (pageMax * this.page)){
                        containsNextPage = true;
                    }
                }
                if(containsNextPage){
                    item = Item.get(Item.ARROW,0,1);
                    item.setCustomName(Main.getMessage(player,"item.murder.shop.hats.nextpage"));
                    if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(ACHIEVEMENTS_BACK_INDEX, item);
                    else items.put(POCKET_ACHIEVEMENTS_BACK_INDEX, item);
                }
                item = Item.get(Item.EMERALD,0,1);
                item.setCustomName(Main.getMessage(player,"item.murder.shop.coin", new String[]{String.valueOf(Stat.getCoin(player))}));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(ACHIEVEMENTS_COIN_INDEX, item);
                else items.put(POCKET_ACHIEVEMENTS_COIN_INDEX, item);*/
            break;
        }
        return items;
    }

    @Override
    public void Function(Player player,int slot,Item item){
        if(Inventorys.getGUI(player) == Inventorys.GUI_POCKET && this.owner.doubletouch != slot){
            this.owner.update();
            this.owner.doubletouch = slot;
            return;
        }
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC){
            if(item.getId() != Item.AIR){
                switch(this.owner.screen){
                    case SCREEN_TOP:
                        switch(slot){
                            case PROFILE_BACK_INDEX:
                                this.owner.screen = 0;
                            break;
                            case ACHIEVEMENTS_INDEX:
                                this.owner.screen = SCREEN_ACHIEVEMENTS;
                            break;
                        }
                    break;
                    case SCREEN_ACHIEVEMENTS:
                        switch(slot){
                            case ACHIEVEMENTS_BACK_INDEX:
                                if(this.page == 1){
                                    this.owner.screen = SCREEN_TOP;
                                    this.page = 1;
                                }else{
                                    this.page--;
                                }
                            break;
                            case ACHIEVEMENTS_NEXT_INDEX:
                                this.page++;
                            break;
                        }
                    break;
                }
                this.owner.update();
            }
        }else{
            if(item.getId() == Item.AIR){
                switch(this.owner.screen){
                    case SCREEN_TOP:
                        switch(slot){
                            case POCKET_PROFILE_BACK_INDEX:
                                this.owner.screen = 0;
                            break;
                            case POCKET_ACHIEVEMENTS_INDEX:
                                this.owner.screen = SCREEN_ACHIEVEMENTS;
                            break;
                        }
                    break;
                    case SCREEN_ACHIEVEMENTS:
                        switch(slot){
                            case POCKET_ACHIEVEMENTS_BACK_INDEX:
                                if(this.page == 1){
                                    this.owner.screen = SCREEN_TOP;
                                    this.page = 1;
                                }else{
                                    this.page--;
                                }
                            break;
                            case POCKET_ACHIEVEMENTS_NEXT_INDEX:
                                this.page++;
                            break;
                        }
                    break;
                }
                this.owner.update();
            }
        }
    }
}