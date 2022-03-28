package br.com.blecaute.inventory.event;

import org.bukkit.inventory.ItemStack;

public interface Updatable {

    void update(int slot, ItemStack itemStack);

}
