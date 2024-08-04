package sote.inventory;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.BlockEntityDataPacket;
import cn.nukkit.network.protocol.ContainerOpenPacket;
import cn.nukkit.network.protocol.ContainerSetContentPacket;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import cn.nukkit.scheduler.Task;
import sote.Main;
import sote.PlayerDataManager;
import sote.ServerItem;
import sote.achievements.Achievements;
import sote.clan.Clan;
import sote.lang.Language;
import sote.setting.Setting;

public class ProfileChestInventory extends ServerChestInventory{

    public static final int CHEST_SIZE = 54;

    public final int ACHIEVEMENTS_INDEX = 12;
    public final int LANGUAGE_INDEX = 14;
    public final int CLAN_INDEX = 29;
    public final int SETTING_INDEX = 33;
    public final int STATUS_INDEX = 40;

    public final int ACHIEVEMENTS_BACK_INDEX = 48;
    public final int ACHIEVEMENTS_NEXT_INDEX = 50;
    public final int[] ACHIEVEMENTS_PAGE_INDEX = new int[]{
            10,11,12,13,14,15,16,
            19,20,21,22,23,24,25,
            28,29,30,31,32,33,34
    };

    public final int STATUS_COIN_INDEX = 12;
    public final int STATUS_LEVEL_INDEX = 14;
    public final int STATUS_RANK_INDEX = 19;
    public final int STATUS_KARMA_INDEX = 25;
    public final int STATUS_GAME_STATUS_INDEX = 31;
    public final int STATUS_BACK_INDEX = 49;

    public final int LANGUAGE_JAPANESE_INDEX = 10;
    public final int LANGUAGE_ENGLISH_US_INDEX = 11;
    public final int LANGUAGE_CHINESE_INDEX = 12;
    public final int LANGUAGE_PORTUGAL_INDEX = 13;
    public final int LANGUAGE_BACK_INDEX = 49;

    public final int CLAN_MEMBER_INDEX = 20;
    public final int CLAN_COIN_INDEX = 22;
    public final int CLAN_TAG_INDEX = 24;
    public final int CLAN_BACK_INDEX = 49;

    public final int SETTING_HIDEPLAYER_INDEX = 10;
    public final int SETTING_HIDEPLAYER_BUTTON_INDEX = 19;
    public final int SETTING_HIDECHAT_INDEX = 11;
    public final int SETTING_HIDECHAT_BUTTON_INDEX = 20;
    public final int SETTING_BACK_INDEX = 49;


    public final int POCKET_ACHIEVEMENTS_INDEX = 0;
    public final int POCKET_LANGUAGE_INDEX = 1;
    public final int POCKET_CLAN_INDEX = 2;
    public final int POCKET_SETTING_INDEX = 3;
    public final int POCKET_STATUS_INDEX = 4;

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

    public final int POCKET_STATUS_COIN_INDEX = 1;
    public final int POCKET_STATUS_LEVEL_INDEX = 2;
    public final int POCKET_STATUS_RANK_INDEX = 3;
    public final int POCKET_STATUS_KARMA_INDEX = 4;
    public final int POCKET_STATUS_GAME_STATUS_INDEX = 5;
    public final int POCKET_STATUS_BACK_INDEX = 6;

    public final int POCKET_LANGUAGE_JAPANESE_INDEX = 0;
    public final int POCKET_LANGUAGE_ENGLISH_US_INDEX = 1;
    public final int POCKET_LANGUAGE_CHINESE_INDEX = 2;
    public final int POCKET_LANGUAGE_PORTUGAL_INDEX = 3;
    public final int POCKET_LANGUAGE_BACK_INDEX = 4;

    public final int POCKET_CLAN_MEMBER_INDEX = 0;
    public final int POCKET_CLAN_COIN_INDEX = 1;
    public final int POCKET_CLAN_TAG_INDEX = 2;
    public final int POCKET_CLAN_BACK_INDEX = 3;

    public final int POCKET_SETTING_HIDEPLAYER_INDEX = 0;
    public final int POCKET_SETTING_HIDEPLAYER_BUTTON_INDEX = 1;
    public final int POCKET_SETTING_HIDECHAT_INDEX = 2;
    public final int POCKET_SETTING_HIDECHAT_BUTTON_INDEX = 3;
    public final int POCKET_SETTING_BACK_INDEX = 4;

    public final int SCREEN_TOP = 0;
    public final int SCREEN_ACHIEVEMENTS = 1;
    public final int SCREEN_LANGUAGE = 2;
    public final int SCREEN_CLAN = 3;
    public final int SCREEN_SETTING = 4;
    public final int SCREEN_STATUS = 5;
    public final int SCREEN_SECOND_TOP = 100;

    public final int ACHIEVEMENTS_PAGE_MAX = 28;
    public final int POCKET_ACHIEVEMENTS_PAGE_MAX = 36;

    public HashMap<Integer, Item> shopItem = new HashMap<Integer, Item>();
    public HashMap<Integer, String> shopHat = new HashMap<Integer, String>();
    //0: Helmet 1: Leggings 2: Boots 3: Weapon
    public int screen = 0;
    public int page = 1;
    public Profile profile;

    public ProfileChestInventory(Player player){
        super(player);
    }

    @Override
    public void register(){
        this.spawnChestBlock();
        this.SpawnBlockEntity();
        HashMap<Integer, Item> items = new HashMap<Integer, Item>();
        Item item;
        item = Item.get(Item.NETHER_STAR,0,1);
        item.setCustomName(Main.getMessage(player,"item.achievement"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(ACHIEVEMENTS_INDEX, item);
        else items.put(POCKET_ACHIEVEMENTS_INDEX, item);
        item = ServerItem.getServerItemByLanguage(Language.getLang(player));
        item.setCustomName(Main.getMessage(player,"item.language"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(LANGUAGE_INDEX, item);
        else items.put(POCKET_LANGUAGE_INDEX, item);
        item = Item.get(Item.EMPTY_MAP,0,1);
        item.setCustomName(Main.getMessage(player,"item.clan"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(CLAN_INDEX, item);
        else items.put(POCKET_CLAN_INDEX, item);
        item = Item.get(Item.COMPARATOR,3,1);
        item.setCustomName(Main.getMessage(player,"item.setting"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(SETTING_INDEX, item);
        else items.put(POCKET_SETTING_INDEX, item);
        item = Item.get(Item.SKULL,3,1);
        item.setCustomName(Main.getMessage(player,"item.status"));
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(STATUS_INDEX, item);
        else items.put(POCKET_STATUS_INDEX, item);
        this.doubleinv = new DoubleServerChestInventory(items);
        this.screen = 0;
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackOpenProfileChestInventory(this), 5);
    }

    public void spawnChestBlock(){
        this.x = (int) player.x;
        this.y = (int) player.y - 3;
        this.z = (int) player.z;
        this.x2 = (int) player.x + 1;
        this.y2 = (int) player.y - 3;
        this.z2 = (int) player.z;
        this.after = player.getLevel().getBlock(new Vector3(x, y, z));
        UpdateBlockPacket pk = new UpdateBlockPacket();
        pk.x = x;
        pk.y = y;
        pk.z = z;
        pk.blockId = 54;
        pk.blockData = 0;
        pk.flags = UpdateBlockPacket.FLAG_NONE;
        player.dataPacket(pk);
        this.after2 = player.getLevel().getBlock(new Vector3(x + 1, y, z));
        UpdateBlockPacket pk2 = new UpdateBlockPacket();
        pk2.x = x + 1;
        pk2.y = y;
        pk2.z = z;
        pk2.blockId = 54;
        pk2.blockData = 0;
        pk2.flags = UpdateBlockPacket.FLAG_NONE;
        player.dataPacket(pk2);
    }

    public void SpawnBlockEntity(){
        CompoundTag nbt = new CompoundTag("")
                .putList(new ListTag<>("Items"))
                .putString("id", BlockEntity.CHEST)
                .putInt("x", x)
                .putInt("y", y)
                .putInt("z", z)
                .putInt("pairx", x + 1)
                .putInt("pairz", z);
        BlockEntityDataPacket pk = new BlockEntityDataPacket();
        pk.x = x;
        pk.y = y;
        pk.z = z;
        try {
            pk.namedTag = NBTIO.write(nbt, ByteOrder.LITTLE_ENDIAN, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        player.dataPacket(pk);
        CompoundTag nbt2 = new CompoundTag("")
                .putList(new ListTag<>("Items"))
                .putString("id", BlockEntity.CHEST)
                .putInt("x", x + 1)
                .putInt("y", y)
                .putInt("z", z)
                .putInt("pairx", x)
                .putInt("pairz", z);
        BlockEntityDataPacket pk2 = new BlockEntityDataPacket();
        pk2.x = x + 1;
        pk2.y = y;
        pk2.z = z;
        try {
            pk2.namedTag = NBTIO.write(nbt2, ByteOrder.LITTLE_ENDIAN, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        player.dataPacket(pk2);
    }

    public void open(){
        ContainerOpenPacket pk = new ContainerOpenPacket();
        pk.windowid = (byte) windowid;
        pk.type = (byte) 0;
        //pk.slots = CHEST_SIZE;
        pk.x = x;
        pk.y = y;
        pk.z = z;
        player.dataPacket(pk);
        sendContents();
    }

    public void sendContents(){
        ContainerSetContentPacket pk2 = new ContainerSetContentPacket();
        pk2.slots = new Item[CHEST_SIZE];
        for (int i = 0; i < CHEST_SIZE; ++i) {
            pk2.slots[i] = doubleinv.getItem(i);
        }
        pk2.windowid = (byte) windowid;
        player.dataPacket(pk2);
    }

    public void sendSlot(){
    }

    @Override
    public void update(){
        HashMap<Integer, Item> items = new HashMap<Integer, Item>();
        Item item;
        if(this.screen >= 100){
            items = this.profile.update(player);
        }
        switch(this.screen){
            case SCREEN_TOP:
                item = Item.get(Item.NETHER_STAR,0,1);
                item.setCustomName(Main.getMessage(player,"item.achievements"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(ACHIEVEMENTS_INDEX, item);
                else items.put(POCKET_ACHIEVEMENTS_INDEX, item);
                item = ServerItem.getServerItemByLanguage(Language.getLang(player));
                item.setCustomName(Main.getMessage(player,"item.language"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(LANGUAGE_INDEX, item);
                else items.put(POCKET_LANGUAGE_INDEX, item);
                item = Item.get(Item.EMPTY_MAP,0,1);
                item.setCustomName(Main.getMessage(player,"item.clan"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(CLAN_INDEX, item);
                else items.put(POCKET_CLAN_INDEX, item);
                item = Item.get(Item.COMPARATOR,3,1);
                item.setCustomName(Main.getMessage(player,"item.setting"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(SETTING_INDEX, item);
                else items.put(POCKET_SETTING_INDEX, item);
                item = Item.get(Item.SKULL,3,1);
                item.setCustomName(Main.getMessage(player,"item.status"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(STATUS_INDEX, item);
                else items.put(POCKET_STATUS_INDEX, item);
            break;
            case SCREEN_ACHIEVEMENTS:
                item = Item.get(Item.ARROW,0,1);
                item.setCustomName(Main.getMessage(player,"item.achievements.backpage"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(ACHIEVEMENTS_BACK_INDEX, item);
                else items.put(POCKET_ACHIEVEMENTS_BACK_INDEX, item);
                boolean containsNextPage = false;
                int count = 0;
                int pageMax;
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) pageMax = ACHIEVEMENTS_PAGE_MAX;
                else pageMax = POCKET_ACHIEVEMENTS_PAGE_MAX;
                for (Map.Entry<String,Item> e : Achievements.item.entrySet()){
                    if(count >= (pageMax * (this.page - 1)) && count < (pageMax * this.page)){
                        if(Achievements.getLevel(player, e.getKey()) >= 2){
                            item = e.getValue();
                            item.setCustomName(Achievements.getItemName(player, e.getKey()));
                        }else{
                            item = Item.get(Item.DYE);
                            item.setCustomName(Achievements.getItemName(player, e.getKey()));
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
                    item.setCustomName(Main.getMessage(player,"item.achievements.nextpage"));
                    if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(ACHIEVEMENTS_BACK_INDEX, item);
                    else items.put(POCKET_ACHIEVEMENTS_BACK_INDEX, item);
                }
            break;
            case SCREEN_LANGUAGE:
                item = ServerItem.getServerItemByString("Japan_Block");
                item.setCustomName(Main.lang.get("ja_JP").get("language.name"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(LANGUAGE_JAPANESE_INDEX, item);
                else items.put(POCKET_LANGUAGE_JAPANESE_INDEX, item);
                item = ServerItem.getServerItemByString("America_Block");
                item.setCustomName(Main.lang.get("en_US").get("language.name"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(LANGUAGE_ENGLISH_US_INDEX, item);
                else items.put(POCKET_LANGUAGE_ENGLISH_US_INDEX, item);
                item = ServerItem.getServerItemByString("China_Block");
                item.setCustomName(Main.lang.get("zn_CN").get("language.name"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(LANGUAGE_CHINESE_INDEX, item);
                else items.put(POCKET_LANGUAGE_CHINESE_INDEX, item);
                item = ServerItem.getServerItemByString("Portugal_Block");
                item.setCustomName(Main.lang.get("pt_PT").get("language.name"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(LANGUAGE_PORTUGAL_INDEX, item);
                else items.put(POCKET_LANGUAGE_PORTUGAL_INDEX, item);
                item = Item.get(Item.GHAST_TEAR,0,1);
                item.setCustomName(Main.getMessage(player,"item.back"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(LANGUAGE_BACK_INDEX, item);
                else items.put(POCKET_LANGUAGE_BACK_INDEX, item);
            break;
            case SCREEN_CLAN:
                Map<String, String> mapp = Clan.clanData.get(player.getName().toLowerCase());
                if(!mapp.get("owner").equals("")){//clanに参加しているかどうか
                    Map<String, String> map = (Map<String, String>) Clan.clanData.get(mapp.get("owner"));
                    item = Item.get(Item.PAPER,0,1);
                    String member = map.get("member").replace(",", "\n");
                    boolean isFirst = true;
                    String members = "";
                    for(String m : member.split("\n")){
                        if(isFirst){
                            members += PlayerDataManager.getPlayerData(m).getName();
                            isFirst = false;
                        }else{
                            members += "\n"+PlayerDataManager.getPlayerData(m).getName();
                        }
                    }
                    item.setCustomName(Main.getMessage(player,"item.clan.member", new String[]{members}));
                    if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(CLAN_MEMBER_INDEX, item);
                    else items.put(POCKET_CLAN_MEMBER_INDEX, item);
                    item = Item.get(Item.DOUBLE_PLANT,0,1);
                    item.setCustomName(Main.getMessage(player,"item.clan.coin", new String[]{String.valueOf(Clan.getClanCoin(player))}));
                    if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(CLAN_COIN_INDEX, item);
                    else items.put(POCKET_CLAN_COIN_INDEX, item);
                    item = Item.get(Item.NAME_TAG,0,1);
                    item.setCustomName(Main.getMessage(player,"item.clan.tag"));
                    if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(CLAN_TAG_INDEX, item);
                    else items.put(POCKET_CLAN_TAG_INDEX, item);
                }
                item = Item.get(Item.GHAST_TEAR,0,1);
                item.setCustomName(Main.getMessage(player,"item.back"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(CLAN_BACK_INDEX, item);
                else items.put(POCKET_CLAN_BACK_INDEX, item);
            break;
            case SCREEN_STATUS:
                item = Item.get(Item.DOUBLE_PLANT,0,1);
                item.setCustomName(Main.getMessage(player,"item.coin"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(STATUS_COIN_INDEX, item);
                else items.put(POCKET_STATUS_COIN_INDEX, item);
                item = Item.get(Item.EXPERIENCE_BOTTLE,0,1);
                item.setCustomName(Main.getMessage(player,"item.level"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(STATUS_LEVEL_INDEX, item);
                else items.put(POCKET_STATUS_LEVEL_INDEX, item);
                item = Item.get(Item.END_CRYSTAL,0,1);
                item.setCustomName(Main.getMessage(player,"item.rank"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(STATUS_RANK_INDEX, item);
                else items.put(POCKET_STATUS_RANK_INDEX, item);
                item = Item.get(Item.DRAGON_BREATH,0,1);
                item.setCustomName(Main.getMessage(player,"item.karma"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(STATUS_KARMA_INDEX, item);
                else items.put(POCKET_STATUS_KARMA_INDEX, item);
                item = Item.get(Item.ENCHANTED_BOOK,0,1);
                item.setCustomName(Main.getMessage(player,"item.gamestatus"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(STATUS_GAME_STATUS_INDEX, item);
                else items.put(POCKET_STATUS_GAME_STATUS_INDEX, item);
                item = Item.get(Item.GHAST_TEAR,0,1);
                item.setCustomName(Main.getMessage(player,"item.back"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(STATUS_BACK_INDEX, item);
                else items.put(POCKET_STATUS_BACK_INDEX, item);
            break;
            case SCREEN_SETTING:
                item = Item.get(Item.GLOWSTONE_DUST,0,1);
                item.setCustomName(Main.getMessage(player,"item.setting.hideplayer"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(SETTING_HIDEPLAYER_INDEX, item);
                else items.put(POCKET_SETTING_HIDEPLAYER_INDEX, item);
                item = Item.get(Item.DYE,0,1);
                if(Setting.getPlayerHide(player)){
                    item.setDamage(10);
                    item.setCustomName(Main.getMessage(player,"item.setting.on"));
                }else{
                    item.setDamage(8);
                    item.setCustomName(Main.getMessage(player,"item.setting.off"));
                }
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(SETTING_HIDEPLAYER_BUTTON_INDEX, item);
                else items.put(POCKET_SETTING_HIDEPLAYER_BUTTON_INDEX, item);
                item = Item.get(Item.REDSTONE_DUST,0,1);
                item.setCustomName(Main.getMessage(player,"item.setting.hidechat"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(SETTING_HIDECHAT_INDEX, item);
                else items.put(POCKET_SETTING_HIDECHAT_INDEX, item);
                item = Item.get(Item.DYE,0,1);
                if(Setting.getChatHide(player)){
                    item.setDamage(10);
                    item.setCustomName(Main.getMessage(player,"item.setting.on"));
                }else{
                    item.setDamage(8);
                    item.setCustomName(Main.getMessage(player,"item.setting.off"));
                }
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(SETTING_HIDECHAT_BUTTON_INDEX, item);
                else items.put(POCKET_SETTING_HIDECHAT_BUTTON_INDEX, item);
                item = Item.get(Item.GHAST_TEAR,0,1);
                item.setCustomName(Main.getMessage(player,"item.back"));
                if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC) items.put(SETTING_BACK_INDEX, item);
                else items.put(POCKET_SETTING_BACK_INDEX, item);
            break;
        }
        doubleinv.setContents(items);
        sendContents();
        Inventorys.data2.get(player).register(player);
    }

    @Override
    public void Function(int slot,Item item){
        if(this.screen >= 100){
            this.profile.Function(player, slot, item);
            return;
        }
        if(Inventorys.getGUI(player) == Inventorys.GUI_POCKET && this.doubletouch != slot){
            update();
            this.doubletouch = slot;
            return;
        }
        if(Inventorys.getGUI(player) == Inventorys.GUI_CLASSIC){
            if(item.getId() != Item.AIR){
                switch(this.screen){
                    case SCREEN_TOP:
                        switch(slot){
                            case ACHIEVEMENTS_INDEX:
                                this.screen = SCREEN_ACHIEVEMENTS;
                            break;
                            case LANGUAGE_INDEX:
                                this.screen = SCREEN_LANGUAGE;
                            break;
                            case CLAN_INDEX:
                                this.screen = SCREEN_CLAN;
                            break;
                            case SETTING_INDEX:
                                this.screen = SCREEN_SETTING;
                            break;
                            case STATUS_INDEX:
                                this.screen = SCREEN_SECOND_TOP;
                            break;
                        }
                    break;
                    case SCREEN_ACHIEVEMENTS:
                        switch(slot){
                            case ACHIEVEMENTS_BACK_INDEX:
                                if(this.page == 1){
                                    this.screen = SCREEN_TOP;
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
                    case SCREEN_LANGUAGE:
                        switch(slot){
                            case LANGUAGE_JAPANESE_INDEX:
                                Language.setLang(player,"jp");
                                player.sendMessage(Main.getMessage(player,"language.set"));
                            break;
                            case LANGUAGE_ENGLISH_US_INDEX:
                                Language.setLang(player,"en");
                                player.sendMessage(Main.getMessage(player,"language.set"));
                            break;
                            case LANGUAGE_CHINESE_INDEX:
                                Language.setLang(player,"ch");
                                player.sendMessage(Main.getMessage(player,"language.set"));
                            break;
                            case LANGUAGE_PORTUGAL_INDEX:
                                Language.setLang(player,"po");
                                player.sendMessage(Main.getMessage(player,"language.set"));
                            break;
                            case LANGUAGE_BACK_INDEX:
                                this.screen = 0;
                            break;
                        }
                    break;
                    case SCREEN_CLAN:
                        switch(slot){
                            case CLAN_BACK_INDEX:
                                this.screen = SCREEN_TOP;
                            break;
                        }
                    break;
                    case SCREEN_SETTING:
                        switch(slot){
                            case SETTING_BACK_INDEX:
                                this.screen = SCREEN_TOP;
                            break;
                            case SETTING_HIDEPLAYER_BUTTON_INDEX:
                                Setting.setPlayerHide(player, !Setting.getPlayerHide(player));
                            break;
                            case SETTING_HIDECHAT_BUTTON_INDEX:
                                Setting.setChatHide(player, !Setting.getChatHide(player));
                            break;
                        }
                    break;
                    case SCREEN_STATUS:
                        switch(slot){
                            case STATUS_BACK_INDEX:
                                this.screen = SCREEN_TOP;
                            break;
                        }
                    break;
                }
                update();
            }
        }else{
            if(item.getId() == Item.AIR){
                switch(this.screen){
                    case SCREEN_TOP:
                        switch(slot){
                            case POCKET_ACHIEVEMENTS_INDEX:
                                this.screen = SCREEN_ACHIEVEMENTS;
                            break;
                            case POCKET_LANGUAGE_INDEX:
                                this.screen = SCREEN_LANGUAGE;
                            break;
                            case POCKET_CLAN_INDEX:
                                this.screen = SCREEN_CLAN;
                            break;
                            case POCKET_SETTING_INDEX:
                                this.screen = SCREEN_SETTING;
                            break;
                            case POCKET_STATUS_INDEX:
                                this.screen = SCREEN_SECOND_TOP;
                            break;
                        }
                    break;
                    case SCREEN_ACHIEVEMENTS:
                        switch(slot){
                            case POCKET_ACHIEVEMENTS_BACK_INDEX:
                                if(this.page == 1){
                                    this.screen = SCREEN_TOP;
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
                    case SCREEN_LANGUAGE:
                        switch(slot){
                            case POCKET_LANGUAGE_JAPANESE_INDEX:
                                Language.setLang(player,"jp");
                                player.sendMessage(Main.getMessage(player,"language.set"));
                            break;
                            case POCKET_LANGUAGE_ENGLISH_US_INDEX:
                                Language.setLang(player,"en");
                                player.sendMessage(Main.getMessage(player,"language.set"));
                            break;
                            case POCKET_LANGUAGE_CHINESE_INDEX:
                                Language.setLang(player,"ch");
                                player.sendMessage(Main.getMessage(player,"language.set"));
                            break;
                            case POCKET_LANGUAGE_PORTUGAL_INDEX:
                                Language.setLang(player,"po");
                                player.sendMessage(Main.getMessage(player,"language.set"));
                            break;
                            case POCKET_LANGUAGE_BACK_INDEX:
                                this.screen = 0;
                            break;
                        }
                    break;
                    case SCREEN_CLAN:
                        switch(slot){
                            case POCKET_CLAN_BACK_INDEX:
                                this.screen = SCREEN_TOP;
                            break;
                        }
                    break;
                    case SCREEN_SETTING:
                        switch(slot){
                            case POCKET_SETTING_BACK_INDEX:
                                this.screen = SCREEN_TOP;
                            break;
                            case POCKET_SETTING_HIDEPLAYER_BUTTON_INDEX:
                                Setting.setPlayerHide(player, !Setting.getPlayerHide(player));
                            break;
                            case POCKET_SETTING_HIDECHAT_BUTTON_INDEX:
                                Setting.setChatHide(player, !Setting.getChatHide(player));
                            break;
                        }
                    break;
                    case SCREEN_STATUS:
                        switch(slot){
                            case POCKET_STATUS_BACK_INDEX:
                                this.screen = SCREEN_TOP;
                            break;
                        }
                    break;
                }
                update();
            }
        }
    }

    @Override
    public void close(){
        super.close();
        Inventorys.data2.get(player).register(player);
    }

    public void setProfile(Profile profile){
        this.profile = profile;
    }
}
class CallbackOpenProfileChestInventory extends Task{

    public ProfileChestInventory owner;

    public CallbackOpenProfileChestInventory(ProfileChestInventory o){
        owner = o;
    }

    public void onRun(int d){
        owner.open();
    }
}