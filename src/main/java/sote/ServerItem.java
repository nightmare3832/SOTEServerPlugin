package sote;

import java.util.HashMap;

import cn.nukkit.item.Item;

public class ServerItem{

    public ServerItem(){
    }

    public static void register(){
        serverItemList.put("Command_Block", Item.get(Item.STONE, 7));
        serverItemList.put("Japan_Block", Item.get(Item.CRAFTING_TABLE, 2));
        serverItemList.put("America_Block", Item.get(Item.CRAFTING_TABLE, 3));
        serverItemList.put("China_Block", Item.get(Item.CRAFTING_TABLE, 4));
        serverItemList.put("Korea_Block", Item.get(Item.CRAFTING_TABLE, 5));
        serverItemList.put("Portugal_Block", Item.get(Item.CRAFTING_TABLE, 6));
    }

    public static Item getServerItemByString(String name){
        if(!serverItemList.containsKey(name)) return Item.get(0);
        return serverItemList.get(name).clone();
    }

    public static Item getServerItemByLanguage(String lang){
        switch(lang){
            case "jp":
                return getServerItemByString("Japan_Block");
            case "en":
                return getServerItemByString("America_Block");
            case "ch":
                return getServerItemByString("China_Block");
            case "ko":
                return getServerItemByString("Korea_Block");
            case "po":
                return getServerItemByString("Portugal_Block");
        }
        return getServerItemByString("America_Block");
    }

    public static HashMap<String, Item> serverItemList = new HashMap<String, Item>();
}