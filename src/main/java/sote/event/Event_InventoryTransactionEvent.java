package sote.event;

import cn.nukkit.event.inventory.InventoryTransactionEvent;

public class Event_InventoryTransactionEvent{

    public Event_InventoryTransactionEvent(){
    }

    public static void onTransaction(InventoryTransactionEvent event){
        /*for(Transaction transaction : ((SimpleTransactionGroup)event.getTransaction()).getTransactions()){
            if(transaction.getInventory() == null) continue;
            if(transaction.getInventory().getHolder() instanceof Player){
                //ServerChestInventorys.Function((Player)transaction.getInventory().getHolder(),transaction.getTargetItem(),event);
                Player player = (Player)transaction.getInventory().getHolder();
                Game game = GameProvider.getPlayingGame(player);
                if(game instanceof SkywarsSolo){
                    if(transaction.getTargetItem().getId() == Item.DYE && transaction.getTargetItem().getDamage() == DyeColor.BLUE.getDyeData()){
                        event.setCancelled();
                    }
                }
            }
        }*/
    }
}