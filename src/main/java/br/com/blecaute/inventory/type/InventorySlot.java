package br.com.blecaute.inventory.type;

import br.com.blecaute.inventory.InventoryBuilder;

import org.bukkit.inventory.ItemStack;

/**
 * Any class that implements this can be used in the @{@link InventoryBuilder}
 * to represent the object as an @{@link ItemStack} and its slot.
 */
public interface InventorySlot extends InventoryItem {

    /**
     * The slot
     *
     * @return The index of slot;
     */
    int getSlot();

}