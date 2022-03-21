package br.com.blecaute.inventory.updater;

import org.bukkit.inventory.ItemStack;

public interface InventoryUpdater {

    void update(int slot, ItemStack itemStack);

    void flush();

}
