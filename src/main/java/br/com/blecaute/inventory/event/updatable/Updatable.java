package br.com.blecaute.inventory.event.updatable;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * The class that implements the Updatable indicates that it
 * has methods to update the item in the inventory.
 */
public interface Updatable {

    /**
     * Update @{@link ItemStack} in inventory.
     *
     * @param slot the slot
     * @param itemStack the @{@link ItemStack}
     */
    void update(int slot, @NotNull ItemStack itemStack);

}