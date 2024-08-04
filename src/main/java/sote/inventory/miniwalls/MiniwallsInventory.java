package sote.inventory.miniwalls;

import sote.inventory.Inventory;

public abstract class MiniwallsInventory extends Inventory{

    public MiniwallsInventory(){
    }

    public int ChargeSpeed = 300;
    public int ChargeMax = 3;
    public int StackMax = 2;

    public int helmetEnchantLevel = 0;
    public int chestplateEnchantLevel = 0;
    public int leggingsEnchantLevel = 0;
    public int bootsEnchantLevel = 0;

    public int healthBonus = 0;

    public boolean isPrestige = false;
}